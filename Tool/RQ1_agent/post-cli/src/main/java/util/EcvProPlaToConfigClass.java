/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public final class EcvProPlaToConfigClass {
    public static final List<String> BC_NAMES_WHICH_SHOULD_NOT_BE_SENT = new LinkedList<>(Arrays.asList("Conf-Component", "CEL", "MAK", "GONF", "BC_BCC", "BC_LIB", "PATCH"));
}
