/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import RestClient.Exceptions.ResponseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;

/**
 * Container for the response sent by the OSLC server.
 *
 */
public class OslcResponse {

    static final private java.util.logging.Logger logger = java.util.logging.Logger.getLogger(OslcResponse.class.getCanonicalName());

    final private Map<String, String> responseHeader;
    final protected List<EcvXmlContainerElement> responseBodyList = new ArrayList<>();
    final private String responseBodyString;
    private ArrayList<OslcResponseRecordI> resultRecords;
    //
    // Quick and dirty solution to introduce file download.
    //
    final private List<byte[]> responseInBinaryFormat;

    /**
     * Create the object for a received response that can be represented as
     * string or XML content
     *
     * @param responseHeader Header fields from the response.
     * @param responseBodyXml Body of the response.
     */
    protected OslcResponse(Map<String, String> responseHeader, EcvXmlElement responseBodyXml, String responseBodyString) {
        assert (responseHeader != null);
        // responseBody is null in case of put command
        // responseBodyString is null in case of put command

        this.responseHeader = responseHeader;
        if (responseBodyXml != null) {
            this.responseBodyList.add((EcvXmlContainerElement) responseBodyXml);
        }
        this.responseBodyString = responseBodyString;
        this.responseInBinaryFormat = null;
    }

    protected OslcResponse(Map<String, String> responseHeader, List<byte[]> responseInBinaryFormat) {
        assert (responseHeader != null);
        assert (responseInBinaryFormat != null);

        this.responseHeader = responseHeader;
        this.responseBodyString = null;
        this.responseInBinaryFormat = responseInBinaryFormat;
    }

    final public String getResponseHeader(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);
        return (responseHeader.get(name));
    }

    final public List<EcvXmlContainerElement> getResponseBodyList() {
        return (responseBodyList);
    }

    final public String getResponseBodyString() {
        return (responseBodyString);
    }

    public List<byte[]> getResponseInBinaryFormat() {
        return responseInBinaryFormat;
    }

    public String getUrlForNextPage() {
        if (responseBodyList.isEmpty() == false) {
            try {
                return (responseBodyList.get(0).getContainerElement("oslc:ResponseInfo").getElement("oslc:nextPage").getAttribute("rdf:resource"));
            } catch (EcvXmlElement.NotfoundException ex) {
            }

            //
            // Get next page for OSLC 1.0
            //
            for (EcvXmlElement linkElement : responseBodyList.get(0).getElementList("link")) {
                if (linkElement instanceof EcvXmlEmptyElement) {
                    if ("next".equals(linkElement.getAttribute("rel")) == true) {
                        return (linkElement.getAttribute("href"));
                    }
                }
            }
        }
        return (null);
    }

    public int getTotalCount() {
        if (responseBodyList.isEmpty() == false) {
            try {
                String totalCount = responseBodyList.get(0).getContainerElement("oslc:ResponseInfo").getTextElement("oslc:totalCount").getText();
                return (Integer.parseInt(totalCount));
            } catch (EcvXmlElement.NotfoundException ex) {
            }
        }
        return (-1);
    }

    private void splitResponseIntoRecords() throws ResponseException {
        if (resultRecords != null) {
            return;
        }

        resultRecords = new ArrayList<>(10);
        for (EcvXmlContainerElement responseBody : responseBodyList) {

            if (responseBody.getName().equals("rdf:RDF") == true) {
                splitResponseIntoRecordsForOslc20(responseBody);
            } else {
                splitResponseIntoRecordsForOslc10(responseBody);
            }
        }

    }

    private void splitResponseIntoRecordsForOslc10(EcvXmlContainerElement responseBody) throws ResponseException {

        //
        // Find elements which contains the result list
        //
        List<EcvXmlContainerElement> resultRecordList = null;

        //
        // An element with name 'feed' is returned, if the result is a list of elements (e.g. for full text search or a search with criterias).
        // No such element exists, if we got the result for a read via RDF-
        //
        if (responseBody.getName().equals("feed") == true) {
            resultRecordList = new ArrayList<>();
            for (EcvXmlContainerElement entry : responseBody.getContainerElementList("entry")) {
                try {
                    EcvXmlContainerElement content = entry.getContainerElement("content");
                    resultRecordList.addAll(content.getContainerElementList());
                } catch (EcvXmlElement.NotfoundException ex) {
                }
            }
        } else {
            //
            // Result for request via RDF. Exactly one record received.
            //
            resultRecordList = new ArrayList<>(1);
            resultRecordList.add(responseBody);
        }

        //
        // Extract data from result list
        //
        for (EcvXmlContainerElement xmlRecord : resultRecordList) {
            resultRecords.add(new OslcResponseRecord_Oslc10(xmlRecord));
        }
    }

    private void splitResponseIntoRecordsForOslc20(EcvXmlContainerElement responseBody) throws ResponseException {

        try {
            if (responseBody.getContainerElement("oslc:ResponseInfo").getTextElement("oslc:totalCount").getText().equals("0") == true) {
                //
                // Result list is empty.
                //
                return;
            }
        } catch (EcvXmlElement.NotfoundException ex) {

        }

        //
        // For each result record, an element rdfs:member exists within rdf:Description.
        // Within rdfs:member, there is the element oslc:ChangeRequest which contains the data fields.
        //
        // Note: The list is empty, if no record matches the search criteria.
        //
        List<EcvXmlContainerElement> resultRecordList;
        try {
            EcvXmlContainerElement rdf_Description = responseBody.getContainerElement("rdf:Description");
            List<EcvXmlContainerElement> rdfs_member = rdf_Description.getContainerElementList("rdfs:member");
            resultRecordList = rdfs_member;
        } catch (EcvXmlElement.NotfoundException ex) {
            //
            // For a GET with direct reference (rdfAbout), no rdf:Description and no rdfs:member exists.
            // Instead of this, oslc_cm:ChangeRequest exists directly in rdf:RDF.
            //
            resultRecordList = new ArrayList<>(1);
            resultRecordList.add(responseBody);
        }

        for (EcvXmlContainerElement xmlElement : resultRecordList) {
            EcvXmlContainerElement xmlRecord;
            try {
                xmlRecord = xmlElement.getContainerElement("oslc_cm:ChangeRequest");
            } catch (EcvXmlElement.NotfoundException ex) {
                logger.warning(ex.getMessage());
                throw new ResponseException("splitResponseIntoRecords", "Get oslc_cm:ChangeRequest from " + (resultRecords.size() + 1) + ". <rdf:Description>.<rdfs:member>", responseBody);
            }
            resultRecords.add(new OslcResponseRecord_Oslc20(xmlRecord));
        }
    }

    final public OslcResponseRecordI getSolelyRecord() throws ResponseException {
        splitResponseIntoRecords();
        if (resultRecords.size() > 1) {
            throw new ResponseException("More than one record received.", responseBodyList.get(0));
        } else if (resultRecords.size() < 1) {
            throw new ResponseException("No response received.");
        }
        return resultRecords.get(0);
    }

    final public List<OslcResponseRecordI> getRecords() throws ResponseException {
        splitResponseIntoRecords();
        return (resultRecords);
    }

    final void addResponse(OslcResponse response) {
        assert (response != null);
        this.responseBodyList.addAll(response.responseBodyList);
    }

}
