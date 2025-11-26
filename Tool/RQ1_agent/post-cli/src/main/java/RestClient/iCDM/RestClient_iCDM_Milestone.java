/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.iCDM;

/**
 *
 * @author GUG2WI
 */
public class RestClient_iCDM_Milestone {

    final private String pidcVerID;
    final private String pidcVersionName;
    final private RestClient_iCDM_MilestoneType milestoneType;
    final private int id;
    final private String name;
    final private String description;
    final private String value;

    RestClient_iCDM_Milestone(String pidcVerID, String pidcVersionName, RestClient_iCDM_MilestoneType milestoneType, int id, String name, String description, String value) {
        assert (pidcVersionName != null);
        assert (pidcVerID != null);
        assert (pidcVerID.isEmpty() == false);
        assert (milestoneType != null);
        assert (name != null);
        assert (name.isEmpty() == false);

        this.pidcVerID = pidcVerID;
        this.pidcVersionName = pidcVersionName;
        this.milestoneType = milestoneType;
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getPidcVerID() {
        return pidcVerID;
    }

    public String getPidcVersionName() {
        return pidcVersionName;
    }

    public int getId() {
        return (id);
    }

    public String getName() {
        return (name);
    }

    public String getDate() {
        return (value);
    }

    public String getDescription() {
        return (description);
    }

    @Override
    public String toString() {
        return ("iCDM-Milestone," + getPidcVerID() + "," + getId() + "," + name + "," + value);
    }

}
