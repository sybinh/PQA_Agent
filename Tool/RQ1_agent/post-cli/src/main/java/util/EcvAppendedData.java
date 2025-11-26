/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
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
 * @author frt83wi
 */
public class EcvAppendedData {

    private Map<String, String> appendedData;

    public EcvAppendedData(Map<String, String> appendedData) {
        this.appendedData = appendedData;
    }

    public Map<String, String> getAppendedData() {
        return appendedData;
    }

    public List<EcvXmlTextElement> getEcvXmlTextElementList() {
        List<EcvXmlTextElement> output = new ArrayList<>();

        for (Map.Entry<String, String> entry : appendedData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            if ((key == null) || (key.isEmpty()))
                continue;
            
            if ((value == null) || (value.isEmpty()))
                continue;
            
            output.add(new EcvXmlTextElement(key, value));
        }

        return output;
    }
}
