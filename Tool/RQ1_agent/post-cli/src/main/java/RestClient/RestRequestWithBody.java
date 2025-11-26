/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient;

/**
 *
 * @author GUG2WI
 */
public class RestRequestWithBody extends RestRequest {

    private boolean hideBody = false;
    final private String body;

    public RestRequestWithBody(String urlServicePath, String body) {
        super(urlServicePath);

        this.body = body;
    }

    public String buildBodyString() {
        return (body);
    }

    public void setHideBody(boolean hideBody) {
        this.hideBody = hideBody;
    }

    public boolean isHideBody() {
        return hideBody;
    }

}
