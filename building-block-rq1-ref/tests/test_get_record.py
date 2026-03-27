import os

from rq1 import BaseUrl, Client
import pytest

from rq1.issue import Issue
from rq1.problem import Problem
from rq1.project import Project
from rq1.release import Release
from rq1.workitem import Workitem

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


def test_get_workitem():
    workitem = client.get_record_by_rq1_number(Workitem, "RQONE03766567")
    assert workitem.dcterms__title is not None
    assert (
        "[rba_IoExtCj135] Analysis of problem RQONE03595339" in workitem.dcterms__title
    )

    with pytest.raises(ValueError):
        client.get_record_by_rq1_number(Workitem, "RQTWO000000")


def test_get_issue():
    issue = client.get_record_by_rq1_number(Issue, "RQONE03765304")
    assert issue.dcterms__title is not None
    assert (
        "[rba_IoExtCj135] Bugfix: Build break when no device is configured in one postbuild"
        in issue.dcterms__title
    )

    with pytest.raises(ValueError):
        client.get_record_by_rq1_number(Issue, "RQTWO000000")


def test_get_release():
    release = client.get_record_by_rq1_number(Release, "RQONE03430088")
    assert release.dcterms__title is not None
    assert "BC : IoExtDev / 1.39.5" in release.dcterms__title

    with pytest.raises(ValueError):
        client.get_record_by_rq1_number(Release, "RQTWO000000")


def test_get_problem():
    release = client.get_record_by_rq1_number(Problem, "RQONE03293565")
    assert release.dcterms__title is not None
    assert (
        "[rba_IoExtCj135] Wrong handling of ring buffer in idle transition"
        in release.dcterms__title
    )

    with pytest.raises(ValueError):
        client.get_record_by_rq1_number(Problem, "RQTWO000000")


def test_get_project():
    project = client.get_record_by_rq1_number(Project, "RQONE00010100")
    assert project.dcterms__title is not None
    assert "HWE - Hardware Encapsulation" in project.dcterms__title

    with pytest.raises(ValueError):
        client.get_record_by_rq1_number(Project, "RQTWO000000")
