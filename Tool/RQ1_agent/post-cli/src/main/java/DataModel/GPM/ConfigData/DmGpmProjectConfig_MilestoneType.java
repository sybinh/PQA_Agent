/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData;

import Rq1Data.Enumerations.ExportCategory;
import Rq1Data.Enumerations.ExportScope;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;

/**
 * Describes a milestone type defined in the project specific config data.
 *
 * @author GUG2WI
 */
public class DmGpmProjectConfig_MilestoneType implements Comparable<DmGpmProjectConfig_MilestoneType> {

    public static final String TAGNAME = "Milestone";

    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_COMMENT = "comment";
    private static final String ATTRIBUTE_EXPORTCATEGORY = "exportCategory";
    private static final String ATTRIBUTE_EXPORTSCOPE = "exportScope";

    final private String milestoneName;
    final private String milestoneComment;
    final private ExportCategory exportCategory;
    final private ExportScope exportScope;

    public DmGpmProjectConfig_MilestoneType(String milestoneName, String milestoneComment, ExportCategory exportCategory, ExportScope exportScope) {
        assert (milestoneName != null);
        assert (milestoneName.isEmpty() == false);
        this.milestoneName = milestoneName;
        this.milestoneComment = (milestoneComment != null) ? milestoneComment : "";
        this.exportCategory = exportCategory;
        this.exportScope = exportScope;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public String getMilestoneComment() {
        return milestoneComment;
    }

    public ExportCategory getExportCategory() {
        return exportCategory;
    }

    public ExportScope getExportScope() {
        return exportScope;
    }

    @Override
    public int compareTo(DmGpmProjectConfig_MilestoneType other) {
        return (milestoneName.compareTo(other.milestoneName));
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DmGpmProjectConfig_MilestoneType) {
            DmGpmProjectConfig_MilestoneType otherMilestone = (DmGpmProjectConfig_MilestoneType) other;
            return (milestoneName.equals(otherMilestone.milestoneName)
                    && milestoneComment.equals(otherMilestone.milestoneComment)
                    && (exportCategory != null && exportCategory.equals(otherMilestone.exportCategory))
                    && (exportScope != null && exportScope.equals(otherMilestone.exportScope)));
        }

        return (false);
    }

    //--------------------------------------------------------------------------
    //
    // Read & Write XML
    //
    //--------------------------------------------------------------------------
    DmGpmProjectConfig_MilestoneType(EcvXmlElement xmlMilestone) throws EcvXmlElement.NotfoundException {
        milestoneName = xmlMilestone.getAttribute(ATTRIBUTE_NAME);
        if (milestoneName == null || milestoneName.isEmpty()) {
            throw (new EcvXmlElement.NotfoundException("Attribute for milestone", xmlMilestone.getFullName(), ATTRIBUTE_NAME));
        }
        String temp = xmlMilestone.getAttribute(ATTRIBUTE_COMMENT);
        milestoneComment = (temp != null) ? temp : "";
        exportCategory = ExportCategory.getValueOf(xmlMilestone.getAttribute(ATTRIBUTE_EXPORTCATEGORY));
        exportScope = ExportScope.getValueOf(xmlMilestone.getAttribute(ATTRIBUTE_EXPORTSCOPE));

    }

    EcvXmlElement provideAsXml() {
        EcvXmlEmptyElement element = new EcvXmlEmptyElement(TAGNAME);
        element.addAttribute(ATTRIBUTE_NAME, milestoneName);
        if (milestoneComment.isEmpty() == false) {
            element.addAttribute(ATTRIBUTE_COMMENT, milestoneComment);
        }
        
        if (exportCategory != null) {
            element.addAttribute(ATTRIBUTE_EXPORTCATEGORY, exportCategory.toString());
        }
        
        if (exportScope != null) {
            element.addAttribute(ATTRIBUTE_EXPORTSCOPE, exportScope.toString());
        }

        return (element);
    }

}
