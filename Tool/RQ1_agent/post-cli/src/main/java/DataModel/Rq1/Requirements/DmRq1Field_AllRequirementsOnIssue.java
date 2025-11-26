/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import DataModel.DmElementI;
import DataModel.DmField;
import DataModel.DmMappedElementListFieldI;
import DataModel.Rq1.Records.DmRq1BaseElement;
import DataModel.Rq1.Records.DmRq1ExternalLink;
import DataModel.DmMappedElement;
import java.util.List;
import java.util.Set;
import util.SafeArrayList;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Field_AllRequirementsOnIssue extends DmField implements DmMappedElementListFieldI<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI>, DmElementI.ChangeListener {

    final private DmRq1BaseElement parentElement;
    final private DmRq1Field_DoorsRequirementsOnIssueFromTables doorsRequirements;
    final private DmRq1Field_DngRequirementsOnIssuesFromExternalLinks dngRequirements;

    private SafeArrayList<DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI>> content = null;

    public DmRq1Field_AllRequirementsOnIssue(DmRq1BaseElement parentElement, DmRq1Field_DoorsRequirementsOnIssueFromTables doorsRequirements, DmRq1Field_DngRequirementsOnIssuesFromExternalLinks dngRequirements, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (parentElement != null);
        assert (doorsRequirements != null);
        assert (dngRequirements != null);

        this.parentElement = parentElement;
        this.doorsRequirements = doorsRequirements;
        this.dngRequirements = dngRequirements;
    }

    @Override
    public List<DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI>> getElementList() {
        if (content == null) {
            SafeArrayList<DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI>> newContent = new SafeArrayList<>();
            for (DmMappedElement<DmRq1LinkToDoors_OnIssueAndIrm, DmElementI> doorsReq : doorsRequirements.getElementList()) {
                newContent.add(new DmMappedElement<>(doorsReq.getMap(), doorsReq.getTarget()));
            }
            for (DmMappedElement<DmRq1ExternalLink, DmElementI> dngReq : dngRequirements.getElementList()) {
                DmRq1LinkToDng_OnIssueAndIrm link = new DmRq1LinkToDng_OnIssueAndIrm(parentElement, dngReq.getMap());
                newContent.add(new DmMappedElement<>(link, dngReq.getTarget()));
            }
            content = newContent;
            parentElement.addChangeListener(this);
        }
        return (content.getImmutableList());
    }

    @Override
    public boolean isReadOnly() {
        return (true);
    }

    @Override
    public void reload() {
        doorsRequirements.reload();
        dngRequirements.reload();
        content = null;
        fireFieldChanged();
    }

    @Override
    public void changed(DmElementI changedElement) {
        reload();
    }

    @Override
    public boolean isLoaded() {
        return (content != null);
    }

    @Override
    public void addElement(DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI> e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Returns a list of link types as string to support the filtering and
     * grouping.
     *
     * The implementation is based on the data stored in the table fields. No
     * requirement is loaded from the database to calculate the result.
     *
     * Links with requirement 'no impact' are not added to the list.
     *
     * @return
     */
    public Set<String> getLinkTypesAsString() {
        Set<String> result = doorsRequirements.getLinkTypesAsString();
        result.addAll(dngRequirements.getLinkTypesAsString());
        return (result);
    }

}
