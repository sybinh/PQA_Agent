/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.OrgData;

import DataModel.DmElementI;
import DataModel.DmElementI.ChangeListener;
import DataModel.GPM.DmGpmUserRole;
import Rq1Data.GPM.GpmXmlTable_GpmUserRoles;
import static DataModel.GPM.DmGpmUserRole.*;
import DataModel.Rq1.Fields.DmRq1Field_Xml;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1User;
import DataModel.Rq1.Records.DmRq1UserRole;
import DataModel.DmMappedElement;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 * Stores the data of the org chart in a XML structure.
 *
 * If the XML structure does not (yet) exist, then a default structure is
 * created. Any role definition available in the list of user roles is added to
 * this default structure.
 *
 * @author GUG2WI
 */
public class DmGpmField_OrgData extends DmRq1Field_Xml implements ChangeListener {

    final private static Logger LOGGER = Logger.getLogger(DmGpmField_OrgData.class.getCanonicalName());

    static final private String TAG_PERSON = "Person";
    static final private String TAG_PROJECT = "Project";
    static final private String TAG_CHILDS = "Childs";
    static final private String TAG_SUPPORTERS = "Supporters";
    static final private String ATTRIBUTE_NAME = "name";
    static final private String ATTRIBUTE_RQ1_USER_ID = "rq1UserId";
    static final private String ATTRIBUTE_ROLE = "role";
    static final public String EMPTY_NAME = "<Name>";
    static final private String ATTRIBUTE_FORWARD_PROJECT = "ForwardProject";

    final private DmRq1Project project;
    private OrgData_Element topElementData = null;

    public DmGpmField_OrgData(DmRq1Project project, Rq1XmlSubField_Xml rq1XmlField, String nameForUserInterface) {
        super(rq1XmlField, nameForUserInterface);
        assert (project != null);
        
        this.project = project;

        project.addChangeListener(this);
    }

    //--------------------------------------------------------------------------
    //
    // Set field value in org data format
    //
    //--------------------------------------------------------------------------
    public void setOrgData(OrgData_Element newTopElementData) {
        assert (newTopElementData != null);

        topElementData = newTopElementData;

        EcvXmlContainerElement xmlTopElement = encode(topElementData);
        EcvXmlContainerElement xmlOrgData = new EcvXmlContainerElement("OrgData");
        xmlOrgData.addElement(xmlTopElement);

        super.setValue(xmlOrgData);
    }

    private EcvXmlContainerElement encode(OrgData_Element element) {
        assert (element != null);

        EcvXmlContainerElement xmlContainer;
        if (element instanceof OrgData_Person) {
            OrgData_Person person = (OrgData_Person) element;
            xmlContainer = new EcvXmlContainerElement(TAG_PERSON);
            xmlContainer.addAttribute(ATTRIBUTE_ROLE, person.getRole());
            if (person.getRq1UserId() != null) {
                xmlContainer.addAttribute(ATTRIBUTE_RQ1_USER_ID, person.getRq1UserId());
            }
            xmlContainer.addAttribute(ATTRIBUTE_NAME, person.getCustomName());
            if (person.getForwardProjectId() != null && !person.getForwardProjectId().isEmpty()) {
                xmlContainer.addAttribute(ATTRIBUTE_FORWARD_PROJECT, person.getForwardProjectId());
            }
        } else if (element instanceof OrgData_Project) {
            OrgData_Project project = (OrgData_Project) element;
            xmlContainer = new EcvXmlContainerElement(TAG_PROJECT);
            xmlContainer.addAttribute(ATTRIBUTE_NAME, project.getName());
        } else {
            throw (new Error("Unkown data type " + element.getClass().getCanonicalName()));
        }

        if (element.getChilds().isEmpty() == false) {
            EcvXmlContainerElement xmlChilds = new EcvXmlContainerElement(TAG_CHILDS);
            for (OrgData_Element child : element.getChilds()) {
                xmlChilds.addElement(encode(child));
            }
            xmlContainer.addElement(xmlChilds);
        }

        if (element.getSupporters().isEmpty() == false) {
            EcvXmlContainerElement xmlSupporters = new EcvXmlContainerElement(TAG_SUPPORTERS);
            for (OrgData_Element suppporter : element.getSupporters()) {
                xmlSupporters.addElement(encode(suppporter));
            }
            xmlContainer.addElement(xmlSupporters);
        }

        return (xmlContainer);
    }

