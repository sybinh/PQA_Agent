
from .base import Resource, QueryStatement, BaseRecord
from pydantic import ConfigDict, Field, field_validator
import typing
from datetime import datetime, timezone
from enum import Enum
import json


from typing import TYPE_CHECKING

if TYPE_CHECKING:

    from .historylog import Historylog

    from .users import Users

    from .issue import Issue

    from .background import Background

    from .ratl_replicas import Ratl_Replicas



class Issueissuemap(BaseRecord):
    
    model_config = ConfigDict(populate_by_name=True, extra="allow")
    
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:Assignee", default=None)
    
    assigneefilter: typing.Optional[str] = Field(alias="cq:AssigneeFilter", default=None)
    
    assigneesearch: typing.Optional[str] = Field(alias="cq:AssigneeSearch", default=None)
    
    creationmode: typing.Optional[str] = Field(alias="cq:CreationMode", default=None)
    
    creator: typing.Optional[Resource] = Field(alias="dcterms:creator", default=None)
    
    dbid: typing.Optional[str] = Field(alias="cq:dbid", default=None)
    
    dcterms__type: typing.Optional[str] = Field(alias="dcterms:type", default=None)
    
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = Field(alias="cq:hasBackground", default=None)
    
    hashistorylogs: typing.List[typing.Union[Resource, 'Historylog']] = Field(alias="cq:hasHistoryLogs", default_factory=lambda: [])
    
    hasmappedissue1: typing.Optional[typing.Union[Resource, 'Issue']] = Field(alias="cq:hasMappedIssue1", default=None)
    
    hasmappedissue1filter: typing.Optional[str] = Field(alias="cq:hasMappedIssue1Filter", default=None)
    
    hasmappedissue1search: typing.Optional[str] = Field(alias="cq:hasMappedIssue1Search", default=None)
    
    hasmappedissue2: typing.Optional[typing.Union[Resource, 'Issue']] = Field(alias="cq:hasMappedIssue2", default=None)
    
    hasmappedissue2filter: typing.Optional[str] = Field(alias="cq:hasMappedIssue2Filter", default=None)
    
    hasmappedissue2search: typing.Optional[str] = Field(alias="cq:hasMappedIssue2Search", default=None)
    
    history: typing.Optional[Resource] = Field(alias="cq:history", default=None)
    
    id: typing.Optional[str] = Field(alias="cq:id", default=None)
    
    identifier: typing.Optional[str] = Field(alias="dcterms:identifier", default=None)
    
    instanceshape: typing.Optional[Resource] = Field(alias="oslc:instanceShape", default=None)
    
    internalcomment: typing.Optional[str] = Field(alias="cq:InternalComment", default=None)
    
    is_active: typing.Optional[int] = Field(alias="cq:is_active", default=None)
    
    lastmodifieddate: typing.Optional[datetime] = Field(alias="cq:LastModifiedDate", default=None)
    
    lifecyclestate: typing.Optional[str] = Field(alias="cq:LifeCycleState", default=None)
    
    lifecyclestatecomment: typing.Optional[str] = Field(alias="cq:LifeCycleStateComment", default=None)
    
    lock_version: typing.Optional[int] = Field(alias="cq:lock_version", default=None)
    
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = Field(alias="cq:locked_by", default=None)
    
    operationcontext: typing.Optional[str] = Field(alias="cq:OperationContext", default=None)
    
    operationmode: typing.Optional[str] = Field(alias="cq:OperationMode", default=None)
    
    ratl_forminfo: typing.Optional[str] = Field(alias="cq:ratl_forminfo", default=None)
    
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_keysite", default=None)
    
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = Field(alias="cq:ratl_mastership", default=None)
    
    rdf__type: typing.List[Resource] = Field(alias="rdf:type", default_factory=lambda: [])
    
    shorttitle: typing.Optional[str] = Field(alias="oslc:shortTitle", default=None)
    
    subject: typing.List[str] = Field(alias="dcterms:subject", default_factory=lambda: [])
    
    submitdate: typing.Optional[datetime] = Field(alias="cq:SubmitDate", default=None)
    
    submitter: typing.Optional[str] = Field(alias="cq:Submitter", default=None)
    
    tags: typing.Optional[str] = Field(alias="cq:Tags", default=None)
    
    title: typing.Optional[str] = Field(alias="dcterms:title", default=None)
    
    version: typing.Optional[int] = Field(alias="cq:version", default=None)
    

    @classmethod
    def shape_id(cls) -> str:
        return "16785427"

    @classmethod
    def shape_name(cls) -> str:
        return "Issueissuemap"

    @field_validator('*', mode="before")
    @classmethod
    def empty_str_to_none(cls, v):
        if v == '':
            return None
        return v

    
    @field_validator('instanceshape', 'history', mode="before")
    @classmethod
    def list_to_resource(cls, v):
        if isinstance(v, list):
            return v[0]
        return v
    


