/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import DataModel.DmConstantField_Text;
import DataModel.DmElement;
import DataModel.DmElementI;
import DataModel.DmValueFieldI_Text;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1Irm_Pst_IssueSw;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1Problem;
import java.util.logging.Logger;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1LinkToRequirement_OnIssueAndIrm extends DmElement {

    final private static Logger LOGGER = Logger.getLogger(DmRq1LinkToRequirement_OnIssueAndIrm.class.getCanonicalName());

    public enum Rq1ElementType {

        UNKNOWN("Unknown"),
        I_SW("I-SW"),
        I_FD("I-FD"),
        IRM("IRM"),
        PROBLEM("Problem");

        private final String type;

        private Rq1ElementType(String type) {
            this.type = type;

        }

        public String getTypeName() {
            return type;
        }

        static public Rq1ElementType getType(DmRq1Element rq1Element) {

            if (rq1Element instanceof DmRq1IssueSW) {
                return (I_SW);
            } else if (rq1Element instanceof DmRq1IssueFD) {
                return (I_FD);
            } else if (rq1Element instanceof DmRq1Irm_Pst_IssueSw) {
                return (IRM);
            } else if (rq1Element instanceof DmRq1Problem) {
                return (PROBLEM);
            } else {
                LOGGER.severe("Unexpected element class: " + rq1Element.getClass().getCanonicalName());
                return (UNKNOWN);
            }
        }

        @Override
        public String toString() {
            return (type.replaceAll("\\-", ""));
        }

    }

    final protected DmRq1Element rq1Element;
    final private DmRq1LinkToRequirement_Type linkType;

    final public DmValueFieldI_Text RQ1_ELEMENT_TYPE;
    final public DmValueFieldI_Text LINK_TYPE;
    final public DmValueFieldI_Text REQUIREMENT_ID;
    final public DmValueFieldI_Text REQUIREMENT_URL;
    final public DmValueFieldI_Text COMMENT;

    protected DmRq1LinkToRequirement_OnIssueAndIrm(DmRq1Element rq1Element, DmRq1LinkToRequirement_Type linkType, String requirementId, String requirementUrl, String comment) {
        super(linkType.getLinkTypeName());
        assert (rq1Element != null);
        assert (requirementId != null);
        assert (requirementUrl != null);

        this.rq1Element = rq1Element;
        this.linkType = linkType;

        addField(RQ1_ELEMENT_TYPE = new DmConstantField_Text("RQ1 Element Type", Rq1ElementType.getType(rq1Element).getTypeName()));
        addField(LINK_TYPE = new DmConstantField_Text("Link Type", linkType.getLinkTypeName()));
        addField(REQUIREMENT_ID = new DmConstantField_Text("Requirement ID", requirementId));
        addField(COMMENT = new DmConstantField_Text("Comment", comment != null ? comment : ""));
        addField(REQUIREMENT_URL = new DmConstantField_Text("Requirement URL", requirementUrl));
    }

    final public DmRq1Element getRq1Element() {
        return (rq1Element);
    }

    public String getRq1ElementType() {
        return (Rq1ElementType.getType(rq1Element).getTypeName());
    }

    public DmRq1LinkToRequirement_Type getLinkType() {
        return linkType;
    }

    public boolean isBackReferenceRequired() {
        return (linkType.isBackReferenceRequired());
    }

    abstract public DmElementI getRequirement();

    @Override
    public String getId() {
        return (REQUIREMENT_ID.getValue());
    }

    @Override
    public String getTitle() {
        return (REQUIREMENT_ID.getValue());
    }

    @Override
    public void reload() {

    }

    @Override
    public String toString() {
        return (Rq1ElementType.getType(rq1Element) + "-" + rq1Element.getId() + "-" + linkType + "-" + REQUIREMENT_ID.getValueAsText());
    }

}
