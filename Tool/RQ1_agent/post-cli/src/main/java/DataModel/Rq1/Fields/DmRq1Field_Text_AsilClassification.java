/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.Rq1.Records.DmRq1Attachment;
import DataModel.Rq1.Records.DmRq1Issue;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Cache.Records.Rq1Issue;
import Rq1Cache.Records.Rq1MetadataChoiceList;
import Rq1Cache.Rq1RecordType;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.UiWorker;

/**
 *
 * @author MIW83WI
 */
public class DmRq1Field_Text_AsilClassification extends DmRq1Field_Text {

    public DmRq1Field_Text_AsilClassification(DmRq1Issue issue, Rq1FieldI_Text rq1TextField, String nameForUserInterface) {
        super(rq1TextField, nameForUserInterface);
    }

    public List<String> calculateAsilClassification() {
        UiWorker<List<String>> work = new UiWorker<List<String>>(UiWorker.LOADING) {
            @Override
            protected List<String> backgroundTask() {
                List<String> returnList = getValidInputValues();
                if (!returnList.contains("")) {
                    returnList.add(0, "");
                }
                return returnList;
            }
        };
        work.execute();
        List<String> returnList = null;
        try {
            returnList = work.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(DmRq1Field_Text_AsilClassification.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1Field_Text_AsilClassification.class.getName(), ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(DmRq1Field_Text_AsilClassification.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1Field_Text_AsilClassification.class.getName(), ex);
        }
        return returnList;
    }

    static List<String> getValidInputValues() {
        return (Rq1MetadataChoiceList.getValidInputValues(Rq1RecordType.ISSUE, Rq1Issue.ATTRIBUTE_ASIL_CLASSIFICATION, "ASILClassification-OLD=''"));
    }

}
