/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.UiSupport;

import DataModel.DmFieldI;
import util.EcvTableData;
import util.EcvTableDescription;

/**
 *
 * @author gug2wi
 */
public interface DmUiTableSource {

    DmFieldI getDmField();

    EcvTableDescription getTableDescription();

    EcvTableData getValue();

    void setValue(EcvTableData newData);

    boolean useLazyLoad();
    
    default boolean getCreateAutoSorter(){
        return(true);
    }
    
}
