/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ProPlaTo;

import DataModel.Xml.DmXmlMappedContainerElementI;
import DataModel.DmConstantField_Text;
import DataModel.DmElementField_ReadOnlyI;
import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import util.EcvXmlContainerElement;

/**
 *
 * @author GUG2WI
 */
public class DmProPlaTo_Aenderung extends DmProPlaTo_Element implements DmXmlMappedContainerElementI {

    final public DmConstantField_Text VORGANGSNUMMER_LIEFERANT;
    final public DmConstantField_Text ISSUE_FD;
    final public DmConstantField_Text ISSUE_KOMMENTAR;
    final public DmConstantField_Text VORGANGSNUMMER_OEM;
    final public DmConstantField_Text EINPLANUNGSSTATUS;
    final public DmConstantField_Text ANGEFORDERT;
    final public DmConstantField_Text PAKET_ID;
    final public DmConstantField_Text PAKET;
    final public DmConstantField_Text PAKET_LANGBEZEICHNUNG;
    final public DmConstantField_Text PAKET_VERSION;
    final public DmConstantField_Text PAKET_VERSION_VORGAENGER_ID;
    final public DmConstantField_Text PAKET_VERSION_VORGAENGER;
    final public DmConstantField_Text FUNKTION_ID;
    final public DmConstantField_Text FUNKTION;
    final public DmConstantField_Text FUNKTION_LANGBEZEICHNUNG;
    final public DmConstantField_Text FUNKTIONSVERSION;
    final public DmConstantField_Text NEUTRALBEDATUNG;
    final public DmConstantField_Text ERSTBEDATUNG;
    final public DmConstantField_Text HEXNEUTRAL;
    final public DmConstantField_Text APPL_VERANTW_LIEFERANT;

    final public DmElementField_ReadOnlyI<DmRq1ElementInterface> VORGANG_ELEMENT;
    final public DmElementField_ReadOnlyI<DmRq1ElementInterface> ISSUE_FD_ELEMENT;
    final public DmElementField_ReadOnlyI<DmRq1ElementInterface> PAKET_ELEMENT;
    final public DmElementField_ReadOnlyI<DmRq1ElementInterface> PAKET_VORGAENGER_ELEMENT;
    final public DmElementField_ReadOnlyI<DmRq1ElementInterface> FUNKTION_ELEMENT;

    DmProPlaTo_Aenderung(EcvXmlContainerElement aenderung) {
        super("Änderung");
        assert (aenderung != null);

        addField(VORGANGSNUMMER_LIEFERANT = createTextField(aenderung, "VorgangsnummerLieferant", "Vorgangsnummer Lieferant"));
        addField(ISSUE_FD = createTextField(aenderung, "IssueFD", "Issue FD ID"));
        addField(ISSUE_KOMMENTAR = createTextField(aenderung, "IssueKommentar", "Issue Kommentar"));
        addField(VORGANGSNUMMER_OEM = createTextField(aenderung, "VorgangsnummerOEM", "Vorgangsnummer OEM"));
        addField(EINPLANUNGSSTATUS = createTextField(aenderung, "Einplanungsstatus", "Einplanungsstatus"));
        addField(ANGEFORDERT = createTextField(aenderung, "Angefordert", "Angefordert"));
        addField(PAKET_ID = createTextField(aenderung, "PaketID", "Paket ID"));
        addField(PAKET = createTextField(aenderung, "Paket", "Paketname"));
        addField(PAKET_LANGBEZEICHNUNG = createTextField(aenderung, "PaketLangbezeichnung", "Langbezeichnung Paket"));
        addField(PAKET_VERSION = createTextField(aenderung, "Paketversion", "Paketversion"));
        addField(PAKET_VERSION_VORGAENGER_ID = createTextField(aenderung, "PaketversionVorgaengerID", "Paket ID Vorgänger"));
        addField(PAKET_VERSION_VORGAENGER = createTextField(aenderung, "PaketversionVorgaenger", "Paketversion Vorgänger"));
        addField(FUNKTION_ID = createTextField(aenderung, "FunktionID", "Funktion ID"));
        addField(FUNKTION = createTextField(aenderung, "Funktion", "Funktionsname"));
        addField(FUNKTION_LANGBEZEICHNUNG = createTextField(aenderung, "FunktionLangbezeichnung", "Langbezeichnung Funktion"));
        addField(FUNKTIONSVERSION = createTextField(aenderung, "Funktionsversion", "Funktionsversion"));
        addField(NEUTRALBEDATUNG = createTextField(aenderung, "Neutralbedatung", "Neutralbedatung"));
        addField(ERSTBEDATUNG = createTextField(aenderung, "Erstbedatung", "Erstbedatung"));
        addField(HEXNEUTRAL = createTextField(aenderung, "Hexneutral", "Hexneutral"));
        addField(APPL_VERANTW_LIEFERANT = createTextField(aenderung, "ApplVerantwLieferant", "Appl. Verantwortlicher Lieferant"));

        addField(VORGANG_ELEMENT = new DmProPlaTo_Rq1ElementField(VORGANGSNUMMER_LIEFERANT, "Vorgang Lieferant"));
        addField(ISSUE_FD_ELEMENT = new DmProPlaTo_Rq1ElementField(ISSUE_FD, "Issue FD"));
        addField(PAKET_ELEMENT = new DmProPlaTo_Rq1ElementField(PAKET_ID, "Paket"));
        addField(PAKET_VORGAENGER_ELEMENT = new DmProPlaTo_Rq1ElementField(PAKET_VERSION_VORGAENGER_ID, "Paket Vorgänger"));
        addField(FUNKTION_ELEMENT = new DmProPlaTo_Rq1ElementField(FUNKTION_ID, "Funktion"));
    }

    @Override
    public String getTitle() {
        return (VORGANGSNUMMER_LIEFERANT.getValueAsText());
    }

    @Override
    public String getId() {
        return (VORGANGSNUMMER_LIEFERANT.getValueAsText());
    }

    @Override
    public DmElementI getMappingElement() {
        return (mappingRq1Element(VORGANGSNUMMER_LIEFERANT));
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + VORGANGSNUMMER_LIEFERANT.getValueAsText() + " " + VORGANGSNUMMER_OEM.getValueAsText());
    }

}
