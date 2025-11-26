/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import ToolUsageLogger.ToolUsageLogger;
import util.error.EcvMailException;
import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miw83wi
 */
public class EcvMailService {

    public static void prepareToSend(MailSendable node, MailSendable.MailActionType type) {
        assert (node != null);
        assert (type != null);
        if (type.getMailAddress(node) == null) {
            return;
        }
        if (type.getMailAddress(node).isEmpty() == false) {
            String recipient = null;
            String title = null;

            try {

                recipient = URLEncoder.encode(type.getMailAddress(node), "utf-8").replaceAll("\\+", "%20");
                if (recipient.equals("%40")) {
                    recipient = "";
                }
                title = URLEncoder.encode(node.getTypeIdTitleforMail(), "utf-8").replaceAll("\\+", "%20");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(EcvMailService.class.getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(EcvMailService.class.getName(), ex);
                return;
            }
            StringBuilder b = new StringBuilder(200);
            b.append("mailto:" + recipient + ";");
            b.append("?subject=" + title + "%20");
            b.append("&body=%0A%0A%0A" + title + "%20");

            try {
                Desktop.getDesktop().mail(new URI(b.toString()));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(EcvMailService.class
                        .getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(EcvMailService.class.getName(), ex);

            }
        }
    }

    public static void prepareToSend(List<MailSendable> list, MailSendable.MailActionType type) throws EcvMailException {
        assert (list != null);
        assert (list.isEmpty() == false);
        assert (type != null);

        List<String> recipients = new ArrayList<>();
        List<String> requestOneId = new ArrayList<>();
        List<String> subject = new ArrayList<>();
        for (MailSendable m : list) {
            String mailAdress = type.getMailAddress(m);
            if (mailAdress != null) {
                if (mailAdress.isEmpty() == false) {
                    if (!recipients.contains(mailAdress) && !mailAdress.equals("@")) {
                        recipients.add(mailAdress);
                    }
                    if (!requestOneId.contains(m.getTypeIdTitleforMail())) {
                        requestOneId.add(m.getTypeIdTitleforMail());
                    }
                    if (!subject.contains(m.getIdForSubject()) && subject.size() < 3) {
                        subject.add(m.getIdForSubject());
                    }
                }
            }
        }
        if (recipients.size() > 0) {
            StringBuilder b = new StringBuilder(200);
            b.append("mailto:");
            for (String s : recipients) {
                b.append(s + ";");
            }
            b.append("?subject=");
            for (String s : subject) {
                try {
                    b.append(URLEncoder.encode(s, "utf-8") + "%20");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(EcvMailService.class.getName()).log(Level.SEVERE, null, ex);
                    ToolUsageLogger.logError(EcvMailService.class.getName(), ex);
                    return;
                }
            }
            if (requestOneId.size() > 3) {
                b.append("...%20");
            }

            b.append("&body=%0A%0A%0A%0A");
            for (String s : requestOneId) {
                try {
                    b.append(URLEncoder.encode(s, "utf-8") + "%0A");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(EcvMailService.class.getName()).log(Level.SEVERE, null, ex);
                    ToolUsageLogger.logError(EcvMailService.class.getName(), ex);
                    return;
                }
            }
            b.append("%20");
            if (b.length() > 3000) {
                throw new EcvMailException("Message too long (" + b.length() + " of 3000 characters)");
            }
            try {
                Desktop.getDesktop().mail(new URI(b.toString().replaceAll("\\+", "%20")));
            } catch (URISyntaxException | IOException ex) {
                Logger.getLogger(EcvMailService.class
                        .getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(EcvMailService.class.getName(), ex);

            }
        }
    }
}
