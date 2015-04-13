package com.octagon.crazygui.idea.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface CXMLTagBase extends CXMLNamedElement {
    @NotNull
    List<CXMLAttribute> getAttributeList();

    @Nullable
    CXMLTagName getTagName();
}
