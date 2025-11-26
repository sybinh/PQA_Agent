/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TimeLineData implements TimeLineDataI {

    final private List<TimeLineData_RowI> rows = new ArrayList<>();
    final private List<TimeLineData_SingleDayElementI> freeElements = new ArrayList<>();

    public TimeLineData() {
    }

    public void addRow(TimeLineData_RowI row) {
        assert (row != null);
        rows.add(row);
    }

    public void addRowSpanElement(TimeLineData_SingleDayElementI freeElement) {
        assert (freeElement != null);
        freeElements.add(freeElement);
    }

    @Override
    public List<TimeLineData_RowI> getRowData() {
        return (rows);
    }

    @Override
    public Collection<TimeLineData_SingleDayElementI> getRowSpanData() {
        return (freeElements);
    }

}
