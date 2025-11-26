/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache;

import Rq1Cache.Records.Rq1Irm_Bc_IssueFd;
import Rq1Cache.Records.Rq1Irm_Bc_IssueSw;
import Rq1Cache.Records.Rq1Irm_EcuRelease_IssueHw;
import Rq1Cache.Records.Rq1Irm_Fc_IssueFd;
import Rq1Cache.Records.Rq1Irm_ModRelease_IssueHw;
import Rq1Cache.Records.Rq1Irm_Pst_IssueSw;
import Rq1Cache.Records.Rq1Irm_Unknown_IssueSw;
import Rq1Cache.Records.Rq1LinkInterface;
import Rq1Cache.Records.Rq1Rrm_Bc_Fc;
import Rq1Cache.Records.Rq1Rrm_Ecu_Bundle;
import Rq1Cache.Records.Rq1Rrm_Ecu_Mod;
import Rq1Cache.Records.Rq1Rrm_Mod_Comp;
import Rq1Cache.Records.Rq1Rrm_Pst_Bc;
import Rq1Cache.Records.Rq1Rrm_Pst_Bundle;
import Rq1Cache.Records.Rq1Rrm_Unknown_Bc;
import Rq1Cache.Records.Rq1UserRole;
import ToolUsageLogger.ToolUsageLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gug2wi
 */
public enum Rq1LinkDescription implements Rq1RecordDescription {

