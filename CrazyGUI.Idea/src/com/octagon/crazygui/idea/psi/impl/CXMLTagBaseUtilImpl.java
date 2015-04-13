package com.octagon.crazygui.idea.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import com.octagon.crazygui.idea.psi.CXMLTagBase;
import org.jetbrains.annotations.Nullable;

public abstract class CXMLTagBaseUtilImpl extends CXMLNamedElementImpl implements PsiNamedElement, CXMLTagBase{
    public CXMLTagBaseUtilImpl(ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    @Override
    public String getName() {
        return null;
    }

    @Override
    public PsiElement setName(String s) throws IncorrectOperationException {
        return null;
    }
}
