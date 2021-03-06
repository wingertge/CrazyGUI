// Generated from E:/Dropbox/IdeaProjects/mods/CrazyGUI/src/main/antlr\CrazyGUI.g4 by ANTLR 4.5
package com.octagon.crazygui.antlr;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CrazyGUILexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, NAME=6, WS=7, PREFIX=8, STRINGVALUE=9, 
		INTVALUE=10;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "NAME", "WS", "PREFIX", "STRINGVALUE", 
		"INTVALUE"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'<'", "'>'", "'</'", "'/>'", "'='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, "NAME", "WS", "PREFIX", "STRINGVALUE", 
		"INTVALUE"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override
	@NotNull
	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CrazyGUILexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CrazyGUI.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\fA\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\7\3\7\7\7&\n\7\f"+
		"\7\16\7)\13\7\3\b\6\b,\n\b\r\b\16\b-\3\b\3\b\3\t\3\t\3\t\3\n\3\n\6\n\67"+
		"\n\n\r\n\16\n8\3\n\3\n\3\13\6\13>\n\13\r\13\16\13?\38\2\f\3\3\5\4\7\5"+
		"\t\6\13\7\r\b\17\t\21\n\23\13\25\f\3\2\7\4\2C\\c|\5\2\62;C\\c|\5\2\13"+
		"\f\17\17\"\"\4\2ZZzz\3\2\62;D\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\3\27\3\2\2\2\5\31\3\2\2\2\7\33\3\2\2\2\t\36\3\2\2\2\13"+
		"!\3\2\2\2\r#\3\2\2\2\17+\3\2\2\2\21\61\3\2\2\2\23\64\3\2\2\2\25=\3\2\2"+
		"\2\27\30\7>\2\2\30\4\3\2\2\2\31\32\7@\2\2\32\6\3\2\2\2\33\34\7>\2\2\34"+
		"\35\7\61\2\2\35\b\3\2\2\2\36\37\7\61\2\2\37 \7@\2\2 \n\3\2\2\2!\"\7?\2"+
		"\2\"\f\3\2\2\2#\'\t\2\2\2$&\t\3\2\2%$\3\2\2\2&)\3\2\2\2\'%\3\2\2\2\'("+
		"\3\2\2\2(\16\3\2\2\2)\'\3\2\2\2*,\t\4\2\2+*\3\2\2\2,-\3\2\2\2-+\3\2\2"+
		"\2-.\3\2\2\2./\3\2\2\2/\60\b\b\2\2\60\20\3\2\2\2\61\62\t\5\2\2\62\63\7"+
		"<\2\2\63\22\3\2\2\2\64\66\7$\2\2\65\67\13\2\2\2\66\65\3\2\2\2\678\3\2"+
		"\2\289\3\2\2\28\66\3\2\2\29:\3\2\2\2:;\7$\2\2;\24\3\2\2\2<>\t\6\2\2=<"+
		"\3\2\2\2>?\3\2\2\2?=\3\2\2\2?@\3\2\2\2@\26\3\2\2\2\7\2\'-8?\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}