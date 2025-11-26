/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.OslcCommandExecutor.ResponseFormat;
import OslcAccess.OslcSelection.DateCriteriaOslc;
import OslcAccess.OslcSelection.SelectionType;
import java.util.Collection;
import java.util.Map.Entry;

/**
 *
 * @author GUG2WI
 */
public class OslcCriteriaSearch extends OslcGetCommand {

    private OslcPropertySet requestedProperties;
    private OslcSelection selection;

    public OslcCriteriaSearch() {
        requestedProperties = new OslcPropertySet();
        selection = null;
    }

    public OslcCriteriaSearch(OslcSelection selection) {
        assert (selection != null);
        assert (selection.isCriteriaSet() == true);

        this.selection = selection;
        requestedProperties = new OslcPropertySet();
    }

    public OslcCriteriaSearch setSelection(OslcSelection selection) {
        assert (selection != null);
        assert (selection.isCriteriaSet() == true);
        assert (this.selection == null);
        this.selection = selection;
        return (this);
    }

    public OslcCriteriaSearch setProperties(OslcPropertySet requestedProperties) {
        assert (requestedProperties != null);
        assert (requestedProperties.isEmpty() == false);
        this.requestedProperties = requestedProperties;
        return (this);
    }

    public OslcCriteriaSearch addProperty(OslcProperty newProperty) {
        assert (newProperty != null);
        requestedProperties.addProperty(newProperty);
        return this;
    }

    public OslcCriteriaSearch addPropertyField(String propertyPath) {
        assert (propertyPath != null);
        assert (propertyPath.isEmpty() == false);
        requestedProperties.addPropertyField(propertyPath);
        return (this);
    }

    @Override
    public ResponseFormat getResponseFormat() {
        return (ResponseFormat.TEXT_OR_XML);
    }

    @Override
    public String buildCommandString(String oslcUrl) {
        assert (oslcUrl != null);
        assert (oslcUrl.isEmpty() == false);
        assert (selection != null);
        assert (selection.isCriteriaSet() == true);

        StringBuilder builder = new StringBuilder(100);

        switch (selection.getSelectionType()) {

            case RDF_ABOUT:
                appendRdfAboutCriteria(builder);
                break;
            case NAME:
                appendNameCriteria(oslcUrl, builder);
                break;
            case FILTER_CRITERIAS:
                appendFilterCriteria(oslcUrl, builder);
                break;
            default:
                throw (new Error("Unknown selection type:" + selection.getSelectionType().toString()));
        }

        //
        // Add requestedProperties
        //
        if (requestedProperties.getProperties().isEmpty() == false) {
            builder.append(getProtocolVersion().getPropertyToken()).append("=");
            appendProperties(builder, requestedProperties.getProperties());
            builder.append("&");
        }

        assert (builder.toString().isEmpty() == false) : "Type: " + selection.getSelectionType() + " "
                + "rdfAbout: " + selection.getRdfAbout();

        builder.append("oslc.pageSize=1000");

        return builder.toString();
    }

    private void appendRdfAboutCriteria(StringBuilder builder) {
        assert (selection.getRdfAbout() != null);
        builder.append(selection.getRdfAbout()).append("?");
    }

    private void appendNameCriteria(String oslcUrl, StringBuilder builder) {
        assert (oslcUrl != null);
        assert (oslcUrl.isEmpty() == false);
        assert (selection.getName() != null);

        builder.append(oslcUrl).append("record/?");
        builder.append("rcm.name=").append(selection.getName());
        if (selection.getRecordType() != null) {
            builder.append("&rcm.type=").append(selection.getRecordType().getOslcType());
        }

        builder.append("&");
    }

