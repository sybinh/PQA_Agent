/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Pool;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Cache.Types.Rq1XmlTable_Enumeration;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import util.EcvEnumeration;
import util.EcvEnumerationSet;
import util.EcvEnumerationValue;
import util.EcvTableData;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_IssueSwSubCategory extends DmRq1Field_TextAsEnumerationSet {

    final private DmRq1Field_Reference<DmRq1Project> projectField;
    private boolean initialLoadDone = false;

    public DmRq1Field_IssueSwSubCategory(DmElementI parent, Rq1FieldI_Text rq1Field, DmRq1Field_Reference<DmRq1Project> projectField, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);
        assert (projectField != null);

        this.projectField = projectField;
    }

    @Override
    public List<EcvEnumeration> getValidInputValues() {

        if (initialLoadDone) {
            //
            // Note: The list is always new created, because the values in the field and in the project might be changed since last creation.
            //
            SortedSet<EcvEnumeration> result;

            DmRq1SwCustomerProject_Pool poolProject = getPoolProject();

            //
            // Add values from project definition
            //
            if (poolProject != null) {
                EcvTableData data = poolProject.ISSUE_SW_SUB_CATEGORIES.getValue();
                Rq1XmlTable_Enumeration table = poolProject.ISSUE_SW_SUB_CATEGORIES.getTableDescription();
                result = table.createEnumerationSet(data);
            } else {
                result = new TreeSet<>();
            }

            //
            // Add values from field, if one is not yet in the set.
            //
            EcvEnumerationSet currentValue = getValue();
            if (currentValue != null) {
                for (EcvEnumeration valueEntry : currentValue) {
                    for (EcvEnumeration resultEntry : result) {
                        if (resultEntry.getText().equals(valueEntry.getText()) == true) {
                            valueEntry = null;
                            break;
                        }
                    }
                    if (valueEntry != null) {
                        result.add(EcvEnumerationValue.createFromList(valueEntry.getText(), result));
                    }
                }
            }
            return (new ArrayList<>(result));
        }

        return (null);
    }

    @Override
    public void loadValidInputValues() {
        getPoolProject();
        initialLoadDone = true;
    }

    private DmRq1SwCustomerProject_Pool getPoolProject() {

        DmRq1Project project = projectField.getElement();
        if (project != null) {
            if (project instanceof DmRq1SwCustomerProject_Pool) {
                return ((DmRq1SwCustomerProject_Pool) project);
            } else if (project.POOL_PROJECT.getElement() instanceof DmRq1SwCustomerProject_Pool) {
                return ((DmRq1SwCustomerProject_Pool) project.POOL_PROJECT.getElement());
            }
        }
        return (null);
    }

}
