// This is a generated file. Not intended for manual editing.
package com.octagon.crazygui.idea.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.octagon.crazygui.idea.psi.impl.*;

public interface CXMLTypes {

  IElementType ATTRIBUTE = new CXMLElementType("ATTRIBUTE");
  IElementType COMMENT = new CXMLElementType("COMMENT");
  IElementType ROOT_TAG = new CXMLElementType("ROOT_TAG");
  IElementType SELF_CONTAINED_TAG = new CXMLElementType("SELF_CONTAINED_TAG");
  IElementType STRING_VALUE = new CXMLElementType("STRING_VALUE");
  IElementType TAG = new CXMLElementType("TAG");
  IElementType TAG_NAME = new CXMLElementType("TAG_NAME");
  IElementType VALUE = new CXMLElementType("VALUE");

  IElementType CLOSE_COMMENT = new CXMLTokenType("CLOSE_COMMENT");
  IElementType CLOSE_TAG = new CXMLTokenType("CLOSE_TAG");
  IElementType CODEVALUE = new CXMLTokenType("CODEVALUE");
  IElementType CRLF = new CXMLTokenType("CRLF");
  IElementType GREATER_THAN = new CXMLTokenType("GREATER_THAN");
  IElementType IDENTIFIER = new CXMLTokenType("IDENTIFIER");
  IElementType INTVALUE = new CXMLTokenType("INTVALUE");
  IElementType LESS_THAN = new CXMLTokenType("LESS_THAN");
  IElementType OPEN_CLOSING_TAG = new CXMLTokenType("OPEN_CLOSING_TAG");
  IElementType OPEN_COMMENT = new CXMLTokenType("OPEN_COMMENT");
  IElementType ROOT = new CXMLTokenType("ROOT");
  IElementType SEPARATOR = new CXMLTokenType("SEPARATOR");
  IElementType STRINGVALUE = new CXMLTokenType("STRINGVALUE");
  IElementType WHITE_SPACE = new CXMLTokenType("WHITE_SPACE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == ATTRIBUTE) {
        return new CXMLAttributeImpl(node);
      }
      else if (type == COMMENT) {
        return new CXMLCommentImpl(node);
      }
      else if (type == ROOT_TAG) {
        return new CXMLRootTagImpl(node);
      }
      else if (type == SELF_CONTAINED_TAG) {
        return new CXMLSelfContainedTagImpl(node);
      }
      else if (type == STRING_VALUE) {
        return new CXMLStringValueImpl(node);
      }
      else if (type == TAG) {
        return new CXMLTagImpl(node);
      }
      else if (type == TAG_NAME) {
        return new CXMLTagNameImpl(node);
      }
      else if (type == VALUE) {
        return new CXMLValueImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
