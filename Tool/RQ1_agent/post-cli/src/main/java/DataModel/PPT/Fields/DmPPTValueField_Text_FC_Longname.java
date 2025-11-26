/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTFCRelease;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_FC_Longname extends DmPPTValueField_Text {

    public DmPPTValueField_Text_FC_Longname(DmPPTFCRelease parent, String nameForUserInterface) {
        super(parent, getLongnameFromFC(parent), nameForUserInterface);
    }

    private static String getLongnameFromFC(DmPPTFCRelease fc) {
        String longname;
        if(fc.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_POOL.getElement().getFcLongnameTable() == null){
            return fc.getName();
        }
        if(fc.getName() != null && !fc.getName().isEmpty()){
            longname = fc.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_POOL.getElement().getFcLongnameTable().getLongNameGerman(fc.getName());
            if (longname == null) {
                return fc.getName();
            }
            return longname;
        }
        return fc.getName();
    }
}