    //
    // Software IRM
    //
    IRM_PVER_REL_ISSUE_SW(Rq1Irm_Pst_IssueSw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.PVER_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_SW),
    IRM_PVER_COLL_ISSUE_SW(Rq1Irm_Pst_IssueSw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.PVER_COLLECTION, "hasMappedIssue", Rq1NodeDescription.ISSUE_SW),
    //
    IRM_PVAR_REL_ISSUE_SW(Rq1Irm_Pst_IssueSw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.PVAR_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_SW),
    IRM_PVAR_COLL_ISSUE_SW(Rq1Irm_Pst_IssueSw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.PVAR_COLLECTION, "hasMappedIssue", Rq1NodeDescription.ISSUE_SW),
    //
    IRM_BC_REL_ISSUE_SW(Rq1Irm_Bc_IssueSw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.BC_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_SW),
    IRM_BC_COLL_ISSUE_SW(Rq1Irm_Bc_IssueSw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.BC_COLLECTION, "hasMappedIssue", Rq1NodeDescription.ISSUE_SW),
    //
    IRM_BC_REL_ISSUE_FD(Rq1Irm_Bc_IssueFd.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.BC_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_FD),
    IRM_BC_COLL_ISSUE_FD(Rq1Irm_Bc_IssueFd.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.BC_COLLECTION, "hasMappedIssue", Rq1NodeDescription.ISSUE_FD),
    //
    IRM_FC_REL_ISSUE_FD(Rq1Irm_Fc_IssueFd.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.FC_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_FD),
    IRM_FC_COL_ISSUE_FD(Rq1Irm_Fc_IssueFd.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.FC_COLLECTION, "hasMappedIssue", Rq1NodeDescription.ISSUE_FD),
    //
    IRM_UNKNOWN_ISSUE_SW(Rq1Irm_Unknown_IssueSw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.UNKNOWN_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_SW),
    //
    // Software RRM
    //
    RRM_UNKNOWN_BC(Rq1Rrm_Unknown_Bc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.UNKNOWN_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.BC_RELEASE),
    //
    RRM_PVER_REL_BC_REL(Rq1Rrm_Pst_Bc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.PVER_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.BC_RELEASE),
    RRM_PVER_COLL_BC_REL(Rq1Rrm_Pst_Bc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.PVER_COLLECTION, "hasMappedChildRelease", Rq1NodeDescription.BC_RELEASE),
    RRM_PVER_REL_BC_COLL(Rq1Rrm_Pst_Bc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.PVER_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.BC_COLLECTION),
    RRM_PVER_COLL_BC_COLL(Rq1Rrm_Pst_Bc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.PVER_COLLECTION, "hasMappedChildRelease", Rq1NodeDescription.BC_COLLECTION),
    //
    RRM_PVAR_REL_BC_REL(Rq1Rrm_Pst_Bc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.PVAR_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.BC_RELEASE),
    RRM_PVAR_COLL_BC_REL(Rq1Rrm_Pst_Bc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.PVAR_COLLECTION, "hasMappedChildRelease", Rq1NodeDescription.BC_RELEASE),
    RRM_PVAR_REL_BC_COLL(Rq1Rrm_Pst_Bc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.PVAR_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.BC_COLLECTION),
    RRM_PVAR_COLL_BC_COLL(Rq1Rrm_Pst_Bc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.PVAR_COLLECTION, "hasMappedChildRelease", Rq1NodeDescription.BC_COLLECTION),
    //
    RRM_BC_REL_FC_REL(Rq1Rrm_Bc_Fc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.BC_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.FC_RELEASE),
    RRM_BC_REL_FC_COLL(Rq1Rrm_Bc_Fc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.BC_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.FC_COLLECTION),
    RRM_BC_COLL_FC_REL(Rq1Rrm_Bc_Fc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.BC_COLLECTION, "hasMappedChildRelease", Rq1NodeDescription.FC_RELEASE),
    RRM_BC_COLL_FC_COL(Rq1Rrm_Bc_Fc.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.BC_COLLECTION, "hasMappedChildRelease", Rq1NodeDescription.FC_COLLECTION),
    //
    // Software RRM
    //
    RRM_ECU_MOD(Rq1Rrm_Ecu_Mod.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.HW_ECU_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.HW_MOD_RELEASE),
    RRM_MOD_COMP(Rq1Rrm_Mod_Comp.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.HW_MOD_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.HW_COMP_RELEASE),
    //    
    // Hardware RRM
    //
    RRM_ECU_BUNDLE(Rq1Rrm_Ecu_Bundle.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.HW_BUNDLE_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.HW_ECU_RELEASE),
    RRM_PVER_C_BUNDLE(Rq1Rrm_Pst_Bundle.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.HW_BUNDLE_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.PVER_COLLECTION),
    RRM_PVER_BUNDLE(Rq1Rrm_Pst_Bundle.class, Rq1RecordType.RRM, "", "hasMappedParentRelease", Rq1NodeDescription.HW_BUNDLE_RELEASE, "hasMappedChildRelease", Rq1NodeDescription.PVER_RELEASE),
    //
    // Hardware IRM
    //
    IRM_HWECU_I_HW_ECU(Rq1Irm_EcuRelease_IssueHw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.HW_ECU_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_HW_ECU),
    IRM_HWECU_I_HW_MOD(Rq1Irm_EcuRelease_IssueHw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.HW_ECU_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_HW_MOD),
    IRM_HWMOD_I_HW_MOD(Rq1Irm_ModRelease_IssueHw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.HW_MOD_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_HW_MOD),
    IRM_HWMOD_I_HW_ECU(Rq1Irm_ModRelease_IssueHw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.HW_MOD_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_HW_ECU),
    //
    // Mixed HW/SW IRM
    //
    IRM_PVER_ISSUE_HWECU(Rq1Irm_EcuRelease_IssueHw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.PVER_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_HW_ECU),
    IRM_PVER_ISSUE_HWMOD(Rq1Irm_EcuRelease_IssueHw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.PVER_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_HW_MOD),
    IRM_PVAR_ISSUE_HWECU(Rq1Irm_EcuRelease_IssueHw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.PVAR_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_HW_ECU),
    IRM_PVAR_ISSUE_HWMOD(Rq1Irm_EcuRelease_IssueHw.class, Rq1RecordType.IRM, "", "hasMappedRelease", Rq1NodeDescription.PVAR_RELEASE, "hasMappedIssue", Rq1NodeDescription.ISSUE_HW_MOD),
    //
    // User Roles
    //
    ROLE_USER_CUSTOMERPROJECT(Rq1UserRole.class, Rq1RecordType.USER_ROLE, "", "isProjectMember", Rq1NodeDescription.SOFTWARE_COSTUMER_PROJECT_LEAF, "LinkedUser", Rq1NodeDescription.USER),
    ROLE_USER_POOLPROJECT(Rq1UserRole.class, Rq1RecordType.USER_ROLE, "", "isProjectMember", Rq1NodeDescription.SOFTWARE_CUSTOMER_PROJECT_POOL, "LinkedUser", Rq1NodeDescription.USER),
    ROLE_USER_SW_REFERENCE_PROJECT_POOL(Rq1UserRole.class, Rq1RecordType.USER_ROLE, "", "isProjectMember", Rq1NodeDescription.SOFTWARE_REFERENCE_PROJECT_POOL, "LinkedUser", Rq1NodeDescription.USER),
    ROLE_USER_SW_REFERENCE_PROJECT_LEAF(Rq1UserRole.class, Rq1RecordType.USER_ROLE, "", "isProjectMember", Rq1NodeDescription.SOFTWARE_REFERENCE_PROJECT_LEAF, "LinkedUser", Rq1NodeDescription.USER),
    ROLE_USER_DEVELOPMENTPROJECT(Rq1UserRole.class, Rq1RecordType.USER_ROLE, "", "isProjectMember", Rq1NodeDescription.SOFTWARE_DEVELOPMENT_PROJECT, "LinkedUser", Rq1NodeDescription.USER),
    ROLE_USER_HW_CUSTOMER_PROJECT(Rq1UserRole.class, Rq1RecordType.USER_ROLE, "", "isProjectMember", Rq1NodeDescription.HARDWARE_COSTUMER_PROJECT_LEAF, "LinkedUser", Rq1NodeDescription.USER),
    ROLE_USER_HW_POOL_PROJECT(Rq1UserRole.class, Rq1RecordType.USER_ROLE, "", "isProjectMember", Rq1NodeDescription.HARDWARE_CUSTOMER_PROJECT_POOL, "LinkedUser", Rq1NodeDescription.USER);

