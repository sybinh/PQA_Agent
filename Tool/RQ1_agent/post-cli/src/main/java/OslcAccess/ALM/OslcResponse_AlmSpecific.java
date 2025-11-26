/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.ALM;

import OslcAccess.OslcResponse;
import java.util.List;
import java.util.Map;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public class OslcResponse_AlmSpecific extends OslcResponse {

    OslcResponse_AlmSpecific(Map<String, String> responseHeader, EcvXmlElement responseBodyXml, String responseBodyString) {
        super(responseHeader, responseBodyXml, responseBodyString);
    }

    OslcResponse_AlmSpecific(Map<String, String> responseHeader, List<byte[]> responseInBinaryFormat) {
        super(responseHeader, responseInBinaryFormat);
    }
    
    @Override
    public String getUrlForNextPage() {        
        if (responseBodyList.isEmpty() == false && responseBodyList.toString().contains("resultToken")) {
            String nextPageUrl = null;
            
            try {
                nextPageUrl = responseBodyList.get(0).getContainerElementList_AttributeContains("rdf:Description", "rdf:about", "resultToken").get(0).getElement("oslc:nextPage").getAttribute("rdf:resource");
            } catch (EcvXmlElement.NotfoundException ex) {
            }
            
            //intermediate Solution to the overhead created by loading too many work items
            if(nextPageUrl != null){
                int i_start = nextPageUrl.indexOf("startIndex=");
                int i_end = nextPageUrl.length();
                
                String startIndex = nextPageUrl.substring(i_start + 11, i_end);
                
                if(Integer.parseInt(startIndex) < 5000) {
                    return (nextPageUrl);
                }
            }
        }
        return (null);
    }
    
    @Override
    public int getTotalCount() {
        if (responseBodyList.isEmpty() == false && responseBodyList.toString().contains("resultToken")) {
            try {
                String totalCount = responseBodyList.get(0).getContainerElementList_AttributeContains("rdf:Description", "rdf:about", "resultToken").get(0).getTextElement("oslc:totalCount").getText();
                return (Integer.parseInt(totalCount));
            } catch (EcvXmlElement.NotfoundException ex) {
            }
        }
        return (-1);
    }

}
