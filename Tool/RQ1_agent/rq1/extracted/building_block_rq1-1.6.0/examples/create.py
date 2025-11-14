from datetime import datetime
import os
from rq1 import BaseUrl, Client
from rich import print  # pretty print
from rq1.base import reference
from rq1.models import Issue, create_issue_changeset
import pytest

RQ1_USER = os.environ["RQ1_USER"]
RQ1_PASSWORD = os.environ["RQ1_PASSWORD"]
RQ1_TOOLNAME = os.environ["RQ1_TOOLNAME"]
RQ1_TOOLVERSION = os.environ["RQ1_TOOLVERSION"]


@pytest.mark.skip(
    reason="Skip this to avoid poluting RQ1 database with nonsense new records"
)
def example():
    client = Client(
        base_url=BaseUrl.ACCEPTANCE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION,
    )

    changeset = create_issue_changeset(
        dcterms__title="Created from building-block-rq1 library",
        belongstoproject=reference(
            "https://rb-dgsrq1-oslc-q.de.bosch.com/cqweb/oslc/repo/RQ1_ACCEPTANCE/db/RQONE/record/16777229-33556572"
        ),
        cq__Description="Generated at " + datetime.now().isoformat(),
        category="Requirement",
    )
    uri = client.create_record(changeset)

    # Get the newly created issue and print it
    issue = client.get_record_by_uri(Issue, uri)
    print(issue)


if __name__ == "__main__":
    example()
