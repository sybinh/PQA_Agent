/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Requirements.DmRq1Field_AllRequirementsOnIssueList;
import DataModel.Rq1.Fields.DmRq1Field_ExternalDescription_PstLines;
import DataModel.Rq1.Fields.DmRq1Field_ExternalDescription_Text;
import DataModel.Rq1.Fields.DmRq1Field_PstLinesEcuProjectComplete;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_AccountNumberFormat;
import DataModel.Rq1.Monitoring.Rule_Ecu_LumpensammlerExists;
import DataModel.Rq1.Monitoring.Rule_Ecu_PstLines;
import Ipe.Annotations.IpeFactoryConstructor;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Records.Rq1SwCustomerProject_Leaf;
import Rq1Cache.Types.Rq1XmlTable_ProPlaToProjectPstRail;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsMilestones;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsTypes;
import Rq1Data.Types.Rq1LineEcuProject;
import java.util.*;

/**
 *
 * @author GUG2WI
 */
public class DmRq1SwCustomerProject_Leaf extends DmRq1SwCustomerProject implements DmRq1LeafProjectI{

    final public DmRq1Field_Text EXTERNAL_ID;
    final public DmRq1Field_Text EXTERNAL_TITLE;
    final public DmRq1Field_Text EXTERNAL_DESCRIPTION;
    final public DmRq1Field_ExternalDescription_PstLines PST_LINES_FROM_EXTERNAL_DESCRIPTION;
    final public DmRq1Field_PstLinesEcuProjectComplete PST_LINES;
    final public DmRq1Field_ReferenceList<DmRq1IssueSW> ALL_ISSUE_SW_ON_PST;
    final public DmRq1Field_ReferenceList<DmRq1IssueFD> ALL_ISSUE_FD_ON_PST;
    final public DmRq1Field_ReferenceList<DmRq1Bc> ALL_BC_ON_PST;
    final public DmRq1Field_Table<Rq1XmlTable_ProPlaToProjectPstRail> PROPLATO_PSTRAILS;
    final public DmRq1Field_AllRequirementsOnIssueList ALL_REQUIREMENTS_ON_ALL_ISSUE_SW_ON_PST;

