/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Query;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RHO2HC
 */
public class Query {
    private List<Criteria> criterias = new ArrayList<>();
    private QuerySorter sorter;
    private QueryGrouping grouping;

    public Query() {
    }

    public Query(QuerySorter sorter, QueryGrouping grouping) {
        this.sorter = sorter;
        this.grouping = grouping;
    }

    public Query(List<Criteria> filters, QuerySorter sorter, QueryGrouping grouping) {
        this.criterias = filters;
        this.sorter = sorter;
        this.grouping = grouping;
    }
    
    public List<Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<Criteria> filters) {
        this.criterias = filters;
    }

    public QuerySorter getSorter() {
        return sorter;
    }

    public void setSorter(QuerySorter sorter) {
        this.sorter = sorter;
    }

    public QueryGrouping getGrouping() {
        return grouping;
    }

    public void setGrouping(QueryGrouping grouping) {
        this.grouping = grouping;
    }

    @Override
    public String toString() {
        return "Criteria{" + "filters=" + criterias + ", sorter=" + sorter + ", grouping=" + grouping + '}';
    }
    
    
}
