Getting started
===============

``building-block-rq1`` is a python library that helps you interract with RQ1 system. To use this libary,
basic knowledge of the python programming language is required.

You can play around with the libary in a python REPL. But we recommend setting up a proper project with a proper IDE
to take advantage of code completion and all the type hints this library has offered.

Beginner should start with this `page <https://inside-docupedia.bosch.com/confluence/x/ujpg9w>`_ to properly set up
a python project at Bosch.

Features
--------

This library currently supports query, read, update and create operations for all 33 record types of the RQ1 system:
Attachmentmapping, Attachments, Background, Commercial, Commercialacl, Contact, Email_Rule, Exchangeconfig,
Exchangeprotocol, Externallink, Groups, History, Historylog, Issue, Issueissuemap, Issuereleasemap, Mapping,
Problem, Project, Ratl_Replicas, Release, Releasereleasemap, Rq1_Configuration, Rq1_Instructiontype, Rq1_Logging,
Rq1_Metadata, Rq1_Rolespermission, Rq1_Roleuser, Rq1_Rulesconfig, Rq1_Tldselector, Rq1_Webtoolconfig, Users, Workitem,

This library also supports downloading and uploading attachments.

.. _installation:

Installation
------------

Follow the instruction `here <https://inside-docupedia.bosch.com/confluence/x/qUlg9w>`_ to set up the pip extra index url.

Then install the library with

.. code-block::

	pip install building-block-rq1

If you ever get ``SSL_HANDSHAKE_ERROR`` when using the library, re-install the library with

.. code-block::

	pip install building-block-rq1[ssl]

Which will install the older version of ``requests`` library with less restrictive SSL condition.

``toolname`` and ``toolversion``
--------------------------------

To interact with RQ1 server, a tool (represented by ``toolname`` and ``toolversion``) needs to be in the whitelist or else all requests to server will be rejected.

Follow instruction here to register your tool: `Tool whitelist <https://www2.intranet.bosch.com/dgs/devenv/info/RequestOne/RQ1Tools/OSLC_AccessDevs.htm#whiteListRegistration>`_

Just for playing around, you can disguise as other tool by using their ``toolname`` and ``toolversion``. Some known working tools are listed below.

===============  ============
Tool name        Tool version
===============  ============
infotool         2.0
EstimationSheet  6.1.0
ImpactChecklist  1.0
...              ...
===============  ============

You can get a list of tools by trying out this `OSLC supertest app <https://rb-dgsrq1-development.de.bosch.com/rq1_data/DEV/OSLC_Interface/OSLCInterface.html>`_

Usage
-----

Check out :doc:`/Examples` for example usage.
