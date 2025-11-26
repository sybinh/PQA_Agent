/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT;

/**
 *
 * @author gug2wi
 */
public enum ProjektBewertung {

    NONE(),
    ANGEFORDERT("angefordert"),
    GEPLANT("geplant");

    final private String textInFile;

    ProjektBewertung() {
        this.textInFile = null;
    }

    ProjektBewertung(String textInFile) {
        assert (textInFile != null);
        assert (textInFile.isEmpty() == false);

        this.textInFile = textInFile;
    }

    public String getTextInFile() {
        return (textInFile);
    }

}
