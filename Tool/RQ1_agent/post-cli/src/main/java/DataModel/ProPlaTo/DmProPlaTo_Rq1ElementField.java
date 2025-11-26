/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ProPlaTo;

import DataModel.DmConstantField_Text;
import DataModel.DmElementField_ReadOnlyFromSource;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import UiSupport.UiThreadChecker;

/**
 *
 * @author GUG2WI
 */
public class DmProPlaTo_Rq1ElementField extends DmElementField_ReadOnlyFromSource<DmRq1ElementInterface> {

    final private DmConstantField_Text idField;
    private boolean loadDone = false;
    private DmRq1ElementInterface element = null;

    public DmProPlaTo_Rq1ElementField(DmConstantField_Text idField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (idField != null);

        this.idField = idField;
    }

    @Override
    public DmRq1ElementInterface getElement() {
        UiThreadChecker.ensureBackgroundThread();
        if (loadDone == false) {
            String rq1Id = idField.getValue();
            if ((rq1Id != null) && (rq1Id.isEmpty() == false)) {
                element = DmRq1Element.getElementById(rq1Id);
            }
            loadDone = true;
        }
        return (element);
    }

}
