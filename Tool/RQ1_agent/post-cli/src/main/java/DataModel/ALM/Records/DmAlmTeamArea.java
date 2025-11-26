/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord;

/**
 *
 * @author CNI83WI
 */
public class DmAlmTeamArea extends DmAlmElement {
    public static final String ELEMENT_TYPE = "TeamArea";
    
    final public DmAlmField_Text TITLE;
    final public DmAlmField_Text DESCRIPTION;
    
    public DmAlmTeamArea(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);
        
        TITLE = addTextField("dcterms:title", "Title");
        ignoreField("rtc_cm:archived");
        ignoreField("oslc:archived");
        ignoreField("rtc_cm:depth");
        ignoreField("rtc_cm:hierarchicalName");
        ignoreField("skos:broader");
        ignoreField("process:projectArea");
        ignoreField("rtc_cm:projectArea");
        ignoreField("rdf:type");
        ignoreField("rtc_cm:defaultTeamArea");
        DESCRIPTION = addTextField("dcterms:description", "Description", true);
        ignoreField("acc:accessContext");
    }

    @Override
    public String getStatus() {
        return("");
    }

    @Override
    public String getTitle() {
        return(TITLE.getValue());
    }

    @Override
    public String getId() {
        String url = this.getUrl();
        return (url.substring(url.lastIndexOf("/")+1));
    } 

}

