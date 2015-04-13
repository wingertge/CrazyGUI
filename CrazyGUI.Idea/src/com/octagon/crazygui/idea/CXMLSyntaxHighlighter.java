package com.octagon.crazygui.idea;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.octagon.crazygui.idea.psi.CXMLTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class CXMLSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey SEPARATOR = createTextAttributesKey("CXML_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey IDENTIFIER = createTextAttributesKey("CXML_NAME", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey ATTRIBUTE_COMPILETIME_KEY = createTextAttributesKey("CXML_COMPILETIME_KEY", DefaultLanguageHighlighterColors.STATIC_FIELD);
    public static final TextAttributesKey ATTRIBUTE_NAME = createTextAttributesKey("CXML_ATTRIBUTE_NAME", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
    public static final TextAttributesKey ATTRIBUTE_STRINGVALUE = createTextAttributesKey("CXML_STRINGVALUE", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey ATTRIBUTE_INTVALUE = createTextAttributesKey("CXML_INTVALUE", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey ATTRIBUTE_CODEVALUE = createTextAttributesKey("CXML_CODEVALUE", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey COMMENT = createTextAttributesKey("CXML_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);

    static final TextAttributesKey BAD_CHARACTER = createTextAttributesKey("CXML_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] SEPARATOR_KEYS = new TextAttributesKey[]{SEPARATOR};
    private static final TextAttributesKey[] IDENTIFIER_KEYS = new TextAttributesKey[]{IDENTIFIER};
    private static final TextAttributesKey[] ATTRIBUTE_STRINGVALUE_KEYS = new TextAttributesKey[]{ATTRIBUTE_STRINGVALUE};
    private static final TextAttributesKey[] ATTRIBUTE_INTVALUE_KEYS = new TextAttributesKey[]{ATTRIBUTE_INTVALUE};
    private static final TextAttributesKey[] ATTRIBUTE_CODEVALUE_KEYS = new TextAttributesKey[]{ATTRIBUTE_CODEVALUE};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];


    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlexAdapter(new CXMLLexer());
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if(tokenType.equals(CXMLTypes.COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        } else if (tokenType.equals(CXMLTypes.SEPARATOR)) {
            return SEPARATOR_KEYS;
        } else if (tokenType.equals(CXMLTypes.IDENTIFIER) || tokenType.equals(CXMLTypes.ROOT)) {
            return IDENTIFIER_KEYS;
        } else if (tokenType.equals(CXMLTypes.STRINGVALUE)) {
            return ATTRIBUTE_STRINGVALUE_KEYS;
        } else if(tokenType.equals(CXMLTypes.INTVALUE)) {
            return ATTRIBUTE_INTVALUE_KEYS;
        } else if(tokenType.equals(CXMLTypes.CODEVALUE)) {
            return ATTRIBUTE_CODEVALUE_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}