    //--------------------------------------------------------------------------
    //
    // Get the org data format
    //
    //--------------------------------------------------------------------------
    public OrgData_Element getOrgData() {

        if (topElementData != null) {
            return (topElementData);
        }

        EcvXmlContainerElement xmlOrgData = super.getValue();
        List<EcvXmlElement> xmlTopElements = xmlOrgData.getElementList(TAG_PERSON);
        if (xmlTopElements.isEmpty() == false) {
            topElementData = decode(xmlTopElements.get(0));
            if (topElementData != null) {
                return (topElementData);
            }
        }

        OrgData_Element defaultOrgData = buildDefaultOrgData();
        addFromUserRoleList(project, defaultOrgData);
        topElementData = defaultOrgData;
        return (topElementData);
    }

    public DmRq1User getUserForRole(String userRole) {
        if (userRole != null) {
            OrgData_Person personForRole = OrgData_Person.getPersonForRole(userRole, getOrgData());
            if (personForRole != null) {
                if (personForRole.getRq1UserId() != null) {
                    //
                    // Find user by user id
                    //
                    for (var map : project.HAS_PROJECT_MEMBERS.getElementList()) {
                        DmRq1User user = map.getTarget();
                        if (user.LOGIN_NAME.getValueAsText().endsWith(personForRole.getRq1UserId())) {
                            return (user);
                        }
                    }
                } else {
                    //
                    // Find user by full name
                    //
                    for (var mappedUser : project.HAS_PROJECT_MEMBERS.getElementList()) {
                        DmRq1User user = mappedUser.getTarget();
                        if (user.FULLNAME.getValueAsText().endsWith(personForRole.getCustomName())) {
                            return (user);
                        }
                    }
                    //
                    // Find user by name without organisation.
                    // This is done because the organisation might change which causes the check in the loop above to fail.
                    //
                    String nameWithoutOrga = personForRole.getCustomName();
                    int i_bracket = nameWithoutOrga.indexOf('(');
                    if (i_bracket > 0) {
                        nameWithoutOrga = nameWithoutOrga.substring(0,i_bracket);
                    }
                    nameWithoutOrga = nameWithoutOrga.trim();
                    for (var map : project.HAS_PROJECT_MEMBERS.getElementList()) {
                        DmRq1User user = map.getTarget();
                        if (user.FULLNAME.getValueAsText().contains(nameWithoutOrga)) {
                            return (user);
                        }
                    }
                }

            }
        }
        return (null);
    }

    public String getForwardProjectIdForRole(String userRole) {
        if (userRole != null) {
            OrgData_Person personForRole = OrgData_Person.getPersonForRole(userRole, getOrgData());
            if (personForRole != null) {
                return personForRole.getForwardProjectId();
            }
        }
        return (null);
    }

    public DmRq1Project getForwardProjectForRole(String userRole) {
        String forwardProject = null;
        if(userRole != null) {
            forwardProject = getForwardProjectIdForRole(userRole);
            
            if (forwardProject != null && !forwardProject.isEmpty()) {
                DmRq1ElementInterface dmElement = DmRq1Element.getElementByRq1Id(forwardProject);

                if (dmElement != null && dmElement instanceof DmRq1Project) {
                    return (DmRq1Project) dmElement;
                }
            }
        }

        return (null);
    }

    @Override
    public void setValue(EcvXmlContainerElement v) {
        throw (new Error("Invalid method call for class " + this.getClass().getCanonicalName()));
    }

    @Override
    public EcvXmlContainerElement getValue() {
        return (null);
    }

    @Override
    public void changed(DmElementI changedElement) {
        topElementData = null;
    }

