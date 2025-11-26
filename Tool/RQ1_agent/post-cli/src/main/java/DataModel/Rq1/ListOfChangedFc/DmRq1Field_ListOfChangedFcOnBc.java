/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.ListOfChangedFc;

import DataModel.Dgs.DmDgsFcReleaseI;
import DataModel.DmElementListField_ReadOnlyI;
import DataModel.DmField;
import DataModel.Rq1.ListOfChangedFc.DmRq1Field_ListOfChangedFcOnBc.FlagsOnFC.Flag;
import static DataModel.Rq1.ListOfChangedFc.DmRq1Field_ListOfChangedFcOnBc.FlagsOnFC.Flag.DIRECTLY_MAPPED;
import static DataModel.Rq1.ListOfChangedFc.DmRq1Field_ListOfChangedFcOnBc.FlagsOnFC.Flag.MAX_FC_LEVEL_REACHED;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1Fc;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.Rq1.Records.DmRq1Rrm_Bc_Fc;
import DataModel.DmMappedElement;
import Rq1Data.Enumerations.IntegrationAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import util.EcvMapMap;
import util.SafeArrayList;
import java.util.EnumSet;
import static DataModel.Rq1.ListOfChangedFc.DmRq1Field_ListOfChangedFcOnBc.FlagsOnFC.Flag.END_OF_FC_PREDECESSOR_REACHED;
import java.util.TreeMap;

/**
 * A field that calculates the FC versions on a BC changed against the
 * predecessor BC. The calculation is based on the mappings between FC and BC
 * and the predecessor list of BC and FC.
 *
 *
 * @author GUG2WI
 */
public class DmRq1Field_ListOfChangedFcOnBc extends DmField implements DmElementListField_ReadOnlyI<DmRq1Fc> {

    final private static Logger LOGGER = Logger.getLogger(DmRq1Field_ListOfChangedFcOnBc.class.getCanonicalName());

    final private static int DEFAULT_MAX_BC_VISIT_LEVEL = 20;
    final private static int DEFAULT_MAX_FC_VISIT_LEVEL = 10;

    /**
     * Container for flags mapped to FCs.
     * <br>
     * Key: The FC for which a warning is stored.
     * <br>
     * Value: The text of the warning.
     */
    public static class FlagsOnFC {

        public enum Flag {
            DIRECTLY_MAPPED,
            MAX_FC_LEVEL_REACHED,
            END_OF_FC_PREDECESSOR_REACHED;

        }

        final private TreeMap<DmRq1Fc, EnumSet<Flag>> map = new TreeMap<>();

        void addFlag(DmRq1Fc fc, Flag flag) {
            assert (fc != null);
            assert (flag != null);

            EnumSet<Flag> flagSet = map.get(fc);
            if (flagSet == null) {
                flagSet = EnumSet.of(flag);
            } else {
                flagSet.add(flag);
            }
            map.put(fc, flagSet);
        }

        /**
         * Returns the set of flags for the requested FC.
         *
         * @param fc The FC for which the flags shall be provided.
         * @return A possible empty set of flags. Never null.
         */
        public EnumSet<Flag> getForFc(DmRq1Fc fc) {
            assert (fc != null);
            EnumSet<Flag> set = map.get(fc);
            if (set == null) {
                set = EnumSet.noneOf(Flag.class);
            }
            return (set);
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append(FlagsOnFC.class.getSimpleName()).append("(");
            for (Map.Entry<DmRq1Fc, EnumSet<Flag>> entry : map.entrySet()) {
                b.append("\n");
                b.append(entry.getKey().toString());
                b.append(",");
                b.append(entry.getValue().toString());
            }
            b.append(")");
            return (b.toString());
        }

    }

    /**
     * Container for holding FCs ordered by name and version.
     * <br>
     * key1: Name of the FC
     * <br>
     * key2: Version of the FC
     * <br>
     * value: The FC for the name and version.
     */
    private static class FcMapMap {

        final private EcvMapMap<String, String, DmRq1Fc> map = new EcvMapMap<>();

        void add(DmRq1Fc fc) {
            assert (fc != null);
            map.put(fc.getName(), fc.getVersion(), fc);
        }

        List<DmRq1Fc> getSortedFc() {
            return (map.getValues());
        }

    }

    final private DmRq1Pst pst;
    final private DmRq1Bc bc;
    private int maxBcVisitLevel = DEFAULT_MAX_BC_VISIT_LEVEL;
    private int maxFcVisitLevel = DEFAULT_MAX_FC_VISIT_LEVEL;

