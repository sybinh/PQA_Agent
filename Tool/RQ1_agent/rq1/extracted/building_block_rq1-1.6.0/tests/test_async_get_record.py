import os
import asyncio
from typing import Any, Callable

from rq1 import BaseUrl, AsyncClient
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


@pytest.fixture
async def async_client():
    """Fixture to provide an AsyncClient instance for tests."""
    async with AsyncClient(
        base_url=BaseUrl.ACCEPTANCE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION,
    ) as client:
        yield client


def async_test(func: Callable[..., Any]) -> Callable[..., Any]:
    """Decorator to run async test functions."""
    def wrapper(*args, **kwargs):
        return asyncio.run(func(*args, **kwargs))
    return wrapper


@async_test
async def test_async_get_workitem():
    async with AsyncClient(
        base_url=BaseUrl.ACCEPTANCE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION,
    ) as client:
        workitem = await client.get_record_by_rq1_number(Workitem, "RQONE03766567")
        assert workitem.dcterms__title is not None
        assert (
            "[rba_IoExtCj135] Analysis of problem RQONE03595339" in workitem.dcterms__title
        )

        with pytest.raises(ValueError):
            await client.get_record_by_rq1_number(Workitem, "RQTWO000000")


@async_test
async def test_async_get_issue():
    async with AsyncClient(
        base_url=BaseUrl.ACCEPTANCE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION,
    ) as client:
        issue = await client.get_record_by_rq1_number(Issue, "RQONE03765304")
        assert issue.dcterms__title is not None
        assert (
            "[rba_IoExtCj135] Bugfix: Build break when no device is configured in one postbuild"
            in issue.dcterms__title
        )

        with pytest.raises(ValueError):
            await client.get_record_by_rq1_number(Issue, "RQTWO000000")


@async_test
async def test_async_get_release():
    async with AsyncClient(
        base_url=BaseUrl.ACCEPTANCE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION,
    ) as client:
        release = await client.get_record_by_rq1_number(Release, "RQONE03430088")
        assert release.dcterms__title is not None
        assert "BC : IoExtDev / 1.39.5" in release.dcterms__title

        with pytest.raises(ValueError):
            await client.get_record_by_rq1_number(Release, "RQTWO000000")


@async_test
async def test_async_get_problem():
    async with AsyncClient(
        base_url=BaseUrl.ACCEPTANCE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION,
    ) as client:
        problem = await client.get_record_by_rq1_number(Problem, "RQONE03293565")
        assert problem.dcterms__title is not None
        assert (
            "[rba_IoExtCj135] Wrong handling of ring buffer in idle transition"
            in problem.dcterms__title
        )

        with pytest.raises(ValueError):
            await client.get_record_by_rq1_number(Problem, "RQTWO000000")


@async_test
async def test_async_get_project():
    async with AsyncClient(
        base_url=BaseUrl.ACCEPTANCE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION,
    ) as client:
        project = await client.get_record_by_rq1_number(Project, "RQONE00010100")
        assert project.dcterms__title is not None
        assert "HWE - Hardware Encapsulation" in project.dcterms__title

        with pytest.raises(ValueError):
            await client.get_record_by_rq1_number(Project, "RQTWO000000")


@async_test
async def test_async_concurrent_queries():
    """Test concurrent async operations."""
    async with AsyncClient(
        base_url=BaseUrl.ACCEPTANCE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION,
    ) as client:
        # Run multiple queries concurrently
        tasks = [
            client.get_record_by_rq1_number(Workitem, "RQONE03766567"),
            client.get_record_by_rq1_number(Issue, "RQONE03765304"),
            client.get_record_by_rq1_number(Release, "RQONE03430088"),
        ]
        
        results = await asyncio.gather(*tasks)
        
        # Verify all results
        workitem, issue, release = results
        
        assert workitem.dcterms__title is not None
        assert "[rba_IoExtCj135] Analysis of problem RQONE03595339" in workitem.dcterms__title
        
        assert issue.dcterms__title is not None
        assert "[rba_IoExtCj135] Bugfix: Build break when no device is configured in one postbuild" in issue.dcterms__title
        
        assert release.dcterms__title is not None
        assert "BC : IoExtDev / 1.39.5" in release.dcterms__title


@async_test
async def test_async_query_with_pagination():
    """Test async query with pagination."""
    async with AsyncClient(
        base_url=BaseUrl.ACCEPTANCE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION,
    ) as client:
        # Query with pagination
        query_result = await client.query(
            Issue,
            select=["cq:id", "dcterms:title"],
            page_size=2
        )
        
        assert query_result.total_count > 0
        assert len(query_result.members) <= 2
        
        # Test pagination if there are more pages
        if query_result.next_page:
            next_page = await client.get_next_query_page(query_result)
            assert next_page is not None
            assert len(next_page.members) <= 2
