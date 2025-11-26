/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gug2wi
 */
public class EcvXmlContainerElement extends EcvXmlElement implements EcvXmlContainerI, EcvParser.Parent {

    final private ArrayList<EcvXmlElement> content;

    public EcvXmlContainerElement(String elementName) {
        super(elementName);
        content = new ArrayList<>(5);
    }

    /**
     * Package private to enable access by parser.
     *
     * @param elementName
     * @param attributes
     */
    EcvXmlContainerElement(String elementName, Map<String, String> attributes) {
        super(elementName, attributes);
        content = new ArrayList<>(5);
    }

    private EcvXmlContainerElement(EcvXmlContainerElement source) {
        super(source);
        content = new ArrayList<>(5);
        for (EcvXmlElement element : source.content) {
            addElement(element.copy());
        }
    }

    private EcvXmlContainerElement(EcvXmlContainerElement source, String newElementName) {
        super(source, newElementName);
        content = new ArrayList<>(5);
        for (EcvXmlElement element : source.content) {
            addElement(element.copy());
        }
    }

    @Override
    final public void addElement(EcvXmlElement element) {
        assert (element != null);
//        assert (content.contains(element) == false);

        content.add(element);
        element.setParent(this);
    }

    @Override
    final public void removeElement(EcvXmlElement element) {
        assert (element != null);
        content.remove(element);
    }

    @Override
    final public boolean isEmpty() {
        return (content.isEmpty());
    }

    /**
     * Returns the first element with name thats fit. Note that several elements
     * with same name may exist.
     *
     * @param name
     * @return
     * @throws NotfoundException if no element with the given name was found.
     */
    public EcvXmlElement getOptionalElement(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        for (EcvXmlElement element : content) {
            if (element.getName().equals(name)) {
                return (element);
            }
        }

        return (null);
    }

    /**
     * Returns the first element with name thats fit. Note that several elements
     * with same name may exist.
     *
     * @param name
     * @return
     * @throws NotfoundException if no element with the given name was found.
     */
    public EcvXmlElement getElement(String name) throws NotfoundException {
        assert (name != null);
        assert (name.isEmpty() == false);

        for (EcvXmlElement element : content) {
            if (element.getName().equals(name)) {
                return (element);
            }
        }

        throw (new EcvXmlElement.NotfoundException("getElement", getFullName(), name));
    }

    /**
     * Checks whether the container contains at least one element of the given
     * name.
     *
     * @param name Name of the requested element.
     * @return True, if at least one element with this name exist. False
     * otherwise.
     */
    public boolean containsElement(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        for (EcvXmlElement element : content) {
            if (element.getName().equals(name)) {
                return (true);
            }
        }
        return (false);
    }

    /**
     * Checks whether the container contains at least one container of the given
     * name.
     *
     * @param name Name of the requested container.
     * @return True, if at least one element with this name exist. False
     * otherwise.
     */
    public boolean containsContainer(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        for (EcvXmlElement element : content) {
            if ((element.getName().equals(name)) && (element instanceof EcvXmlContainerElement)) {
                return (true);
            }
        }
        return (false);
    }

    /**
     * Returns the first container with the given name, if such a container
     * exists.
     *
     * @param name Name of the wanted container.
     * @return The first container with the given name or null.
     */
    public EcvXmlContainerElement getOptionalContainerElement(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        for (EcvXmlElement element : content) {
            if ((element instanceof EcvXmlContainerElement)
                    && (element.getName().equals(name))) {
                return ((EcvXmlContainerElement) element);
            }
        }
        return (null);
    }

    /**
     * Returns the first container with the given name.
     *
     * @param name Name of the wanted container.
     * @return The first container with the given name.
     * @throws util.EcvXmlElement.NotfoundException If no container with the
     * given name exists.
     */
    public EcvXmlContainerElement getContainerElement(String name) throws NotfoundException {
        assert (name != null);
        assert (name.isEmpty() == false);

        for (EcvXmlElement element : content) {
            if ((element instanceof EcvXmlContainerElement)
                    && (element.getName().equals(name))) {
                return ((EcvXmlContainerElement) element);
            }
        }

        throw (new EcvXmlElement.NotfoundException("getContainerElement", getFullName(), name));
    }

    /**
     * Searches for the first container with the given name.
     *
     * @param name The name to search for.
     * @return The first container with the matching name or null, if no
     * container with the matching name exists.
     */
    public EcvXmlContainerElement findFirstContainerElement(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        for (EcvXmlElement element : content) {
            if ((element instanceof EcvXmlContainerElement)
                    && (element.getName().equals(name))) {
                return ((EcvXmlContainerElement) element);
            }
        }

        return (null);
    }

