/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmField;
import DataModel.DmFieldI;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Types.DmRq1Table_PvarDerivatives;
import util.EcvDate;
import Rq1Data.Types.Rq1DerivativeMapping;
import Rq1Data.Types.Rq1DerivativePlannedDates;
import util.EcvTableData;
import DataModel.UiSupport.DmUiTableSource;
import java.util.List;
import util.EcvTableRow;
import DataModel.DmValueFieldI;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1SoftwareProject;
import java.util.Collection;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_DerivativesTable extends DmField implements DmValueFieldI<EcvTableData>, DmUiTableSource {

    final private DmValueFieldI<Rq1DerivativeMapping> derivativeMappingField;
    final private DmRq1Field_DerivativesDate PLANNED_DATE_DERIVATIVES;
    final private DmRq1Table_PvarDerivatives table;
    final private DmRq1Field_Reference<DmRq1Project> projectField;

    public DmRq1Field_DerivativesTable(DmRq1Pst parent, DmValueFieldI<Rq1DerivativeMapping> derivativeMappingField,
            DmRq1Field_Reference<DmRq1Project> projectField, DmRq1Field_DerivativesDate PLANNED_DATE_DERIVATIVES, String nameForUserInterface) {
        super(nameForUserInterface);

        this.derivativeMappingField = derivativeMappingField;
        this.table = new DmRq1Table_PvarDerivatives(projectField);
        this.PLANNED_DATE_DERIVATIVES = PLANNED_DATE_DERIVATIVES;
        this.projectField = projectField;

    }

    @Override
    public EcvTableData getValue() {

        EcvTableData tableData = table.createTableData();
        Rq1DerivativeMapping mapping = derivativeMappingField.getValue();
        Rq1DerivativePlannedDates derivativesDate = PLANNED_DATE_DERIVATIVES.getValue();

        for (String derivativeName : mapping.getDerivativeNames()) {

            EcvTableRow newRow = tableData.createRow();

            table.DERIVATIVE.setValue(newRow, derivativeName);
            table.MAPPING.setValue(newRow, mapping.getModeForDerivative(derivativeName).toString());
            EcvDate date = derivativesDate.getDateForDerivative(derivativeName);
            if ((date != null) && (date.isEmpty() == false)) {
                table.PLANNED_DATE.setValue(newRow, date);
            }

            tableData.addRow(newRow);
        }
        return (tableData);
    }

    @Override
    public String getValueAsText() {
        return (derivativeMappingField.getValue().provideValueAsStringForDb());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(EcvTableData value) {
        Rq1DerivativeMapping mapping = new Rq1DerivativeMapping();
        Rq1DerivativePlannedDates derivativesDate = new Rq1DerivativePlannedDates();

        for (EcvTableRow row : value.getRows()) {

            Object mappingObject = row.getValueAt(table.MAPPING);
            Object derivativeNameObject = row.getValueAt(table.DERIVATIVE);
            Object plannedDate = row.getValueAt(table.PLANNED_DATE);

            if ((mappingObject != null) && (derivativeNameObject != null)) {
                Rq1DerivativeMapping.Mode mode = Rq1DerivativeMapping.Mode.getModeForString(mappingObject.toString());
                String derivativeName = derivativeNameObject.toString();
                if (derivativeName.isEmpty() == false) {
                    mapping.addMapping(mode, derivativeName);
                    if (plannedDate != null) {
                        derivativesDate.addDate(derivativeName, (EcvDate) plannedDate);
                    }
                }
            }
        }
        derivativeMappingField.setValue(mapping);
        PLANNED_DATE_DERIVATIVES.setValue(derivativesDate);
    }

    @Override
    public DmRq1Table_PvarDerivatives getTableDescription() {
        return (table);
    }

    @Override
    public boolean isReadOnly() {
        return (derivativeMappingField.isReadOnly());
    }

    @Override
    public DmFieldI getDmField() {
        return (this);
    }

    @Override
    public boolean useLazyLoad() {
        return (false);
    }

    /**
     * *
     * Returns a List with all the InternalNames of the derivatives based on a
     * project
     *
     * @return List containing all the derivatives internal names based on a
     * project
     */
    public Collection<String> getDerivativesInternalName() {
        DmRq1SoftwareProject project = (DmRq1SoftwareProject) projectField.getElement();
        return (project.getDerivativeNames());
    }

    public List<DmRq1Table_PvarDerivatives.Record> getRecords() {
        return (DmRq1Table_PvarDerivatives.extract(getValue()));
    }
}
