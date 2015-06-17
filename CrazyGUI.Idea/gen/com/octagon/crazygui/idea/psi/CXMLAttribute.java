// This is a generated file. Not intended for manual editing.
package com.octagon.crazygui.idea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface CXMLAttribute extends CXMLNamedElement {

  @Nullable
  CXMLValue getValue();

  String getName();

  PsiElement setName(String newName);

  PsiElement getNameIdentifier();

  String getAttributeName();

  @Nullable
  ItemPresentation getPresentation();

  boolean canNavigate();

  boolean canNavigateToSource();

  void navigate(boolean b);

}
