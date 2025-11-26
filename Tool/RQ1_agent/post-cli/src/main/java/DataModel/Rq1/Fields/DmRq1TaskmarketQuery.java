/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

/**
 *
 * @author chh83wi
 */
public class DmRq1TaskmarketQuery extends DmRq1StoredQuery {

    public DmRq1TaskmarketQuery(String id) {
        super("Taskmarket", id);
    }

    @Override
    public String getViewTitle() {
        return "Taskmarket";
    }

    @Override
    public String getNameForUserInterface() {
        return "Taskmarket";
    }

    @Override
    public String getTitle() {
        return "Taskmarket";
    }

    @Override
    public String getId() {
        return super.getId();
    }

}
