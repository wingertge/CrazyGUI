// This is a generated file. Not intended for manual editing.
package com.octagon.crazygui.idea.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class CXMLVisitor extends PsiElementVisitor {

  public void visitAttribute(@NotNull CXMLAttribute o) {
    visitNamedElement(o);
  }

  public void visitComment(@NotNull CXMLComment o) {
    visitPsiElement(o);
  }

  public void visitRootTag(@NotNull CXMLRootTag o) {
    visitTagBase(o);
  }

  public void visitSelfContainedTag(@NotNull CXMLSelfContainedTag o) {
    visitTagBase(o);
  }

  public void visitStringValue(@NotNull CXMLStringValue o) {
    visitPsiElement(o);
  }

  public void visitTag(@NotNull CXMLTag o) {
    visitTagBase(o);
  }

  public void visitTagName(@NotNull CXMLTagName o) {
    visitPsiElement(o);
  }

  public void visitValue(@NotNull CXMLValue o) {
    visitPsiElement(o);
  }

  public void visitNamedElement(@NotNull CXMLNamedElement o) {
    visitPsiElement(o);
  }

  public void visitTagBase(@NotNull CXMLTagBase o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