class IssueissuemapProperty(Enum):
    assignee = "cq:Assignee"
    assigneefilter = "cq:AssigneeFilter"
    assigneesearch = "cq:AssigneeSearch"
    creationmode = "cq:CreationMode"
    creator = "dcterms:creator"
    dbid = "cq:dbid"
    dcterms__type = "dcterms:type"
    hasbackground = "cq:hasBackground"
    hashistorylogs = "cq:hasHistoryLogs"
    hasmappedissue1 = "cq:hasMappedIssue1"
    hasmappedissue1filter = "cq:hasMappedIssue1Filter"
    hasmappedissue1search = "cq:hasMappedIssue1Search"
    hasmappedissue2 = "cq:hasMappedIssue2"
    hasmappedissue2filter = "cq:hasMappedIssue2Filter"
    hasmappedissue2search = "cq:hasMappedIssue2Search"
    history = "cq:history"
    id = "cq:id"
    identifier = "dcterms:identifier"
    instanceshape = "oslc:instanceShape"
    internalcomment = "cq:InternalComment"
    is_active = "cq:is_active"
    lastmodifieddate = "cq:LastModifiedDate"
    lifecyclestate = "cq:LifeCycleState"
    lifecyclestatecomment = "cq:LifeCycleStateComment"
    lock_version = "cq:lock_version"
    locked_by = "cq:locked_by"
    operationcontext = "cq:OperationContext"
    operationmode = "cq:OperationMode"
    ratl_forminfo = "cq:ratl_forminfo"
    ratl_keysite = "cq:ratl_keysite"
    ratl_mastership = "cq:ratl_mastership"
    rdf__type = "rdf:type"
    shorttitle = "oslc:shortTitle"
    subject = "dcterms:subject"
    submitdate = "cq:SubmitDate"
    submitter = "cq:Submitter"
    tags = "cq:Tags"
    title = "dcterms:title"
    version = "cq:version"
    

    def reformat(self, value) -> str:
            if isinstance(value, Resource):
                return f"<{value.uri}>"
            if isinstance(value, datetime):
                dt = value.astimezone(timezone.utc)
                return f'"{dt.isoformat().replace("+00:00", "Z")}"'
            return json.dumps(value)

    def is_one_of(self, values: typing.List[typing.Any]) -> QueryStatement:
        return QueryStatement("{} in [{}]".format(self.value,",".join(map(lambda x: self.reformat(x), values))))

    def nested(self, value: typing.Union[QueryStatement, list[typing.Union[str, Enum]]]) -> QueryStatement:
        if isinstance(value, QueryStatement):
            return QueryStatement(self.value + "{" + str(value) + "}")
        else:
            return QueryStatement(self.value + "{" + ",".join([str(x) for x in value]) + "}")

    def __eq__(self, other):
            return QueryStatement(f"{self.value}={self.reformat(other)}")

    def __ne__(self, other):
        return QueryStatement(f"{self.value}!={self.reformat(other)}")

    def __lt__(self, other):
        return QueryStatement(f"{self.value}<{self.reformat(other)}")

    def __le__(self, other):
        return QueryStatement(f"{self.value}<={self.reformat(other)}")

    def __gt__(self, other):
        return QueryStatement(f"{self.value}>{self.reformat(other)}")

    def __ge__(self, other):
        return QueryStatement(f"{self.value}>={self.reformat(other)}")

    def __str__(self):
        return self.value

    def __repr__(self):
        return self.value


