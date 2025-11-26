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
import DataModel.DmFieldI;
import DataModel.DmMappedElementListFieldI;
import DataModel.DmMappedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import util.EcvIdentityMapList;

/**
 *
 * @author gug2wi
 */
public abstract class DmRq1Field_AllRequirementsOnRelease extends DmField implements DmMappedElementListFieldI<DmRq1LinkToDoors_OnRelease, DmElementI>, DmFieldI.ChangeListener {

    private List<DmMappedElement<DmRq1LinkToDoors_OnRelease, DmElementI>> content = null;

    public DmRq1Field_AllRequirementsOnRelease(DmElementI parent, String nameForUserInterface) {
        super(nameForUserInterface);
    }

    @Override
    final public boolean isReadOnly() {
        return (true);
    }

    @Override
    final public List<DmMappedElement<DmRq1LinkToDoors_OnRelease, DmElementI>> getElementList() {
        if (content == null) {
            //
            // Create map with mapped requirements
            //
            EcvIdentityMapList<DmElementI, DmRq1LinkToRequirement_OnIssueAndIrm> accumulationMap = new EcvIdentityMapList<>();
            for (DmRq1LinkToRequirement_OnIssueAndIrm issueLink : getContent()) {
                accumulationMap.add(issueLink.getRequirement(), issueLink);
            }
            content = new ArrayList<>();
            //
            // Create link objects for each requirement
            //
            for (Map.Entry<DmElementI, List<DmRq1LinkToRequirement_OnIssueAndIrm>> accumulatedLinks : accumulationMap.entrySet()) {
                DmRq1LinkToDoors_OnRelease releaseLink = new DmRq1LinkToDoors_OnRelease(accumulatedLinks.getValue());
                content.add(new DmMappedElement<>(releaseLink, accumulatedLinks.getKey()));
            }
        }
        return (content);
    }

    /**
     * Returns the list of requirements mapped on all issues
     *
     * @return
     */
    protected abstract List<DmRq1LinkToRequirement_OnIssueAndIrm> getContent();

    @Override
    public boolean isLoaded() {
        return (content != null);
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
    public void addElement(DmMappedElement<DmRq1LinkToDoors_OnRelease, DmElementI> e) {
        throw new UnsupportedOperationException("Not supported for field " + getNameForUserInterface()); //To change body of generated methods, choose Tools | Templates.
    }

}
