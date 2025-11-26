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
public class RestResponseText implements RestResponseI {

    final private String textREsponse;

    public RestResponseText(String jsonResponse) {
        assert (jsonResponse != null);

        this.textREsponse = jsonResponse;
    }
    
    public String getTextResponse(){
        return this.textREsponse;
    }
}
