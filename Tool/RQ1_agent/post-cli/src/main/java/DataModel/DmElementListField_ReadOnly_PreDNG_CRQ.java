/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataModel.Rq1.Records.DmRq1ElementInterface;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author GEA83WI
 */
public class DmElementListField_ReadOnly_PreDNG_CRQ extends DmElementListField_ReadOnlyFromSource<DmRq1ElementInterface> {

    private final DmValueFieldI_Text textField;
    public List<DmRq1ElementInterface> elementList = new ArrayList<DmRq1ElementInterface>();

    public DmElementListField_ReadOnly_PreDNG_CRQ(DmValueFieldI_Text textField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (textField != null);

        this.textField = textField;
    }

    @Override
    protected Collection<DmRq1ElementInterface> loadElementList() {
        Collection<String> preDNGCRQElements = extractIdOfReferencedRq1Elements(textField.getValue());
        for (String id : preDNGCRQElements){
            elementList.add(DataModel.Rq1.Records.DmRq1Element.getElementById(id));
        }
        return elementList;
    }
    
    static Collection<String> extractIdOfReferencedRq1Elements(String value) {

        Pattern numberPattern = Pattern.compile("RQONE[0-9]{8}");
        Set<String> result = new TreeSet<>();

        if (value != null) {
            String[] lines = value.split("\n");
            for (String line : lines) {
                Matcher matcher = numberPattern.matcher(line);
                while (matcher.find() == true) {
                    result.add(matcher.group());
                }
            }
        }
        return (result);
    }
}
