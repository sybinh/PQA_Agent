/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Eis;

/**
 *
 * @author hfi5wi
 */
public class DmEisWorkitemTableRow {
    
    final private String prefixTitle;
    final private DmEisWorkitemTable_WiType_Enumeration type;
    final private String assigneeUserId;
    final private String order;

    public DmEisWorkitemTableRow(String prefixTitle, DmEisWorkitemTable_WiType_Enumeration type, String assigneeUserId, String predecessor) {
        assert (prefixTitle != null);
        assert (prefixTitle.isEmpty() == false);
        assert (type != null);

        this.prefixTitle = prefixTitle;
        this.type = type;
        this.assigneeUserId = assigneeUserId;
        this.order = predecessor;
    }

    public String getPrefixTitle() {
        return prefixTitle;
    }

    public DmEisWorkitemTable_WiType_Enumeration getType() {
        return type;
    }

    public String getAssigneeUserId() {
        return assigneeUserId;
    }

    public String getOrder() {
        return order;
    }
    
    /**
     * Convert a DmEisWorkitemTableRow to a String array
 This method is used for the table model because it does not 
 not support lists
     * @return String []
     */
    public String[] toStringArray() {
        return new String[]{
            this.getPrefixTitle(), this.getType().toString(), 
            this.getAssigneeUserId(), this.getOrder()
        };
    }
    
    public String toString(String separator) {
        return this.getPrefixTitle()+ separator +
                this.getType() + separator + 
                this.getAssigneeUserId() + separator + 
                this.getOrder();
    }
}
