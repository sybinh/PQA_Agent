/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTAenderung;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Boolean_ISSUE_ASAM extends DmPPTValueField_Boolean {

    public DmPPTValueField_Boolean_ISSUE_ASAM(DmPPTAenderung parent, String nameForUserInterface) {
        super(parent, getAsamIssue(parent), nameForUserInterface);
    }

    private static Boolean getAsamIssue(DmPPTAenderung parent) {
        if (parent != null) {
            if (parent.RQ1_ISSUE_SW.getElement() != null
                    && parent.RQ1_ISSUE_SW.getElement().EXCHANGE_WORKFLOW != null
                    && parent.RQ1_ISSUE_SW.getElement().EXCHANGE_WORKFLOW.getValue().contains("VAG_ASAM")) {
                return true;
            }
        }
        return false;
    }
}
