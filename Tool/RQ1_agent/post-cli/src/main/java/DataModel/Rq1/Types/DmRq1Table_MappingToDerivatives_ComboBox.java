/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.DmElementI;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Release;
import Rq1Data.Types.Rq1DerivativeMapping;
import Rq1Data.Types.Rq1DerivativeMapping.Mode;
import java.awt.Font;
import java.util.Map;
import java.util.TreeMap;
import util.EcvTableDescription;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
import util.EcvTableColumn_ComboBox;
import util.EcvTableColumn_Derivate;

/**
 * A derivative table in which the possible derivatives are selected from a
 * list.
 *
 * @author GUG2WI
 */
public class DmRq1Table_MappingToDerivatives_ComboBox extends EcvTableDescription {

    private class DerivativeComboBox extends JComboBox<String> implements DmRq1Pst.ChangeListener {

        private DerivativeComboBox(DmRq1Field_Reference<? extends DmRq1Release> HAS_MAPPED_RELEASE) {
            //
            // Connect combo box to release
            //
            if (getRelease() != null) {
                changed(null);
                getRelease().addChangeListener(this);
            }
        }

        @Override
        final public void changed(DmElementI changedElement) {
            removeAllItems();
            for (String derivateName : getActiveDerivativesOfRelease().keySet()) {
                addItem(derivateName);
            }
        }

    }

    public class DerivativeColumn extends EcvTableColumn_ComboBox {

        final private DmRq1Field_Reference<? extends DmRq1Release> HAS_MAPPED_RELEASE;

        public DerivativeColumn(String uiName, DmRq1Field_Reference<? extends DmRq1Release> HAS_MAPPED_RELEASE) {
            super(uiName, new String[]{"-"});
            assert (HAS_MAPPED_RELEASE != null);
            this.HAS_MAPPED_RELEASE = HAS_MAPPED_RELEASE;
        }

        @Override
        public TableCellEditor getTableCellEditor(Font font) {
            JComboBox<String> comboBox = new DerivativeComboBox(HAS_MAPPED_RELEASE);

            return (new DefaultCellEditor(comboBox));
        }
    }

    final public EcvTableColumn_Derivate MAPPING;
    final public DerivativeColumn DERIVATIVE;
    final private DmRq1Field_Reference<? extends DmRq1Release> HAS_MAPPED_RELEASE;

    public DmRq1Table_MappingToDerivatives_ComboBox(DmRq1Field_Reference<? extends DmRq1Release> HAS_MAPPED_RELEASE) {
        assert (HAS_MAPPED_RELEASE != null);
        this.HAS_MAPPED_RELEASE = HAS_MAPPED_RELEASE;

        //
        // Get list of available mapping modes
        //
        String[] mappingModes = new String[Rq1DerivativeMapping.Mode.values().length];
        int i = 0;
        for (Rq1DerivativeMapping.Mode m : Rq1DerivativeMapping.Mode.values()) {
            mappingModes[i++] = m.getModeString();
        }

        //
        // Create columns
        //
        addIpeColumn(MAPPING = new EcvTableColumn_Derivate("Mapping", 12, mappingModes));
        addIpeColumn(DERIVATIVE = new DerivativeColumn("Derivative", HAS_MAPPED_RELEASE));
    }

    final public DmRq1Pst getRelease() {
        if (HAS_MAPPED_RELEASE.getElement() instanceof DmRq1Pst) {
            return ((DmRq1Pst) HAS_MAPPED_RELEASE.getElement());
        } else {
            return (null);
        }
    }

    final public Map<String, Mode> getActiveDerivativesOfRelease() {
        if (getRelease() != null) {
            return (getRelease().DERIVATIVES.getValue().getActiveDerivativeMapping());
        } else {
            return (new TreeMap<>());
        }

    }
}
