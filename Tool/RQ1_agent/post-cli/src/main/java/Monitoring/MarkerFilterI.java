/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import java.util.Collection;

/**
 * Supports the filtering of markers
 *
 * @author gug2wi
 */
public interface MarkerFilterI {

    /**
     * Creates a new list that contains only those markers that match the
     * filter.
     *
     * @param markers List of markers that shall be filtered.
     * @return
     */
    Collection<Marker> filterCollection(Collection<Marker> markers);

}
