package com.octagon.crazygui.idea.psi;

import com.intellij.psi.tree.IElementType;
import com.octagon.crazygui.idea.CXMLLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class CXMLElementType extends IElementType {
    public CXMLElementType(@NotNull @NonNls String debugName) {
        super(debugName, CXMLLanguage.INSTANCE);
    }
}
