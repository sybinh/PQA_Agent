Query
=====

This library provides the interface :py:meth:`rq1.Client.query` that can performs query according to 
the `OSLC Query <https://docs.oasis-open-projects.org/oslc-op/query/v3.0/os/oslc-query.html#using-a-query-capability>`_ specification.

The ``where`` parameter
-----------------------

This parameter specifies the criterias of a query. Normally, you would have to pass in something like ``dcterms:title="Some title"``.
However, I've provided the capability to build such query string using Python syntax.

To start, you can find the properties of a record in ``<RecordClass>Property`` (e.g :py:class:`~rq1.workitem.WorkitemProperty`, :py:class:`~rq1.issue.IssueProperty`). You
can use these Property class to build the query. For example: ``q = IssueProperty.dcterms__title == "issue 1"``, you can ``print(q)`` and see ``dcterms:title="issue 1"``.

Supported comparation operators are:

* Equal: ``==``
* Not equal: ``!=``
* Greater than: ``>``
* Equal or greater than: ``>=``
* Less than: ``<``
* Equal or less than: ``<=``

You can, of course, query datetime: ``IssueProperty.submitdate > datetime.datetime(year=2020, month=1, day=1)``. If the timezone is not provided in datetime initialization, local timezone will be used.

You can also join 2 criterias with ``&`` operator. For example: ``(IssueProperty.dcterms__title == "some title") & (IssueProperty.dcterms__description == "some description")``.

You may instinctively use the ``|`` operator, but this is not supported. Instead you can use the ``is_one_of`` method. Example: ``IssueProperty.dcterms__title.is_one_of(["title 1", "title 2", "title 3"])``.

See :ref:`query-example` for some query usage.

The ``select`` parameter
------------------------

This parameter specifies which properties you need to get in the query result.

Similar to ``where`` parameter, normally you would have to pass in something like ``select=["dcterms:title", "dcterms:description"]``. But you can use ``select=[IssueProperty.dcterms__title, IssueProperty.dcterms__description]`` instead.

You can pass in ``select="*"`` to get all the workitem properties.

You may wonder why we don't use ``select="*"`` all the time? Because it will be very slow to get all properties. In production, you should only specify what you need.

Paging
------

Results of a query are returned by pages (meaning you will not get all the results at once). The :py:meth:`rq1.Client.query` interface provides ``paging`` and ``page_size`` parameter to
control the paging of query results. When ``paging=True``, you can control the number of results per page with ``page_size``.

You can get the next results page with :py:meth:`rq1.Client.get_next_query_page` or you
can get a page of results starting from any index with :py:meth:`rq1.Client.get_query_page`.
