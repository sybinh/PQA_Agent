/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import RestClient.GeneralServerDescription;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

/**
 * Class to parse the command line arguments passed to main.
 *
 * The class parses the following default parameters: user ... the username for
 * DB login password ... the password for DB login db ...the name of the RQ1
 * database that has to be used
 *
 * These default parameters are stored
 *
 * @author gug2wi
 */
public class EcvArgvParser {

    public enum Occurence {
        ONE_OPTIONAL(false, false),
        ONE_MANDATORY(true, false),
        MANY_OPTIONAL(false, true);

        final private boolean mandatory;
        final private boolean repetitionAllowed;

        private Occurence(boolean mandatory, boolean repetitionAllowed) {
            this.mandatory = mandatory;
            this.repetitionAllowed = repetitionAllowed;
        }

        boolean isMandatory() {
            return (mandatory);
        }

        public boolean isRepetitionAllowed() {
            return repetitionAllowed;
        }

    }

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(EcvArgvParser.class.getCanonicalName());

    public static class ConfigurationException extends Exception {

        public ConfigurationException(String text) {
            super(text);
        }
    }

    private static class ParameterDefinition {

        final private String name;
        final private Occurence occurence;
        final private Set<String> allowedValues;

        public ParameterDefinition(String name, Occurence occurence) {
            assert (name != null);
            assert (name.isEmpty() == false);
            assert (occurence != null);

            this.name = name;
            this.occurence = occurence;
            this.allowedValues = null;
        }

        public ParameterDefinition(String name, Occurence occurence, String... allowedValues) {
            assert (name != null);
            assert (name.isEmpty() == false);
            assert (occurence != null);

            this.name = name;
            this.occurence = occurence;
            this.allowedValues = new TreeSet<String>(Arrays.asList(allowedValues));
        }

        public String getName() {
            return name;
        }

        boolean isMandatory() {
            return occurence.isMandatory();
        }

        public boolean isRepetitionAllowed() {
            return occurence.isRepetitionAllowed();
        }

        public Set<String> getAllowedValues() {
            return allowedValues;
    }

    }

    final private List<ParameterDefinition> parameterDefinitons = new ArrayList<>();
    final private EcvMapList<String, String> parameterValues = new EcvMapList<>();
    private EcvLoginData loginData = null;

    public EcvArgvParser() {

    }

    /**
     * Add a parameter definition that shall be found in argv.
     *
     * @param name Name of the parameter.
     * @param occurence
     */
    final public void addParameterDefiniton(String name, Occurence occurence) {
        assert (name != null);
        assert (name.isEmpty() == false);
        parameterDefinitons.add(new ParameterDefinition(name, occurence));
    }

    final public void addParameterDefiniton(String name, Occurence occurence, String... allowedValues) {
        assert (name != null);
        assert (name.isEmpty() == false);
        parameterDefinitons.add(new ParameterDefinition(name, occurence, allowedValues));
    }

