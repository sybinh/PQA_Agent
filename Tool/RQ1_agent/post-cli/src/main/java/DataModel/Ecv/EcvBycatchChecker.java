/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv;

import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1Fc;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.DmMappedElement;
import Monitoring.Info;
import Rq1Data.Enumerations.IntegrationAction;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Enumerations.SoftwareIssueCategory;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
public class EcvBycatchChecker {

    public enum BycatchType {
        NO_BYCATCH, // green
        SET_TO_NO_BYCATCH, // green and bold
        PROJECT_BYCATCH, // orange and bold
        SUPPLIER_BYCATCH; // red and bold

        static BycatchType getHigher(BycatchType b1, BycatchType b2) {
            if (b2.ordinal() > b1.ordinal()) {
                return (b2);
            } else {
                return (b1);
            }
        }
    }

    static public BycatchType getBcBycatchType(DmRq1Pst pst, DmRq1Bc bc) {
        assert (pst != null);
        assert (bc != null);

        BycatchType bc_result = BycatchType.NO_BYCATCH;
        boolean is_anything_mapped = false;

        List<DmMappedElement<DmRq1Rrm, DmRq1Release>> mapped_fc = bc.MAPPED_CHILDREN.getElementList();
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> map : mapped_fc) {
            if ((map.getMap().isCanceled() == false) && (map.getMap().INTEGRATION_ACTION.getValue() != IntegrationAction.REMOVE)) {
                is_anything_mapped = true;
                DmRq1Fc fc = (DmRq1Fc) map.getTarget();
                BycatchType i_fd_result = getFcBycatchType(pst, fc);
                bc_result = BycatchType.getHigher(bc_result, i_fd_result);
            }
        }

