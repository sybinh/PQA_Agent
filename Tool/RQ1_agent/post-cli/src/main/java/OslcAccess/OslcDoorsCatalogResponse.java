/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import OslcAccess.Exceptions.FieldNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 * Implements the access to the elements in the doors response.
 *
 * @author gug2wi
 */
public class OslcDoorsCatalogResponse {

    public enum ElementType {
        PROJECT,
        FOLDER,
        MODULE;
    }

    public static class Element {

        final private ElementType type;
        final private String title;
        final private String serviceRdfAbout;
        final private String catalogRdfAbout;

        private Element(ElementType type, String title, String serviceRdfAbout, String catalogRdfAbout) {
            assert (type != null);
            assert (title != null);
            assert (title.isEmpty() == false);

            this.type = type;
            this.title = title;
            this.serviceRdfAbout = serviceRdfAbout;
            this.catalogRdfAbout = catalogRdfAbout;
        }

        public ElementType getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getServiceRdfAbout() {
            return serviceRdfAbout;
        }

        public String getCatalogRdfAbout() {
            return catalogRdfAbout;
        }

        @Override
        public String toString() {
            return (type.name() + " - " + title);
        }

    }

    final private EcvXmlContainerElement serviceProviderCatalog;
    private List<Element> content = null;

    OslcDoorsCatalogResponse(OslcResponse oslcResponse) throws FieldNotFoundException {
        assert (oslcResponse != null);

        if (oslcResponse.getResponseBodyList().isEmpty() || oslcResponse.getResponseBodyList().get(0) == null) {
            throw (new FieldNotFoundException("No body received or body is not in XML format."));
        } else {
            EcvXmlContainerElement body = oslcResponse.getResponseBodyList().get(0);
            try {
                serviceProviderCatalog = body.getContainerElement("oslc:ServiceProviderCatalog");
            } catch (EcvXmlElement.NotfoundException ex) {
                throw (new FieldNotFoundException("No oslc:ServiceProviderCatalog found in response.", body, ex));
            }
        }
    }

    final public String getTitle() throws FieldNotFoundException {
        try {
            return (serviceProviderCatalog.getText("dcterms:title"));
        } catch (EcvXmlElement.NotfoundException ex) {
            throw (new FieldNotFoundException("No dcterms:title found in response.", serviceProviderCatalog, ex));
        }
    }

    final public String getDescription() {
        try {
            return (serviceProviderCatalog.getText("dcterms:description"));
        } catch (EcvXmlElement.NotfoundException ex) {
            return (null);
        }
    }

    final public String getCatalogRdfAbout() {
        return (serviceProviderCatalog.getAttribute("rdf:about"));
    }

    final public List<Element> getElements() throws FieldNotFoundException {
        if (content == null) {
            content = new ArrayList<>();
            Map<String, String> serviceMap = extractElements("oslc:serviceProvider", "oslc:ServiceProvider");
            Map<String, String> catalogMap = extractElements("oslc:serviceProviderCatalog", "oslc:ServiceProviderCatalog");

            for (Map.Entry<String, String> service : serviceMap.entrySet()) {
                String serviceTitle = service.getKey();
                String serviceRdfAbout = service.getValue();

                String catalogRdfAbout = catalogMap.get(serviceTitle);

                if (catalogRdfAbout != null) {
                    //
                    // Element is service and Catalog. This means it is a project.
                    //
                    content.add(new Element(ElementType.PROJECT, serviceTitle, serviceRdfAbout, catalogRdfAbout));
                    catalogMap.remove(serviceTitle);
                } else {
                    //
                    // Element is only a service. This means it is a module.
                    //
                    content.add(new Element(ElementType.MODULE, serviceTitle, serviceRdfAbout, null));
                }
            }

            for (Map.Entry<String, String> catalog : catalogMap.entrySet()) {
                //
                // Element is only a catalog. This means it is a folder.
                //
                content.add(new Element(ElementType.FOLDER, catalog.getKey(), null, catalog.getValue()));
            }

        }
        return (content);
    }

    private Map<String, String> extractElements(String nameLevel1, String nameLevel2) throws FieldNotFoundException {
        Map<String, String> result = new TreeMap<>();

        for (EcvXmlContainerElement subContainer : serviceProviderCatalog.getContainerElementList(nameLevel1)) {
            EcvXmlContainerElement subsubContainer;
            try {
                subsubContainer = subContainer.getContainerElement(nameLevel2);
            } catch (EcvXmlElement.NotfoundException ex) {
                throw (new FieldNotFoundException("No " + nameLevel2 + " found in sub element.", serviceProviderCatalog, ex));
            }
            String rdfAbout = subsubContainer.getAttribute("rdf:about");
            if (rdfAbout == null) {
                throw (new FieldNotFoundException("No rdfAbout found in sub element " + nameLevel2 + ".", serviceProviderCatalog));
            }
            String title;
            try {
                title = subsubContainer.getText("dcterms:title");
            } catch (EcvXmlElement.NotfoundException ex) {
                throw (new FieldNotFoundException("No dcterms:title found in sub element " + rdfAbout + " in " + nameLevel2 + ".", serviceProviderCatalog, ex));
            }
            result.put(title, rdfAbout);
        }

        return (result);
    }

}
