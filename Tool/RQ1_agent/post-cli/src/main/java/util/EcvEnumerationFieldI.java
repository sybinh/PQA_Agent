/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 *
 * @author gug2wi
 */
public interface EcvEnumerationFieldI {

    public EcvEnumeration[] getValidInputValues();

    public EcvEnumeration getValue();

    public void setValue(EcvEnumeration v);
}
