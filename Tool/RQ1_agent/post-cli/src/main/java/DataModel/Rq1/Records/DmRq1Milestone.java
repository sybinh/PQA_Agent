/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmMilestoneI;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1Milestone;
import Rq1Data.Enumerations.LifeCycleState_Release;
import Rq1Data.Enumerations.QualityMeasureState;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Enumerations.YesNoEmpty;
import util.EcvDate;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Milestone extends DmRq1ReleaseRecord implements DmMilestoneI {

    public static final String ELEMENTTYPE = "Milestone";

    final public DmRq1Field_Enumeration SWTEST_BFT;
    final public DmRq1Field_Enumeration SWTEST_COM;
    final public DmRq1Field_Enumeration SWTEST_CST;
    final public DmRq1Field_Enumeration SWTEST_EEPROM;
    final public DmRq1Field_Enumeration SWTEST_FT;
    final public DmRq1Field_Enumeration SWTEST_IO;
    final public DmRq1Field_Enumeration SWTEST_OPT;
    final public DmRq1Field_Enumeration SWTEST_OST;
    final public DmRq1Field_Enumeration SWTEST_PVER_CONF;
    final public DmRq1Field_Enumeration SWTEST_PVER_I;
    final public DmRq1Field_Enumeration SWTEST_SRR;
    final public DmRq1Field_Enumeration SWTEST_VIVA;

    final public DmRq1Field_Text SWTEST_BFT_COMMENT;
    final public DmRq1Field_Text SWTEST_COM_COMMENT;
    final public DmRq1Field_Text SWTEST_CST_COMMENT;
    final public DmRq1Field_Text SWTEST_EEPROM_COMMENT;
    final public DmRq1Field_Text SWTEST_FT_COMMENT;
    final public DmRq1Field_Text SWTEST_IO_COMMENT;
    final public DmRq1Field_Text SWTEST_OPT_COMMENT;
    final public DmRq1Field_Text SWTEST_OST_COMMENT;
    final public DmRq1Field_Text SWTEST_PVER_CONF_COMMENT;
    final public DmRq1Field_Text SWTEST_PVER_I_COMMENT;
    final public DmRq1Field_Text SWTEST_SRR_COMMENT;
    final public DmRq1Field_Text SWTEST_VIVA_COMMENT;

    final public DmRq1Field_Date IMPLEMENTATION_FREEZE;

    //
    // Fields for GPM - Guided project management
    //
    final public DmRq1Field_Enumeration EXPORT_SCOPE;
    final public DmRq1Field_Enumeration EXPORT_CATEGORY;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Milestone(Rq1Milestone rq1Milestone) {
        super(ELEMENTTYPE, rq1Milestone);

        addField(SWTEST_BFT = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_BFT, "SWTest_BFT"));
        addField(SWTEST_COM = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_COM, "SWTest_COM"));
        addField(SWTEST_CST = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_CST, "SWTest_CST"));
        addField(SWTEST_EEPROM = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_EEPROM, "SWTest_EEPROM"));
        addField(SWTEST_FT = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_FT, "SWTest_FT"));
        addField(SWTEST_IO = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_IO, "SWTest_IO"));
        addField(SWTEST_OPT = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_OPT, "SWTest_OPT"));
        addField(SWTEST_OST = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_OST, "SWTest_OST"));
        addField(SWTEST_PVER_CONF = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_PVER_CONF, "SWTest_PVER-Conf"));
        addField(SWTEST_PVER_I = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_PVER_I, "SWTest_PVER-I"));
        addField(SWTEST_SRR = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_SRR, "SWTest_SRR"));
        addField(SWTEST_VIVA = new DmRq1Field_Enumeration(this, rq1Milestone.SWTEST_VIVA, "SWTest_VIVA"));

        addField(SWTEST_BFT_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_BFT_COMMENT, "BFT Comment"));
        addField(SWTEST_COM_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_COM_COMMENT, "COM Comment"));
        addField(SWTEST_CST_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_CST_COMMENT, "CST Comment"));
        addField(SWTEST_EEPROM_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_EEPROM_COMMENT, "EEPROM Comment"));
        addField(SWTEST_FT_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_FT_COMMENT, "FT Comment"));
        addField(SWTEST_IO_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_IO_COMMENT, "IO Comment"));
        addField(SWTEST_OPT_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_OPT_COMMENT, "OPT Comment"));
        addField(SWTEST_OST_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_OST_COMMENT, "OST Comment"));
        addField(SWTEST_PVER_CONF_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_PVER_CONF_COMMENT, "PVER-Conf Comment"));
        addField(SWTEST_PVER_I_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_PVER_I_COMMENT, "PVER-I Comment"));
        addField(SWTEST_SRR_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_SRR_COMMENT, "SRR Comment"));
        addField(SWTEST_VIVA_COMMENT = new DmRq1Field_Text(this, rq1Milestone.SWTEST_VIVA_COMMENT, "VIVA Comment"));

        addField(IMPLEMENTATION_FREEZE = new DmRq1Field_Date(this, rq1Milestone.IMPLEMENTATION_FREEZE, "Implementation Freeze"));

        addField(EXPORT_SCOPE = new DmRq1Field_Enumeration(rq1Milestone.EXPORT_SCOPE, "Guided PjM Export Scope"));
        addField(EXPORT_CATEGORY = new DmRq1Field_Enumeration(rq1Milestone.EXPORT_CATEGORY, "Guided PjM Export Category"));
    }

    @Override
    final public EcvEnumeration getLifeCycleState() {
        return (LIFE_CYCLE_STATE.getValue());
    }

    public static DmRq1Milestone create() {
        Rq1Milestone rq1Record = new Rq1Milestone();
        DmRq1Milestone dmElement = new DmRq1Milestone(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmElement);
        return (dmElement);
    }

    public static DmRq1Milestone createBasedOnProject(DmRq1Project project) {
        assert (project != null);

        DmRq1Milestone milestone = create();

        milestone.ACCOUNT_NUMBERS.setValue(project.ACCOUNT_NUMBERS.getValue());
        milestone.SCOPE.setValue(Scope.INTERNAL);

        //
        // Connect Collection - Project
        //
        milestone.PROJECT.setElement(project);
        project.ALL_MILESTONES.addElementIfLoaded(milestone);
        project.OPEN_MILESTONES.addElementIfLoaded(milestone);

        return (milestone);
    }

    @Override
    public boolean save() {
        var lcs = LIFE_CYCLE_STATE.getValue();
        if ((lcs != null) && (lcs != LifeCycleState_Release.NEW)) {
            checkAndSet(SWTEST_BFT, SWTEST_BFT_COMMENT);
            checkAndSet(SWTEST_COM, SWTEST_COM_COMMENT);
            checkAndSet(SWTEST_CST, SWTEST_CST_COMMENT);
            checkAndSet(SWTEST_EEPROM, SWTEST_EEPROM_COMMENT);
            checkAndSet(SWTEST_FT, SWTEST_FT_COMMENT);
            checkAndSet(SWTEST_IO, SWTEST_IO_COMMENT);
            checkAndSet(SWTEST_OPT, SWTEST_OPT_COMMENT);
            checkAndSet(SWTEST_OST, SWTEST_OST_COMMENT);
            checkAndSet(SWTEST_PVER_CONF, SWTEST_PVER_CONF_COMMENT);
            checkAndSet(SWTEST_PVER_I, SWTEST_PVER_I_COMMENT);
            checkAndSet(SWTEST_SRR, SWTEST_SRR_COMMENT);
            checkAndSet(SWTEST_VIVA, SWTEST_VIVA_COMMENT);
            checkAndSet(PLANNING_FREEZE, PLANNED_DATE);
            checkAndSet(SPECIFICATION_FREEZE, PLANNED_DATE);
            checkAndSet(IMPLEMENTATION_FREEZE, PLANNED_DATE);
            if (BASED_ON_PREDECESSOR.getValue() == YesNoEmpty.EMPTY) {
                BASED_ON_PREDECESSOR.setValue(YesNoEmpty.NO);
            }
        }
        return super.save();
    }

    private void checkAndSet(DmRq1Field_Enumeration testField, DmRq1Field_Text commentField) {
        if (testField.getValue() == QualityMeasureState.EMPTY) {
            testField.setValue(QualityMeasureState.NOT_REQUIRED);
            commentField.setValue("Not required because it is a guidedPjM milestone.");
        }
    }

    private void checkAndSet(DmRq1Field_Date dateFieldToCheckAndSet, DmRq1Field_Date dateFieldToGetValueFrom) {
        EcvDate currentValue = dateFieldToCheckAndSet.getValue();
        if (currentValue == null || currentValue.isEmpty()) {
            EcvDate newValue = dateFieldToGetValueFrom.getDate();
            if (newValue != null && newValue.isNotEmpty()) {
                dateFieldToCheckAndSet.setValue(newValue);
            }
        }
    }

    @Override
    public String getType() {
        return (ELEMENTTYPE);
    }

    @Override
    public String getName() {
        return (TITLE.getValueAsText());
    }

    @Override
    public String getVersion() {
        return ("");
    }
}
