/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 * Stores the login data for authorization to an OLSC server of a RQ1 database.
 *
 * @author gug2wi
 */
class OslcLoginData {

    /**
     * State of the object.
     */
    public enum State {

        /**
         * No login data stored.
         */
        EMPTY,
        /**
         * Login data stored, no attempt for login done yet with this data.
         */
        SET,
        /**
         * Login data stored, successfull login was already performed with this
         * data.
         */
        VALIDATED
    }
    private String loginName;
    private char[] password;
    private String fullname;
    private String email;
    private String phone;
    private String isActive;
    private String miscInfo;
    private OslcRecordIdentifier databaseReference;

    private OslcRq1ServerDescription serverDescription;
    private State state;

    /**
     * Creates an object with no login data set. The state is set to
     * {@link Rq1OslcLoginData.State#EMPTY}.
     */
    public OslcLoginData() {
        reset();
    }

    /**
     * Returns the state of the object.
     *
     * @return Object state.
     */
    public State getState() {
        return state;
    }

    /**
     * Set the state of the object
     *
     * @param state New state.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Deletes the contained login data and sets the state to
     * empty.
     */
    final public void reset() {
        loginName = "";
        if (password != null) {
            for (char c : password) {
                c = '0';
            }
            password = null;
        }
        serverDescription = null;
        loginName = "";
        state = State.EMPTY;
    }

    /**
     * Sets the login data in the object. The state is set to
     * {@link Rq1OslcLoginData.State#SET}.
     *
     * @param loginName User name for login.
     * @param password Password.
     * @param serverDescription Information about the OSLC server.
     */
    public void set(String loginName, char[] password, OslcRq1ServerDescription serverDescription) {
        if ((loginName == null) || (password == null) || (serverDescription == null)) {
            throw (new Error("loginName == null"));
        }
        this.loginName = loginName;
        this.password = password;
        this.serverDescription = serverDescription;
        state = State.SET;
    }

    /**
     * Returns the login name. An empty string is returned, if no login data is
     * set.
     *
     * @return Login name or empty string.
     */
    public String getLoginName() {
        if (state != State.EMPTY) {
            return loginName;
        } else {
            return "";
        }
    }

    /**
     * Returns the server description or null, if no data was set.
     *
     * @return Server description.
     */
    public OslcRq1ServerDescription getServerDescription() {
        return (serverDescription);
    }

    /**
     * Returns the password.
     */
    public char[] getPassword() {
        return (password);
    }

    public void setFullName(String fullName) {
        this.fullname = fullName;
    }

    public void setEmail(String eMail) {
        this.email = eMail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public void setMiscInfo(String miscInfo) {
        this.miscInfo = miscInfo;
    }

    public void setDatabaseReference(OslcRecordIdentifier reference) {
        assert (reference != null);
        this.databaseReference = reference;
    }

    public String getFullName() {
        return (fullname);
    }

    public String getEmail() {
        return (email);
    }

    public String getPhone() {
        return (phone);
    }

    public String getIsActive() {
        return (isActive);
    }

    public String getMiscInfo() {
        return (miscInfo);
    }

    public OslcRecordIdentifier getDatabaseReference() {
        return (databaseReference);
    }
}