def create_issueissuemap_changeset(
    assignee: typing.Optional[typing.Union[Resource, 'Users']] = None,
    assigneefilter: typing.Optional[str] = None,
    assigneesearch: typing.Optional[str] = None,
    creationmode: typing.Optional[str] = None,
    creator: typing.Optional[Resource] = None,
    dbid: typing.Optional[str] = None,
    hasbackground: typing.Optional[typing.Union[Resource, 'Background']] = None,
    hashistorylogs: typing.Optional[typing.List[typing.Union[Resource, 'Historylog']]] = None,
    hasmappedissue1: typing.Optional[typing.Union[Resource, 'Issue']] = None,
    hasmappedissue1filter: typing.Optional[str] = None,
    hasmappedissue1search: typing.Optional[str] = None,
    hasmappedissue2: typing.Optional[typing.Union[Resource, 'Issue']] = None,
    hasmappedissue2filter: typing.Optional[str] = None,
    hasmappedissue2search: typing.Optional[str] = None,
    history: typing.Optional[Resource] = None,
    id: typing.Optional[str] = None,
    identifier: typing.Optional[str] = None,
    instanceshape: typing.Optional[Resource] = None,
    internalcomment: typing.Optional[str] = None,
    is_active: typing.Optional[int] = None,
    lastmodifieddate: typing.Optional[datetime] = None,
    lifecyclestate: typing.Optional[str] = None,
    lifecyclestatecomment: typing.Optional[str] = None,
    lock_version: typing.Optional[int] = None,
    locked_by: typing.Optional[typing.Union[Resource, 'Users']] = None,
    operationcontext: typing.Optional[str] = None,
    operationmode: typing.Optional[str] = None,
    ratl_forminfo: typing.Optional[str] = None,
    ratl_keysite: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    ratl_mastership: typing.Optional[typing.Union[Resource, 'Ratl_Replicas']] = None,
    rdf__type: typing.Optional[typing.List[Resource]] = None,
    shorttitle: typing.Optional[str] = None,
    subject: typing.Optional[typing.List[str]] = None,
    submitdate: typing.Optional[datetime] = None,
    submitter: typing.Optional[str] = None,
    tags: typing.Optional[str] = None,
    title: typing.Optional[str] = None,
    version: typing.Optional[int] = None,
    
) -> Issueissuemap:
    '''
    Create a Issueissuemap changeset.
    '''
    fields = {}
    if assignee is not None:
        fields["cq:Assignee"] = assignee 
    if assigneefilter is not None:
        fields["cq:AssigneeFilter"] = assigneefilter 
    if assigneesearch is not None:
        fields["cq:AssigneeSearch"] = assigneesearch 
    if creationmode is not None:
        fields["cq:CreationMode"] = creationmode 
    if creator is not None:
        fields["dcterms:creator"] = creator 
    if dbid is not None:
        fields["cq:dbid"] = dbid 
    if hasbackground is not None:
        fields["cq:hasBackground"] = hasbackground 
    if hashistorylogs is not None:
        fields["cq:hasHistoryLogs"] = hashistorylogs 
    if hasmappedissue1 is not None:
        fields["cq:hasMappedIssue1"] = hasmappedissue1 
    if hasmappedissue1filter is not None:
        fields["cq:hasMappedIssue1Filter"] = hasmappedissue1filter 
    if hasmappedissue1search is not None:
        fields["cq:hasMappedIssue1Search"] = hasmappedissue1search 
    if hasmappedissue2 is not None:
        fields["cq:hasMappedIssue2"] = hasmappedissue2 
    if hasmappedissue2filter is not None:
        fields["cq:hasMappedIssue2Filter"] = hasmappedissue2filter 
    if hasmappedissue2search is not None:
        fields["cq:hasMappedIssue2Search"] = hasmappedissue2search 
    if history is not None:
        fields["cq:history"] = history 
    if id is not None:
        fields["cq:id"] = id 
    if identifier is not None:
        fields["dcterms:identifier"] = identifier 
    if instanceshape is not None:
        fields["oslc:instanceShape"] = instanceshape 
    if internalcomment is not None:
        fields["cq:InternalComment"] = internalcomment 
    if is_active is not None:
        fields["cq:is_active"] = is_active 
    if lastmodifieddate is not None:
        fields["cq:LastModifiedDate"] = lastmodifieddate 
    if lifecyclestate is not None:
        fields["cq:LifeCycleState"] = lifecyclestate 
    if lifecyclestatecomment is not None:
        fields["cq:LifeCycleStateComment"] = lifecyclestatecomment 
    if lock_version is not None:
        fields["cq:lock_version"] = lock_version 
    if locked_by is not None:
        fields["cq:locked_by"] = locked_by 
    if operationcontext is not None:
        fields["cq:OperationContext"] = operationcontext 
    if operationmode is not None:
        fields["cq:OperationMode"] = operationmode 
    if ratl_forminfo is not None:
        fields["cq:ratl_forminfo"] = ratl_forminfo 
    if ratl_keysite is not None:
        fields["cq:ratl_keysite"] = ratl_keysite 
    if ratl_mastership is not None:
        fields["cq:ratl_mastership"] = ratl_mastership 
    if rdf__type is not None:
        fields["rdf:type"] = rdf__type 
    if shorttitle is not None:
        fields["oslc:shortTitle"] = shorttitle 
    if subject is not None:
        fields["dcterms:subject"] = subject 
    if submitdate is not None:
        fields["cq:SubmitDate"] = submitdate 
    if submitter is not None:
        fields["cq:Submitter"] = submitter 
    if tags is not None:
        fields["cq:Tags"] = tags 
    if title is not None:
        fields["dcterms:title"] = title 
    if version is not None:
        fields["cq:version"] = version 
    

    fields["dcterms:type"] = "Issueissuemap"
    return Issueissuemap(**fields)