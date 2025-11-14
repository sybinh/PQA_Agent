from datetime import datetime
import os
from rq1 import BaseUrl, Client
from rq1.models import create_issue_changeset, Issue

RQ1_USER = os.environ["RQ1_USER"]
RQ1_PASSWORD = os.environ["RQ1_PASSWORD"]
RQ1_TOOLNAME = os.environ["RQ1_TOOLNAME"]
RQ1_TOOLVERSION = os.environ["RQ1_TOOLVERSION"]


def example():
    client = Client(
        base_url=BaseUrl.ACCEPTANCE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION,
    )

    uri = "https://rb-dgsrq1-oslc-q.de.bosch.com/cqweb/oslc/repo/RQ1_ACCEPTANCE/db/RQONE/record/16777231-37424407"

    new_description = "Hello from building-block-rq1 - at " + datetime.now().isoformat()

    changset = create_issue_changeset(dcterms__description=new_description)
    client.modify_record(uri, changset)

    # Get back the issue and check new description was updated
    modified_issue = client.get_record_by_uri(Issue, uri)
    assert modified_issue.dcterms__description == new_description


if __name__ == "__main__":
    example()
