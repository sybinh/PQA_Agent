/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.List;
import util.SafeArrayList;

/**
 * This class holds a list of data with a special characteristic.
 * Each element, that is added, will also be added to another list reachable through getTempElementsToAdd().
 * Each element, that is removed, will be added to another list reachable through getTempElementsToDelete().
 * These separate lists can be cleared by calling clearTempLists().
 * 
 * Such a characteristic can be used for writeable lists in a database. When saving 
 * the list in the database, it is absolutely clear which elements has to added and deleted.
 * 
 * @author hfi5wi
 */
public class Rq1ReferenceList_Writeable<T> {
    
    private final SafeArrayList<T> elementList;
    private final SafeArrayList<T> tempElementsToAdd;
    private final SafeArrayList<T> tempElementsToDelete;
    
    public Rq1ReferenceList_Writeable() {
        elementList = new SafeArrayList<>();
        tempElementsToAdd = new SafeArrayList<>();
        tempElementsToDelete = new SafeArrayList<>();
    }
    
    public Rq1ReferenceList_Writeable(List<T> existingElements) {
        elementList = new SafeArrayList<>(existingElements);
        tempElementsToAdd = new SafeArrayList<>();
        tempElementsToDelete = new SafeArrayList<>();
    }
    
    /**
     * Add element to the elementList and to the temporary list.
     * 
     * @param newElement
     */
    public void addElement(T newElement) {
        assert (newElement != null);
        if (elementList.contains(newElement) == false) {
            elementList.add(newElement);
            if (tempElementsToAdd.contains(newElement) == false) {
                tempElementsToAdd.add(newElement);
            }
            tempElementsToDelete.remove(newElement);
        }
    }
    
//    public void addAll(Rq1ReferenceList_Writeable<T> newElements) {
//        newElements.getElementList().forEach((newElement) -> addElement(newElement));
//        newElements.getTempElementsToDelete().forEach((newElement) -> removeElement(newElement));
//    }
    
    public boolean removeElement(T element) {
        assert (element != null);
        if (elementList.remove(element) == true) {
            if (tempElementsToDelete.contains(element) == false) {
                tempElementsToDelete.add(element);
            }
            tempElementsToAdd.remove(element);
            return true;
        } else {
            return false;
        }
    }
    
//    public void clearTempLists() {
//        this.tempElementsToAdd.clear();
//        this.tempElementsToDelete.clear();
//    }
    
    public List<T> getElementList() {
        return (elementList.getImmutableList());
    }
    
    public List<T> getTempElementsToDelete() {
        return (tempElementsToDelete.getImmutableList());
    }
    
    public List<T> getTempElementsToAdd() {
        return (tempElementsToAdd.getImmutableList());
    }
}
