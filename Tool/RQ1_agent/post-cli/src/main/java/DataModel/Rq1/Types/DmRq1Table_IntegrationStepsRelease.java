/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import Rq1Data.Enumerations.IntegrationStep;
import util.EcvDate;
import util.EcvTableColumn_ComboBox;
import util.EcvTableColumn_Date;
import util.EcvTableColumn_String;
import util.EcvTableDescription;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Table_IntegrationStepsRelease extends EcvTableDescription {

    final public EcvTableColumn_ComboBox ID;
    final public EcvTableColumn_Date DATE;
    final public EcvTableColumn_String NAME;
    final public EcvTableColumn_String COMMENT;

    public DmRq1Table_IntegrationStepsRelease() {
        addIpeColumn(ID = new EcvTableColumn_ComboBox("Step", IntegrationStep.getAllShortName()));
        addIpeColumn(NAME = new EcvTableColumn_String("Name"));
        addIpeColumn(DATE = new EcvTableColumn_Date("Date"));
        addIpeColumn(COMMENT = new EcvTableColumn_String("Comment"));
    }

    //--------------------------------------------------------------------------
    //
    // Record based access
    //
    //--------------------------------------------------------------------------
    static public class Record {

        final private String shortName;
        final private EcvDate date;
        private String givenName;
        private String comment;

        public Record(String shortName, EcvDate date, String givenName, String comment) {
            this.shortName = getNotEmpty(shortName);
            this.date = (date != null) ? date : EcvDate.getEmpty();
            this.givenName = getNotEmpty(givenName);
            this.comment = getNotEmpty(comment);
        }

        private String getNotEmpty(String v) {
            return ((v != null) && (v.isEmpty() == false) ? v : null);
        }

        public void addGivenNameAndComment(String givenName, String comment) {
            this.givenName = getNotEmpty(givenName);
            this.comment = getNotEmpty(comment);
        }

        public String getShortName() {
            return shortName;
        }

        public EcvDate getDate() {
            return date;
        }

        public String getGivenName() {
            return givenName;
        }

        public String getGenericLongName() {
            if (givenName != null) {
                return (givenName);
            } else {
                return (IntegrationStep.getLongNameByShortName(shortName));
            }
        }

        public String getComment() {
            return comment;
        }

        public String toString() {
            String s = "(";
            s += shortName != null ? shortName : "";
            s += ",";
            s += date != null ? date.toString() : "";
            s += ",";
            s += givenName != null ? givenName : "";
            s += ",";
            s += comment != null ? comment : "";
            s += ")";
            return (s);
        }

    }

}
