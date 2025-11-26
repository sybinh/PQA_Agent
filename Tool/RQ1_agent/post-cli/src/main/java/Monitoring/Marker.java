/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Holds th information of a marker.
 *
 * @author gug2wi
 */
public abstract class Marker implements MarkerI {

    final private RuleI rule;
    final private String title;
    private String description;
    final private List<AffectedObject> affectedElements;
    private boolean hideAtExport;

    /**
     * Creates a marker for a rule with title and description.
     *
     * @param rule SwitchableRuleI to which the marker belongs.
     * @param title Title of the marker.
     * @param description Description for the marker.
     */
    protected Marker(RuleI rule, String title, String description) {
        assert (rule != null);
        assert (title != null);
        assert (title.isEmpty() == false);
        assert (description != null);
        assert (description.isEmpty() == false);
        this.rule = rule;
        this.title = title;
        this.description = description;
        this.affectedElements = new ArrayList<>(1);
        this.hideAtExport = false;
    }

    /**
     * Creates a marker for a rule with title.
     *
     * @param rule SwitchableRuleI to which the marker belongs.
     * @param title Title of the marker.
     */
    protected Marker(RuleI rule, String title) {
        assert (rule != null);
        assert (title != null);
        assert (title.isEmpty() == false);
        this.rule = rule;
        this.title = title;
        this.description = "";
        this.affectedElements = new ArrayList<>(1);
        this.hideAtExport = true;
    }

    /**
     * Sets the description to
     *
     * @param description
     */
    final protected void setDescription(String description) {
        assert (description != null);
        assert (description.isEmpty() == false);
        this.description = description;
    }

    final public Marker addAffectedElement(AffectedObject element) {
        affectedElements.add(element);
        return (this);
    }

    final public Marker hideAtExport() {
        hideAtExport = true;
        return (this);
    }

    abstract public String getType();

    @Override
    final public String getTitle() {
        return (title);
    }

    final public String getTypeAndTitle() {
        return (getType() + ": " + getTitle());
    }

    final public String getDescription() {
        return (description);
    }

    final public RuleI getRule() {
        return (rule);
    }

    final public List<AffectedObject> getAffectedElements() {
        return (affectedElements);
    }

    final boolean markerForExport() {
        return (!hideAtExport);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.rule);
        hash = 19 * hash + Objects.hashCode(this.title);
        hash = 19 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return (true);
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Marker other = (Marker) obj;
        if (!Objects.equals(this.rule, other.rule)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (getType() + ": " + title);
    }

}
