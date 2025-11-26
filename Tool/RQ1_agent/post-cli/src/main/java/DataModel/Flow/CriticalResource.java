/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Flow;

/**
 *
 * @author bel5cob
 */
public interface CriticalResource {

    String getStartDate();

    String getEndDate();

    String getComment();

    String getCrTime();

    String getDPhase1();

    String getDPhase2();

    String getDPhase();

    String getResConflict();

    void setStartDate(String startDate);

    void setEndDate(String endDate);

    void setComment(String comment);

    void setCrTime(String crTime);

    void setDPhase1(String phase1);

    void setDPhase2(String phase2);

    void setDPhase(String phaseNeeded);

    void setResConflict(String phaseNeeded);
}
