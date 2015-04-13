package com.octagon.crazygui.idea.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.octagon.crazygui.idea.CXMLFileType;
import com.octagon.crazygui.idea.CXMLLanguage;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CXMLFile extends PsiFileBase {
    public CXMLFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, CXMLLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return CXMLFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "CXML File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}
