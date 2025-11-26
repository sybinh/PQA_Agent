/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import DataModel.Rq1.DmRq1LinkInterface;
import DataModel.Rq1.DmRq1NodeInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mos83wi
 */
public class WarningExport {

    final private List<MarkableI> markables;
    private MarkerExportFile exportFile;

    public WarningExport(DmRq1NodeInterface dmRq1Node, DmRq1LinkInterface dmRq1Link, List<? extends MarkableI> markables) {
        assert (dmRq1Node != null);
        assert (dmRq1Link != null);
        assert (markables != null);

        List<MarkableI> fullList = new ArrayList<>(markables);
        fullList.add(dmRq1Node);
        fullList.add(dmRq1Link);
        this.markables = Collections.unmodifiableList(fullList);

        this.exportFile = null;
    }

    public WarningExport(DmRq1NodeInterface dmRq1Node, List<? extends MarkableI> markables) {
        assert (dmRq1Node != null);
        assert (markables != null);

        List<MarkableI> fullList = new ArrayList<>(markables);
        fullList.add(dmRq1Node);
        this.markables = Collections.unmodifiableList(fullList);

        this.exportFile = null;
    }

    public WarningExport(DmRq1LinkInterface dmRq1Link, List<? extends MarkableI> markables) {
        assert (dmRq1Link != null);
        assert (markables != null);

        List<MarkableI> fullList = new ArrayList<>(markables);
        fullList.add(dmRq1Link);
        this.markables = Collections.unmodifiableList(fullList);

        this.exportFile = null;
    }

    public List<MarkableI> getMarkables() {
        return (markables);
    }

    public void setExportFile(MarkerExportFile exportFile) {
        this.exportFile = exportFile;
    }

    public MarkerExportFile getExportFile() {
        return this.exportFile;
    }

}
