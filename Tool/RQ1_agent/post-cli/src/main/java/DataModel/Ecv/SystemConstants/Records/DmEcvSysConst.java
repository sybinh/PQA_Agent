/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.SystemConstants.Records;

import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvSysConst {

    final static public String CONTAINER_NAME = "Systemkonstante";
    final static private String FIELD_NAME = "Name";                            // SYSCONST_NAME
    final static private String FIELD_WERT = "Wert";                            // SYSCONST_VALUE
    final static private String FIELD_FUNKTION_ID = "FunktionID";               // SYSCONST_FUNCTION_ID
    final static private String FIELD_FUNKTION_NAME = "FunktionName";           // SYSCONST_FUNCTION_NAME
    final static private String FIELD_FUNKTION_VERSION = "FunktionVersion";     // SYSCONST_FUNCTION_VERSION
    final static private String FIELD_KONSTANTENTYP = "Systemkonstantentyp";    // SYSCONST_TYPE
    final static private String FIELD_ERSTVERSION_ID = "ErstversionID";         // SYSCONST_FIRSTVERSION_ID
    final static private String FIELD_ERSTVERSION = "Erstversion";              // SYSCONST_FIRSTVERSION

    private String name = null;
    private String value = null;
    private String functionId = null;
    private String functionName = null;
    private String functionVersion = null;
    private String konstantenTyp = null;
    private String erstversionId = null;
    private String erstversion = null;

    /**
     * Create new object with the data from the given object.
     *
     * @param o
     */
    public DmEcvSysConst(DmEcvSysConst o) {
        assert (o != null);

        this.name = o.name;
        this.value = o.value;
        this.functionId = o.functionId;
        this.functionName = o.functionName;
        this.functionVersion = o.functionVersion;
        this.konstantenTyp = o.konstantenTyp;
        this.erstversionId = o.erstversionId;
        this.erstversion = o.erstversion;
    }

    public DmEcvSysConst(String elementType, String name, String value) {
        this.name = name;
        this.value = value;
    }

    public DmEcvSysConst(EcvXmlContainerElement systemKonstante) {
        assert (systemKonstante != null);

        name = systemKonstante.findFirstText(FIELD_NAME);
        value = systemKonstante.findFirstText(FIELD_WERT);
        functionId = systemKonstante.findFirstText(FIELD_FUNKTION_ID);
        functionName = systemKonstante.findFirstText(FIELD_FUNKTION_NAME);
        functionVersion = systemKonstante.findFirstText(FIELD_FUNKTION_VERSION);
        konstantenTyp = systemKonstante.findFirstText(FIELD_KONSTANTENTYP);
        erstversionId = systemKonstante.findFirstText(FIELD_ERSTVERSION_ID);
        erstversion = systemKonstante.findFirstText(FIELD_ERSTVERSION);
    }

    public String getNameNotNull() {
        return (getNotNull(name));
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueNotNull() {
        return (getNotNull(value));
    }

    public String getFunctionIdNotNull() {
        return (getNotNull(functionId));
    }

    public String getFunctionNameNotNull() {
        return (getNotNull(functionName));
    }

    public String getFunctionVersionNotNull() {
        return (getNotNull(functionVersion));
}

    public String getKonstantTypNotNull() {
        return (getNotNull(konstantenTyp));
    }

    public String getErstversionId() {
        return (getNotNull(erstversionId));
    }

    public String getErstversion() {
        return (getNotNull(erstversion));
    }

    private String getNotNull(String text) {
        if (text != null) {
            return (text);
        } else {
            return ("");
        }
    }

}
