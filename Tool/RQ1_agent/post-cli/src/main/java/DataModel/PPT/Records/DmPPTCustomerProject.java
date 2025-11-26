/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.Fields.DmPPTField_Reference;
import DataModel.PPT.Fields.DmPPTField_ReferenceList;
import DataModel.PPT.Fields.DmPPTField_Reference_CustProj_Line;
import DataModel.PPT.Fields.DmPPTField_Reference_CustomerProject_InfoFcBc;
import DataModel.PPT.Fields.DmPPTField_Reference_CustomerProject_InfoSysCo;
import DataModel.PPT.Fields.DmPPTField_Reference_PPT_CustProj_IssueSW;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Leaf;
import DataModel.Rq1.Records.DmRq1SoftwareProject;
import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Cache.Types.Rq1XmlTable_ProPlaToProjectPstRail;
import Rq1Data.Types.Rq1LineEcuProject;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import util.EcvAppendedData;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTCustomerProject extends DmPPTRecord implements Comparable<DmPPTCustomerProject> {

    /*RULE MANAGEMENT*/
    static final public RuleDescription moreThanOneClusterInCustomerProjectDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROPLATO),
            "More than one Cluster in Customer Project",
            "There is only one cluster per customer project allowed.");
    private PPTDataRule moreThanOneClusterInCustomerProjectRule = null;

    static final public RuleDescription noClusterInCustomerProjectDescpriton = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROPLATO),
            "No Cluster defined in PPT Line in Customer Project",
            "There hast to be a cluster defined, for each PPT Line given in a Customer Project");
    private PPTDataRule noClusterInCustomerProjectDescpritonRule = null;

    public final DmPPTField_Reference<DmRq1SwCustomerProject_Leaf> RQ1_CUSTOMER_PROJECT;
    public final DmPPTField_Reference<DmPPTCluster> PPT_CLUSTER;
    public final DmPPTField_Reference<DmPPTPoolProject> PPT_POOL;
    public final DmPPTField_ReferenceList<DmPPTLine> PPT_LINES;
    public final DmPPTField_ReferenceList<DmPPTAenderung_IssueSW> NOT_CANCELED_ISSUE_SW;
    public final DmPPTField_Reference_CustomerProject_InfoSysCo SYSTEMKONSTANTEN_TIDIED;
    public final DmPPTField_Reference_CustomerProject_InfoFcBc FCBC_TIDIED;
    public final Map<String, String> LINE_INTERNAL_TO_EXTERNAL;
    public final Map<String, List<String>> LINE_EXTERNAL_TO_INTERNAL;
    public final DmPPTValueField_Text LINE_MAPPING;
    public final DmPPTValueField_Text COMPANY_DATA_ID;

    public DmPPTCustomerProject(DmRq1SwCustomerProject_Leaf rq1CustomerProject, DmPPTPoolProject pool) {
        this(rq1CustomerProject, null, pool);
    }

    public DmPPTCustomerProject(DmRq1SwCustomerProject_Leaf rq1CustomerProject, DmPPTCluster cluster, DmPPTPoolProject pool) {
        super("Data Model Pro Pla To Customer Project");
        assert (pool != null);
        this.LINE_INTERNAL_TO_EXTERNAL = new HashMap<>();
        this.LINE_EXTERNAL_TO_INTERNAL = new HashMap<>();
        addField(RQ1_CUSTOMER_PROJECT = new DmPPTField_Reference<>(this, rq1CustomerProject, "RQ1 Customer Project"));
        //Check if there are more than one clusters defined in the rq1 project
        Set<String> proPlaToClusters = checkIfMoreThanOneCluster(rq1CustomerProject);
        if (proPlaToClusters.size() > 1) {
            StringBuilder s = new StringBuilder(40);
            s.append("There is more than one cluster defined in the given Customer Project (ID: " + rq1CustomerProject.getId() + " ) ");
            boolean first = true;
            for (String clust : proPlaToClusters) {
                if (!first) {
                    s.append(" , ");
                }
                s.append(clust);
                first = false;
            }
            this.addMarker(new Warning(getMoreThanOneClusterInCustomerProjectRule(), "More than one cluster defined Rule", s.toString()));
        }
        addField(PPT_CLUSTER = new DmPPTField_Reference<>(this, cluster, "ProPlaTo Cluster"));
        addField(PPT_POOL = new DmPPTField_Reference<>(this, pool, "ProPlaTo Pool Project"));
        addField(NOT_CANCELED_ISSUE_SW = new DmPPTField_Reference_PPT_CustProj_IssueSW(this,
                this.RQ1_CUSTOMER_PROJECT.getElement().NOT_CANCELED_ISSUE_SW, "ProPlaTo Issues SW"));
        addField(PPT_LINES = new DmPPTField_Reference_CustProj_Line(this, "ProPlaTo Schiene"));
        List<Rq1LineEcuProject> lines = rq1CustomerProject.PST_LINES_FROM_EXTERNAL_DESCRIPTION.getValue();
        for (Rq1LineEcuProject ecuLine : lines) {
            if (!this.LINE_INTERNAL_TO_EXTERNAL.containsKey(ecuLine.getInternalName())) {
                this.LINE_INTERNAL_TO_EXTERNAL.put(ecuLine.getInternalName(), ecuLine.getExternalName());
            }
            List<String> internalLines = new LinkedList<>();
            if (this.LINE_EXTERNAL_TO_INTERNAL.containsKey(ecuLine.getExternalName())) {
                internalLines = this.LINE_EXTERNAL_TO_INTERNAL.get(ecuLine.getExternalName());
            }
            internalLines.add(ecuLine.getInternalName());
            this.LINE_EXTERNAL_TO_INTERNAL.put(ecuLine.getExternalName(), internalLines);
        }
        String output = "";
        for (String key : LINE_INTERNAL_TO_EXTERNAL.keySet()) {
            output += "\n" + key + "=>" + LINE_INTERNAL_TO_EXTERNAL.get(key);
        }
        if (output.isEmpty()) {
            output = "no entries";
        }
        addField(LINE_MAPPING = new DmPPTValueField_Text(this, output, "PPT Line Mapping"));

        addField(COMPANY_DATA_ID = new DmPPTValueField_Text(this, rq1CustomerProject.COMPANY_DATA_ID.getValueAsText(), "PPT Company Data ID"));
        addField(SYSTEMKONSTANTEN_TIDIED = new DmPPTField_Reference_CustomerProject_InfoSysCo(this, "ProPlaTo Systemkonstanten tidied"));
        addField(FCBC_TIDIED = new DmPPTField_Reference_CustomerProject_InfoFcBc(this, "ProPlaTo FCBC tidied"));

    }

    private Set<String> checkIfMoreThanOneCluster(DmRq1SwCustomerProject_Leaf custProj) {
        Set<String> proPlaToClusters = new HashSet<>();
        EcvTableData data = custProj.PROPLATO_PSTRAILS.getValue();
        Rq1XmlTable_ProPlaToProjectPstRail desc = custProj.PROPLATO_PSTRAILS.getTableDescription();
        for (EcvTableRow row : data.getRows()) {
            //Line Handling
            if (row.getValueAt(desc.CLUSTER) == null) {
                StringBuilder s = new StringBuilder(40);
                s.append("There is a line information without Cluster in the given Customer Project");
                if (row.getValueAt(desc.RAIL) != null && row.getValueAt(desc.RAIL).toString() != null) {
                    s.append("\n Row information: ");
                    s.append(row.getValueAt(desc.RAIL).toString());
                }
                this.addMarker(new Warning(getNoClusterInCustomerProjectDescpritonRule(), "No Cluster defined in Line", s.toString()));
            } else if (row.getValueAt(desc.CLUSTER).toString() != null && !row.getValueAt(desc.CLUSTER).toString().isEmpty()) {
                if (!proPlaToClusters.contains(row.getValueAt(desc.CLUSTER).toString())) {
                    proPlaToClusters.add(row.getValueAt(desc.CLUSTER).toString());
                }
            }
        }
        return proPlaToClusters;
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCanceled() {
        return this.RQ1_CUSTOMER_PROJECT.getElement().isCanceled();
    }

    @Override
    public String getTitle() {
        return this.RQ1_CUSTOMER_PROJECT.getElement().getTitle();
    }

    @Override
    public String getId() {
        return this.RQ1_CUSTOMER_PROJECT.getElement().getId();
    }

    public DmRq1Field_Table<Rq1XmlTable_ProPlaToProjectPstRail> getPROPLATO_PSTRAILS() {
        return this.RQ1_CUSTOMER_PROJECT.getElement().PROPLATO_PSTRAILS;
    }

    public DmRq1Field_ReferenceList<DmRq1Pst> getPST_RELEASES() {
        return this.RQ1_CUSTOMER_PROJECT.getElement().PROPLATO_PST;
    }

    public boolean isInCluster(DmPPTCluster cluster) {
        if (this.RQ1_CUSTOMER_PROJECT.getElement().PROPLATO_PSTRAILS.getValue().isEmpty() == false) {
            EcvTableData data = this.RQ1_CUSTOMER_PROJECT.getElement().PROPLATO_PSTRAILS.getValue();
            Rq1XmlTable_ProPlaToProjectPstRail desc = this.RQ1_CUSTOMER_PROJECT.getElement().PROPLATO_PSTRAILS.getTableDescription();
            for (EcvTableRow row : data.getRows()) {
                if (row.getValueAt(desc.CLUSTER) != null && cluster.getTitle().equals(row.getValueAt(desc.CLUSTER).toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public DmRq1Field_ReferenceList<DmRq1Pst> getPST_RELEASES_ROADMAP() {
        return this.RQ1_CUSTOMER_PROJECT.getElement().PROPLATO_PST_ROADMAP;
    }

    public DmRq1Field_ReferenceList<DmRq1Pst> getPST_RELEASES_ROADMAP_WITHOUT_DATE() {
        return this.RQ1_CUSTOMER_PROJECT.getElement().PROPLATO_PST_ROADMAP_WITHOUT_DATE;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.RQ1_CUSTOMER_PROJECT.getElement().getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DmPPTCustomerProject other = (DmPPTCustomerProject) obj;
        return other.getId().equals(this.getId());
    }

    public String getProPlaToLineExternal(String line) {
        DmRq1SoftwareProject prj = this.RQ1_CUSTOMER_PROJECT.getElement();
        if (prj instanceof DmRq1SwCustomerProject_Leaf) {
            DmRq1SwCustomerProject_Leaf cprj = (DmRq1SwCustomerProject_Leaf) prj;
            List<Rq1LineEcuProject> lines = cprj.PST_LINES_FROM_EXTERNAL_DESCRIPTION.getValue();
            for (Rq1LineEcuProject ecuLine : lines) {
                if (ecuLine.getInternalName().equals(line)) {
                    return ecuLine.getExternalName();
                }
            }
        }
        return line;
    }

    @Override
    public EcvAppendedData getEcvAppendedData() {
        Map<String, String> appendedData = new HashMap<>();
        appendedData.put("Customer_Project_ID", this.getId());
        appendedData.put("Customer_Project_Title", this.getTitle());
        return new EcvAppendedData(appendedData);
    }

    private PPTDataRule getMoreThanOneClusterInCustomerProjectRule() {
        if (moreThanOneClusterInCustomerProjectRule == null) {
            moreThanOneClusterInCustomerProjectRule = new PPTDataRule(moreThanOneClusterInCustomerProjectDescription);
        }
        return (moreThanOneClusterInCustomerProjectRule);
    }

    private PPTDataRule getNoClusterInCustomerProjectDescpritonRule() {
        if (noClusterInCustomerProjectDescpritonRule == null) {
            noClusterInCustomerProjectDescpritonRule = new PPTDataRule(noClusterInCustomerProjectDescpriton);
        }
        return (noClusterInCustomerProjectDescpritonRule);
    }

    @Override
    public int compareTo(DmPPTCustomerProject o) {
        assert (o != null);
        return (getId().compareTo(o.getId()));
    }

}
