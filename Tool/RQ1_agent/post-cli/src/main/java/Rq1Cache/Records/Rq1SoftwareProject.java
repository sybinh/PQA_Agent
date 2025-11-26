/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataStore.DsField_Xml;
import Rq1Cache.Fields.*;
import Rq1Cache.Rq1NodeDescription;
import static Rq1Cache.Rq1NodeDescription.ISSUE_FD;
import static Rq1Cache.Rq1NodeDescription.ISSUE_SW;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.*;
import Rq1Data.Enumerations.ClassificationPstRelease;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import Rq1Data.Enumerations.LifeCycleState_Release;
import Rq1Data.Enumerations.Scope;
import java.util.EnumSet;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class Rq1SoftwareProject extends Rq1Project {

    final public Rq1QueryField OPEN_ISSUE_SW;
    final public Rq1QueryField OPEN_ISSUE_FD;
    final public Rq1QueryField ALL_ISSUE_SW;
    final public Rq1QueryField ALL_ISSUE_FD;

    final public Rq1QueryField NOT_CANCELED_ISSUE_SW;

    final public Rq1ReferenceListField_FilterByClass ALL_PST;
    final public Rq1ReferenceListField_FilterByClass ALL_BC;
    final public Rq1ReferenceListField_FilterByClass ALL_FC;
    final public Rq1ReferenceListField_FilterByClass OPEN_PST;
    final public Rq1QueryField_belongsToProject OPEN_PST_RELEASE;
    final public Rq1ReferenceListField_FilterByClass OPEN_BC;
    final public Rq1ReferenceListField_FilterByClass OPEN_FC;

    final private Rq1XmlSubField_Xml DERIVATIVES_FIELD;
    final public Rq1XmlSubField_Table<Rq1XmlTable_ProjectDerivatives> DERIVATIVES;

    final public Rq1XmlSubField_Xml PST_CREATION_OFFSETS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_PstCreationOffset> PST_CREATION_OFFSET;

    final private Rq1XmlSubField_Xml ISSUE_SW_SUB_CATEGORIES_FIELD;
    final public Rq1XmlSubField_Table<Rq1XmlTable_Enumeration> ISSUE_SW_SUB_CATEGORIES;

    final public Rq1ReferenceListField_FilterByClass PROPLATO_PST;

    final public Rq1ReferenceListField_FilterByClass PPT_PST_SINCE_2014;

    final public Rq1ReferenceListField_FilterByClass PPT_PST_ALL;

    final protected Rq1XmlSubField_Xml SW_METRICS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_SwMetricsValues> SW_METRICS_VALUES;

    final public Rq1XmlSubField_Xml  QMM;
    final public Rq1XmlSubField_Table<Rq1XmlTable_QmmFilterCriteria> PROFIT_CENTER;
    final public Rq1XmlSubField_Table<Rq1XmlTable_QmmFilterCriteria> P_ID_DISPLAY_NAME;
    final public Rq1XmlSubField_Table<Rq1XmlTable_QmmFilterCriteria> PROJECT_STATUS;

    public Rq1SoftwareProject(Rq1NodeDescription subjectDescription) {
        super(subjectDescription);

        //
        // Issues
        //
        addField(ALL_ISSUE_FD = new Rq1QueryField_belongsToProject(this, "All I-FD", ISSUE_FD.getRecordType()));
        ISSUE_FD.setFixedRecordCriterias(ALL_ISSUE_FD);

        addField(ALL_ISSUE_SW = new Rq1QueryField_belongsToProject(this, "All I-SW", ISSUE_SW.getRecordType()));
        ISSUE_SW.setFixedRecordCriterias(ALL_ISSUE_SW);

        addField(OPEN_ISSUE_FD = new Rq1QueryField_belongsToProject(this, "Open I-FD", ISSUE_FD.getRecordType()));
        OPEN_ISSUE_FD.addCriteria_ValueList(FIELDNAME_LIFE_CYCLE_STATE, LifeCycleState_Issue.getAllOpenState());
        ISSUE_FD.setFixedRecordCriterias(OPEN_ISSUE_FD);

        addField(OPEN_ISSUE_SW = new Rq1QueryField_belongsToProject(this, "Open I-SW", ISSUE_SW.getRecordType()));
        OPEN_ISSUE_SW.addCriteria_ValueList(FIELDNAME_LIFE_CYCLE_STATE, LifeCycleState_Issue.getAllOpenState());
        ISSUE_SW.setFixedRecordCriterias(OPEN_ISSUE_SW);

        addField(NOT_CANCELED_ISSUE_SW = new Rq1QueryField_belongsToProject(this, "ProPlaToIssues", ISSUE_SW.getRecordType()));
        NOT_CANCELED_ISSUE_SW.addCriteria_ValueList("Scope", EnumSet.of(Scope.EXTERNAL));
        EnumSet<LifeCycleState_Issue> wantedLifeCycleStatesProPlaTo = EnumSet.allOf(LifeCycleState_Issue.class);
        wantedLifeCycleStatesProPlaTo.remove(LifeCycleState_Issue.CANCELED);
        NOT_CANCELED_ISSUE_SW.addCriteria_ValueList(FIELDNAME_LIFE_CYCLE_STATE, wantedLifeCycleStatesProPlaTo);
        NOT_CANCELED_ISSUE_SW.addCriteria_ValueList("Scope", EnumSet.of(Scope.EXTERNAL));
        ISSUE_SW.setFixedRecordCriterias(NOT_CANCELED_ISSUE_SW);

        addField(DERIVATIVES_FIELD = new Rq1XmlSubField_Xml(this, TAGS, "Derivatives"));
        addField(DERIVATIVES = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_ProjectDerivatives(), DERIVATIVES_FIELD, "Derivative"));
        DERIVATIVES_FIELD.setOptional();

        addField(PST_CREATION_OFFSETS = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKOWN_ELEMENTS_NOT_ALLOWED, "PstCreationOffsets"));
        PST_CREATION_OFFSETS.setOptional();
        addField(PST_CREATION_OFFSET = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_PstCreationOffset(), PST_CREATION_OFFSETS, "PstCreationOffset"));

        addField(ISSUE_SW_SUB_CATEGORIES_FIELD = new Rq1XmlSubField_Xml(this, TAGS, "IssueSWC"));
        addField(ISSUE_SW_SUB_CATEGORIES = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_Enumeration(), ISSUE_SW_SUB_CATEGORIES_FIELD, "SubCat"));
        ISSUE_SW_SUB_CATEGORIES_FIELD.setOptional();

        //
        // Releases
        //
        addField(ALL_PST = new Rq1ReferenceListField_FilterByClass(this, ALL_RELEASES, Rq1Pst.class));
        addField(ALL_BC = new Rq1ReferenceListField_FilterByClass(this, ALL_RELEASES, Rq1Bc.class));
        addField(ALL_FC = new Rq1ReferenceListField_FilterByClass(this, ALL_RELEASES, Rq1Fc.class));

        addField(OPEN_PST = new Rq1ReferenceListField_FilterByClass(this, OPEN_RELEASES, Rq1Pst.class));
        addField(OPEN_BC = new Rq1ReferenceListField_FilterByClass(this, OPEN_RELEASES, Rq1Bc.class));
        addField(OPEN_FC = new Rq1ReferenceListField_FilterByClass(this, OPEN_RELEASES, Rq1Fc.class));

        addField(OPEN_PST_RELEASE = new Rq1QueryField_belongsToProject(this, "OpenPst", Rq1RecordType.RELEASE));
        OPEN_PST_RELEASE.addCriteria_ValueList(FIELDNAME_LIFE_CYCLE_STATE, LifeCycleState_Release.getAllOpenState());
        OPEN_PST_RELEASE.addCriteria_ValueList(Rq1Release.ATTRIBUTE_TYPE, Rq1Pver.TYPE, Rq1Pvar.TYPE);
        OPEN_PST_RELEASE.addCriteria_ValueList(Rq1Release.ATTRIBUTE_CATEGORY, Rq1Pst.CATEGORY_SW_VERSION, Rq1Pst.CATEGORY_MIGRATION_BASELINE);

        //
        // ProPlaTo
        //
        Rq1QueryField_belongsToProject proPlaToReleases;
        addField(proPlaToReleases = new Rq1QueryField_belongsToProject(this, "ProPlaToReleases", Rq1NodeDescription.PVAR_RELEASE.getRecordType()));

        EnumSet<LifeCycleState_Release> l_proPlaTo = EnumSet.allOf(LifeCycleState_Release.class);
        l_proPlaTo.remove(LifeCycleState_Release.CANCELED);
        proPlaToReleases.addCriteria_ValueList("Scope", EnumSet.of(Scope.EXTERNAL));
        proPlaToReleases.addCriteria_ValueList("LifeCycleState", l_proPlaTo);
        //Only for the Roadmap
        //proPlaToReleases.addCriteria_isLaterOrEqualThen("PlannedDate", new EcvDate().setDateValue(2014, 01, 01));
        //proPlaToReleases.addCriteria_ValueList("Classification", EnumSet.of(ReleaseClassification.PRELIMINARY, ReleaseClassification.FINALE, 
        //        ReleaseClassification.SERIES, ReleaseClassification.PROTOTYPE).toArray(new ReleaseClassification[0]));
        addField(PROPLATO_PST = new Rq1ReferenceListField_FilterByClass(this, proPlaToReleases, Rq1Pst.class));

        //
        // ProPlaTo Roadmap
        //
        EnumSet<ClassificationPstRelease> classifications = EnumSet.allOf(ClassificationPstRelease.class);
        classifications.remove(ClassificationPstRelease.EMPTY);
        //
        Rq1QueryField_belongsToProject proPlaToReleasesRoadmap;
        addField(proPlaToReleasesRoadmap = new Rq1QueryField_belongsToProject(this, "ProPlaToReleasesRoadmap", Rq1NodeDescription.PVAR_RELEASE.getRecordType()));
        proPlaToReleasesRoadmap.addCriteria_ValueList("Scope", EnumSet.of(Scope.EXTERNAL));
        proPlaToReleasesRoadmap.addCriteria_ValueList("LifeCycleState", l_proPlaTo); //not canceled
        proPlaToReleasesRoadmap.addCriteria_isLaterOrEqualThen("PlannedDate", EcvDate.getDate(2014, 01, 01));
        proPlaToReleasesRoadmap.addCriteria_ValueList("Classification", classifications);
        addField(PPT_PST_SINCE_2014 = new Rq1ReferenceListField_FilterByClass(this, proPlaToReleasesRoadmap, Rq1Pst.class));

        //
        // ProPlaTo Roadmap without Date
        //
        Rq1QueryField_belongsToProject proPlaToReleasesRoadmap_without_Date;
        addField(proPlaToReleasesRoadmap_without_Date = new Rq1QueryField_belongsToProject(this, "ProPlaToReleasesRoadmap without Date", Rq1NodeDescription.PVAR_RELEASE.getRecordType()));
        proPlaToReleasesRoadmap_without_Date.addCriteria_ValueList("Scope", EnumSet.of(Scope.EXTERNAL));
        proPlaToReleasesRoadmap_without_Date.addCriteria_ValueList("LifeCycleState", l_proPlaTo); //not canceled
        proPlaToReleasesRoadmap_without_Date.addCriteria_ValueList("Classification", classifications);
        addField(PPT_PST_ALL = new Rq1ReferenceListField_FilterByClass(this, proPlaToReleasesRoadmap_without_Date, Rq1Pst.class));

        //
        // SW-Metrics
        //
        addField(SW_METRICS = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKOWN_ELEMENTS_NOT_ALLOWED, "SW-Metric"));
        SW_METRICS.setOptional();
        addField(SW_METRICS_VALUES = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_SwMetricsValues(), SW_METRICS, "Value"));
        SW_METRICS_VALUES.setOptional();

        addField(QMM = new Rq1XmlSubField_Xml(this, TAGS, "QMM"));
        QMM.setOptional();
        addField(PROFIT_CENTER = new Rq1XmlSubField_Table<Rq1XmlTable_QmmFilterCriteria>(this, new Rq1XmlTable_QmmFilterCriteria(), QMM, "PROFIT_CENTER"));
        PROFIT_CENTER.setOptional();
        addField(P_ID_DISPLAY_NAME = new Rq1XmlSubField_Table<Rq1XmlTable_QmmFilterCriteria>(this, new Rq1XmlTable_QmmFilterCriteria(), QMM, "P_ID_DISPLAY_NAME"));
        P_ID_DISPLAY_NAME.setOptional();
        addField(PROJECT_STATUS = new Rq1XmlSubField_Table<Rq1XmlTable_QmmFilterCriteria>(this, new Rq1XmlTable_QmmFilterCriteria(), QMM, "PROJECT_STATUS"));
        PROJECT_STATUS.setOptional();

    }

}
