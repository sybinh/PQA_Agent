/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.Fields.DmPPTField_Reference;
import DataModel.PPT.Fields.DmPPTField_Reference_Cluster_InfoFcBc;
import DataModel.PPT.Fields.DmPPTField_Reference_Cluster_Lines;
import DataModel.PPT.Fields.DmPPTField_Reference_Cluster_MemberProjects;
import DataModel.PPT.Fields.DmPPTField_Reference_Cluster_InfoSysCo;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import util.EcvAppendedData;

/**
 *
 * @author moe83wi
 */
public class DmPPTCluster extends DmPPTRecord implements Comparable<DmPPTCluster> {

    private final DmPPTValueField_Text NAME;
    public final DmPPTField_Reference_Cluster_MemberProjects PPT_MEMBER_PROJECTS;
    public final DmPPTField_Reference<DmPPTPoolProject> PPT_POOL_PROJECT;
    public final DmPPTField_Reference_Cluster_Lines PPT_LINES;
    public final DmPPTField_Reference_Cluster_InfoSysCo SYSTEMKONSTANTEN_TIDIED;
    public final DmPPTField_Reference_Cluster_InfoFcBc FCBC_TIDIED;

    public DmPPTCluster(DmPPTPoolProject parentPoolProject, String name) {
        super("Data Model Pro Pla To Cluster");
        assert (name != null);
        assert (!name.isEmpty());
        addField(NAME = new DmPPTValueField_Text(this, name, "PPT Name"));
        addField(PPT_POOL_PROJECT = new DmPPTField_Reference<>(this, parentPoolProject, "ProPlaTo Pool Project"));
        addField(PPT_MEMBER_PROJECTS = new DmPPTField_Reference_Cluster_MemberProjects(this,
                this.PPT_POOL_PROJECT.getElement().PPT_CUSTOMER_PROJECT_MEMBERS, "ProPlaTo Member Projects"));
        addField(PPT_LINES = new DmPPTField_Reference_Cluster_Lines(this, "ProPlaTo Lines"));
        addField(SYSTEMKONSTANTEN_TIDIED = new DmPPTField_Reference_Cluster_InfoSysCo(this, "ProPlaTo Systemkonstanten tidied"));
        addField(FCBC_TIDIED = new DmPPTField_Reference_Cluster_InfoFcBc(this, "ProPlaTo FCBC tidied"));
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public String getTitle() {
        return this.NAME.getValueAsText();
    }

    @Override
    public String getId() {
        return this.NAME.getValueAsText();
    }

    @Override
    public EcvAppendedData getEcvAppendedData() {
        Map<String, String> appendedData = new HashMap<>();
        appendedData.put("Cluster", this.getTitle());
        return new EcvAppendedData(appendedData);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.NAME.getValue());
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
        final DmPPTCluster other = (DmPPTCluster) obj;
        if (!Objects.equals(this.NAME.getValue(), other.NAME.getValue())) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(DmPPTCluster o) {
        assert (o != null);
        return (getId().compareTo(o.getId()));
    }

}