    private SafeArrayList<DmRq1Fc> list = null;
    private boolean loaded = false;
    private FlagsOnFC flags = null;

    public DmRq1Field_ListOfChangedFcOnBc(DmRq1Pst pst, DmRq1Bc bc, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (pst != null);
        assert (bc != null);

        this.pst = pst;
        this.bc = bc;
    }

    public DmRq1Field_ListOfChangedFcOnBc(DmRq1Bc bc, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (bc != null);

        this.pst = null;
        this.bc = bc;
    }

    public void setMaxBcVisitLevel(int maxBcVisitLevel) {
        this.maxBcVisitLevel = maxBcVisitLevel;
    }

    public void setMaxFcVisitLevel(int maxFcVisitLevel) {
        this.maxFcVisitLevel = maxFcVisitLevel;
    }

    public boolean isFcBcListAvailable() {
        if (pst != null) {
            DmRq1Release predecessorPst = pst.PREDECESSOR.getElement();
            if (predecessorPst instanceof DmRq1Pst) {
                return (((DmRq1Pst) predecessorPst).isFcBcListAvailable());
            }
        }
        return (false);
    }

    @Override
    public List<DmRq1Fc> getElementList() {
        SafeArrayList<DmRq1Fc> alreadyLoadedList = list;
        if (alreadyLoadedList != null) {
            return (alreadyLoadedList.getImmutableList());
        } else {
            return (getElementList_Synchronized().getImmutableList());
        }
    }

    private synchronized SafeArrayList<DmRq1Fc> getElementList_Synchronized() {

        if (list == null) {

            //
            // Check if FC-BC-List is available in the predecessor PST
            //
            DmRq1Pst predecessorPstWithFcBcList = null;
            if (pst != null) {
                DmRq1Release predecessorPst = pst.PREDECESSOR.getElement();
                if (predecessorPst instanceof DmRq1Pst) {
                    if (((DmRq1Pst) predecessorPst).isFcBcListAvailable() == true) {
                        predecessorPstWithFcBcList = (DmRq1Pst) predecessorPst;
                    }
                }
            }

            //
            // Create new structures for the result
            //
            SafeArrayList<DmRq1Fc> newList = new SafeArrayList<>();
            FlagsOnFC newFlags = new FlagsOnFC();

            //
            // Put mapped FC in an ordered form
            //
            FcMapMap fcNameMap = new FcMapMap();
            for (DmMappedElement<DmRq1Rrm, DmRq1Release> mappedFc : bc.MAPPED_CHILDREN.getElementList()) {
                if (mappedFc.getMap() instanceof DmRq1Rrm_Bc_Fc) {
                    DmRq1Rrm_Bc_Fc rrm = (DmRq1Rrm_Bc_Fc) mappedFc.getMap();
                    if (rrm.isCanceled() == false) {
                        if (mappedFc.getTarget() instanceof DmRq1Fc) {
                            DmRq1Fc fc = (DmRq1Fc) mappedFc.getTarget();
                            fcNameMap.add(fc);
                            newFlags.addFlag(fc, DIRECTLY_MAPPED);
                        }
                    }
                }

            }

            //
            // Create List of FC with intermediate FCs in reverse order
            //
            for (DmRq1Fc fc : fcNameMap.getSortedFc()) {
                newList.add(fc);
                List<DmRq1Fc> intermediateFc;
                if (predecessorPstWithFcBcList != null) {
                    intermediateFc = getPredecessorFcForFc_BasedOnFcBcList(predecessorPstWithFcBcList, fc, newFlags);
                } else {
                    intermediateFc = getPredecessorFcForFc_BasedOnDatabaseRelations(fc, newFlags);
                }
                newList.addAll(intermediateFc);
            }

            //
            // Reverse list to get correct order
            //
            newList.reverse();

            //
            // Set class variables for the result
            //
            list = newList;
            flags = newFlags;
            loaded = true;
        }

        return (list);
    }

    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public void reload() {
        list = null;
        fireFieldChanged();
    }

    public FlagsOnFC getFlags() {
        return flags;
    }

