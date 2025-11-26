/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Doors;

import DataModel.DmElementI;
import DataModel.DmField;
import DataModel.DmMappedElementListFieldI;
import DataModel.Doors.Records.DmDoorsElement;
import DataModel.Doors.Records.DmDoorsFactory;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Types.DmMappedElement;
import Rq1Cache.Types.Rq1XmlTable_Requirement;
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
public class DmRq1Field_RequirementsFromTables extends DmField implements DmMappedElementListFieldI<DmRq1IssueToDoorsLink, DmDoorsElement>, DmElementI.ChangeListener {

    final private DmRq1Issue rq1Issue;
    final private Map<DmRq1ToDoorsLinkType, DmRq1Field_Table<Rq1XmlTable_Requirement>> fieldMap;

    private List<DmMappedElement<DmRq1IssueToDoorsLink, DmDoorsElement>> content = null;

    public DmRq1Field_RequirementsFromTables(DmRq1Issue parent, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (parent != null);

        rq1Issue = parent;
        fieldMap = new TreeMap<>();
    }

    public void addTableField(DmRq1ToDoorsLinkType mapType, DmRq1Field_Table<Rq1XmlTable_Requirement> field) {
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
    public synchronized List<DmMappedElement<DmRq1IssueToDoorsLink, DmDoorsElement>> getElementList() {
        if (content == null) {
            content = new ArrayList<>();

            //------------------------------
            //
            // Extract URLs from all tables
            //
            //------------------------------
            Set<String> urlSet = new TreeSet<>();
            for (Map.Entry<DmRq1ToDoorsLinkType, DmRq1Field_Table<Rq1XmlTable_Requirement>> fieldMapEntry : fieldMap.entrySet()) {
                for (Rq1XmlTable_Requirement.Record requirementRecord : Rq1XmlTable_Requirement.extract(fieldMapEntry.getValue().getValue())) {
                    if (requirementRecord.getLink().isEmpty() == false) {
                        urlSet.add(requirementRecord.getLink());
                    }
                }
            }

            //------------------------------
            //
            // Get records for URLs
            //
            //------------------------------
            if (urlSet.isEmpty() == false) {

                Map<String, DmDoorsElement> elementMap = DmDoorsFactory.getElementsByUrls(urlSet);

                for (Map.Entry<DmRq1ToDoorsLinkType, DmRq1Field_Table<Rq1XmlTable_Requirement>> fieldMapEntry : fieldMap.entrySet()) {
                    DmRq1ToDoorsLinkType linkType = fieldMapEntry.getKey();
                    DmRq1Field_Table<Rq1XmlTable_Requirement> tableField = fieldMapEntry.getValue();
                    for (Rq1XmlTable_Requirement.Record requirementRecord : Rq1XmlTable_Requirement.extract(tableField.getValue())) {
                        DmDoorsElement doorsElement = null;
                        if (requirementRecord.getLink().isEmpty() == false) {
                            doorsElement = elementMap.get(requirementRecord.getLink());
                        } else if (requirementRecord.getRequirement().isEmpty() == false) {
                            doorsElement = DmDoorsFactory.getElementWithoutUrl(requirementRecord.getRequirement());
                        }
                        if (doorsElement != null) {
                            DmRq1IssueToDoorsLink doorsMap = new DmRq1IssueToDoorsLink(rq1Issue, linkType, requirementRecord.getRequirement(), requirementRecord.getLink(), doorsElement);
                            content.add(new DmMappedElement<>(doorsMap, doorsElement));
                        }
                    }

                }
            }

            rq1Issue.addChangeListener(this);
        }
        return (content);
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

        for (Map.Entry<DmRq1ToDoorsLinkType, DmRq1Field_Table<Rq1XmlTable_Requirement>> fieldEntry : fieldMap.entrySet()) {
            DmRq1Field_Table<Rq1XmlTable_Requirement> field = fieldEntry.getValue();
            List<Rq1XmlTable_Requirement.Record> records = Rq1XmlTable_Requirement.extract(field.getValue());
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

        for (Map.Entry<DmRq1ToDoorsLinkType, DmRq1Field_Table<Rq1XmlTable_Requirement>> fieldEntry : fieldMap.entrySet()) {

            DmRq1ToDoorsLinkType linkType = fieldEntry.getKey();
            DmRq1Field_Table<Rq1XmlTable_Requirement> field = fieldEntry.getValue();

            List<Rq1XmlTable_Requirement.Record> records = Rq1XmlTable_Requirement.extract(field.getValue());

            if (records.isEmpty() == false) {
                if (linkType != DmRq1ToDoorsLinkType.L2_PLANNED) {
                    result.add(fieldEntry.getKey().getLinkTypeName());
                } else {
                    for (Rq1XmlTable_Requirement.Record record : records) {
                        if (record.getRequirement().toLowerCase().equals("no impact") == false) {
                            result.add(fieldEntry.getKey().getLinkTypeName());
                            break;
                        }
                    }
                }
                continue;
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
    public void addElement(DmMappedElement<DmRq1IssueToDoorsLink, DmDoorsElement> e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
