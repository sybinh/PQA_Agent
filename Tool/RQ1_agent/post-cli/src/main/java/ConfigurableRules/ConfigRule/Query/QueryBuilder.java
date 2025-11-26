/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Query;

import ConfigurableRules.ConfigRule.Enumerations.SorterOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RHO2HC
 */
public class QueryBuilder {
    private List<Criteria> filters = new ArrayList<>();
    private QuerySorter sorter = new QuerySorter();
    private QueryGrouping grouping;
    
    public Query create() {
        return new Query(filters, sorter, grouping);
    }
    
    public QueryBuilder addCriteria(Criteria filter) {
        this.filters.add(filter);
        return this;
    }
    
    public QueryBuilder addCriterias(List<Criteria> filters) {
        this.filters = filters;
        return this;
    }
    
    public QueryBuilder sortedBy(QuerySorter sorter) {
        this.sorter.setSortedBy(sorter.getSortedBy());
        this.sorter.setSubSortedBy(sorter.getSubSortedBy());
        return this;
    }
    
    public QueryBuilder orderBy(SorterOrder order) {
        this.sorter.setOrder(order);
        return this;
    }
    
    public QueryBuilder groupedBy(QueryGrouping grouping) {
        this.grouping = grouping;
        return this;
    }
}
