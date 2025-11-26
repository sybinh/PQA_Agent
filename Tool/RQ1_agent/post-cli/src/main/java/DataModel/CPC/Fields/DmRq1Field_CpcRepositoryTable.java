/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.CPC.Fields;

import DataModel.DmElementI;
import DataModel.DmElementI.ChangeListener;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Types.Rq1XmlTable;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableColumnI;

/**
 *
 * @author hfi5wi
 * @param <T>
 * @param <T_DATA>
 */
public abstract class DmRq1Field_CpcRepositoryTable<T extends Rq1XmlTable, T_DATA> extends DmRq1Field_Table<T> implements ChangeListener {
    
    private final List<String> attributeList = new ArrayList<>();
    protected List<T_DATA> dataList = null;
    protected String descriptionForUserInterface = null;
    
    public DmRq1Field_CpcRepositoryTable(DmRq1Project project, Rq1XmlSubField_Table<T> cpcField, String nameForUserInterface) {
        super(cpcField, nameForUserInterface);
        cpcField.getTable().getColumns().forEach((column) -> {
            if (column.getVisibility() != EcvTableColumnI.Visibility.ALWAYS_HIDDEN) {
                attributeList.add(column.getUiName());
            }
        });
        project.addChangeListener(this);
    }
    
    public DmRq1Field_CpcRepositoryTable(DmRq1Project project, Rq1XmlSubField_Table<T> cpcField, String nameForUserInterface, String descriptionForUI) {
        this(project, cpcField, nameForUserInterface);
        descriptionForUserInterface = descriptionForUI;
    }
    
    public List<String> getAttributeList() {
        return (attributeList);
    }
    
    public int getAttributeListSize() {
        return (attributeList.size());
    }
    
    public abstract List<T_DATA> getDataList();
    
    public abstract void setValueAsList(List<T_DATA> dataList);
    
    @Override
    public void changed(DmElementI changedElement) {
        dataList = null;
    }
    
    public String getDescriptionForUI() {
        if(descriptionForUserInterface == null) {
            return "";
        }
        return descriptionForUserInterface;
    }
}
