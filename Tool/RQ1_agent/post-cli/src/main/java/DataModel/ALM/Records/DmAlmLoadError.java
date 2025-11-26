/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Text;
import DataModel.DmConstantField_Text;
import DataModel.DmElement;
import DataStore.ALM.DsAlmLoadErrorRecord;

/**
 *
 * @author GUG2WI
 */
public class DmAlmLoadError extends DmElement implements DmAlmElementI {

    public static final String ELEMENT_TYPE = "ALM Load Error";

    final private DsAlmLoadErrorRecord dsAlmRecord;

    final public DmAlmField_Text URL;
    final public DmConstantField_Text PROBLEM;

    public DmAlmLoadError(DsAlmLoadErrorRecord dsAlmRecord) {
        super(ELEMENT_TYPE);
        assert (dsAlmRecord != null);

        this.dsAlmRecord = dsAlmRecord;

        addField(URL = new DmAlmField_Text(dsAlmRecord.URL, "URL"));
        addField(PROBLEM = new DmConstantField_Text("Internal Problem Description", dsAlmRecord.getProblem().toString()));
    }

    @Override
    public void reload() {
    }

    @Override
    public String getTitle() {
        return (dsAlmRecord.getId());
    }

    @Override
    public String getId() {
        return (dsAlmRecord.getId());
    }

    @Override
    public String getUrl() {
        return (URL.getValueAsText());
    }

    @Override
    public String checkForUnusedFields() {
        return ("No unused fields");
    }

    @Override
    public String toString() {
        return (getElementType() + " - " + dsAlmRecord.URL.getDataModelValueAsString());
    }

}
