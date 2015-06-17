package com.octagon.crazygui.antlr;

import com.intellij.navigation.ItemPresentation;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CXMLItemPresentation implements ItemPresentation {

    private final String name;
    private final Icon icon;

    public CXMLItemPresentation(String name, Icon icon) {

        this.name = name;
        this.icon = icon;
    }


    @Nullable
    @Override
    public String getPresentableText() {
        return name;
    }

    @Nullable
    @Override
    public String getLocationString() {
        return null;
    }

    @Nullable
    @Override
    public Icon getIcon(boolean b) {
        return icon;
    }
}
