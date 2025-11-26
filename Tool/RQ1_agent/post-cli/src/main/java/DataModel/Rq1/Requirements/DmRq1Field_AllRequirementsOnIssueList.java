/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import DataModel.DmElementI;
import DataModel.DmElementListFieldI;
import DataModel.DmField;
import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.DmMappedElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_AllRequirementsOnIssueList extends DmField implements DmElementListFieldI<DmElementI>, DmFieldI.ChangeListener {

    final private DmRq1Field_ReferenceList<DmRq1IssueSW> issueField;
    private List<DmElementI> content = null;

    public DmRq1Field_AllRequirementsOnIssueList(DmRq1Field_ReferenceList<DmRq1IssueSW> issueField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (issueField != null);
        this.issueField = issueField;
    }

    @Override
    final synchronized public List<DmElementI> getElementList() {
        if (content == null) {
            content = new ArrayList<>();
            for (DmRq1IssueSW i_sw : issueField.getElementList()) {
                for (DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI> map : i_sw.MAPPED_REQUIREMENTS.getElementList()) {
                    DmElementI element = map.getTarget();
                    if (content.contains(element) == false) {
                        content.add(element);
                    }
                }
                i_sw.MAPPED_REQUIREMENTS.addChangeListener(this);
            }
            issueField.addChangeListener(this);
        }
        return (content);
    }

    @Override
    final public void reload() {
        content = null;
        fireFieldChanged();
    }

    @Override
    final public void changed(DmFieldI changedElement) {
        reload();
    }

    @Override
    public void addElement(DmElementI e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
