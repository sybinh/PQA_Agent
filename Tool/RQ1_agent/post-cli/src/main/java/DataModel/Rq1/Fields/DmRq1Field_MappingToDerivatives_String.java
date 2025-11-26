/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmFieldI;
import DataModel.DmToDsField;
import DataModel.DmValueFieldI;
import DataModel.Rq1.Types.DmRq1Table_MappingToDerivatives_String;
import DataModel.UiSupport.DmUiTableSource;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Data.Types.Rq1DerivativeMapping;
import Rq1Data.Types.Rq1DerivativeMapping.Mode;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;

/**
 * A derivative field in which the derivatives can be entered as text by the
 * user.
 *
 * @author GUG2WI
 */
public class DmRq1Field_MappingToDerivatives_String extends DmToDsField<Rq1DerivativeMapping> implements DmValueFieldI<EcvTableData>, DmUiTableSource {

    final DmRq1Table_MappingToDerivatives_String table;

    public DmRq1Field_MappingToDerivatives_String(DmElementI parent, Rq1FieldI<Rq1DerivativeMapping> rq1Field, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);

        this.table = new DmRq1Table_MappingToDerivatives_String();
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData tableData = table.createTableData();
        Rq1DerivativeMapping mapping = dsField.getDataModelValue();
        for (String derivativeName : mapping.getDerivativeNames()) {
            EcvTableRow newRow = tableData.createRow();
            table.DERIVATIVE.setValue(newRow, derivativeName);
            table.MAPPING.setValue(newRow, mapping.getModeForDerivative(derivativeName).getModeString());
            tableData.addRow(newRow);
        }
        return (tableData);
    }

    @Override
    public void setValue(EcvTableData value) {
        Rq1DerivativeMapping mapping = new Rq1DerivativeMapping();
        for (EcvTableRow row : value.getRows()) {
            Object mappingObject = row.getValueAt(table.MAPPING);
            Object derivativeNameObject = row.getValueAt(table.DERIVATIVE);
            if ((mappingObject != null) && (derivativeNameObject != null)) {
                Mode mode = Mode.getModeForString(mappingObject.toString());
                String derivativeName = derivativeNameObject.toString();
                if (derivativeName.isEmpty() == false) {
                    mapping.addMapping(mode, derivativeName);
                }
            }
        }
        dsField.setDataModelValue(mapping);
    }

    @Override
    public String getValueAsText() {
        return (dsField.getDataModelValue().provideValueAsStringForDb());
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return (table);
    }

    @Override
    public DmFieldI getDmField() {
        return (this);
    }

    @Override
    public boolean useLazyLoad() {
        return (false);
    }
}
