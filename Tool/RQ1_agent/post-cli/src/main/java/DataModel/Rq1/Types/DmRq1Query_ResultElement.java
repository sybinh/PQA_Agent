/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.DmElement;
import DataModel.DmElementI;
import DataModel.DmElementListField_ReadOnlyI;
import DataModel.DmElementListField_ReadOnly_FilteredByClass;
import DataModel.DmValueField_Text;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1WorkItem;
import UiSupport.UiTreeViewRootElementI;

/**
 *
 * @author THS83WI
 */
public class DmRq1Query_ResultElement extends DmElement implements UiTreeViewRootElementI {

    final public DmElementListField_ReadOnlyI<? extends DmElementI> ALL_ELEMENTS;
    final public DmValueField_Text INPUT_PLAINTEXT;

    final public DmElementListField_ReadOnlyI<DmRq1Issue> ISSUES;
    final public DmElementListField_ReadOnlyI<DmRq1Release> RELEASES;
    final public DmElementListField_ReadOnlyI<DmRq1WorkItem> WORKITEMS;
    final public DmElementListField_ReadOnlyI<DmRq1Project> PROJECTS;

    public DmRq1Query_ResultElement(DmElementListField_ReadOnlyI<? extends DmElementI> listField, String plaintext) {
        super("Search by RQ1-ID Result");
        assert (listField != null);
        assert (plaintext != null);
        addField(INPUT_PLAINTEXT = new DmValueField_Text("Search Text", plaintext));
        addField(ALL_ELEMENTS = listField);

        addField(ISSUES = new DmElementListField_ReadOnly_FilteredByClass<DmRq1Issue>("Issues", listField, DmRq1Issue.class));
        addField(RELEASES = new DmElementListField_ReadOnly_FilteredByClass<DmRq1Release>("Releases", listField, DmRq1Release.class));
        addField(WORKITEMS = new DmElementListField_ReadOnly_FilteredByClass<DmRq1WorkItem>("Workitems", listField, DmRq1WorkItem.class));
        addField(PROJECTS = new DmElementListField_ReadOnly_FilteredByClass<DmRq1Project>("Projects", listField, DmRq1Project.class));
    }

    @Override
    public String getTitle() {
        return "RQ1-IDs";
    }

    @Override
    public String getViewTitle() {
        return getTitle();
    }

    @Override
    public void reload() {
        // not needed now
    }

    @Override
    public String getId() {
        return ("AllElements");
    }
}