    /**
     * Parse the parameter definitions.
     *
     * @param args Arguments passed to main(),
     * @throws util.EcvArgvParser.ConfigurationException if parsing fails.
     */
    final public void parse(String args[]) throws ConfigurationException {
        assert (args != null);

        int argumentNumber = 0;
        String username = null;
        String password = null;
        String serverDescription = null;

        for (String arg : args) {
            argumentNumber++;

            if ((arg.contains("--") == false) || (arg.contains("=") == false)) {
                LOGGER.warning("Argument " + argumentNumber + " does not contain '='.");
                throw (new ConfigurationException("Argument " + argumentNumber + " does not contain '='."));
            }

            arg = arg.replace("--", "");
            String[] configString = arg.split("=");
            if (configString.length != 2) {
                LOGGER.warning("Argument " + argumentNumber + " is not proper defined ");
                throw new ConfigurationException("Argument " + argumentNumber + " is not proper defined ");
            }

            switch (configString[0]) {
                case "user":
                    if (username != null) {
                        LOGGER.log(Level.WARNING, "Argument {0} repeats parameter >username<.", new Object[]{argumentNumber, configString[0]});
                        throw (new ConfigurationException("Argument " + argumentNumber + " repeats parameter >username<."));
                    }
                    username = configString[1];
                    break;
                case "password":
                    if (password != null) {
                        LOGGER.log(Level.WARNING, "Argument {0} repeats parameter >password<.", new Object[]{argumentNumber, configString[0]});
                        throw (new ConfigurationException("Argument " + argumentNumber + " repeats parameter >password<."));
                    }
                    password = configString[1];
                    break;
                case "db":
                    if (serverDescription != null) {
                        LOGGER.log(Level.WARNING, "Argument {0} repeats parameter >db<.", new Object[]{argumentNumber, configString[0]});
                        throw (new ConfigurationException("Argument " + argumentNumber + " repeats parameter >db<."));
                    }
                    serverDescription = configString[1];
                    break;
                default:
                    boolean validParameterName = false;
                    for (ParameterDefinition parameterDefinition : parameterDefinitons) {
                        if (parameterDefinition.getName().equals(configString[0]) == true) {
                            validParameterName = true;
                            if (parameterDefinition.getAllowedValues() != null) {
                                if (parameterDefinition.getAllowedValues().contains(configString[1]) == false) {
                                    LOGGER.log(Level.WARNING, "Argument {0} with name {1} has invalid value >{2}<.", new Object[]{argumentNumber, configString[0], configString[1]});
                                    throw (new ConfigurationException("Argument " + argumentNumber + " with name '" + configString[0] + "' has invalid value >" + configString[1] + "<."));
                                }
                            }
                            if ((parameterDefinition.isRepetitionAllowed() == true)
                                    || (parameterValues.containsKey(configString[0]) == false)) {
                                parameterValues.add(configString[0], configString[1]);
                            } else {
                                LOGGER.log(Level.WARNING, "Argument {0} repeats parameter >{1}<.", new Object[]{argumentNumber, configString[0]});
                                throw (new ConfigurationException("Argument " + argumentNumber + " repeats parameter >" + configString[0] + "<."));
                            }
                        }
                    }
                    if (validParameterName == false) {
                        LOGGER.log(Level.WARNING, "Argument {0} has invalid parameter name >{1}<.", new Object[]{argumentNumber, configString[0]});
                        throw (new ConfigurationException("Argument " + argumentNumber + " has invalid parameter name >" + configString[0] + "<."));
                    }
            }

        }

        //
        // Check if all mandatory parameters are set
        //
        StringBuilder b = new StringBuilder();
        if (username == null) {
            b.append(", username");
        }
        if (password == null) {
            b.append(", password");
        }
        if (serverDescription == null) {
            b.append(", db");
        }

        for (ParameterDefinition parameterDefinition : parameterDefinitons) {
            if (parameterDefinition.isMandatory() == true) {
                if (parameterValues.containsKey(parameterDefinition.getName()) == false) {
                    b.append(", " + parameterDefinition.getName());
                }
            }
        }

        if (b.length() > 0) {
            LOGGER.warning("Missing parameter: " + b.toString().substring(2));
            throw (new ConfigurationException("Missing parameter: " + b.toString().substring(2)));
        }

        //
        // Check the serverDescription
        //
        GeneralServerDescription desc = GeneralServerDescription.getDescriptionByName(serverDescription);
        if (desc == null) {
            LOGGER.warning("The given server Description >" + serverDescription + "< is unknown.");
            throw (new ConfigurationException("The given server Description >" + serverDescription + "< is unknown."));
        } else {
            loginData = new EcvLoginData(username, password.toCharArray(), desc);
        }

        //
        // Log given parameters
        //
        LOGGER.info("serverdescription = " + serverDescription);
        for (Map.Entry<String, List<String>> parameterValue : parameterValues.entrySet()) {
            if (parameterValue.getValue().size() == 1) {
                LOGGER.info(parameterValue.getKey() + " = " + parameterValue.getValue().get(0));
            } else {
                LOGGER.info(parameterValue.getKey() + " = " + parameterValue.getValue());
            }
        }

    }

    /**
     * Parse the parameter definitions.
     *
     * @param args Arguments passed to main(),
     * @throws util.EcvArgvParser.ConfigurationException if parsing fails.
     */
    final public void parseByVarargs(String... args) throws ConfigurationException {
        parse(args);
    }

    /**
     * Returns the parsed login data.
     *
     * EcvArgvParser takes the login data from the parameters user, password and
     * db.
     *
     * @return
     */
    final public EcvLoginData getLoginData() {
        return (loginData);
    }

    /**
     * Returns the value for the parameter with the given name.
     *
     * @param name Name of the requested parameter.
     * @return null, if no parameter with this names was provided. Otherwise the
     * value given in the command line.
     */
    final public String getParameterValue(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        List<String> result = parameterValues.get(name);
        if (result != null) {
            return (result.get(0));
        } else {
            return (null);
        }
    }

    /**
     * Returns the list of values for a parameter
     *
     * @param name Name of the requested parameter.
     * @return A list of the values for the parameter. The list is empty if no
     * parameter of this name was set.
     */
    final public List<String> getParameterValues(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);

        List<String> result = parameterValues.get(name);
        if (result != null) {
            return (result);
        } else {
            return (new ArrayList<>());
}

    }

}
