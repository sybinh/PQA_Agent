/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmToDsValueField;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Data.Types.Rq1LinesPoolProject;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_PstLinesPoolProject extends DmToDsValueField<Rq1RecordInterface, Rq1LinesPoolProject> {

    public DmRq1Field_PstLinesPoolProject(DmElementI parent, Rq1FieldI<Rq1LinesPoolProject> rq1Field, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);
    }

}
