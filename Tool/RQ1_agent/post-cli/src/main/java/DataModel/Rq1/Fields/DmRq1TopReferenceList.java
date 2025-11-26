/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.Rq1.Records.DmRq1ElementInterface;
import Rq1Cache.Fields.Interfaces.Rq1ReferenceList_ReadOnlyI;
import UiSupport.UiTreeViewRootListI;

/**
 *
 * @author miw83wi
 * @param <T>
 */
public class DmRq1TopReferenceList<T extends DmRq1ElementInterface> extends DmRq1ReferenceList<T> implements UiTreeViewRootListI {

    public DmRq1TopReferenceList(Rq1ReferenceList_ReadOnlyI rq1ReferenceListField, String nameForUserInterface) {
        super(rq1ReferenceListField, nameForUserInterface);
    }

    @Override
    public String getViewTitle() {
        return (super.getNameForUserInterface());
    }

}
