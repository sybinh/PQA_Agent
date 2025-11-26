/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules;

import Customization.EcvCustomizable;
import Customization.EcvCustomizer;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1Project;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlParser;
import util.EcvXmlTextElement;

/**
 *
 * @author GUG2WI
 */
public class ConfigurableRulesCustomizer implements EcvCustomizable {

    final private static Logger LOGGER = Logger.getLogger(ConfigurableRulesCustomizer.class.getCanonicalName());

    private static final String LIST_OF_ACTIVE_PROJECTS = "listOfActiveProjects";

    private static boolean projectsMapIsLoaded = false;
    private static Map<String, DmRq1Project> appliedProjects = new HashMap<>();

    public static void setListOfActiveProjects(Collection<String> projectIds) {
        assert (projectIds != null);
        EcvCustomizer customizer = new EcvCustomizer(ConfigurableRulesCustomizer.class);

        if (projectIds.isEmpty() == true) {
            customizer.delete(LIST_OF_ACTIVE_PROJECTS);
            return;
        }

        EcvXmlContainerElement list = new EcvXmlContainerElement("ActiveProjects");
        for (String id : projectIds) {
            if ((id != null) && (id.isEmpty() == false)) {
                list.addElement(new EcvXmlTextElement("ProjectId", id));
            }
        }

        customizer.setString(LIST_OF_ACTIVE_PROJECTS, list.getXmlString());
    }

    public static Set<String> getListOfActiveProjects() {

        Set<String> result = new TreeSet<>();
        EcvCustomizer customizer = new EcvCustomizer(ConfigurableRulesCustomizer.class);

        String xmlString = customizer.getString(LIST_OF_ACTIVE_PROJECTS, "");
        if (xmlString.isEmpty() == false) {
            try {
                EcvXmlParser parser = new EcvXmlParser(xmlString);
                EcvXmlElement list = parser.parse();
                if (list instanceof EcvXmlContainerElement) {
                    for (EcvXmlElement element : ((EcvXmlContainerElement) list).getElementList()) {
                        if (element instanceof EcvXmlTextElement) {
                            result.add(((EcvXmlTextElement) element).getText());
                        } else {
                            LOGGER.log(Level.SEVERE, "Parsing of " + LIST_OF_ACTIVE_PROJECTS + " failed: Element is not a text element.");
                        }
                    }
                } else {
                    LOGGER.log(Level.SEVERE, "Parsing of " + LIST_OF_ACTIVE_PROJECTS + " failed: Content is not a container.");
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Parsing of " + LIST_OF_ACTIVE_PROJECTS + " failed.", ex);
                ToolUsageLogger.logError(ConfigurableRulesCustomizer.class.getCanonicalName(), ex);
            }
        }

        return (result);
    }

    public static Map<String, DmRq1Project> getAppliedProjectsGroupedById() {
        if (!projectsMapIsLoaded) {
            Set<String> projectIds = getListOfActiveProjects();
            if (projectIds != null && !projectIds.isEmpty()) {
                projectIds.stream().forEach(id -> {
                    if (!id.isEmpty()) {
                        DmRq1Project project = getProjectFromLoginUser(id);
                        if (project != null) {
                            appliedProjects.put(id, project);
                        }
                    }
                });
            }
            projectsMapIsLoaded = true;
        }
        return appliedProjects;
    }

    public static DmRq1Project getProjectFromLoginUser(String id) {
        DmRq1ElementInterface element = DmRq1Element.getElementByRq1Id(id);
        return element instanceof DmRq1Project ? (DmRq1Project) element : null;
    }

    public static void setAppliedProjectsGroupedByIds(Map<String, DmRq1Project> projects) {
        appliedProjects = projects;
        // convert appliedProjects to a list of ids
        setListOfActiveProjects(appliedProjects.keySet().stream().collect(Collectors.toList()));
    }
}
