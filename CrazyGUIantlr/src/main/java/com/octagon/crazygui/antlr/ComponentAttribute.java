package com.octagon.crazygui.antlr;

import java.util.List;

public class ComponentAttribute {
    private ICXMLSerializable parser;
    private final String name;
    private Object value;
    private boolean dirty;

    public ComponentAttribute(ICXMLSerializable parser, String name, String value) {
        this.parser = parser;
        this.name = name;
        this.value = parser.deserialize(value);
    }

    public ComponentAttribute(Object value, ICXMLSerializable parser, String name) {
        this.parser = parser;
        this.name = name;
        this.value = value;
    }

    public String getValueString() {
        return parser.serialize(value);
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setValueString(String valueString) {
        this.value = parser.deserialize(valueString.replace("\"", ""));
    }

    public static ComponentAttribute get(List<ComponentAttribute> tagAttributes, String name) {
        return tagAttributes.stream().filter(a -> a.getName().equals(name)).findFirst().orElse(null);
    }

    public ICXMLSerializable getParser() {
        return parser;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void markClean() {
        this.dirty = false;
    }
}