    private void appendFilterCriteria(String oslcUrl, StringBuilder builder) {
        assert (oslcUrl != null);
        assert (oslcUrl.isEmpty() == false);
        assert (selection.getRecordType() != null);
        assert ((selection.getReferences().isEmpty() == false)
                || (selection.getDateCriterias().isEmpty() == false)
                || (selection.getValues().isEmpty() == false)
                || (selection.getValueLists().isEmpty() == false));

        builder.append(oslcUrl).append("record/?");
        builder.append("rcm.type=").append(selection.getRecordType().getOslcType());
        builder.append("&oslc.where=");

        boolean isFirstCriteria = true;

        //
        // Add reference criterias
        //
        for (Entry<String, String> e : selection.getReferences().entrySet()) {
            if (isFirstCriteria == false) {
                builder.append(" and ");
            }
            isFirstCriteria = false;
            String names[] = e.getKey().split("\\.");
            switch (names.length) {
                case 1:
                    builder.append("cq:").append(names[0]).append("=<").append(encodeForHttp(e.getValue())).append(">");
                    break;
                case 2:
                    builder.append("cq:").append(names[0]).append("{cq:").append(names[1]).append("=<").append(encodeForHttp(e.getValue())).append(">}");
                    break;
                case 3:
                    builder.append("cq:").append(names[0]).append("{cq:").append(names[1]).append("{cq:").append(names[2]).append("=<").append(encodeForHttp(e.getValue())).append(">}}");
                    break;
                default:
                    throw new Error("Not supported names.length = " + names.length);
            }
        }

        //
        // Add fields with value lists
        //
        for (Entry<String, Collection<String>> e : selection.getValueLists().entrySet()) {
            if (isFirstCriteria == false) {
                builder.append(" and ");
            }
            isFirstCriteria = false;
            if (e.getValue().size() == 1) {
                //
                // Use '=' if only one value is given in the list.
                //
                builder.append("cq:").append(e.getKey()).append("=\"").append(encodeForHttp(e.getValue().iterator().next())).append("\"");
            } else {
                //
                // Create the value list
                //
                builder.append("cq:").append(e.getKey()).append(" in [");
                boolean isFirstValue = true;
                for (String value : e.getValue()) {
                    if (isFirstValue == false) {
                        builder.append(",");
                    }
                    isFirstValue = false;
                    builder.append("\"").append(encodeForHttp(value)).append("\"");
                }
                builder.append("]");
            }

        }

        //
        // Add fields with values
        //
        for (Entry<String, String> e : selection.getValues().entrySet()) {
            if (isFirstCriteria == false) {
                builder.append(" and ");
            }
            isFirstCriteria = false;
            String names[] = e.getKey().split("\\.");
            switch (names.length) {
                case 1:
                    builder.append("cq:").append(names[0]).append("=\"").append(encodeForHttp(e.getValue())).append("\"");
                    break;
                case 2:
                    builder.append("cq:").append(names[0]).append("{cq:").append(names[1]).append("=\"").append(encodeForHttp(e.getValue())).append("\"}");
                    break;
                case 3:
                    builder.append("cq:").append(names[0]).append("{cq:").append(names[1]).append("{cq:").append(names[2]).append("=\"").append(encodeForHttp(e.getValue())).append("\"}}");
                    break;
                case 4:
                    builder.append("cq:").append(names[0]).append("{cq:").append(names[1]).append("{cq:").append(names[2]).append("{cq:").append(names[3]).append("=\"").append(encodeForHttp(e.getValue())).append("\"}}}");
                    break;
                case 5:
                    builder.append("cq:").append(names[0]).append("{cq:").append(names[1]).append("{cq:").append(names[2]).append("{cq:").append(names[3]).append("{cq:").append(names[4]).append("=\"").append(encodeForHttp(e.getValue())).append("\"}}}}");
                    break;
                case 6:
                    builder.append("cq:").append(names[0]).append("{cq:").append(names[1]).append("{cq:").append(names[2]).append("{cq:").append(names[3]).append("{cq:").append(names[4]).append("{cq:").append(names[5]).append("=\"").append(encodeForHttp(e.getValue())).append("\"}}}}}");
                    break;
                default:
                    throw new Error("Not supported names.length = " + names.length);
            }

        }

        //
        // Add fields with dates
        //
        for (Entry<String, DateCriteriaOslc> e : selection.getDateCriterias().entrySet()) {
            if (isFirstCriteria == false) {
                builder.append(" and ");
            }
            isFirstCriteria = false;
            String names[] = e.getKey().split("\\.");
            switch (names.length) {
                case 1:
                    builder.append("cq:").append(names[0]).append(">=\"").append(e.getValue().getDate().getOslcValue()).append("\"");
                    break;
                case 2:
                    builder.append("cq:").append(names[0]).append("{cq:").append(names[1]).append(">=\"").append(e.getValue().getDate().getOslcValue()).append("\"}");
                    break;
                default:
                    throw new Error("Not supported names.length = " + names.length);
            }
        }

        builder.append("&");
    }

    private void appendProperties(StringBuilder builder, Collection<OslcProperty> properties) {
        boolean firstField = true;
        for (OslcProperty property : properties) {
            if (firstField == false) {
                builder.append(',');
            }
            firstField = false;
            builder.append(property.getName());
            if (property instanceof OslcPropertyRecord) {
                builder.append("{");
                appendProperties(builder, ((OslcPropertyRecord) property).getProperties());
                builder.append("}");
            }
        }
    }

    @Override
    public String getAddressForUi() {
        if (selection.getSelectionType() == SelectionType.RDF_ABOUT) {
            return (selection.getRdfAbout());
        } else if (selection.getSelectionType() == SelectionType.NAME) {
            return (selection.getName());
        } else if (selection.getSelectionType() == SelectionType.FILTER_CRITERIAS) {
            StringBuilder b = new StringBuilder(50);
            appendFilterCriteria("", b);
            return (b.toString());
        } else {
            return ("See log file.");
        }
    }

}
