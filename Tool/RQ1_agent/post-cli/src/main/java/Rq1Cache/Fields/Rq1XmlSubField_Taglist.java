/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsField_XmlSourceI;
import DataStore.DsField_XmlSubField;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Cache.Records.Rq1RecordInterface;
import java.util.ArrayList;
import java.util.List;
import util.EcvXmlCombinedElement;
import util.EcvXmlElement;

/**
 * Implements access to the taglist stored in the tag fields of BC releases.
 * This taglist contains a CDDATA element that as to be preserved.
 *
 * @author GUG2WI
 */
public class Rq1XmlSubField_Taglist extends DsField_XmlSubField<Rq1RecordInterface, String> implements Rq1FieldI_Text {

    private static final String ATTRIBUTE_URL = "URL";

    public Rq1XmlSubField_Taglist(Rq1RecordInterface parent, DsField_XmlSourceI source, String elementName) {
        super(parent, source, elementName, ElementType.ANY, "");
    }

    @Override
    protected void loadValueFromDb(List<EcvXmlElement> xmlElements, Source source) {
        assert (xmlElements != null);
        assert (xmlElements.size() <= 1);

        if (xmlElements.size() == 1) {
            String url = xmlElements.get(0).getAttribute(ATTRIBUTE_URL);
            setDataSourceValue(url, source);
        } else {
            setDataSourceValue(null, source);
        }
    }

    @Override
    protected List<EcvXmlElement> provideValueAsXmlListForDb() {
        List<EcvXmlElement> value = new ArrayList<>();

        String url = getDataSourceValue();
        if ((url != null) && (url.isEmpty() == false)) {
            EcvXmlCombinedElement xmlElement = new EcvXmlCombinedElement(getElementName());
            xmlElement.addAttribute(ATTRIBUTE_URL, url);
            xmlElement.addCData(url);
            value.add(xmlElement);
        }

        return (value);
    }

}
