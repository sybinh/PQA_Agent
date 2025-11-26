/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Fields;

import java.util.ArrayList;
import java.util.List;
import util.EcvEnumeration;

/**
 *
 * @author RHO2HC
 */
public class FieldNameObject {
    
    private String name = "";
    private String nameForUserInterface = "";
    private String type = "";
    private List<String> operators = new ArrayList<>();
    private EcvEnumeration[] rangeForEnumObject; 

    public FieldNameObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String nameInDatabase) {
        this.name = nameInDatabase;
    }

    public String getNameForUserInterface() {
        return nameForUserInterface;
    }

    public void setNameForUserInterface(String nameForUserInterface) {
        this.nameForUserInterface = nameForUserInterface;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getOperators() {
        return operators;
    }

    public void setOperators(List<String> operators) {
        this.operators = operators;
    }

    public EcvEnumeration[] getRangeForEnumObject() {
        return rangeForEnumObject;
    }

    public void setRangeForEnumObject(EcvEnumeration[] rangeForEnumObject) {
        this.rangeForEnumObject = rangeForEnumObject;
    }
}
