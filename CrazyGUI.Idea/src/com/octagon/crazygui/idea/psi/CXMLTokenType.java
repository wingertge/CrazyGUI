package com.octagon.crazygui.idea.psi;

import com.intellij.psi.tree.IElementType;
import com.octagon.crazygui.idea.CXMLLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CXMLTokenType extends IElementType {
    public CXMLTokenType(@NotNull @NonNls String debugName) {
        super(debugName, CXMLLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "CXMLTokenType." + super.toString();
    }
}
