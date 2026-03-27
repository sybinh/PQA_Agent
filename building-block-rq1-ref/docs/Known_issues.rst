Known issues
============

SSL handshake error
-------------------

See :ref:`installation`.

Network error when getting a record
-----------------------------------

It is known that sometimes, when using ``get_record_by_uri`` or ``get_record_by_rq1_number``, the servers return network timeout error.
The mentioned interfaces try to get all information of a record, so if a record has too many information (for example, a project can have
thousands of ``hasworkitems``), it will likely cause timeout.

The solution is to limit the amount of information that we need to get. You can provide ``select`` parameter to ``get_record_by_rq1_number``
or use ``query`` + ``select`` to get a record.

RQ1 not found even though it exists
-----------------------------------

If you use the interface :py:meth:`rq1.Client.get_record_by_rq1_number` and get error "RQ1 number not found" even though you can see the record on IPE or RQ1 web. Then
add the ``select=[something]`` param in the method call. The reason is that, by default, the library try to access all information of a record. However, security policy
prevents one from accessing attachments of a different project. That's why the record will not be visible in that case.

422 Unprocessable Entity
------------------------

It's likely that your username, password, toolname and toolversion is wrong.
