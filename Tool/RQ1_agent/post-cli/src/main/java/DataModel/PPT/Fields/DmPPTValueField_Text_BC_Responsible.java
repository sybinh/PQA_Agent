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
public class DmPPTValueField_Text_BC_Responsible extends DmPPTValueField_Text {

    public DmPPTValueField_Text_BC_Responsible(DmPPTBCRelease parent, String nameForUserInterface) {
        super(parent, getResponsibleFromBC(parent), nameForUserInterface);
    }

    private static String getResponsibleFromBC(DmPPTBCRelease bc) {
        String responsible;
        String project_DsGs;
        project_DsGs = bc.HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().COMPANY_DATA_ID.getValueAsText();
        bc.HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_POOL.getElement().getBcResponsibleTable().setCompanyDataId(project_DsGs);
        if(bc.getName()!= null && ! bc.getName().isEmpty()){
            responsible = bc.HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_POOL.getElement().getBcResponsibleTable().getResponsible(bc.getName());
        }else{
            responsible = "unbekannt";
        }
        return responsible;
    }
}
