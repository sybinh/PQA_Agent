/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmElementI;
import Rq1Data.Enumerations.CategoryPstRelease;

/**
 *
 * @author gug2wi
 */
public interface DmRq1PstReleaseI extends DmElementI {

    CategoryPstRelease getCategory();

    /**
     * The classification may be set to a old value. Therefore, other values
     * then from the enumeration ClassificationPstRelease may be returned.
     *
     * @return
     */
//    EcvEnumeration getClassification();

}
