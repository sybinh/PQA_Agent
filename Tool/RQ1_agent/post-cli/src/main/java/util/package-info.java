/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 *
 * Structure of a program using EcvTool.jar
 *
 *
 * 1) Set the application data via EcvApplication.setApplicationData(...)
 *
 * The toolName and the toolVersionForAccessControl has to be in the white list
 * of RQ1. toolVersionForLogging is only used for logging. applicationType
 * defines the handling of errors.
 *
 * 2) Set the login data via EcvApplication.setLoginData()
 *
 * 3) Load any object from RQ1 as a base for the further work.
 * DmRq1Element.getElementById().
 *
 * Then add the rest of the code.
 *
 * Example: Get all I-SW on a PVER
 *
 * DmRq1PverRelease pver = DmRq1Element.getElementById(... id of PVER ...);
 *
 * for (DmMappedElement<DmRq1Irm, DmRq1Issue> map :
 * pver.MAPPED_ISSUES.getElementList()) {
 *
 * DmRq1IssueSW i_sw = (DmRq1IssueSW) map.getTarget();
 *
 * ... do what ever is needed with the i_sw ...
 *
 * }
 *
 *
 * Example: Create new I-SW in a project.
 *
 * DmRq1Project targetProject = (DmRq1Project)DmRq1Element.getElementById(... id
 * of Projekt * ...);
 *
 * DmRq1IssueSW i_sw = DmRq1IssueSW.create(targetProject);
 *
 * i_sw.TITLE.setValue("Title");
 *
 * i_sw.DESCRIPTION.setValue("Description");
 *
 * i_sw.CATEGORY.setValue(SoftwareIssueCategory.REQUIREMENT);
 *
 * i_sw.save();
 *
 *
 *
 *
 * Example: Change data in an existing record.
 *
 * DmRq1IssueSW i_sw = (DmRq1IssueSW)DmRq1Element.getElementById(... id of Issue
 * ...);
 *
 * String oldComment = i_sw.INTERNAL_COMMENT-getValue();
 *
 * String newComment = oldComment + "\nNew Line in the comment";
 *
 * i_sw.INTERNAL_COMMENT.setValue( newComment );
 *
 * i_sw.save();
 *
 * Note: You can change any field in the record. save() will always save all
 * change fields.
 *
 */
