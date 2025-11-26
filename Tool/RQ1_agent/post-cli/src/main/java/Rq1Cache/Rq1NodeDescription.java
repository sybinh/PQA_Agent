/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache;

import Rq1Cache.Fields.Interfaces.Rq1QueryI;
import Rq1Cache.Records.Rq1Attachment;
import Rq1Cache.Records.Rq1AttachmentMapping;
import Rq1Cache.Records.Rq1BcCollection;
import Rq1Cache.Records.Rq1BcRelease;
import Rq1Cache.Records.Rq1BundleRelease;
import Rq1Cache.Records.Rq1CompRelease;
import Rq1Cache.Records.Rq1Contact;
import Rq1Cache.Records.Rq1EcuRelease;
import Rq1Cache.Records.Rq1ExternalLink;
import Rq1Cache.Records.Rq1FcCollection;
import Rq1Cache.Records.Rq1FcRelease;
import Rq1Cache.Records.Rq1HistoryLog;
import Rq1Cache.Records.Rq1HwCustomerProject_Leaf;
import Rq1Cache.Records.Rq1HwCustomerProject_Pool;
import Rq1Cache.Records.Rq1HwPlatformProject_Leaf;
import Rq1Cache.Records.Rq1IssueHwEcu;
import Rq1Cache.Records.Rq1IssueFD;
import Rq1Cache.Records.Rq1IssueMod;
import Rq1Cache.Records.Rq1IssueSW;
import Rq1Cache.Records.Rq1MetadataChoiceList;
import Rq1Cache.Records.Rq1ModRelease;
import Rq1Cache.Records.Rq1NodeInterface;
import Rq1Cache.Records.Rq1Problem;
import Rq1Cache.Records.Rq1Milestone;
import Rq1Cache.Records.Rq1PvarCollection;
import Rq1Cache.Records.Rq1PvarRelease;
import Rq1Cache.Records.Rq1PverCollection;
import Rq1Cache.Records.Rq1PverRelease;
import Rq1Cache.Records.Rq1SwCustomerProject_Leaf;
import Rq1Cache.Records.Rq1SwCustomerProject_Pool;
import Rq1Cache.Records.Rq1SwDevelopmentProject;
import Rq1Cache.Records.Rq1SwReferenceProject_Leaf;
import Rq1Cache.Records.Rq1SwReferenceProject_Pool;
import Rq1Cache.Records.Rq1UnknownIssue;
import Rq1Cache.Records.Rq1UnknownProject;
import Rq1Cache.Records.Rq1UnknownRelease;
import Rq1Cache.Records.Rq1User;
import Rq1Cache.Records.Rq1WorkItem_Any;
import Rq1Cache.Records.Rq1WorkItem_Bc;
import Rq1Cache.Records.Rq1WorkItem_Bundle;
import Rq1Cache.Records.Rq1WorkItem_CustPrj;
import Rq1Cache.Records.Rq1WorkItem_DevPrj;
import Rq1Cache.Records.Rq1WorkItem_Fc;
import Rq1Cache.Records.Rq1WorkItem_HwComp;
import Rq1Cache.Records.Rq1WorkItem_HwCustomerProject;
import Rq1Cache.Records.Rq1WorkItem_HwEcu;
import Rq1Cache.Records.Rq1WorkItem_HwMod;
import Rq1Cache.Records.Rq1WorkItem_HwPlatformProject;
import Rq1Cache.Records.Rq1WorkItem_IssueFD;
import Rq1Cache.Records.Rq1WorkItem_IssueHwEcu;
import Rq1Cache.Records.Rq1WorkItem_IssueHwMod;
import Rq1Cache.Records.Rq1WorkItem_IssueSW;
import Rq1Cache.Records.Rq1WorkItem_Problem;
import Rq1Cache.Records.Rq1WorkItem_Pvar;
import Rq1Cache.Records.Rq1WorkItem_Pver;
import Rq1Cache.Records.Rq1WorkItem_RefPrj;
import ToolUsageLogger.ToolUsageLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gug2wi
 */
