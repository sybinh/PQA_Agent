/*
 *  Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 *  This program and the accompanying materials are made available under
 *  the terms of the Bosch Internal Open Source License v4
 *  which accompanies this distribution, and is available at
 *  http://bios.intranet.bosch.com/bioslv4.txt
 */
package backend;

import util.EcvApplication.ApplicationType;
import OslcAccess.OslcRq1ServerDescription;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Records.Rq1QueryHit;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Types.Rq1Reference;
import Rq1Cache.Types.Rq1StoredQuery;
import java.util.LinkedHashMap;
import util.EcvApplication;
import util.EcvApplicationException;

/**
 *
 * @author SOD83WI
 */
public class RQ1QueryExecutor {

    public EcvApplication.LoginData loginData; //mandatory
    /**
     * This method connects the user to the RQ1 database
     *
     * @param username username, to use for login
     * @param password password, to use for login
     * @param serverDescription serverDescription, to use for login
     * @throws backend.RQ1Exception
     */
    public void connectToDatabaseOther(String username, String password, String serverDescription) throws RQ1Exception {
        OslcRq1ServerDescription desc = OslcRq1ServerDescription.getDescriptionByName(serverDescription);
        if (desc == null) {
            throw new RQ1Exception("The given server Description is unknown");
        } else {
            this.loginData = new EcvApplication.LoginData(username, password.toCharArray(), desc);
        }
        EcvApplication.setLoginData(this.loginData);
        EcvApplication.setApplicationData("Rq1Statuspflege", "1.2.1", "1.2.1", ApplicationType.DaemonProcess);
        //EcvApplication.setApplicationData("Rq1Statuspflege", "1.2.1", ApplicationType.DaemonProcess);
    }
     /**
     * This method executes a query and returns the result as a LinkedHashmap with the rows as keys and a LinkedHashmap as values.
     * The inner LinkedHashmap has the fieldnames as keys and the fieldvalues as values 
     *
     * @param QUERY_ID Query, which was executed
     * @return
     */
    public static LinkedHashMap<Integer, LinkedHashMap<String, String>> executeQuery(String QUERY_ID) throws EcvApplicationException {
        Rq1StoredQuery storedQueryOne = new Rq1StoredQuery(QUERY_ID);
        LinkedHashMap<Integer, LinkedHashMap<String, String>> outerMap = new LinkedHashMap<>();
        int row = 1;

        for (Rq1Reference ref : storedQueryOne.getReferenceList()) {
            Rq1RecordInterface rq1Record = ref.getRecord();
            LinkedHashMap<String, String> innerMap = new LinkedHashMap<>();

            if (rq1Record instanceof Rq1QueryHit) {
                Rq1QueryHit hit = (Rq1QueryHit) rq1Record;

                for (Rq1FieldI field : hit.getFields()) {
                    innerMap.put(field.getFieldName(), field.getDataModelValue().toString());
                }
            }
            outerMap.put(row, innerMap);
            row++;
        }
        return outerMap;
    }
}
