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
public class DmDgsBcRelease implements DmDgsBcReleaseI {

    final private String type;
    final private String name;
    final private String version;

    public DmDgsBcRelease(String type, String name, String version) {
        assert (name != null);
        assert (type.isEmpty() == false);
        assert (type != null);
        assert (name.isEmpty() == false);
        assert (version != null);
        assert (version.isEmpty() == false);

        this.type = type;
        this.name = name;
        this.version = version;
    }

    @Override
    public String getType() {
        return type;
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
    public boolean equals(Object o) {
       return (BcReleaseComparator.equals(this, o));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public int compareTo(DmDgsBcReleaseI t) {
        return (BcReleaseComparator.compare(this, t));
    }

    @Override
    public String getTitle() {
        StringBuilder b = new StringBuilder();
        b.append(type).append(" : ").append(name).append(" / ").append(version);
        return (b.toString());
    }

    @Override
    public String toString() {
        return (type + " : " + name + " / " + version);
    }

}
