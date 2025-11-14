Developer guide
===============

Project management
------------------

This project uses ``pyproject.toml`` to manage dependencies, metadata and tool configs. I use `rye <https://github.com/astral-sh/rye>`_ to work
with this file but you can use `pdm <https://github.com/pdm-project/pdm>`_ or `Hatch <https://hatch.pypa.io/latest/>`_ or even nothing since
it's just a text file after all. Note: don't use ``poetry`` because it does not follow PEP 621.

To start, run ``rye sync`` or ``pdm sync`` or ``pip install -e .[all]`` to install the library and alls the tool needed to work with it. You should
also set ``RQ1_USER``, ``RQ1_PASSWORD``, ``RQ1_TOOLNAME``, ``RQ1_TOOLVERSION`` environment variable to run test.

This project uses `ruff <https://github.com/astral-sh/ruff>`_ for both formating and linting.

Prerequisite knowledges
-----------------------

Before working with this library, you should self research about these fields:

* How to manage a python project (i.e working with ``pyproject.toml``, build, distribute package,...)
* Web request/response, REST api (``requests`` library, how to use web client like ``postman`` or ``Thunder Client`` to debug REST api)
* RDF format, SPARQL query language
* OSLC standard

	* `OSLC query <https://docs.oasis-open-projects.org/oslc-op/query/v3.0/os/oslc-query.html>`_
	* `OSLC resource shape <https://docs.oasis-open-projects.org/oslc-op/core/v3.0/os/core-shapes.html>`_
	* `OSLC change management <https://docs.oasis-open-projects.org/oslc-op/cm/v3.0/ps01/change-mgt-shapes.html>`_
	* ...

* How to work with ``pydantic`` library
* How to work with ``jinja2`` library

Misc
--------

The RQ1 system real name is ``ClearQuest``. You can use this name to search for its REST api, past issues,...

Services discovery
------------------

The RQ1 system provides discovery ability.

To explore the public OSLC APIs, you can send a GET request to `<https://rb-dgsrq1-oslc-p.de.bosch.com/cqweb/oslc/repo/RQ1_PRODUCTIVE/db/RQONE>`_.

Design
------

The design of this library is extremely simple. All the important functionalities of this library are in the ``src/rq1/__init__.py``.
Other files are just data type definitions and should not be changed.

Even the working of all the interfaces are very simple:

* For query and read interfaces: we send request to the appropriate API, deserialize the response and return it.
* For modify and create interfaces: we serialize python object to json, send it to the API, get response, deserialize it and return.

The serialization from python object to json and deserialization from json to python object are handled by the ``pydantic`` library. As long as the
data type definitions are proper, we don't need to worry about this part. 

Code generation
---------------

Resource shape: each resource type (Workitem, Issue,...) has a resource shape rdf which describes every properties it can have. The resource shapes are
provided along with the APIs in discovery endpoint.

In the ``gen`` folder, there's code that can parse these resource shapes and generate corresponding ``.py`` file using ``jinja2`` template engine. 
You can run the code generation with ``python gen/main.py src/rq1/``.

Query builder
-------------

If you have used this library, you may wonder why we can use ``IssueProperty.title = "some title" & IssueProperty.submitdate > datetime(year, month, day)`` in the query. It just seems like magic, doesn't it. 
Actually, I took inspiration from the ``polars`` library query. The principle is simple, I just override some built-in methods like ``__eq__``, ``__gt__``, ``__and__``,... so that they return a ``QueryStatement``
which can be coverted to the actual OSLC query string. You can see the implementation in the generated code (ex. ``IssueProperty`` class).
