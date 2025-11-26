/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import OslcAccess.Rq1.OslcRq1Client;
import OslcAccess.Rq1.OslcRq1ServerDescription;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1NodeDescription;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlParser;
import util.EcvXmlTextElement;

/**
 *
 * @author gug2wi
 */
public abstract class Rq1Metadata extends Rq1Record implements Rq1NodeInterface {

    final private static Logger LOGGER = Logger.getLogger(Rq1Metadata.class.getCanonicalName());

    static private Map<String, Rq1Metadata> cacheForMetadata = null;

    final static public String FIELDNAME_NAME = "Name";

    final public Rq1DatabaseField_Text NAME;
    final public Rq1DatabaseField_Text VALUE;

    public Rq1Metadata(Rq1NodeDescription recordDescription) {
        super(recordDescription);

        addField(NAME = new Rq1DatabaseField_Text(this, FIELDNAME_NAME));
        addField(VALUE = new Rq1DatabaseField_Text(this, "Value"));
        NAME.setReadOnly();
        VALUE.setReadOnly();
    }

    @Override
    final public void reload() {
        // ignored
    }

    /**
     * Returns the latest version/revision of the meta data with the given name.
     *
     * @param name Name of the meta data.
     * @return Latest version of the meta data or null if no record with the
     * given name exists.
     */
    static protected Rq1Metadata getMetaData(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        if (cacheForMetadata == null) {
            OslcRq1ServerDescription db = OslcRq1Client.getCurrentDatabase();
            if (db == null) {
                return (null);
            }
            String fileName = "Rq1Metadata_" + db.getServerNameForConfigFile() + ".xml";
            cacheForMetadata = loadMetadataFromFile(fileName);
            assert (cacheForMetadata != null) : fileName;
        }

        if (cacheForMetadata.containsKey(name) == true) {
            return (cacheForMetadata.get(name));
        }

        return (null);
    }

    static private Map<String, Rq1Metadata> loadMetadataFromFile(String fileName) {
        assert (fileName != null);
        assert (fileName.isEmpty() == false);

        InputStream iStream = Rq1Metadata.class.getResourceAsStream(fileName);
        if (iStream == null) {
            LOGGER.severe("Could not open " + fileName);
            return (null);
        }
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(iStream, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOGGER.log(Level.SEVERE, "Parsing of " + fileName + " failed.", ex);
            ToolUsageLogger.logError(Rq1Metadata.class.getCanonicalName(), ex);
            return (null);
        }

        EcvXmlParser parser = new EcvXmlParser(reader);
        EcvXmlElement root;
        try {
            root = parser.parse();
        } catch (EcvXmlParser.ParseException ex) {
            LOGGER.log(Level.SEVERE, "Parsing of " + fileName + " failed.", ex);
            ToolUsageLogger.logError(Rq1Metadata.class.getCanonicalName(), ex);
            return (null);
        }

        Map<String, Rq1Metadata> result = new TreeMap<>();
        if (root instanceof EcvXmlContainerElement) {
            for (EcvXmlElement choicelist : ((EcvXmlContainerElement) root).getElementList("Choicelist")) {
                if (choicelist instanceof EcvXmlTextElement) {
                    String key = choicelist.getAttribute("record") + "." + choicelist.getAttribute("field");
                    String id = choicelist.getAttribute("id");
                    String content = ((EcvXmlTextElement) choicelist).getText();
                    Rq1MetadataChoiceList metadata = new Rq1MetadataChoiceList();
                    metadata.NAME.setOslcValue(id);
                    metadata.VALUE.setOslcValue(content);

                    result.put(key, metadata);
                }
            }
        }
        return (result);
    }

}