    //--------------------------------------------------------------------------
    //
    // Extract org data from org data tag
    //
    //--------------------------------------------------------------------------
    private OrgData_Element decode(EcvXmlElement xmlContent) {
        assert (xmlContent != null);

        //
        // Create matching elementData and set parameters for the element
        //
        OrgData_Element elementData;
        switch (xmlContent.getName()) {
            case TAG_PERSON:
                elementData = createPerson(xmlContent);
                break;

            case TAG_PROJECT:
                String name = xmlContent.getAttribute(ATTRIBUTE_NAME);
                elementData = new OrgData_Project(name);
                break;

            default:
                LOGGER.log(Level.WARNING, "Unexpected tag name in GPM Org Data: {0}", xmlContent.getFullName());
                return (null);
        }

        if (xmlContent instanceof EcvXmlContainerElement) {
            //
            // Decode and add childs
            //
            for (EcvXmlContainerElement xmlChilds : ((EcvXmlContainerElement) xmlContent).getContainerElementList(TAG_CHILDS)) {
                for (EcvXmlElement xmlChild : xmlChilds.getElementList()) {
                    OrgData_Element childData = decode(xmlChild);
                    if (childData != null) {
                        elementData.addChild(childData);
                    }
                }
            }

            //
            // Decode and add suppporters
            //
            for (EcvXmlContainerElement xmlSupporters : ((EcvXmlContainerElement) xmlContent).getContainerElementList(TAG_SUPPORTERS)) {
                for (EcvXmlElement xmlSupporter : xmlSupporters.getElementList()) {
                    OrgData_Element supporterData = decode(xmlSupporter);
                    if (supporterData != null) {
                        elementData.addSupporter(supporterData);
                    }
                }
            }
        }

        return (elementData);
    }

    private OrgData_Person createPerson(EcvXmlElement xmlContent) {
        assert (xmlContent != null);
        String role = xmlContent.getAttribute(ATTRIBUTE_ROLE);
        String rq1UserId = xmlContent.getAttribute(ATTRIBUTE_RQ1_USER_ID);
        String name = xmlContent.getAttribute(ATTRIBUTE_NAME);
        String forwardProject = xmlContent.getAttribute(ATTRIBUTE_FORWARD_PROJECT);
        if(forwardProject == null) {
            forwardProject = "";
        }

        if (role.equals("SW-PjM")) {
            return (new OrgData_Person(DmGpmUserRole.SPJM_SW.getCombinedName(), rq1UserId, name, forwardProject));
        }
        if (role.equals("SysArchPD")) {
            return (new OrgData_Person(DmGpmUserRole.SYS_ARCH.getCombinedName(), rq1UserId, name, forwardProject));
        }
        if (role.equals("AE-Project")) {
            return (new OrgData_Person("PLEASE SELECT ROLE", rq1UserId, name, forwardProject));
        }

        for (DmGpmUserRole roles : DmGpmUserRole.values()) {
            if (roles.getCombinedName().equals(role)) {
                return (new OrgData_Person(role, rq1UserId, name, forwardProject));
            }
            if (role.equals(roles.getLongName())) {
                return (new OrgData_Person(roles.getCombinedName(), rq1UserId, name, forwardProject));
            }
            if (role.equals(roles.getAbbreviation())) {
                return (new OrgData_Person(roles.getCombinedName(), rq1UserId, name, forwardProject));
            }

        }
        return (new OrgData_Person(role, rq1UserId, name, forwardProject));
    }

    private OrgData_Project createProject(EcvXmlElement xmlContent) {
        assert (xmlContent != null);
        String name = xmlContent.getAttribute(ATTRIBUTE_NAME);
        return (new OrgData_Project(name));
    }

