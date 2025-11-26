/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.PPT.BackChannel.BewertungOem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import util.EcvDate;
import util.EcvTableData;
import util.EcvTableRow;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_CustomerResponse extends Rq1XmlTable {

    //--------------------------------------------------------------------------
    //
    // Table definition
    //
    //--------------------------------------------------------------------------
    final public Rq1XmlTableColumn_String CUSTOMER_STATE;
    final public Rq1XmlTableColumn_String CUSTOMER_COMMENT;
    final public Rq1XmlTableColumn_String CUSTOMER_CONTACT;
    final public Rq1XmlTableColumn_String DATE;
    final public Rq1XmlTableColumn_Set DERIVATIVE;

    protected Rq1XmlTable_CustomerResponse() {

        addXmlColumn(CUSTOMER_STATE = new Rq1XmlTableColumn_String("Customer State", 10, "state", ColumnEncodingMethod.ATTRIBUTE));
        CUSTOMER_STATE.setOptional();

        addXmlColumn(CUSTOMER_COMMENT = new Rq1XmlTableColumn_String("Customer Comment", 10, "comment", ColumnEncodingMethod.ATTRIBUTE));
        CUSTOMER_COMMENT.setOptional();

        addXmlColumn(CUSTOMER_CONTACT = new Rq1XmlTableColumn_String("Customer Contact", 10, "contact", ColumnEncodingMethod.ATTRIBUTE));
        CUSTOMER_CONTACT.setOptional();

        addXmlColumn(DATE = new Rq1XmlTableColumn_String("Date", 10, "date", ColumnEncodingMethod.ATTRIBUTE));
        DATE.setOptional();

        addXmlColumn(DERIVATIVE = new Rq1XmlTableColumn_Set("Derivative", 20, "Derivate", ColumnEncodingMethod.ELEMENT_LIST));
        DERIVATIVE.setOptional();
    }

    /**
     * Supports the derivate handling in the customer response tables. The
     * derivatives in the customer response table is stored in a column of type
     * set of String.
     *
     * If a row in the table belongs to a PVER (has no derivatives), then the
     * set is empty.
     *
     * If a row in the table belongs to a PVAR (has derivatives), then the set
     * contains the internal derivative names for which the row is valid.
     */
    static public class Derivatives {

        final private SortedSet<String> derivativeSet = new TreeSet<>();

        /**
         * Create a empty derivative set. Fits to a PVER.
         */
        public Derivatives() {
        }

        private Derivatives(Derivatives template) {
            assert (template != null);
            derivativeSet.addAll(template.derivativeSet);
        }

        /**
         * Create a derivative set containing all the derivatives from the given
         * set.
         *
         * @param other Initial values for the derivative set.
         */
        private Derivatives(SortedSet<String> other) {
            assert (other != null);
            derivativeSet.addAll(other);
        }

        /**
         * Create a derivative set initialized with the given derivative.
         *
         * @param derivative Initial derivative in the set.
         */
        public Derivatives(String derivative) {
            assert (derivative != null);
            assert (derivative.isEmpty() == false);

            derivativeSet.add(derivative);
        }

        public Derivatives(String derivative1, String derivative2) {
            assert (derivative1 != null);
            assert (derivative1.isEmpty() == false);
            assert (derivative2 != null);
            assert (derivative2.isEmpty() == false);

            derivativeSet.add(derivative1);
            derivativeSet.add(derivative2);
        }

        /**
         * Returns a new derivative set which are contains all derivatives
         * available in both sets. Returns null, the sets have no derivatives in
         * common.
         *
         * @param other
         * @return null, if there are no derivatives common in both objects.
         */
        public Derivatives createRetainSet(Derivatives other) {
            assert (other != null);

            if (derivativeSet.isEmpty() && other.derivativeSet.isEmpty()) {
                return (new Derivatives());
            }

            Derivatives result = copy();
            result.derivativeSet.retainAll(other.derivativeSet);
            if (result.derivativeSet.isEmpty() == false) {
                return (result);
            }

            return (null);
        }

        public Derivatives createRetainSet(Record record) {
            assert (record != null);
            return (this.createRetainSet(record.getDerivatives()));
        }

        public Derivatives createSubtractSet(Derivatives other) {
            assert (other != null);

            if (derivativeSet.isEmpty() && other.derivativeSet.isEmpty()) {
                return (null);
            }

            Derivatives result = copy();
            result.derivativeSet.removeAll(other.derivativeSet);
            if (result.derivativeSet.isEmpty() == false) {
                return (result);
            }

            return (null);
        }

        public Derivatives createSubtractSet(Record record) {
            assert (record != null);
            return (Derivatives.this.createSubtractSet(record.getDerivatives()));
        }

        public Derivatives copy() {
            return (new Derivatives(this));
        }

        /**
         * Adds the given derivatives.
         *
         * @param toAdd
         * @return
         */
        public Derivatives addDerivatives(Derivatives toAdd) {
            assert (toAdd != null);
            derivativeSet.addAll(toAdd.derivativeSet);
            return (this);
        }

        public Derivatives addDerivatives(Record toAdd) {
            assert (toAdd != null);
            derivativeSet.addAll(toAdd.getDerivatives().derivativeSet);
            return (this);
        }

        /**
         * Returns the derivatives as a list that can be used to call the
         * setResponse methods in a loop.
         *
         * The list contains one empty string, if the set is empty.
         */
        public List<String> provideAsList() {
            List<String> result;

            if (derivativeSet.isEmpty()) {
                result = new ArrayList<>(1);
                result.add("");
            } else {
                result = new ArrayList<>(derivativeSet);
            }

            return (result);
        }

        public List<Derivatives> splitByDerivative() {
            List<Derivatives> result;

            if (derivativeSet.isEmpty()) {
                result = new ArrayList<>(0);
                result.add(new Derivatives());
            } else {
                result = new ArrayList<>(derivativeSet.size());
                for (String d : derivativeSet) {
                    result.add(new Derivatives(d));
                }
            }

            return (result);
        }

        boolean contains(Derivatives other) {
            assert (other != null);

            if (derivativeSet.isEmpty() && other.derivativeSet.isEmpty()) {
                return (true);
            } else {
                return (derivativeSet.containsAll(other.derivativeSet));
            }
        }

        public Collection<String> provideAsSet() {
            return (new TreeSet<>(derivativeSet));
        }

        public boolean isEmpty() {
            return (derivativeSet.isEmpty());
        }

        public void addDerivative(String line) {
            derivativeSet.add(line);
        }

    }

    static public class Record {

        final private String customerState;
        final private String customerComment;
        final private String customerContact;
        final private String date;
        final private Derivatives derivatives;

        protected Record(Response response, Derivatives derivative) {
            assert (response != null);
            assert (derivative != null);

            this.customerState = response.getBewertungOem().getTextInFile();
            this.customerComment = response.getKommentarOEM();
            this.customerContact = response.getAnsprechpartnerOEM();
            this.date = response.getOemDatumOrToday().getXmlValue();
            this.derivatives = derivative;
        }

        @SuppressWarnings("unchecked")
        protected Record(EcvTableData data, EcvTableRow row) {
            assert (data != null);
            assert (Rq1XmlTable_CustomerResponse.class.isInstance(data.getDescription()) == true);
            assert (row != null);

            Rq1XmlTable_CustomerResponse d = (Rq1XmlTable_CustomerResponse) data.getDescription();

            customerState = d.CUSTOMER_STATE.getValue(row);
            customerComment = d.CUSTOMER_COMMENT.getValue(row);
            customerContact = d.CUSTOMER_CONTACT.getValue(row);
            date = (String) d.DATE.getValue(row);
            SortedSet<String> der = d.DERIVATIVE.getValue(row);
            if (der != null) {
                derivatives = new Derivatives(d.DERIVATIVE.getValue(row));
            } else {
                derivatives = new Derivatives();
            }
        }

        protected Record(Record template, Derivatives derivatives) {
            assert (template != null);
            assert (derivatives != null);

            this.customerState = template.customerState;
            this.customerComment = template.customerComment;
            this.customerContact = template.customerContact;
            this.date = template.date;
            this.derivatives = derivatives;
        }

        protected void setValues(EcvTableRow row, Rq1XmlTable_CustomerResponse_Workitem d) {
            assert (row != null);
            assert (d != null);

            d.CUSTOMER_STATE.setValue(row, customerState);
            d.CUSTOMER_COMMENT.setValue(row, customerComment);
            d.CUSTOMER_CONTACT.setValue(row, customerContact);
            d.DATE.setValue(row, date);
            d.DERIVATIVE.setValue(row, derivatives.derivativeSet);
        }

        final public String getCustomerState() {
            return customerState;
        }

        final public String getCustomerComment() {
            return customerComment;
        }

        final public String getCustomerContact() {
            return customerContact;
        }

        final public String getDate() {
            return date;
        }

        final public Derivatives getDerivatives() {
            return derivatives;
        }

        /**
         * Returns the list of derivatives as string, or an empty string, if no
         * derivative is set.
         *
         * @return
         */
        final public String getDerivativeAsString() {
            if ((derivatives != null) && (derivatives.derivativeSet.isEmpty() == false)) {
                StringBuilder b = new StringBuilder();
                boolean first = true;
                for (String derivat : derivatives.derivativeSet) {
                    if (first == true) {
                        first = false;
                    } else {
                        b.append(", ");
                    }
                    b.append(derivat);
                }
                return (b.toString());
            }
            return ("");
        }

        protected boolean matchesResponse(Response response) {
            assert (response != null);

            return (customerState.equals(response.getBewertungOem().getTextInFile())
                    && customerComment.equals(response.getKommentarOEM())
                    && customerContact.equals(response.getAnsprechpartnerOEM()));
        }

        protected boolean matchesResponse(Record other) {
            assert (other != null);

            return (customerState.equals(other.customerState)
                    && customerComment.equals(other.customerComment)
                    && customerContact.equals(other.customerContact));
        }

        public void addDerivatives(Derivatives toAdd) {
            assert (toAdd != null);
            derivatives.addDerivatives(toAdd);
        }

        /**
         * Adds the values of the record as attributes to the given xmlElement.
         * Can be used for logging and diagnosis.
         *
         * @param xmlElement The element to which the data will be added.
         */
        public void addContentAsAttributes(EcvXmlElement xmlElement) {
            exportNonEmpty(xmlElement, "state", getCustomerState());
            exportNonEmpty(xmlElement, "comment", getCustomerComment());
            exportNonEmpty(xmlElement, "contact", getCustomerContact());
            exportNonEmpty(xmlElement, "date", getDate());
            exportNonEmpty(xmlElement, "derivative", getDerivativeAsString());
        }

        protected void exportNonEmpty(EcvXmlElement responseElement, String name, String value) {
            if ((value != null) && (value.isEmpty() == false)) {
                responseElement.addAttribute(name, value);
            }
        }

    }

    //--------------------------------------------------------------------------
    //
    // Support for user response
    //
    //--------------------------------------------------------------------------
    static public interface Response {

        public String getAnsprechpartnerOEM();

        public BewertungOem getBewertungOem();

        public String getKommentarOEM();

        /**
         * Get the OEM date from the input data, if it is set. If not, then the
         * date value for today will be returned.
         *
         * @return OEM date or today.
         */
        public EcvDate getOemDatumOrToday();
    }

    //--------------------------------------------------------------------------
    //
    // Support for derivativ handling
    //
    //--------------------------------------------------------------------------
    protected enum RemoveResult {
        UNCHANGED,
        CHANGED,
        CHANGED_AND_NOW_EMPTY;

    }

    protected RemoveResult removeDerivatives(EcvTableRow row, Collection<String> derivatives) {
        assert (row != null);
        assert (derivatives != null);

        SortedSet<String> derivativesInRow = DERIVATIVE.getValueNonNull(row);

        if (derivativesInRow.isEmpty() && derivatives.isEmpty()) {
            return (RemoveResult.CHANGED_AND_NOW_EMPTY);
        } else {
            if (derivativesInRow.removeAll(derivatives) == true) {
                DERIVATIVE.setValue(row, derivativesInRow);
                if (derivativesInRow.isEmpty() == true) {
                    return (RemoveResult.CHANGED_AND_NOW_EMPTY);
                } else {
                    return (RemoveResult.CHANGED);
                }
            }
        }

        return (RemoveResult.UNCHANGED);
    }

    //--------------------------------------------------------------------------
    //
    // Specialized access
    //
    //--------------------------------------------------------------------------
    final public boolean containsRejected(EcvTableData data) {
        assert (data != null);
        assert (Rq1XmlTable_CustomerResponse.class.isInstance(data.getDescription()));

        for (EcvTableRow row : data.getRows()) {
            String state = (String) row.getValueAt(CUSTOMER_STATE);
            if (state.equals(BewertungOem.ABGELEHNT) == true) {
                return (true);
            }
        }

        return (false);
    }

    final public Set<String> getAllCustomerState(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse);

        Set<String> result = new TreeSet<>();

        for (EcvTableRow row : data.getRows()) {
            String state = (String) row.getValueAt(CUSTOMER_STATE);
            result.add(state);
        }

        return (result);
    }

}
