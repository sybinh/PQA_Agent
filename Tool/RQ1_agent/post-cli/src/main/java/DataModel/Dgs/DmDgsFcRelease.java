/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Dgs;

import java.util.Objects;

/**
 *
 * @author gug2wi
 */
public class DmDgsFcRelease implements DmDgsFcReleaseI {

    final private String name;
    final private String version;

    public DmDgsFcRelease(String name, String version) {
        assert (name != null);
        assert (name.isEmpty() == false);
        assert (version != null);
        assert (version.isEmpty() == false);

        this.name = name;
        this.version = version;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public int compareTo(DmDgsFcReleaseI other) {
        return (FcInterface.compare(this, other));
    }

    @Override
    public boolean equals(Object other) {
        return (FcInterface.equals(this, other));
    }

    @Override
    public String getTitle() {
        StringBuilder b = new StringBuilder();
        b.append("FC : ").append(name).append(" / ").append(version);
        return (b.toString());
    }

    @Override
    public String toString() {
        return ("FC : " + name + " / " + version);
    }

    @Override
    public String getVariant() {
        return (FcInterface.getVariant(getVersion()));
    }

}
