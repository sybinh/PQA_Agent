/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Records;

import DataModel.Monitoring.DmRule;
import Monitoring.Hint;
import Monitoring.Info;
import Monitoring.Marker;
import Monitoring.ToDo;
import Monitoring.Warning;
import Rq1Cache.Monitoring.Rq1WriteFailure;
import java.util.Arrays;
import java.util.List;
import util.EcvXmlEmptyElement;

/**
 *
 * @author RHO2HC
 */
public class MarkerContent {
    
    public static final String MARKER = "Marker";
    public static final String ATTRIBUTE_TYPE = "type";
    public static final String ATTRIBUTE_TITLE = "title";
    public static final String ATTRIBUTE_DESCRIPTION = "description";
    public static final String ATTRIBUTE_TARGET = "target";
    
    public static final List<String> fieldNames = Arrays.asList(ATTRIBUTE_TYPE, ATTRIBUTE_TITLE, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_TARGET);
    
    private String type = "";
    private String target = "";
    private String title = "";
    private String description = "";
    private EcvXmlEmptyElement xmlObject;
    
    public MarkerContent() {
    }

    public MarkerContent(String type, String title) {
        this.type = type;
        this.title = title;
    }
    
    public MarkerContent(String type, String target, String title, String description) {
        this.type = type;
        this.target = target;
        this.title = title;
        this.description = description;
    }
    
    public MarkerContent(EcvXmlEmptyElement element) {
        this.type = element.getAttribute(ATTRIBUTE_TYPE);
        this.target = element.getAttribute(ATTRIBUTE_TARGET);
        this.title = element.getAttribute(ATTRIBUTE_TITLE);
        this.description = element.getAttribute(ATTRIBUTE_DESCRIPTION);
        this.xmlObject = element;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
    
    public Marker execute(DmRule rule) {
        Marker marker = null;
        this.description = this.description.isEmpty() ? "." : this.description;
        switch(this.type) {
            case "Info":
                marker = new Info(rule, this.title, this.description);
                break;
            case "Warning":
                marker = new Warning(rule, this.title, this.description);
                break;
            case "ToDo":
                marker = new ToDo(rule, this.title, this.description);
                break;
            case "Failure":
                marker = new Rq1WriteFailure(rule, this.title, this.description);
                break;
            case "Hint":
                marker = new Hint(rule, this.title, this.description);
                break;
        }
        return marker;
    }
    
    public EcvXmlEmptyElement convertToXmlObject(String ruleId) {
        EcvXmlEmptyElement result = new EcvXmlEmptyElement(MARKER);
        result.addAttribute(AbstractConfigurableRuleRecord.ATTRIBUTE_RULE_ID, ruleId);
        result.addAttribute(ATTRIBUTE_TYPE, type);
        result.addAttribute(ATTRIBUTE_TARGET, target);
        result.addAttribute(ATTRIBUTE_TITLE, title);
        result.addAttribute(ATTRIBUTE_DESCRIPTION, description);
        this.xmlObject = result;
        return result;
    }

    public EcvXmlEmptyElement getXmlObject() {
        return xmlObject;
    }

    public void setXmlObject(EcvXmlEmptyElement xmlObject) {
        this.xmlObject = xmlObject;
    }
}
