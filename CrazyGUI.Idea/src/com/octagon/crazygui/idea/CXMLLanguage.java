package com.octagon.crazygui.idea;

import com.intellij.lang.Language;

public class CXMLLanguage extends Language {
    public static final CXMLLanguage INSTANCE = new CXMLLanguage();

    protected CXMLLanguage() {
        super("CXML");
    }
}
