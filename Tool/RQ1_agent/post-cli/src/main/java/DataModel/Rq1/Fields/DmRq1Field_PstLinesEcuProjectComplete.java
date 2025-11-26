/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmToDsField;
import DataModel.DmValueFieldI;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Data.Types.Rq1LineEcuProject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_PstLinesEcuProjectComplete extends DmToDsField<String> implements DmValueFieldI<List<Rq1LineEcuProject>> {

    final private Rq1FieldI<String> rq1ExternalId;
    final private DmRq1Field_ExternalDescription_PstLines pstLinesFromExternalDescription;

    public DmRq1Field_PstLinesEcuProjectComplete(DmElementI parent, DmRq1Field_ExternalDescription_PstLines pstLinesFromExternalDescription, Rq1FieldI<String> rq1ExternalId, String nameForUserInterface) {
        super(rq1ExternalId, nameForUserInterface);
        assert (pstLinesFromExternalDescription != null);
        this.pstLinesFromExternalDescription = pstLinesFromExternalDescription;
        this.rq1ExternalId = rq1ExternalId;
    }

    @Override
    public List<Rq1LineEcuProject> getValue() {
        List<Rq1LineEcuProject> combinedList = new ArrayList<>(pstLinesFromExternalDescription.getValue());
        String extId = rq1ExternalId.getDataModelValue();
        if (extId.isEmpty() == false) {
            combinedList.add(new Rq1LineEcuProject(extId, extId));
        }
        return (combinedList);
    }

    @Override
    public void setValue(List<Rq1LineEcuProject> v) {

    }

    @Override
    public boolean isReadOnly() {
        return (pstLinesFromExternalDescription.isReadOnly());
    }

}
