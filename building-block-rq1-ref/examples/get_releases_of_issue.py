import os
from rq1 import BaseUrl, Client
from rich import print  # pretty print
from rq1.models import Issuereleasemap, Issue, Release

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


def example():
    issue = client.get_record_by_uri(
        Issue,
        "https://rb-dgsrq1-oslc-q.de.bosch.com/cqweb/oslc/repo/RQ1_ACCEPTANCE/db/RQONE/record/16777231-37313097",
    )
    for irmap in issue.hasmappedreleases:
        issuereleasemap = client.get_record_by_uri(Issuereleasemap, irmap.uri)
        release = client.get_record_by_uri(Release, issuereleasemap.uri)
        print(release.dcterms__title)


if __name__ == "__main__":
    example()
