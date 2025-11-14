import os
import logging
from typing import Literal, Optional, TypeVar, Union, List
from requests.sessions import Session
import urllib3
from urllib3.exceptions import InsecureRequestWarning
import requests
import asyncio
import aiohttp
import ssl

from .base import BaseRecord, QueryResult, QueryStatement, parse_query_result
from .project import Project, ProjectProperty
from enum import Enum
import re


# Suppress the warnings from urllib3
urllib3.disable_warnings(category=InsecureRequestWarning)

# Setup logger
logger = logging.getLogger(__name__)
console_handler = logging.StreamHandler()
formatter = logging.Formatter("%(funcName)s:%(levelname)s: %(message)s")
console_handler.setFormatter(formatter)
logger.addHandler(console_handler)

R = TypeVar("R", bound=BaseRecord)

# -------------- Tracking tool integration ---------------------
try:
    user_id = os.getlogin()
except Exception:
    user_id = "unknown"

TRACKING_TOOL_URL = "https://sgpvmc0521.apac.bosch.com:8443/portal/api/tracking/save"
TRACKING_TOOL_PARAMS = {
    "toolId": "BGSV_Building_Blocks",
    "userId": user_id,
    "functionName": "Building_Block_RQ1",
}


def __send_request(url: str, params: dict[str, str]):
    logger.debug(
        "send request to tracking tool at `{}` with params=`{}`".format(url, params)
    )
    try:
        res = requests.get(url, params=params, verify=False)
        if res.status_code != 200:
            logger.warning(
                "Failed to send request to tracking tool. Error code: `{}`. Response: `{}`".format(
                    res.status_code, res.text
                )
            )
    except Exception:
        logger.warning("Unable to access tracking tool server.")


# do not track CI/CD usage
if os.environ.get("CI_ENVIRONMENT") is None:
    __send_request(TRACKING_TOOL_URL, TRACKING_TOOL_PARAMS)
# ---------------------------------------------------------


class BaseUrl:
    PRODUCTIVE = (
        "https://rb-dgsrq1-oslc-p.de.bosch.com/cqweb/oslc/repo/RQ1_PRODUCTIVE/db/RQONE/"
    )
    APPROVAL = (
        "https://rb-dgsrq1-approval.de.bosch.com/cqweb/oslc/repo/RQ1_APPROVAL/db/RQONE/"
    )
    ACCEPTANCE = (
        "https://rb-dgsrq1-oslc-q.de.bosch.com/cqweb/oslc/repo/RQ1_ACCEPTANCE/db/RQONE/"
    )