    //--------------------------------------------------------------------------
    //
    // Build the default org data if no org data tag exists
    //
    //--------------------------------------------------------------------------
    private OrgData_Element buildDefaultOrgData() {

        OrgData_Person systemProjectManager = new OrgData_Person(SYS_PJM.getCombinedName(), null, EMPTY_NAME);

        OrgData_Project calibration = new OrgData_Project("Calibration (QG0 before May2020)");
        systemProjectManager.addChild(calibration);
        OrgData_Person subPjmCalibration = new OrgData_Person(SPJM_CAL.getCombinedName(), null, EMPTY_NAME);
        calibration.addChild(subPjmCalibration);
        OrgData_Person calibrationEngineer = new OrgData_Person(CAL_ENG.getCombinedName(), null, EMPTY_NAME);
        subPjmCalibration.addChild(calibrationEngineer);
        OrgData_Person qChampionDepartmentCal = new OrgData_Person(QC_D_CAL.getCombinedName(), null, EMPTY_NAME);
        calibrationEngineer.addChild(qChampionDepartmentCal);

        OrgData_Person ecuProjectManager = new OrgData_Person(PJM_ECU.getCombinedName(), null, EMPTY_NAME);
        systemProjectManager.addChild(ecuProjectManager);

        OrgData_Project hardware = new OrgData_Project("Hardware");
        ecuProjectManager.addChild(hardware);
        OrgData_Person subPjmHardware = new OrgData_Person(SPJM_HW.getCombinedName(), null, EMPTY_NAME);
        hardware.addChild(subPjmHardware);
        OrgData_Person hwArchitectEcu = new OrgData_Person(HW_ARCH_ECU.getCombinedName(), null, EMPTY_NAME);
        subPjmHardware.addChild(hwArchitectEcu);
        OrgData_Person hwIntegrator = new OrgData_Person(HW_INT.getCombinedName(), null, EMPTY_NAME);
        hwArchitectEcu.addChild(hwIntegrator);
        OrgData_Person aeProjectManager = new OrgData_Person(SALES.getCombinedName(), null, EMPTY_NAME);
        hwIntegrator.addChild(aeProjectManager);
        OrgData_Person hwEngineeringProductQuality = new OrgData_Person(HW_EPQ.getCombinedName(), null, EMPTY_NAME);
        aeProjectManager.addChild(hwEngineeringProductQuality);

        OrgData_Project software = new OrgData_Project("Software");
        ecuProjectManager.addChild(software);
        OrgData_Person subPjmSoftware = new OrgData_Person(SPJM_SW.getCombinedName(), null, EMPTY_NAME);
        software.addChild(subPjmSoftware);
        OrgData_Person swSd = new OrgData_Person(SW_SD.getCombinedName(), null, EMPTY_NAME);
        subPjmSoftware.addChild(swSd);
        OrgData_Person swPd = new OrgData_Person(SW_PD.getCombinedName(), null, EMPTY_NAME);
        swSd.addChild(swPd);
        OrgData_Person sysArchPd = new OrgData_Person(SYS_ARCH.getCombinedName(), null, EMPTY_NAME);
        swPd.addChild(sysArchPd);

        OrgData_Project calibrationNew = new OrgData_Project("Calibration (QG0 after May2020)");
        ecuProjectManager.addChild(calibrationNew);
        OrgData_Person subPjmCalibrationNew = new OrgData_Person(SPJM_CAL.getCombinedName(), null, EMPTY_NAME);
        calibrationNew.addChild(subPjmCalibrationNew);
        OrgData_Person calibrationEngineerNew = new OrgData_Person(CAL_ENG.getCombinedName(), null, EMPTY_NAME);
        subPjmCalibrationNew.addChild(calibrationEngineerNew);
        OrgData_Person qChampionDepartmentCalNew = new OrgData_Person(QC_D_CAL.getCombinedName(), null, EMPTY_NAME);
        calibrationEngineerNew.addChild(qChampionDepartmentCalNew);

        OrgData_Person psm = new OrgData_Person(PSM.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(psm);
        OrgData_Person fmea = new OrgData_Person(FMEA_MOD.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(fmea);
        OrgData_Person qg = new OrgData_Person(QG_MODERATOR.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(qg);
        OrgData_Person leadEpq = new OrgData_Person(LEAD_EPQ.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(leadEpq);
        OrgData_Person cmResponsible = new OrgData_Person(CM_R.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(cmResponsible);
        OrgData_Person technicalSalesEcu = new OrgData_Person(TECHNICAL_SALES_ECU.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(technicalSalesEcu);
        OrgData_Person technicalSalesSw = new OrgData_Person(TECHNICAL_SALES_SW_CALIBRATION.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(technicalSalesSw);
        OrgData_Person subContractManager = new OrgData_Person(SPJM_SW.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(subContractManager);
        OrgData_Person pSecM = new OrgData_Person(P_SEC_M.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(pSecM);
        OrgData_Person launchManager = new OrgData_Person(LAM.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(launchManager);
        OrgData_Person smartCardResponsible = new OrgData_Person(SCR.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(smartCardResponsible);
        OrgData_Person productLineArchitect = new OrgData_Person(PL_A.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(productLineArchitect);
        OrgData_Person pecC = new OrgData_Person(PEC_C.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(pecC);
        OrgData_Person edr = new OrgData_Person(EDR.getCombinedName(), null, EMPTY_NAME);
        ecuProjectManager.addSupporter(edr);

        return (systemProjectManager);
    }

    static void addFromUserRoleList(DmRq1Project project, OrgData_Element defaultOrgData) {
        assert (project != null);
        assert (defaultOrgData != null);

        Map<String, OrgData_Person> rolesInDefaultOrgData = new TreeMap<>();
        addToRoleMap(rolesInDefaultOrgData, defaultOrgData);

        Map<String, String> projectMembers = null;
        Map<GpmXmlTable_GpmUserRoles.Record, String> usersWithoutOrgData = new IdentityHashMap<>();
        for (GpmXmlTable_GpmUserRoles.Record userRecord : project.GPM_USER_ROLES.getValueAsRecordList()) {
            //
            // Handle only roles for which a user id is set
            //
            String userId = userRecord.getUser_id();
            if ((userId != null) && (userId.isEmpty() == false)) {

                //
                // Lazy load of project members
                //
                if (projectMembers == null) {
                    projectMembers = new TreeMap<>();
                    for (DmMappedElement<DmRq1UserRole, DmRq1User> member : project.HAS_PROJECT_MEMBERS.getElementList()) {
                        DmRq1User rq1User = member.getTarget();
                        projectMembers.put(rq1User.LOGIN_NAME.getValueAsText(), rq1User.FULLNAME.getValueAsText());
                    }
                }

                //
                // Get full name for user id
                //
                String userFullName = projectMembers.get(userId);
                if (userFullName == null) {
                    userFullName = userId;
                }

                //
                // Get org data by role name
                //
                OrgData_Person userOrgData = rolesInDefaultOrgData.remove(userRecord.getRole());
                if (userOrgData == null) {
                    //
                    // Try to get org data by role description
                    //
                    DmGpmUserRole userRole = DmGpmUserRole.getByLongNameOrAbbreviation(userRecord.getRole());
                    if (userRole != null) {
                        userOrgData = rolesInDefaultOrgData.remove(userRole.getLongName());
                    }
                }

                if (userOrgData != null) {
                    //
                    // Set name and user id for org data element
                    //
                    userOrgData.setRq1UserId(userId);
                    userOrgData.setCustomName(userFullName);
                } else {
                    //
                    // Keep user without org data element for later adding.
                    //
                    usersWithoutOrgData.put(userRecord, userFullName);
                }
            }
        }

        //
        // Add rest of users to irg data
        //
        if (usersWithoutOrgData.isEmpty() == false) {
            OrgData_Project otherProject = new OrgData_Project("Other Users");
            defaultOrgData.addChild(otherProject);

            OrgData_Element lastElement = otherProject;
            for (Map.Entry<GpmXmlTable_GpmUserRoles.Record, String> userWithoutOrgData : usersWithoutOrgData.entrySet()) {
                String role = userWithoutOrgData.getKey().getRole();
                String userId = userWithoutOrgData.getKey().getUser_id();
                String fullName = userWithoutOrgData.getValue();
                OrgData_Person person = new OrgData_Person(role, userId, fullName);

                lastElement.addChild(person);
                lastElement = person;
            }
        }
    }

    static private void addToRoleMap(Map<String, OrgData_Person> map, OrgData_Element element) {
        assert (map != null);

        if (element instanceof OrgData_Person) {
            map.put(((OrgData_Person) element).getRole(), (OrgData_Person) element);
        } else if (element instanceof OrgData_Project == false) {
            throw (new Error("Unknown type: " + element.getClass().getCanonicalName()));
        }

        for (OrgData_Element child : element.getChilds()) {
            addToRoleMap(map, child);
        }
        for (OrgData_Element supporter : element.getSupporters()) {
            addToRoleMap(map, supporter);
        }
    }

}