public enum Rq1NodeDescription implements Rq1RecordDescription {

    //
    // Software Projects
    //
    SOFTWARE_COSTUMER_PROJECT_LEAF(Rq1SwCustomerProject_Leaf.class, Rq1RecordType.PROJECT, "Type=CustPrj,Domain=Software,IsPoolProject=No"),
    SOFTWARE_CUSTOMER_PROJECT_POOL(Rq1SwCustomerProject_Pool.class, Rq1RecordType.PROJECT, "Type=CustPrj,Domain=Software,IsPoolProject=Yes"),
    SOFTWARE_REFERENCE_PROJECT_LEAF(Rq1SwReferenceProject_Leaf.class, Rq1RecordType.PROJECT, "Type=RefPrj,Domain=Software,IsPoolProject=No"),
    SOFTWARE_REFERENCE_PROJECT_POOL(Rq1SwReferenceProject_Pool.class, Rq1RecordType.PROJECT, "Type=RefPrj,Domain=Software,IsPoolProject=Yes"),
    SOFTWARE_DEVELOPMENT_PROJECT(Rq1SwDevelopmentProject.class, Rq1RecordType.PROJECT, "Type=DevPrj,Domain=Software"),
    //
    // Hardware Projects
    //
    HARDWARE_COSTUMER_PROJECT_LEAF(Rq1HwCustomerProject_Leaf.class, Rq1RecordType.PROJECT, "Type=Customer Project,Domain=Hardware,IsPoolProject=No"),
    HARDWARE_CUSTOMER_PROJECT_POOL(Rq1HwCustomerProject_Pool.class, Rq1RecordType.PROJECT, "Type=Customer Project,Domain=Hardware,IsPoolProject=Yes"),
    HARDWARE_PLATFORM_PROJECT_LEAF(Rq1HwPlatformProject_Leaf.class, Rq1RecordType.PROJECT, "Type=Platform Project,Domain=Hardware,IsPoolProject=No"),
    //
    // Other Projects
    //
    UNKNOWN_PROJECT(Rq1UnknownProject.class, Rq1RecordType.PROJECT, ""),
    //
    // Software Releases
    //
    PROJECT_MILESTONE(Rq1Milestone.class, Rq1RecordType.RELEASE, "Domain=Software,Type=PVER,Category=Collection,Classification=Milestone"),
    PVER_RELEASE(Rq1PverRelease.class, Rq1RecordType.RELEASE, "Domain=Software,Type=PVER,Category=(SW-Version)|(Migration-Baseline)"),
    PVER_COLLECTION(Rq1PverCollection.class, Rq1RecordType.RELEASE, "Domain=Software,Type=PVER,Category=Collection"),
    PVAR_RELEASE(Rq1PvarRelease.class, Rq1RecordType.RELEASE, "Domain=Software,Type=PVAR/PFAM,Category=(SW-Version)|(Migration-Baseline)"),
    PVAR_COLLECTION(Rq1PvarCollection.class, Rq1RecordType.RELEASE, "Domain=Software,Type=PVAR/PFAM,Category=Collection"),
    BC_RELEASE(Rq1BcRelease.class, Rq1RecordType.RELEASE, "Domain=Software,Type=BC,Category=(SW-Version)|(Migration-Baseline)"),
    BC_COLLECTION(Rq1BcCollection.class, Rq1RecordType.RELEASE, "Domain=Software,Type=BC,Category=Collection"),
    FC_RELEASE(Rq1FcRelease.class, Rq1RecordType.RELEASE, "Domain=Software,Type=FC,Category=(SW-Version)|(Migration-Baseline)"),
    FC_COLLECTION(Rq1FcCollection.class, Rq1RecordType.RELEASE, "Domain=Software,Type=FC,Category=Collection"),
    //
    // Hardware Releases
    //
    HW_ECU_RELEASE(Rq1EcuRelease.class, Rq1RecordType.RELEASE, "Type=HW-ECU,Domain=Hardware,Category=(HW Version|Collection)"),
    HW_MOD_RELEASE(Rq1ModRelease.class, Rq1RecordType.RELEASE, "Type=HW-MOD,Domain=Hardware"),
    HW_COMP_RELEASE(Rq1CompRelease.class, Rq1RecordType.RELEASE, "Type=HW-COMP,Domain=Hardware"),
    HW_BUNDLE_RELEASE(Rq1BundleRelease.class, Rq1RecordType.RELEASE, "Type=Bundle,Domain=Hardware,Category=Collection"),
    //
    // Other Releases
    //
    UNKNOWN_RELEASE(Rq1UnknownRelease.class, Rq1RecordType.RELEASE, ""),
    //
    // Software WorkItem
    //
    WORKITEM_ISSUE_SW(Rq1WorkItem_IssueSW.class, Rq1RecordType.WORKITEM, "Domain=Software,Category=Issue,SubCategory=Issue SW"),
    WORKITEM_ISSUE_FD(Rq1WorkItem_IssueFD.class, Rq1RecordType.WORKITEM, "Domain=Software,Category=Issue,SubCategory=Issue FD"),
    WORKITEM_PVAR_RELEASE(Rq1WorkItem_Pvar.class, Rq1RecordType.WORKITEM, "Domain=Software,Category=Release,SubCategory=PVAR\\/PFAM"),
    WORKITEM_PVER_RELEASE(Rq1WorkItem_Pver.class, Rq1RecordType.WORKITEM, "Domain=Software,Category=Release,SubCategory=PVER"),
    WORKITEM_BC_RELEASE(Rq1WorkItem_Bc.class, Rq1RecordType.WORKITEM, "Domain=Software,Category=Release,SubCategory=BC"),
    WORKITEM_FC_RELEASE(Rq1WorkItem_Fc.class, Rq1RecordType.WORKITEM, "Domain=Software,Category=Release,SubCategory=FC"),
    WORKITEM_PROBLEM(Rq1WorkItem_Problem.class, Rq1RecordType.WORKITEM, "Category=Problem"),
    WORKITEM_CUST_PRJ(Rq1WorkItem_CustPrj.class, Rq1RecordType.WORKITEM, "Domain=Software,Category=Project,SubCategory=CustPrj"),
    WORKITEM_DEV_PRJ(Rq1WorkItem_DevPrj.class, Rq1RecordType.WORKITEM, "Domain=Software,Category=Project,SubCategory=DevPrj"),
    WORKITEM_REF_PRJ(Rq1WorkItem_RefPrj.class, Rq1RecordType.WORKITEM, "Domain=Software,Category=Project,SubCategory=RefPrj"),
    //
    // Hardware WorkItem
    //
    WORKITEM_HW_ECU(Rq1WorkItem_HwEcu.class, Rq1RecordType.WORKITEM, "Domain=Hardware,Category=Release,SubCategory=HW-ECU"),
    WORKITEM_HW_MOD(Rq1WorkItem_HwMod.class, Rq1RecordType.WORKITEM, "Domain=Hardware,Category=Release,SubCategory=HW-MOD"),
    WORKITEM_HW_COMP(Rq1WorkItem_HwComp.class, Rq1RecordType.WORKITEM, "Domain=Hardware,Category=Release,SubCategory=HW-COMP"),
    WORKITEM_ISSUE_HW_ECU(Rq1WorkItem_IssueHwEcu.class, Rq1RecordType.WORKITEM, "Domain=Hardware,Category=Issue,SubCategory=Issue HW-ECU"),
    WORKITEM_ISSUE_HW_MOD(Rq1WorkItem_IssueHwMod.class, Rq1RecordType.WORKITEM, "Domain=Hardware,Category=Issue,SubCategory=Issue HW-MOD"),
    WORKITEM_HW_CUST_PRJ(Rq1WorkItem_HwCustomerProject.class, Rq1RecordType.WORKITEM, "Domain=Hardware,Category=Project,SubCategory=Customer Project"),
    WORKITEM_HW_PLAT_PRJ(Rq1WorkItem_HwPlatformProject.class, Rq1RecordType.WORKITEM, "Domain=Hardware,Category=Project,SubCategory=Platform Project"),
    WORKITEM_BUNDLE(Rq1WorkItem_Bundle.class, Rq1RecordType.WORKITEM, "Domain=Hardware,Category=Release,SubCategory=Bundle"),
    //
    // Other Workitems
    //
    WORKITEM(Rq1WorkItem_Any.class, Rq1RecordType.WORKITEM, ""),
    //
    // Software Issues
    //
    ISSUE_SW(Rq1IssueSW.class, Rq1RecordType.ISSUE, "Domain=Software,Type=Issue SW"),
    ISSUE_FD(Rq1IssueFD.class, Rq1RecordType.ISSUE, "Domain=Software,Type=Issue FD"),
    //
    // Hardware Issues
    //
    ISSUE_HW_ECU(Rq1IssueHwEcu.class, Rq1RecordType.ISSUE, "Domain=Hardware,Type=Issue HW-ECU"),
    ISSUE_HW_MOD(Rq1IssueMod.class, Rq1RecordType.ISSUE, "Domain=Hardware,Type=Issue HW-MOD"),
    //
    // Other Issues
    //
    UNKNOWN_ISSUE(Rq1UnknownIssue.class, Rq1RecordType.ISSUE, ""),
    //
    // Users, Contacts, Attachments, History, ...
    //
    USER(Rq1User.class, Rq1RecordType.USER, ""),
    CONTACT(Rq1Contact.class, Rq1RecordType.CONTACT, ""),
    EXTERNAL_LINK(Rq1ExternalLink.class, Rq1RecordType.EXTERNAL_LINK, ""),
    ATTACHMENT(Rq1Attachment.class, Rq1RecordType.ATTACHMENT, ""),
    ATTACHMENT_MAPPING(Rq1AttachmentMapping.class, Rq1RecordType.ATTACHMENT_MAPPING, ""),
    HISTORY_LOG(Rq1HistoryLog.class, Rq1RecordType.HISTORY_LOG, ""),
    METADATA_CHOICELIST(Rq1MetadataChoiceList.class, Rq1RecordType.METADATA, "Name=choicelist.*"),
    PROBLEM(Rq1Problem.class, Rq1RecordType.PROBLEM, "");

