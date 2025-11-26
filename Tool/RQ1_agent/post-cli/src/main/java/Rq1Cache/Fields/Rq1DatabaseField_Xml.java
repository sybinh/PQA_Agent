/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsFieldI;
import DataStore.DsFieldI_Xml;
import DataStore.DsField_Xml;
import DataStore.DsField_XmlSourceI;
import DataStore.DsField_XmlSubField;
import DataStore.Exceptions.UnexpectedDataFailure;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Xml;
import Rq1Cache.Monitoring.Rq1DataRule;
import Rq1Cache.Monitoring.Rq1ParseFieldException;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Data.Monitoring.Rq1RuleDescription;
import java.util.EnumSet;
import java.util.logging.Level;
import util.EcvXmlCombinedElement;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlParser;
import util.EcvXmlTextElement;

/**
 * Represents a field of an RQ1 record which exists in this form in the RQ1
 * database and holds a XML value.
 *
 * It supports the splitting of the XML string in sub fields. This splitting is
 * done by adding sub fields via addSubField().
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_Xml extends Rq1DatabaseField_StringAccess<EcvXmlContainerElement> implements DsFieldI_Xml<Rq1RecordInterface>, DsField_XmlSourceI, Rq1FieldI_Xml {

    /**
     * Supports the conversion of non XML values to a XML value.
     * <p>
     * With this converter, it is possible to read values from fields whose
     * value are expected as XML value by IPE, but contain a non XML value.
     */
    public interface Converter {

        /**
         *
         * @param dbValue Non XML value read from the database.
         * @return An XML value, if the conversion succeeded or null if not.
         */
        EcvXmlContainerElement convertContentToXml(String dbValue);
    }

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rq1DatabaseField_Xml.class.getCanonicalName());

    @EcvElementList("Rq1Data.Monitoring.Rq1RuleDescription")
    static final public Rq1RuleDescription invalidXmlStringDescription = new Rq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.RQ1_DATA),
            "Valid format for XML field.",
            "The content of a field in the RQ1 database, which should contain a XML value, does not contain a valid XML value.\n"
            + "\n"
            + "The warning 'Unexpected data read from RQ1 database.' is set on the element that contains the field. "
            + "The field name and a problem text is added to the description of the warning.");

    private Rq1DataRule invalidXmlStringRule = null;

    final private DsField_Xml rq1Field_Xml;
    private Converter converter = null;
    private EcvXmlElement lastValueFromDb = null;
    private EcvXmlContainerElement lastValueProvidedForDb = null;

    public Rq1DatabaseField_Xml(Rq1RecordInterface parent, String dbFieldName) {
        this(parent, DsField_Xml.ContentMode.UNKNOWN_ELEMENTS_ALLOWED, dbFieldName);
    }

    public Rq1DatabaseField_Xml(Rq1RecordInterface parent, DsField_Xml.ContentMode unknownElementsAllowed, String dbFieldName) {
        super(parent, dbFieldName, new EcvXmlContainerElement("Content"));
        this.rq1Field_Xml = new DsField_Xml(this, unknownElementsAllowed);
    }

    @Override
    final public void addSubField(DsField_XmlSubField field) {
        assert (field != null);
        rq1Field_Xml.addSubField(field);
    }

    @Override
    public DsFieldI getSubField(String tagName) {
        return (rq1Field_Xml.getSubField(tagName));
    }

    final public Rq1DatabaseField_Xml addConverter(Converter converter) {
        assert (converter != null);
        this.converter = converter;
        return (this);
    }

    @Override
    final public boolean setOslcValue_Internal(String dbValue, Source source) {
        assert (dbValue != null);
        assert (source != null);

        removeMarker();

        //
        // Prepare string for parsing of XML values.
        // - Surround with an XML Tag to get a list of all contained fields.
        //
        StringBuilder dbValueSurrounded = new StringBuilder(50);
        dbValueSurrounded.append("<").append(getOslcPropertyName()).append(">");
        dbValueSurrounded.append(dbValue);
        dbValueSurrounded.append("</").append(getOslcPropertyName()).append(">");

        //
        // Create parser object and parse the string.
        //
        EcvXmlParser parser = new EcvXmlParser(dbValueSurrounded.toString());
        EcvXmlElement dbXml;
        try {
            dbXml = parser.parse();
        } catch (EcvXmlParser.ParseException ex) {
            dbXml = handleInvalidXml(dbValue, dbValueSurrounded.toString(), ex);
        }

        //
        // The field content was not in XML format, this is why we got an EcvXmlTextElement or an EcvXmlCombinedElement.
        //
        if ((dbXml instanceof EcvXmlTextElement) || (dbXml instanceof EcvXmlCombinedElement)) {
            dbXml = handleInvalidXml(dbValue, dbValueSurrounded.toString(), null);
        }

        //
        // The field was empty.
        //
        if (dbXml instanceof EcvXmlEmptyElement) {
            dbXml = new EcvXmlContainerElement(getOslcPropertyName());
        }

        //
        // Check if the value is changed.
        // Note: We cannot use the return value from setValue(), because the DB value contains only the rest of the XML content.
        //
        boolean dbValueChanged = false;
        if (dbXml.equals(lastValueFromDb) == false) {
            dbValueChanged = true;
            lastValueFromDb = dbXml.copy();
        }
        if ((lastValueProvidedForDb != null) && (lastValueProvidedForDb.equals(dbXml) == false)) {
            dbValueChanged = true;
        }

        //
        // Extract data for subfields
        //
        rq1Field_Xml.pushToSubfields((EcvXmlContainerElement) dbXml, source);

        //
        // Set remaining data
        //
        setDataSourceValue((EcvXmlContainerElement) dbXml, source);

        return (dbValueChanged);
    }

    private EcvXmlContainerElement handleInvalidXml(String dbValue, String dbValueSurrounded, EcvXmlParser.ParseException ex) {
        assert (dbValue != null);
        assert (dbValueSurrounded != null);
        assert (dbValueSurrounded.isEmpty() == false);

        //
        // Use converter to decode the database value.
        //
        EcvXmlContainerElement generatedXml = null;
        if (converter != null) {
            generatedXml = converter.convertContentToXml(dbValue);
        }

        if (generatedXml != null) {
            return (generatedXml);
        } else {
            //
            // Write diagnosis data to log.
            //
            logger.log(Level.WARNING, "dbValue=>{0}<", dbValue);
            logger.log(Level.WARNING, "dbValueSurrounded=>{0}<", dbValueSurrounded);

            Rq1ParseFieldException newEx;
            if (ex != null) {
                logger.log(Level.WARNING, ex.getErrorLog());
                newEx = new Rq1ParseFieldException("Unexpected XML-String. See log file for details.", ex);
            } else {
                newEx = new Rq1ParseFieldException("Unexpected XML-String. See log file for details.");
            }
            //
            // Create and attach failure object
            //
            StringBuilder s = new StringBuilder(50);
            s.append("Problem processing data from field ").append(getOslcPropertyName()).append(".\n");
            Rq1ParseFieldException containerEx = new Rq1ParseFieldException(s.toString(), newEx);
            if (invalidXmlStringRule == null) {
                invalidXmlStringRule = new Rq1DataRule(invalidXmlStringDescription);
            }
            this.getParentRecord().setMarker(new UnexpectedDataFailure(invalidXmlStringRule, getParentRecord(), this, containerEx));
            return (new EcvXmlContainerElement(getOslcPropertyName()));
        }
    }

    @Override
    final protected String getOslcValue_Internal() {
        lastValueProvidedForDb = (EcvXmlContainerElement) getDataSourceValue().copy();
        rq1Field_Xml.pullFromSubFields(lastValueProvidedForDb);
        return (lastValueProvidedForDb.getXmlString_WithoutContainer(EcvXmlElement.EncodeFormat.LONG_EMPTY_VALUE));
    }

    final public EcvXmlContainerElement getPureDataSourceValue() {
        EcvXmlContainerElement result = (EcvXmlContainerElement) getDataSourceValue().copy();
        rq1Field_Xml.pullFromSubFields(result);
        return (result);
    }

    @Override
    public boolean isChangedByDataModel() {
        if (super.isChangedByDataModel() == true) {
            return (true);
        } else {
            return (rq1Field_Xml.isChangedByDataModel());
        }
    }

    @Override
    public boolean isWritePending() {
        if (super.isWritePending() == true) {
            return (true);
        } else {
            return (rq1Field_Xml.isWritePending());
        }
    }

    private void removeMarker() {
        if (invalidXmlStringRule != null) {
            this.getParentRecord().removeMarkers(invalidXmlStringRule);
        }
    }

    @Override
    public void dbValueWasWrittenToDb() {
        removeMarker();
        super.dbValueWasWrittenToDb();
        rq1Field_Xml.dbValueWasWrittenToDb();
    }
}
