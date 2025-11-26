/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import static util.EcvXmlElement.encodeXml;

/**
 * An XML element that can contain elements, comments and text in any order.
 *
 * @author gug2wi
 */
public class EcvXmlCombinedElement extends EcvXmlElement implements EcvXmlContainerI {

    final public static String COMMENT_START = "<!--";
    final public static String COMMENT_END = "-->";
    final public static int MAX_COMMENT_LENGTH = 10000;
    final public static String CDATA_START = "<![CDATA[";
    final public static String CDATA_END = "]]>";
    final public static int MAX_CDATA_LENGTH = 10000;

    static public abstract class ContentPart {

        public abstract ContentPart copy();

        @Override
        final public String toString() {
            return (toString(""));
        }

        protected abstract String toString(String prefix);

        protected abstract String getUiString(String prefix);

        protected abstract String getXmlString(EncodeFormat encodeFormat);
    }

    /**
     * A non empty text content.
     */
    static public class TextPart extends ContentPart {

        final private String text;

        public TextPart(String text) {
            assert (text != null);
            assert (text.isEmpty() == false);
            this.text = text;
        }

        public String getText() {
            return text;
        }

        @Override
        public ContentPart copy() {
            return (new TextPart(text));
        }

        @Override
        protected String toString(String prefix) {
            return (prefix + "TextPart: " + text);
        }

        @Override
        protected String getUiString(String prefix) {
            return (text);
        }

        @Override
        protected String getXmlString(EncodeFormat encodeFormat) {
            return (encodeXml(text));
        }

    }

    /**
     * Contains an element.
     */
    static public class ElementPart extends ContentPart {

        final private EcvXmlElement element;

        public ElementPart(EcvXmlElement element) {
            assert (element != null);
            this.element = element;
        }

        public EcvXmlElement getElement() {
            return (element);
        }

        @Override
        public ContentPart copy() {
            return (new ElementPart(element.copy()));
        }

        @Override
        protected String toString(String prefix) {
            return (prefix + "ElementContent:\n" + element.toString(prefix));
        }

        @Override
        protected String getUiString(String prefix) {
            return (element.getUiString(prefix));
        }

        @Override
        protected String getXmlString(EncodeFormat encodeFormat) {
            return (element.getXmlString(encodeFormat));
        }
    }

    /**
     * Contains the text of a comment. May contain an empty text.
     */
    static public class CommentPart extends ContentPart {

        final private String commentText;

        public CommentPart(String commentText) {
            assert (commentText != null);
            this.commentText = commentText;
        }

        public String getCommentText() {
            return (commentText);
        }

        @Override
        public ContentPart copy() {
            return (new CommentPart(commentText));
        }

        @Override
        protected String toString(String prefix) {
            return (prefix + "CommentContent: " + commentText);
        }

        @Override
        protected String getUiString(String prefix) {
            return (COMMENT_START + commentText + COMMENT_END);
        }

        @Override
        protected String getXmlString(EncodeFormat encodeFormat) {
            return (COMMENT_START + commentText + COMMENT_END);
        }
    }

    /**
     * Contains the text of a CDATA element. May contain an empty text.
     */
    static public class CDataPart extends ContentPart {

        final private String dDataText;

        public CDataPart(String cDataText) {
            assert (cDataText != null);
            this.dDataText = cDataText;
        }

        public String getCDataText() {
            return (dDataText);
        }

        @Override
        public ContentPart copy() {
            return (new CDataPart(dDataText));
        }

        @Override
        protected String toString(String prefix) {
            return (prefix + "CDataContent: " + dDataText);
        }

        @Override
        protected String getUiString(String prefix) {
            return (CDATA_START + dDataText + CDATA_END);
        }

        @Override
        protected String getXmlString(EncodeFormat encodeFormat) {
            return (CDATA_START + dDataText + CDATA_END);
        }
    }

    final private List<ContentPart> content;

    public EcvXmlCombinedElement(String elementName) {
        super(elementName);
        content = new ArrayList<>();
    }