    final public static String TYPE_FIELDNAME = "Type";
    final public static String TYPE_BC = "BC";
    final public static String TYPE_FC = "FC";
    final public static String TYPE_ISSUE_SW = "Issue SW";

    final private Rq1RecordType recordType;
    final private FixedRecordValue[] fixedRecordValues;
    final private Class<? extends Rq1NodeInterface> nodeClass;

    Rq1NodeDescription(Class<? extends Rq1NodeInterface> nodeClass, Rq1RecordType recordType, String fixedRecordValueString) {
        this.recordType = recordType;
        this.fixedRecordValues = Rq1RecordDescription.FixedRecordValue.create(fixedRecordValueString);
        this.nodeClass = nodeClass;
    }

    @Override
    final public Rq1RecordType getRecordType() {
        return (recordType);
    }

    @Override
    final public FixedRecordValue[] getFixedRecordValues() {
        return (fixedRecordValues);
    }

    final public void setFixedRecordCriterias(Rq1QueryI query) {
        assert (query != null);
        for (FixedRecordValue fixedValue : fixedRecordValues) {
            if (fixedValue.isOnlyOneValueAllowed() == true) {
                query.addCriteria_Value(fixedValue.getFieldName(), fixedValue.getValue());
            }
        }
    }

    final public Class getNodeClass() {
        return (nodeClass);
    }

    final public Rq1NodeInterface createSubject() {
        try {
            return (nodeClass.getDeclaredConstructor().newInstance());
        } catch (Exception ex) {
            Logger.getLogger(Rq1NodeDescription.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(Rq1NodeDescription.class.getName(), ex);
            throw (new Error(ex));
        }
    }

}
