package com.octagon.crazygui.idea.psi.impl;

import com.intellij.lang.ASTNode;
import com.octagon.crazygui.idea.psi.CXMLTag;
import com.octagon.crazygui.idea.psi.CXMLTagBase;
import com.octagon.crazygui.idea.psi.CXMLTagName;
import org.jetbrains.annotations.Nullable;

public abstract class CXMLTagUtilImpl extends CXMLTagBaseUtilImpl implements CXMLTagBase, CXMLTag {
    public CXMLTagUtilImpl(ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    @Override
    public CXMLTagName getTagName() {
        return getTagNameList().size() != 0 ? getTagNameList().get(0) : null;
    }
}
