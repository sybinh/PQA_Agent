/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Monitoring.RuleExecutionGroup;
import Rq1Cache.Records.Rq1Irm_Bc_IssueFd;
import Rq1Cache.Records.Rq1Irm_Fc_IssueFd;
import Rq1Cache.Records.Rq1Irm_ModRelease_IssueHw;
import Rq1Cache.Records.Rq1IssueFD;
import Rq1Cache.Records.Rq1IssueSW;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Records.Rq1Rrm_Bc_Fc;
import Rq1Cache.Records.Rq1Rrm_Ecu_Bundle;
import Rq1Cache.Records.Rq1Rrm_Pst_Bc;
import Rq1Cache.Records.Rq1Rrm_Pst_Bundle;
import Rq1Cache.Records.Rq1SubjectInterface;
import Rq1Cache.Records.Rq1WorkItem_Bc;
import Rq1Cache.Records.Rq1WorkItem_Fc;
import Rq1Cache.Records.Rq1WorkItem_IssueFD;
import Rq1Cache.Records.Rq1WorkItem_IssueSW;
import java.util.IdentityHashMap;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1ElementCache {

    //
    // Map to store all existing DmElement for Rq1Record
    //
    static final private IdentityHashMap<Rq1RecordInterface, DmRq1Element> elementMap = new IdentityHashMap<>();

    static void addElement(Rq1RecordInterface rq1Record, DmRq1Element dmElement) {
        assert (rq1Record != null);
        assert (dmElement != null);
        assert (elementMap.containsKey(rq1Record) == false);

        elementMap.put(rq1Record, dmElement);
        dmElement.setRuleExecutionGroupActive(RuleExecutionGroup.ELEMENT_INTEGRITY);

    }

    /**
     * Returns the DmRq1ElementInterface for the given RQ1-Id. The record is
     * loaded from the database, if it is not yet in the cache.
     *
     * @param rq1Id
     * @return null, if no record exists for this id in the database.
     */
    public static synchronized DmRq1ElementInterface getElement(String rq1Id) {
        assert (rq1Id != null);

        Rq1SubjectInterface rq1Subject = Rq1Cache.Rq1RecordIndex.getSubjectByRq1Id(rq1Id);
        if (rq1Subject == null) {
            return (null);
        }

        return (DmRq1ElementCache.getElement(rq1Subject));
    }

    /**
     * Returns the DmElement for a Rq1Record.
     *
     * @param rq1Record
     * @return DmElemtent
     */
    public static synchronized DmRq1ElementInterface getElement(Rq1RecordInterface rq1Record) {
        assert (rq1Record != null);

        //
        // Check if DmObject already exists. If so, return it.
        //
        DmRq1Element dmElement;
        if ((dmElement = elementMap.get(rq1Record)) != null) {
            return (dmElement);
        }

        //
        // Create new element
        //
        if ((dmElement = DmRq1ElementFactory.create(rq1Record)) == null) {
            throw (new Error("Unknown class " + rq1Record.getClass().getName()));
        }

        addElement(rq1Record, dmElement);

        dmElement.setRuleExecutionGroupActive(RuleExecutionGroup.ELEMENT_INTEGRITY);

        return (dmElement);
    }

    public static DmRq1IssueFD createIssueFD() {
        Rq1IssueFD rq1IssueFD = new Rq1IssueFD();
        DmRq1IssueFD dmIssueFD = new DmRq1IssueFD(rq1IssueFD);
        addElement(rq1IssueFD, dmIssueFD);
        return (dmIssueFD);
    }

    static DmRq1WorkItem_IssueSW createWorkItem_IssueSW() {
        Rq1WorkItem_IssueSW rq1WorkItem = new Rq1WorkItem_IssueSW();
        DmRq1WorkItem_IssueSW dmWorkItem = new DmRq1WorkItem_IssueSW(rq1WorkItem);
        addElement(rq1WorkItem, dmWorkItem);
        return (dmWorkItem);
    }

    static DmRq1WorkItem_IssueFD createWorkItem_IssueFD() {
        Rq1WorkItem_IssueFD rq1WorkItem = new Rq1WorkItem_IssueFD();
        DmRq1WorkItem_IssueFD dmWorkItem = new DmRq1WorkItem_IssueFD(rq1WorkItem);
        addElement(rq1WorkItem, dmWorkItem);
        return (dmWorkItem);
    }

    static DmRq1WorkItem_Bc createWorkItem_Bc() {
        Rq1WorkItem_Bc rq1WorkItem = new Rq1WorkItem_Bc();
        DmRq1WorkItem_Bc dmWorkItem = new DmRq1WorkItem_Bc(rq1WorkItem);
        addElement(rq1WorkItem, dmWorkItem);
        return (dmWorkItem);
    }

    static DmRq1WorkItem_Fc createWorkItem_Fc() {
        Rq1WorkItem_Fc rq1WorkItem = new Rq1WorkItem_Fc();
        DmRq1WorkItem_Fc dmWorkItem = new DmRq1WorkItem_Fc(rq1WorkItem);
        addElement(rq1WorkItem, dmWorkItem);
        return (dmWorkItem);
    }

    static DmRq1IssueSW createIssueSW() {
        Rq1IssueSW rq1IssueSW = new Rq1IssueSW();
        DmRq1IssueSW dmIssueSW = new DmRq1IssueSW(rq1IssueSW);
        addElement(rq1IssueSW, dmIssueSW);
        return (dmIssueSW);
    }

    static DmRq1Rrm_Pst_Bc createRrm_Pst_Bc() {
        Rq1Rrm_Pst_Bc rq1Rrm = new Rq1Rrm_Pst_Bc();
        DmRq1Rrm_Pst_Bc dmRrm = new DmRq1Rrm_Pst_Bc(rq1Rrm);
        addElement(rq1Rrm, dmRrm);
        return (dmRrm);
    }

    static DmRq1Rrm_Bc_Fc createRrm_Bc_Fc() {
        Rq1Rrm_Bc_Fc rq1Rrm = new Rq1Rrm_Bc_Fc();
        DmRq1Rrm_Bc_Fc dmRrm = new DmRq1Rrm_Bc_Fc(rq1Rrm);
        addElement(rq1Rrm, dmRrm);
        return (dmRrm);
    }
    
    static DmRq1Rrm_Ecu_Bundle createRrm_Ecu_Bundle() {
        Rq1Rrm_Ecu_Bundle rq1Rrm = new Rq1Rrm_Ecu_Bundle();
        DmRq1Rrm_Ecu_Bundle dmRrm = new DmRq1Rrm_Ecu_Bundle(rq1Rrm);
        addElement(rq1Rrm, dmRrm);
        return (dmRrm);
    }
    
    static DmRq1Rrm_Pst_Bundle createRrm_Pst_Bundle() {
        Rq1Rrm_Pst_Bundle rq1Rrm = new Rq1Rrm_Pst_Bundle();
        DmRq1Rrm_Pst_Bundle dmRrm = new DmRq1Rrm_Pst_Bundle(rq1Rrm);
        addElement(rq1Rrm, dmRrm);
        return (dmRrm);
    }
    
    static DmRq1Irm_Bc_IssueFd createIrm_Bc_IssueFd() {
        Rq1Irm_Bc_IssueFd rq1Irm = new Rq1Irm_Bc_IssueFd();
        DmRq1Irm_Bc_IssueFd dmIrm = new DmRq1Irm_Bc_IssueFd(rq1Irm);
        addElement(rq1Irm, dmIrm);
        return (dmIrm);
    }

    static DmRq1Irm_Fc_IssueFd createIrm_Fc_IssueFd() {
        Rq1Irm_Fc_IssueFd rq1Irm = new Rq1Irm_Fc_IssueFd();
        DmRq1Irm_Fc_IssueFd dmIrm = new DmRq1Irm_Fc_IssueFd(rq1Irm);
        addElement(rq1Irm, dmIrm);
        return (dmIrm);
    }

    static DmRq1Irm_ModRelease_IssueHw createIrm_ModRelease_IssueHw() {
        Rq1Irm_ModRelease_IssueHw rq1HwIrm = new Rq1Irm_ModRelease_IssueHw();
        DmRq1Irm_ModRelease_IssueHw dmHwIrm = new DmRq1Irm_ModRelease_IssueHw(rq1HwIrm);
        addElement(rq1HwIrm, dmHwIrm);
        return (dmHwIrm);
    }

}