    /**
     * Returns the first text element with the given name.
     *
     * @param name Name of the wanted element.
     * @return The found element.
     * @throws util.EcvXmlElement.NotfoundException If the container does not
     * contain an text element with the given name.
     */
    public EcvXmlTextElement getTextElement(String name) throws NotfoundException {
        assert (name != null);
        assert (name.isEmpty() == false);

        for (EcvXmlElement element : content) {
            if ((element instanceof EcvXmlTextElement)
                    && (element.getName().equals(name))) {
                return ((EcvXmlTextElement) element);
            }
        }

        throw (new EcvXmlElement.NotfoundException("getTextElement", getFullName(), name));
    }

    /**
     * Returns the text of the first element with the given name. If the first
     * matching element is a text element, the content of this element is
     * returned. If the first matching element is an empty element, then an
     * empty string is returned.
     *
     * @param name Name of the wanted element.
     * @return The text content of the found element.
     * @throws util.EcvXmlElement.NotfoundException If the container does not
     * contain an text element or empty element with the given name.
     */
    public String getText(String name) throws NotfoundException {
        assert (name != null);
        assert (name.isEmpty() == false);

        for (EcvXmlElement element : content) {
            if ((element instanceof EcvXmlTextElement)
                    && (element.getName().equals(name))) {
                return (((EcvXmlTextElement) element).getText());
            } else if ((element instanceof EcvXmlEmptyElement)
                    && (element.getName().equals(name))) {
                return ("");
            }
        }

        throw (new EcvXmlElement.NotfoundException("getText", getFullName(), name));
    }

    /**
     * Returns the text of the first element with the given name.
     * <li> If the first matching element is a text element, the content of this
     * element is returned.
     * <li>If the first matching element is an empty element, then an empty
     * string is returned.
     * <li>If the first matching element is an combined element or container
     * element, then the XML content of the combined element or container
     * element is returned as text.
     *
     * @param name Name of the wanted element.
     * @return The text content of the found element.
     * @throws util.EcvXmlElement.NotfoundException If the container does not
     * contain an text element of empty element with the given name.
     */
    public String getHtmlText(String name) throws NotfoundException {
        assert (name != null);
        assert (name.isEmpty() == false);

        for (EcvXmlElement element : content) {
            if (element.getName().equals(name)) {
                if (element instanceof EcvXmlTextElement) {
                    return (((EcvXmlTextElement) element).getText());
                } else if (element instanceof EcvXmlEmptyElement) {
                    return ("");
                } else if (element instanceof EcvXmlCombinedElement) {
                    return (((EcvXmlCombinedElement) element).getXmlString_WithoutContainer(EncodeFormat.SHORT_EMPTY_VALUE));
                } else if (element instanceof EcvXmlContainerElement) {
                    return (((EcvXmlContainerElement) element).getXmlString_WithoutContainer(EncodeFormat.SHORT_EMPTY_VALUE));
                }
            }
        }

        throw (new EcvXmlElement.NotfoundException("getText", getFullName(), name));
    }

    /**
     * Searches for the first text element or empty element with the given name.
     *
     * @param name The name to search for.
     * @return The text of the first text element, the empty string of the first
     * element was an empty element or null if no matching element was found.
     */
    public String findFirstText(String name) {
        try {
            return (getText(name));
        } catch (NotfoundException ex) {
            return (null);
        }
    }

    public List<EcvXmlContainerElement> getContainerElementList(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        ArrayList<EcvXmlContainerElement> list = new ArrayList<>();
        for (EcvXmlElement element : content) {
            if ((element instanceof EcvXmlContainerElement)
                    && (element.getName().equals(name))) {
                list.add((EcvXmlContainerElement) element);
            }
        }
        return (list);
    }
    
    public List<EcvXmlContainerElement> getContainerElementList_AttributeContains(String name, String attribute, String attributeContent) {
        assert (name != null);
        assert (name.isEmpty() == false);

        ArrayList<EcvXmlContainerElement> list = new ArrayList<>();
        for (EcvXmlElement element : content) {
            if ((element instanceof EcvXmlContainerElement)
                    && (element.getName().equals(name)) && (element.hasAttribute(attribute)) && (element.getAttribute(attribute).contains(attributeContent))) {
                list.add((EcvXmlContainerElement) element);
            }
        }
        return (list);
    }
    
        public List<EcvXmlContainerElement> getContainerElementList_AttributeEquals(String name, String attribute, String attributeContent) {
        assert (name != null);
        assert (name.isEmpty() == false);

        ArrayList<EcvXmlContainerElement> list = new ArrayList<>();
        for (EcvXmlElement element : content) {
            if ((element instanceof EcvXmlContainerElement)
                    && (element.getName().equals(name)) && (element.hasAttribute(attribute)) && (element.getAttribute(attribute).equals(attributeContent))) {
                list.add((EcvXmlContainerElement) element);
            }
        }
        return (list);
    }

    /**
     * Returns all container elements contained in the object.
     *
     * @return The list of all container elements within this object. The list
     * might be empty.
     */
    public List<EcvXmlContainerElement> getContainerElementList() {
        ArrayList<EcvXmlContainerElement> list = new ArrayList<>();
        for (EcvXmlElement element : content) {
            if (element instanceof EcvXmlContainerElement) {
                list.add((EcvXmlContainerElement) element);
            }
        }
        return (list);
    }

