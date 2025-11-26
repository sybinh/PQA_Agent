/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmProjectAreaList;
import DataStore.ALM.DsAlmField_ResourceList;
import DataStore.ALM.DsAlmField_Text;
import DataStore.ALM.DsAlmLoadErrorRecord;
import DataStore.ALM.DsAlmRecord;
import DataStore.ALM.DsAlmRecordFactory;
import DataStore.ALM.DsAlmRecordI;
import DataStore.ALM.DsAlmRecord_ChangeSet;
import DataStore.ALM.DsAlmRecord_Iteration;
import DataStore.ALM.DsAlmRecord_ProjectArea;
import java.util.ArrayList;
import DataStore.ALM.DsAlmRecord_Requirement;
import DataStore.ALM.DsAlmRecord_TeamArea;
import DataStore.ALM.DsAlmRecord_User;
import DataStore.ALM.DsAlmRecord_Workitem;
import OslcAccess.ALM.OslcAlmServerDescription;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author GUG2WI
 */
public class DmAlmElementFactory {

    private static final Logger LOGGER = Logger.getLogger(DmAlmElementFactory.class.getCanonicalName());

    final private static Map<String, DmAlmElementI> elementCache = new TreeMap<>();

    public static DmAlmElementI getElementByUrl(String originalUrl) {
        assert (originalUrl != null);
        assert (originalUrl.isEmpty() == false);

        String url = redirectUrl(originalUrl);

        DmAlmElementI dmElement = elementCache.get(url);
        if (dmElement == null) {
            DsAlmRecordI dsRecord = DsAlmRecordFactory.getRecordByUrl(url);
            if (dsRecord != null) {
                dmElement = build(dsRecord);
                if (dmElement == null) {
                    LOGGER.severe("No DmAlmElement found for DsAlmRecord!\nURL = " + url + "\n" + dsRecord.getFieldsAsTextSorted());
                }
                elementCache.put(url, dmElement);
            }
        }

        return (dmElement);
    }

    public static List<DmAlmElementI> getQueryElementByUrl(String originalUrl) {
        assert (originalUrl != null);
        assert (originalUrl.isEmpty() == false);

        String url = redirectUrl(originalUrl);
        
        List<DsAlmRecord_Workitem> dsRecordList = DsAlmRecordFactory.getQueryByUrl(url);
        List<DmAlmElementI> dmElementList = new ArrayList<>();
        
        if(dsRecordList != null) {
           dmElementList = build(dsRecordList);
        }
        
        return dmElementList;
    }

    public static DmAlmProjectAreaList getProjectAreaList() {

        DmAlmProjectAreaList dmProjectAreaList = new DmAlmProjectAreaList("Project Areas");
        DmAlmElementI dmElement;

        List<DsAlmRecordI> projectAreaRecordList = DsAlmRecordFactory.loadProjectAreaRecordList();
        if (projectAreaRecordList != null) {
            for (DsAlmRecordI dsRecord : projectAreaRecordList) {
                dmElement = build(dsRecord);
                if (dmElement instanceof DmAlmProjectArea) {
                    dmProjectAreaList.addElement((DmAlmProjectArea) dmElement);
                } else {
                    LOGGER.severe("Unexpected record type " + dmElement.getClass().getCanonicalName() + " for " + dmElement.getId());
                }
            }
        }
        return dmProjectAreaList;
    }

    private static List<DmAlmElementI> build(List<DsAlmRecord_Workitem> recordList) {
        List<DmAlmElementI> dmElementList = new ArrayList<>();

        for (DsAlmRecordI dsRecord : recordList) {
            DmAlmElementI dmElement = build(dsRecord);
            dmElementList.add(dmElement);
        }

        return dmElementList;
    }

