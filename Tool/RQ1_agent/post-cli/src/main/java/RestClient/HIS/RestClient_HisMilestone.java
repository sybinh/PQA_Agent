/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.HIS;

import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class RestClient_HisMilestone implements Comparable<RestClient_HisMilestone> {

    public enum Type {
        MILESTONE,
        QUALITY_GATE;
    }

    final private Type type;
    final private String ecuHardwareId;
    final private String targetEcuName;
    final private String milestoneName;
    final private EcvDate date;

    public RestClient_HisMilestone(Type type, String ecuHardwareId, String targetEcuName, String milestoneName, EcvDate date) {
        assert (type != null);
        assert (ecuHardwareId != null);
        assert (ecuHardwareId.isEmpty() == false);
        assert (targetEcuName != null);
        assert (milestoneName != null);
        assert (milestoneName.isEmpty() == false);
        assert (date != null);

        this.type = type;
        this.ecuHardwareId = ecuHardwareId;
        this.targetEcuName = targetEcuName;
        this.milestoneName = milestoneName;
        this.date = date;
    }

    public Type getType() {
        return type;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public EcvDate getDate() {
        return date;
    }

    public String getEcuHardwareId() {
        return ecuHardwareId;
    }

    public String getTargetEcuName() {
        return targetEcuName;
    }

    @Override
    public String toString() {
        return (type.name() + "-" + ecuHardwareId + "-" + targetEcuName + "-" + milestoneName + "-" + date.getXmlValue());
    }

    @Override
    public int compareTo(RestClient_HisMilestone otherMilestone) {

        int c = type.name().compareTo(otherMilestone.type.name());
        if (c == 0) {
            c = ecuHardwareId.compareTo(otherMilestone.ecuHardwareId);
            if (c == 0) {
                c = targetEcuName.compareTo(otherMilestone.targetEcuName);
                if (c == 0) {
                    c = milestoneName.compareTo(otherMilestone.milestoneName);
                }
            }
        }

        return (c);
    }

}
