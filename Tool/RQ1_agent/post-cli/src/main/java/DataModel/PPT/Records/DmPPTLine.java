/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.Fields.DmPPTField_Reference;
import DataModel.PPT.Fields.DmPPTField_Reference_Line_InfoFcBc;
import DataModel.PPT.Fields.DmPPTField_Reference_Line_InfoSyCo;
import DataModel.PPT.Fields.DmPPTField_Reference_Line_Release_CUPF_File_generation;
import DataModel.PPT.Fields.DmPPTField_Reference_Line_Release_PlanDatPSTListeSysConst;
import DataModel.PPT.Fields.DmPPTField_Reference_Line_Release_Roadmap;
import DataModel.PPT.Fields.DmPPTField_Reference_Line_Release_Roadmap_Without_Date;
import DataModel.PPT.Fields.DmPPTMap_Line_CustProj_PPT_Tags;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import util.EcvAppendedData;

/**
 *
 * @author moe83wi
 */
public class DmPPTLine extends DmPPTRecord implements Comparable<DmPPTLine> {

    private final DmPPTMap_Line_CustProj_PPT_Tags PPT_CUST_PROJ_TAGS;
    public final DmPPTField_Reference<DmPPTCustomerProject> BELONGS_TO_PROJECT;
    public final DmPPTValueField_Text NAME;
    public final DmPPTField_Reference_Line_Release_Roadmap PPT_RELEASES;
    public final DmPPTField_Reference_Line_Release_CUPF_File_generation PPT_CUPF_RELEASES;
    public final DmPPTField_Reference_Line_Release_Roadmap_Without_Date PPT_RELEASES_WITHOUT_DATE_LIMITATION;
    public final DmPPTField_Reference_Line_Release_PlanDatPSTListeSysConst PPT_RELEASES_PLANDAT_PST_SYSCONST;
    public final DmPPTField_Reference_Line_InfoSyCo SYSTEMKONSTANTEN_TIDIED;
    public final DmPPTField_Reference_Line_InfoFcBc FCBC_TIDIED;
    public final DmPPTValueField_Text FAHRZEUG;
    public final DmPPTValueField_Text MOTOR;
    public final DmPPTValueField_Text GETRIEBE;
    public final DmPPTValueField_Text ABGASSTANDARD;
    public final DmPPTValueField_Text EIGENSCHAFTEN;
    public final DmPPTValueField_Text HARDWARE;
    private final String clusterOutOfTags;

    public DmPPTLine(String name, DmPPTCustomerProject project) {
        this(name, project, "");
    }

    public DmPPTLine(String name, DmPPTCustomerProject project, String cluster) {
        super("ProPlaTo Schiene");
        assert (name != null);
        assert (name.isEmpty() == false);
        assert (project != null);
        clusterOutOfTags = cluster;
        //tree view
        addField(BELONGS_TO_PROJECT = new DmPPTField_Reference<>(this, project, "ProPlaTo Customer Project"));
        addField(PPT_RELEASES = new DmPPTField_Reference_Line_Release_Roadmap(this, project, "ProPlaTo Releases"));
        addField(PPT_CUPF_RELEASES = new DmPPTField_Reference_Line_Release_CUPF_File_generation(this, project, "ProPlaTo CUPF Releases"));
        addField(PPT_RELEASES_WITHOUT_DATE_LIMITATION = new DmPPTField_Reference_Line_Release_Roadmap_Without_Date(this, project, "ProPlaTo Releases Roadmap without date"));
        addField(PPT_RELEASES_PLANDAT_PST_SYSCONST = new DmPPTField_Reference_Line_Release_PlanDatPSTListeSysConst(this, "ProPlaTo Releases Planungsdated und PST Liste"));
        addField(SYSTEMKONSTANTEN_TIDIED = new DmPPTField_Reference_Line_InfoSyCo(this, "ProPlaTo Systemkonstanten tidied"));
        addField(FCBC_TIDIED = new DmPPTField_Reference_Line_InfoFcBc(this, "ProPlaTo FCBC tidied"));

        //ProPlaTo View
        addField(NAME = new DmPPTValueField_Text(this, name, "ProPlaTo: <Name>"));
        this.PPT_CUST_PROJ_TAGS = new DmPPTMap_Line_CustProj_PPT_Tags(this, "ProPlaTo CustProj Tags");
        addField(FAHRZEUG = new DmPPTValueField_Text(this, this.PPT_CUST_PROJ_TAGS.getElement(this.PPT_CUST_PROJ_TAGS.DESC.AUTOMOTIVE), "ProPlaTo CustProj: <Fahrzeug>"));
        addField(MOTOR = new DmPPTValueField_Text(this, this.PPT_CUST_PROJ_TAGS.getElement(this.PPT_CUST_PROJ_TAGS.DESC.ENGINE), "ProPlaTo CustProj: <Motor>"));
        addField(GETRIEBE = new DmPPTValueField_Text(this, this.PPT_CUST_PROJ_TAGS.getElement(this.PPT_CUST_PROJ_TAGS.DESC.TRANSMISSION), "ProPlaTo CustProj: <Getriebe>"));
        addField(ABGASSTANDARD = new DmPPTValueField_Text(this, this.PPT_CUST_PROJ_TAGS.getElement(this.PPT_CUST_PROJ_TAGS.DESC.EXHAUSTSTANDARD), "ProPlaTo CustProj: <Abgasstandard>"));
        addField(EIGENSCHAFTEN = new DmPPTValueField_Text(this, this.PPT_CUST_PROJ_TAGS.getElement(this.PPT_CUST_PROJ_TAGS.DESC.PROPERTIES), "ProPlaTo CustProj: <Eigenschaften>"));
        addField(HARDWARE = new DmPPTValueField_Text(this, this.PPT_CUST_PROJ_TAGS.getElement(this.PPT_CUST_PROJ_TAGS.DESC.HARDWARE), "ProPlaTo CustProj: <Hardware>"));
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

        if (BELONGS_TO_PROJECT.getElement() != null && BELONGS_TO_PROJECT.getElement().PPT_CLUSTER.getElement() != null) {
            appendedData.put("Cluster", BELONGS_TO_PROJECT.getElement().PPT_CLUSTER.getElement().getTitle());
        } else {
            appendedData.put("Cluster", "");
        }
        appendedData.put("Schiene", this.getTitle());
        appendedData.put("Projekt_ID", BELONGS_TO_PROJECT.getElement().getId());
        appendedData.put("Project_Leader", BELONGS_TO_PROJECT.getElement().RQ1_CUSTOMER_PROJECT.getElement().PROJECT_LEADER_FULLNAME.getValueAsText());

        return new EcvAppendedData(appendedData);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.NAME.getValue());
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
        final DmPPTLine other = (DmPPTLine) obj;
        if (!Objects.equals(this.NAME.getValue(), other.NAME.getValue())) {
            return false;
        }
        return true;
    }

    public String getClusterOfTags() {
        return this.clusterOutOfTags;
    }

    @Override
    public String toString() {
        return (NAME.getValueAsText());
    }

    @Override
    public int compareTo(DmPPTLine o) {
        assert (o != null);
        return (getId().compareTo(o.getId()));
    }

}
