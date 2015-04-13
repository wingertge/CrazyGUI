// This is a generated file. Not intended for manual editing.
package com.octagon.crazygui.idea.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.octagon.crazygui.idea.psi.CXMLTypes.*;
import com.octagon.crazygui.idea.psi.*;

public class CXMLAttributeImpl extends CXMLNamedElementImpl implements CXMLAttribute {

  public CXMLAttributeImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CXMLVisitor) ((CXMLVisitor)visitor).visitAttribute(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public CXMLValue getValue() {
    return findChildByClass(CXMLValue.class);
  }

  public String getName() {
    return CXMLPsiImplUtil.getName(this);
  }

  public PsiElement setName(String newName) {
    return CXMLPsiImplUtil.setName(this, newName);
  }

  public PsiElement getNameIdentifier() {
    return CXMLPsiImplUtil.getNameIdentifier(this);
  }

  public String getAttributeName() {
    return CXMLPsiImplUtil.getAttributeName(this);
  }

}
