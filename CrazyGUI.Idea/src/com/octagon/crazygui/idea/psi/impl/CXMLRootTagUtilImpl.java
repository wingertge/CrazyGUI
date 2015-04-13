package com.octagon.crazygui.idea.psi.impl;

import com.intellij.lang.ASTNode;
import com.octagon.crazygui.idea.psi.CXMLRootTag;
import com.octagon.crazygui.idea.psi.CXMLTagBase;
import com.octagon.crazygui.idea.psi.CXMLTagName;
import org.jetbrains.annotations.Nullable;

public abstract class CXMLRootTagUtilImpl extends CXMLTagBaseUtilImpl implements CXMLTagBase, CXMLRootTag {
    public CXMLRootTagUtilImpl(ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    @Override
    public CXMLTagName getTagName() {
        return null;
    }
}
