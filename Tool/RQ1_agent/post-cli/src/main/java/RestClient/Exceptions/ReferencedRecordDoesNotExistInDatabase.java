/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Exceptions;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gug2wi
 */
public class ReferencedRecordDoesNotExistInDatabase extends WriteToDatabaseRejected {

    final private String topMessage;
    final private String detailedMessage;

    final private static Map<String, String> propertyMap;

    static {
        propertyMap = new TreeMap<>();
        propertyMap.put("hasMappedIssue", "IRM,issue");
        propertyMap.put("hasMappedRelease", "IRM,release");
        propertyMap.put("hasMappedParentRelease", "RRM,parent release");
         propertyMap.put("hasMappedChildRelease", "RRM,child release");
    }

    public ReferencedRecordDoesNotExistInDatabase(String oslcPropertyName) {
        assert (oslcPropertyName != null);
        assert (oslcPropertyName.isEmpty() == false);

        String elementTypes = propertyMap.get(oslcPropertyName);
        if (elementTypes != null) {
            String[] s = elementTypes.split(",");
            String map = s[0];
            String target = s[1];

            this.topMessage = "Mapped " + target + " does not yet exist in database.";
            this.detailedMessage
                    = "Cannot save the " + map + " because the referenced " + target + " does not yet exist in the database.\n"
                    + "Save the " + target + " before saving the " + map + ".";
        } else {
            topMessage = "Referenced record does not exist.";
            detailedMessage = "Referenced record for " + oslcPropertyName + " does not yet exist in the database.";
        }

    }

    @Override
    public String getMessage() {
        return (topMessage);
    }

    @Override
    public String getMessageForUi() {
        return (topMessage);
    }

    @Override
    public String getDetailedMessageForUi() {
        return (detailedMessage);
    }
}
