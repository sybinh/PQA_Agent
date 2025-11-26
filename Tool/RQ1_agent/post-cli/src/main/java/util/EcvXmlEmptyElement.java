/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Map;

/**
 *
 * @author gug2wi
 */
public class EcvXmlEmptyElement extends EcvXmlElement {

    public EcvXmlEmptyElement(String elementName) {
        super(elementName);
    }

    protected EcvXmlEmptyElement(String elementName, Map<String, String> attributes) {
        super(elementName, attributes);
    }

    public EcvXmlEmptyElement(EcvXmlEmptyElement source) {
        super(source);
    }

    @Override
    public boolean isEmpty() {
        return (true);
    }

    @Override
    protected String toString(String prefix) {
        return (prefix + "EcvXmlEmptyElement:" + getXmlString(EncodeFormat.SHORT_EMPTY_VALUE));
    }

    @Override
    protected String getUiString(String prefix) {
        return (prefix + getXmlString(EncodeFormat.SHORT_EMPTY_VALUE));
    }

    @Override
    public String getXmlString(EncodeFormat encodeFormat) {
        assert (encodeFormat != null);

        StringBuilder builder = new StringBuilder(30);

        if (encodeFormat == EncodeFormat.SHORT_EMPTY_VALUE) {
            builder.append('<');
            builder.append(getName());
            addAttributeString(builder);
            builder.append("/>");
        } else if (encodeFormat == EncodeFormat.LONG_EMPTY_VALUE) {
            builder.append('<');
            builder.append(getName());
            addAttributeString(builder);
            builder.append(">");
            builder.append("</");
            builder.append(getName());
            builder.append(">");
        } else {
            throw (new Error("Unexpected encode format :" + encodeFormat.toString()));
        }

        return (builder.toString());
    }

    @Override
    public EcvXmlElement copy() {
        return (new EcvXmlEmptyElement(this));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EcvXmlEmptyElement) {
            EcvXmlEmptyElement e = (EcvXmlEmptyElement) o;
            return (equalsNameAndAttributes(e));
        } else if (o instanceof EcvXmlElement) {
            EcvXmlElement e = (EcvXmlElement) o;
            return (e.isEmpty() && equalsNameAndAttributes(e));
        } else {
            return (false);
        }
    }
}
