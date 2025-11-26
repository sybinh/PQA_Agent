/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
/**
 * This package implements the data model for GPM (Guided Project Management).
 *
 *
 * For GPM, the handling of milestones on projects is enhanced in the following
 * way:
 *
 * 1) It is possible to use characters in milestone names which are not allowed
 * in names of XML tags.
 *
 * 2) It is possible to map workitems to milestones.
 *
 *
 *
 * ad 1) Allow all characters in milestone names
 *
 * Project milestones in RQ1 are stored in the milestone field of a project. For
 * RQ1, the name of the milestone is defined by the tag name. The date is stored
 * in the content of the tag.
 *
 * This format limits the allowed characters in names for milestones to that
 * characters allowed in XML tags. To overcome this limitation, IPE adds the
 * attribute 'name' to the XML tag. The attribute contains the true name of the
 * milestone while the tag name is derived from this name by removing all
 * invalid characters.
 *
 *
 *
 * ad 2) map workitems to milestones
 *
 * The mapping is stored in the tag field of the project. In the tags, the
 * milestone name is mapped to the RQ1-ID of the workitems.<br>
 * This kind of mappings has some consequences:
 *
 * 1: The milestones and mappings are store in the RQ1 project.<br>
 * 2: No information about the mapping is stored in the work items.<br>
 * 3: Work items might be moved to other projects without losing the
 * mapping.<br>
 * 4: The mapping has to be kept consistent by the logic in IPE.<br>
 *
 *
 *
 * Rq1Project
 *
 * The elements in Rq1Project manage the loading and saving of data in the
 * fields milestone and tag. On this level, the fields are independent from each
 * other.
 *
 * final private Rq1DatabaseField_Xml MILESTONE_FIELD; ... Access to field
 * 'milestones'.
 *
 * final public Rq1XmlSubField_Table<Rq1XmlTable_Milestones> MILESTONES; ...
 * Access to milestones in field 'milestones'.
 *
 * final private Rq1XmlSubField_Xml MILESTONES_IN_TAGS; ... Sub-Tag in tags
 * containing infos to the project milestones.
 *
 * final public Rq1XmlSubField_Table<Rq1XmlTable_TasksOnMilestones>
 * TASKS_ON_MILESTONES; ... List of mapping between milestone and work items
 * taken from the tag field.
 *
 *
 *
 * DmRq1Project
 *
 * The elements in DmRq1Project combine the data provided by the Rq1Project
 * elements. They generate the data model objects for milestones and list of
 * workitems on milestones.
 *
 * final public DmRq1Field_MilestoneTable MILESTONES_TABLE; ... Provides the
 * milestones from the milestone field in a table form.
 *
 * final public DmGpmField_MilestonesOnProject MILESTONES_ON_PROJECT; ...
 * Generates and manages all milestones on a project.<br>
 * a) Milestones from milestone field.<br>
 * b) Milestones for PVER and PVAR.<br>
 * c) Milestones from HIS<br>
 *
 * final public DmGpmField_TasksOnMilestones TASKS_ON_MILESTONES; .. manages the
 * tasks for milestones
 *
 * final public DmRq1Field_WorkitemsOnMilestone WORKITEMS_ON_MILESTONES;
 *
 *
 *
 */
package DataModel.GPM;
