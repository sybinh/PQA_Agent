/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.Ecv.PST.Records.DmEcvBc;
import DataModel.Ecv.PST.Records.DmEcvFc;
import DataModel.Ecv.PST.Records.DmEcvPSTSchiene;
import DataModel.Ecv.PST.Records.DmEcvPSTSchieneTable;
import DataModel.Ecv.PST.Records.DmEcvProgrammstand;
import DataModel.Ecv.Records.DmEcvBcLongNameTable;
import DataModel.Ecv.Records.DmEcvBcResponsibleTable;
import DataModel.Ecv.Records.DmEcvFcLongNameTable;
import DataModel.PPT.Records.DmPPTBCRelease;
import DataModel.PPT.Records.DmPPTCustomerProject;
import DataModel.PPT.Records.DmPPTFCRelease;
import DataModel.PPT.Records.DmPPTPoolProject;
import DataModel.PPT.Records.DmPPTRelease;
import DataModel.PPT.Records.PPTDataRule;
import DataModel.Rq1.Records.DmRq1Attachment;
import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import util.EcvApplicationException;
import util.EcvDate;

/**
 * This field is used to specify the FcBc reading and delta calculation
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Release_PST_Release extends DmPPTField_Reference<DmEcvProgrammstand> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmPPTField_Reference_Release_PST_Release.class.getCanonicalName());

    /*RULE MANAGEMENT*/
    static final public RuleDescription fcbcListDoubleRuleDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROPLATO),
            "Double FC_BC List.xml",
            "For each Release and line there should only be one fcbc list attached\n"
            + "otherwise this would lower the quality of the proplato data");
    private PPTDataRule fcbcListDoubleRule = null;

    static final public RuleDescription fcbcListMissingRuleDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROPLATO),
            "Missing FC_BC List.xml",
            "For each delivered programm version (planned date of release is in the past) a FC_BC_List.xml\n"
            + "has to be attached to the given RQ1 release.\n"
            + "In case of a PVAR planning, for each derivative withing the PVAR there has to be \n"
            + "a serperate FC_BC_List attached to the release.\n"
            + "\n"
            + "XML content check: name of PST line has to fit to the RQ1 release informations.\n"
            + "XML format has to fit to the requirements.");
    private PPTDataRule fcbcListMissingRule = null;
    /*RULE MANAGEMENT*/

    private final DmPPTRelease RELEASE;
    private boolean isFcBcListAtThisPver;
    public List<DmPPTRelease> relevantPredecessors;
    private boolean getFCBCListAlreadyTried;
    private boolean alreadyLoadedAndEmpty;
    private boolean isLoadLocalFcBcListDone = false;

    public DmPPTField_Reference_Release_PST_Release(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, null, nameForUserInterface);
        this.RELEASE = parent;
        getFCBCListAlreadyTried = false;
        isFcBcListAtThisPver = false;
        alreadyLoadedAndEmpty = false;
        relevantPredecessors = new LinkedList<>();
    }

    public boolean loadLocalFcBcList() {

        if (isLoadLocalFcBcListDone == false) {
            isLoadLocalFcBcListDone = true;

            int firsttime = 0;

            for (DmRq1Attachment att : this.RELEASE.getATTACHMENTS().getElementList()) {
                //Check the filename, if it fits to the release
                if (att.FILENAME.getValueAsText().contains("FC_BC_") && att.FILENAME.getValueAsText().contains(".xml")) { //Spaceholder
                    DmEcvPSTSchieneTable schieneTable = new DmEcvPSTSchieneTable();
                    schieneTable.loadFromEcvXmlContainerElement(att.getFileContent());
                    //in one FcBc list there is only one PST Line
                    assert (schieneTable.getElements().size() <= 1);
                    DmEcvPSTSchiene schiene = schieneTable.getElements().get(0);
                    //in one FcBc Line there is only one Programming stand
                    assert (schiene.SCHIENEN_PROGRAMMSTAENDE.getElementList().size() <= 1);
                    //If the fcbc list belongs to the line, the programmstand belongs to

                    DmPPTCustomerProject project = this.RELEASE.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement();
                    DmEcvProgrammstand programmstand = schiene.SCHIENEN_PROGRAMMSTAENDE.getElementList().get(0);

                    String fcbcLine = project.LINE_INTERNAL_TO_EXTERNAL.get(schiene.SCHIENEN_NAME.getValueAsText());
                    if (this.RELEASE.BELONG_TO_SCHIENE.getElement().getTitle().equals(fcbcLine)) {
                        if (firsttime == 0) {
                            if (super.getElement() == null) {
                                programmstand.PROGRAMMSTAND_NAME.setValue(RELEASE.getTitle());
                                this.setElement(programmstand);
                                super.getElement().PROGRAMMSTAND_NAME.setValue(RELEASE.getTitle());
                                isFcBcListAtThisPver = true;
                                firsttime = 1;
                            }
                        } else if (firsttime == 1) {
                            /*RULE MANAGEMENT*/
                            //There are more than one suitable FC BC Lists which could be taken
                            StringBuilder s = new StringBuilder(40);
                            s.append("There are more than one FC BC Lists attached for the Line ").append(RELEASE.BELONG_TO_SCHIENE.getElement().getTitle());
                            RELEASE.addMarker(new Warning(getFcBcListDoubleRule(), "Double FC_BC List.xml Warning", s.toString()));
                            firsttime = 2;
                            /*RULE MANAGEMENT*/
                        }
                    }

                }
            }

            /*RULE MANAGEMENT*/
            //If the release is planned, there has to be a fcbc list attached to the release
            if (this.RELEASE.AUSLIEFERDATUM_SOLL.getValue().isEarlierOrEqualThen(getProcessDate())
                    && super.getElement() == null) {
                StringBuilder s = new StringBuilder(40);
                s.append("Planned date of Release is in the Past: ").append(this.RELEASE.AUSLIEFERDATUM_SOLL.getValueAsText()).
                        append("\n There has to be a propper FC_BC_List.xml attached to the release for the Line ").append(RELEASE.BELONG_TO_SCHIENE.getElement().getTitle());
                RELEASE.addMarker(new Warning(getFcBcListMissingRule(), "Missing FC_BC List.xml Warning", s.toString()));
                this.relevantPredecessors.add(RELEASE);
            }
            /*RULE MANAGEMENT*/

        }

        return (isFcBcListAtThisPver);
    }

    protected DmEcvProgrammstand getFCBC_List() {

        getFCBCListAlreadyTried = true;

        loadLocalFcBcList();

        //this release has no fc bc list
        //search the list of the predecessor
        if (super.getElement() == null && this.RELEASE.PREDECESSOR.getElement() != null) {
            if (this.RELEASE.PREDECESSOR.getElement().PST_PROGRAMMSTAND.getElement() != null) {
                this.setElement(this.RELEASE.PREDECESSOR.getElement().PST_PROGRAMMSTAND.getElement().getCopy());
            }
            this.relevantPredecessors.addAll(this.RELEASE.PREDECESSOR.getElement().PST_PROGRAMMSTAND.relevantPredecessors);
            if (super.getElement() != null) {
                makeDeltaCalculation();
            }
        }
        return super.getElement();
    }

    public void makeDeltaCalculation() throws EcvApplicationException {

        this.RELEASE.getRq1Release().loadCacheForPPT();

        //Wenn die Planung noch nicht abgeschlossen ist, so wird das RQ1 
        //save the fcbc Programmstand
        DmEcvProgrammstand fcbcProgrammstand = super.getElement();
        assert (fcbcProgrammstand != null);
        assert (fcbcProgrammstand.PROGRAMMSTAND_PAKETE != null);

        //Get the unique names of the bcs in Rq1
        //Map<String, DmEcvBc> uniqueBcs = new TreeMap<>();
        //Map<String, DmPPTBCRelease> ecvBcToRq1Bc = new TreeMap<>();
        Map<String, List<String>> bcNameToAlternatives = new TreeMap<>();
        Map<String, Map<String, EcvDate>> bcNameToAlternativesToPlannedDate = new TreeMap<>();
        Map<String, Map<String, List<String>>> bcNameToAlternativesToVersion = new TreeMap<>();
        Map<String, Map<String, DmPPTBCRelease>> bcNameToVersionToBC = new TreeMap<>();

        try {

            //First check the different alternatives per BC Name
            for (DmPPTBCRelease bcRelease : this.RELEASE.PPT_BCS.getElementList()) {
                String name = bcRelease.getName();
                String version = bcRelease.getVersion();

                if (bcNameToAlternatives.containsKey(name)) {
                    //The bcname is known
                    List<String> alternativesPerName = bcNameToAlternatives.get(name);
                    if (version.contains(".")) {
                        //The version has to contain a "."
                        if (alternativesPerName.contains(version.split("[.]")[0])) {
                            //The alternative is known
                            //Check if the version of the alternative is also known
                            List<String> versionPerAlternatives = bcNameToAlternativesToVersion.get(name).get(version.split("[.]")[0]);
                            //If the version is not saved at the moment
                            if (!versionPerAlternatives.contains(version)) {
                                versionPerAlternatives.add(version);
                                Map<String, DmPPTBCRelease> versionToRelease = bcNameToVersionToBC.get(name);
                                versionToRelease.put(version, bcRelease);

                                EcvDate latestPlannedDateOfAlternative = bcNameToAlternativesToPlannedDate.get(name).get(version.split("[.]")[0]);
                                if (latestPlannedDateOfAlternative.isEarlierThen(bcRelease.getPlannedDate())) {
                                    bcNameToAlternativesToPlannedDate.get(name).put(version.split("[.]")[0], bcRelease.getPlannedDate());
                                }
                            }
                        } else {
                            // Then a new alternative has been found case
                            //The alternative is not known
                            alternativesPerName.add(version.split("[.]")[0]);
                            List<String> versionPerAlternatives = new ArrayList<>();
                            versionPerAlternatives.add(version);
                            Map<String, List<String>> alternativeToVersions = bcNameToAlternativesToVersion.get(name);
                            alternativeToVersions.put(version.split("[.]")[0], versionPerAlternatives);
                            Map<String, DmPPTBCRelease> versionToRelease = bcNameToVersionToBC.get(name);
                            versionToRelease.put(version, bcRelease);
                            Map<String, EcvDate> alternativeToPlannedDate = bcNameToAlternativesToPlannedDate.get(name);
                            alternativeToPlannedDate.put(version.split("[.]")[0], bcRelease.getPlannedDate());
                        }
                    }
                } else {
                    //The bc name is not known
                    List<String> alternatives = new ArrayList<>();
                    String actualAlternative = "0";
                    if (version.contains(".")) {
                        actualAlternative = version.split("[.]")[0];
                    }

                    alternatives.add(actualAlternative);
                    bcNameToAlternatives.put(name, alternatives);

                    List<String> versions = new ArrayList<>();
                    versions.add(version);
                    Map<String, List<String>> alternativeToVersion = new TreeMap<>();
                    alternativeToVersion.put(actualAlternative, versions);
                    bcNameToAlternativesToVersion.put(name, alternativeToVersion);

                    Map<String, DmPPTBCRelease> versionToRelease = new TreeMap<>();
                    versionToRelease.put(version, bcRelease);
                    bcNameToVersionToBC.put(name, versionToRelease);

                    Map<String, EcvDate> alternativeToPlannedDate = new TreeMap<>();
                    alternativeToPlannedDate.put(actualAlternative, bcRelease.getPlannedDate());
                    bcNameToAlternativesToPlannedDate.put(name, alternativeToPlannedDate);
                }
            }
        } catch (Throwable thr) {
            logger.warning("PPT WARNING: DmPPTField_Reference_Release_PST_Release: First check the different alternatives per BC Name:  Unable to calc the Delta Information " + thr.getMessage());
        }

        Map<String, String> bcNameToAlternativeToTake = new TreeMap<>();

        try {
            //Check which alternative should be taken
            for (String name : bcNameToAlternatives.keySet()) {
                List<String> alternatives = bcNameToAlternatives.get(name);
                //If there are more than one alternatives
                if (alternatives.size() > 1) {
                    //Compare the planned dates
                    String winningAlternative = (String) alternatives.toArray()[0];
                    Map<String, EcvDate> alternativeToPlannedDate = bcNameToAlternativesToPlannedDate.get(name);
                    Iterator<String> iter = alternatives.iterator();
                    while (iter.hasNext()) {
                        String alter = iter.next();

                        if (alternativeToPlannedDate.get(winningAlternative).isEarlierThen(
                                alternativeToPlannedDate.get(alter))) {
                            //The alternative has a planned Date which is later then 
                            //the planned date of the winning alternative
                            //so save the new one
                            winningAlternative = alter;
                        } else if (alternativeToPlannedDate.get(winningAlternative).equals(
                                alternativeToPlannedDate.get(alter))) {
                            //They have the same planned date
                            //Check if the alternatives are numbers
                            int winning, newAlter;
                            try {
                                winning = Integer.parseInt(winningAlternative);
                                newAlter = Integer.parseInt(alter);

                                if (newAlter > winning) {
                                    winningAlternative = alter;
                                }
                            } catch (Exception e) {
                                //was not able to parse the Integers
                                //so check them alphabetically
                                if (alter.compareTo(winningAlternative) < 0) {
                                    winningAlternative = alter;
                                }
                            }
                        }
                    }

                    bcNameToAlternativeToTake.put(name, winningAlternative);
                } else {
                    bcNameToAlternativeToTake.put(name, (String) alternatives.toArray()[0]);
                }
            }
        } catch (Throwable thr) {
            logger.warning("PPT WARNING: DmPPTField_Reference_Release_PST_Release: Check which alternative should be taken:  Unable to calc the Delta Information " + thr.getMessage());
        }

        Map<String, String> bcNameToVersionToTake = new TreeMap<>();
        try {
            //Check which version of the winning alternative should be taken

            for (String bcName : bcNameToAlternativeToTake.keySet()) {
                String alternative = bcNameToAlternativeToTake.get(bcName);
                //Init
                bcNameToVersionToTake.put(bcName, bcNameToAlternativesToVersion.get(bcName).get(alternative).get(0));
                for (String version : bcNameToAlternativesToVersion.get(bcName).get(alternative)) {
                    DmPPTBCRelease currentRelease = bcNameToVersionToBC.get(bcName).get(version);
                    DmPPTBCRelease oldRelease = bcNameToVersionToBC.get(bcName).get(bcNameToVersionToTake.get(bcName));
                    //Compare the currentRelease against the old release
                    if (oldRelease.isPredecessor(currentRelease)) {
                        //the current one wins
                        bcNameToVersionToTake.put(bcName, version);
                    } else if (!currentRelease.isPredecessor(oldRelease)) {
                        if (currentRelease.getPlannedDate().toString().isEmpty()
                                || oldRelease.getPlannedDate().toString().isEmpty()) {
                            if (currentRelease.getVersion().compareTo(oldRelease.getVersion()) > 0) {
                                //the current one wins
                                bcNameToVersionToTake.put(bcName, version);
                            }
                        } else if ((currentRelease.getPlannedDate().isLaterThen(oldRelease.getPlannedDate())
                                || (currentRelease.getPlannedDate().equals(oldRelease.getPlannedDate())
                                && currentRelease.getVersion().compareTo(oldRelease.getVersion()) > 0))) {
                            //the current one wins
                            bcNameToVersionToTake.put(bcName, version);
                        }
                    }
                }
            }
        } catch (Throwable thr) {
            logger.warning("PPT WARNING: DmPPTField_Reference_Release_PST_Release: Check which version of the winning alternative should be taken:  Unable to calc the Delta Information " + thr.getMessage());
        }

        //Collect all the fcs for the given version
        //Make a map for the given fcName and a list of FCs
        Map<String, List<DmPPTFCRelease>> bcToFCs = new TreeMap<>();

        for (String name : bcNameToAlternativeToTake.keySet()) {
            String alternative = bcNameToAlternativeToTake.get(name);
            List<DmPPTFCRelease> fcs = new ArrayList<>();
            for (String version : bcNameToAlternativesToVersion.get(name).get(alternative)) {
                DmPPTBCRelease bc = bcNameToVersionToBC.get(name).get(version);
                fcs.addAll(bc.PPT_FCS.getElementList());
            }
            bcToFCs.put(name, fcs);
        }

        Map<String, Map<String, List<String>>> bcNameToFcNameToAlternatives = new TreeMap<>();
        Map<String, Map<String, Map<String, List<String>>>> bcNameToFcNameToAlternativesToVersions = new TreeMap<>();
        Map<String, Map<String, Map<String, DmPPTFCRelease>>> bcNameToFcNameToVersionToFC = new TreeMap<>();
        try {
            //bcNameToFcNameToVersionToFC

            for (String bc : bcToFCs.keySet()) {
                List<DmPPTFCRelease> fcs = bcToFCs.get(bc);
                Map<String, List<String>> fcToAlternative = new TreeMap<>();
                Map<String, Map<String, List<String>>> fcToAlternativeToVersion = new TreeMap<>();
                Map<String, Map<String, DmPPTFCRelease>> fcToVersionToFC = new TreeMap<>();
                for (DmPPTFCRelease fc : fcs) {
                    String name = fc.getName();
                    String version = fc.getVersion();
                    if (version.contains(".")) {
                        //The version has to contain at least one .
                        String alternative = fc.getVersion().split("[.]")[0];
                        if (fcToAlternative.containsKey(fc.getName())) {
                            //The fc is known
                            List<String> alternatives = fcToAlternative.get(name);
                            if (!alternatives.contains(alternative)) {
                                alternatives.add(alternative);

                                List<String> versions = new ArrayList<>();
                                versions.add(version);
                                Map<String, List<String>> alternativeToVersions = fcToAlternativeToVersion.get(name);
                                alternativeToVersions.put(alternative, versions);

                                Map<String, DmPPTFCRelease> versionToFc = fcToVersionToFC.get(name);
                                versionToFc.put(version, fc);
                            } else {
                                List<String> versions = fcToAlternativeToVersion.get(name).get(alternative);
                                if (!versions.contains(version)) {
                                    versions.add(version);
                                }
                                Map<String, DmPPTFCRelease> versionToFc = fcToVersionToFC.get(name);
                                if (!versionToFc.containsKey(version)) {
                                    versionToFc.put(version, fc);
                                }
                            }
                        } else {
                            List<String> alternatives = new ArrayList<>();
                            alternatives.add(alternative);
                            fcToAlternative.put(fc.getName(), alternatives);

                            List<String> versions = new ArrayList<>();
                            versions.add(version);
                            Map<String, List<String>> alternativeToVersions = new TreeMap<>();
                            alternativeToVersions.put(alternative, versions);
                            fcToAlternativeToVersion.put(name, alternativeToVersions);

                            Map<String, DmPPTFCRelease> versionToFc = new TreeMap<>();
                            versionToFc.put(version, fc);
                            fcToVersionToFC.put(name, versionToFc);
                        }
                    }
                }

                bcNameToFcNameToAlternatives.put(bc, fcToAlternative);
                bcNameToFcNameToAlternativesToVersions.put(bc, fcToAlternativeToVersion);
                bcNameToFcNameToVersionToFC.put(bc, fcToVersionToFC);
            }
        } catch (Throwable thr) {
            logger.warning("PPT WARNING: DmPPTField_Reference_Release_PST_Release: bcNameToFcNameToVersionToFC:  Unable to calc the Delta Information " + thr.getMessage());
        }

        Map<String, Map<String, Map<String, EcvDate>>> bcNameToFcNameToAlternativeToPlannedDate = new TreeMap<>();
        try {
            //Check which alternative of FC should be taken
            for (String bcName : bcNameToFcNameToAlternativesToVersions.keySet()) {
                Map<String, Map<String, EcvDate>> fcNameToAlternativeToPlannedDate = new TreeMap<>();
                for (String fcName : bcNameToFcNameToAlternativesToVersions.get(bcName).keySet()) {
                    Map<String, EcvDate> alternativeToPlannedDate = new TreeMap<>();
                    for (String alternative : bcNameToFcNameToAlternativesToVersions.get(bcName).get(fcName).keySet()) {
                        List<String> versions = bcNameToFcNameToAlternativesToVersions.get(bcName).get(fcName).get(alternative);
                        //it is possible, that the versions don't have a planned date
                        //so the alternative should have a planned date which is very early
                        EcvDate latestPlannedDate = EcvDate.getDate(1900, 1, 1);
                        for (String version : versions) {
                            //Get the latest planned date of the alternative
                            EcvDate actualPlannedDate = bcNameToFcNameToVersionToFC.get(bcName).get(fcName).get(version).getPlannedDate();
                            if (!actualPlannedDate.isEmpty()) {
                                if (actualPlannedDate.isLaterOrEqualThen(latestPlannedDate)) {
                                    //if the planned date of the version is later than the prior latest
                                    latestPlannedDate = actualPlannedDate;
                                }
                            }
                        }
                        alternativeToPlannedDate.put(alternative, latestPlannedDate);
                    }
                    fcNameToAlternativeToPlannedDate.put(fcName, alternativeToPlannedDate);
                }
                bcNameToFcNameToAlternativeToPlannedDate.put(bcName, fcNameToAlternativeToPlannedDate);
            }
        } catch (Throwable thr) {
            logger.warning("PPT WARNING: DmPPTField_Reference_Release_PST_Release: bcNameToFcNameToVersionToFC:  Unable to calc the Delta Information " + thr.getMessage());
        }

        try {
            //It is important, that the BC handling is done at this step, because
            //the fc has to look at the alternative which is in the fcbc list
            //and there should be the new values
            //First check if the alternative of the bc is in the DmPPTProgrammstand
            for (String bcName : bcNameToAlternativeToTake.keySet()) {
                String alternative = bcNameToAlternativeToTake.get(bcName);
                String version = bcNameToVersionToTake.get(bcName);
                DmEcvBc fcbcBc = fcbcProgrammstand.PROGRAMMSTAND_PAKETE.getPaketByTitle(bcName);
                //--------------------------BC Handling
                if (fcbcBc == null) {
                    //The BC is not in the fcBc List
                    //So add it
                    fcbcProgrammstand.PROGRAMMSTAND_PAKETE.addElement(new DmEcvBc(bcNameToVersionToBC.get(bcName).get(version)));
                } else if (!fcbcBc.BC_VERSION.getValueAsText().contains(".")
                        || !fcbcBc.BC_VERSION.getValueAsText().split("[.]")[0].equals(alternative)) {
                    //The version of the BC in the fcBc list, does not contain a "."
                    //So we should take the BC of RQ1
                    fcbcProgrammstand.PROGRAMMSTAND_PAKETE.removeElement(bcName);
                    fcbcProgrammstand.PROGRAMMSTAND_PAKETE.addElement(new DmEcvBc(bcNameToVersionToBC.get(bcName).get(version)));
                } else if (fcbcBc.BC_VERSION.getValueAsText().contains(".") && fcbcBc.BC_VERSION.getValueAsText().split("[.]")[0].equals(alternative)) {
                    fcbcBc.updateInformation(bcNameToVersionToBC.get(bcName).get(version));
                }
            }
        } catch (Throwable thr) {
            logger.warning("PPT WARNING: DmPPTField_Reference_Release_PST_Release: First check if the alternative of the bc is in the DmPPTProgrammstand:  Unable to calc the Delta Information " + thr.getMessage());
        }

        //Check against the fcbc file, which alternative of the fc is in
        Map<String, Map<String, String>> bcNameToFcNameToAlternativeToTake = new TreeMap<>();

        try {
            for (String bcName : bcNameToFcNameToAlternatives.keySet()) {
                DmEcvBc actualBc = fcbcProgrammstand.PROGRAMMSTAND_PAKETE.getPaketByTitle(bcName);
                Map<String, String> fcNameToAlternativeToTake = new TreeMap<>();
                if (actualBc != null) {
                    Map<String, List<String>> fcNameToAlternative = bcNameToFcNameToAlternatives.get(bcName);
                    for (String fcName : fcNameToAlternative.keySet()) {
                        List<String> alternatives = fcNameToAlternative.get(fcName);
                        if (alternatives.size() > 1) {
                            DmEcvFc actualFc = actualBc.BC_FC.getFunctionByTitle(fcName);
                            if (actualFc != null && actualFc.FC_VERSION.getValueAsText().contains(".")) {
                                //Check if there is one of the alternatives in the fcBc List
                                String alternative = actualFc.FC_VERSION.getValueAsText().split("[.]")[0];

                                if (alternatives.contains(alternative)) {
                                    //The alternative of the fcbc List is taken
                                    fcNameToAlternativeToTake.put(fcName, alternative);
                                }
                            }
                            //If there is no entry in the fcNameToAlternativeToTake Map make one
                            //Only check the RQ1 Alternatives because there is no FC in the fcbc list
                            //or the alternatives are not in the fcbc List
                            if (!fcNameToAlternativeToTake.containsKey(fcName)) {
                                //Same handling as BC alternative 
                                //First check the planned dates of the alternatives against each others
                                String winningAlternative = alternatives.get(0);
                                Map<String, EcvDate> alternativeToPlannedDate
                                        = bcNameToFcNameToAlternativeToPlannedDate.get(bcName).get(fcName);
                                for (String alter : alternatives) {
                                    if (alternativeToPlannedDate.get(winningAlternative).isEarlierThen(
                                            alternativeToPlannedDate.get(alter))) {
                                        //The alternative has a planned Date which is later then 
                                        //the planned date of the winning alternative
                                        //so save the new one
                                        winningAlternative = alter;
                                    } else if (alternativeToPlannedDate.get(winningAlternative).equals(
                                            alternativeToPlannedDate.get(alter))) {
                                        //They have the same planned date
                                        //Check if the alternatives are numbers
                                        int winning, newAlter;
                                        try {
                                            winning = Integer.parseInt(winningAlternative);
                                            newAlter = Integer.parseInt(alter);

                                            if (newAlter > winning) {
                                                winningAlternative = alter;
                                            }
                                        } catch (Exception e) {
                                            //was not able to parse the Integers
                                            //so check them alphabetically
                                            if (alter.compareTo(winningAlternative) < 0) {
                                                winningAlternative = alter;
                                            }
                                        }
                                    }
                                }
                                fcNameToAlternativeToTake.put(fcName, winningAlternative);
                            }
                        } else {
                            assert (!alternatives.isEmpty());
                            //if there is only one alternative, so take it
                            fcNameToAlternativeToTake.put(fcName, alternatives.get(0));
                        }
                    }
                }
                bcNameToFcNameToAlternativeToTake.put(bcName, fcNameToAlternativeToTake);
            }
        } catch (Throwable thr) {
            logger.warning("PPT WARNING: DmPPTField_Reference_Release_PST_Release: bcNameToFcNameToAlternativeToTake:  Unable to calc the Delta Information " + thr.getMessage());
        }

        //Check which version of the taken alternative of the FC should be taken
        //Check with predecessor and successor 
        //check with planned date
        //Check wtih alphabetically order of the version
        Map<String, Map<String, String>> bcNameToFcNameToVersionToTake = new TreeMap<>();

        try {

            for (String bcName : bcNameToFcNameToAlternativeToTake.keySet()) {
                Map<String, String> fcNameToVersionToTake = new TreeMap<>();
                for (String fcName : bcNameToFcNameToAlternativeToTake.get(bcName).keySet()) {
                    String alternative = bcNameToFcNameToAlternativeToTake.get(bcName).get(fcName);
                    //There should be at least one version for the fc alternative
                    assert (!bcNameToFcNameToAlternativesToVersions.get(bcName).get(fcName).get(alternative).isEmpty());
                    String winningVersion = bcNameToFcNameToAlternativesToVersions.get(bcName).get(fcName).get(alternative).get(0);
                    for (String version : bcNameToFcNameToAlternativesToVersions.get(bcName).get(fcName).get(alternative)) {
                        //Check the versions against each other, which one is the winning
                        DmPPTFCRelease currentRelease = bcNameToFcNameToVersionToFC.get(bcName).get(fcName).get(version);
                        DmPPTFCRelease oldRelease = bcNameToFcNameToVersionToFC.get(bcName).get(fcName).get(winningVersion);
                        //Compare the currentRelease against the old release
                        if (oldRelease.isPredecessor(currentRelease)) {
                            //the current one wins
                            fcNameToVersionToTake.put(fcName, version);
                        } else if (!currentRelease.isPredecessor(oldRelease)) {
                            if (currentRelease.getPlannedDate().toString().isEmpty()
                                    || oldRelease.getPlannedDate().toString().isEmpty()) {
                                if (currentRelease.getVersion().compareTo(oldRelease.getVersion()) > 0) {
                                    //the current one wins
                                    winningVersion = version;
                                    fcNameToVersionToTake.put(fcName, version);
                                }
                            } else if ((currentRelease.getPlannedDate().isLaterThen(oldRelease.getPlannedDate())
                                    || (currentRelease.getPlannedDate().equals(oldRelease.getPlannedDate())
                                    && currentRelease.getVersion().compareTo(oldRelease.getVersion()) > 0))) {
                                //the current one wins
                                fcNameToVersionToTake.put(fcName, version);
                            }
                        } else if ((currentRelease.getPlannedDate().isLaterThen(oldRelease.getPlannedDate())
                                || (currentRelease.getPlannedDate().equals(oldRelease.getPlannedDate())
                                && currentRelease.getVersion().compareTo(oldRelease.getVersion()) > 0))) {
                            //the current one wins
                            fcNameToVersionToTake.put(fcName, version);
                        }
                    }
                    if (!fcNameToVersionToTake.containsKey(fcName)) {
                        fcNameToVersionToTake.put(fcName, winningVersion);
                    }
                }

                bcNameToFcNameToVersionToTake.put(bcName, fcNameToVersionToTake);
            }
        } catch (Throwable thr) {
            logger.warning("PPT WARNING: DmPPTField_Reference_Release_PST_Release: Check which version of the taken alternative of the FC should be taken:  Unable to calc the Delta Information " + thr.getMessage());
        }

        //Important Maps
        //bcNameToAlternativeToTake
        //bcNameToVersionToTake
        //bcNameToVersionToBC
        //bcNameTofcNameToAlternativeToTake        
        //bcNameToFcNameToVersionToTake
        //bcNameToFcNameToVersionToFC
        try {
            for (String bcName : bcNameToFcNameToVersionToTake.keySet()) {
                DmEcvBc fcbcBc = fcbcProgrammstand.PROGRAMMSTAND_PAKETE.getPaketByTitle(bcName);
                for (String fcName : bcNameToFcNameToVersionToTake.get(bcName).keySet()) {
                    String version = bcNameToFcNameToVersionToTake.get(bcName).get(fcName);
                    DmEcvFc fcbcFc = fcbcBc.BC_FC.getFunctionByTitle(fcName);
                    if (fcbcFc == null) {
                        //If the fc is not in the FcBc List, than add it
                        fcbcBc.BC_FC.addElement(new DmEcvFc(bcNameToFcNameToVersionToFC.get(bcName).get(fcName).get(version)));
                    } else {
                        //If the fc is in the FcBc List, than update the version
                        fcbcFc.updateInformation(bcNameToFcNameToVersionToFC.get(bcName).get(fcName).get(version));
                    }
                }
            }
        } catch (Throwable thr) {
            logger.warning("PPT WARNING: DmPPTField_Reference_Release_PST_Release: Last loop => finishing:  Unable to calc the Delta Information " + thr.getMessage());
        }
    }

    @Override
    public DmEcvProgrammstand getElement() {

        if (super.getElement() != null) {
            logger.info("PPT Programmstand: " + RELEASE.getId() + " PST Liste is already loaded");
            super.getElement().PROGRAMMSTAND_NAME.setValue(RELEASE.getTitle());
            return super.getElement();
        }
        logger.info("PPT Programmstand: " + RELEASE.getId() + " FCBC List has to be loaded over Attachments");
        if (!getFCBCListAlreadyTried) {
            this.getFCBC_List();
        }
//        Check if delta calculation is needed at this point
//        by planned Date, and isFcBc List at this release
        if (isFcBcListAtThisPver) {
            if (this.RELEASE.AUSLIEFERDATUM_SOLL.getValue().isLaterOrEqualThen(getProcessDate())) {
                makeDeltaCalculation();
            }
        }
        if (super.getElement() != null) {
            super.getElement().PROGRAMMSTAND_NAME.setValue(RELEASE.getTitle());

            //Enhance the information of the release
            DmPPTPoolProject pool = this.RELEASE.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_POOL.getElement();
            String companyDataId = this.RELEASE.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().COMPANY_DATA_ID.getValueAsText();
            pool.getBcResponsibleTable().setCompanyDataId(companyDataId);
            DmEcvBcLongNameTable bcLong = pool.getBcLongnameTable();
            DmEcvFcLongNameTable fcLong = pool.getFcLongnameTable();
            DmEcvBcResponsibleTable bcResp = pool.getBcResponsibleTable();
            super.getElement().enhanceInformation(bcLong, fcLong, bcResp);
        }
        return super.getElement();
    }

    private PPTDataRule getFcBcListDoubleRule() {
        if (fcbcListDoubleRule == null) {
            fcbcListDoubleRule = new PPTDataRule(fcbcListDoubleRuleDescription);
        }
        return (fcbcListDoubleRule);
    }

    private PPTDataRule getFcBcListMissingRule() {
        if (fcbcListMissingRule == null) {
            fcbcListMissingRule = new PPTDataRule(fcbcListMissingRuleDescription);
        }
        return (fcbcListMissingRule);
    }
}
