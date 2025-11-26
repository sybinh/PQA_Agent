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
 * @author mto1so
 */
public class Rq1XmlTable_ClusterConfiguration extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String NAME;
    final public Rq1XmlTableColumn_String S;
    final public Rq1XmlTableColumn_String M;
    final public Rq1XmlTableColumn_String L;
    final public Rq1XmlTableColumn_String XL;
    final public Rq1XmlTableColumn_String NB_P;
    final public Rq1XmlTableColumn_String WEEKLY_HR;
    final public Rq1XmlTableColumn_String ID;
    final public Rq1XmlTableColumn_String XXL;
    final public Rq1XmlTableColumn_String MON_NB_P;
    final public Rq1XmlTableColumn_String FB_LD;
    final public Rq1XmlTableColumn_String EXC_ASSGN;
    final public Rq1XmlTableColumn_String ORD_ID;
    final public Rq1XmlTableColumn_String AVERAGE_WIP;

    public Rq1XmlTable_ClusterConfiguration() {

        addXmlColumn(NAME = new Rq1XmlTableColumn_String("Name", 15, "Name", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(S = new Rq1XmlTableColumn_String("S", 10, "S", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(M = new Rq1XmlTableColumn_String("M", 10, "M", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(L = new Rq1XmlTableColumn_String("L", 10, "L", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(XL = new Rq1XmlTableColumn_String("XL", 10, "XL", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(NB_P = new Rq1XmlTableColumn_String("No of Persons", 10, "NB_P", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(WEEKLY_HR = new Rq1XmlTableColumn_String("Weekly Hours", 10, "WEEKLY_HR", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(ID = new Rq1XmlTableColumn_String("ID", "ID", ColumnEncodingMethod.ATTRIBUTE));
        ID.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(XXL = new Rq1XmlTableColumn_String("XXL", 10, "XXL", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(MON_NB_P = new Rq1XmlTableColumn_String("MON_NB_P", "MON_NB_P", ColumnEncodingMethod.ATTRIBUTE));
        MON_NB_P.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(FB_LD = new Rq1XmlTableColumn_String("FB_LD", "FB_LD", ColumnEncodingMethod.ATTRIBUTE));
        FB_LD.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(EXC_ASSGN = new Rq1XmlTableColumn_String("Exc_Assgn", "Exc_Assgn", ColumnEncodingMethod.ATTRIBUTE));
        EXC_ASSGN.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(ORD_ID = new Rq1XmlTableColumn_String("ORD_ID", "ORD_ID", ColumnEncodingMethod.ATTRIBUTE));
        ORD_ID.setVisibility(Visibility.ALWAYS_HIDDEN);
        addXmlColumn(AVERAGE_WIP = new Rq1XmlTableColumn_String("AVG_WIP", 10, "AVG_WIP", ColumnEncodingMethod.ATTRIBUTE));

        ID.setOptional();
        XXL.setOptional();
        MON_NB_P.setOptional();
        EXC_ASSGN.setOptional();
        FB_LD.setOptional();
        ORD_ID.setOptional();
        AVERAGE_WIP.setOptional();
    }
}
