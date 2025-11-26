/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a JSON object. A JSON object contains zero or more members. Each
 * member has a name and a value.
 *
 * @author GUG2WI
 */
public class EcvJsonObject extends EcvJsonTopLevelValue {

    public static class JsonNotFound extends Exception {

        public JsonNotFound(String nameOfSearchedMember) {
            super("No member with name '" + nameOfSearchedMember + "' found in EcvJsonObject.");
        }

        public JsonNotFound(String nameOfSearchedMember, Class acceptedClass) {
            super("No member with name '" + nameOfSearchedMember + "' and value of type " + acceptedClass.getSimpleName() + " found in EcvJsonObject.");
        }
    }

    public static class JsonWrongType extends Exception {

        public JsonWrongType(String nameOfSearchedMember, Class acceptedClass) {
            super("Member with name '" + nameOfSearchedMember + "' contains elements which are not " + acceptedClass.getSimpleName() + ".");
        }
    }

    final private List<EcvJsonMember> members;

    public EcvJsonObject() {
        members = new ArrayList<>();
    }

    public void add(EcvJsonMember member) {
        assert (member != null);
        members.add(member);
    }

    /**
     * Add a member of type EcvJsonString to the object.
     *
     * @param name Name of the member.
     * @param value Value of the member.
     */
    public void addJsonString(String name, String value) {
        add(new EcvJsonMember<EcvJsonString>(name, new EcvJsonString(value)));
    }

    /**
     * Adds an empty array as member to the object. The new array is returned so
     * that it can be filled with values.
     *
     * @param name Name of the member.
     * @return The array that was added to the object.
     */
    public EcvJsonArray addJsonArray(String name) {
        EcvJsonArray array = new EcvJsonArray();
        add(new EcvJsonMember<EcvJsonArray>(name, array));
        return (array);
    }