    @Override
    public List<EcvXmlElement> getElementList() {
        return (new ArrayList<>(content));
    }

    /**
     * Returns all elements that match the given tag name.
     *
     * @param name The name of tag for which the elements shall be returned.
     * @return All elements whose tag name matches the given name.
     */
    @Override
    public List<EcvXmlElement> getElementList(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        ArrayList<EcvXmlElement> list = new ArrayList<>();
        for (EcvXmlElement element : content) {
            if (element.getName().equals(name)) {
                list.add(element);
            }
        }
        return (list);
    }

    /**
     * Returns true, if at least one element with the given name exists in the
     * container.
     *
     * @param name The name of the searched element.
     * @return true, if at least one element with the ame exists; false
     * otherwise.
     */
    public boolean hasElement(String name) {
        for (EcvXmlElement element : content) {
            if (element.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String toString(String prefix) {
        StringBuilder builder = new StringBuilder(30);

        builder.append(prefix);
        builder.append("EcvXmlContainerElement: <");
        builder.append(getName());
        addAttributeString(builder);
        builder.append(">\n");

        for (EcvXmlElement e : content) {
            builder.append(e.toString(prefix + "    "));
            builder.append("\n");
        }

        builder.append(prefix);
        builder.append("</");
        builder.append(getName());
        builder.append(">");

        return (builder.toString());
    }

    @Override
    protected String toStringSorted(String prefix) {
        StringBuilder builder = new StringBuilder(30);

        builder.append(prefix);
        builder.append("EcvXmlContainerElement: <");
        builder.append(getName());
        addAttributeString(builder);
        builder.append(">\n");

        List<String> sortableContent = new ArrayList<>();
        for (EcvXmlElement e : content) {
            sortableContent.add(e.toStringSorted(prefix + "    "));
        }
        sortableContent.sort((String t, String t1) -> {
            return (t.compareTo(t1));
        });

        for (String s : sortableContent) {
            builder.append(s);
            builder.append("\n");
        }

        builder.append(prefix);
        builder.append("</");
        builder.append(getName());
        builder.append(">");

        return (builder.toString());
    }

    @Override
    protected String getUiString(String prefix) {
        StringBuilder builder = new StringBuilder(30);

        builder.append(prefix);
        builder.append('<');
        builder.append(getName());
        addAttributeString(builder);
        builder.append(">");

        builder.append("\n");

        for (EcvXmlElement e : content) {
            builder.append(e.getUiString(prefix + "    "));
            builder.append("\n");
        }

        builder.append(prefix);
        builder.append("</");
        builder.append(getName());
        builder.append(">");

        return (builder.toString());
    }

    public String getUiString_WithoutContainer() {
        StringBuilder builder = new StringBuilder(30);

        for (EcvXmlElement e : content) {
            builder.append(e.getUiString());
//            builder.append("\n");
        }

        return (builder.toString());
    }

    @Override
    public String getXmlString(EncodeFormat encodeFormat) {
        assert (encodeFormat != null);

        StringBuilder builder = new StringBuilder(30);

        builder.append('<');
        builder.append(getName());
        addAttributeString(builder);
        builder.append(">");

        if (content.isEmpty() == false) {
            //
            // Do not add a new line if the container is empty.
            // Otherwise, a new line is created as content when the empty container is parsed.
            //
            builder.append("\n");
        }
        for (EcvXmlElement e : content) {
            builder.append(e.getXmlString(encodeFormat));
            builder.append("\n");
        }

        builder.append("</");
        builder.append(getName());
        builder.append(">");

        return (builder.toString());
    }

    public String getXmlString_WithoutContainer(EncodeFormat encodeFormat) {
        assert (encodeFormat != null);

        StringBuilder builder = new StringBuilder(30);

        boolean firstElement = true;

        for (EcvXmlElement e : content) {
            if (firstElement == false) {
                builder.append("\n");
            }
            firstElement = false;
            builder.append(e.getXmlString(encodeFormat));
        }

        return (builder.toString());
    }

    @Override
    public EcvXmlElement copy() {
        return (new EcvXmlContainerElement(this));
    }

    public EcvXmlElement copy(String newElementName) {
        assert (newElementName != null);
        assert (newElementName.isEmpty() == false);
        return (new EcvXmlContainerElement(this, newElementName));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return (true);
        } else if (o instanceof EcvXmlContainerElement) {
            EcvXmlContainerElement e = (EcvXmlContainerElement) o;
            return (equalsNameAndAttributes(e) && (content.containsAll(e.content)) && (e.content.containsAll(content)));
        } else if (o instanceof EcvXmlEmptyElement) {
            EcvXmlEmptyElement e = (EcvXmlEmptyElement) o;
            return (isEmpty() && equalsNameAndAttributes(e));
        } else {
            return (false);
        }
    }
    
}