    //--------------------------------------------------------------------------
    //
    // Get predecessor FCs for one FC, if no FC/BC/List of predecessor PST is available.
    //
    // The following logic is used:
    // - Walk throught the list of predecessors of the FC.
    // - For each of this predecessor FC: Check if it is mapped to the predecessor PST.
    //
    //--------------------------------------------------------------------------
    List<DmRq1Fc> getPredecessorFcForFc_BasedOnFcBcList(DmRq1Pst predecessorPst, DmRq1Fc fc, FlagsOnFC mapOfWarnings) {
        assert (predecessorPst != null);
        assert (fc != null);
        assert (mapOfWarnings != null);

        List<DmRq1Fc> predecessorFc = new ArrayList<>();
        List<DmDgsFcReleaseI> fcWithSameNameOnPredecessorPst = getFcWithSameNameOnPredecessorPst(predecessorPst, fc);

        //
        // If FC itself is in the predecessor PST -> return empty predecessor list.
        //
        if (fc.equalsFcInCollection(fcWithSameNameOnPredecessorPst) == true) {
            return (predecessorFc);
        }

        //
        // Get the FCs with same name on the same BC. Lookup for predecessor will stop, if the predecessor FC is mapped to the same BC.
        //
        List<DmRq1Fc> fcWithSameNameOnSameBc = getFcWithSameNameOnSameBc(fc.getName());

        //
        // Get predecessor FC as starting point for the loop below.
        //
        DmRq1Fc currentFc = (DmRq1Fc) fc.PREDECESSOR.getElement();

        //
        // Loop through predecessor FCs till max number of visits is reached or no predecessor is available.
        //
        int currentFcVisitLevel = 0;
        while ((currentFc != null) && (currentFcVisitLevel < maxFcVisitLevel)) {

            //
            // Stop here and return the FCs listed till now, if this predecessor FC is mapped to the current BC.
            //
            if (currentFc.equalsFcInCollection(fcWithSameNameOnSameBc) == true) {
                return (predecessorFc);
            }

            //
            // Stop here and return the FCs listed till now, if this predecessor FC is on the predecessor PST.
            //
            //
            if (currentFc.equalsFcInCollection(fcWithSameNameOnPredecessorPst) == true) {
                return (predecessorFc);
            }

            //
            // No matching till now. Add current FC to predecessor list and switch to next FC.
            //
            predecessorFc.add(currentFc);
            currentFc = (DmRq1Fc) currentFc.PREDECESSOR.getElement();
            currentFcVisitLevel++;
        }

        //
        // Mark the last predecessor with a warning, if the list of predecessor FCs ends without a match.
        //
        if (currentFc == null) {
            setFlag(mapOfWarnings, fc, predecessorFc, END_OF_FC_PREDECESSOR_REACHED);
            return (predecessorFc);
        }

        //
        // Mark the last predecessor with a warning, if the maximum number of FC to visit was reached.
        //
        if (currentFcVisitLevel >= maxFcVisitLevel) {
            setFlag(mapOfWarnings, fc, predecessorFc, MAX_FC_LEVEL_REACHED);
            return (predecessorFc);
        }

        throw (new Error("Unreachable statement reached."));
    }

    private List<DmDgsFcReleaseI> getFcWithSameNameOnPredecessorPst(DmRq1Pst predecessorPst, DmRq1Fc fc) {
        assert (predecessorPst != null);
        assert (predecessorPst.isFcBcListAvailable());
        assert (fc != null);

        List<DmDgsFcReleaseI> result = new ArrayList<>();

        List<DmRq1Pst.FinalRelease<DmDgsFcReleaseI>> listOfFinalFc = predecessorPst.determineFinalFc(fc.getName());
        for (DmRq1Pst.FinalRelease<DmDgsFcReleaseI> finalFc : listOfFinalFc) {
            result.addAll(finalFc.getReleases());
        }

        return (result);
    }

