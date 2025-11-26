/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Dgs;

/**
 *
 * @author gug2wi
 */
public class DmDgsIssueSW implements DmDgsIssueSW_I {

    final private String title;
    final private String id;

    public DmDgsIssueSW(String id, String title) {
        assert (id != null);
        assert (id.isEmpty() == false);
        assert (title != null);
        assert (title.isEmpty() == false);

        this.id = id;
        this.title = title;
    }

    @Override
    public String getTitle() {
        return (title);
    }

    @Override
    public String getId() {
        return (id);
    }

}
