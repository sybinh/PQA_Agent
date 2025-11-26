/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import UiSupport.EcvUserMessage;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gug2wi
 */
public class EcvTableColumn_Link extends EcvTableColumn_String {

    public EcvTableColumn_Link(String uiName) {
        super(uiName);
    }

    public EcvTableColumn_Link(String uiName, int columnWidth) {
        super(uiName, columnWidth);
    }

    @Override
    public boolean handleDoubleClick(Object cellValue) {
        if (cellValue instanceof String) {
            String trimedCellValue = ((String) cellValue).trim();
            try {
                Desktop.getDesktop().browse(new java.net.URI(trimedCellValue.replaceAll(" ", "%20")));
            } catch (IOException | URISyntaxException browseEx) {
                try {
                    Desktop.getDesktop().open(new java.io.File(trimedCellValue));
                } catch (IOException | IllegalArgumentException openEx) {
                    Logger.getLogger(EcvTableColumn_Link.class.getName()).log(Level.WARNING, null, browseEx);
                    Logger.getLogger(EcvTableColumn_Link.class.getName()).log(Level.WARNING, null, openEx);
                    EcvUserMessage.showMessageDialog("Failed to open " + cellValue + " in browser and explorer.", "Error", EcvUserMessage.MessageType.ERROR_MESSAGE);
                }
            }
        }
        return (true);
    }

}
