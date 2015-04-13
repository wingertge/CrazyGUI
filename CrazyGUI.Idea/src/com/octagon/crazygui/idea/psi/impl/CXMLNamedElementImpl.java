package com.octagon.crazygui.idea.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.octagon.crazygui.idea.psi.CXMLNamedElement;

public abstract class CXMLNamedElementImpl extends ASTWrapperPsiElement implements CXMLNamedElement {
    public CXMLNamedElementImpl(ASTNode astNode) {
        super(astNode);
    }
}
