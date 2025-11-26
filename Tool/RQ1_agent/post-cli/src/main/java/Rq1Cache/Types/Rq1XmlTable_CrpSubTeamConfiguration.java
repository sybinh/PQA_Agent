/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import util.EcvTableColumnI.Visibility;

/**
 *
 * @author YSA1COB
 */
public class Rq1XmlTable_CrpSubTeamConfiguration extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String NAME;
    final public Rq1XmlTableColumn_String ID;
    final public Rq1XmlTableColumn_String INC_ASSGN;

    public Rq1XmlTable_CrpSubTeamConfiguration() {

        addXmlColumn(NAME = new Rq1XmlTableColumn_String("Name", 15, "Name", ColumnEncodingMethod.ATTRIBUTE));

        addXmlColumn(ID = new Rq1XmlTableColumn_String("ID", "ID", ColumnEncodingMethod.ATTRIBUTE));
        ID.setVisibility(Visibility.ALWAYS_HIDDEN);

        addXmlColumn(INC_ASSGN = new Rq1XmlTableColumn_String("INC_Assgn", "INC_Assgn", ColumnEncodingMethod.ATTRIBUTE));
        INC_ASSGN.setVisibility(Visibility.ALWAYS_HIDDEN);

    }
}
