/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.Fields;

import DataModel.DmElement;
import DataModel.DmElementI;
import DataModel.DmElementListFieldI;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmEcvField_ReferenceList<T extends DmElement> extends DmEcvField implements DmElementListFieldI<T>{

    protected final List<T> elements;
    
    public DmEcvField_ReferenceList(DmElementI parent, List<T> element,  String nameForUserInterface) {
        super(parent, nameForUserInterface);
        assert(element != null); 
        this.elements = element;
    }

    @Override
    public List<T> getElementList() {
        return elements;
    }

    @Override
    public void addElement(T e) {
        this.elements.add(e);
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