class Client(object):
    """An RQ1 client"""

    def __init__(
        self,
        base_url: str,
        username: str,
        password: str,
        toolname: str,
        toolversion: str,
    ):
        self.base_url = base_url if base_url.endswith("/") else base_url + "/"
        self.session = Session()
        self.session.verify = False
        self.session.auth = (username, password)
        self.session.headers.update(
            {
                "OSLC-Core-Version": "2.0",
                "x-requester": f"toolname={toolname};toolversion={toolversion};user={username}",
                "Accept": "application/json",
            }
        )
    
    def run_query(
        self,
        rtype: type[R],
        query_id: Union[str, int],
        select: Union[List[Union[Enum, str, QueryStatement]],
        Literal["*"]] = [], page_size: Optional[int] = None,
    ) -> QueryResult[R]:
        """Run predefined query.

        Args:
            rtype (type[R]): record type.
            query_id (Union[str, int]): the predefined query id.
            select (Union[List[Union[Enum, str]], Literal[, optional): select which properties to be populated, "*" to select all. Defaults to [].
            page_size (Optional[int], optional): configure how many results per page. Defaults to None.

        Returns:
            QueryResult[R]: the first result page of the query.
        """
        url = self.base_url + f"query/{query_id}"
        res = self.session.get(
            url,
            params={
                "oslc.select": "*"
                if select == "*"
                else ",".join([str(s) for s in select]),
                "oslc.pageSize": page_size,
            },
        )
        if res.status_code >= 400:
            logger.error(res.text)
        res.raise_for_status()
        return parse_query_result(rtype, res.json())

    def query(
        self,
        rtype: type[R],
        where: Optional[Union[str, QueryStatement]] = None,
        select: Union[List[Union[Enum, str, QueryStatement]], Literal["*"]] = [],
        paging: Optional[bool] = None,
        page_size: Optional[int] = None,
        search_terms: List[str] = [],
    ) -> QueryResult[R]:
        """Query records.

        Args:
            rtype (type[R]): record type.
            where (Optional[Union[QueryStatement, str]], optional): the query criterias, if this is None, it matches all records on the system. Defaults to None.
            select (Union[List[Union[Enum, str]], Literal[, optional): select which properties to be populated, "*" to select all. Defaults to [].
            paging (Optional[bool], optional): enable pagination if ``True``. Defaults to None.
            page_size (Optional[int], optional): configure how many results per page. Defaults to None.
            search_terms (List[str]): search terms for full-text search on text values of records. Some records do not support query by search terms. Default to [].

        Returns:
            QueryResult[R]: the first result page of the query.
        """
        url = self.base_url + f"simpleQuery/{rtype.shape_id()}"

        # preprocess search terms
        if len(search_terms) == 0:
            final_search_terms = None
        else:
            final_search_terms = ",".join([f'"{s}"' for s in search_terms])

        res = self.session.get(
            url,
            params={
                "oslc.select": "*"
                if select == "*"
                else ",".join([str(s) for s in select]),
                "oslc.where": str(where) if where is not None else None,
                "oslc.paging": paging,
                "oslc.pageSize": page_size,
                "oslc.searchTerms": final_search_terms,
            },
        )
        if res.status_code >= 400:
            logger.error(res.text)
        res.raise_for_status()
        return parse_query_result(rtype, res.json())

    def get_next_query_page(self, query: QueryResult[R]) -> Optional[QueryResult[R]]:
        """Get next query page.

        Args:
            query (QueryResult[R]): a query page.

        Returns:
            Optional[QueryResult[R]]: next query page.
        """
        if query.next_page is None:
            return None
        res = self.session.get(query.next_page)
        if res.status_code >= 400:
            logger.error(res.text)
        res.raise_for_status()
        return parse_query_result(query.member_type, res.json())

    def get_query_page(
        self, query: QueryResult[R], start_index: int
    ) -> Optional[QueryResult[R]]:
        """Get an arbitrary query page.

        Args:
            query (QueryResult[R]): a query page.
            start_index (int): start index of the query result. Please note that this is not the page index.

        Returns:
            Optional[QueryResult[R]]: query page that contains result from index ``start_index`` to ``start_index + page_size``
        """
        if query.next_page is None:
            return None
        page_url = re.sub(
            r"rcm\.startIndex=\d+", f"rcm.startIndex={start_index}", query.next_page
        )
        res = self.session.get(page_url)
        if res.status_code >= 400:
            logger.error(res.text)
        res.raise_for_status()
        return parse_query_result(query.member_type, res.json())

    def get_record_by_uri(self, rtype: type[R], uri: str) -> R:
        """Get a record by its uri.

        Args:
            rtype (type[R]): record type.
            uri (str): record uri.

        Returns:
            R: the record object.
        """
        res = self.session.get(uri)
        if res.status_code >= 400:
            logger.error(res.text)
        res.raise_for_status()
        record = rtype(**res.json())
        if record.dcterms__type != rtype.shape_name():
            logger.warning(
                f"The record {uri} is a {record.dcterms__type}, but you are trying to deserialize it into a {rtype.shape_name()}."
            )
        return record

    def get_record_by_rq1_number(
        self,
        rtype: type[R],
        rq1_num: str,
        select: Optional[Union[List[Union[Enum, str, QueryStatement]], Literal["*"]]] = None,
    ) -> R:
        """Get a record by RQ1 number.

        Args:
            rtype (type[R]): the record type.
            rq1_num (str): the record RQ1 number.
            select (Optional[Union[List[Union[Enum, str]], Literal["*"]]]): Select which properties to be populated. Default to '*' for
                other record types aside from Project. For project, to reduce request time, only some common properties are populated if
                ``select`` is not set.

        Raises:
            ValueError: record not found.

        Returns:
            R: the record. In case of Project, only some properties are returned to reduce request time.
        """
        if rtype == Project:
            # In case of Project, we can't use select="*". Maybe some bug in the RQ1 system.
            if select is None:
                select = [
                    ProjectProperty.dcterms__title,
                    ProjectProperty.dcterms__description,
                    ProjectProperty.accountnumbers,
                    ProjectProperty.creator,
                    ProjectProperty.domain,
                    ProjectProperty.status,
                    ProjectProperty.state,
                    ProjectProperty.customer,
                    ProjectProperty.dbid,
                    ProjectProperty.belongstopoolproject,
                    ProjectProperty.belongstoreferenceproject,
                    ProjectProperty.bulkoperations,
                    ProjectProperty.classification,
                    ProjectProperty.externaldescription,
                    ProjectProperty.subject,
                    ProjectProperty.tags,
                    ProjectProperty.shorttitle,
                    ProjectProperty.lifecyclestate,
                    ProjectProperty.lifecyclestatecomment,
                ]
            query = self.query(
                rtype,
                where=f'cq:id="{rq1_num}"',
                select=select,
            )
        else:
            query = self.query(
                rtype,
                where=f'cq:id="{rq1_num}"',
                select="*" if select is None else select,
            )

        if query.total_count == 0:
            raise ValueError(f"{rtype.__name__} {rq1_num} not found.")
        return query.members[0]

    def create_record(self, changeset: BaseRecord) -> str:
        """Create a new record.

        Args:
            changeset (BaseRecord): a changeset contains initial values of the record.

        Returns:
            str: uri of the newly created record.
        """
        url = self.base_url + "record"
        changeset.operationmode = "building-block-rq1"
        changeset.operationcontext = "POST"
        res = self.session.post(
            url,
            headers={"Content-Type": "application/json"},
            data=changeset.model_dump_json(exclude_unset=True, by_alias=True),
        )
        if res.status_code >= 400:
            logger.error(res.text)
        res.raise_for_status()
        return res.headers["Location"]

    def modify_record(self, uri: str, changeset: BaseRecord):
        """Modify a record.

        Args:
            uri (str): record uri.
            changeset (BaseRecord): a changset contains changed values of the record.
        """
        changeset.operationmode = "building-block-rq1"
        changeset.operationcontext = "PUT"
        changeset_dict = changeset.model_dump(by_alias=True, exclude_unset=True)
        changed_properties = ",".join(changeset_dict.keys())
        res = self.session.put(
            uri,
            headers={"Content-Type": "application/json"},
            params={"rcm.action": "modify", "oslc.properties": changed_properties},
            data=changeset.model_dump_json(exclude_unset=True, by_alias=True),
        )
        if res.status_code >= 400:
            logger.error(res.text)
        res.raise_for_status()

    def get_attachement_content(
        self, rq1_uri: str, attachment_fielddef_id: str, attachment_id: str
    ) -> bytes:
        """Get content of an attachment.

        Args:
            rq1_uri (str): the issue/workitem/problem... where the attachment is attached to.
            attachment_fielddef_id (str): the attachment field definition id.
            attachment_id (str): the attachment id.

        Returns:
            bytes: content of the attachment.
        """
        if rq1_uri.endswith("/"):
            rq1_uri = rq1_uri[:-1]
        url = f"{rq1_uri}/field/Attachments/attachment/{attachment_fielddef_id}-{attachment_id}"
        res = self.session.get(url)
        if res.status_code >= 400:
            logger.error(res.text)
        res.raise_for_status()
        return res.content

    def upload_attachment(
        self, rq1_uri: str, file_path: str, description: Optional[str] = None
    ):
        """Upload attachment to a record

        Args:
            rq1_uri (str): the uri of issue/workitem/problem... to attach the attachment.
            file_path (str): the path of attachment file.
            description (Optional[str]): the attachment description.
        """
        if rq1_uri.endswith("/"):
            rq1_uri = rq1_uri[:-1]
        url = f"{rq1_uri}/field/Attachments/attachment"
        res = self.session.post(
            url,
            params={"rcm.action": "modify"},
            data={"description": description},
            files={"file": open(file_path, "rb")},
        )
        # Don't know why the server always returns 501 - Internal Server Error but still uploads the file successfully anyway
        if res.status_code >= 400 and res.status_code != 501:
            logger.error(res.text)
            res.raise_for_status()


