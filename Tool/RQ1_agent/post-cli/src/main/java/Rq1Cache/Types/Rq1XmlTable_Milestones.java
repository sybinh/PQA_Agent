/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Data.Enumerations.ExportCategory;
import util.EcvTableRow;

/**
 * Implements access to the milestones field in RQ1.
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_Milestones extends Rq1XmlTable {

    final public static Rq1XmlTable_Milestones DESC = new Rq1XmlTable_Milestones();

    final public Rq1XmlTableColumn_String TAG_NAME;
    final public Rq1XmlTableColumn_String MILESTONE_NAME;
    final public Rq1XmlTableColumn_Date DATE;
    final public Rq1XmlTableColumn_ComboBox EXPORT_CATEGORY;
    final public Rq1XmlTableColumn_ComboBox EXPORT_SCOPE;

    public Rq1XmlTable_Milestones() {

        addXmlColumn(TAG_NAME = new Rq1XmlTableColumn_String("Tag-Name", 40, ColumnEncodingMethod.TAG_NAME));
        addXmlColumn(MILESTONE_NAME = new Rq1XmlTableColumn_String("Name", 40, "name", ColumnEncodingMethod.ATTRIBUTE));
        MILESTONE_NAME.setOptional();
        addXmlColumn(DATE = new Rq1XmlTableColumn_Date("Date", 30, ColumnEncodingMethod.CONTENT));
        addXmlColumn(EXPORT_CATEGORY = new Rq1XmlTableColumn_ComboBox("Export-Category", ExportCategory.values(), "Category", ColumnEncodingMethod.ATTRIBUTE));
        EXPORT_CATEGORY.setOptional();
        addXmlColumn(EXPORT_SCOPE = new Rq1XmlTableColumn_ComboBox("Export-Scope", ExportCategory.values(), "Scope", ColumnEncodingMethod.ATTRIBUTE));
        EXPORT_SCOPE.setOptional();
    }

    @Override
    public boolean isRowValid(EcvTableRow row) {
        String tagName = TAG_NAME.getValue(row);
        return ((tagName != null) && (tagName.isEmpty() == false));
    }

}
