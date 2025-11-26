/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * Stores cookies of a connection.
 */
public class CookieHandler {

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
        Cookie cookie = new BasicClientCookie(name, value);
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
    public void pushCookies(HttpResponse method) {

        Header[] headers = method.getAllHeaders();

        for (int idx = 0; idx < headers.length; idx++) {
            if (headers[idx].getName().contentEquals("Set-Cookie")
                    || headers[idx].getName().contentEquals("Set-Cookie2")) {
                for (HeaderElement element : headers[idx].getElements()) {
                    Cookie cookie = new BasicClientCookie(element.getName(), element.getValue());
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
    public void popCookies(HttpRequestBase method) {

        StringBuilder sb = new StringBuilder();
        int idx = 0;
        for (Cookie cookie : cookies.values()) {
            if (idx > 0) {
                sb.append(';');
            }
            sb.append(cookie.getName() + "=" + cookie.getValue());
            idx++;
        }

        method.setHeader("Cookie", sb.toString());
    }
}
