/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Query;

import ConfigurableRules.ConfigRule.Enumerations.SorterOrder;

/**
 *
 * @author RHO2HC
 */
public class QuerySorter {
    private String sortedBy = "";
    private String subSortedBy = "";
    private SorterOrder order = SorterOrder.ASC;

    public QuerySorter() {
    }
 
    public QuerySorter(String sortedBy) {
        this.sortedBy = sortedBy;
    }
    
    public QuerySorter(String sortedBy, String subSortedBy) {
        this.sortedBy = sortedBy;
        this.subSortedBy = subSortedBy;
    }
    
    public QuerySorter(String sortedBy, SorterOrder order) {
        this.sortedBy = sortedBy;
        this.order = order;
    }
    
    public QuerySorter(String sortedBy, String subSortedBy, SorterOrder order) {
        this.sortedBy = sortedBy;
        this.subSortedBy = subSortedBy;
        this.order = order;
    }
    
    public String getSortedBy() {
        return sortedBy;
    }

    public void setSortedBy(String sortedBy) {
        this.sortedBy = sortedBy;
    }

    public SorterOrder getOrder() {
        return order;
    }

    public void setOrder(SorterOrder order) {
        this.order = order;
    }

    public String getSubSortedBy() {
        return subSortedBy;
    }

    public void setSubSortedBy(String subSortedBy) {
        this.subSortedBy = subSortedBy;
    }

    @Override
    public String toString() {
        return "CriteriaSorter{" + "sortedBy=" + sortedBy + ", subSortedBy=" + subSortedBy + ", order=" + order + '}';
    }
}
