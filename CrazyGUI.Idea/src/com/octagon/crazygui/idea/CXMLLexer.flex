package com.octagon.crazygui.idea;
import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static com.octagon.crazygui.idea.psi.CXMLTypes.*;

%%

%{
  public CXMLLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class CXMLLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL="\r"|"\n"|"\r\n"
LINE_WS=[\ \t\f]
WHITE_SPACE=({LINE_WS}|{EOL})+

ROOT=[rR][oO][oO][tT]
STRINGVALUE=\"[a-zA-Z0-9\.\(\)\s]*\"
INTVALUE=[0-9]+
CODEVALUE=\"\{[a-zA-Z0-9\.\(\)]*\}\"
IDENTIFIER=([a-zA-Z]+:)?[a-zA-Z][a-zA-Z0-9]*
WHITE_SPACE=[ \t\n\x0B\f\r]+

%%
<YYINITIAL> {
  {WHITE_SPACE}         { return com.intellij.psi.TokenType.WHITE_SPACE; }

  "\\n|\\r|\\r\\n"      { return CRLF; }
  "="                   { return SEPARATOR; }
  ">"                   { return GREATER_THAN; }
  "<"                   { return LESS_THAN; }
  "/>"                  { return CLOSE_TAG; }
  "</"                  { return OPEN_CLOSING_TAG; }
  "<!--"                { return OPEN_COMMENT; }
  "-->"                 { return CLOSE_COMMENT; }

  {ROOT}                { return ROOT; }
  {STRINGVALUE}         { return STRINGVALUE; }
  {INTVALUE}            { return INTVALUE; }
  {CODEVALUE}           { return CODEVALUE; }
  {IDENTIFIER}          { return IDENTIFIER; }
  {WHITE_SPACE}         { return WHITE_SPACE; }

  [^] { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}
