/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataStore.DsField_Xml;
import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.ExportCategory;
import Rq1Data.Enumerations.ExportScope;
import Rq1Data.Enumerations.QualityMeasureState;

/**
 *
 * @author GUG2WI
 */
public class Rq1Milestone extends Rq1ReleaseRecord {

    final public Rq1DatabaseField_Enumeration SWTEST_BFT;
    final public Rq1DatabaseField_Enumeration SWTEST_COM;
    final public Rq1DatabaseField_Enumeration SWTEST_CST;
    final public Rq1DatabaseField_Enumeration SWTEST_EEPROM;
    final public Rq1DatabaseField_Enumeration SWTEST_FT;
    final public Rq1DatabaseField_Enumeration SWTEST_IO;
    final public Rq1DatabaseField_Enumeration SWTEST_OPT;
    final public Rq1DatabaseField_Enumeration SWTEST_OST;
    final public Rq1DatabaseField_Enumeration SWTEST_PVER_CONF;
    final public Rq1DatabaseField_Enumeration SWTEST_PVER_I;
    final public Rq1DatabaseField_Enumeration SWTEST_SRR;
    final public Rq1DatabaseField_Enumeration SWTEST_VIVA;

    final public Rq1XmlSubField_Text SWTEST_BFT_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_COM_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_CST_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_EEPROM_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_FT_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_IO_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_OPT_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_OST_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_PVER_CONF_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_PVER_I_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_SRR_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_VIVA_COMMENT;

    final public Rq1DatabaseField_Date IMPLEMENTATION_FREEZE;

    //
    // GPM - Guided project management
    //
    final private Rq1XmlSubField_Xml GPM;
    final public Rq1XmlSubField_Enumeration EXPORT_SCOPE;
    final public Rq1XmlSubField_Enumeration EXPORT_CATEGORY;

    public Rq1Milestone() {
        super(Rq1NodeDescription.PROJECT_MILESTONE);

        addField(SWTEST_BFT = new Rq1DatabaseField_Enumeration(this, "SWTest_BFT", QualityMeasureState.values()));
        addField(SWTEST_COM = new Rq1DatabaseField_Enumeration(this, "SWTest_COM", QualityMeasureState.values()));
        addField(SWTEST_CST = new Rq1DatabaseField_Enumeration(this, "SWTest_CST", QualityMeasureState.values()));
        addField(SWTEST_EEPROM = new Rq1DatabaseField_Enumeration(this, "SWTest_EEPROM", QualityMeasureState.values()));
        addField(SWTEST_FT = new Rq1DatabaseField_Enumeration(this, "SWTest_FT", QualityMeasureState.values()));
        addField(SWTEST_IO = new Rq1DatabaseField_Enumeration(this, "SWTest_IO", QualityMeasureState.values()));
        addField(SWTEST_OPT = new Rq1DatabaseField_Enumeration(this, "SWTest_OPT", QualityMeasureState.values()));
        addField(SWTEST_OST = new Rq1DatabaseField_Enumeration(this, "SWTest_OST", QualityMeasureState.values()));
        addField(SWTEST_PVER_CONF = new Rq1DatabaseField_Enumeration(this, "SWTest_PVER_Conf", QualityMeasureState.values()));
        addField(SWTEST_PVER_I = new Rq1DatabaseField_Enumeration(this, "SWTest_PVER_I", QualityMeasureState.values()));
        addField(SWTEST_SRR = new Rq1DatabaseField_Enumeration(this, "SWTest_SRR", QualityMeasureState.values()));
        addField(SWTEST_VIVA = new Rq1DatabaseField_Enumeration(this, "SWTest_VIVA", QualityMeasureState.values()));

        SWTEST_BFT.acceptInvalidValuesInDatabase();
        SWTEST_COM.acceptInvalidValuesInDatabase();
        SWTEST_CST.acceptInvalidValuesInDatabase();
        SWTEST_EEPROM.acceptInvalidValuesInDatabase();
        SWTEST_FT.acceptInvalidValuesInDatabase();
        SWTEST_IO.acceptInvalidValuesInDatabase();
        SWTEST_OPT.acceptInvalidValuesInDatabase();
        SWTEST_OST.acceptInvalidValuesInDatabase();
        SWTEST_PVER_CONF.acceptInvalidValuesInDatabase();
        SWTEST_PVER_I.acceptInvalidValuesInDatabase();
        SWTEST_SRR.acceptInvalidValuesInDatabase();
        SWTEST_VIVA.acceptInvalidValuesInDatabase();

        addField(SWTEST_BFT_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_BFT_Comment"));
        addField(SWTEST_COM_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_COM_Comment"));
        addField(SWTEST_CST_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_CST_Comment"));
        addField(SWTEST_EEPROM_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_EEPROM_Comment"));
        addField(SWTEST_FT_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_FT_Comment"));
        addField(SWTEST_IO_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_IO_Comment"));
        addField(SWTEST_OPT_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_OPT_Comment"));
        addField(SWTEST_OST_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_OST_Comment"));
        addField(SWTEST_PVER_CONF_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_PVER_Conf_Comment"));
        addField(SWTEST_PVER_I_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_PVER_I_Comment"));
        addField(SWTEST_SRR_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_SRR_Comment"));
        addField(SWTEST_VIVA_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_VIVA_Comment"));

        addField(IMPLEMENTATION_FREEZE = new Rq1DatabaseField_Date(this, "ImplementationFreeze", "ImplementationFreeze"));

        addField(GPM = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKNOWN_ELEMENTS_ALLOWED, "GPM"));
        GPM.setOptional();
        addField(EXPORT_SCOPE = new Rq1XmlSubField_Enumeration(this, GPM, "Export_Scope", ExportScope.values(), ExportScope.EMPTY));
        EXPORT_SCOPE.setOptional();
        addField(EXPORT_CATEGORY = new Rq1XmlSubField_Enumeration(this, GPM, "Export_Category", ExportCategory.values(), ExportCategory.EMPTY));
        EXPORT_CATEGORY.setOptional();
        EXPORT_CATEGORY.acceptInvalidValuesInDatabase();
    }
}
