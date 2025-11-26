/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Eis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author hfi5wi
 */
public class DmEisWorkitemTable extends DefaultTableModel {
    
    // Separators that are used for transformation between table and string
    public static final String WORKITEMTABLE_ROWELEMENT_SEPARATOR = ",";
    public static final String WORKITEMTABLE_ROW_SEPARATOR = ";";
    
    // Table header names
    private static final String HEADERNAME_WI_PREFIX_TITLE = "Wi Prefix Title";
    private static final String HEADERNAME_WI_TYPE = "Wi Type";
    private static final String HEADERNAME_ASSIGNEE = "Assignee";
    private static final String HEADERNAME_ORDER = "Order";
    
    private final String[] wiTableColumnNames = {
        HEADERNAME_WI_PREFIX_TITLE, HEADERNAME_WI_TYPE, HEADERNAME_ASSIGNEE, HEADERNAME_ORDER
    };
    
    private TableRowSorter<DefaultTableModel> sorter;
    
    public DmEisWorkitemTable(String tableDataAsString) {
        List<DmEisWorkitemTableRow> wiTableDataList = extractFromString(tableDataAsString);
        String[][] wiTableDataArray = new String[][]{};
        int N = 0;
        
        for(DmEisWorkitemTableRow wi : wiTableDataList) {
            wiTableDataArray = Arrays.copyOf(wiTableDataArray, N + 1);
            wiTableDataArray[N] = wi.toStringArray();
            N = N + 1;
        }
        
        this.setDataVector(wiTableDataArray, wiTableColumnNames);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(this);
        sorter.setComparator(this.getWiOrderColumnNumber(), (String t, String t1) -> {
            int ans;
            //If a Pre/Suc is empty ( "" ), override it with a high number
            if(t.isEmpty() && !t1.isEmpty()) {
                t = String.valueOf(Long.MAX_VALUE);
            } else if(t1.isEmpty() && !t.isEmpty()) {
                t1 = String.valueOf(Long.MAX_VALUE);
            }
            ans = t1.compareTo(t);
            return ans;
        });
    }
    
    /**
     * Add new row after the position pos. If pos = -1 or pos > table row number 
     * then the new row will be at the end.
     * @param pos Position where to add new Row
     */
    public void addRowWithData(int pos) {
        addRow(new String[wiTableColumnNames.length]);
        setValueAt("New Wi", getRowCount()-1, 0);
        setValueAt(DmEisWorkitemTable_WiType_Enumeration.EVALUATE, getRowCount()-1, 1);
        setValueAt(getRowCount(), getRowCount()-1, 3);  
        if(pos != -1 && pos <= getRowCount()) {
            moveRow(getRowCount()-1, getRowCount()-1, pos+1);
        }
    }
    
    public static List<DmEisWorkitemTableRow> extractFromString(String customizerString){
        String[] wiElements;
        String[] wiTableRowsArray;
        List<DmEisWorkitemTableRow> wiTableRowsList = new ArrayList<>();
        
        if(!customizerString.isEmpty()) {
            if(customizerString.contains(WORKITEMTABLE_ROW_SEPARATOR)) {
                wiTableRowsArray = customizerString.split(WORKITEMTABLE_ROW_SEPARATOR);
                // for each table row
                for (String wiTableRow : wiTableRowsArray) {
                    wiElements = wiTableRow.split(WORKITEMTABLE_ROWELEMENT_SEPARATOR, -1);
                    switch(wiElements.length) {
                        case 1: wiTableRowsList.add(new DmEisWorkitemTableRow(wiElements[0], DmEisWorkitemTable_WiType_Enumeration.EVALUATE, "", ""));
                                break;
                        case 2: wiTableRowsList.add(new DmEisWorkitemTableRow(wiElements[0], DmEisWorkitemTable_WiType_Enumeration.getWiType(wiElements[1]), "", ""));
                                break;
                        case 3: wiTableRowsList.add(new DmEisWorkitemTableRow(wiElements[0], DmEisWorkitemTable_WiType_Enumeration.getWiType(wiElements[1]), wiElements[2], ""));
                                break;
                        case 4: wiTableRowsList.add(new DmEisWorkitemTableRow(wiElements[0], DmEisWorkitemTable_WiType_Enumeration.getWiType(wiElements[1]), wiElements[2], wiElements[3]));
                                break;
                    }
                }
            }
        }
        return wiTableRowsList;
    }
    
    /**
     * Convert table to string.Go row-wise through table cells, each cell text is separated by a COMMA ",",
     * each row is separated by a SEMICOLON ";"
     *
     * @param wiTableRows 
     * @return
     */
    public static String packIntoString(List<DmEisWorkitemTableRow> wiTableRows){
        String tableToString = "";

        for(DmEisWorkitemTableRow wiTableRow : wiTableRows){
            if(wiTableRow.getPrefixTitle().isEmpty() || wiTableRow.getType().equals("") ) {
                //Empty wi type is invalid, so break the line
                break;
            }
            tableToString = tableToString + 
                wiTableRow.toString(WORKITEMTABLE_ROWELEMENT_SEPARATOR) + 
                WORKITEMTABLE_ROW_SEPARATOR;
        }
        
        return tableToString;
    }

    public int getPrefxTitleColumnNumber() {
        return Arrays.asList(wiTableColumnNames).indexOf(HEADERNAME_WI_PREFIX_TITLE);
    }
    
    public int getWiTypeColumnNumber() {
        return Arrays.asList(wiTableColumnNames).indexOf(HEADERNAME_WI_TYPE);
    }
    
    public int getWiAssigneeColumnNumber() {
        return Arrays.asList(wiTableColumnNames).indexOf(HEADERNAME_ASSIGNEE);
    }
    
    public int getWiOrderColumnNumber() {
        return Arrays.asList(wiTableColumnNames).indexOf(HEADERNAME_ORDER);
    }
    
    public TableRowSorter<DefaultTableModel> getSorter() {
        return this.sorter;
    }
}