    final public DmRq1Field_Table<Rq1XmlTable_SwMetricsTypes> SW_METRICS_TYPES;
    final public DmRq1Field_Table<Rq1XmlTable_SwMetricsMilestones> SW_METRICS_MILESTONES;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1SwCustomerProject_Leaf(Rq1SwCustomerProject_Leaf rq1Project) {
        super("CustPrj", rq1Project);

        final DmRq1SwCustomerProject_Leaf myThis = this;

        //
        // Create and add fields
        //
        addField(PROPLATO_PSTRAILS = new DmRq1Field_Table<>(this, rq1Project.PROPLATO_PSTRAILS, "ProPlaTo PST Schienen"));

        addField(EXTERNAL_ID = new DmRq1Field_Text(this, rq1Project.EXTERNAL_ID, "External ID"));
        addField(EXTERNAL_TITLE = new DmRq1Field_Text(this, rq1Project.EXTERNAL_TITLE, "External Title"));
        addField(EXTERNAL_DESCRIPTION = new DmRq1Field_ExternalDescription_Text(this, rq1Project.EXTERNAL_DESCRIPTION, "External Description"));

        addField((PST_LINES_FROM_EXTERNAL_DESCRIPTION = new DmRq1Field_ExternalDescription_PstLines(this, rq1Project.EXTERNAL_DESCRIPTION, "PST lines from External Description")));
        addField((PST_LINES = new DmRq1Field_PstLinesEcuProjectComplete(this, PST_LINES_FROM_EXTERNAL_DESCRIPTION, rq1Project.EXTERNAL_ID, "PST Lines")));

        addField(ALL_ISSUE_SW_ON_PST = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_ISSUE_SW_ON_PST, "All I-SW from all Releases"));
        addField(ALL_ISSUE_FD_ON_PST = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_ISSUE_FD_ON_PST, "All I-FD from all Releases"));

        addField(ALL_BC_ON_PST = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_BC_ON_PST, "All BC from all Releases"));

        addField(ALL_REQUIREMENTS_ON_ALL_ISSUE_SW_ON_PST = new DmRq1Field_AllRequirementsOnIssueList(ALL_ISSUE_SW_ON_PST, "All Requirements of I-SW from all Releases"));

        addField(SW_METRICS_TYPES = new DmRq1Field_Table<>(this, rq1Project.SW_METRICS_TYPES, "SW-Metric Types"));
        addField(SW_METRICS_MILESTONES = new DmRq1Field_Table<>(this, rq1Project.SW_METRICS_MILESTONES, "SW-Metric Milestones"));

        //
        // Create and add rules
        //
        addRule(new Rule_Ecu_LumpensammlerExists(this));
        addRule(new Rule_Ecu_PstLines(this));
        //     addRule(new Rule_SwCustomerProject_ProPlaTo(this));
        addRule(new Rule_AccountNumberFormat(this));

        //
        // Add switchable rule groups
        //
        addSwitchableGroup(EnumSet.of(RuleExecutionGroup.PROJECT));
        addSwitchableGroup(EnumSet.of(RuleExecutionGroup.PROPLATO));
    }

    @Override
    protected void setSwitchableGroupActiveForSubElements(RuleExecutionGroup group, boolean activate) {
        assert (group != null);
        for (DmRq1Pst release : this.ALL_PST.getElementList()) {
            release.setSwitchableGroupActive(group, activate);
        }
    }

    public List<Rq1LineEcuProject> getPstLinesFromExternalDescription() {
        return (PST_LINES_FROM_EXTERNAL_DESCRIPTION.getValue());
    }

    public List<Rq1LineEcuProject> getPstLines() {
        return (PST_LINES.getValue());
    }

    public String getExternalID() {
        return (EXTERNAL_ID.getValue());
    }

    //--------------------------------------------------------------------------
    //
    // VW specific support for derivatives
    //
    //--------------------------------------------------------------------------
    @Override
    public Collection<String> getDerivativeNames() {

        if (Customer.getCustomer(this) == Customer.VW_GROUP) {
            List<String> internalNames = new ArrayList<>();
            for (Rq1LineEcuProject data : PST_LINES.getValue()) {
                internalNames.add(data.getInternalName());
            }
            return internalNames;
        }

        return (super.getDerivativeNames());
    }

    //--------------------------------------------------------------------------
    //
    // Support for access of ProPlaTo data
    //
    //--------------------------------------------------------------------------
    private List<Rq1XmlTable_ProPlaToProjectPstRail.Record> getAllProPlaTo() {
        return (Rq1XmlTable_ProPlaToProjectPstRail.toRecordList(PROPLATO_PSTRAILS.getValue()));
    }

    public Set<String> getAll_ProPlaTo_AbgasStandard() {

        Set<String> result = new TreeSet<>();

        for (Rq1XmlTable_ProPlaToProjectPstRail.Record r : getAllProPlaTo()) {
            result.addAll(r.getAbgasStandard());
        }

        return (result);
    }

    public Set<String> getGroupsProPlaToAbgasStandard() {
        Set<String> result = new TreeSet<>();
        for (Rq1XmlTable_ProPlaToProjectPstRail.Record r : getAllProPlaTo()) {
            List<String> abgasStandardList = r.getAbgasStandard();
            if (abgasStandardList.isEmpty() == true) {
                result.add("<Empty>");
            } else {
                for (String abgasStandard : abgasStandardList) {
                    if ((abgasStandard != null) && (abgasStandard.trim().isEmpty() == false)) {
                        result.add(abgasStandard);
                    } else {
                        result.add("<Empty>");
                    }
                }
            }

        }
        return (result);
    }

    public Set<String> getAll_ProPlaTo_Hardware() {

        Set<String> result = new TreeSet<>();

        for (Rq1XmlTable_ProPlaToProjectPstRail.Record r : getAllProPlaTo()) {
            if (r.getHardware() != null) {
                result.add(r.getHardware());
            }
        }

        return (result);
    }

    public Set<String> getGroupsProPlaToHardware() {
        Set<String> result = new TreeSet<>();
        for (Rq1XmlTable_ProPlaToProjectPstRail.Record r : getAllProPlaTo()) {
            String hardware = r.getHardware();
            if ((hardware != null) && (hardware.trim().isEmpty() == false)) {
                result.add(r.getHardware());
            } else {
                result.add("<Empty>");
            }
        }
        return (result);
    }

    public Set<String> getAll_ProPlaTo_Cluster() {
        Set<String> result = new TreeSet<>();
        for (Rq1XmlTable_ProPlaToProjectPstRail.Record r : getAllProPlaTo()) {
            String cluster = r.getCluster();
            if ((cluster != null) && (cluster.trim().isEmpty() == false)) {
                result.add(cluster.trim());
            }
        }
        return (result);
    }

    public Set<String> getGroupsForProPlaToCluster() {
        Set<String> result = new TreeSet<>();
        for (Rq1XmlTable_ProPlaToProjectPstRail.Record r : getAllProPlaTo()) {
            String cluster = r.getCluster();
            if ((cluster != null) && (cluster.trim().isEmpty() == false)) {
                result.add(cluster.trim());
            } else {
                result.add("<Empty>");
            }
        }
        return (result);
    }

    public Set<String> getAll_ProPlaTo_Schiene() {
        Set<String> result = new TreeSet<>();
        for (Rq1XmlTable_ProPlaToProjectPstRail.Record record : getAllProPlaTo()) {
            String schiene = record.getSchiene();
            if ((schiene != null) && (schiene.trim().isEmpty() == false)) {
                result.add(schiene.trim());
            }
        }
        return (result);
    }

    public Set<String> getGroupsForProPlaToSchiene() {
        Set<String> result = new TreeSet<>();
        for (Rq1XmlTable_ProPlaToProjectPstRail.Record record : getAllProPlaTo()) {
            String schiene = record.getSchiene();
            if ((schiene != null) && (schiene.trim().isEmpty() == false)) {
                result.add(schiene.trim());
            } else {
                result.add("<Empty");
            }
        }
        return (result);
    }

    public String getClusterForProgrammstandsschiene(String programmstandsschiene) {
        assert (programmstandsschiene != null);
        assert (programmstandsschiene.isEmpty() == false);

        for (Rq1XmlTable_ProPlaToProjectPstRail.Record r : getAllProPlaTo()) {

            if (programmstandsschiene.equals(r.getSchiene()) == true) {
                String cluster = r.getCluster();
                if ((cluster != null) && (cluster.trim().isEmpty() == false)) {
                    return (cluster.trim());
                } else {
                    return (null);
                }
            }
        }
        return (null);
    }

}
