/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTRelease;
import DataModel.Rq1.Records.DmRq1Pst;
import Rq1Data.Types.Rq1DerivativeMapping;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_Release_Programmstandskennung extends DmPPTValueField_Text {

    public DmPPTValueField_Text_Release_Programmstandskennung(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, getNeutralCalibration(parent), nameForUserInterface);
    }

    private static String getNeutralCalibration(DmPPTRelease release) {
        DmRq1Pst dmRQ1Release = release.getRq1Release();
        String programmstandskennung;
        switch (dmRQ1Release.PROPLATO_PROGRAMMSTANDSKENNUNG.getValueAsText().toUpperCase()) {
            case "BUGFIX":
                programmstandskennung =  "Bugfix";
                break;
            case "TEST":
                programmstandskennung = "Test";
                break;
            case "ZZZ":
                programmstandskennung = "Lumpensammler";
                break;
            case "INTERN":
                programmstandskennung = "intern";
                break;
            default:
                programmstandskennung = "";
        }
        
        if(release.areDerivativesRelevant()){
            for(String derivative : dmRQ1Release.DERIVATIVES.getValue().getMapping().keySet()){
                //Check if the derivative equals the Line of the Release
                String extDerivative = release.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getProPlaToLineExternal(derivative);
                if(release.BELONG_TO_SCHIENE.getElement().getTitle().equals(extDerivative)){
                    if(dmRQ1Release.DERIVATIVES.getValue().getMapping().get(derivative).equals(Rq1DerivativeMapping.Mode.TOLERATED)){
                        programmstandskennung = "intern";
                    }
                }
            }
        }
        
        return programmstandskennung;
    }
}