class AsyncClient:
    """An asynchronous RQ1 client"""

    def __init__(
        self,
        base_url: str,
        username: str,
        password: str,
        toolname: str,
        toolversion: str,
        connector: Optional[aiohttp.TCPConnector] = None,
    ):
        self.base_url = base_url if base_url.endswith("/") else base_url + "/"
        self.username = username
        self.password = password
        self.basic_auth = aiohttp.BasicAuth(self.username, self.password)
        self.headers = {
            "OSLC-Core-Version": "2.0",
            "x-requester": f"toolname={toolname};toolversion={toolversion};user={username}",
            "Accept": "application/json",
        }
        
        # Create SSL context that doesn't verify certificates (similar to verify=False in requests)
        ssl_context = ssl.create_default_context()
        ssl_context.check_hostname = False
        ssl_context.verify_mode = ssl.CERT_NONE
        
        # Use provided connector or create default one with SSL disabled
        self.connector = connector or aiohttp.TCPConnector(ssl=ssl_context)
        self._session: Optional[aiohttp.ClientSession] = None

    async def __aenter__(self):
        self._session = aiohttp.ClientSession(
            connector=self.connector,
            auth=self.basic_auth,
            headers=self.headers,
        )
        return self

    async def __aexit__(self, exc_type, exc_val, exc_tb):
        if self._session:
            await self._session.close()

    @property
    def session(self) -> aiohttp.ClientSession:
        if self._session is None:
            raise RuntimeError("AsyncClient must be used as an async context manager")
        return self._session

    async def run_query(
        self,
        rtype: type[R],
        query_id: Union[str, int],
        select: Union[List[Union[Enum, str, QueryStatement]], Literal["*"]] = [],
        page_size: Optional[int] = None,
    ) -> QueryResult[R]:
        """Run predefined query asynchronously.

        Args:
            rtype (type[R]): record type.
            query_id (Union[str, int]): the predefined query id.
            select (Union[List[Union[Enum, str]], Literal[, optional): select which properties to be populated, "*" to select all. Defaults to [].
            page_size (Optional[int], optional): configure how many results per page. Defaults to None.

        Returns:
            QueryResult[R]: the first result page of the query.
        """
        url = self.base_url + f"query/{query_id}"
        params = {
            "oslc.select": "*"
            if select == "*"
            else ",".join([str(s) for s in select]),
        }
        if page_size is not None:
            params["oslc.pageSize"] = page_size

        async with self.session.get(url, params=params) as response:
            if response.status >= 400:
                text = await response.text()
                logger.error(text)
            response.raise_for_status()
            json_data = await response.json()
            return parse_query_result(rtype, json_data)

    async def query(
        self,
        rtype: type[R],
        where: Optional[Union[str, QueryStatement]] = None,
        select: Union[List[Union[Enum, str, QueryStatement]], Literal["*"]] = [],
        paging: Optional[bool] = None,
        page_size: Optional[int] = None,
        search_terms: List[str] = [],
    ) -> QueryResult[R]:
        """Query records asynchronously.

        Args:
            rtype (type[R]): record type.
            where (Optional[Union[QueryStatement, str]], optional): the query criterias, if this is None, it matches all records on the system. Defaults to None.
            select (Union[List[Union[Enum, str]], Literal[, optional): select which properties to be populated, "*" to select all. Defaults to [].
            paging (Optional[bool], optional): enable pagination if ``True``. Defaults to None.
            page_size (Optional[int], optional): configure how many results per page. Defaults to None.
            search_terms (List[str]): search terms for full-text search on text values of records. Some records do not support query by search terms. Default to [].

        Returns:
            QueryResult[R]: the first result page of the query.
        """
        url = self.base_url + f"simpleQuery/{rtype.shape_id()}"

        # preprocess search terms
        if len(search_terms) == 0:
            final_search_terms = None
        else:
            final_search_terms = ",".join([f'"{s}"' for s in search_terms])

        params = {
            "oslc.select": "*"
            if select == "*"
            else ",".join([str(s) for s in select]),
        }
        if where is not None:
            params["oslc.where"] = str(where)
        if paging is not None:
            params["oslc.paging"] = paging
        if page_size is not None:
            params["oslc.pageSize"] = page_size
        if final_search_terms is not None:
            params["oslc.searchTerms"] = final_search_terms

        async with self.session.get(url, params=params) as response:
            if response.status >= 400:
                text = await response.text()
                logger.error(text)
            response.raise_for_status()
            json_data = await response.json()
            return parse_query_result(rtype, json_data)

    async def get_next_query_page(self, query: QueryResult[R]) -> Optional[QueryResult[R]]:
        """Get next query page asynchronously.

        Args:
            query (QueryResult[R]): a query page.

        Returns:
            Optional[QueryResult[R]]: next query page.
        """
        if query.next_page is None:
            return None
        
        async with self.session.get(query.next_page) as response:
            if response.status >= 400:
                text = await response.text()
                logger.error(text)
            response.raise_for_status()
            json_data = await response.json()
            return parse_query_result(query.member_type, json_data)

    async def get_query_page(
        self, query: QueryResult[R], start_index: int
    ) -> Optional[QueryResult[R]]:
        """Get an arbitrary query page asynchronously.

        Args:
            query (QueryResult[R]): a query page.
            start_index (int): start index of the query result. Please note that this is not the page index.

        Returns:
            Optional[QueryResult[R]]: query page that contains result from index ``start_index`` to ``start_index + page_size``
        """
        if query.next_page is None:
            return None
        page_url = re.sub(
            r"rcm\.startIndex=\d+", f"rcm.startIndex={start_index}", query.next_page
        )
        
        async with self.session.get(page_url) as response:
            if response.status >= 400:
                text = await response.text()
                logger.error(text)
            response.raise_for_status()
            json_data = await response.json()
            return parse_query_result(query.member_type, json_data)

    async def get_record_by_uri(self, rtype: type[R], uri: str) -> R:
        """Get a record by its uri asynchronously.

        Args:
            rtype (type[R]): record type.
            uri (str): record uri.

        Returns:
            R: the record object.
        """
        async with self.session.get(uri) as response:
            if response.status >= 400:
                text = await response.text()
                logger.error(text)
            response.raise_for_status()
            json_data = await response.json()
            record = rtype(**json_data)
            if record.dcterms__type != rtype.shape_name():
                logger.warning(
                    f"The record {uri} is a {record.dcterms__type}, but you are trying to deserialize it into a {rtype.shape_name()}."
                )
            return record

    async def get_record_by_rq1_number(
        self,
        rtype: type[R],
        rq1_num: str,
        select: Optional[Union[List[Union[Enum, str, QueryStatement]], Literal["*"]]] = None,
    ) -> R:
        """Get a record by RQ1 number asynchronously.

        Args:
            rtype (type[R]): the record type.
            rq1_num (str): the record RQ1 number.
            select (Optional[Union[List[Union[Enum, str]], Literal["*"]]]): Select which properties to be populated. Default to '*' for
                other record types aside from Project. For project, to reduce request time, only some common properties are populated if
                ``select`` is not set.

        Raises:
            ValueError: record not found.

        Returns:
            R: the record. In case of Project, only some properties are returned to reduce request time.
        """
        if rtype == Project:
            # In case of Project, we can't use select="*". Maybe some bug in the RQ1 system.
            if select is None:
                select = [
                    ProjectProperty.dcterms__title,
                    ProjectProperty.dcterms__description,
                    ProjectProperty.accountnumbers,
                    ProjectProperty.creator,
                    ProjectProperty.domain,
                    ProjectProperty.status,
                    ProjectProperty.state,
                    ProjectProperty.customer,
                    ProjectProperty.dbid,
                    ProjectProperty.belongstopoolproject,
                    ProjectProperty.belongstoreferenceproject,
                    ProjectProperty.bulkoperations,
                    ProjectProperty.classification,
                    ProjectProperty.externaldescription,
                    ProjectProperty.subject,
                    ProjectProperty.tags,
                    ProjectProperty.shorttitle,
                    ProjectProperty.lifecyclestate,
                    ProjectProperty.lifecyclestatecomment,
                ]
            query = await self.query(
                rtype,
                where=f'cq:id="{rq1_num}"',
                select=select,
            )
        else:
            query = await self.query(
                rtype,
                where=f'cq:id="{rq1_num}"',
                select="*" if select is None else select,
            )

        if query.total_count == 0:
            raise ValueError(f"{rtype.__name__} {rq1_num} not found.")
        return query.members[0]

    async def create_record(self, changeset: BaseRecord) -> str:
        """Create a new record asynchronously.

        Args:
            changeset (BaseRecord): a changeset contains initial values of the record.

        Returns:
            str: uri of the newly created record.
        """
        url = self.base_url + "record"
        changeset.operationmode = "building-block-rq1"
        changeset.operationcontext = "POST"
        
        data = changeset.model_dump_json(exclude_unset=True, by_alias=True)
        headers = {"Content-Type": "application/json"}
        
        async with self.session.post(url, headers=headers, data=data) as response:
            if response.status >= 400:
                text = await response.text()
                logger.error(text)
            response.raise_for_status()
            return response.headers["Location"]

    async def modify_record(self, uri: str, changeset: BaseRecord):
        """Modify a record asynchronously.

        Args:
            uri (str): record uri.
            changeset (BaseRecord): a changset contains changed values of the record.
        """
        changeset.operationmode = "building-block-rq1"
        changeset.operationcontext = "PUT"
        changeset_dict = changeset.model_dump(by_alias=True, exclude_unset=True)
        changed_properties = ",".join(changeset_dict.keys())
        
        headers = {"Content-Type": "application/json"}
        params = {"rcm.action": "modify", "oslc.properties": changed_properties}
        data = changeset.model_dump_json(exclude_unset=True, by_alias=True)
        
        async with self.session.put(uri, headers=headers, params=params, data=data) as response:
            if response.status >= 400:
                text = await response.text()
                logger.error(text)
            response.raise_for_status()

    async def get_attachement_content(
        self, rq1_uri: str, attachment_fielddef_id: str, attachment_id: str
    ) -> bytes:
        """Get content of an attachment asynchronously.

        Args:
            rq1_uri (str): the issue/workitem/problem... where the attachment is attached to.
            attachment_fielddef_id (str): the attachment field definition id.
            attachment_id (str): the attachment id.

        Returns:
            bytes: content of the attachment.
        """
        if rq1_uri.endswith("/"):
            rq1_uri = rq1_uri[:-1]
        url = f"{rq1_uri}/field/Attachments/attachment/{attachment_fielddef_id}-{attachment_id}"
        
        async with self.session.get(url) as response:
            if response.status >= 400:
                text = await response.text()
                logger.error(text)
            response.raise_for_status()
            return await response.read()

    async def upload_attachment(
        self, rq1_uri: str, file_path: str, description: Optional[str] = None
    ):
        """Upload attachment to a record asynchronously

        Args:
            rq1_uri (str): the uri of issue/workitem/problem... to attach the attachment.
            file_path (str): the path of attachment file.
            description (Optional[str]): the attachment description.
        """
        if rq1_uri.endswith("/"):
            rq1_uri = rq1_uri[:-1]
        url = f"{rq1_uri}/field/Attachments/attachment"
        
        # Prepare multipart data
        data = aiohttp.FormData()
        if description:
            data.add_field('description', description)
        
        # Add file
        with open(file_path, 'rb') as f:
            data.add_field('file', f, filename=os.path.basename(file_path))
            
            params = {"rcm.action": "modify"}
            
            async with self.session.post(url, params=params, data=data) as response:
                # Don't know why the server always returns 501 - Internal Server Error but still uploads the file successfully anyway
                if response.status >= 400 and response.status != 501:
                    text = await response.text()
                    logger.error(text)
                    response.raise_for_status()
