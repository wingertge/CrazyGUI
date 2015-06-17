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
import com.intellij.navigation.ItemPresentation;

public class CXMLSelfContainedTagImpl extends CXMLTagBaseUtilImpl implements CXMLSelfContainedTag {

  public CXMLSelfContainedTagImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CXMLVisitor) ((CXMLVisitor)visitor).visitSelfContainedTag(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<CXMLAttribute> getAttributeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CXMLAttribute.class);
  }

  @Override
  @Nullable
  public CXMLTagName getTagName() {
    return findChildByClass(CXMLTagName.class);
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

  @Nullable
  public ItemPresentation getPresentation() {
    return CXMLPsiImplUtil.getPresentation(this);
  }

  public void navigate(boolean b) {
    CXMLPsiImplUtil.navigate(this, b);
  }

  public boolean canNavigate() {
    return CXMLPsiImplUtil.canNavigate(this);
  }

  public boolean canNavigateToSource() {
    return CXMLPsiImplUtil.canNavigateToSource(this);
  }

}
