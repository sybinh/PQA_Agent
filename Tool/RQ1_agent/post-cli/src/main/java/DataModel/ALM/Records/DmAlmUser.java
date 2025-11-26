/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Resource;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord;

/**
 *
 * @author CNI83WI
 */
public class DmAlmUser extends DmAlmElement {
    public static final String ELEMENT_TYPE = "User";
    
    final public DmAlmField_Text NAME;
    final public DmAlmField_Text NICK;
    final public DmAlmField_Resource MBOX;
    
    public DmAlmUser(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);
        
        NAME = addTextField("foaf:name", "Name");
        NICK = addTextField("foaf:nick", "Nickname");
        MBOX = addResourceField("foaf:mbox", "Mailbox");
    }

    @Override
    public String getStatus() {
        return ("");
    }

    @Override
    public String getTitle() {
        return(NAME.getValueAsText());
    }

    @Override
    public String getId() {
        return(NICK.getValueAsText());
    }
    
    @Override
    public String toString(){
        return("User: " + NICK.getValueAsText() + " - " + NAME.getValueAsText());
    }

}
