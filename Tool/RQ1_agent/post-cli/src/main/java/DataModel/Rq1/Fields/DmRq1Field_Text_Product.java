/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1WorkItem;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author MIW83WI
 */
public class DmRq1Field_Text_Product extends DmRq1Field_Text {

    private final DmElementI workitem;

    public DmRq1Field_Text_Product(DmElementI workitem, Rq1FieldI_Text rq1TextField, String nameForUserInterface) {
        super(rq1TextField, nameForUserInterface);
        this.workitem = workitem;
    }

    public Collection<String> calculateProduct() {
        DmRq1WorkItem dmworkitem = (DmRq1WorkItem) workitem;
        DmRq1Project project = dmworkitem.PROJECT.getElement();
        Set<String> productvalues = new TreeSet<>();
        if (project != null) {
            productvalues.addAll(project.PRODUCTS.getValue());
        }
        if (dmworkitem != null) {
            productvalues.add(dmworkitem.PRODUCT.getValueAsText());
        }
        if (!productvalues.contains("")) {
            productvalues.add("");
        }
        return productvalues;
    }

}
