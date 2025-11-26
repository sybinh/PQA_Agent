/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.List;

/**
 *
 * @author gug2wi
 */
public interface EcvXmlContainerI {

    List<EcvXmlElement> getElementList();

    List<EcvXmlElement> getElementList(String name);

    boolean isEmpty();

    void addElement(EcvXmlElement element);

    void removeElement(EcvXmlElement element);

}
