/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.ALM.Records.DmAlmProjectArea;
import DataModel.DmElementListField_ReadOnlyI;
import DataModel.DmField;
import UiSupport.UiTreeViewRootListI;
import java.util.ArrayList;
import java.util.List;
import util.SafeArrayList;

/**
 *
 * @author CNI83WI
 */
public class DmAlmProjectAreaList extends DmField implements DmElementListField_ReadOnlyI<DmAlmProjectArea>, UiTreeViewRootListI {

    List<DmAlmProjectArea> almElements = new ArrayList<>();

    private SafeArrayList<DmAlmProjectArea> list = null;

    public DmAlmProjectAreaList(String nameForUserInterface) {
        super(nameForUserInterface);
    }

    public void addElement(DmAlmProjectArea element) {
        this.almElements.add(element);
    }

    @Override
    public List<DmAlmProjectArea> getElementList() {
        return almElements;
    }

    @Override
    public void reload() {

    }

    @Override
    public String getViewTitle() {
        return "CCM";
    }
    
    @Override
    public String toString(){
        List<String> projectAreaNames = new ArrayList<>();
        for(DmAlmProjectArea projectArea : this.almElements) {
            projectAreaNames.add(projectArea.getId());
        }
        return projectAreaNames.toString();
        
    }

}
