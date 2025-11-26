/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmElementField_ReadOnlyFromSource;
import DataModel.DmElementField_ReadOnlyI;
import DataModel.DmElementI;
import DataModel.DmValueField_Enumeration;
import DataModel.Rq1.Records.DmRq1Release;
import Rq1Data.Enumerations.ExportCategoryDelivery;
import Rq1Data.Enumerations.ExportScope;
import util.EcvEnumeration;
import util.EcvEnumerationFieldI;

/**
 * A milestone object representing a delivery in a project.
 *
 * @author GUG2WI
 * @param <DM_RELEASE>
 */
public abstract class DmGpmMilestone_FromRelease<DM_RELEASE extends DmRq1Release> extends DmGpmMilestone_FromReleaseRecord<DM_RELEASE> {

    final public DmElementField_ReadOnlyI<DM_RELEASE> RELEASE;

    protected DmGpmMilestone_FromRelease(DM_RELEASE release, Type type, String elementType) {
        super(release, type, elementType, release.PROJECT.getElement());

        addField(RELEASE = new DmElementField_ReadOnlyFromSource<DM_RELEASE>(type.getText()) {
            @Override
            public DM_RELEASE getElement() {
                return (release);
            }
        });

        addField(EXPORT_SCOPE = new DmValueField_Enumeration("Export Scope", release.EXPORT_SCOPE.getValue(), ExportScope.values()) {
            @Override
            public void setValue(EcvEnumeration newValue) {
                super.setValue(newValue);
            }
        });

        addField(EXPORT_CATEGORY = new DmValueField_Enumeration("Export Category", ExportCategoryDelivery.DELIVERY, ExportCategoryDelivery.values()));
    }

    @Override
    public boolean save() {

        boolean changed = false;

        if (release.TITLE.getValueAsText().equals(NAME.getValueAsText()) == false) {
            release.TITLE.setValue(NAME.getValueAsText());
            changed = true;
        }
        if (release.PLANNED_DATE.getDateNotNull().equals(DATE.getDateNotNull()) == false) {
            release.PLANNED_DATE.setValue(DATE.getDate());
            changed = true;
        }
        if (release.EXPORT_SCOPE.getValue().equals(EXPORT_SCOPE.getValue()) == false) {
            release.EXPORT_SCOPE.setValue(EXPORT_SCOPE.getValue());
            changed = true;
        }

        if (changed == true) {
            return (release.save());
        }
        return (false);
    }

    @Override
    public void changed(DmElementI changedRecord) {
        if (release != null) {
            if (release.TITLE.getValueAsText().equals(NAME.getValueAsText()) == false) {
                NAME.setValue(release.TITLE.getValueAsText());
            }
            if (release.PLANNED_DATE.getDateNotNull().equals(DATE.getDateNotNull()) == false) {
                DATE.setValue(release.PLANNED_DATE.getDate());
            }
            if (release.EXPORT_SCOPE.getValue().equals(EXPORT_SCOPE.getValue()) == false) {
                ((EcvEnumerationFieldI) EXPORT_SCOPE).setValue(release.EXPORT_SCOPE.getValue());
            }
            fireChange();
        }
    }

    public abstract String getClassification();

}
