import os
from rq1 import BaseUrl, Client
from rich import print  # pretty print
from rq1.models import Workitem, Issue, IssueProperty

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


def example_1():
    wi = client.get_record_by_uri(
        Workitem,
        "https://rb-dgsrq1-oslc-q.de.bosch.com/cqweb/oslc/repo/RQ1_ACCEPTANCE/db/RQONE/record/16777232-37320999",
    )
    print(wi)


def example_2():
    # This will get all properties of the issue, but it will take some time
    issue = client.get_record_by_rq1_number(Issue, "RQONE03765304")
    print(issue)

    # Select only the properties that you need, this will be faster
    issue = client.get_record_by_rq1_number(
        Issue, "RQONE03765304", select=[IssueProperty.accountnumbers]
    )
    print(issue)


if __name__ == "__main__":
    example_1()
    example_2()