    public EcvJsonObject addJsonObject(String name, EcvJsonObject object) {
        add(new EcvJsonMember<EcvJsonObject>(name, object));
        return (object);
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    public List<EcvJsonMember> getMembers() {
        return (members);
    }

    @Override
    public List<EcvJsonValue> getElements() {
        List<EcvJsonValue> result = new ArrayList<>(members.size());
        for (EcvJsonMember member : members) {
            result.add(member.getValue());
        }
        return (result);
    }

    /**
     * Returns the value of the first member with the given name.
     *
     * @param name Name of the member that shall be returned.
     * @return The value for the first marching member.
     * @throws util.EcvJsonObject.JsonNotFound
     */
    public EcvJsonValue getValue(String name) throws JsonNotFound {
        assert (name != null);

        EcvJsonValue result = getValueOrNull(name);
        if (result == null) {
            throw (new JsonNotFound(name));
        }
        return (result);
    }

    /**
     * Returns the value of the first member with the given name.
     *
     * @param name Name of the member that shall be returned.
     * @return The value for the first marching member or null if no matching
     * member was found.
     */
    public EcvJsonValue getValueOrNull(String name) {
        assert (name != null);

        for (EcvJsonMember member : members) {
            if (name.equals(member.getName())) {
                return (member.value);
            }
        }

        return (null);
    }

    /**
     * Returns the value of the first member with the given name, if the value
     * type fits the given accepted class.
     *
     * @param <T_ACCEPTED> Type of the accepted value.
     * @param name Name of the member that shall be returned.
     * @param acceptedClass Class for checking against accepted class.
     * @return The value for the first marching member.
     * @throws util.EcvJsonObject.JsonNotFound
     */
    @SuppressWarnings("unchecked")
    public <T_ACCEPTED extends EcvJsonValue> T_ACCEPTED getValue(String name, Class<T_ACCEPTED> acceptedClass) throws JsonNotFound {
        assert (name != null);
        assert (acceptedClass != null);

        T_ACCEPTED result = getValueOrNull(name, acceptedClass);
        if (result == null) {
            throw (new JsonNotFound(name, acceptedClass));
        }
        return (result);
    }

    /**
     * Returns the value of the first member with the given name, if the value
     * type fits the given accepted class.
     *
     * @param <T_ACCEPTED> Type of the accepted value.
     * @param name Name of the member that shall be returned.
     * @param acceptedClass Class for checking against accepted class.
     * @return The value for the first marching member or null if no match was
     * found.
     */
    @SuppressWarnings("unchecked")
    public <T_ACCEPTED extends EcvJsonValue> T_ACCEPTED getValueOrNull(String name, Class<T_ACCEPTED> acceptedClass) {
        assert (name != null);
        assert (acceptedClass != null);

        for (EcvJsonMember member : members) {
            if (name.equals(member.getName())) {
                if (acceptedClass.isInstance(member.getValue())) {
                    return ((T_ACCEPTED) member.value);
                }
            }
        }

        return (null);
    }

    /**
     * Returns the first EcvJsonObject that matches the given name.
     *
     * @param name Name of the member for which the object shall be returned.
     * @return
     * @throws util.EcvJsonObject.JsonNotFound
     */
    public EcvJsonObject getObject(String name) throws JsonNotFound {
        return (getValue(name, EcvJsonObject.class));
    }

    /**
     * Returns the value of the first EcvJsonString that matches the given name.
     *
     * @param name Name of the member for which the value shall be returned.
     * @return
     * @throws util.EcvJsonObject.JsonNotFound
     */
    public String getString(String name) throws JsonNotFound {
        return (getValue(name, EcvJsonString.class).getValue());
    }

    /**
     * Returns the value of the first EcvJsonString that matches the given name
     * or null, if a null value was found for this name.
     *
     * @param name Name of the member for which the value shall be returned.
     * @return
     * @throws util.EcvJsonObject.JsonNotFound
     */
    public String getStringOrNullValue(String name) throws JsonNotFound {
        for (EcvJsonMember member : members) {
            if (name.equals(member.getName())) {
                EcvJsonValue value = member.getValue();
                if (value instanceof EcvJsonString) {
                    return (((EcvJsonString) value).getValue());
                } else if (value instanceof EcvJsonNull) {
                    return (null);
                }
            }
        }
        throw (new JsonNotFound(name, EcvJsonString.class));
    }

    /**
     * Returns the elements from the member array with given name and ensures
     * that this array has only elements of type T_ACCEPTED.
     *
     * @param <T_ACCEPTED>
     * @param name
     * @param acceptedClass
     * @return
     * @throws util.EcvJsonObject.JsonNotFound
     */
    @SuppressWarnings("unchecked")
    public <T_ACCEPTED extends EcvJsonValue> List<T_ACCEPTED> getAllValuesFromArray(String name, Class<T_ACCEPTED> acceptedClass) throws JsonNotFound, JsonWrongType {
        assert (name != null);
        assert (acceptedClass != null);

        List<EcvJsonValue> elements = getValue(name, EcvJsonArray.class).getElements();

        List<T_ACCEPTED> result = new ArrayList<>();
        for (EcvJsonValue element : elements) {
            if (acceptedClass.isInstance(element) == false) {
                throw (new JsonWrongType(name, acceptedClass));
            }
            result.add((T_ACCEPTED) element);
        }
        return (result);
    }

    @Override
    public String toString(String prefix) {

        if (members.isEmpty() == true) {
            return ("{}");
        }

        StringBuilder b = new StringBuilder();
        boolean isFirst = true;
        String memberPrefix = prefix + LEVEL_PREFIX;

        b.append('{');
        for (EcvJsonMember member : members) {
            if (isFirst == false) {
                b.append(",");
            }
            b.append("\n");
            b.append(memberPrefix).append(member.toString(memberPrefix));
            isFirst = false;
        }
        b.append("\n");
        b.append(prefix).append('}');

        return (b.toString());
    }

    @Override
    public String toJsonLine() {

        if (members.isEmpty() == true) {
            return ("{}");
        }

        StringBuilder b = new StringBuilder();
        boolean isFirst = true;

        b.append('{');
        for (EcvJsonMember member : members) {
            if (isFirst == false) {
                b.append(",");
            }
            b.append(member.toJsonLine());
            isFirst = false;
        }
        b.append('}');

        return (b.toString());
    }

}
