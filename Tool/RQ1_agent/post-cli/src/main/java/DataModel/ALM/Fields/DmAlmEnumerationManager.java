/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.ALM.Records.DmAlmElement;
import DataModel.ALM.Records.DmAlmElementFactory;
import DataStore.ALM.DsAlmEnumerationFactory;
import DataStore.ALM.DsAlmEnumerationList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author GUG2WI
 */
public class DmAlmEnumerationManager {

    private static final Map<String, DmAlmEnumerationList> cache = new TreeMap<>();
    private static final Map<String, List<DmAlmElement>> elementCache = new TreeMap<>();

    public static DmAlmEnumerationList getEnumerations(String urlOfEnumeration) {

        if ((urlOfEnumeration == null) || (urlOfEnumeration.isEmpty())) {
            return (new DmAlmEnumerationList());
        }

        DmAlmEnumerationList dmList = cache.get(urlOfEnumeration);
        if (dmList != null) {
            return (dmList);
        }

        if (cache.containsKey(urlOfEnumeration) == true) {
            // The loading for this url failed already.
            // Don't try again.
            return (null);
        }

        DsAlmEnumerationList dsList = DsAlmEnumerationFactory.loadEnumerationList(urlOfEnumeration);
        if (dsList == null) {
            cache.put(urlOfEnumeration, null);
            return (null);
        }

        dmList = new DmAlmEnumerationList(dsList);
        cache.put(urlOfEnumeration, dmList);
        return (dmList);
    }

    public static List<DmAlmElement> getEnumerationElements(String urlOfEnumeration) {

        if ((urlOfEnumeration == null) || (urlOfEnumeration.isEmpty())) {
            return (null);
        }
        
        List<DmAlmElement> dmList = elementCache.get(urlOfEnumeration);
        
        if (dmList != null) {
            return (dmList);
        }
        
        if (elementCache.containsKey(urlOfEnumeration) == true) {
            // The loading for this url failed already.
            // Don't try again.
            return (null);
        }
        
        List<String> resourceList = DsAlmEnumerationFactory.loadEnumerationRecordList(urlOfEnumeration);
        List<DmAlmElement> elementList = new ArrayList<>();
        for (String url : resourceList) {
            if(url.contains("unassigned") == false) {
                DmAlmElement resourceElement = (DmAlmElement) DmAlmElementFactory.getElementByUrl(url);
                elementList.add(resourceElement);
            }
        }

        return elementList;
    }

}
