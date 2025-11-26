/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmElementI;
import DataModel.DmFieldI;
import DataModel.Rq1.Records.DmRq1Project;
import java.util.List;

abstract class DmGpmFieldOnProject_TimeScheduleOfProject_SubList<T_MILESTONE extends DmGpmMilestone, T_SOURCEFIELD extends DmFieldI> {

    protected enum DependencyType {
        FIELD,
        PROJECT;
    }

    final private DmGpmFieldOnProject_TimeScheduleOfProject timeScheduleField;
    final protected DmRq1Project project;
    final protected T_SOURCEFIELD sourceField;

    private List<T_MILESTONE> content = null;
    private DependencyType dependency;
    private DmFieldI.ChangeListener fieldListener = null;
    private DmElementI.ChangeListener elementListener = null;

    @SuppressWarnings("LeakingThisInConstructor")
    DmGpmFieldOnProject_TimeScheduleOfProject_SubList(DmGpmFieldOnProject_TimeScheduleOfProject timeScheduleField, DmRq1Project project, T_SOURCEFIELD sourceField, DependencyType dependency) {
        assert (timeScheduleField != null);
        assert (project != null);
        assert (sourceField != null);
        assert (dependency != null);

        this.timeScheduleField = timeScheduleField;
        this.project = project;
        this.sourceField = sourceField;
        this.dependency = dependency;

    }

    private void setDependency() {
        if (dependency == DependencyType.FIELD) {
            fieldListener = (changedElement) -> {
                DmGpmFieldOnProject_TimeScheduleOfProject_SubList.this.changed();
            };
            sourceField.addChangeListener(fieldListener);
            dependency = null;
        } else if (dependency == DependencyType.PROJECT) {
            elementListener = (changedElement) -> {
                DmGpmFieldOnProject_TimeScheduleOfProject_SubList.this.changed();
            };
            project.addChangeListener(elementListener);
            dependency = null;
        }
    }

    final List<T_MILESTONE> getElementList() {
        if (content == null) {
            setDependency();
            content = loadElementList();
        }
        return (content);
    }

    abstract protected List<T_MILESTONE> loadElementList();

    void reload() {
        if (content != null) {
            content = null;
            reloadSourceField();
        }
    }

    protected void reloadSourceField() {
    }

    protected void changed() {
        if (content != null) {
            content = null;
            timeScheduleField.handleDependencyChange();
        }
    }

    protected void addToContent(T_MILESTONE milestone) {
        if (content != null) {
            content.add(milestone);
        }
    }
}
