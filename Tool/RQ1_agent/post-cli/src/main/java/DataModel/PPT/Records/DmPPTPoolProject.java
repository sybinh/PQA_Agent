/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.Ecv.Records.DmEcvBcLongNameTable;
import DataModel.Ecv.Records.DmEcvBcResponsibleTable;
import DataModel.Ecv.Records.DmEcvFcLongNameTable;
import DataModel.PPT.Fields.DmPPTField_Reference;
import DataModel.PPT.Fields.DmPPTField_Reference_PPT_IssueSW;
import DataModel.PPT.Fields.DmPPTField_Reference_PoolPrj_Cluster;
import DataModel.PPT.Fields.DmPPTField_Reference_PoolPrj_MemberPrj;
import DataModel.PPT.Fields.DmPPTField_Reference_PoolProject_InfoFcBc;
import DataModel.PPT.Fields.DmPPTField_Reference_PoolProject_InfoSysCo;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Pool;
import ToolUsageLogger.ToolUsageLogger;
import UiSupport.UiTreeViewRootElementI;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvAppendedData;

/**
 *
 * @author moe83wi
 */
public class DmPPTPoolProject extends DmPPTRecord implements UiTreeViewRootElementI {

    final private static Logger LOGGER = Logger.getLogger(DmPPTPoolProject.class.getCanonicalName());

    final private static String RQ1_RESSOURCE_PATH = "\\\\bosch.com\\dfsrb\\DfsDE\\DIV\\DGS\\08\\EC\\21_ECV\\OU\\DE\\97_Tools\\RQ1_Ressources";

    public final DmPPTField_Reference<DmRq1SwCustomerProject_Pool> RQ1_POOL_PROJECT;
    public final DmPPTField_Reference_PoolPrj_MemberPrj PPT_CUSTOMER_PROJECT_MEMBERS;
    public final DmPPTField_Reference_PPT_IssueSW NOT_CANCELED_ISSUE_SW;
    public final DmPPTField_Reference_PoolPrj_Cluster PPT_CLUSTER;
    public final DmPPTField_Reference_PoolProject_InfoSysCo SYSTEMKONSTANTEN_TIDIED;
    public final DmPPTField_Reference_PoolProject_InfoFcBc FCBC_TIDIED;

    private DmEcvBcLongNameTable bcLongNameTable = null;
    private DmEcvFcLongNameTable fcLongNameTable = null;
    private DmEcvBcResponsibleTable bcResponsibleTable = null;

    public DmPPTPoolProject(DmRq1SwCustomerProject_Pool poolProject) {
        super("Data Model Pro Pla To Pool Project");
        addField(RQ1_POOL_PROJECT = new DmPPTField_Reference<>(this, poolProject, "RQ1 Pool Project"));
        addField(PPT_CUSTOMER_PROJECT_MEMBERS = new DmPPTField_Reference_PoolPrj_MemberPrj(this,
                this.RQ1_POOL_PROJECT.getElement().POOL_PROJECT_MEMBERS_CUSTOMER_PROJECTS, "ProPlaTo Customer Project Members"));
        addField(NOT_CANCELED_ISSUE_SW = new DmPPTField_Reference_PPT_IssueSW(this,
                this.RQ1_POOL_PROJECT.getElement().NOT_CANCELED_ISSUE_SW, "ProPlaTo Issues SW"));
        addField(PPT_CLUSTER = new DmPPTField_Reference_PoolPrj_Cluster(this,
                this.RQ1_POOL_PROJECT.getElement().POOL_PROJECT_MEMBERS_CUSTOMER_PROJECTS, "ProPlaTo Cluster"));

        addField(SYSTEMKONSTANTEN_TIDIED = new DmPPTField_Reference_PoolProject_InfoSysCo(this, "ProPlaTo Systemkonstanten tidied"));
        addField(FCBC_TIDIED = new DmPPTField_Reference_PoolProject_InfoFcBc(this, "ProPlaTo FCBC tidied"));
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCanceled() {
        return this.RQ1_POOL_PROJECT.getElement().isCanceled();
    }

    @Override
    public String getTitle() {
        return this.RQ1_POOL_PROJECT.getElement().getTitle();
    }

    @Override
    public String getId() {
        return this.RQ1_POOL_PROJECT.getElement().getId();
    }

    @Override
    public String getViewTitle() {
        return ("ProPlaTo-View for Pool Project: " + getId() + " - " + getTitle());
    }

    public DmEcvBcLongNameTable getBcLongnameTable() {
        if (bcLongNameTable == null) {
            bcLongNameTable = new DmEcvBcLongNameTable();
            File bcLongnames = new File(RQ1_RESSOURCE_PATH + "\\BC_Longnames.xml");
            try {
                bcLongNameTable.loadFromFile(bcLongnames);
            } catch (FileNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Load BC longnames", ex);
                ToolUsageLogger.logError(DmPPTPoolProject.class.getCanonicalName(), ex);
                throw new Error("File not Found " + ex.toString());
            }
        }
        return bcLongNameTable;
    }

    public DmEcvFcLongNameTable getFcLongnameTable() {
        if (fcLongNameTable == null) {
            fcLongNameTable = new DmEcvFcLongNameTable();
            File fcLongnames = new File(RQ1_RESSOURCE_PATH + "\\FC_Longnames.xml");
            try {
                fcLongNameTable.loadFromFile(fcLongnames);
            } catch (FileNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Load FC longnames", ex);
                ToolUsageLogger.logError(DmPPTPoolProject.class.getCanonicalName(), ex);
                throw new Error("File not Found " + ex.toString());
            }
        }
        return fcLongNameTable;
    }

    public DmEcvBcResponsibleTable getBcResponsibleTable() {
        if (bcResponsibleTable == null) {
            bcResponsibleTable = new DmEcvBcResponsibleTable();
            File bcResponsible = new File(RQ1_RESSOURCE_PATH + "\\BC_Verantwortlichkeit.xml");
            try {
                bcResponsibleTable.loadFromFile(bcResponsible);
            } catch (FileNotFoundException ex) {
                LOGGER.log(Level.SEVERE, "Load FC longnames", ex);
                ToolUsageLogger.logError(DmPPTPoolProject.class.getCanonicalName(), ex);
                throw new Error("File not Found " + ex.toString());
            }
        }
        return bcResponsibleTable;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.RQ1_POOL_PROJECT.getElement().getId());
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
        final DmPPTPoolProject other = (DmPPTPoolProject) obj;
        return this.getId().equals(other.getId());
    }

    @Override
    public EcvAppendedData getEcvAppendedData() {
        Map<String, String> appendedData = new HashMap<>();
        return new EcvAppendedData(appendedData);
    }

}
