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
import DataModel.DmFieldI;
import DataModel.DmMappedElementListFieldI;
import DataModel.Doors.Records.DmDoorsElement;
import DataModel.Doors.Records.DmDoorsFactory;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.DmMappedElement;
import DataModel.UiSupport.DmUiTableSource;
import Rq1Cache.Types.Rq1XmlTable_RequirementOnIssue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import util.EcvMapList;
import util.EcvTableColumn_ComboBox;
import util.EcvTableColumn_Link;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_DoorsRequirementsOnIssueFromTables extends DmField implements DmMappedElementListFieldI<DmRq1LinkToDoors_OnIssueAndIrm, DmElementI>, DmUiTableSource, DmElementI.ChangeListener {

    final private DmRq1Element parentElement;
    final private Map<DmRq1LinkToRequirement_Type, DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue>> fieldMap;

    private List<DmMappedElement<DmRq1LinkToDoors_OnIssueAndIrm, DmElementI>> content = null;

    public DmRq1Field_DoorsRequirementsOnIssueFromTables(DmRq1Element parentElement, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (parentElement != null);

        this.parentElement = parentElement;
        fieldMap = new TreeMap<>();
    }

    public void addTableField(DmRq1LinkToRequirement_Type mapType, DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> field) {
        assert (mapType != null);
        assert (fieldMap.containsKey(mapType) == false) : "mapType = >" + mapType + "< already used.";
        assert (field != null);

        fieldMap.put(mapType, field);
    }

    @Override
    public boolean isReadOnly() {
        return (false);
    }

    @Override
    public synchronized List<DmMappedElement<DmRq1LinkToDoors_OnIssueAndIrm, DmElementI>> getElementList() {
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

            for (Map.Entry<DmRq1LinkToRequirement_Type, DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue>> fieldMapEntry : fieldMap.entrySet()) {
                DmRq1LinkToRequirement_Type linkType = fieldMapEntry.getKey();
                DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> tableField = fieldMapEntry.getValue();
                for (Rq1XmlTable_RequirementOnIssue.Record requirementRecord : Rq1XmlTable_RequirementOnIssue.extract(tableField.getValue())) {
                    DmDoorsElement doorsElement = null;
                    if (requirementRecord.getLink().isEmpty() == false) {
                        doorsElement = elementMap.get(requirementRecord.getLink());
                    } else if (requirementRecord.getRequirement().isEmpty() == false) {
                        doorsElement = DmDoorsFactory.getElementWithoutUrl(requirementRecord.getRequirement());
                    }
                    if (doorsElement != null) {
                        DmRq1LinkToDoors_OnIssueAndIrm doorsMap = new DmRq1LinkToDoors_OnIssueAndIrm(parentElement, linkType, requirementRecord.getRequirement(), requirementRecord.getLink(), doorsElement);
                        content.add(new DmMappedElement<>(doorsMap, doorsElement));
                    }
                }

            }

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
        for (Map.Entry<DmRq1LinkToRequirement_Type, DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue>> fieldMapEntry : fieldMap.entrySet()) {
            for (Rq1XmlTable_RequirementOnIssue.Record requirementRecord : Rq1XmlTable_RequirementOnIssue.extract(fieldMapEntry.getValue().getValue())) {
                if (requirementRecord.getLink().isEmpty() == false) {
                    result.add(requirementRecord.getLink());
                }
            }
        }
        return (result);
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

        for (Map.Entry<DmRq1LinkToRequirement_Type, DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue>> fieldEntry : fieldMap.entrySet()) {

            DmRq1LinkToRequirement_Type linkType = fieldEntry.getKey();
            DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> field = fieldEntry.getValue();

            List<Rq1XmlTable_RequirementOnIssue.Record> records = Rq1XmlTable_RequirementOnIssue.extract(field.getValue());

            if (records.isEmpty() == false) {
                if (linkType != DmRq1LinkToRequirement_Type.L2_PLANNED) {
                    result.add(fieldEntry.getKey().getLinkTypeName());
                } else {
                    for (Rq1XmlTable_RequirementOnIssue.Record record : records) {
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
    public void addElement(DmMappedElement<DmRq1LinkToDoors_OnIssueAndIrm, DmElementI> e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //--------------------------------------------------------------------------
    //
    // Implement table view
    //
    //--------------------------------------------------------------------------
    static final public class TableDescription extends EcvTableDescription {

        final public EcvTableColumn_ComboBox LINK_TYPE;
        final public EcvTableColumn_String REQUIREMENT;
        final public EcvTableColumn_String TYPE;
        final public EcvTableColumn_String BASELINE;
        final public EcvTableColumn_Link LINK;

        public TableDescription() {
            addIpeColumn(LINK_TYPE = new EcvTableColumn_ComboBox("Link Type", 20, DmRq1LinkToRequirement_Type.getDoorsTypes()));
            addIpeColumn(REQUIREMENT = new EcvTableColumn_String("Requirement", 30));
            addIpeColumn(TYPE = new EcvTableColumn_String("Type", 15));
            addIpeColumn(BASELINE = new EcvTableColumn_String("Baseline", 10));
            addIpeColumn(LINK = new EcvTableColumn_Link("Link to DOORS (double click to open)", 40));
        }

    }

    static public final TableDescription TABLE_DESCRIPTION = new TableDescription();

    @Override
    public DmFieldI getDmField() {
        return (this);
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return (TABLE_DESCRIPTION);
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData value = TABLE_DESCRIPTION.createTableData();

        for (Map.Entry<DmRq1LinkToRequirement_Type, DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue>> fieldMapEntry : fieldMap.entrySet()) {
            DmRq1LinkToRequirement_Type linkType = fieldMapEntry.getKey();
            DmRq1Field_Table<Rq1XmlTable_RequirementOnIssue> tableField = fieldMapEntry.getValue();

            for (Rq1XmlTable_RequirementOnIssue.Record requirementRecord : Rq1XmlTable_RequirementOnIssue.extract(tableField.getValue())) {
                EcvTableRow row = value.createAndAddRow();
                TABLE_DESCRIPTION.LINK_TYPE.setValue(row, linkType);
                TABLE_DESCRIPTION.REQUIREMENT.setValue(row, requirementRecord.getRequirement());
                TABLE_DESCRIPTION.BASELINE.setValue(row, requirementRecord.getBaseline());
                TABLE_DESCRIPTION.TYPE.setValue(row, requirementRecord.getType());
                TABLE_DESCRIPTION.LINK.setValue(row, requirementRecord.getLink());
            }
        }

        return (value);
    }

    @Override
    public void setValue(EcvTableData newCombinedData) {
        assert (newCombinedData != null);
        assert (newCombinedData.getDescription() == TABLE_DESCRIPTION);

        EcvMapList<DmRq1LinkToRequirement_Type, Rq1XmlTable_RequirementOnIssue.Record> newRecordMapList = new EcvMapList<>(fieldMap.keySet());

        for (EcvTableRow combinedRow : newCombinedData.getRows()) {
            DmRq1LinkToRequirement_Type linkType = DmRq1LinkToRequirement_Type.getType(TABLE_DESCRIPTION.LINK_TYPE.getValue(combinedRow));
            String requirement = TABLE_DESCRIPTION.REQUIREMENT.getValue(combinedRow);

            if ((linkType != null) && (requirement != null) && (requirement.isEmpty() == false)) {
                String type = TABLE_DESCRIPTION.TYPE.getValue(combinedRow);
                String baseline = TABLE_DESCRIPTION.BASELINE.getValue(combinedRow);
                String link = TABLE_DESCRIPTION.LINK.getValue(combinedRow);
                newRecordMapList.add(linkType, new Rq1XmlTable_RequirementOnIssue.Record(requirement, type, baseline, link));
            }
        }

        for (DmRq1LinkToRequirement_Type linkType : fieldMap.keySet()) {
            EcvTableData newSpecificData = Rq1XmlTable_RequirementOnIssue.pack(newRecordMapList.get(linkType));
            fieldMap.get(linkType).setValue(newSpecificData);
        }

    }

    @Override
    public boolean useLazyLoad() {
        return (false);
    }

}
