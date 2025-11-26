/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
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
public class EcvXmlTextElement extends EcvXmlElement {

    static public class CommentData {

        final private List<String> contentList;
        final private List<String> commentList;

        protected CommentData(String content, String comment) {
            assert (content != null);
            assert (content.isEmpty() == false);
            assert (comment != null);

            this.contentList = new ArrayList<>();
            this.contentList.add(content);
            this.commentList = new ArrayList<>();
            this.commentList.add(comment);
        }

        protected CommentData(List<String> contentList, List<String> commentList) {
            assert (contentList != null);
            assert (contentList.isEmpty() == false);
            assert (commentList != null);
            assert (commentList.isEmpty() == false);
            assert ((contentList.size() == commentList.size()) || (contentList.size() == (commentList.size() + 1)));

            this.contentList = contentList;
            this.commentList = commentList;
        }

        public String getContent() {
            StringBuilder b = new StringBuilder();
            for (String s : contentList) {
                b.append(s);
            }
            return (b.toString());
        }

        public List<String> getContentList() {
            return contentList;
        }

        public List<String> getCommentList() {
            return commentList;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof CommentData) {
                CommentData d = (CommentData) o;
                return (contentList.equals(d.contentList) && commentList.equals(d.commentList));
            } else {
                return (false);
            }
        }

    }

    final private CommentData commentData;
    final private String content;

    public EcvXmlTextElement(String elementName, String content) {
        super(elementName);
        assert (content != null);
        assert (content.isEmpty() == false);

        this.commentData = null;
        this.content = content;

    }

    public EcvXmlTextElement(String elementName, String content, String comment) {
        super(elementName);

        this.commentData = new CommentData(content, comment);
        this.content = this.commentData.getContent();

    }

    protected EcvXmlTextElement(String elementName, Map<String, String> attributes, String content) {
        super(elementName, attributes);
        assert (content != null);
        assert (content.isEmpty() == false);

        this.commentData = null;
        this.content = content;

    }

    protected EcvXmlTextElement(String elementName, Map<String, String> attributes, List<String> contentList, List<String> commentList) {
        super(elementName, attributes);

        this.commentData = new CommentData(contentList, commentList);
        this.content = this.commentData.getContent();

    }

    private EcvXmlTextElement(EcvXmlTextElement source) {
        super(source);

        this.content = source.content;
        this.commentData = source.commentData;
    }

    public String getText() {
        return (content);
    }

    public CommentData getCommentData() {
        return (commentData);
    }

    @Override
    public boolean isEmpty() {
        return (false);
    }

    @Override
    public String toString(String prefix) {
        return (prefix + "EcvXmlTextElement: " + getXmlString(EncodeFormat.SHORT_EMPTY_VALUE));
    }

    @Override
    protected String getUiString(String prefix) {
        StringBuilder builder = new StringBuilder(30);

        builder.append(prefix);
        builder.append('<');
        builder.append(getName());
        addAttributeString(builder);
        builder.append(">");

        if (commentData == null) {
            builder.append(content);
        } else {
            int i;
            for (i = 0; i < commentData.getCommentList().size(); i++) {
                builder.append(commentData.getContentList().get(i));
                builder.append("<!--");
                builder.append(commentData.getCommentList().get(i));
                builder.append("-->");
            }
            if (i < commentData.getContentList().size()) {
                builder.append(commentData.getContentList().get(i));
            }
        }

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

        if (commentData == null) {
            builder.append(encodeXml(content));
        } else {
            int i;
            for (i = 0; i < commentData.getCommentList().size(); i++) {
                builder.append(encodeXml(commentData.getContentList().get(i)));
                builder.append("<!--");
                builder.append(commentData.getCommentList().get(i));
                builder.append("-->");
            }
            if (i < commentData.getContentList().size()) {
                builder.append(encodeXml(commentData.getContentList().get(i)));
            }
        }

        builder.append("</");
        builder.append(getName());
        builder.append(">");

        return (builder.toString());
    }

    @Override
    public EcvXmlElement copy() {
        return (new EcvXmlTextElement(this));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EcvXmlTextElement) {

            EcvXmlTextElement e = (EcvXmlTextElement) o;
            if (equalsNameAndAttributes(e) == true) {
                if (commentData == null) {
                    return ((content.equals(e.content)));
                } else {
                    return (commentData.equals(e.getCommentData()));
                }
            } else {
                return (false);
            }
        } else {
            return (false);
        }
    }

}
