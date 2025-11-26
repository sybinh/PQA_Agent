/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Authentication;

import RestClient.Exceptions.RestInternalException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import ToolUsageLogger.ToolUsageLogger;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Implements the authentication via oAuth 1.0.
 *
 * @author gug2wi
 */
public class OAuthAuthenticationProvider implements AuthenticationProviderI {

    static private final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(OAuthAuthenticationProvider.class.getCanonicalName());

    /**
     * Supports the building of signature base string and authorization header.
     */
    private class Parameter implements Comparable<Parameter> {

        final String name;
        final String value;

        public Parameter(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public int compareTo(Parameter other) {
            int c = name.compareTo(other.name);
            if (c == 0) {
                c = value.compareTo(other.value);
            }
            return (c);
        }

    }

    final private String consumerKey;
    final private String consumerSecret;

    private String oAuthToken = null;
    private String oAuthTokenSecret = null;

    private int nonceSalt = 0;

    public OAuthAuthenticationProvider(String consumerKey, String consumerSecret) {
        assert (consumerKey != null);
        assert (consumerKey.isEmpty() == false);
        assert (consumerSecret != null);
        assert (consumerSecret.isEmpty() == false);

        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
    }

    public void setOAuthToken(String oAuthToken, String oAuthTokenSecret) {
        assert (oAuthToken != null);
        assert (oAuthToken.isEmpty() == false);
        assert (oAuthTokenSecret != null);
        assert (oAuthTokenSecret.isEmpty() == false);

        this.oAuthToken = oAuthToken;
        this.oAuthTokenSecret = oAuthTokenSecret;
    }

    @Override
    public void setAuthData(HttpClient httpClient) {
    }

    @Override
    public void setAuthData(HttpRequestBase httpMethod) throws RestInternalException {
        assert (httpMethod != null);

        if (httpMethod instanceof HttpPost) {
            setAuthData(httpMethod, "POST");
        } else if (httpMethod instanceof HttpPut) {
            setAuthData(httpMethod, "PUT");
        } else if (httpMethod instanceof HttpGet) {
            setAuthData(httpMethod, "GET");
        }
    }

    private void setAuthData(HttpRequestBase httpMethod, String method) throws RestInternalException {
        assert (httpMethod != null);
        assert (method != null);
        assert (method.isEmpty() == false);

        //---------------------------
        // Build timestamp and nonce
        //---------------------------
        long timestamp = new Date().getTime();
        String nonce = "N-" + Long.toString(timestamp) + "-" + Integer.toString(nonceSalt++);

        //------------------------------------------------
        // Build parameter list for authentication values
        //------------------------------------------------
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("oauth_version", "1.0"));
        parameters.add(new Parameter("oauth_consumer_key", urlEncode(consumerKey)));
        parameters.add(new Parameter("oauth_signature_method", "HMAC-SHA1"));
        parameters.add(new Parameter("oauth_timestamp", Long.toString(timestamp)));
        parameters.add(new Parameter("oauth_nonce", nonce));
        if (oAuthToken != null) {
            parameters.add(new Parameter("oauth_token", oAuthToken)); // Auth Token was received via HTTP. So it is already encoded.
        }

        //--------------------------
        // Get URI from HTTP method
        //--------------------------
        String methodUri = "";

