/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import java.util.List;

/**
 * Interface for all Software Pool Projects
 *
 * @author miw83wi
 */
public interface DmRq1SwPoolProjectI {

    List<DmRq1Pst> getOpenPstOnMemberProjects();

    List<DmRq1Pst> getAllPstOnMemberProjects();
}
