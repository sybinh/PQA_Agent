/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.List;

/**
 *
 * @author miw83wi
 */
public interface MailSendable {

    public enum MailActionType {
        ASSIGNEE("Send Mail to Assignee of Element") {
            /**
             * Returns a valid email address, if it is not valid, null is
             * returned
             *
             * @param sendable
             * @return
             */
            @Override
            public String getMailAddress(MailSendable sendable) {
                String mailAddress = sendable.getAssigneeMail();
                if (mailAddress == null) {
                    return null;
                }
                if (mailAddress.isEmpty() || mailAddress.equals("@")) {
                    return null;
                }
                return mailAddress;
            }
        },
        ASSIGNEEMAP("Send Mail to Assignee of Map") {
            /**
             * Returns a valid email address, if it is not valid, null is
             * returned
             *
             * @param sendable
             * @return
             */
            @Override
            public String getMailAddress(MailSendable sendable) {
                String mailAddress = sendable.getAssigneeMail();
                if (mailAddress == null) {
                    return null;
                }
                if (mailAddress.isEmpty() || mailAddress.equals("@")) {
                    return null;
                }
                return mailAddress;
            }
        },
        PROJECTLEADER("Send Mail to Project Leader") {
            /**
             * Returns a valid email address, if it is not valid, null is
             * returned
             *
             * @param sendable
             * @return
             */
            @Override
            public String getMailAddress(MailSendable sendable) {
                String mailAddress = sendable.getProjectLeaderMail();
                if (mailAddress == null) {
                    return null;
                }
                if (mailAddress.isEmpty() || mailAddress.equals("@")) {
                    return null;
                }
                return mailAddress;
            }
        },
        REQUESTER("Send Mail to Requester of Element") {
            /**
             * Returns a valid email address, if it is not valid, null is
             * returned
             *
             * @param sendable
             * @return
             */
            @Override
            public String getMailAddress(MailSendable sendable) {
                String mailAddress = sendable.getRequesterMail();
                if (mailAddress == null) {
                    return null;
                }
                if (mailAddress.isEmpty() || mailAddress.equals("@")) {
                    return null;
                }
                return mailAddress;
            }
        },
        CONTACT("Send Mail to Contact of Problem") {
            /**
             * Returns a valid email address, if it is not valid, null is
             * returned
             *
             * @param sendable
             * @return
             */
            @Override
            public String getMailAddress(MailSendable sendable) {
                String mailAddress = sendable.getContactMail();
                if (mailAddress == null) {
                    return null;
                }
                if (mailAddress.isEmpty() || mailAddress.equals("@")) {
                    return null;
                }
                return mailAddress;
            }
        };

        private String title;

        private MailActionType(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }

        public abstract String getMailAddress(MailSendable sendable);
    }

    /**
     * Returns the assignee email or null if no assignee available for the
     * element
     *
     * @return assignee email or null;
     */
    String getAssigneeMail();

    /**
     * Returns the project leader email or null if no project leader available
     * for the element
     *
     * @return project email or null;
     */
    String getProjectLeaderMail();

    /**
     * Returns the requester email or null if no requester available for the
     * element
     *
     * @return requester email or null;
     */
    String getRequesterMail();
    
    /**
     * Returns the contact email or null if no contact available for the
     * element
     *
     * @return contact email or null;
     */
    String getContactMail();

    String getTypeIdTitle();

    String getId();

    /**
     * Returns a list of the actiontypes which are possible for the element
     *
     * @return
     */
    List<MailActionType> getActionName();

    /**
     * Default Method for Mail Body Title, used to extend the functionality to
     * IRM,RRM
     *
     * @return String representation for Body Title
     */
    String getTypeIdTitleforMail();

    String getIdForSubject();

}
