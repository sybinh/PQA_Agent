/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmElementI.ChangeListener;
import DataModel.DmField;
import DataModel.DmValueFieldI;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1SoftwareProject;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Cache.Monitoring.Rq1DataRule;
import Rq1Cache.Monitoring.Rq1ParseFieldException;
import Rq1Cache.Monitoring.Rq1UnexpectedDataFailure;
import Rq1Data.Monitoring.Rq1RuleDescription;
import Rq1Data.Types.Rq1LineEcuProject;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlParser;
import util.EcvXmlTextElement;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_ExternalDescription_PstLines extends DmField implements DmValueFieldI<List<Rq1LineEcuProject>>, ChangeListener {

    @EcvElementList("Rq1Data.Monitoring.Rq1RuleDescription")
    static final public Rq1RuleDescription invalidXmlStringDescription = new Rq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.RQ1_DATA),
            "Valid format for XML field.",
            "The content of a field in the RQ1 database, which should contain a XML value, does not contain a valid XML value.\n"
            + "\n"
            + "The warning 'Unexpected data read from RQ1 database.' is set on the element that contains the field. "
            + "The field name and a problem text is added to the description of the warning.");

    private Rq1DataRule invalidXmlStringRule = null;

    final private DmRq1SoftwareProject project;
    final private Rq1FieldI_Text rq1Field;
    boolean valueValid = false;
    private List<Rq1LineEcuProject> value = null;

    @SuppressWarnings("LeakingThisInConstructor")
    public DmRq1Field_ExternalDescription_PstLines(DmRq1SoftwareProject parent, Rq1FieldI_Text rq1Field, String nameForUserInterface) {
        super(nameForUserInterface);

        this.project = parent;
        this.rq1Field = rq1Field;

        parent.addChangeListener(this);
    }

    @Override
    public boolean isReadOnly() {
        if (DmRq1Project.Customer.getCustomer(project) == DmRq1Project.Customer.VW_GROUP) {
            return (false);
        } else {
            return (true);
        }
    }

    @Override
    public synchronized List<Rq1LineEcuProject> getValue() {
        if (valueValid == false) {
            if (isReadOnly() == false) {
                removeMarker();
                try {
                    value = toList(rq1Field.getDataModelValue());
                } catch (EcvXmlParser.ParseException ex) {
                    setMarker(ex);
                    value = new ArrayList<>();
                } catch (java.lang.ClassCastException ex) {
                    setMarker(ex);
                    value = new ArrayList<>();
                }
            } else {
                value = new ArrayList<>();
            }
            valueValid = true;
        }
        assert (value != null) : project.getRq1Id();
        return (value);
    }

    @Override
    public synchronized void setValue(List<Rq1LineEcuProject> value) {
        assert (value != null);
        this.value = value;
        this.valueValid = true;
        rq1Field.setDataModelValue(toXml(value));
        removeMarker();
    }

    @Override
    public void changed(DmElementI changedElement) {
        valueValid = false;
        super.fireFieldChanged();
    }

    private void removeMarker() {
        if (invalidXmlStringRule != null) {
            project.removeMarkers(invalidXmlStringRule);
        }
    }

    private void setMarker(Exception ex) {
        StringBuilder s = new StringBuilder(50);
        s.append("Problem processing data from field ").append(rq1Field.getFieldName()).append(".\n");
        Rq1ParseFieldException containerEx = new Rq1ParseFieldException(s.toString(), ex);
        if (invalidXmlStringRule == null) {
            invalidXmlStringRule = new Rq1DataRule(invalidXmlStringDescription);
        }
        project.setMarker(new Rq1UnexpectedDataFailure(invalidXmlStringRule, project.getRq1Record(), rq1Field, containerEx));
    }

    /**
     * Made static public to simplify testing.
     *
     * @param xmlString
     * @return
     * @throws util.EcvXmlElement.ParseException
     */
    static public List<Rq1LineEcuProject> toList(String xmlString) throws EcvXmlParser.ParseException {
        List<Rq1LineEcuProject> result = new ArrayList<>();
        if ((xmlString != null) && (xmlString.isEmpty() == false)) {
            EcvXmlParser parser = new EcvXmlParser("<List>" + xmlString + "</List>");
            EcvXmlContainerElement xml = (EcvXmlContainerElement) parser.parse();
            for (EcvXmlElement element : xml.getElementList("MAPINT2EXT")) {
                String internalName = element.getAttribute("SI");
                String externalName = ((EcvXmlTextElement) element).getText();
                result.add(new Rq1LineEcuProject(internalName, externalName));
            }
        }
        return (result);
    }

    /**
     * Made static public to simplify testing.
     *
     * @param list
     * @return
     */
    static public String toXml(List<Rq1LineEcuProject> list) {

        if ((list != null) && (list.isEmpty() == false)) {
            StringBuilder b = new StringBuilder();
            for (Rq1LineEcuProject line : list) {
                EcvXmlElement element = new EcvXmlTextElement("MAPINT2EXT", line.getExternalName()).addAttribute("SI", line.getInternalName());
                if (b.length() > 0) {
                    b.append("\n");
                }
                b.append(element.getXmlString());
            }
            return (b.toString());
        } else {
            return (null);
        }

    }

}
