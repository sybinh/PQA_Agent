/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static util.EcvXmlCombinedElement.CDATA_END;
import static util.EcvXmlCombinedElement.CDATA_START;
import util.EcvXmlCombinedElement.CDataPart;
import static util.EcvXmlCombinedElement.COMMENT_END;
import static util.EcvXmlCombinedElement.COMMENT_START;
import util.EcvXmlCombinedElement.CommentPart;
import util.EcvXmlCombinedElement.ContentPart;
import util.EcvXmlCombinedElement.ElementPart;
import static util.EcvXmlCombinedElement.MAX_CDATA_LENGTH;
import static util.EcvXmlCombinedElement.MAX_COMMENT_LENGTH;
import util.EcvXmlCombinedElement.TextPart;

/**
 * Creates a tree of EcvXmlElements out of a text containing XML.
 *
 * @author gug2wi
 */
public class EcvXmlParser extends EcvParser {

    static final private String[] htmlSingleTags = {"br", "BR", "hr", "HR"};

    /**
     * Creates a parser for parsing the provided XML string.
     *
     * @param xmlString XML string that shall be parsed.
     */
    public EcvXmlParser(String xmlString) {
        super(xmlString);
    }

    public EcvXmlParser(File file) throws FileNotFoundException {
        super(file);
    }

    /**
     * Creates a parser for parsing from the provided InputStreamReader.
     *
     * @param inputReader Reader from which the XML shall be parsed.
     */
    public EcvXmlParser(InputStreamReader inputReader) {
        super(inputReader);
    }

    public EcvXmlParser(EcvPositionAwarePushbackReader pushBackReader) {
        super(pushBackReader);
    }

    public EcvXmlElement parse() throws ParseException {
        if (checkEnd() != true) {
            skipHeaderElements();
        }
        if (checkEnd() != true) {
            try {
                return parseElement(null);
            } catch (java.lang.AssertionError ex) {
                throw (new ParseException("AssertionError during parsing.", input, ex));
            }
        }
        return null;
    }

    private EcvXmlElement parseElement(EcvXmlContainerElement parent) throws ParseException {
        currentParent = parent;

        //----------------------------------------------------------------------
        //
        // Sometimes we find version strings in tags -> skip them
        //
        //----------------------------------------------------------------------
        skipSpecialElements();

        //----------------------------------------------------------------------
        //
        // Parse starting tag with name
        //
        //----------------------------------------------------------------------
        String elementName;

        skipWhitespace();
        expectString("<");
        elementName = parseName();
        skipWhitespace();

        //----------------------------------------------------------------------
        //
        // Parse attributes
        //
        //----------------------------------------------------------------------
        Map<String, String> attributes = new TreeMap<>();

        while ((checkCharacter('>') == false) && (checkCharacter('/') == false)) {

            String attributeName = parseName();
            if (checkCharacter('=') == true) {
                expectString("=");
                if (checkString("\r\n") == true) {
                    //
                    // This is a special handling added for response from ALM.
                    // Sometimes, the parameter for an attributes comes on the next line.
                    //
                    skipWhitespace();
                }
                String value = EcvXmlElement.decodeXml(parseQuotedText());
                attributes.put(attributeName, value);
            } else {
                attributes.put(attributeName, null);
            }
            skipWhitespace();
        }

        //----------------------------------------------------------------------
        //
        // Check, if it is an empty tag.
        //
        //----------------------------------------------------------------------
        if (checkCharacter('/')) {
            expectString("/>");
            return new EcvXmlEmptyElement(elementName, attributes);
        }

        // It is not an empty tag
        expectCharacter('>');

        //----------------------------------------------------------------------
        //
        // Check if this is a HTML tag without closing tag
        //
        //----------------------------------------------------------------------
        for (String name : htmlSingleTags) {
            if (name.equals(elementName)) {
                //
                // Note: This is not the correct element type because these tags are not realy empty tags.
                // But we only want to read the tags. We do not support the writting of such tags. So this
                // solution is sufficient.
                //
                String endTag = "</" + elementName + ">";
                if (checkString(endTag) == true) {
                    // In some cases, these empty tags might be followed by an closing tag.
                    expectString(endTag);
                }
                return new EcvXmlEmptyElement(elementName, attributes);
            }
        }

        //----------------------------------------------------------------------
        //
        // Parse content of element
        //
        //----------------------------------------------------------------------
        EcvXmlContainerElement tmpContainer = null; // Used as parent when parsing sub-elements to provide a full name for error messages.
        List<ContentPart> content = new ArrayList<>();

        //
        // Loop till end tag is reached.
        //
        while (checkString("</") == false) {

            if (checkString(COMMENT_START) == true) {
                //
                // Handle comments within content
                //
                expectString(COMMENT_START);
                content.add(new CommentPart(parseWithoutEndString(COMMENT_END, MAX_COMMENT_LENGTH)));
            } else if (checkString(CDATA_START) == true) {
                //
                // Handle text in CDATA
                //
                expectString(CDATA_START);
                content.add(new CDataPart(parseWithoutEndString(CDATA_END, MAX_CDATA_LENGTH)));
            } else if ((checkCharacter('<') == true) && (checkString("</") == false)) {
                //
                // Handle start tag
                //
                if (tmpContainer == null) {
                    tmpContainer = new EcvXmlContainerElement(elementName, attributes);
                }
                content.add(new ElementPart(parseElement(tmpContainer)));
                currentParent = parent;
            } else {
                //
                // Handle encoded text
                //
                StringBuilder b = new StringBuilder();
                do {
                    String encodedText = parseTextExclusive('<');
                    b.append(EcvXmlElement.decodeXml(encodedText));

                    //
                    // Special handling for <br/>, which has to be handled like an text element.
                    // Could be replaced by a more sophisticated and general code. But it works for now.
                    //
                    if (checkString("<br/>")) {
                        expectString("<br/>");
                        b.append("\n");
                    }
                    // End of special handling for <br/>

                    encodedText = parseTextExclusive('<');
                    b.append(EcvXmlElement.decodeXml(encodedText));

                } while (checkString("<br/>"));

                content.add(new TextPart(b.toString()));
            }

        }

        //----------------------------------------------------------------------
        //
        // Create element fitting to content
        //
        //----------------------------------------------------------------------
        EcvXmlElement newElement = createElement(elementName, attributes, content, tmpContainer);

        //----------------------------------------------------------------------
        //
        // Handle end tag
        //
        //----------------------------------------------------------------------
        expectString("</");
        String endTagName = parseName();

        if (elementName.equals(endTagName) == false) {
            StringBuilder builder = new StringBuilder(80);
            builder.append("Wrong name in end tag at ").append(input.getPosition().toString()).append("\n");
            builder.append("Expected name: ").append(elementName).append("\n");
            builder.append("Found name: ").append(endTagName);
            throw new ParseException(builder.toString(), input, parent);
        }

        skipWhitespace();

        expectString(">");

        return newElement;
    }

