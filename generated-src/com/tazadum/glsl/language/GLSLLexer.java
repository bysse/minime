// Generated from GLSL.g4 by ANTLR 4.7.1
package com.tazadum.glsl.language;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GLSLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ATTRIBUTE=1, BOOL=2, BREAK=3, BVEC2=4, BVEC3=5, BVEC4=6, CONST=7, CONTINUE=8, 
		DISCARD=9, DO=10, ELSE=11, FLOAT=12, FOR=13, HIGH_PRECISION=14, IF=15, 
		IN=16, INOUT=17, INT=18, INVARIANT=19, IVEC2=20, IVEC3=21, IVEC4=22, LOW_PRECISION=23, 
		MAT2=24, MAT3=25, MAT4=26, MEDIUM_PRECISION=27, OUT=28, PRECISION=29, 
		RETURN=30, SAMPLER2D=31, SAMPLERCUBE=32, STRUCT=33, UNIFORM=34, VARYING=35, 
		VEC2=36, VEC3=37, VEC4=38, VOID=39, WHILE=40, IDENTIFIER=41, FLOATCONSTANT=42, 
		INTCONSTANT=43, BOOLCONSTANT=44, INC_OP=45, DEC_OP=46, LE_OP=47, GE_OP=48, 
		EQ_OP=49, NE_OP=50, AND_OP=51, OR_OP=52, XOR_OP=53, MUL_ASSIGN=54, DIV_ASSIGN=55, 
		ADD_ASSIGN=56, MOD_ASSIGN=57, SUB_ASSIGN=58, LEFT_PAREN=59, RIGHT_PAREN=60, 
		LEFT_BRACKET=61, RIGHT_BRACKET=62, LEFT_BRACE=63, RIGHT_BRACE=64, DOT=65, 
		COMMA=66, COLON=67, EQUAL=68, SEMICOLON=69, BANG=70, DASH=71, TILDE=72, 
		PLUS=73, STAR=74, SLASH=75, PERCENT=76, LEFT_ANGLE=77, RIGHT_ANGLE=78, 
		VERTICAL_BAR=79, CARET=80, AMPERSAND=81, QUESTION=82, WHITESPACE=83, COMMENT=84, 
		MULTILINE_COMMENT=85;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"ATTRIBUTE", "BOOL", "BREAK", "BVEC2", "BVEC3", "BVEC4", "CONST", "CONTINUE", 
		"DISCARD", "DO", "ELSE", "FALSE", "FLOAT", "FOR", "HIGH_PRECISION", "IF", 
		"IN", "INOUT", "INT", "INVARIANT", "IVEC2", "IVEC3", "IVEC4", "LOW_PRECISION", 
		"MAT2", "MAT3", "MAT4", "MEDIUM_PRECISION", "OUT", "PRECISION", "RETURN", 
		"SAMPLER2D", "SAMPLERCUBE", "STRUCT", "TRUE", "UNIFORM", "VARYING", "VEC2", 
		"VEC3", "VEC4", "VOID", "WHILE", "IDENTIFIER", "EXPONENT_PART", "FLOATCONSTANT", 
		"DECIMAL_CONSTANT", "OCTAL_CONSTANT", "HEXADECIMAL_CONSTANT", "HEXDIGIT", 
		"INTCONSTANT", "BOOLCONSTANT", "INC_OP", "DEC_OP", "LE_OP", "GE_OP", "EQ_OP", 
		"NE_OP", "AND_OP", "OR_OP", "XOR_OP", "MUL_ASSIGN", "DIV_ASSIGN", "ADD_ASSIGN", 
		"MOD_ASSIGN", "SUB_ASSIGN", "LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACKET", 
		"RIGHT_BRACKET", "LEFT_BRACE", "RIGHT_BRACE", "DOT", "COMMA", "COLON", 
		"EQUAL", "SEMICOLON", "BANG", "DASH", "TILDE", "PLUS", "STAR", "SLASH", 
		"PERCENT", "LEFT_ANGLE", "RIGHT_ANGLE", "VERTICAL_BAR", "CARET", "AMPERSAND", 
		"QUESTION", "WHITESPACE", "COMMENT", "MULTILINE_COMMENT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'attribute'", "'bool'", "'break'", "'bvec2'", "'bvec3'", "'bvec4'", 
		"'const'", "'continue'", "'discard'", "'do'", "'else'", "'float'", "'for'", 
		"'highp'", "'if'", "'in'", "'inout'", "'int'", "'invariant'", "'ivec2'", 
		"'ivec3'", "'ivec4'", "'lowp'", "'mat2'", "'mat3'", "'mat4'", "'mediump'", 
		"'out'", "'precision'", "'return'", "'sampler2D'", "'samplerCube'", "'struct'", 
		"'uniform'", "'varying'", "'vec2'", "'vec3'", "'vec4'", "'void'", "'while'", 
		null, null, null, null, "'++'", "'--'", "'<='", "'>='", "'=='", "'!='", 
		"'&&'", "'||'", "'^^'", "'*='", "'/='", "'+='", "'%='", "'-='", "'('", 
		"')'", "'['", "']'", "'{'", "'}'", "'.'", "','", "':'", "'='", "';'", 
		"'!'", "'-'", "'~'", "'+'", "'*'", "'/'", "'%'", "'<'", "'>'", "'|'", 
		"'^'", "'&'", "'?'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "ATTRIBUTE", "BOOL", "BREAK", "BVEC2", "BVEC3", "BVEC4", "CONST", 
		"CONTINUE", "DISCARD", "DO", "ELSE", "FLOAT", "FOR", "HIGH_PRECISION", 
		"IF", "IN", "INOUT", "INT", "INVARIANT", "IVEC2", "IVEC3", "IVEC4", "LOW_PRECISION", 
		"MAT2", "MAT3", "MAT4", "MEDIUM_PRECISION", "OUT", "PRECISION", "RETURN", 
		"SAMPLER2D", "SAMPLERCUBE", "STRUCT", "UNIFORM", "VARYING", "VEC2", "VEC3", 
		"VEC4", "VOID", "WHILE", "IDENTIFIER", "FLOATCONSTANT", "INTCONSTANT", 
		"BOOLCONSTANT", "INC_OP", "DEC_OP", "LE_OP", "GE_OP", "EQ_OP", "NE_OP", 
		"AND_OP", "OR_OP", "XOR_OP", "MUL_ASSIGN", "DIV_ASSIGN", "ADD_ASSIGN", 
		"MOD_ASSIGN", "SUB_ASSIGN", "LEFT_PAREN", "RIGHT_PAREN", "LEFT_BRACKET", 
		"RIGHT_BRACKET", "LEFT_BRACE", "RIGHT_BRACE", "DOT", "COMMA", "COLON", 
		"EQUAL", "SEMICOLON", "BANG", "DASH", "TILDE", "PLUS", "STAR", "SLASH", 
		"PERCENT", "LEFT_ANGLE", "RIGHT_ANGLE", "VERTICAL_BAR", "CARET", "AMPERSAND", 
		"QUESTION", "WHITESPACE", "COMMENT", "MULTILINE_COMMENT"
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

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public GLSLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "GLSL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2W\u028b\b\1\4\2\t" +
                    "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
		"\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\22\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32"+
		"\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\35"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3"+
		"!\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3\"\3#\3"+
		"#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3"+
		"&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3(\3(\3(\3(\3(\3)\3)\3)\3)\3)\3*\3*\3*"+
		"\3*\3*\3+\3+\3+\3+\3+\3+\3,\3,\7,\u01c3\n,\f,\16,\u01c6\13,\3-\3-\3-\5"+
		"-\u01cb\n-\3-\6-\u01ce\n-\r-\16-\u01cf\3.\6.\u01d3\n.\r.\16.\u01d4\3."+
		"\3.\7.\u01d9\n.\f.\16.\u01dc\13.\3.\5.\u01df\n.\3.\3.\6.\u01e3\n.\r.\16"+
		".\u01e4\3.\5.\u01e8\n.\3.\6.\u01eb\n.\r.\16.\u01ec\3.\5.\u01f0\n.\3/\3"+
		"/\7/\u01f4\n/\f/\16/\u01f7\13/\3\60\3\60\7\60\u01fb\n\60\f\60\16\60\u01fe"+
		"\13\60\3\61\3\61\3\61\6\61\u0203\n\61\r\61\16\61\u0204\3\62\3\62\3\63"+
		"\3\63\3\63\5\63\u020c\n\63\3\64\3\64\5\64\u0210\n\64\3\65\3\65\3\65\3"+
		"\66\3\66\3\66\3\67\3\67\3\67\38\38\38\39\39\39\3:\3:\3:\3;\3;\3;\3<\3"+
		"<\3<\3=\3=\3=\3>\3>\3>\3?\3?\3?\3@\3@\3@\3A\3A\3A\3B\3B\3B\3C\3C\3D\3"+
		"D\3E\3E\3F\3F\3G\3G\3H\3H\3I\3I\3J\3J\3K\3K\3L\3L\3M\3M\3N\3N\3O\3O\3"+
		"P\3P\3Q\3Q\3R\3R\3S\3S\3T\3T\3U\3U\3V\3V\3W\3W\3X\3X\3Y\3Y\3Z\3Z\3[\3"+
                    "[\3[\5[\u026f\n[\3[\3[\3\\\3\\\3\\\3\\\7\\\u0277\n\\\f\\\16\\\u027a\13" +
                    "\\\3\\\3\\\3]\3]\3]\3]\7]\u0282\n]\f]\16]\u0285\13]\3]\3]\3]\3]\3]\3\u0283" +
                    "\2^\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\2\33\16\35\17" +
                    "\37\20!\21#\22%\23\'\24)\25+\26-\27/\30\61\31\63\32\65\33\67\349\35;\36" +
                    "=\37? A!C\"E#G\2I$K%M&O\'Q(S)U*W+Y\2[,]\2_\2a\2c\2e-g.i/k\60m\61o\62q" +
                    "\63s\64u\65w\66y\67{8}9\177:\u0081;\u0083<\u0085=\u0087>\u0089?\u008b" +
                    "@\u008dA\u008fB\u0091C\u0093D\u0095E\u0097F\u0099G\u009bH\u009dI\u009f" +
                    "J\u00a1K\u00a3L\u00a5M\u00a7N\u00a9O\u00abP\u00adQ\u00afR\u00b1S\u00b3" +
                    "T\u00b5U\u00b7V\u00b9W\3\2\t\5\2C\\aac|\6\2\62;C\\aac|\4\2GGgg\4\2ZZz" +
                    "z\5\2\62;CHch\5\2\13\f\16\17\"\"\4\2\f\f\17\17\2\u0298\2\3\3\2\2\2\2\5" +
                    "\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2" +
                    "\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\33\3\2\2\2\2\35" +
                    "\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)" +
                    "\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2" +
                    "\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2" +
                    "A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3" +
                    "\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2[\3\2\2\2\2e\3\2\2" +
                    "\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2" +
                    "s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177" +
                    "\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2" +
                    "\2\2\u0089\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091" +
                    "\3\2\2\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2" +
                    "\2\2\u009b\3\2\2\2\2\u009d\3\2\2\2\2\u009f\3\2\2\2\2\u00a1\3\2\2\2\2\u00a3" +
                    "\3\2\2\2\2\u00a5\3\2\2\2\2\u00a7\3\2\2\2\2\u00a9\3\2\2\2\2\u00ab\3\2\2" +
                    "\2\2\u00ad\3\2\2\2\2\u00af\3\2\2\2\2\u00b1\3\2\2\2\2\u00b3\3\2\2\2\2\u00b5" +
                    "\3\2\2\2\2\u00b7\3\2\2\2\2\u00b9\3\2\2\2\3\u00bb\3\2\2\2\5\u00c5\3\2\2" +
                    "\2\7\u00ca\3\2\2\2\t\u00d0\3\2\2\2\13\u00d6\3\2\2\2\r\u00dc\3\2\2\2\17" +
                    "\u00e2\3\2\2\2\21\u00e8\3\2\2\2\23\u00f1\3\2\2\2\25\u00f9\3\2\2\2\27\u00fc" +
                    "\3\2\2\2\31\u0101\3\2\2\2\33\u0107\3\2\2\2\35\u010d\3\2\2\2\37\u0111\3" +
                    "\2\2\2!\u0117\3\2\2\2#\u011a\3\2\2\2%\u011d\3\2\2\2\'\u0123\3\2\2\2)\u0127" +
                    "\3\2\2\2+\u0131\3\2\2\2-\u0137\3\2\2\2/\u013d\3\2\2\2\61\u0143\3\2\2\2" +
                    "\63\u0148\3\2\2\2\65\u014d\3\2\2\2\67\u0152\3\2\2\29\u0157\3\2\2\2;\u015f" +
                    "\3\2\2\2=\u0163\3\2\2\2?\u016d\3\2\2\2A\u0174\3\2\2\2C\u017e\3\2\2\2E"+
		"\u018a\3\2\2\2G\u0191\3\2\2\2I\u0196\3\2\2\2K\u019e\3\2\2\2M\u01a6\3\2"+
		"\2\2O\u01ab\3\2\2\2Q\u01b0\3\2\2\2S\u01b5\3\2\2\2U\u01ba\3\2\2\2W\u01c0"+
		"\3\2\2\2Y\u01c7\3\2\2\2[\u01ef\3\2\2\2]\u01f1\3\2\2\2_\u01f8\3\2\2\2a"+
		"\u01ff\3\2\2\2c\u0206\3\2\2\2e\u020b\3\2\2\2g\u020f\3\2\2\2i\u0211\3\2"+
		"\2\2k\u0214\3\2\2\2m\u0217\3\2\2\2o\u021a\3\2\2\2q\u021d\3\2\2\2s\u0220"+
		"\3\2\2\2u\u0223\3\2\2\2w\u0226\3\2\2\2y\u0229\3\2\2\2{\u022c\3\2\2\2}"+
		"\u022f\3\2\2\2\177\u0232\3\2\2\2\u0081\u0235\3\2\2\2\u0083\u0238\3\2\2"+
		"\2\u0085\u023b\3\2\2\2\u0087\u023d\3\2\2\2\u0089\u023f\3\2\2\2\u008b\u0241"+
		"\3\2\2\2\u008d\u0243\3\2\2\2\u008f\u0245\3\2\2\2\u0091\u0247\3\2\2\2\u0093"+
		"\u0249\3\2\2\2\u0095\u024b\3\2\2\2\u0097\u024d\3\2\2\2\u0099\u024f\3\2"+
		"\2\2\u009b\u0251\3\2\2\2\u009d\u0253\3\2\2\2\u009f\u0255\3\2\2\2\u00a1"+
		"\u0257\3\2\2\2\u00a3\u0259\3\2\2\2\u00a5\u025b\3\2\2\2\u00a7\u025d\3\2"+
		"\2\2\u00a9\u025f\3\2\2\2\u00ab\u0261\3\2\2\2\u00ad\u0263\3\2\2\2\u00af"+
                    "\u0265\3\2\2\2\u00b1\u0267\3\2\2\2\u00b3\u0269\3\2\2\2\u00b5\u026e\3\2" +
                    "\2\2\u00b7\u0272\3\2\2\2\u00b9\u027d\3\2\2\2\u00bb\u00bc\7c\2\2\u00bc" +
                    "\u00bd\7v\2\2\u00bd\u00be\7v\2\2\u00be\u00bf\7t\2\2\u00bf\u00c0\7k\2\2"+
		"\u00c0\u00c1\7d\2\2\u00c1\u00c2\7w\2\2\u00c2\u00c3\7v\2\2\u00c3\u00c4"+
		"\7g\2\2\u00c4\4\3\2\2\2\u00c5\u00c6\7d\2\2\u00c6\u00c7\7q\2\2\u00c7\u00c8"+
		"\7q\2\2\u00c8\u00c9\7n\2\2\u00c9\6\3\2\2\2\u00ca\u00cb\7d\2\2\u00cb\u00cc"+
		"\7t\2\2\u00cc\u00cd\7g\2\2\u00cd\u00ce\7c\2\2\u00ce\u00cf\7m\2\2\u00cf"+
		"\b\3\2\2\2\u00d0\u00d1\7d\2\2\u00d1\u00d2\7x\2\2\u00d2\u00d3\7g\2\2\u00d3"+
		"\u00d4\7e\2\2\u00d4\u00d5\7\64\2\2\u00d5\n\3\2\2\2\u00d6\u00d7\7d\2\2"+
		"\u00d7\u00d8\7x\2\2\u00d8\u00d9\7g\2\2\u00d9\u00da\7e\2\2\u00da\u00db"+
		"\7\65\2\2\u00db\f\3\2\2\2\u00dc\u00dd\7d\2\2\u00dd\u00de\7x\2\2\u00de"+
		"\u00df\7g\2\2\u00df\u00e0\7e\2\2\u00e0\u00e1\7\66\2\2\u00e1\16\3\2\2\2"+
		"\u00e2\u00e3\7e\2\2\u00e3\u00e4\7q\2\2\u00e4\u00e5\7p\2\2\u00e5\u00e6"+
		"\7u\2\2\u00e6\u00e7\7v\2\2\u00e7\20\3\2\2\2\u00e8\u00e9\7e\2\2\u00e9\u00ea"+
		"\7q\2\2\u00ea\u00eb\7p\2\2\u00eb\u00ec\7v\2\2\u00ec\u00ed\7k\2\2\u00ed"+
		"\u00ee\7p\2\2\u00ee\u00ef\7w\2\2\u00ef\u00f0\7g\2\2\u00f0\22\3\2\2\2\u00f1"+
		"\u00f2\7f\2\2\u00f2\u00f3\7k\2\2\u00f3\u00f4\7u\2\2\u00f4\u00f5\7e\2\2"+
		"\u00f5\u00f6\7c\2\2\u00f6\u00f7\7t\2\2\u00f7\u00f8\7f\2\2\u00f8\24\3\2"+
		"\2\2\u00f9\u00fa\7f\2\2\u00fa\u00fb\7q\2\2\u00fb\26\3\2\2\2\u00fc\u00fd"+
		"\7g\2\2\u00fd\u00fe\7n\2\2\u00fe\u00ff\7u\2\2\u00ff\u0100\7g\2\2\u0100"+
		"\30\3\2\2\2\u0101\u0102\7h\2\2\u0102\u0103\7c\2\2\u0103\u0104\7n\2\2\u0104"+
		"\u0105\7u\2\2\u0105\u0106\7g\2\2\u0106\32\3\2\2\2\u0107\u0108\7h\2\2\u0108"+
		"\u0109\7n\2\2\u0109\u010a\7q\2\2\u010a\u010b\7c\2\2\u010b\u010c\7v\2\2"+
		"\u010c\34\3\2\2\2\u010d\u010e\7h\2\2\u010e\u010f\7q\2\2\u010f\u0110\7"+
		"t\2\2\u0110\36\3\2\2\2\u0111\u0112\7j\2\2\u0112\u0113\7k\2\2\u0113\u0114"+
		"\7i\2\2\u0114\u0115\7j\2\2\u0115\u0116\7r\2\2\u0116 \3\2\2\2\u0117\u0118"+
		"\7k\2\2\u0118\u0119\7h\2\2\u0119\"\3\2\2\2\u011a\u011b\7k\2\2\u011b\u011c"+
		"\7p\2\2\u011c$\3\2\2\2\u011d\u011e\7k\2\2\u011e\u011f\7p\2\2\u011f\u0120"+
		"\7q\2\2\u0120\u0121\7w\2\2\u0121\u0122\7v\2\2\u0122&\3\2\2\2\u0123\u0124"+
		"\7k\2\2\u0124\u0125\7p\2\2\u0125\u0126\7v\2\2\u0126(\3\2\2\2\u0127\u0128"+
		"\7k\2\2\u0128\u0129\7p\2\2\u0129\u012a\7x\2\2\u012a\u012b\7c\2\2\u012b"+
		"\u012c\7t\2\2\u012c\u012d\7k\2\2\u012d\u012e\7c\2\2\u012e\u012f\7p\2\2"+
		"\u012f\u0130\7v\2\2\u0130*\3\2\2\2\u0131\u0132\7k\2\2\u0132\u0133\7x\2"+
		"\2\u0133\u0134\7g\2\2\u0134\u0135\7e\2\2\u0135\u0136\7\64\2\2\u0136,\3"+
		"\2\2\2\u0137\u0138\7k\2\2\u0138\u0139\7x\2\2\u0139\u013a\7g\2\2\u013a"+
		"\u013b\7e\2\2\u013b\u013c\7\65\2\2\u013c.\3\2\2\2\u013d\u013e\7k\2\2\u013e"+
		"\u013f\7x\2\2\u013f\u0140\7g\2\2\u0140\u0141\7e\2\2\u0141\u0142\7\66\2"+
		"\2\u0142\60\3\2\2\2\u0143\u0144\7n\2\2\u0144\u0145\7q\2\2\u0145\u0146"+
		"\7y\2\2\u0146\u0147\7r\2\2\u0147\62\3\2\2\2\u0148\u0149\7o\2\2\u0149\u014a"+
		"\7c\2\2\u014a\u014b\7v\2\2\u014b\u014c\7\64\2\2\u014c\64\3\2\2\2\u014d"+
		"\u014e\7o\2\2\u014e\u014f\7c\2\2\u014f\u0150\7v\2\2\u0150\u0151\7\65\2"+
		"\2\u0151\66\3\2\2\2\u0152\u0153\7o\2\2\u0153\u0154\7c\2\2\u0154\u0155"+
		"\7v\2\2\u0155\u0156\7\66\2\2\u01568\3\2\2\2\u0157\u0158\7o\2\2\u0158\u0159"+
		"\7g\2\2\u0159\u015a\7f\2\2\u015a\u015b\7k\2\2\u015b\u015c\7w\2\2\u015c"+
		"\u015d\7o\2\2\u015d\u015e\7r\2\2\u015e:\3\2\2\2\u015f\u0160\7q\2\2\u0160"+
		"\u0161\7w\2\2\u0161\u0162\7v\2\2\u0162<\3\2\2\2\u0163\u0164\7r\2\2\u0164"+
		"\u0165\7t\2\2\u0165\u0166\7g\2\2\u0166\u0167\7e\2\2\u0167\u0168\7k\2\2"+
		"\u0168\u0169\7u\2\2\u0169\u016a\7k\2\2\u016a\u016b\7q\2\2\u016b\u016c"+
		"\7p\2\2\u016c>\3\2\2\2\u016d\u016e\7t\2\2\u016e\u016f\7g\2\2\u016f\u0170"+
		"\7v\2\2\u0170\u0171\7w\2\2\u0171\u0172\7t\2\2\u0172\u0173\7p\2\2\u0173"+
		"@\3\2\2\2\u0174\u0175\7u\2\2\u0175\u0176\7c\2\2\u0176\u0177\7o\2\2\u0177"+
		"\u0178\7r\2\2\u0178\u0179\7n\2\2\u0179\u017a\7g\2\2\u017a\u017b\7t\2\2"+
		"\u017b\u017c\7\64\2\2\u017c\u017d\7F\2\2\u017dB\3\2\2\2\u017e\u017f\7"+
		"u\2\2\u017f\u0180\7c\2\2\u0180\u0181\7o\2\2\u0181\u0182\7r\2\2\u0182\u0183"+
		"\7n\2\2\u0183\u0184\7g\2\2\u0184\u0185\7t\2\2\u0185\u0186\7E\2\2\u0186"+
		"\u0187\7w\2\2\u0187\u0188\7d\2\2\u0188\u0189\7g\2\2\u0189D\3\2\2\2\u018a"+
		"\u018b\7u\2\2\u018b\u018c\7v\2\2\u018c\u018d\7t\2\2\u018d\u018e\7w\2\2"+
		"\u018e\u018f\7e\2\2\u018f\u0190\7v\2\2\u0190F\3\2\2\2\u0191\u0192\7v\2"+
		"\2\u0192\u0193\7t\2\2\u0193\u0194\7w\2\2\u0194\u0195\7g\2\2\u0195H\3\2"+
		"\2\2\u0196\u0197\7w\2\2\u0197\u0198\7p\2\2\u0198\u0199\7k\2\2\u0199\u019a"+
		"\7h\2\2\u019a\u019b\7q\2\2\u019b\u019c\7t\2\2\u019c\u019d\7o\2\2\u019d"+
		"J\3\2\2\2\u019e\u019f\7x\2\2\u019f\u01a0\7c\2\2\u01a0\u01a1\7t\2\2\u01a1"+
		"\u01a2\7{\2\2\u01a2\u01a3\7k\2\2\u01a3\u01a4\7p\2\2\u01a4\u01a5\7i\2\2"+
		"\u01a5L\3\2\2\2\u01a6\u01a7\7x\2\2\u01a7\u01a8\7g\2\2\u01a8\u01a9\7e\2"+
		"\2\u01a9\u01aa\7\64\2\2\u01aaN\3\2\2\2\u01ab\u01ac\7x\2\2\u01ac\u01ad"+
		"\7g\2\2\u01ad\u01ae\7e\2\2\u01ae\u01af\7\65\2\2\u01afP\3\2\2\2\u01b0\u01b1"+
		"\7x\2\2\u01b1\u01b2\7g\2\2\u01b2\u01b3\7e\2\2\u01b3\u01b4\7\66\2\2\u01b4"+
		"R\3\2\2\2\u01b5\u01b6\7x\2\2\u01b6\u01b7\7q\2\2\u01b7\u01b8\7k\2\2\u01b8"+
		"\u01b9\7f\2\2\u01b9T\3\2\2\2\u01ba\u01bb\7y\2\2\u01bb\u01bc\7j\2\2\u01bc"+
		"\u01bd\7k\2\2\u01bd\u01be\7n\2\2\u01be\u01bf\7g\2\2\u01bfV\3\2\2\2\u01c0"+
		"\u01c4\t\2\2\2\u01c1\u01c3\t\3\2\2\u01c2\u01c1\3\2\2\2\u01c3\u01c6\3\2"+
		"\2\2\u01c4\u01c2\3\2\2\2\u01c4\u01c5\3\2\2\2\u01c5X\3\2\2\2\u01c6\u01c4"+
		"\3\2\2\2\u01c7\u01ca\t\4\2\2\u01c8\u01cb\5\u00a1Q\2\u01c9\u01cb\5\u009d"+
		"O\2\u01ca\u01c8\3\2\2\2\u01ca\u01c9\3\2\2\2\u01ca\u01cb\3\2\2\2\u01cb"+
		"\u01cd\3\2\2\2\u01cc\u01ce\4\62;\2\u01cd\u01cc\3\2\2\2\u01ce\u01cf\3\2"+
		"\2\2\u01cf\u01cd\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0Z\3\2\2\2\u01d1\u01d3"+
		"\4\62;\2\u01d2\u01d1\3\2\2\2\u01d3\u01d4\3\2\2\2\u01d4\u01d2\3\2\2\2\u01d4"+
		"\u01d5\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d6\u01da\7\60\2\2\u01d7\u01d9\4"+
		"\62;\2\u01d8\u01d7\3\2\2\2\u01d9\u01dc\3\2\2\2\u01da\u01d8\3\2\2\2\u01da"+
		"\u01db\3\2\2\2\u01db\u01de\3\2\2\2\u01dc\u01da\3\2\2\2\u01dd\u01df\5Y"+
		"-\2\u01de\u01dd\3\2\2\2\u01de\u01df\3\2\2\2\u01df\u01f0\3\2\2\2\u01e0"+
		"\u01e2\7\60\2\2\u01e1\u01e3\4\62;\2\u01e2\u01e1\3\2\2\2\u01e3\u01e4\3"+
		"\2\2\2\u01e4\u01e2\3\2\2\2\u01e4\u01e5\3\2\2\2\u01e5\u01e7\3\2\2\2\u01e6"+
		"\u01e8\5Y-\2\u01e7\u01e6\3\2\2\2\u01e7\u01e8\3\2\2\2\u01e8\u01f0\3\2\2"+
		"\2\u01e9\u01eb\4\62;\2\u01ea\u01e9\3\2\2\2\u01eb\u01ec\3\2\2\2\u01ec\u01ea"+
		"\3\2\2\2\u01ec\u01ed\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee\u01f0\5Y-\2\u01ef"+
		"\u01d2\3\2\2\2\u01ef\u01e0\3\2\2\2\u01ef\u01ea\3\2\2\2\u01f0\\\3\2\2\2"+
		"\u01f1\u01f5\4\63;\2\u01f2\u01f4\4\62;\2\u01f3\u01f2\3\2\2\2\u01f4\u01f7"+
		"\3\2\2\2\u01f5\u01f3\3\2\2\2\u01f5\u01f6\3\2\2\2\u01f6^\3\2\2\2\u01f7"+
		"\u01f5\3\2\2\2\u01f8\u01fc\7\62\2\2\u01f9\u01fb\4\629\2\u01fa\u01f9\3"+
		"\2\2\2\u01fb\u01fe\3\2\2\2\u01fc\u01fa\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fd"+
		"`\3\2\2\2\u01fe\u01fc\3\2\2\2\u01ff\u0200\7\62\2\2\u0200\u0202\t\5\2\2"+
		"\u0201\u0203\5c\62\2\u0202\u0201\3\2\2\2\u0203\u0204\3\2\2\2\u0204\u0202"+
		"\3\2\2\2\u0204\u0205\3\2\2\2\u0205b\3\2\2\2\u0206\u0207\t\6\2\2\u0207"+
		"d\3\2\2\2\u0208\u020c\5]/\2\u0209\u020c\5_\60\2\u020a\u020c\5a\61\2\u020b"+
		"\u0208\3\2\2\2\u020b\u0209\3\2\2\2\u020b\u020a\3\2\2\2\u020cf\3\2\2\2"+
		"\u020d\u0210\5G$\2\u020e\u0210\5\31\r\2\u020f\u020d\3\2\2\2\u020f\u020e"+
		"\3\2\2\2\u0210h\3\2\2\2\u0211\u0212\7-\2\2\u0212\u0213\7-\2\2\u0213j\3"+
		"\2\2\2\u0214\u0215\7/\2\2\u0215\u0216\7/\2\2\u0216l\3\2\2\2\u0217\u0218"+
		"\7>\2\2\u0218\u0219\7?\2\2\u0219n\3\2\2\2\u021a\u021b\7@\2\2\u021b\u021c"+
		"\7?\2\2\u021cp\3\2\2\2\u021d\u021e\7?\2\2\u021e\u021f\7?\2\2\u021fr\3"+
		"\2\2\2\u0220\u0221\7#\2\2\u0221\u0222\7?\2\2\u0222t\3\2\2\2\u0223\u0224"+
		"\7(\2\2\u0224\u0225\7(\2\2\u0225v\3\2\2\2\u0226\u0227\7~\2\2\u0227\u0228"+
		"\7~\2\2\u0228x\3\2\2\2\u0229\u022a\7`\2\2\u022a\u022b\7`\2\2\u022bz\3"+
		"\2\2\2\u022c\u022d\7,\2\2\u022d\u022e\7?\2\2\u022e|\3\2\2\2\u022f\u0230"+
		"\7\61\2\2\u0230\u0231\7?\2\2\u0231~\3\2\2\2\u0232\u0233\7-\2\2\u0233\u0234"+
		"\7?\2\2\u0234\u0080\3\2\2\2\u0235\u0236\7\'\2\2\u0236\u0237\7?\2\2\u0237"+
		"\u0082\3\2\2\2\u0238\u0239\7/\2\2\u0239\u023a\7?\2\2\u023a\u0084\3\2\2"+
		"\2\u023b\u023c\7*\2\2\u023c\u0086\3\2\2\2\u023d\u023e\7+\2\2\u023e\u0088"+
		"\3\2\2\2\u023f\u0240\7]\2\2\u0240\u008a\3\2\2\2\u0241\u0242\7_\2\2\u0242"+
		"\u008c\3\2\2\2\u0243\u0244\7}\2\2\u0244\u008e\3\2\2\2\u0245\u0246\7\177"+
		"\2\2\u0246\u0090\3\2\2\2\u0247\u0248\7\60\2\2\u0248\u0092\3\2\2\2\u0249"+
		"\u024a\7.\2\2\u024a\u0094\3\2\2\2\u024b\u024c\7<\2\2\u024c\u0096\3\2\2"+
		"\2\u024d\u024e\7?\2\2\u024e\u0098\3\2\2\2\u024f\u0250\7=\2\2\u0250\u009a"+
		"\3\2\2\2\u0251\u0252\7#\2\2\u0252\u009c\3\2\2\2\u0253\u0254\7/\2\2\u0254"+
		"\u009e\3\2\2\2\u0255\u0256\7\u0080\2\2\u0256\u00a0\3\2\2\2\u0257\u0258"+
		"\7-\2\2\u0258\u00a2\3\2\2\2\u0259\u025a\7,\2\2\u025a\u00a4\3\2\2\2\u025b"+
		"\u025c\7\61\2\2\u025c\u00a6\3\2\2\2\u025d\u025e\7\'\2\2\u025e\u00a8\3"+
		"\2\2\2\u025f\u0260\7>\2\2\u0260\u00aa\3\2\2\2\u0261\u0262\7@\2\2\u0262"+
		"\u00ac\3\2\2\2\u0263\u0264\7~\2\2\u0264\u00ae\3\2\2\2\u0265\u0266\7`\2"+
		"\2\u0266\u00b0\3\2\2\2\u0267\u0268\7(\2\2\u0268\u00b2\3\2\2\2\u0269\u026a"+
                    "\7A\2\2\u026a\u00b4\3\2\2\2\u026b\u026f\t\7\2\2\u026c\u026d\7^\2\2\u026d" +
                    "\u026f\7p\2\2\u026e\u026b\3\2\2\2\u026e\u026c\3\2\2\2\u026f\u0270\3\2" +
                    "\2\2\u0270\u0271\b[\2\2\u0271\u00b6\3\2\2\2\u0272\u0273\7\61\2\2\u0273" +
                    "\u0274\7\61\2\2\u0274\u0278\3\2\2\2\u0275\u0277\n\b\2\2\u0276\u0275\3" +
                    "\2\2\2\u0277\u027a\3\2\2\2\u0278\u0276\3\2\2\2\u0278\u0279\3\2\2\2\u0279" +
                    "\u027b\3\2\2\2\u027a\u0278\3\2\2\2\u027b\u027c\b\\\2\2\u027c\u00b8\3\2" +
                    "\2\2\u027d\u027e\7\61\2\2\u027e\u027f\7,\2\2\u027f\u0283\3\2\2\2\u0280" +
                    "\u0282\13\2\2\2\u0281\u0280\3\2\2\2\u0282\u0285\3\2\2\2\u0283\u0284\3" +
                    "\2\2\2\u0283\u0281\3\2\2\2\u0284\u0286\3\2\2\2\u0285\u0283\3\2\2\2\u0286" +
                    "\u0287\7,\2\2\u0287\u0288\7\61\2\2\u0288\u0289\3\2\2\2\u0289\u028a\b]" +
                    "\2\2\u028a\u00ba\3\2\2\2\25\2\u01c4\u01ca\u01cf\u01d4\u01da\u01de\u01e4" +
                    "\u01e7\u01ec\u01ef\u01f5\u01fc\u0204\u020b\u020f\u026e\u0278\u0283\3\b" +
                    "\2\2";
    public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}