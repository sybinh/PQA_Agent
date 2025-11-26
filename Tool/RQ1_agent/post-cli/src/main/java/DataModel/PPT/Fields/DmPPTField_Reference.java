/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.DmElement;
import DataModel.DmElementFieldI;

/**
 * 
 * @author moe83wi
 * @param <T> 
 */
public class DmPPTField_Reference<T extends DmElement> extends DmPPTField implements DmElementFieldI<T>{

    private T element;
    
    public DmPPTField_Reference(DmElement parent, T element, String nameForUserInterface) {
        super(parent, nameForUserInterface);
        this.element = element;
    }
    
    @Override
    public T getElement() {
        return element;
    }

    @Override
    public void setElement(T v) {
        this.element = v;
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeElement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString(){
        return( getNameForUserInterface() + ":" + ((element != null) ? element.getId() : "null"));
    }
    
}
