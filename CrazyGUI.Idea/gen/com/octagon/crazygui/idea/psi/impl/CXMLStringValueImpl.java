// This is a generated file. Not intended for manual editing.
package com.octagon.crazygui.idea.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.octagon.crazygui.idea.psi.CXMLTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.octagon.crazygui.idea.psi.*;

public class CXMLStringValueImpl extends ASTWrapperPsiElement implements CXMLStringValue {

  public CXMLStringValueImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CXMLVisitor) ((CXMLVisitor)visitor).visitStringValue(this);
    else super.accept(visitor);
  }

}
