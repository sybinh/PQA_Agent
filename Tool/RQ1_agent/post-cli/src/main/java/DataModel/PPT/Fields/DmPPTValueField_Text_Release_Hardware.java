/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;
import DataModel.PPT.Records.DmPPTLine;
import DataModel.PPT.Records.DmPPTRelease;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_Release_Hardware extends DmPPTValueField_Text {

    public DmPPTValueField_Text_Release_Hardware(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, getInfo(parent), nameForUserInterface);
    }
    
    private static String getInfo(DmPPTRelease parent){
        DmPPTLine line = parent.BELONG_TO_SCHIENE.getElement();
        String info = "";
        if (parent.getHardwareInformationProPlaTo().containsKey(line.NAME.getValueAsText())) {
             info = parent.getHardwareInformationProPlaTo().get(line.NAME.getValueAsText());
        }
        if(info.replace(" ", "").isEmpty()){
            //If there is no hardware Tag at the release then take the one
            //from the project
            if (line.HARDWARE.getValueAsText().replace(" ", "").isEmpty()) {
                info = "unbekannt";
            } else {
                info = line.HARDWARE.getValueAsText();
            }
        }
        return info;
    }
}
