package com.octagon.crazygui.idea;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class CXMLFileType extends LanguageFileType {
    public static final CXMLFileType INSTANCE = new CXMLFileType();

    public CXMLFileType() {
        super(CXMLLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "CXML File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "CXML Gui Description File";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "cxml";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return CrazyGUIIcons.FILE;
    }
}
