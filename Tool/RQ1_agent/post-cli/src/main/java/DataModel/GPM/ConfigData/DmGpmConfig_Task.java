/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData;

import Rq1Cache.Records.Rq1SubjectInterface;
import Rq1Data.Enumerations.ImpactCategory;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author GUG2WI
 */
public interface DmGpmConfig_Task {

    public enum Source {
        UNKNOWN("Unknown"),
        PLIB("PLib"),
        PROJECT("Project");

        private final String source;

        Source(String text) {
            source = text;
        }

        public String getTextForSource() {
            return (source);
        }

        @Override
        public String toString() {
            return source;
        }

        static public Source getSourceForText(String text) {
            for (Source s : values()) {
                if (s.source.equals(text)) {
                    return (s);
                }
            }

            if (Rq1SubjectInterface.isRq1Id(text) == true) {
                return (PROJECT);
            }
            return (UNKNOWN);
        }
    }

    public String getTaskId();

    public String getTaskName();

    public Source getSource();

    public String getProcessLink();

    public String getDescription();

    public String getPlibVersion();

    public EnumSet<ImpactCategory> getImpactCategory();

    public String getResponsibleRole();

    public Set<String> getLinkedMilestones();

    public int getTimeRelation();

    public String getEffort();

    public String getLinkToArtefact();

    public String getArtefactName();

    public String getEstimatedComment();
}