        if (httpMethod.getURI() != null) {
            methodUri = httpMethod.getURI().toString();

            LOGGER.log(Level.FINER, "methodUri = >{0}<", methodUri);

            //----------------------------------
            // Split in base URL and parameters
            //----------------------------------
            String signatureBaseUrl = "";
            String parameterString = "";
            int parameterStart = methodUri.indexOf("?");
            if (parameterStart >= 0) {
                signatureBaseUrl = urlEncode(methodUri.substring(0, parameterStart));
                parameterString = methodUri.substring(parameterStart + 1, methodUri.length());
            } else {
                signatureBaseUrl = urlEncode(methodUri);
            }

            LOGGER.log(Level.FINER, "signatureBaseUrl = >{0}<", signatureBaseUrl);
            LOGGER.log(Level.FINER, "parameterString = >{0}<", parameterString);

        //--------------------------
            // Parse and add parameters 
            //--------------------------
            if (parameterString.isEmpty() == false) {
                String[] parameterArray = parameterString.split("&");
                for (String parameter : parameterArray) {
                    String[] nameValuePair = parameter.split("=");
                    String encodedName = urlEncode(nameValuePair[0]);
                    if (nameValuePair.length == 1) {
                    //
                        // Parameter without value
                        //
                        parameters.add(new Parameter(encodedName, ""));
                    } else if (nameValuePair.length == 2) {
                    //
                        // Parameter with value
                        //
                        String pureValue = "";
                        try {
                            pureValue = URLDecoder.decode(nameValuePair[1], "UTF-8");
                        } catch (UnsupportedEncodingException ex) {
                            LOGGER.log(Level.SEVERE, "Getting value for parameter.", ex);
                            ToolUsageLogger.logError(OAuthAuthenticationProvider.class.getCanonicalName(), ex);
                        }
                        String encodedValue = urlEncode(pureValue);
                        parameters.add(new Parameter(encodedName, encodedValue));
                    } else {
                        LOGGER.log(Level.SEVERE, "Invalid parameter: >{0}<", parameter);
                    }
                }
            }

        //-----------------------------
            // Build signature base string
            //-----------------------------
            StringBuilder signatureBaseString = new StringBuilder();
            signatureBaseString.append(method);
            signatureBaseString.append('&');
            signatureBaseString.append(signatureBaseUrl);
            signatureBaseString.append('&');

            Collections.sort(parameters);
            for (int i = 0; i < parameters.size(); i++) {
                if (i != 0) {
                    signatureBaseString.append(urlEncode("&"));
                }
                signatureBaseString.append(urlEncode(parameters.get(i).name));
                signatureBaseString.append(urlEncode("="));
                signatureBaseString.append(urlEncode(parameters.get(i).value));
            }

            LOGGER.log(Level.FINER, "Signature Base String = >{0}<", signatureBaseString);

        //---------------------
            // Calculate signature
            //---------------------
            String signKeyString = urlEncode(consumerSecret) + "&";
            if (oAuthTokenSecret != null) {
                signKeyString += oAuthTokenSecret; // Auth Token Secret was received via HTTP. So it is already encoded.
            }
            SecretKeySpec signkey = new SecretKeySpec(signKeyString.getBytes(), "HmacSHA1");
            String signature = "";
            try {
                Mac mac = Mac.getInstance("HmacSHA1");
                mac.init(signkey);
                byte[] rawSignature = mac.doFinal(signatureBaseString.toString().getBytes());
                signature = Base64.getEncoder().encodeToString(rawSignature);
            } catch (NoSuchAlgorithmException ex) {
                LOGGER.log(Level.SEVERE, "Creation of MAC failed.", ex);
                ToolUsageLogger.logError(OAuthAuthenticationProvider.class.getCanonicalName(), ex);
                throw (new RestInternalException("Creation of MAC failed.", ex));
            } catch (InvalidKeyException ex) {
                LOGGER.log(Level.SEVERE, "Setting key failed.", ex);
                ToolUsageLogger.logError(OAuthAuthenticationProvider.class.getCanonicalName(), ex);
                throw (new RestInternalException("Creation of MAC failed.", ex));
            }

        //--------------------------------------------
            // Set authorization parameter in HTTP method
            //--------------------------------------------
            parameters.add(new Parameter("oauth_signature", urlEncode(signature)));
            Collections.sort(parameters);
            StringBuilder authorizationHeader = new StringBuilder();
            for (int i = 0; i < parameters.size(); i++) {
                if (i != 0) {
                    authorizationHeader.append(",");
                }
                authorizationHeader.append(parameters.get(i).name);
                authorizationHeader.append("=\"");
                authorizationHeader.append(parameters.get(i).value);
                authorizationHeader.append("\"");
            }
            httpMethod.setHeader("Authorization", "OAuth " + authorizationHeader);
        }
    }

    /**
     * Encodes the given string for URL.
     *
     * @param unencoded Not encoded string.
     * @return Encoded string.
     */
    static public String urlEncode(String unencoded) {

        StringBuilder encoded = new StringBuilder();

        for (char chUnencoded : unencoded.toCharArray()) {
            urlEncodeChar(encoded, chUnencoded);

        }

        return (encoded.toString());
    }

    static public void urlEncodeChar(StringBuilder b, char chUnencoded) {
        if (Character.isAlphabetic(chUnencoded) || Character.isDigit(chUnencoded) || (chUnencoded == '-') || (chUnencoded == '.') || (chUnencoded == '_') || (chUnencoded == '~')) {
            b.append(chUnencoded);
            return;
        }
        for (int i = 0; i < uriEncodeTable.length; i++) {
            if (uriEncodeTable[i][0].charAt(0) == chUnencoded) {
                b.append(uriEncodeTable[i][1]);
                return;
            }
        }
        LOGGER.log(Level.SEVERE, "Encoding failed for character {0} >{1}<.", new Object[]{Character.getNumericValue(chUnencoded), chUnencoded});
    }

    static private CharSequence[][] uriEncodeTable = {
        {" ", "%20"}, // &amp has to be the first element to encode and the last to decode when replace() is used !
        {"!", "%21"},
        {"\"", "%22"},
        {"#", "%23"},
        {"$", "%24"},
        {"%", "%25"},
        {"&", "%26"},
        {"'", "%27"},
        {"(", "%28"},
        {")", "%29"},
        {"*", "%2A"},
        {"+", "%2B"},
        {",", "%2C"},
        //    {"-", "%2D"},
        {"/", "%2F"},
        //
        {":", "%3A"},
        {";", "%3B"},
        {"<", "%3C"},
        {"=", "%3D"},
        {">", "%3E"},
        {"?", "%3F"},
        {"@", "%40"},
        {"[", "%5B"},
        {"\\", "%5C"},
        {"]", "%5D"},
        {"^", "%5E"},
        //    {"_", "%5F"},
        {"`", "%60"},
        //
        {"{", "%7B"},
        {"|", "%7C"},
        {"}", "%7D"}
    //       {"~", "%7E"}
    };

}
