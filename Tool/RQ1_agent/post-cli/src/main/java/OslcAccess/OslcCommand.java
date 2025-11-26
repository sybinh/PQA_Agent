/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.Map;
import java.util.TreeMap;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author gug2wi
 */
public abstract class OslcCommand {

    private OslcProtocolVersion protocolVersion = OslcProtocolVersion.OSLC_20;
    private Map<String, String> headerData = new TreeMap<>();

    protected OslcCommand() {
        protocolVersion = OslcProtocolVersion.OSLC_20;
    }

    protected OslcCommand(OslcProtocolVersion protocolVersion) {
        assert (protocolVersion != null);

        this.protocolVersion = protocolVersion;
    }

    public void setProtocolVersion(OslcProtocolVersion protocolVersion) {
        assert (protocolVersion != null);
        this.protocolVersion = protocolVersion;
    }

    public OslcProtocolVersion getProtocolVersion() {
        return (protocolVersion);
    }
    
    protected EcvXmlElement generateXmlElement(OslcPropertyField property) {
        EcvXmlElement cq_fieldName;
        
        if ((property.getValue() != null) && (property.getValue().isEmpty() == false)) {
            cq_fieldName = new EcvXmlTextElement("cq:" + property.getName(), property.getValue());
        } else {
            cq_fieldName = new EcvXmlEmptyElement("cq:" + property.getName());
        }
        cq_fieldName.addAttribute("rdf:ID", property.getName());
        
        return cq_fieldName;
    }
    
    public abstract String buildCommandString(String oslcUrl);

    public abstract String getAddressForUi();
    
    public void addHeaderData(String name, String value) {
        headerData.put(name, value);
    }
    
    public Map<String, String> getHeaderData() {
        return (headerData);
    }

    static final private CharSequence[][] httpSpecialCharacterTable = {
        {"#", "%23"},
        {"?", "%3F"}};

    static public String encodeForHttp(String clearText) {
        String htmlString = clearText;

        for (int i = 0; i < httpSpecialCharacterTable.length; i++) {
            if (httpSpecialCharacterTable[i][0].length() > 0) {
                htmlString = htmlString.replace(httpSpecialCharacterTable[i][0], httpSpecialCharacterTable[i][1]);
            }
        }
        return (htmlString);
    }
}
