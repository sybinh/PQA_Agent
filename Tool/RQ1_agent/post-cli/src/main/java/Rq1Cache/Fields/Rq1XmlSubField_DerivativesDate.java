/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField_XmlSubField;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Monitoring.Rq1DataRule;
import Rq1Cache.Monitoring.Rq1ParseFieldException;
import Rq1Cache.Monitoring.Rq1UnexpectedDataFailure;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Data.Monitoring.Rq1RuleDescription;
import Rq1Data.Types.Rq1DerivativePlannedDates;
import Rq1Data.Types.Rq1ParseException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import util.EcvDate.DateParseException;
import util.EcvXmlElement;
import util.EcvXmlTextElement;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlSubField_DerivativesDate extends DsField_XmlSubField<Rq1RecordInterface, Rq1DerivativePlannedDates> implements Rq1FieldI<Rq1DerivativePlannedDates> {

    @EcvElementList("Rq1Data.Monitoring.Rq1RuleDescription")
    static final public Rq1RuleDescription derivativeFormatDesc = new Rq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.RQ1_DATA),
            "Syntax of derivative dates.",
            "The dates for derivatives in the milestones field of a release has to match a certain syntax.\n"
            + "\n"
            + "The warning 'Unexpected data read from RQ1 database.' is set on the release, if the syntax is wrong. "
            + "The problem with the syntax is descriped in the warning.");

    final private Rq1DataRule validRule = new Rq1DataRule(derivativeFormatDesc);

    private List<EcvXmlElement> xmlElements = null;

    public Rq1XmlSubField_DerivativesDate(Rq1RecordInterface parent, Rq1DatabaseField_Xml source, String elementName) {
        super(parent, source, elementName, ElementType.TEXT, new Rq1DerivativePlannedDates());
    }

    @Override
    public void loadValueFromDb(List<EcvXmlElement> xmlElements, Source source) {
        assert (xmlElements != null);
        assert (xmlElements.size() <= 1);
        assert (source != null);

        this.xmlElements = xmlElements;

        getParentRecord().removeMarkers(validRule);

        String xmlValue;
        if ((xmlElements.size() == 1) && (xmlElements.get(0) instanceof EcvXmlTextElement)) {
            xmlValue = ((EcvXmlTextElement) xmlElements.get(0)).getText();
        } else {
            setDataSourceValue(new Rq1DerivativePlannedDates(), source);
            return;
        }

        try {
            Rq1DerivativePlannedDates dbDate = new Rq1DerivativePlannedDates();
            dbDate.setXmlValue(xmlValue);
            setDataSourceValue(dbDate, source);
        } catch (Rq1ParseException | DateParseException ex) {
            StringBuilder s = new StringBuilder(50);
            s.append("Problem processing data from field ").append(getFieldName()).append(".");
            Rq1ParseFieldException containerEx = new Rq1ParseFieldException(s.toString(), ex);
            getParentRecord().setMarker(new Rq1UnexpectedDataFailure(validRule, getParentRecord(), this, containerEx));
            setDataSourceValue(new Rq1DerivativePlannedDates(), source);
        }

    }

    @Override
    public List<EcvXmlElement> provideValueAsXmlListForDb() {

        List<EcvXmlElement> list = new ArrayList<>();
        Rq1DerivativePlannedDates dates = getDataSourceValue();
        if (dates.isEmpty() == false) {
            EcvXmlTextElement xmlElement = new EcvXmlTextElement(super.getElementName(), dates.getXmlValue());
            list.add(xmlElement);
        }
        return (list);

    }

    @Override
    protected void dbValueWasWrittenToDb() {
        getParentRecord().removeMarkers(validRule);
        super.dbValueWasWrittenToDb();
    }

}