    private EcvXmlElement createElement(String elementName, Map<String, String> attributes, List<ContentPart> content, EcvXmlContainerElement tmpContainer) throws ParseException {

        //
        // No content -> empty element
        //
        if (content.isEmpty() == true) {
            return (new EcvXmlEmptyElement(elementName, attributes));
        }

        //
        // Determine which content is available
        //
        boolean containsText = false;
        boolean containsElement = false;
        boolean containsComment = false;
        boolean containsCData = false;
        for (ContentPart part : content) {
            if (part instanceof TextPart) {
                containsText = true;
            } else if (part instanceof ElementPart) {
                containsElement = true;
            } else if (part instanceof CommentPart) {
                containsComment = true;
            } else if (part instanceof CDataPart) {
                containsCData = true;
            }
        }

        //
        // Content that can and shall be stored in text element -> it's a text element
        //
        if (containsText == true && containsElement == false && containsCData == false) {

            //
            // no comments -> simple text element
            //
            if (containsComment == false) {
                StringBuilder text = new StringBuilder();
                for (ContentPart part : content) {
                    assert (part instanceof TextPart) : part.getClass().toString();
                    text.append(((TextPart) part).getText());
                }
                return (new EcvXmlTextElement(elementName, attributes, text.toString()));
            }

            //
            // text with comments -> complex text element
            //
            List<String> textParts = new ArrayList<>();
            List<String> commentParts = new ArrayList<>();
            for (ContentPart part : content) {
                if (part instanceof TextPart) {
                    textParts.add(((TextPart) part).getText());
                } else {
                    commentParts.add(((CommentPart) part).getCommentText());
                }
            }
            return (new EcvXmlTextElement(elementName, attributes, textParts, commentParts));

        }

        if (containsComment == false && containsCData == false) {

            //
            // Remove text, if it is all white space
            //
            if (containsText == true) {
                StringBuilder b = new StringBuilder();
                for (ContentPart part : content) {
                    if (part instanceof TextPart) {
                        b.append(((TextPart) part).getText());
                    }
                }
                String text = b.toString().replaceAll("\\h+", "").trim();
                if (text.isEmpty()) {
                    List<ContentPart> reducedContent = new ArrayList<>();
                    for (ContentPart part : content) {
                        if (part instanceof ElementPart) {
                            reducedContent.add(part);
                        } else {
                            assert (part instanceof TextPart);
                        }
                    }
                    content = reducedContent;
                    containsText = false;
                }
            }

            //
            // Only elements -> it's a container element
            //
            if (containsText == false) {
                if (tmpContainer == null) {
                    throw new ParseException("tmpContainer == null", input, currentParent);
                }
                for (ContentPart part : content) {
                    assert (part instanceof ElementPart) : tmpContainer.getFullName();
                    tmpContainer.addElement(((ElementPart) part).getElement());
                }
                return (tmpContainer);
            }
        }

        //
        // Combination of several types -> it's a combined element
        //
        return (new EcvXmlCombinedElement(elementName, attributes, content));
    }

    /**
     * Skips the XML header. The XML header starts with '<?xml' or '<---'.
     *
     * @throws util.EcvXmlElement.ParseException
     */
    private void skipHeaderElements() throws ParseException {
        boolean found;
        do {
            skipWhitespace();
            if (checkString("<?xml")) {
                found = true;
                expectString("<?xml");
                skipAfterEndString("?>");
            } else if (checkString("<!DOCTYPE")) {
                found = true;
                expectString("<!DOCTYPE");
                skipAfterEndString(">");
            } else if (checkString("<!--")) {
                found = true;
                expectString("<!--");
                skipAfterEndString("-->");
            } else {
                found = false;
            }
        } while (found == true);
    }

    private void skipSpecialElements() throws ParseException {
        boolean found;
        do {
            skipWhitespace();
            if (checkString("<?xml")) {
                found = true;
                expectString("<?xml");
                skipAfterEndString("?>");
            } else if (checkString("<!DOCTYPE")) {
                found = true;
                expectString("<!DOCTYPE");
                skipAfterEndString(">");
            } else if (checkString("<!doctype")) {
                found = true;
                expectString("<!doctype");
                skipAfterEndString(">");
            } else if (checkString("<meta")) {
                found = true;
                expectString("<meta");
                skipAfterEndString(">");
            } else {
                found = false;
            }
        } while (found == true);
    }

}
