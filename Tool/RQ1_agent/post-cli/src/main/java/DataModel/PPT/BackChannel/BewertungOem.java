/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.BackChannel;

public enum BewertungOem {

    EMPTY(""),
    UNKNOWN(""), // Returned for unknown text.
    GEDULDET("Geduldet"),
    ABGELEHNT("Abgelehnt"),
    GEFORDERT("Gefordert"),
    GEFORDERT_ABER_NICHT_ALS_SPLIT("Gefordert, aber nicht als Split"),
    IMPLIZIT_GEFORDERT("Implizit gefordert");

    final private String textInFile;

    BewertungOem(String textInFile) {
        this.textInFile = textInFile;
    }

    public String getTextInFile() {
        return (textInFile);
    }

    static public BewertungOem get(String bewertung) {
        if ((bewertung != null) && (bewertung.isEmpty() == false)) {
            for (BewertungOem value : values()) {
                if (bewertung.equals(value.textInFile) == true) {
                    return (value);
                }
            }
            return (UNKNOWN);
        }
        return (EMPTY);
    }

    public int compareTo(String bewertung) {
        BewertungOem c = get(bewertung);
        if (c == null) {
            return (-1);
        }
        return (compareTo(c));
    }

}
