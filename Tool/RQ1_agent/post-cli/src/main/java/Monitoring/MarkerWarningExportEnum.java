/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

/**
 *
 * @author mos83wi
 */
public enum MarkerWarningExportEnum {

    MARKER_TYPE("MarkerType") {

        @Override
        String extractValue(Marker marker) {
            if (marker != null) {
                return (marker.getType());
            } else {
                return "";
            }
        }
    },
    MARKER_TITLE("MarkerTitle") {

        @Override
        String extractValue(Marker marker) {
            if (marker != null) {
                return marker.getTitle();
            } else {
                return "";
            }
        }
    },
    MARKER_DETAILS("MarkerDetails") {

        @Override
        String extractValue(Marker marker) {
            if (marker != null) {
                return marker.getDescription();
            } else {
                return "";
            }
        }
    },
    RULE_NAME("RuleName") {

        @Override
        String extractValue(Marker marker) {
            if (marker != null) {
                return marker.getRule().getRuleTitle();
            } else {
                return "";
            }
        }
    },
    RULE_DESCRIPTION("RuleDescription") {

        @Override
        String extractValue(Marker marker) {
            if (marker != null) {
                return marker.getRule().getRuleDescriptionText();
            } else {
                return "";
            }
        }
    },
    I_SW("I-SW") {

        @Override
        String extractValue(Marker marker) {
            String iSW = "";
            for (AffectedObject affected : marker.getAffectedElements()) {
                if (affected.getResponsible() != null && affected.getResponsible().isEmpty() == false) {
                    if ("I-SW".equals(affected.getElementType())) {
                        iSW = affected.getId();
                    }
                }
            }
            return iSW;
        }
    },
    RESPONSIBLE("Responsible") {

        @Override
        String extractValue(Marker marker) {
            boolean isFirstAffected = true;
            String responsible = "";
            for (AffectedObject affected : marker.getAffectedElements()) {
                if (affected.getElementType() != null && affected.getElementType().isEmpty() == false) {
                    if (affected.getResponsible() != null && affected.getResponsible().isEmpty() == false) {
                        if (isFirstAffected == true) {
                            responsible = affected.getResponsible();
                            isFirstAffected = false;
                        }
                    }
                }
            }
            return responsible;
        }
    },
    I_FD("I-FD") {

        @Override
        String extractValue(Marker marker) {
            String iFD = "";
            for (AffectedObject affected : marker.getAffectedElements()) {
                if (affected.getElementType() != null && affected.getElementType().isEmpty() == false) {
                    if (affected.getResponsible() != null && affected.getResponsible().isEmpty() == false) {
                        if (affected.getElementType().equals("I-FD")) {
                            iFD = affected.getId();
                        }
                    }
                }
            }
            return iFD;
        }
    },
    IRM_PST_ISSUE_SW("IRM-PST-ISSUE_SW") {

        @Override
        String extractValue(Marker marker) {
            String irm = "";
            for (AffectedObject affected : marker.getAffectedElements()) {
                if (affected.getElementType() != null && affected.getElementType().isEmpty() == false) {
                    if (affected.getResponsible() != null && affected.getResponsible().isEmpty() == false) {
                        if (affected.getElementType().equals("IRM-PST-ISSUE_SW")) {
                            irm = affected.getId();
                        }
                    }
                }
            }
            return irm;
        }
    },
    BC("BC") {

        @Override
        String extractValue(Marker marker) {
            String bc = "";
            for (AffectedObject affected : marker.getAffectedElements()) {
                if (affected.getElementType() != null && affected.getElementType().isEmpty() == false) {
                    if (affected.getResponsible() != null && affected.getResponsible().isEmpty() == false) {
                        if (affected.getElementType().equals("BC")) {
                            bc = affected.getId();
                        }
                    }
                }
            }
            return bc;
        }
    };

    final private String value;
    private boolean activated;

    MarkerWarningExportEnum(String value) {
        assert (value != null);
        assert (value.isEmpty() == false);

        this.value = value;
        this.activated = true;
    }

    public String getValue() {
        return this.value;
    }

    public void setActivated(boolean newActivated) {
        this.activated = newActivated;
    }

    public boolean getActivated() {
        return this.activated;
    }

    abstract String extractValue(Marker marker);
}
