/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import Rq1Cache.Types.Rq1XmlTable_SwMetricsMilestones.Milestone;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsTypes.Type;
import java.util.stream.Collectors;
import util.EcvMapMap;

/**
 *
 * @author gug2wi
 */
public class DmRq1SoftwareMetrics extends EcvMapMap<Milestone, Type, Integer> {

    @Override
    public String toString() {
        String s = getEntrySet().stream().map((Entry<Milestone, Type, Integer> t) -> {
            return (t.getKey1().toString() + " | " + t.getKey2().toString() + " | " + t.getValue().toString());
        }).collect(Collectors.joining("\n"));
        return (s);
    }

}
