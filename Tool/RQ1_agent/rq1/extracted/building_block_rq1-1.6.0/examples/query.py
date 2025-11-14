from datetime import datetime
import os
from rq1 import BaseUrl, Client
from rq1.models import Issue, IssueProperty, Workitem, Project, ProjectProperty, Users, UsersProperty
from rq1.base import reference
from rich import print  # pretty print

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
    # query all workitems in the database
    # select="*": take all properties
    # paging=True, page_size=5: use paging, each page has 5 members
    query = client.query(Workitem, select="*", paging=True, page_size=5)

    # check that only 5 workitems are returned in the first page
    assert len(query.members) == 5

    # here is the total workitems
    print(query.total_count)

    # you can get the next page with
    next_page = client.get_next_query_page(query)
    assert next_page is not None
    assert len(next_page.members) == 5

    # you can get a page at any index with
    # start_index=13 is not page index, but member index.
    # So, the line below should return members from index 13 to 17 since we have 5 members per page
    random_page = client.get_query_page(query, start_index=13)
    assert random_page is not None
    assert len(random_page.members) == 5


def example_2():
    # let's try a more complex query
    # first, we get the uri of project RQONE00002140
    query = client.query(
        Project,
        where=ProjectProperty.id == "RQONE00002140",
        select=[
            ProjectProperty.dcterms__title,
            ProjectProperty.status,
        ],  # only take title and status of the project
    )
    assert query.total_count == 1
    project = query.members[0]
    assert (
        project.dcterms__title is not None
        and "IoExtDev - IO External Devices" in project.dcterms__title
    )
    assert project.status == "Active"

    # then get uri of some users
    query = client.query(Users, where=UsersProperty.login_name == "NTO7HC")
    user1 = query.members[0]
    query = client.query(Users, where=UsersProperty.login_name == "VDO8HC")
    user2 = query.members[0]

    clause_1 = IssueProperty.belongstoproject == reference(project.uri)
    clause_2 = IssueProperty.submitdate > datetime(2020, 1, 1)
    clause_3 = IssueProperty.assignee.is_one_of(
        [reference(user1.uri), reference(user2.uri)]
    )
    query = client.query(
        Issue, where=clause_1 & clause_2 & clause_3, paging=True, page_size=5
    )
    print(query)


def example_3():
    # You can also use a predefined query from RQ1 web app
    query = client.run_query(Workitem, query_id=192940459, select="*", page_size=10)
    print(query)


if __name__ == "__main__":
    example_1()
    example_2()
    example_3()
