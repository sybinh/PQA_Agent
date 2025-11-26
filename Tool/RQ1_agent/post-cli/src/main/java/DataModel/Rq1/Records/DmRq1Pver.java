/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmValueField_Rq1PollerResponse;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_Pver_NotSuitableForINMA;
import RestClient.Exceptions.RestException;
import RestClient.Rq1Poller.RestClient_Rq1Poller;
import RestClient.Rq1Poller.RestClient_Rq1PollerResponse;
import Rq1Cache.Records.Rq1Pver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingWorker;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvDate;
import util.UiWorker;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1Pver extends DmRq1Pst {
    
    final private static Logger LOGGER = Logger.getLogger(DmRq1Pver.class.getCanonicalName());
    
    final public DmRq1Field_Text INTEGRATOR;
    final public DmRq1Field_Text PROPLATO_HARDWARE;

    final public DmRq1Field_Enumeration IN_MA_BUILD_STATUS;
    final public DmValueField_Rq1PollerResponse RQ1_POLLER_RESPONSE;
    
    private SwingWorker<RestClient_Rq1PollerResponse, Void> rq1PollerWorker;
    private final AtomicBoolean isRq1PollerActive = new AtomicBoolean(false);
    private UiWorker<RestClient_Rq1PollerResponse> manageWorker;
    private final AtomicBoolean isManageWorkerActive = new AtomicBoolean(false);
    private final List<Rq1PollerListener> listenerList = new ArrayList<>();
    
    public DmRq1Pver(String subjectType, Rq1Pver rq1Pver) {
        super(subjectType, rq1Pver);

        addField(INTEGRATOR = new DmRq1Field_Text(this, rq1Pver.INTEGRATOR, "ECB: Integrator"));
        addField(PROPLATO_HARDWARE = new DmRq1Field_Text(this, rq1Pver.PROPLATO_HARDWARE, "Hardware"));

        addField(IN_MA_BUILD_STATUS = new DmRq1Field_Enumeration(rq1Pver.IN_MA_BUILD_STATUS, "InMa build Status"));
        addField(RQ1_POLLER_RESPONSE = new DmValueField_Rq1PollerResponse("InMa Comment", null));
        
        addRule(new Rule_Pver_NotSuitableForINMA(this));
    }

    @Override
    final public SortedSet<String> getActiveDerivatives() {
        SortedSet<String> result = new TreeSet<>();
        result.add(extractPverName(TITLE.getValue()));

        return (result);
    }

    @Override
    final public Set<String> getAll_ProPlaTo_Cluster() {
        Set<String> result = new TreeSet<>();
        if (EXTERNAL_ID.isEmpty() == false) {
            if (PROJECT.getElement() instanceof DmRq1SwCustomerProject_Leaf) {
                String cluster = ((DmRq1SwCustomerProject_Leaf) PROJECT.getElement()).getClusterForProgrammstandsschiene(EXTERNAL_ID.getValue());
                if (cluster != null) {
                    result.add(cluster);
                }
            }
        }
        return result;
    }

    @Override
    final public Set<String> getAll_ProPlaTo_Schiene() {
        Set<String> l = new TreeSet<>();
        String externalId = EXTERNAL_ID.getValue();
        if ((externalId != null) && (externalId.isEmpty() == false)) {
            if (PROJECT.getElement() instanceof DmRq1SwCustomerProject_Leaf) {
                String cluster = ((DmRq1SwCustomerProject_Leaf) PROJECT.getElement()).getClusterForProgrammstandsschiene(externalId);
                if (cluster != null) {
                    l.add(externalId);
                }
            }

        }
        return (l);
    }

    @Override
    final public Set<String> getAll_ProPlaTo_Hardware() {
        Set<String> result = new TreeSet<>();
        String hardware = PROPLATO_HARDWARE.getValue();
        if ((hardware != null) && hardware.trim().isEmpty() == false) {
            result.add(hardware.trim());
        }
        return result;
    }

    @Override
    final public Map<String, String> getHardwareInformation() {
        Map<String, String> retVal = new HashMap<>();
        retVal.put(EXTERNAL_ID.getValue(), PROPLATO_HARDWARE.getValue());
        return retVal;
    }

    @Override
    final public Map<String, String> getHardwareInformationProPlaTo() {
        return getHardwareInformation();
    }

    @Override
    final public boolean isInProPlaToLine(String line) {
        return this.EXTERNAL_ID.getValue().equals(line);
    }

    @Override
    final public boolean getProPlaToKennungMeta(String line) {
        return true;
    }

    @Override
    final public String getMetaSchiene() {
        return this.EXTERNAL_ID.getValue().toString();
    }

    @Override
    final public EcvDate getPlannedDateForPstLineByExternalName(String line) {
        return this.PLANNED_DATE.getValue();
    }

    @Override
    final public boolean areDerivativesRelevant() {
        return false;
    }

    static final private Pattern matchPattern = Pattern.compile("(PVER)? *:? *([a-zA-Z0-9_]+)");

    static public String extractPverName(String title) {
        Matcher matcher = matchPattern.matcher(title);
        if (matcher.find() == true) {
            String name = matcher.group(2);
            return (name);
        } else {
            return ("");
        }
    }
    
    //---------------------------------------------------------------------
    //
    // Handle requests to Rq1Poller (InMa)
    //
    //---------------------------------------------------------------------
    public interface Rq1PollerListener {
        void checkedPverForInMa(RestClient_Rq1PollerResponse response);
    }
    
    private RestClient_Rq1PollerResponse executeWorker() {
        if (isRq1PollerActive.compareAndSet(false, true)) {
            rq1PollerWorker = new SwingWorker<RestClient_Rq1PollerResponse, Void>() {
                @Override
                protected RestClient_Rq1PollerResponse doInBackground() throws Exception {
                    try {
                        return (RestClient_Rq1Poller.client.checkPverForINMA(getRq1Id()));
                    } catch (RestException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                        ToolUsageLogger.logError(DmRq1Pver.class.getCanonicalName(), ex);
                        return null;
                    }
                }
                
                @Override
                protected void done() {
                    rq1PollerWorker = null;
                    isRq1PollerActive.set(false);
                }
            };
            rq1PollerWorker.execute();
        }
        try {
            return (rq1PollerWorker.get());
        } catch (InterruptedException | ExecutionException ex) {
            LOGGER.log(Level.SEVERE, "Getting result failed.", ex);
            ToolUsageLogger.logError(DmRq1Pver.class.getCanonicalName(), ex);
        } catch (CancellationException ex) {
            LOGGER.log(Level.INFO, "Getting result was cancelled.", ex);
        }
        return (null);
    }
    
    public void addListener(Rq1PollerListener listener) {
        assert (listener != null);
        if (listenerList.contains(listener) == false) {
            listenerList.add(listener);
        }
    }
    
    public void checkPverForInMa() {
        if (isManageWorkerActive.compareAndSet(false, true)) {
            UiWorker.execute(manageWorker = new UiWorker<RestClient_Rq1PollerResponse>(UiWorker.LOADING) {
                @Override
                protected RestClient_Rq1PollerResponse backgroundTask() {
                    return(executeWorker());
                }
                
                @Override
                protected void uiEndTask() {
                    if (isCancelled() == false) {
                        RestClient_Rq1PollerResponse result = getResult();
                        (new ArrayList<>(listenerList)).forEach((listener) -> {
                            listener.checkedPverForInMa(result);
                        });
                    }
                    manageWorker = null;
                    isManageWorkerActive.set(false);
                }
            });
        }
    }
    
    public void cancelCheckPverForInMa() {
        if (isRq1PollerActive.get() == true && rq1PollerWorker != null 
                && rq1PollerWorker.isDone() == false && rq1PollerWorker.isCancelled() == false) {
            rq1PollerWorker.cancel(true);
        }
        if (isManageWorkerActive.get() == true && manageWorker != null 
                && manageWorker.isDone() == false && manageWorker.isCancelled() == false) {
            manageWorker.cancel(true);
        }
    }
}
