/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */

package OslcAccess.ALM;

import OslcAccess.OslcResponse;
import RestClient.Exceptions.ResponseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author CNI83WI
 */
public class OslcAlmQueryResponse extends OslcAlmResponse{
    static private final Logger LOGGER = Logger.getLogger(OslcAlmResponse.class.getCanonicalName());
    
    final private String responseUrl;
    
    public OslcAlmQueryResponse(ResponseType responseType, String responseUrl) {
        super(responseType);
        
        this.responseUrl = responseUrl;
    }
    
    public static List<OslcAlmQueryResponse> buildResult(OslcResponse response) throws ResponseException {
        assert (response != null);
        
        List<OslcAlmQueryResponse> oslcAlmResponseList = new ArrayList<>();
        List<EcvXmlContainerElement> responseList = response.getResponseBodyList();
        
        for(EcvXmlContainerElement workItemContainer : responseList) {
            List<EcvXmlContainerElement> workItemContainerList = workItemContainer.getContainerElementList();

            for(EcvXmlContainerElement container : workItemContainerList) {
                if(container.hasAttribute("rdf:about") && container.getAttribute("rdf:about").contains("resource")) {
                OslcAlmQueryResponse almResponse = new OslcAlmQueryResponse(OslcAlmResponse.ResponseType.WORKITEM, container.getAttribute("rdf:about"));
                for (EcvXmlElement element : container.getElementList()) {
                    almResponse.addFieldForXmlElement(workItemContainer, element);
                }

                oslcAlmResponseList.add(almResponse);
                }
            }
        }
        
        return oslcAlmResponseList;   
    }

    public String getResponseUrl() {
        return responseUrl;
    }
       
}
