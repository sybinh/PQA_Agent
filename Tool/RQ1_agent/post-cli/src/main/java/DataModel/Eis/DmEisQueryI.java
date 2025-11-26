/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Eis;

import Rq1Data.Enumerations.LifeCycleState_Issue;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author hfi5wi
 * @param <T_TYPE>
 */
public interface DmEisQueryI<T_TYPE> {
    
    final static EnumSet<LifeCycleState_Issue> LCS_NEW_CONFLICTED = EnumSet.of(LifeCycleState_Issue.NEW, LifeCycleState_Issue.CONFLICTED);
    
    /**
     * Return all loaded results of the query
     * @return List
     */
    public abstract List<T_TYPE> getResult();
}
