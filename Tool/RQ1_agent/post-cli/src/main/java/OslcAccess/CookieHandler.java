/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpMethod;

/**
 * Stores cookies of a connection.
 */
class CookieHandler {

    Map<String, Cookie> cookies;

    public CookieHandler() {
        cookies = new HashMap<String, Cookie>();
    }

    /**
     * @return the cookie names
     */
    public List<String> getCookieKeys() {

        return new ArrayList<String>(cookies.keySet());
    }

    /**
     * @return the cookie
     */
    public Cookie getCookie(String name) {
        Cookie cookie = cookies.get(name);

        if (cookie == null) {
            return null;
        }

        return cookie;
    }

    /**
     * @return the cookie value
     */
    public String getCookieValue(String name) {
        Cookie cookie = cookies.get(name);

        if (cookie == null) {
            return null;
        }

        return cookie.getValue();
    }

    /**
     * create new cookie
     *
     * @param name
     * @param value
     */
    public void setCookie(String name, String value) {
        Cookie cookie = new Cookie();
        cookie.setName(name);
        cookie.setValue(value);
        cookies.put(name, cookie);
    }

    /**
     * add cookie
     *
     */
    public void addCookie(Cookie cookie) {
        if (cookie == null) {
            return;
        }

        cookies.put(cookie.getName(), cookie);
    }

    /**
     * delete given cookie
     *
     * @param name
     */
    public void removeCookie(String name) {
        cookies.remove(name);
    }

    /**
     * delete all cookies
     */
    public void removeCookies() {
        cookies = new HashMap<String, Cookie>();
    }

    /**
     * store cookies of response header
     *
     * @param method
     */
    public void pushCookies(HttpMethod method) {

        Header[] headers = method.getResponseHeaders();

        for (int idx = 0; idx < headers.length; idx++) {
            if (headers[idx].getName().contentEquals("Set-Cookie")
                    || headers[idx].getName().contentEquals("Set-Cookie2")) {
                for (HeaderElement element : headers[idx].getElements()) {
                    Cookie cookie = new Cookie();
                    cookie.setName(element.getName());
                    cookie.setValue(element.getValue());
                    cookies.put(cookie.getName(), cookie);
                }
            }
        }
    }

    /**
     * add cookies to request header
     *
     * @param method
     */
    public void popCookies(HttpMethod method) {

        StringBuilder sb = new StringBuilder();
        int idx = 0;
        for (Cookie cookie : cookies.values()) {
            if (idx > 0) {
                sb.append(';');
            }
            sb.append(cookie.getName() + "=" + cookie.getValue());
            idx++;
        }

        method.setRequestHeader("Cookie", sb.toString());
    }
}