    private static DmAlmElementI build(DsAlmRecordI dsRecord) {
        assert (dsRecord != null);

        //
        // Handle requirements
        //
        if (dsRecord instanceof DsAlmRecord_Requirement) {
            DsAlmField_ResourceList rdfType = (DsAlmField_ResourceList) dsRecord.getFieldByName("rdf:type");
            if (rdfType != null) {
                String value = rdfType.getDataModelValueAsString();

                if (value.contains(DmAlmRequirement_Software.REQ_TYPE)) {
                    // Example: [http://bosch.com/dng/artifact-types/SOFTWARE_REQ, http://jazz.net/ns/rm#Text]
                    return (new DmAlmRequirement_Software((DsAlmRecord_Requirement) dsRecord));
                } else if (value.contains(DmAlmRequirement_System.REQ_TYPE)) {
                    return (new DmAlmRequirement_System((DsAlmRecord_Requirement) dsRecord));
                } else if (value.contains(DmAlmRequirement_SystemInterface.REQ_TYPE)) {
                    return (new DmAlmRequirement_SystemInterface((DsAlmRecord_Requirement) dsRecord));
                }
            }

            return (new DmAlmRequirement_Unspecific((DsAlmRecord_Requirement) dsRecord));
        }

        //
        // Handle change sets
        //
        if (dsRecord instanceof DsAlmRecord_ChangeSet) {
            return (new DmAlmChangeSet((DsAlmRecord_ChangeSet) dsRecord));
        }
        //
        // Handle load error
        //
        if (dsRecord instanceof DsAlmLoadErrorRecord) {
            return (new DmAlmLoadError((DsAlmLoadErrorRecord) dsRecord));
        }

        //
        // Handle project areas
        //
        if (dsRecord instanceof DsAlmRecord_ProjectArea) {
            return (new DmAlmProjectArea((DsAlmRecord_ProjectArea) dsRecord));
        }

        //
        // Handle team areas
        //
        if (dsRecord instanceof DsAlmRecord_TeamArea) {
            return (new DmAlmTeamArea((DsAlmRecord_TeamArea) dsRecord));
        }

        //
        // Handle iterations
        //
        if (dsRecord instanceof DsAlmRecord_Iteration) {
            return (new DmAlmIteration((DsAlmRecord_Iteration) dsRecord));
        }
        
        if(dsRecord instanceof DsAlmRecord_User) {
            return (new DmAlmUser((DsAlmRecord) dsRecord));
        }

        //
        // Handle workitems
        //
        // Example: https://rb-alm-05-q.de.bosch.com/ccm/resource/itemName/com.ibm.team.workitem.WorkItem/70387W
        if (dsRecord instanceof DsAlmRecord_Workitem) {
            DsAlmField_Text dctermsType = (DsAlmField_Text) dsRecord.getFieldByName("dcterms:type");
            if (dctermsType != null) {
                switch (dctermsType.getDataModelValue()) {
                    case DmAlmWorkitem_Epic.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Epic((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_PortfolioEpic.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_PortfolioEpic((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_SolutionEpic.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_SolutionEpic((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Capability.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Capability((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Task.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Task((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Feature.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Feature((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Risk.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Risk((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Story.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Story((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Delivery.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Delivery((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_DefectFix.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_DefectFix((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Review.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Review((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Generic.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Generic((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_SupportTicket.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_SupportTicket((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_FBDefect.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_FBDefect((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Change.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Change((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Problem.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Problem((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Impediment.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Impediment((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Release.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Release((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Meeting.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Meeting((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Defect.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Defect((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_LearningMilestone.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_LearningMilestone((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Retrospective.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Retrospective((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Improvement.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Improvement((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_CustomerEpic.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_CustomerEpic((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_ProgramEpic.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_ProgramEpic((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_TrackBuildItem.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_TrackBuildItem((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_AdoptionItem.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_AdoptionItem((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_BusinessNeed.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_BusinessNeed((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Commercial.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Commercial((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_DefectEval.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_DefectEval((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Issue.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Issue((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Milestone.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Milestone((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_Need.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_Need((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_PIObjective.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_PIObjective((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_RelevantStream.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_RelevantStream((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_RiskAction.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_RiskAction((DsAlmRecord) dsRecord));
                    case DmAlmWorkitem_ProjectChangeRequest.ELEMENT_TYPE:
                        return (new DmAlmWorkitem_ProjectChangeRequest((DsAlmRecord) dsRecord));

                }

                return (new DmAlmWorkitem_Unspecific((DsAlmRecord_Workitem) dsRecord));
            }
        }

        return (null);
    }

    /**
     * Not all URL used in links in RQ1 refer to the pure element that can be
     * accessed via OSLC. Some URL refer to the HTML representation of the
     * element. Such a URL cannot be opened via OSLC. The method 'originalUrl'
     * checks the original URL for such HTML links and creates an URL that can
     * be used for OSLC.
     *
     * @param originalUrl
     * @return
     */
    static String redirectUrl(String originalUrl) {
        assert (originalUrl != null);
        assert (originalUrl.isEmpty() == false);

        OslcAlmServerDescription server = OslcAlmServerDescription.getServerDescriptionForUrl(originalUrl);
        if (server != null) {
            //
            final String workItemPattern = "#action=com.ibm.team.workitem.viewWorkItem&id=";
            int patternIndex = originalUrl.indexOf(workItemPattern);
            if (patternIndex > 0) {
                String id = originalUrl.substring(patternIndex + workItemPattern.length(), originalUrl.length());
                return (buildUrlForWorkitem(server, id));
            }
            //
            final String changeSetStartPattern = "#action=com.ibm.rdm.web.pages.editChangeSet&configurationUri=https://rb-alm-05-p.de.bosch.com/rm/cm/changeset/";
            final String changeSetEndPattern = "&tab=changedResourcesId";
            int patternStartIndex = originalUrl.indexOf(changeSetStartPattern);
            int patternEndIndex = originalUrl.indexOf(changeSetEndPattern);
            if (patternStartIndex > 0 && patternEndIndex > 0 && patternStartIndex < patternEndIndex) {
                String id = originalUrl.substring(patternStartIndex + changeSetStartPattern.length(), patternEndIndex);
                return (buildUrlForChangeSetPattern(server, id));
            }
        }

        return (originalUrl);
    }

    static String buildUrlForWorkitem(OslcAlmServerDescription server, String id) {
        assert (server != null);
        assert (id != null);
        assert (id.isEmpty() == false);
        return (server.getWorkItemUrl() + id);
    }

    static String buildUrlForChangeSetPattern(OslcAlmServerDescription server, String id) {
        assert (server != null);
        assert (id != null);
        assert (id.isEmpty() == false);
        return (server.getOslcUrl() + "/rm/cm/changeset/" + id);
    }
}
