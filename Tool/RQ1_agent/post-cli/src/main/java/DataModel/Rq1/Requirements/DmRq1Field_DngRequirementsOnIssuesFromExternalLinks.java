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
import DataModel.DmMappedElementListField_ReadOnlyI;
import DataModel.Rq1.Records.DmRq1BaseElement;
import DataModel.Rq1.Records.DmRq1ExternalLink;
import DataModel.Rq1.Records.DmRq1ExternalLink.LinkState;
import DataModel.Rq1.Records.DmRq1ExternalLink.TargetSystem;
import DataModel.DmMappedElement;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import util.SafeArrayList;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Field_DngRequirementsOnIssuesFromExternalLinks extends DmField implements DmMappedElementListField_ReadOnlyI<DmRq1ExternalLink, DmElementI>, DmElementI.ChangeListener {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DmRq1Field_DngRequirementsOnIssuesFromExternalLinks.class.getCanonicalName());

    final private DmRq1BaseElement parentElement;

    private SafeArrayList<DmMappedElement<DmRq1ExternalLink, DmElementI>> content = null;

    public DmRq1Field_DngRequirementsOnIssuesFromExternalLinks(DmRq1BaseElement parentElement, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (parentElement != null);

        this.parentElement = parentElement;
    }

    @Override
    public synchronized List<DmMappedElement<DmRq1ExternalLink, DmElementI>> getElementList() {
        if (content == null) {
            SafeArrayList<DmMappedElement<DmRq1ExternalLink, DmElementI>> newContent = new SafeArrayList<>();
            for (DmRq1ExternalLink externalLink : parentElement.HAS_EXTERNAL_LINKS.getElementList()) {
                if (LinkState.getState(externalLink.LINK_STATE) == LinkState.ACTIVE) {
                    if (externalLink.getTargetSystem() == TargetSystem.DNG) {
                        LOGGER.warning(externalLink.getId() + " -> " + externalLink.TARGET_URL.getValueAsText());
                        DmElementI externalElement = externalLink.EXTERNAL_LINK.getElement();
                        if (externalElement != null) {
                            newContent.add(new DmMappedElement<DmRq1ExternalLink, DmElementI>(externalLink, externalElement));
                        }
                    }
                }
            }
            content = newContent;
        }
        return (content.getImmutableList());
    }

    public Set<String> getLinkTypesAsString() {
        Set<String> result = new TreeSet<>();
        if (content != null) {
            if (content.isEmpty() == false) {
                result.add(DmRq1LinkToRequirement_Type.NONE.getLinkTypeName());
            }
        } else {
            if (parentElement.HAS_EXTERNAL_LINKS.getElementList().isEmpty() == false) {
                result.add(DmRq1LinkToRequirement_Type.NONE.getLinkTypeName());
            }
        }
        return (result);
    }

    @Override
    public boolean isReadOnly() {
        return (true);
    }

    @Override
    public void reload() {
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

}
