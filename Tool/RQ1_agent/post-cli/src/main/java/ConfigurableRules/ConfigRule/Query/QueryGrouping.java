/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Query;

/**
 *
 * @author RHO2HC
 */
public class QueryGrouping {
    private String groupedBy;
    private String subGroupedBy;

    public QueryGrouping() {
    }
    
    public QueryGrouping(String groupedBy) {
        this.groupedBy = groupedBy;
    }
    
    public QueryGrouping(String groupedBy, String subGroupedBy) {
        this.groupedBy = groupedBy;
        this.subGroupedBy = subGroupedBy;
    }

    public String getGroupedBy() {
        return groupedBy;
    }

    public void setGroupedBy(String groupedBy) {
        this.groupedBy = groupedBy;
    }

    public String getSubGroupedBy() {
        return subGroupedBy;
    }

    public void setSubGroupedBy(String subGroupedBy) {
        this.subGroupedBy = subGroupedBy;
    }

    @Override
    public String toString() {
        return "Grouptor{" + "groupedBy=" + groupedBy + ", subGroupedBy=" + subGroupedBy + '}';
    }
}
