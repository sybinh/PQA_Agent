/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.ALM.Records.DmAlmElementFactory;
import DataModel.ALM.Records.DmAlmElementI;
import DataModel.DmElementListFieldI;
import DataModel.DmField;
import java.util.List;
import util.SafeArrayList;

/**
 *
 * @author CNI83WI
 * @param <T>
 */
public class DmAlmField_ReferencedAlmElementsForProjectArea<T extends DmAlmElementI> extends DmField implements DmElementListFieldI<T> {

    private SafeArrayList<T> content = null;
    final private String workitemUrl;

    public DmAlmField_ReferencedAlmElementsForProjectArea(String projectAreaUrl, String nameForUserInterface) {
        super(nameForUserInterface);
        this.workitemUrl = projectAreaUrl.replace("process/project-areas", "oslc/contexts") + "/workitems?oslc.select=*";
    }

    public DmAlmField_ReferencedAlmElementsForProjectArea(String projectAreaUrl, String nameForUserInterface, String type) {
        super(nameForUserInterface);
        this.workitemUrl = projectAreaUrl.replace("process/project-areas", "oslc/contexts") + "/workitems?oslc.select=*&oslc.where=rtc_cm:type=\"" + type + "\"";
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getElementList() {
        if (content == null) {
            SafeArrayList<T> newContent = new SafeArrayList<>();
            List<T> dmElementList = (List<T>) DmAlmElementFactory.getQueryElementByUrl(this.workitemUrl); //DmAlmProjectArea as param

            if (dmElementList != null) {
                newContent.addAll(dmElementList);
            }
            content = newContent;
        }
        return (content.getImmutableList());
    }

    @Override
    public void reload() {
        content = null;
        fireFieldChanged();
    }

    @Override
    public void addElement(T element) {
        getElementList();
        content.add(element);
        
        fireFieldChanged();
        
    }
    
}
