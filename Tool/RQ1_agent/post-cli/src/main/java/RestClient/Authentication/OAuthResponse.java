/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Authentication;

import OslcAccess.OslcResponse;
import java.util.Map;
import java.util.TreeMap;

/**
 * Supports the access to the oAuth response values.
 *
 * @author gug2wi
 */
public class OAuthResponse {

    final private OslcResponse oslcResponse;

    private Map<String, String> stringContent = null;

    public OAuthResponse(OslcResponse oslcResponse) {
        assert (oslcResponse != null);
        this.oslcResponse = oslcResponse;
    }

    final public String getAuthToken() {
        splitBodyString();
        return (stringContent.get("oauth_token"));
    }

    final public String getAuthTokenSecret() {
        splitBodyString();
        return (stringContent.get("oauth_token_secret"));
    }

    final public String getAccessVerifier() {
        String locationValue = oslcResponse.getResponseHeader("Location");
        String[] parts = locationValue.split("[\\?\\=\\&]");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("oauth_verifier") == true) {
                if (i + 1 < parts.length) {
                    return (parts[i + 1]);
                }
            }
        }
        return (null);
    }

    final void splitBodyString() {
        if (stringContent == null) {
            stringContent = new TreeMap<>();
            String[] parameters = oslcResponse.getResponseBodyString().split("\\&");
            for (String parameter : parameters) {
                String[] split = parameter.split("\\=");
                if (split.length == 2) {
                    stringContent.put(split[0], split[1]);
                }
            }
        }
    }

    final String getAccessTokenVerifier() {
        String locationValue = oslcResponse.getResponseHeader("Location");
        String[] parts = locationValue.split("[\\?\\=\\&]");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("oauth_verifier") == true) {
                if (i + 1 < parts.length) {
                    return (parts[i + 1]);
                }
            }
        }
        return (null);
    }

    final public String getAccessToken() {
        String locationValue = oslcResponse.getResponseHeader("Location");
        String[] parts = locationValue.split("[\\?\\=\\&]");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("oauth_token") == true) {
                if (i + 1 < parts.length) {
                    return (parts[i + 1]);
                }
            }
        }
        return (null);
    }

}