    final private Rq1RecordType recordType;
    final private FixedRecordValue[] fixedRecordValues;
    final private String fieldNameSubjectA;
    final private String fieldNameSubjectB;
    final private Rq1NodeDescription subjectA;
    final private Rq1NodeDescription subjectB;
    final private Class<? extends Rq1LinkInterface> linkClass;

    Rq1LinkDescription(Class<? extends Rq1LinkInterface> linkClass, Rq1RecordType recordType, String fixedRecordValueString,
            String fieldNameSubjectA, Rq1NodeDescription subjectA, String fieldNameSubjectB, Rq1NodeDescription subjectB) {
        assert (linkClass != null);
        assert (recordType != null);
        assert (fixedRecordValueString != null);
        assert (subjectA != null);
        assert (subjectB != null);

        this.linkClass = linkClass;
        this.recordType = recordType;
        this.fixedRecordValues = Rq1RecordDescription.FixedRecordValue.create(fixedRecordValueString);
        this.fieldNameSubjectA = fieldNameSubjectA;
        this.fieldNameSubjectB = fieldNameSubjectB;
        this.subjectA = subjectA;
        this.subjectB = subjectB;

    }

    @Override
    final public Rq1RecordType getRecordType() {
        return (recordType);
    }

    final public String getFieldNameSubjectA() {
        return (fieldNameSubjectA);
    }

    final public String getFieldNameSubjectB() {
        return (fieldNameSubjectB);
    }

    final public Rq1NodeDescription getSubjectA() {
        return (subjectA);
    }

    final public Rq1NodeDescription getSubjectB() {
        return (subjectB);
    }

    @Override
    final public FixedRecordValue[] getFixedRecordValues() {
        return (fixedRecordValues);
    }

    final public Rq1LinkInterface createMap() {
        try {
            return (linkClass.getConstructor(Rq1LinkDescription.class).newInstance(this));
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex1) {
            try {
                return (linkClass.getDeclaredConstructor().newInstance());
            } catch (Exception ex) {
                Logger.getLogger(Rq1NodeDescription.class
                        .getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(Rq1NodeDescription.class.getName(), ex);
                throw (new Error(ex));
            }
        }
    }
}
