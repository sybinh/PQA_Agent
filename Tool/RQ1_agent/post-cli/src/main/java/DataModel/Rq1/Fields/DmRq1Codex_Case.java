/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1WorkItem;
import DataModel.DmMappedElement;
import Rq1Cache.Fields.Interfaces.Rq1ListI;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Types.Rq1Reference;
import java.util.*;
import util.SafeArrayList;

/**
 *
 * @author cil83wi
 */
public class DmRq1Codex_Case extends DmRq1Field_ReferenceList<DmRq1WorkItem> {

    private SafeArrayList<DmRq1WorkItem> finalCodexCaseList = null;
    private final DmRq1Release parent;

    public <F extends Rq1FieldI<List<Rq1Reference>> & Rq1ListI> DmRq1Codex_Case(
            DmRq1Release parent, F rq1ReferenceListField, String nameForUserInterface) {
        super(rq1ReferenceListField, nameForUserInterface);
        this.parent = parent;
    }

    @Override
    public List<DmRq1WorkItem> getElementList() {
        if (finalCodexCaseList == null) {

            SafeArrayList<DmRq1WorkItem> codexCaseWIList = new SafeArrayList<>();

            for (DmMappedElement<DmRq1Irm, DmRq1Issue> issue : parent.MAPPED_ISSUES.getElementList()) {
                if (!issue.getMap().isCanceled()) {
                    for (DmRq1WorkItem issueWorkItem : issue.getTarget().WORKITEMS.getElementList()) {
                        if ("Codex Case".equals(issueWorkItem.TYPE.getValueAsText())
                                || "Codex Measure".equals(issueWorkItem.TYPE.getValueAsText())) {
                            codexCaseWIList.add(issueWorkItem);
                        }

                        if ("Misc".equals(issueWorkItem.TYPE.getValueAsText())) {
                            for (DmRq1WorkItem predecessor : issueWorkItem.PREDECESSORS.getElementList()) {
                                if ("Codex Case".equals(predecessor.TYPE.getValueAsText())) {
                                    codexCaseWIList.add(issueWorkItem);
                                    break;
                                }
                            }
                        }
                    }
                    for (DmRq1IssueFD issuefd : ((DmRq1IssueSW) issue.getTarget()).CHILDREN.getElementList()) {
                        for (DmRq1WorkItem fdWorkitem : issuefd.WORKITEMS.getElementList()) {
                            if ((("Codex Case".equals(fdWorkitem.TYPE.getValueAsText())
                                    || "Codex Measure".equals(fdWorkitem.TYPE.getValueAsText())))) {
                                codexCaseWIList.add(fdWorkitem);
                            }

                            if ("Misc".equals(fdWorkitem.TYPE.getValueAsText())) {
                                for (DmRq1WorkItem predecessor : fdWorkitem.PREDECESSORS.getElementList()) {
                                    if ("Codex Case".equals(predecessor.TYPE.getValueAsText())) {
                                        codexCaseWIList.add(fdWorkitem);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            finalCodexCaseList = codexCaseWIList;
            return finalCodexCaseList.getImmutableList();
        } else {
            return finalCodexCaseList.getImmutableList();
        }
    }

    @Override
    public void reload() {
        if (finalCodexCaseList != null) {
            finalCodexCaseList = null;
            ((Rq1ListI) dsField).reload();
        }
    }

    @Override
    public void changed(Rq1ListI changedElement) {
        finalCodexCaseList = null;
        super.changed(changedElement);
    }

    @Override
    public boolean isLoaded() {
        return ((finalCodexCaseList != null) || (dsField.isNull() == false));
    }

}
