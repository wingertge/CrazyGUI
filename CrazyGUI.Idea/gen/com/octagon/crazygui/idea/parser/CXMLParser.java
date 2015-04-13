// This is a generated file. Not intended for manual editing.
package com.octagon.crazygui.idea.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.octagon.crazygui.idea.psi.CXMLTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class CXMLParser implements PsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == ATTRIBUTE) {
      r = attribute(b, 0);
    }
    else if (t == COMMENT) {
      r = comment(b, 0);
    }
    else if (t == ROOT_TAG) {
      r = rootTag(b, 0);
    }
    else if (t == SELF_CONTAINED_TAG) {
      r = selfContainedTag(b, 0);
    }
    else if (t == STRING_VALUE) {
      r = stringValue(b, 0);
    }
    else if (t == TAG) {
      r = tag(b, 0);
    }
    else if (t == TAG_NAME) {
      r = tagName(b, 0);
    }
    else if (t == VALUE) {
      r = value(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return cxmlFile(b, l + 1);
  }

  /* ********************************************************** */
  // (IDENTIFIER WHITE_SPACE? SEPARATOR WHITE_SPACE? value) | IDENTIFIER WHITE_SPACE? SEPARATOR WHITE_SPACE? | IDENTIFIER
  public static boolean attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = attribute_0(b, l + 1);
    if (!r) r = attribute_1(b, l + 1);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, ATTRIBUTE, r);
    return r;
  }

  // IDENTIFIER WHITE_SPACE? SEPARATOR WHITE_SPACE? value
  private static boolean attribute_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && attribute_0_1(b, l + 1);
    r = r && consumeToken(b, SEPARATOR);
    r = r && attribute_0_3(b, l + 1);
    r = r && value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // WHITE_SPACE?
  private static boolean attribute_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0_1")) return false;
    consumeToken(b, WHITE_SPACE);
    return true;
  }

  // WHITE_SPACE?
  private static boolean attribute_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_0_3")) return false;
    consumeToken(b, WHITE_SPACE);
    return true;
  }

  // IDENTIFIER WHITE_SPACE? SEPARATOR WHITE_SPACE?
  private static boolean attribute_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && attribute_1_1(b, l + 1);
    r = r && consumeToken(b, SEPARATOR);
    r = r && attribute_1_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // WHITE_SPACE?
  private static boolean attribute_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_1_1")) return false;
    consumeToken(b, WHITE_SPACE);
    return true;
  }

  // WHITE_SPACE?
  private static boolean attribute_1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "attribute_1_3")) return false;
    consumeToken(b, WHITE_SPACE);
    return true;
  }

  /* ********************************************************** */
  // OPEN_COMMENT CLOSE_COMMENT
  public static boolean comment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment")) return false;
    if (!nextTokenIs(b, OPEN_COMMENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, OPEN_COMMENT, CLOSE_COMMENT);
    exit_section_(b, m, COMMENT, r);
    return r;
  }

  /* ********************************************************** */
  // rootTag
  static boolean cxmlFile(PsiBuilder b, int l) {
    return rootTag(b, l + 1);
  }

  /* ********************************************************** */
  // tag_ | CRLF | WHITE_SPACE | comment
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = tag_(b, l + 1);
    if (!r) r = consumeToken(b, CRLF);
    if (!r) r = consumeToken(b, WHITE_SPACE);
    if (!r) r = comment(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LESS_THAN ROOT attribute* GREATER_THAN item_* OPEN_CLOSING_TAG ROOT GREATER_THAN
  public static boolean rootTag(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rootTag")) return false;
    if (!nextTokenIs(b, LESS_THAN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LESS_THAN, ROOT);
    r = r && rootTag_2(b, l + 1);
    r = r && consumeToken(b, GREATER_THAN);
    r = r && rootTag_4(b, l + 1);
    r = r && consumeTokens(b, 0, OPEN_CLOSING_TAG, ROOT, GREATER_THAN);
    exit_section_(b, m, ROOT_TAG, r);
    return r;
  }

  // attribute*
  private static boolean rootTag_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rootTag_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!attribute(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "rootTag_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // item_*
  private static boolean rootTag_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rootTag_4")) return false;
    int c = current_position_(b);
    while (true) {
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "rootTag_4", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // LESS_THAN WHITE_SPACE? tagName attribute* WHITE_SPACE? CLOSE_TAG | LESS_THAN WHITE_SPACE? tagName WHITE_SPACE? attribute* | (LESS_THAN WHITE_SPACE?)
  public static boolean selfContainedTag(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag")) return false;
    if (!nextTokenIs(b, LESS_THAN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = selfContainedTag_0(b, l + 1);
    if (!r) r = selfContainedTag_1(b, l + 1);
    if (!r) r = selfContainedTag_2(b, l + 1);
    exit_section_(b, m, SELF_CONTAINED_TAG, r);
    return r;
  }

  // LESS_THAN WHITE_SPACE? tagName attribute* WHITE_SPACE? CLOSE_TAG
  private static boolean selfContainedTag_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LESS_THAN);
    r = r && selfContainedTag_0_1(b, l + 1);
    r = r && tagName(b, l + 1);
    r = r && selfContainedTag_0_3(b, l + 1);
    r = r && selfContainedTag_0_4(b, l + 1);
    r = r && consumeToken(b, CLOSE_TAG);
    exit_section_(b, m, null, r);
    return r;
  }

  // WHITE_SPACE?
  private static boolean selfContainedTag_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag_0_1")) return false;
    consumeToken(b, WHITE_SPACE);
    return true;
  }

  // attribute*
  private static boolean selfContainedTag_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag_0_3")) return false;
    int c = current_position_(b);
    while (true) {
      if (!attribute(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "selfContainedTag_0_3", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // WHITE_SPACE?
  private static boolean selfContainedTag_0_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag_0_4")) return false;
    consumeToken(b, WHITE_SPACE);
    return true;
  }

  // LESS_THAN WHITE_SPACE? tagName WHITE_SPACE? attribute*
  private static boolean selfContainedTag_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LESS_THAN);
    r = r && selfContainedTag_1_1(b, l + 1);
    r = r && tagName(b, l + 1);
    r = r && selfContainedTag_1_3(b, l + 1);
    r = r && selfContainedTag_1_4(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // WHITE_SPACE?
  private static boolean selfContainedTag_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag_1_1")) return false;
    consumeToken(b, WHITE_SPACE);
    return true;
  }

  // WHITE_SPACE?
  private static boolean selfContainedTag_1_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag_1_3")) return false;
    consumeToken(b, WHITE_SPACE);
    return true;
  }

  // attribute*
  private static boolean selfContainedTag_1_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag_1_4")) return false;
    int c = current_position_(b);
    while (true) {
      if (!attribute(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "selfContainedTag_1_4", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // LESS_THAN WHITE_SPACE?
  private static boolean selfContainedTag_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LESS_THAN);
    r = r && selfContainedTag_2_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // WHITE_SPACE?
  private static boolean selfContainedTag_2_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "selfContainedTag_2_1")) return false;
    consumeToken(b, WHITE_SPACE);
    return true;
  }

  /* ********************************************************** */
  // STRINGVALUE | CODEVALUE
  public static boolean stringValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stringValue")) return false;
    if (!nextTokenIs(b, "<string value>", CODEVALUE, STRINGVALUE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<string value>");
    r = consumeToken(b, STRINGVALUE);
    if (!r) r = consumeToken(b, CODEVALUE);
    exit_section_(b, l, m, STRING_VALUE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LESS_THAN WHITE_SPACE? tagName attribute* GREATER_THAN item_* (OPEN_CLOSING_TAG tagName GREATER_THAN | OPEN_CLOSING_TAG tagName | OPEN_CLOSING_TAG | LESS_THAN)
  public static boolean tag(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag")) return false;
    if (!nextTokenIs(b, LESS_THAN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LESS_THAN);
    r = r && tag_1(b, l + 1);
    r = r && tagName(b, l + 1);
    r = r && tag_3(b, l + 1);
    r = r && consumeToken(b, GREATER_THAN);
    r = r && tag_5(b, l + 1);
    r = r && tag_6(b, l + 1);
    exit_section_(b, m, TAG, r);
    return r;
  }

  // WHITE_SPACE?
  private static boolean tag_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_1")) return false;
    consumeToken(b, WHITE_SPACE);
    return true;
  }

  // attribute*
  private static boolean tag_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_3")) return false;
    int c = current_position_(b);
    while (true) {
      if (!attribute(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tag_3", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // item_*
  private static boolean tag_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_5")) return false;
    int c = current_position_(b);
    while (true) {
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tag_5", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // OPEN_CLOSING_TAG tagName GREATER_THAN | OPEN_CLOSING_TAG tagName | OPEN_CLOSING_TAG | LESS_THAN
  private static boolean tag_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = tag_6_0(b, l + 1);
    if (!r) r = tag_6_1(b, l + 1);
    if (!r) r = consumeToken(b, OPEN_CLOSING_TAG);
    if (!r) r = consumeToken(b, LESS_THAN);
    exit_section_(b, m, null, r);
    return r;
  }

  // OPEN_CLOSING_TAG tagName GREATER_THAN
  private static boolean tag_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPEN_CLOSING_TAG);
    r = r && tagName(b, l + 1);
    r = r && consumeToken(b, GREATER_THAN);
    exit_section_(b, m, null, r);
    return r;
  }

  // OPEN_CLOSING_TAG tagName
  private static boolean tag_6_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_6_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPEN_CLOSING_TAG);
    r = r && tagName(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean tagName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tagName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, TAG_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // selfContainedTag | tag
  static boolean tag_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_")) return false;
    if (!nextTokenIs(b, LESS_THAN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = selfContainedTag(b, l + 1);
    if (!r) r = tag(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // stringValue | INTVALUE
  public static boolean value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<value>");
    r = stringValue(b, l + 1);
    if (!r) r = consumeToken(b, INTVALUE);
    exit_section_(b, l, m, VALUE, r, false, null);
    return r;
  }

}
