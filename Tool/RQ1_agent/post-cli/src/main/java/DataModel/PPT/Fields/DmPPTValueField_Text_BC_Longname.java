/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTBCRelease;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_BC_Longname extends DmPPTValueField_Text {

    public DmPPTValueField_Text_BC_Longname(DmPPTBCRelease parent, String nameForUserInterface) {
        super(parent, getLongnameFromBC(parent), nameForUserInterface);
    }

    private static String getLongnameFromBC(DmPPTBCRelease bc) {
        String longname;
        if(bc.getName()!= null && !bc.getName().isEmpty()){
            longname = bc.HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_POOL.getElement().getBcLongnameTable().getLongNameGerman(bc.getName());
            if (longname == null) {
                return bc.getName();
            }
            return longname;
        }
        return bc.getName();
    }
}