        List<DmMappedElement<DmRq1Irm, DmRq1Issue>> mapped_i_fd = bc.MAPPED_ISSUES.getElementList();
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> map : mapped_i_fd) {
            if (map.getMap().isCanceled() == false) {
                is_anything_mapped = true;
                DmRq1IssueFD i_fd = (DmRq1IssueFD) map.getTarget();
                BycatchType i_fd_result = getIfdBycatchType(pst, i_fd);
                bc_result = BycatchType.getHigher(bc_result, i_fd_result);
            }
        }

        if (is_anything_mapped == false) {
            return (BycatchType.SUPPLIER_BYCATCH);
        } else {
            return (bc_result);
        }
    }

    static public BycatchType getFcBycatchType(DmRq1Pst pst, DmRq1Fc fc) {
        assert (pst != null);
        assert (fc != null);

        List<DmMappedElement<DmRq1Irm, DmRq1Issue>> mapped_i_fd = fc.MAPPED_ISSUES.getElementList();

        boolean is_i_fd_mapped = false;
        BycatchType fc_result = BycatchType.NO_BYCATCH;

        for (DmMappedElement<DmRq1Irm, DmRq1Issue> map : mapped_i_fd) {
            if (map.getMap().isCanceled() == false) {
                is_i_fd_mapped = true;
                DmRq1IssueFD i_fd = (DmRq1IssueFD) map.getTarget();
                BycatchType i_fd_result = getIfdBycatchType(pst, i_fd);
                fc_result = BycatchType.getHigher(fc_result, i_fd_result);
            }
        }

        if (is_i_fd_mapped == false) {
            return (BycatchType.SUPPLIER_BYCATCH);
        } else {
            return (fc_result);
        }
    }

    static public BycatchType getIfdBycatchType(DmRq1Pst pst, DmRq1IssueFD i_fd) {

        if (isSetToNoByCatch(i_fd) == true) {
            return (BycatchType.SET_TO_NO_BYCATCH);
        }

        if (isVisibleIssue(pst, i_fd) == false) {
            return (BycatchType.SUPPLIER_BYCATCH);
        }

        if (isSpecialIssueFD(i_fd) == false) {
            return (BycatchType.SUPPLIER_BYCATCH);
        }

        if (isIssueMappedToPst(pst, i_fd) == false) {
            return (BycatchType.PROJECT_BYCATCH);
        }

        return (BycatchType.NO_BYCATCH);
    }

    static boolean isSetToNoByCatch(DmRq1IssueFD i_fd) {
        assert (i_fd != null);

        if (i_fd.PARENT.getElement() instanceof DmRq1IssueSW == false) {
            return (false);
        }
        DmRq1IssueSW i_sw = (DmRq1IssueSW) i_fd.PARENT.getElement();

        for (Info info : i_sw.getInfos()) {
            if (info.getTitle().trim().toLowerCase().startsWith("no-ecvbycatch") == true) {
                return (true);
            }
        }

        return (false);
    }

    static boolean isVisibleIssue(DmRq1Pst pst, DmRq1IssueFD i_fd) {
        assert (pst != null);
        assert (i_fd != null);

        if (i_fd.PARENT.getElement() instanceof DmRq1IssueSW == false) {
            return (false);
        }
        DmRq1IssueSW i_sw = (DmRq1IssueSW) i_fd.PARENT.getElement();

        if (i_sw.SCOPE.getValue() != Scope.EXTERNAL) {
            return (false);
        }

        //
        // Check if I-SW is in the project or pool project, if so, then the I-FD is visible
        //
        DmRq1Project projectOfPst = pst.PROJECT.getElement();
        if (projectOfPst != null) {
            if (i_sw.PROJECT.isElementEqual(projectOfPst)) {
                return (true);
            }

            DmRq1Project poolProjectOfPst = projectOfPst.POOL_PROJECT.getElement();
            if ((poolProjectOfPst != null) && (i_sw.PROJECT.isElementEqual(poolProjectOfPst))) {
                return (true);
            }
        }

        return (false);
    }

    static boolean isIssueMappedToPst(DmRq1Pst pst, DmRq1IssueFD i_fd) {
        assert (pst != null);
        assert (i_fd != null);

        if (i_fd.PARENT.getElement() instanceof DmRq1IssueSW == false) {
            return (false);
        }
        DmRq1IssueSW i_sw_on_i_fd = (DmRq1IssueSW) i_fd.PARENT.getElement();

        for (DmMappedElement<DmRq1Irm, DmRq1Issue> i_sw_map : pst.MAPPED_ISSUES.getElementList()) {
            if (i_sw_map.getMap().isCanceled() == false) {
                if (i_sw_map.getTarget() instanceof DmRq1IssueSW) {
                    DmRq1IssueSW i_sw_on_pst = (DmRq1IssueSW) i_sw_map.getTarget();
                    if (i_sw_on_i_fd == i_sw_on_pst) {
                        return (true);
                    }
                }
            }
        }

        return (false);
    }

    static boolean isSpecialIssueFD(DmRq1IssueFD i_fd) {
        assert (i_fd != null);

        DmRq1IssueSW i_sw = (DmRq1IssueSW) i_fd.PARENT.getElement();
        if (i_sw == null) {
            return (false);
        }

        if (isForLastenheft(i_sw) == true) {
            return (true);
        }
        if (isKonzernanforderung(i_sw) == true) {
            return (true);
        }
        if (isDefect(i_sw) == true) {
            return (true);
        }
        if (isMisraWarning(i_sw) == true) {
            return (true);
        }
        return (false);
    }

    static boolean isForLastenheft(DmRq1IssueSW i_sw) {
        assert (i_sw != null);
        // 'Lastenheft-Forderung'.  „LH##“ in the title.
        return (i_sw.TITLE.getValueAsText().contains("LH##"));
    }

    static boolean isKonzernanforderung(DmRq1IssueSW i_sw) {
        assert (i_sw != null);

        String externalID = i_sw.EXTERNAL_ID.getValueAsText();
        if (externalID.length() == 5) {
            String title = i_sw.TITLE.getValueAsText();
            return (title.endsWith("#" + externalID));
        }
        return (false);
    }

    static boolean isDefect(DmRq1IssueSW i_sw) {
        assert (i_sw != null);
        // Category „Defect“.
        return (i_sw.CATEGORY.getValue() == SoftwareIssueCategory.DEFECT);
    }

    static boolean isMisraWarning(DmRq1IssueSW i_sw) {
        assert (i_sw != null);
        // MISRA warning. „MISRA Warning Removal“ in the title.
        return (i_sw.TITLE.getValueAsText().toLowerCase().contains("misra"));
    }
}
