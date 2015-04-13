// This is a generated file. Not intended for manual editing.
package com.octagon.crazygui.idea.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface CXMLTag extends CXMLTagBase {

  @NotNull
  List<CXMLAttribute> getAttributeList();

  @NotNull
  List<CXMLComment> getCommentList();

  @NotNull
  List<CXMLSelfContainedTag> getSelfContainedTagList();

  @NotNull
  List<CXMLTag> getTagList();

  @NotNull
  List<CXMLTagName> getTagNameList();

  String getName();

  PsiElement setName(String newName);

  PsiElement getNameIdentifier();

}
