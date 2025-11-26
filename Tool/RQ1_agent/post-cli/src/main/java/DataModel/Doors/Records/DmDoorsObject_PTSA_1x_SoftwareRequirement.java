/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmConstantField_Text;
import Doors.DoorsObject;

public class DmDoorsObject_PTSA_1x_SoftwareRequirement extends DmDoorsObject_PTSA_1x_Requirement {

    final public DmConstantField_Text AFFECTED_SW_FUNCTION;
    final public DmConstantField_Text APPLICATION;
    final public DmConstantField_Text BIT_SIZE;
    final public DmConstantField_Text COMPONENT_CATEGORY;
    final public DmConstantField_Text COMPONENT_TYPE;
    final public DmConstantField_Text COMPUTATION_CYCLE;
    final public DmConstantField_Text CUSTOMER_SYSTEM_RELEASE;
    final public DmConstantField_Text DEFAULT_VALUE;
    final public DmConstantField_Text DUTY_CYCLE_MIN;
    final public DmConstantField_Text DUTY_CYCLE_MAX;
    final public DmConstantField_Text FT_TEST_ID;
    final public DmConstantField_Text HW_MODULE;
    final public DmConstantField_Text HW_MODULE_VARIANT;
    final public DmConstantField_Text HW_SIGNAL_NAME;
    final public DmConstantField_Text IMPACT_ANALYSIS;
    final public DmConstantField_Text LINK_TO_TEST_DOCUMENTS;
    final public DmConstantField_Text LOWER_LIMIT_1;
    final public DmConstantField_Text LOWER_LIMIT_2;
    final public DmConstantField_Text PWM_FREQUENCE_MIN;
    final public DmConstantField_Text PWM_FREQUENCE_MAX;
    final public DmConstantField_Text SW_SIGNALNAME;
    final public DmConstantField_Text SW_VARIABLE_1;
    final public DmConstantField_Text SW_VARIABLE_2;
    final public DmConstantField_Text SW_DATA_TYPE_1;
    final public DmConstantField_Text SW_DATA_TYPE_2;
    final public DmConstantField_Text SYSTEM_FUNCTION;
    final public DmConstantField_Text SYS_SIGNALNAME;
    final public DmConstantField_Text TEST_GOAL_ID;
    final public DmConstantField_Text TESTLEVEL;
    final public DmConstantField_Text UNIT_1;
    final public DmConstantField_Text UNIT_2;
    final public DmConstantField_Text UPPER_LIMIT_1;
    final public DmConstantField_Text UPPER_LIMIT_2;

    public DmDoorsObject_PTSA_1x_SoftwareRequirement(DoorsObject doorsObject) {
        super(ElementType.SOFTWARE_REQ, doorsObject);
        assert (doorsObject != null);

        AFFECTED_SW_FUNCTION = extractUserDefinedField("Affected-SW-Function");
        APPLICATION = extractUserDefinedField("Application");
        BIT_SIZE = extractUserDefinedField("BitSize");
        COMPONENT_CATEGORY = extractUserDefinedField("ComponentCategory");
        COMPONENT_TYPE = extractUserDefinedField("ComponentType");
        COMPUTATION_CYCLE = extractUserDefinedField("ComputationCycle");
        CUSTOMER_SYSTEM_RELEASE = extractUserDefinedField("Customer System Release");
        DEFAULT_VALUE = extractUserDefinedField("DefaultValue", "Default Value");
        DUTY_CYCLE_MIN = extractUserDefinedField("DutyCycleMin", "Duty Cycle Min");
        DUTY_CYCLE_MAX = extractUserDefinedField("DutyCycleMax", "Duty Cycle Max");
        FT_TEST_ID = extractUserDefinedField("FT-Test ID");
        HW_MODULE = extractUserDefinedField("HW_Module");
        IMPACT_ANALYSIS = extractUserDefinedField("HW_ModuleVariant");
        HW_MODULE_VARIANT = extractUserDefinedField("HW_SignalName");
        HW_SIGNAL_NAME = extractUserDefinedField("Impact Analysis");
        LINK_TO_TEST_DOCUMENTS = extractUserDefinedField("Link to Test Documents");
        LOWER_LIMIT_1 = extractUserDefinedField("Lower Limit1", "Lower Limit 1");
        LOWER_LIMIT_2 = extractUserDefinedField("Lower Limit2", "Lower Limit 2");
        PWM_FREQUENCE_MIN = extractUserDefinedField("PWM_FrequenceMin", "PWM Frequence min");
        PWM_FREQUENCE_MAX = extractUserDefinedField("PWM_FrequenceMax", "PWM Frequence max");
        SW_SIGNALNAME = extractUserDefinedField("SW_SignalName", "SW Signalname");
        SW_VARIABLE_1 = extractUserDefinedField("SW_Variable1", "SW Variable 1");
        SW_VARIABLE_2 = extractUserDefinedField("SW_Variable2", "SW Variable 2");
        SW_DATA_TYPE_1 = extractUserDefinedField("SW_DataType1", "SW Datatype 1");
        SW_DATA_TYPE_2 = extractUserDefinedField("SW_Datatype2", "SW Datatype 2");
        SYSTEM_FUNCTION = extractUserDefinedField("SystemFunction", "System Function");
        SYS_SIGNALNAME = extractUserDefinedField("Sys_SignalName", "Sys SignalName");
        TEST_GOAL_ID = extractUserDefinedField("Test goal ID");
        TESTLEVEL = extractUserDefinedField("TestLevel");
        UPPER_LIMIT_1 = extractUserDefinedField("Upper Limit1", "Upper Limit 1");
        UPPER_LIMIT_2 = extractUserDefinedField("Upper Limit2", "Upper Limit 2");
        UNIT_1 = extractUserDefinedField("Unit1", "Unit 1");
        UNIT_2 = extractUserDefinedField("Unit2", "Unit 2");

        finishExtractionOfUserDefinedFields();
    }

}
