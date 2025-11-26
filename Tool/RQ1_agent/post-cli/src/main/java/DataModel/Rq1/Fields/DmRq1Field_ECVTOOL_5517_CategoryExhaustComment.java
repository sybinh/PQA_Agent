/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmToDsField;
import DataModel.DmValueFieldI_Text;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Text;

/**
 * Field for work around introduced with ECVTOOL-5517.
 *
 * @author GUG2WI
 */
public class DmRq1Field_ECVTOOL_5517_CategoryExhaustComment extends DmToDsField<String> implements DmValueFieldI_Text {

    final private Rq1DatabaseField_Text rq1Field;
    final private Rq1XmlSubField_Text tagField;

    public DmRq1Field_ECVTOOL_5517_CategoryExhaustComment(Rq1DatabaseField_Text rq1Field, Rq1XmlSubField_Text tagField, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);

        assert (rq1Field != null);
        assert (tagField != null);

        this.rq1Field = rq1Field;
        this.tagField = tagField;
    }

    @Override
    public String getValue() {
        String value = rq1Field.getDataModelValue();
        if ((value == null) || (value.isEmpty())) {
            value = tagField.getDataModelValue();
        }
        return (value);
    }

    @Override
    public void setValue(String value) {
        rq1Field.setDataModelValue(value);
        tagField.setDataModelValue(value);
    }

}