    //--------------------------------------------------------------------------
    //
    // Get predecessor FCs for one FC, if no FC/BC/List of predecessor PST is available.
    //
    // The following logic is used:
    // - Walk throught the list of predecessors of the FC.
    // - For each of this predecessor FC: Check if it is mapped to the current BC or to one of the predecessor BCs.
    //
    //--------------------------------------------------------------------------
    List<DmRq1Fc> getPredecessorFcForFc_BasedOnDatabaseRelations(DmRq1Fc fc, FlagsOnFC mapOfWarnings) {
        assert (fc != null);
        assert (mapOfWarnings != null);

        List<DmRq1Fc> predecessorFc = new ArrayList<>();
        List<DmRq1Fc> fcWithSameNameOnPredecessorBc = getFcWithSameNameOnPredecessorBc(fc.getName());

        //
        // If FC itself is in the predecessor BC -> return empty predecessor list.
        //
        if (fc.equalsFcInCollection(fcWithSameNameOnPredecessorBc) == true) {
            return (predecessorFc);
        }

        //
        // Get the FCs with same name on the same BC. Lookup for predecessor will stop, if the predecessor FC is mapped to the same BC.
        //
        List<DmRq1Fc> fcWithSameNameOnSameBc = getFcWithSameNameOnSameBc(fc.getName());

        //
        // Get predecessor FC as starting point for the loop below.
        //
        DmRq1Fc currentFc = (DmRq1Fc) fc.PREDECESSOR.getElement();

        //
        // Loop through predecessor FCs till max number of visits is reached or no predecessor is available.
        //
        int currentFcVisitLevel = 0;
        while ((currentFc != null) && (currentFcVisitLevel < maxFcVisitLevel)) {

            //
            // Stop here and return the FCs listed till now, if this predecessor FC is mapped to the current BC.
            //
            if (currentFc.equalsFcInCollection(fcWithSameNameOnSameBc) == true) {
                return (predecessorFc);
            }

            //
            // Stop here and return the FCs listed till now, if this predecessor FC is on the predecessor BC.
            //
            //
            if (currentFc.equalsFcInCollection(fcWithSameNameOnPredecessorBc) == true) {
                return (predecessorFc);
            }

            //
            // No matching till now. Add current FC to predecessor list and switch to next FC.
            //
            predecessorFc.add(currentFc);
            currentFc = (DmRq1Fc) currentFc.PREDECESSOR.getElement();
            currentFcVisitLevel++;
        }

        //
        // Mark the last predecessor with a warning, if the list of predecessor FCs ends without a match.
        //
        if (currentFc == null) {
            setFlag(mapOfWarnings, fc, predecessorFc, END_OF_FC_PREDECESSOR_REACHED);
            return (predecessorFc);
        }

        //
        // Mark the last predecessor with a warning, if the maximum number of FC to visit was reached.
        //
        if (currentFcVisitLevel >= maxFcVisitLevel) {
            setFlag(mapOfWarnings, fc, predecessorFc, MAX_FC_LEVEL_REACHED);
            return (predecessorFc);
        }

        throw (new Error("Unreachable statement reached."));
    }

    private void setFlag(FlagsOnFC mapOfWarnings, DmRq1Fc fc, List<DmRq1Fc> predecessorFc, Flag type) {
        assert (mapOfWarnings != null);
        assert (fc != null);
        assert (predecessorFc != null);
        assert (type != null);

        if (predecessorFc.isEmpty() == true) {
            mapOfWarnings.addFlag(fc, type);
        } else {
            mapOfWarnings.addFlag(predecessorFc.get(predecessorFc.size() - 1), type);
        }

    }

    List<DmRq1Fc> getFcWithSameNameOnSameBc(String fcName) {
        assert (fcName != null);
        assert (fcName.isEmpty() == false);

        return (getFcWithSameNameOnBc(bc, fcName));
    }

    List<DmRq1Fc> getFcWithSameNameOnBc(DmRq1Bc bcToExamine, String fcName) {
        assert (bcToExamine != null);
        assert (fcName != null);
        assert (fcName.isEmpty() == false);

        List<DmRq1Fc> result = new ArrayList<>();
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> mappedFc : bcToExamine.MAPPED_CHILDREN.getElementList()) {
            if (mappedFc.getMap() instanceof DmRq1Rrm_Bc_Fc) {
                DmRq1Rrm_Bc_Fc rrm = (DmRq1Rrm_Bc_Fc) mappedFc.getMap();
                if ((rrm.isCanceled() == false) && (rrm.INTEGRATION_ACTION.getValue() != IntegrationAction.REMOVE)) {
                    if (mappedFc.getTarget() instanceof DmRq1Fc) {
                        DmRq1Fc fc = (DmRq1Fc) mappedFc.getTarget();
                        if (fcName.equals(fc.getName()) == true) {
                            result.add(fc);
                        }
                    }
                }
            }
        }

        return (result);
    }

    //--------------------------------------------------------------------------
    //
    // Determine FC on predecessor BC
    //
    //--------------------------------------------------------------------------
    private List<DmRq1Fc> getFcWithSameNameOnPredecessorBc(String fcName) {
        assert (fcName != null);
        assert (fcName.isEmpty() == false);

        DmRq1Bc currentBc = (DmRq1Bc) bc.PREDECESSOR.getElement();
        int currentBcVisitLevel = 0;

        while ((currentBc != null) && (currentBcVisitLevel < maxBcVisitLevel)) {
            List<DmRq1Fc> fcList = getFcWithSameNameOnBc(currentBc, fcName);
            if (fcList.isEmpty() == false) {
                return (fcList);
            }
            currentBc = (DmRq1Bc) currentBc.PREDECESSOR.getElement();
            currentBcVisitLevel++;
        }

        return (new ArrayList<>());
    }

}
