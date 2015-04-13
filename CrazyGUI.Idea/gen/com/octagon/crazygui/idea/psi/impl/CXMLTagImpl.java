// This is a generated file. Not intended for manual editing.
package com.octagon.crazygui.idea.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.octagon.crazygui.idea.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CXMLTagImpl extends CXMLTagUtilImpl implements CXMLTag {

  public CXMLTagImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CXMLVisitor) ((CXMLVisitor)visitor).visitTag(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<CXMLAttribute> getAttributeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CXMLAttribute.class);
  }

  @Override
  @NotNull
  public List<CXMLComment> getCommentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CXMLComment.class);
  }

  @Override
  @NotNull
  public List<CXMLSelfContainedTag> getSelfContainedTagList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CXMLSelfContainedTag.class);
  }

  @Override
  @NotNull
  public List<CXMLTag> getTagList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CXMLTag.class);
  }

  @Override
  @NotNull
  public List<CXMLTagName> getTagNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CXMLTagName.class);
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

}