    @SuppressWarnings("LeakingThisInConstructor")
    protected EcvXmlCombinedElement(String elementName, Map<String, String> attributes, List<ContentPart> content) {
        super(elementName, attributes);
        assert (content != null);
        this.content = content;
        for (ContentPart part : content) {
            if (part instanceof ElementPart) {
                ((ElementPart) part).getElement().setParent(this);
            }
        }
    }

    private EcvXmlCombinedElement(EcvXmlCombinedElement source) {
        super(source);
        content = new ArrayList<>();
        for (ContentPart part : source.content) {
            content.add(part);
        }
    }

    @Override
    public boolean isEmpty() {
        return (content.isEmpty());
    }

    @Override
    public List<EcvXmlElement> getElementList() {
        List<EcvXmlElement> result = new ArrayList<>();
        for (ContentPart part : content) {
            if (part instanceof ElementPart) {
                result.add(((ElementPart) part).getElement());
            }
        }
        return (result);
    }

    @Override
    public List<EcvXmlElement> getElementList(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        List<EcvXmlElement> result = new ArrayList<>();
        for (ContentPart part : content) {
            if (part instanceof ElementPart) {
                EcvXmlElement element = ((ElementPart) part).getElement();
                if (element.getName().endsWith(name) == true) {
                    result.add(element);
                }
            }
        }
        return (result);
    }

    @Override
    public void addElement(EcvXmlElement element) {
        assert (element != null);
        content.add(new ElementPart(element));
    }

    public void addElements(Collection<EcvXmlElement> elements) {
        assert (elements != null);
        for (EcvXmlElement element : elements) {
            addElement(element);
        }
    }

    @Override
    public void removeElement(EcvXmlElement element) {
        assert (element != null);
        ContentPart partToDelete = null;
        for (ContentPart part : content) {
            if (part instanceof ElementPart) {
                if (((ElementPart) part).getElement() == element) {
                    partToDelete = part;
                    break;
                }
            }
        }
        if (partToDelete != null) {
            content.remove(partToDelete);
        }
    }

    public String getText() {
        StringBuilder result = new StringBuilder();
        for (ContentPart part : content) {
            if (part instanceof TextPart) {
                result.append(((TextPart) part).getText());
            }
        }
        return (result.toString());
    }

    public void addText(String text) {
        assert (text != null);
        assert (text.isEmpty() == false);
        content.add(new TextPart(text));
    }

    public void removeText() {
        ContentPart partToDelete;
        do {
            partToDelete = null;
            for (ContentPart part : content) {
                if (part instanceof TextPart) {
                    partToDelete = part;
                    break;
                }
            }
            if (partToDelete != null) {
                content.remove(partToDelete);
            }
        } while (partToDelete != null);
    }

    public void addCData(String data) {
        assert (data != null);
        content.add(new CDataPart(data));
    }

    @Override
    public EcvXmlElement copy() {
        return (new EcvXmlCombinedElement(this));
    }

    @Override
    protected String toString(String prefix) {
        StringBuilder builder = new StringBuilder();

        builder.append(prefix);
        builder.append("EcvXmlCombinedElement: <");
        builder.append(getName());
        addAttributeString(builder);
        builder.append(">\n");

        for (ContentPart part : content) {
            builder.append(part.toString(prefix + "    "));
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

        for (ContentPart part : content) {
            builder.append(part.getUiString(prefix + "    "));
        }

        builder.append(prefix);
        builder.append("</");
        builder.append(getName());
        builder.append(">");

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

        for (ContentPart part : content) {
            builder.append(part.getXmlString(encodeFormat));
        }

        builder.append("</");
        builder.append(getName());
        builder.append(">");

        return (builder.toString());
    }

    public String getXmlString_WithoutContainer(EncodeFormat encodeFormat) {
        assert (encodeFormat != null);

        StringBuilder builder = new StringBuilder(30);

        for (ContentPart part : content) {
            builder.append(part.getXmlString(encodeFormat));
        }

        return (builder.toString());
    }

}
