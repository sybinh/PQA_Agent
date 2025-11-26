/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.PST.Fields;

import DataModel.DmElementI;
import DataModel.Ecv.Fields.DmEcvField_ReferenceList;
import DataModel.Ecv.PST.Records.DmEcvFc;
import DataModel.Ecv.PST.Records.DmEcvFcTable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvField_Reference_BC_FC extends DmEcvField_ReferenceList<DmEcvFc> {

    private EcvXmlContainerElement CONTAINER;

    public DmEcvField_Reference_BC_FC(DmElementI parent, EcvXmlContainerElement container, String nameForUserInterface) {
        super(parent, new ArrayList<DmEcvFc>(), nameForUserInterface);
        this.CONTAINER = container;
    }

    @Override
    public List<DmEcvFc> getElementList() {
        if (elements.isEmpty() && CONTAINER != null) {
            DmEcvFcTable fcTable = new DmEcvFcTable();
            fcTable.loadFromString(CONTAINER.getXmlString());
            for (DmEcvFc fc : fcTable.getElements()) {
                if (fc.FC_VERSION != null && !fc.FC_VERSION.getValueAsText().equals("null")) {
                    this.addElement(fc);
                }
            }
        }
        CONTAINER = null;
        return elements;
    }

    public DmEcvFc getFunctionByTitle(String name) {
        this.getElementList();
        Iterator<DmEcvFc> iter = this.elements.iterator();
        while(iter.hasNext()){
            DmEcvFc fc = iter.next();
            if (fc.FC_NAME.getValueAsText().equals(name)) {
                if (fc.FC_VERSION != null) {
                   return fc;
                }
            }
        }
        return null;
    }
    
    
    public DmEcvFc getFunctionByTitleAndAlternative(String name, String alternative) {
        this.getElementList();
        Iterator<DmEcvFc> iter = this.elements.iterator();
        while(iter.hasNext()){
            DmEcvFc fc = iter.next();
            if (fc.FC_NAME.getValueAsText().equals(name)) {
                if (fc.FC_VERSION != null && fc.FC_VERSION.getValue().startsWith(alternative)) {
                   return fc;
                }
            }
        }
        return null;
    }
}
