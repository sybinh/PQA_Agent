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
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Pvar;
import DataModel.Rq1.Records.DmRq1PvarRelease;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Types.DmRq1Table_MappingToDerivatives_ComboBox;
import DataModel.UiSupport.DmUiTableSource;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Data.Types.Rq1DerivativeMapping;
import Rq1Data.Types.Rq1DerivativeMapping.Mode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * A derivative field in which the possible derivatives are selected from a
 * list.
 *
 * @author GUG2WI
 */
public class DmRq1Field_MappingToDerivatives_ComboBox extends DmToDsField<Rq1DerivativeMapping> implements DmValueFieldI<EcvTableData>, DmUiTableSource {

    final private DmRq1Table_MappingToDerivatives_ComboBox table;
    final private DmRq1Field_Reference<? extends DmRq1Release> HAS_MAPPED_RELEASE;

    public DmRq1Field_MappingToDerivatives_ComboBox(DmElementI parent, Rq1FieldI<Rq1DerivativeMapping> rq1Field,
            DmRq1Field_Reference<? extends DmRq1Release> HAS_MAPPED_RELEASE, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);

        this.HAS_MAPPED_RELEASE = HAS_MAPPED_RELEASE;

        this.table = new DmRq1Table_MappingToDerivatives_ComboBox(HAS_MAPPED_RELEASE);
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

    public Rq1DerivativeMapping getValueAsMapping() {
        return (dsField.getDataModelValue());
    }

    public void setValueAsMapping(Rq1DerivativeMapping mapping) {
        assert (mapping != null);
        dsField.setDataModelValue(mapping);
    }

    @Override
    public String getValueAsText() {
        return (dsField.getDataModelValue().provideValueAsStringForDb());
    }

    @Override
    public DmRq1Table_MappingToDerivatives_ComboBox getTableDescription() {
        return (table);
    }

    /**
     * Returns the names of all derivatives set in the field. It contains also
     * the Excluded values.
     *
     * @return Names of the derivatives.
     */
    final public List<String> getDerivatives() {
        Rq1DerivativeMapping mapping = dsField.getDataModelValue();
        return (new ArrayList<>(mapping.getDerivativeNames()));
    }

    /**
     * Returns the names of all derivatives set in the field as not excluded.
     *
     * @return Names of the derivatives that are not excluded.
     */
    final public Set<String> getActiveDerivatives() {
        Rq1DerivativeMapping mapping = dsField.getDataModelValue();
        return (mapping.getActiveDerivativeNames());
    }

    final public Rq1DerivativeMapping getDerivativeMapping() {
        return (dsField.getDataModelValue());
    }

    /**
     * Returns the list of derivatives active on the release.
     *
     * @return
     */
    public Set<String> getActiveDerivativesForBulkOperation() {
        if (HAS_MAPPED_RELEASE.getElement() instanceof DmRq1PvarRelease) {
            DmRq1PvarRelease release = (DmRq1PvarRelease) HAS_MAPPED_RELEASE.getElement();
            return (release.getActiveDerivatives());
        } else {
            return (new TreeSet<>());
        }
    }

    /**
     * Updates the derivative setting to fit to the given release.
     *
     * @param pstRelease PST for which the derivatives shall be updated.
     */
    final public void updateForPst(DmRq1Pst pstRelease) {
        assert (pstRelease != null);

        //
        // Get the, possibly empty, derivative map of the PST
        //
        Map<String, Rq1DerivativeMapping.Mode> derivativesFromPst;
        if (pstRelease instanceof DmRq1Pvar) {
            // Use a copy, because the map is changed later.
            derivativesFromPst = new TreeMap<>(((DmRq1Pvar) pstRelease).DERIVATIVES.getValue().getMapping());
        } else {
            derivativesFromPst = new TreeMap<>();
        }

        //
        // Prepare new data
        //
        DmRq1Table_MappingToDerivatives_ComboBox tableDescription = this.getTableDescription();
        EcvTableData newData = tableDescription.createTableData();

        //
        // Copy old settings that fit to derivatives in PST
        //
        EcvTableData oldData = getValue();
        for (EcvTableRow oldRow : oldData.getRows()) {
            String derivativeName = (String) oldRow.getValueAt(tableDescription.DERIVATIVE);
            if (derivativesFromPst.containsKey(derivativeName) == true) {
                if (derivativesFromPst.get(derivativeName) != Rq1DerivativeMapping.Mode.EXCLUDED) {
                    EcvTableRow newRow = newData.createRow();
                    tableDescription.DERIVATIVE.setValue(newRow, tableDescription.DERIVATIVE.getValue(oldRow));
                    tableDescription.MAPPING.setValue(newRow, tableDescription.MAPPING.getValue(oldRow));
                    newData.addRow(newRow);
                    derivativesFromPst.remove(derivativeName);
                }
            }
        }

        //
        // Add missing derivatives
        //
        for (Map.Entry<String, Rq1DerivativeMapping.Mode> derivative : derivativesFromPst.entrySet()) {
            Rq1DerivativeMapping.Mode mode = null;
            switch (derivative.getValue()) {
                case MANDATORY:
                case PILOT:
                case NOT_DECIDED:
                    mode = derivative.getValue();
                    break;
                case TOLERATED:
                    mode = Rq1DerivativeMapping.Mode.MANDATORY;
                    break;
                case EXCLUDED:
                    break;
                default:
                    throw (new Error("Unexpected mode: " + derivative.getValue()));
            }
            if (mode != null) {
                EcvTableRow row = newData.createRow();
                tableDescription.DERIVATIVE.setValue(row, derivative.getKey());
                tableDescription.MAPPING.setValue(row, mode.getModeString());
                newData.addRow(row);
            }
        }

        setValue(newData);
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
