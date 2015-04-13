// This is a generated file. Not intended for manual editing.
package com.octagon.crazygui.idea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CXMLSelfContainedTag extends CXMLTagBase {

  @NotNull
  List<CXMLAttribute> getAttributeList();

  @Nullable
  CXMLTagName getTagName();

  String getName();

  PsiElement setName(String newName);

  PsiElement getNameIdentifier();

}
