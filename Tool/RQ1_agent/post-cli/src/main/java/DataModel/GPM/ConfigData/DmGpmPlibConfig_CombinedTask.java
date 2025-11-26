/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData;

import DataModel.GPM.ConfigData.Plib.DmGpmPlibConfig_Task;
import Rq1Data.Enumerations.ImpactCategory;
import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A task combined from a Plib task and an optional project specific delta.
 *
 * @author GUG2WI
 */
public class DmGpmPlibConfig_CombinedTask implements DmGpmConfig_Task {

    final private DmGpmPlibConfig_Task pLibTask;
    final private DmGpmProjectConfig_PlibTaskDelta pLibDelta;

    final private static Logger LOGGER = Logger.getLogger(DmGpmPlibConfig_CombinedTask.class.getCanonicalName());
    
    public DmGpmPlibConfig_CombinedTask(DmGpmPlibConfig_Task pLibTask, DmGpmProjectConfig_PlibTaskDelta pLibDelta) {
        assert (pLibTask != null);

        this.pLibTask = pLibTask;
        this.pLibDelta = pLibDelta;
    }

    @Override
    public Source getSource() {
        return pLibTask.getSource();
    }

    @Override
    public String getTaskName() {
        return pLibTask.getTaskName();
    }

    @Override
    public EnumSet<ImpactCategory> getImpactCategory() {
        return pLibTask.getImpactCategory();
    }

    @Override
    public String getResponsibleRole() {
        return (pLibDelta != null && pLibDelta.getResponsible()!= null && pLibDelta.getResponsible().isEmpty() == false) ? pLibDelta.getResponsible():  pLibTask.getResponsibleRole();
    }

    @Override
    public String getProcessLink() {
        return pLibTask.getProcessLink();
    }

    @Override
    public String getLinkToArtefact() {
        return pLibTask.getLinkToArtefact();
    }

    @Override
    public String getArtefactName() {
        return pLibTask.getArtefactName();
    }

    @Override
    public Set<String> getLinkedMilestones() {
        Set<String> result = new TreeSet<>(pLibTask.getLinkedMilestones());
        if (pLibDelta != null) {
            result.addAll(pLibDelta.getAddedMilestoneTypes());
            result.removeAll(pLibDelta.getRemovedMilestoneTypes());
        }
        return (result);
    }

    @Override
    public String getDescription() {
        return pLibTask.getDescription();
    }

    @Override
    public String getPlibVersion() {
        return pLibTask.getPlibVersion();
    }

    @Override
    public String getEffort() {
        return (pLibDelta != null && pLibDelta.getEffort() != null && pLibDelta.getEffort().isEmpty() == false) ? pLibDelta.getEffort() :  pLibTask.getEffort() ;
    }

    @Override
    public String getTaskId() {
        return pLibTask.getTaskId();
    }

    @Override
    public final int getTimeRelation() {
        try {
            return (pLibDelta != null && pLibDelta.getTimeRelation() != null && pLibDelta.getTimeRelation().isEmpty() == false) ? Integer.valueOf(pLibDelta.getTimeRelation()) : pLibTask.getTimeRelation();
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Time relation was not a valid integer " + e );
            return 0;
        }
        
    }

    @Override
    public String toString() {
        return (DmGpmPlibConfig_CombinedTask.class.getSimpleName() + "(" + getTaskId() + "," + getTaskName() + "," + getLinkedMilestones().toString() + ")");
    }

    @Override
    public String getEstimatedComment() {
        return (pLibDelta != null && pLibDelta.getEffort().equals(pLibTask.getEffort()) == false) ? "" : "Estimated centrally via Guided PjM";
    }

}
