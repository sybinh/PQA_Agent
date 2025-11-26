/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Rq1;

import OslcAccess.Doors.OslcDoorsModuleResponse;
import OslcAccess.OslcCommandExecutor;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvLoginManager;
import OslcAccess.OslcProtocolVersion;
import OslcAccess.OslcResponseFactory;
import RestClient.Exceptions.ConstraintViolationReportedByServer;
import RestClient.Exceptions.RestException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvXmlContainerElement;

/**
 * Added for test reasons only. Do not use for normal operation.
 *
 * @author gug2wi
 */
public class OslcRq1TestExecutor extends OslcCommandExecutor {

    public OslcRq1TestExecutor() {
        super(EcvLoginManager.getTestLoginData().getServerDescription().getRq1Server(), new Rq1AuthenticationProvider(EcvLoginManager.getTestLoginData()), new OslcResponseFactory());
    }

    public EcvXmlContainerElement testGetCommand(String urlString) {
        OslcRq1Response response;
        try {
            response = new OslcRq1Response(performGetRequest(OslcProtocolVersion.OSLC_20, urlString, ResponseFormat.TEXT_OR_XML));
        } catch (RestException | ConstraintViolationReportedByServer ex) {
            Logger.getLogger(OslcRq1TestExecutor.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1TestExecutor.class.getName(), ex);
            return (null);
        }

        return (response.getXmlContent());
    }

    public EcvXmlContainerElement testGetCommand(OslcProtocolVersion version, String urlString) {
        OslcRq1Response response;
        try {
            response = new OslcRq1Response(performGetRequest(version, urlString, ResponseFormat.TEXT_OR_XML));
        } catch (RestException | ConstraintViolationReportedByServer ex) {
            Logger.getLogger(OslcRq1TestExecutor.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1TestExecutor.class.getName(), ex);
            return (null);
        }

        return (response.getXmlContent());
    }

    public EcvXmlContainerElement testPutCommand(String urlString, String body) {
        OslcRq1Response response;
        try {
            response = new OslcRq1Response(performPutRequest(OslcProtocolVersion.OSLC_20, urlString, body, null));
        } catch (RestException | ConstraintViolationReportedByServer ex) {
            Logger.getLogger(OslcRq1TestExecutor.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(OslcRq1TestExecutor.class.getName(), ex);
            return (null);
        }

        return (response.getXmlContent());
    }

    public String testGet(OslcRq1ServerDescription server, String recordType, String id, String properties) {

        return (testGetCommand(server.getOslcUrl() + "record/?rcm.type=" + recordType + "&oslc.where=cq:id=\"" + id + "\"&oslc.properties=" + properties).getXmlString());

    }

}
