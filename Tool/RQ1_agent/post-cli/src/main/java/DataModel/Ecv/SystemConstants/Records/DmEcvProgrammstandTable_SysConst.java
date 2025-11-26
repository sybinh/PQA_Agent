/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.SystemConstants.Records;

import DataModel.Xml.DmXmlTable;
import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvProgrammstandTable_SysConst extends DmXmlTable<DmEcvProgrammstand_SysConst> {

    @Override
    protected DmEcvProgrammstand_SysConst createElement(EcvXmlContainerElement elem) {
        return new DmEcvProgrammstand_SysConst("Dm Ecv Programmstand SysConst", elem);
    }
}
