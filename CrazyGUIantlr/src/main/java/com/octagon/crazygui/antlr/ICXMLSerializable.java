package com.octagon.crazygui.antlr;

public interface ICXMLSerializable<T> {
    T deserialize(String string);
    String serialize(T value);
}
