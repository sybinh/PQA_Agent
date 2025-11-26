/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient;

/**
 * Contains the data for a rest command within the IPE rest framework.
 *
 * @author GUG2WI
 */
public abstract class RestRequest {

    public static enum ContentType {
        JSON("application/json");

        final private String textForHeader;

        private ContentType(String textForHeader) {
            this.textForHeader = textForHeader;
        }

        public String getTextForHeader() {
            return textForHeader;
        }

    }

    public static enum ResponseFormat {
        BINARY,
        TEXT_OR_XML,
        JSON
    }

    final private String urlServicePath;

    /**
     * Create a request object for the given service path.
     *
     * @param urlServicePath Path to the service below the server service path.
     * Might be empty or null.
     */
    public RestRequest(String urlServicePath) {
        this.urlServicePath = urlServicePath;
    }

    /**
     * Build the url for the http request. By default the given service url
     * concatenated with the service path of the request.
     *
     * @param serviceUrl
     * @return
     */
    public String buildRequestUrl(String serviceUrl) {
        assert (serviceUrl != null);
        assert (serviceUrl.isEmpty() == false);

        if ((urlServicePath != null) && (urlServicePath.isEmpty() == false)) {
            return (serviceUrl + "/" + urlServicePath);
        } else {
            return (serviceUrl);
        }
    }

    public ContentType getContentType() {
        return (ContentType.JSON);
    }

    public ResponseFormat getResponseFormat() {
        return (ResponseFormat.TEXT_OR_XML);
    }

}
