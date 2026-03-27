import os
from rq1 import BaseUrl, Client
from rq1.models import Attachments, AttachmentsProperty, Issue, Problem
from rich import print  # pretty print
import pytest

RQ1_USER = os.environ["RQ1_USER"]
RQ1_PASSWORD = os.environ["RQ1_PASSWORD"]
RQ1_TOOLNAME = os.environ["RQ1_TOOLNAME"]
RQ1_TOOLVERSION = os.environ["RQ1_TOOLVERSION"]

client = Client(
    base_url=BaseUrl.ACCEPTANCE,
    username=RQ1_USER,
    password=RQ1_PASSWORD,
    toolname=RQ1_TOOLNAME,
    toolversion=RQ1_TOOLVERSION,
)


def example_query_and_download():
    # Get all the attachments of RQONE03659033
    issue = client.get_record_by_rq1_number(Issue, "RQONE03659033")

    query = client.query(
        Attachments,
        select=[
            AttachmentsProperty.filename,
            AttachmentsProperty.dcterms__description,
            AttachmentsProperty.dbid,
            AttachmentsProperty.entity_dbid,
            AttachmentsProperty.entity_fielddef_id,
            AttachmentsProperty.identifier,
        ],
        where=AttachmentsProperty.entity_dbid == issue.identifier,
    )
    print(query)

    # Download the first attachment
    content = client.get_attachement_content(
        issue.uri, query.members[0].entity_fielddef_id, query.members[0].identifier
    )
    with open(query.members[0].filename, "wb") as f:
        f.write(content)

    # do something with the file
    # ...
    # then delete it
    os.remove(query.members[0].filename)


@pytest.mark.skip(
    reason="Exclude this example from testing because we can't upload the same file multiple time, we'll get an error the second time this example runs"
)
def example_upload():
    # upload the attachment file
    problem = client.get_record_by_rq1_number(Problem, "RQONE03232418")
    client.upload_attachment(
        problem.uri, "some_file.txt", description="some description"
    )

    # query the attachments to check if the file was uploaded
    query = client.query(
        Attachments,
        select=[
            AttachmentsProperty.filename,
            AttachmentsProperty.dcterms__description,
            AttachmentsProperty.cq__description,
            AttachmentsProperty.dbid,
            AttachmentsProperty.entity_dbid,
            AttachmentsProperty.entity_fielddef_id,
            AttachmentsProperty.identifier,
        ],
        where=AttachmentsProperty.entity_dbid == problem.identifier,
    )
    print(query)


if __name__ == "__main__":
    example_query_and_download()
    # example_upload()
