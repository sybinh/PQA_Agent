/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Eis;

import java.util.logging.Logger;
import util.EcvEnumeration;

/**
 *
 * @author hfi5wi
 */
public enum DmEisWorkitemTable_WiType_Enumeration implements EcvEnumeration {

    EVALUATE("Evaluate"),
    IMPLEMENT("Implement"),
    CONDFMEA("CondFMEA"), 
    CONDDRBFM("CondDRBFM"), 
    PREPCUSTREV("PrepCustRev"), 
    QUALIFY("Qualify"), 
    TRANSLATE("Translate"), 
    SUPPORT("Support"), 
    MISC("Misc"),
    CODEXSWCOMPLIANCE("Codex SW Compliance");
    private final String type;

    private final static Logger LOGGER = Logger.getLogger(DmEisWorkitemTable_WiType_Enumeration.class.getCanonicalName());
    
    DmEisWorkitemTable_WiType_Enumeration(String type) {
        assert (type != null);
        this.type = type;
    }
    
    /**
     * equal to valueOf() method but with IllegalArgumentException catch if 
     * string type is not a valid WiType. If the string is not a WiType, this function
     * return WiType.EVALUATE.
     */
    public static DmEisWorkitemTable_WiType_Enumeration getWiType(String type) {
        try {
            String wiTypeString = type.trim().toUpperCase();
            DmEisWorkitemTable_WiType_Enumeration wiType = DmEisWorkitemTable_WiType_Enumeration.valueOf(wiTypeString);
            return wiType;
        } catch(IllegalArgumentException ex) {
            LOGGER.severe("Unknown WiType: " + ex.getMessage());
            return DmEisWorkitemTable_WiType_Enumeration.EVALUATE;
        }
    }
    
    @Override
    public String getText() {
        return type;
    }

    @Override
    public String toString() {
        return (getText());
    }
}
