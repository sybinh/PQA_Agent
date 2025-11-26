/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Monitoring.Rq1DataRule;
import Rq1Cache.Monitoring.Rq1ParseFieldException;
import Rq1Cache.Monitoring.Rq1UnexpectedDataFailure;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Data.Monitoring.Rq1RuleDescription;
import Rq1Data.Types.Rq1LinesPoolProject;
import Rq1Data.Types.Rq1ParseException;
import java.util.EnumSet;

/**
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_PstLinesPoolProject extends Rq1DatabaseField_StringAccess<Rq1LinesPoolProject> implements Rq1FieldI<Rq1LinesPoolProject> {

    @EcvElementList("Rq1Data.Monitoring.Rq1RuleDescription")
    static final public Rq1RuleDescription descriptionPstLines = new Rq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.RQ1_DATA),
            "Syntax of PST lines in pool project.",
            "Checks the syntax of the PST lines defined in the pool project.\n"
            + "\n"
            + "The warning 'Unexpected data read from RQ1 database.' is set on the pool project, if the syntax is wrong. "
            + "The problem with the syntax is descriped in the warning.");

    private Rq1DataRule rulePstLines = null;

    //
    public Rq1DatabaseField_PstLinesPoolProject(Rq1RecordInterface parent, String dbFieldName) {
        super(parent, dbFieldName, null);
    }

    @Override
    public boolean setOslcValue_Internal(String dbValue, Source source) {
        assert (dbValue != null);
        assert (source != null);

        removeMarker();

        Rq1LinesPoolProject pstList = new Rq1LinesPoolProject();
        try {
            pstList.setRq1Value(dbValue);
        } catch (Rq1ParseException ex) {
            StringBuilder s = new StringBuilder();
            s.append("Problem processing data from field ").append(getOslcPropertyName()).append(".");
            Rq1ParseFieldException containerEx = new Rq1ParseFieldException(s.toString(), ex);
            getParentRecord().setMarker(new Rq1UnexpectedDataFailure(getRule(), getParentRecord(), this, containerEx));
        }
        return (setDataSourceValue(pstList, source));
    }

    private Rq1DataRule getRule() {
        if (rulePstLines == null) {
            rulePstLines = new Rq1DataRule(descriptionPstLines);
        }
        return (rulePstLines);
    }

    private void removeMarker() {
        if (rulePstLines != null) {
            getParentRecord().removeMarkers(rulePstLines);
        }
    }

    @Override
    protected String getOslcValue_Internal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void dbValueWasWrittenToDb() {
        removeMarker();
        super.dbValueWasWrittenToDb();
    }

}
