/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import DataModel.DmElementI;
import DataModel.DmField;
import DataModel.DmMappedElementListFieldI;
import DataModel.Doors.Records.DmDoorsElement;
import DataModel.Doors.Records.DmDoorsFactory;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.DmMappedElement;
import Rq1Cache.Types.Rq1XmlTable_RequirementOnIRM;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_DoorsRequirementsOnIrmFromTables extends DmField implements DmMappedElementListFieldI<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI>, DmElementI.ChangeListener {

    final private DmRq1Irm rq1Irm;
    final private Map<DmRq1LinkToRequirement_Type, DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM>> fieldMap;

    private List<DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI>> content = null;

    public DmRq1Field_DoorsRequirementsOnIrmFromTables(DmRq1Irm parent, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (parent != null);

        rq1Irm = parent;
        fieldMap = new TreeMap<>();
    }

    public void addTableField(DmRq1LinkToRequirement_Type mapType, DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> field) {
        assert (mapType != null);
        assert (fieldMap.containsKey(mapType) == false) : "mapType = >" + mapType + "< already used.";
        assert (field != null);

        fieldMap.put(mapType, field);
    }

    @Override
    public boolean isReadOnly() {
        return (true);
    }

    @Override
    public synchronized List<DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI>> getElementList() {
        if (content == null) {
            content = new ArrayList<>();

            //------------------------------
            //
            // Extract URLs from all tables
            //
            //------------------------------
            Set<String> urlSet = getUrlToDoorsElements();

            //------------------------------
            //
            // Get records for URLs
            //
            //------------------------------
            Map<String, DmDoorsElement> elementMap = DmDoorsFactory.getElementsByUrls(urlSet);

            for (Map.Entry<DmRq1LinkToRequirement_Type, DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM>> fieldMapEntry : fieldMap.entrySet()) {
                DmRq1LinkToRequirement_Type linkType = fieldMapEntry.getKey();
                DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> tableField = fieldMapEntry.getValue();
                for (Rq1XmlTable_RequirementOnIRM.Record requirementRecord : Rq1XmlTable_RequirementOnIRM.extract(tableField.getValue())) {
                    DmDoorsElement doorsElement = null;
                    if (requirementRecord.getLink().isEmpty() == false) {
                        doorsElement = elementMap.get(requirementRecord.getLink());
                    } else if (requirementRecord.getRequirement().isEmpty() == false) {
                        doorsElement = DmDoorsFactory.getElementWithoutUrl(requirementRecord.getRequirement());
                    }
                    if (doorsElement != null) {
                        DmRq1LinkToDoors_OnIssueAndIrm doorsMap = new DmRq1LinkToDoors_OnIssueAndIrm(rq1Irm, linkType, requirementRecord.getRequirement(), requirementRecord.getComment(), requirementRecord.getLink(), doorsElement);
                        content.add(new DmMappedElement<>(doorsMap, doorsElement));
                    }
                }

            }

            rq1Irm.addChangeListener(this);
        }
        return (content);
    }

    /**
     * Extract URL for all referenced Doors Elements. Useful for load cache.
     *
     * @return
     */
    public synchronized Set<String> getUrlToDoorsElements() {
        Set<String> result = new TreeSet<>();
        for (Map.Entry<DmRq1LinkToRequirement_Type, DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM>> fieldMapEntry : fieldMap.entrySet()) {
            for (Rq1XmlTable_RequirementOnIRM.Record requirementRecord : Rq1XmlTable_RequirementOnIRM.extract(fieldMapEntry.getValue().getValue())) {
                if (requirementRecord.getLink().isEmpty() == false) {
                    result.add(requirementRecord.getLink());
                }
            }
        }
        return (result);
    }

    /**
     * Returns true, if the field does not contain at least one entry for a
     * requirement.
     *
     * Note that invalid or empty links are counted like normal links.
     *
     * The implementation is based on the data stored in the table fields. No
     * requirement is loaded from the database to calculate the result.
     *
     * @return True, if the list is empty.
     */
    public boolean isEmpty() {

        for (Map.Entry<DmRq1LinkToRequirement_Type, DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM>> fieldEntry : fieldMap.entrySet()) {
            DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> field = fieldEntry.getValue();
            List<Rq1XmlTable_RequirementOnIRM.Record> records = Rq1XmlTable_RequirementOnIRM.extract(field.getValue());
            if (records.isEmpty() == false) {
                return (false);
            }
        }
        return (true);
    }

    /**
     * Returns a list of link types as string to support the filtering and
     * grouping.
     *
     * The implementation is based on the data stored in the table fields. No
     * requirement is loaded from the database to calculate the result.
     *
     * Links with requirement 'no impact' are not added to the list.
     *
     * @return
     */
    public Set<String> getLinkTypesAsString() {

        Set<String> result = new TreeSet<>();

        for (Map.Entry<DmRq1LinkToRequirement_Type, DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM>> fieldEntry : fieldMap.entrySet()) {

            DmRq1LinkToRequirement_Type linkType = fieldEntry.getKey();
            DmRq1Field_Table<Rq1XmlTable_RequirementOnIRM> field = fieldEntry.getValue();

            List<Rq1XmlTable_RequirementOnIRM.Record> records = Rq1XmlTable_RequirementOnIRM.extract(field.getValue());

            if (records.isEmpty() == false) {
                if (linkType != DmRq1LinkToRequirement_Type.L2_PLANNED) {
                    result.add(fieldEntry.getKey().getLinkTypeName());
                } else {
                    for (Rq1XmlTable_RequirementOnIRM.Record record : records) {
                        if (record.getRequirement().toLowerCase().equals("no impact") == false) {
                            result.add(fieldEntry.getKey().getLinkTypeName());
                            break;
                        }
                    }
                }
            }
        }

        return (result);
    }

    @Override
    public void reload() {
        content = null;
        fireFieldChanged();
    }

    @Override
    public void changed(DmElementI changedElement) {
        reload();
    }

    @Override
    public boolean isLoaded() {
        return (content != null);
    }

    @Override
    public void addElement(DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI> e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
