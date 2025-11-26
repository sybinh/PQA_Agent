/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.ALM;

import OslcAccess.OslcPutCommand;
import java.util.List;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author CNI83WI
 */
public class OslcAlmPutCommand extends OslcPutCommand {

    private final String url;
    private final List<OslcAlmResponse.Field> fieldList;

    public OslcAlmPutCommand(String url, List<OslcAlmResponse.Field> fieldList) {
        assert(url != null);
        assert(fieldList != null);
        
        this.url = url;
        this.fieldList = fieldList;
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        return url;
    }

    @Override
    public String getAddressForUi() {
        return url;
    }

    @Override
    public String buildBodyString() {
        EcvXmlContainerElement xmlContainer = new EcvXmlContainerElement("rdf:RDF");
        xmlContainer.addAttribute("xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        xmlContainer.addAttribute("xmlns:dcterms", "http://purl.org/dc/terms/");
        xmlContainer.addAttribute("xmlns:rtc_ext", "http://jazz.net/xmlns/prod/jazz/rtc/ext/1.0/");
        xmlContainer.addAttribute("xmlns:oslc", "http://open-services.net/ns/core#");
        xmlContainer.addAttribute("xmlns:acp", "http://jazz.net/ns/acp#");
        xmlContainer.addAttribute("xmlns:oslc_cm", "http://open-services.net/ns/cm#");
        xmlContainer.addAttribute("xmlns:oslc_cmx", "http://open-services.net/ns/cm-x#");
        xmlContainer.addAttribute("xmlns:oslc_pl", "http://open-services.net/ns/pl#");
        xmlContainer.addAttribute("xmlns:acc", "http://open-services.net/ns/core/acc#");
        xmlContainer.addAttribute("xmlns:rtc_cm", "http://jazz.net/xmlns/prod/jazz/rtc/cm/1.0/");
        xmlContainer.addAttribute("xmlns:process", "http://jazz.net/ns/process#");
        EcvXmlContainerElement description = new EcvXmlContainerElement("rdf:Description");
        description.addAttribute("rdf:about", url);
        xmlContainer.addElement(description);

        for (OslcAlmResponse.Field field : fieldList) {
            String fieldName = field.getName();
            String fieldValue = field.getValue();
            
            EcvXmlElement element;
            
            if(fieldValue != null && fieldValue.isEmpty() == false) {
                element = new EcvXmlTextElement(fieldName, fieldValue);
            } else {
                element = new EcvXmlEmptyElement(fieldName);
            }
            
            description.addElement(element);
            
        }

        String bodyString = xmlContainer.getXmlString();

        return bodyString;
    }
}
