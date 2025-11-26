/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1HistoryLog;
import DataModel.UiSupport.DmUiTableSource;
import java.util.logging.Level;
import util.EcvDateTime;
import util.EcvTableColumn_DateTime;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlParser;

/**
 *
 * @author miw83wi
 */
public class DmRq1Table_HistoryLogsByAction extends EcvTableDescription implements DmUiTableSource {

    final private EcvTableColumn_DateTime TIME;
    final private EcvTableColumn_String FIELD;
    final private EcvTableColumn_String CHANGE;
    final private EcvTableColumn_String CHANGEBY;

    final DmRq1Field_ReferenceList<DmRq1HistoryLog> elementlist;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmRq1Table_HistoryLogsByAction.class.getCanonicalName());

    public DmRq1Table_HistoryLogsByAction(DmRq1Field_ReferenceList<DmRq1HistoryLog> elementlist) {
        assert (elementlist != null);
        this.elementlist = elementlist;
        addIpeColumn(TIME = new EcvTableColumn_DateTime("Time", 18));
        addIpeColumn(FIELD = new EcvTableColumn_String("Field", 6));
        addIpeColumn(CHANGE = new EcvTableColumn_String("Change", 15));
        addIpeColumn(CHANGEBY = new EcvTableColumn_String("Change by", 10));
        setDefaultSortColumn(TIME, false);
    }

    @Override
    public DmFieldI getDmField() {
        return elementlist;
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return this;
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData tableData = super.createTableData();
        for (DmRq1HistoryLog log : elementlist.getElementList()) {
            EcvDateTime time = log.LAST_MODIFIED_DATE.getValue();
            EcvXmlParser parser = new EcvXmlParser(log.HISTORY_LOG.getValue());
            try {
                EcvXmlElement root = parser.parse();
                if (root instanceof EcvXmlContainerElement) {
                    if (((EcvXmlContainerElement) root).hasElement("cq:FIELD")) {
                        for (EcvXmlElement element : ((EcvXmlContainerElement) root).getElementList("cq:FIELD")) {
                            EcvTableRow row = tableData.createRow();
                            String set = "";
                            try {
                                set = ((EcvXmlContainerElement) element).getElement("cq:SET").getXmlString();
                                set = set.substring(8, set.length() - 9);
                            } catch (EcvXmlElement.NotfoundException ex ) {
                                set = "";
                            }catch(StringIndexOutOfBoundsException e){
                                set = "";
                            }
                            row.setValueAt(FIELD, element.getAttribute("name"));
                            row.setValueAt(TIME, time);
                            row.setValueAt(CHANGE, set);
                            row.setValueAt(CHANGEBY, root.getAttribute("fullname") + " (" + root.getAttribute("login") + ")");
                            tableData.addRow(row);
                        }
                    }
                }
            } catch (EcvXmlParser.ParseException ex) {
                logger.log(Level.WARNING, "Couldn't parse HistoryLog for HistoryId = " + log.HISTORY_ID.getValue(), ex);
                logger.warning("=== HistoryLog:");
                logger.warning(log.HISTORY_LOG.getValue());
                logger.warning("===============");
            }
        }
        return (tableData);
    }

    @Override
    public void setValue(EcvTableData newData) {
    }

    @Override
    public boolean useLazyLoad() {
        return false;
    }

}
