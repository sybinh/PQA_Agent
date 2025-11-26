/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import java.util.List;

/**
 *
 * @author mos83wi
 */
public class EcvExportWarningsService {

    public static void writeToFile(List<WarningExport> warningList, MarkerExportFile exportFile) {
        for (WarningExport warning : warningList) {
            for (MarkableI markable : warning.getMarkables()) {
                List<Marker> markerList = markable.getMarkers();
                for (Marker mark : markerList) {
                    if (mark instanceof Warning) {
                        exportFile.addMark(mark, null);
                    }
                }
            }
        }
        exportFile.finishFile();
    }
}
