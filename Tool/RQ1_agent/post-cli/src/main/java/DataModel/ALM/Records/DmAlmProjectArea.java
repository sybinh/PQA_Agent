/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Boolean;
import DataModel.ALM.Fields.DmAlmField_Date;
import DataModel.ALM.Fields.DmAlmField_ReferencedAlmElementsForProjectArea;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord_ProjectArea;
import UiSupport.UiTreeViewRootElementI;

/**
 *
 * @author CNI83WI
 */
public class DmAlmProjectArea extends DmAlmElement implements UiTreeViewRootElementI {
    public static final String ELEMENT_TYPE = "ProjectArea";
    
    final public DmAlmField_Text TITLE;
    final public DmAlmField_Text SUMMARY;
    final public DmAlmField_Boolean ARCHIVED;
    final public DmAlmField_Date MODIFIED;
    final public DmAlmField_Boolean IS_ACCESS_VISIBLE_TO_MEMBERS;
    final public DmAlmField_Boolean IS_ACCESS_PUBLIC;
    final public DmAlmField_Text DESCRIPTION;
    final public DmAlmField_Boolean IS_ACCESS_VISIBLE_TO_ACCESS_LIST;
    final public DmAlmField_ReferencedAlmElementsForProjectArea<DmAlmWorkitem> REFERENCED_WORKITEMS;
    final public DmAlmField_ReferencedAlmElementsForProjectArea<DmAlmWorkitem_Capability> REFERENCED_CAPABILITIES;
    final public DmAlmField_ReferencedAlmElementsForProjectArea<DmAlmWorkitem_Task> REFERENCED_TASKS;

    public DmAlmProjectArea(DsAlmRecord_ProjectArea dsAlmProjectArea) {
        super(ELEMENT_TYPE, dsAlmProjectArea);
        
        ignoreField("rdf:type");
        TITLE = addTextField("dcterms:title", "Title");
        ARCHIVED = addBooleanField("oslc:archived", "Archived");
        SUMMARY = addTextField("process:summary", "Summary", true);
        MODIFIED = addDateField("dcterms:modified", "Modified");
        IS_ACCESS_VISIBLE_TO_MEMBERS = addBooleanField("process:isAccessVisibleToMembers", "Access visible to Members");
        IS_ACCESS_PUBLIC = addBooleanField("process:isAccessPublic", "Access Public");
        DESCRIPTION = addTextField("dcterms:description", "Description", true);
        ignoreField("oslc:instanceShape");
        IS_ACCESS_VISIBLE_TO_ACCESS_LIST = addBooleanField("process:isAccessVisibleToAccessList", "Access Visible To Access List");
        ignoreField("acc:accessContext");

        addField(REFERENCED_WORKITEMS = new DmAlmField_ReferencedAlmElementsForProjectArea<>(getUrl(), "Referenced Workitems"));
        addResourceListField("referenced work items", "Referenced Work Items", true);

        addField(REFERENCED_CAPABILITIES = new DmAlmField_ReferencedAlmElementsForProjectArea<>(getUrl(), "Referenced Capabilities", "com.ibm.team.workitem.workItemType.capability"));
        addResourceListField("referenced capabilities", "Referenced Capabilities", true);
        
        addField(REFERENCED_TASKS = new DmAlmField_ReferencedAlmElementsForProjectArea<>(getUrl(), "Referenced Tasks", "com.ibm.team.workitem.workItemType.task"));
        addResourceListField("referenced tasks", "Referenced Tasks", true);
    }

    @Override
    public String getStatus() {
        return (ARCHIVED.getValueAsText());
    }

    @Override
    public String getTitle() {
        return (TITLE.getValue());
    }

    @Override
    public String getId() {
        return (TITLE.getValue());
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + TITLE.getValueAsText());
    }
}
