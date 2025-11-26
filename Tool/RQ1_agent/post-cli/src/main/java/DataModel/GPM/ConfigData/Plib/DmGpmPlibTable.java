/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData.Plib;

import TablePlus.Csv.CsvLoadException;
import TablePlus.Csv.CsvTable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author GUG2WI
 */
public abstract class DmGpmPlibTable<T_ID, T_ROW extends DmGpmPlibRow<T_ID>> extends CsvTable<T_ROW> {

    final private Map<T_ID, T_ROW> idMap = new TreeMap<>();

    @Override
    public boolean load(InputStream iStream, Encoding encoding, FieldSeparator separator, boolean hasHeaderLine) throws IOException, CsvLoadException {
        super.load(iStream, encoding, separator, hasHeaderLine);
        for (var row : this.getRows()) {
            this.idMap.put(row.getId(), row);
        }
        return (true);
    }

    public T_ROW getRowById(T_ID id) {
        var row = idMap.get(id);
        if (row == null) {
            throw new Error("Invalid id: " + id);
        }
        return (row);
    }

    public List<T_ROW> getRowsById(Collection<T_ID> ids) {
        var result = new ArrayList<T_ROW>();
        for (var id : ids) {
            var row = idMap.get(id);
            if (row == null) {
                throw new Error("Invalid id: " + id);
            }
            result.add(row);
        }
        return (result);
    }

}
