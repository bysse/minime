// Generated from GLSL.g4 by ANTLR 4.5.3
package com.tazadum.glsl.language;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class GLSLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

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
	public static final int
		RULE_translation_unit = 0, RULE_variable_identifier = 1, RULE_primary_expression = 2, 
		RULE_postfix_expression = 3, RULE_primary_expression_or_function_call = 4, 
		RULE_integer_expression = 5, RULE_function_call = 6, RULE_function_call_header = 7, 
		RULE_function_identifier = 8, RULE_constructor_identifier = 9, RULE_unary_expression = 10, 
		RULE_unary_operator = 11, RULE_numeric_expression = 12, RULE_logical_expression = 13, 
		RULE_conditional_expression = 14, RULE_assignment_expression = 15, RULE_assignment_operator = 16, 
		RULE_expression = 17, RULE_constant_expression = 18, RULE_declaration = 19, 
		RULE_function_prototype = 20, RULE_function_declarator = 21, RULE_function_header = 22, 
		RULE_parameter_declaration = 23, RULE_parameter_qualifier = 24, RULE_init_declarator_list = 25, 
		RULE_array_specifier = 26, RULE_single_declaration = 27, RULE_fully_specified_type = 28, 
		RULE_type_qualifier = 29, RULE_type_specifier = 30, RULE_type_specifier_no_prec = 31, 
		RULE_precision_qualifier = 32, RULE_struct_specifier = 33, RULE_struct_declaration_list = 34, 
		RULE_struct_declaration = 35, RULE_struct_declarator_list = 36, RULE_struct_declarator = 37, 
		RULE_initializer = 38, RULE_declaration_statement = 39, RULE_statement_no_new_scope = 40, 
		RULE_simple_statement = 41, RULE_compound_statement_with_scope = 42, RULE_statement_with_scope = 43, 
		RULE_compound_statement_no_new_scope = 44, RULE_statement_list = 45, RULE_expression_statement = 46, 
		RULE_selection_statement = 47, RULE_condition = 48, RULE_iteration_statement = 49, 
		RULE_for_init_statement = 50, RULE_for_rest_statement = 51, RULE_jump_statement = 52, 
		RULE_external_declaration = 53, RULE_function_definition = 54, RULE_field_selection = 55;
	public static final String[] ruleNames = {
		"translation_unit", "variable_identifier", "primary_expression", "postfix_expression", 
		"primary_expression_or_function_call", "integer_expression", "function_call", 
		"function_call_header", "function_identifier", "constructor_identifier", 
		"unary_expression", "unary_operator", "numeric_expression", "logical_expression", 
		"conditional_expression", "assignment_expression", "assignment_operator", 
		"expression", "constant_expression", "declaration", "function_prototype", 
		"function_declarator", "function_header", "parameter_declaration", "parameter_qualifier", 
		"init_declarator_list", "array_specifier", "single_declaration", "fully_specified_type", 
		"type_qualifier", "type_specifier", "type_specifier_no_prec", "precision_qualifier", 
		"struct_specifier", "struct_declaration_list", "struct_declaration", "struct_declarator_list", 
		"struct_declarator", "initializer", "declaration_statement", "statement_no_new_scope", 
		"simple_statement", "compound_statement_with_scope", "statement_with_scope", 
		"compound_statement_no_new_scope", "statement_list", "expression_statement", 
		"selection_statement", "condition", "iteration_statement", "for_init_statement", 
		"for_rest_statement", "jump_statement", "external_declaration", "function_definition", 
		"field_selection"
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

	@Override
	public String getGrammarFileName() { return "GLSL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public GLSLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class Translation_unitContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(GLSLParser.EOF, 0); }
		public List<External_declarationContext> external_declaration() {
			return getRuleContexts(External_declarationContext.class);
		}
		public External_declarationContext external_declaration(int i) {
			return getRuleContext(External_declarationContext.class,i);
		}
		public Translation_unitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_translation_unit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterTranslation_unit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitTranslation_unit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitTranslation_unit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Translation_unitContext translation_unit() throws RecognitionException {
		Translation_unitContext _localctx = new Translation_unitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_translation_unit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ATTRIBUTE) | (1L << BOOL) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << CONST) | (1L << FLOAT) | (1L << HIGH_PRECISION) | (1L << IN) | (1L << INOUT) | (1L << INT) | (1L << INVARIANT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << LOW_PRECISION) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << MEDIUM_PRECISION) | (1L << OUT) | (1L << PRECISION) | (1L << SAMPLER2D) | (1L << SAMPLERCUBE) | (1L << STRUCT) | (1L << UNIFORM) | (1L << VARYING) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << VOID) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(112);
				external_declaration();
				}
				}
				setState(117);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(118);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Variable_identifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public Variable_identifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterVariable_identifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitVariable_identifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitVariable_identifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Variable_identifierContext variable_identifier() throws RecognitionException {
		Variable_identifierContext _localctx = new Variable_identifierContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_variable_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Primary_expressionContext extends ParserRuleContext {
		public TerminalNode INTCONSTANT() { return getToken(GLSLParser.INTCONSTANT, 0); }
		public TerminalNode FLOATCONSTANT() { return getToken(GLSLParser.FLOATCONSTANT, 0); }
		public TerminalNode BOOLCONSTANT() { return getToken(GLSLParser.BOOLCONSTANT, 0); }
		public Variable_identifierContext variable_identifier() {
			return getRuleContext(Variable_identifierContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(GLSLParser.LEFT_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(GLSLParser.RIGHT_PAREN, 0); }
		public Primary_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterPrimary_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitPrimary_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitPrimary_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Primary_expressionContext primary_expression() throws RecognitionException {
		Primary_expressionContext _localctx = new Primary_expressionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_primary_expression);
		try {
			setState(130);
			switch (_input.LA(1)) {
			case INTCONSTANT:
				enterOuterAlt(_localctx, 1);
				{
				setState(122);
				match(INTCONSTANT);
				}
				break;
			case FLOATCONSTANT:
				enterOuterAlt(_localctx, 2);
				{
				setState(123);
				match(FLOATCONSTANT);
				}
				break;
			case BOOLCONSTANT:
				enterOuterAlt(_localctx, 3);
				{
				setState(124);
				match(BOOLCONSTANT);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(125);
				variable_identifier();
				}
				break;
			case LEFT_PAREN:
				enterOuterAlt(_localctx, 5);
				{
				setState(126);
				match(LEFT_PAREN);
				setState(127);
				expression();
				setState(128);
				match(RIGHT_PAREN);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Postfix_expressionContext extends ParserRuleContext {
		public Primary_expression_or_function_callContext primary_expression_or_function_call() {
			return getRuleContext(Primary_expression_or_function_callContext.class,0);
		}
		public Postfix_expressionContext postfix_expression() {
			return getRuleContext(Postfix_expressionContext.class,0);
		}
		public TerminalNode LEFT_BRACKET() { return getToken(GLSLParser.LEFT_BRACKET, 0); }
		public Integer_expressionContext integer_expression() {
			return getRuleContext(Integer_expressionContext.class,0);
		}
		public TerminalNode RIGHT_BRACKET() { return getToken(GLSLParser.RIGHT_BRACKET, 0); }
		public TerminalNode DOT() { return getToken(GLSLParser.DOT, 0); }
		public Field_selectionContext field_selection() {
			return getRuleContext(Field_selectionContext.class,0);
		}
		public TerminalNode INC_OP() { return getToken(GLSLParser.INC_OP, 0); }
		public TerminalNode DEC_OP() { return getToken(GLSLParser.DEC_OP, 0); }
		public Postfix_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postfix_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterPostfix_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitPostfix_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitPostfix_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Postfix_expressionContext postfix_expression() throws RecognitionException {
		return postfix_expression(0);
	}

	private Postfix_expressionContext postfix_expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Postfix_expressionContext _localctx = new Postfix_expressionContext(_ctx, _parentState);
		Postfix_expressionContext _prevctx = _localctx;
		int _startState = 6;
		enterRecursionRule(_localctx, 6, RULE_postfix_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(133);
			primary_expression_or_function_call();
			}
			_ctx.stop = _input.LT(-1);
			setState(149);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(147);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new Postfix_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfix_expression);
						setState(135);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(136);
						match(LEFT_BRACKET);
						setState(137);
						integer_expression();
						setState(138);
						match(RIGHT_BRACKET);
						}
						break;
					case 2:
						{
						_localctx = new Postfix_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfix_expression);
						setState(140);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(141);
						match(DOT);
						setState(142);
						field_selection();
						}
						break;
					case 3:
						{
						_localctx = new Postfix_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfix_expression);
						setState(143);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(144);
						match(INC_OP);
						}
						break;
					case 4:
						{
						_localctx = new Postfix_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfix_expression);
						setState(145);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(146);
						match(DEC_OP);
						}
						break;
					}
					} 
				}
				setState(151);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Primary_expression_or_function_callContext extends ParserRuleContext {
		public Primary_expressionContext primary_expression() {
			return getRuleContext(Primary_expressionContext.class,0);
		}
		public Function_callContext function_call() {
			return getRuleContext(Function_callContext.class,0);
		}
		public Primary_expression_or_function_callContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary_expression_or_function_call; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterPrimary_expression_or_function_call(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitPrimary_expression_or_function_call(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitPrimary_expression_or_function_call(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Primary_expression_or_function_callContext primary_expression_or_function_call() throws RecognitionException {
		Primary_expression_or_function_callContext _localctx = new Primary_expression_or_function_callContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_primary_expression_or_function_call);
		try {
			setState(154);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(152);
				primary_expression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(153);
				function_call();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Integer_expressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Integer_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integer_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterInteger_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitInteger_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitInteger_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Integer_expressionContext integer_expression() throws RecognitionException {
		Integer_expressionContext _localctx = new Integer_expressionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_integer_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_callContext extends ParserRuleContext {
		public Function_call_headerContext function_call_header() {
			return getRuleContext(Function_call_headerContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(GLSLParser.RIGHT_PAREN, 0); }
		public List<Assignment_expressionContext> assignment_expression() {
			return getRuleContexts(Assignment_expressionContext.class);
		}
		public Assignment_expressionContext assignment_expression(int i) {
			return getRuleContext(Assignment_expressionContext.class,i);
		}
		public TerminalNode VOID() { return getToken(GLSLParser.VOID, 0); }
		public List<TerminalNode> COMMA() { return getTokens(GLSLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GLSLParser.COMMA, i);
		}
		public Function_callContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_call; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFunction_call(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFunction_call(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFunction_call(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_callContext function_call() throws RecognitionException {
		Function_callContext _localctx = new Function_callContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_function_call);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			function_call_header();
			setState(170);
			switch (_input.LA(1)) {
			case VOID:
			case RIGHT_PAREN:
				{
				setState(160);
				_la = _input.LA(1);
				if (_la==VOID) {
					{
					setState(159);
					match(VOID);
					}
				}

				}
				break;
			case BOOL:
			case BVEC2:
			case BVEC3:
			case BVEC4:
			case FLOAT:
			case INT:
			case IVEC2:
			case IVEC3:
			case IVEC4:
			case MAT2:
			case MAT3:
			case MAT4:
			case VEC2:
			case VEC3:
			case VEC4:
			case IDENTIFIER:
			case FLOATCONSTANT:
			case INTCONSTANT:
			case BOOLCONSTANT:
			case INC_OP:
			case DEC_OP:
			case LEFT_PAREN:
			case BANG:
			case DASH:
			case PLUS:
				{
				setState(162);
				assignment_expression();
				setState(167);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(163);
					match(COMMA);
					setState(164);
					assignment_expression();
					}
					}
					setState(169);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(172);
			match(RIGHT_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_call_headerContext extends ParserRuleContext {
		public Function_identifierContext function_identifier() {
			return getRuleContext(Function_identifierContext.class,0);
		}
		public TerminalNode LEFT_PAREN() { return getToken(GLSLParser.LEFT_PAREN, 0); }
		public Function_call_headerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_call_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFunction_call_header(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFunction_call_header(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFunction_call_header(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_call_headerContext function_call_header() throws RecognitionException {
		Function_call_headerContext _localctx = new Function_call_headerContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_function_call_header);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			function_identifier();
			setState(175);
			match(LEFT_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_identifierContext extends ParserRuleContext {
		public Constructor_identifierContext constructor_identifier() {
			return getRuleContext(Constructor_identifierContext.class,0);
		}
		public Function_identifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFunction_identifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFunction_identifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFunction_identifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_identifierContext function_identifier() throws RecognitionException {
		Function_identifierContext _localctx = new Function_identifierContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_function_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			constructor_identifier();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Constructor_identifierContext extends ParserRuleContext {
		public TerminalNode FLOAT() { return getToken(GLSLParser.FLOAT, 0); }
		public TerminalNode INT() { return getToken(GLSLParser.INT, 0); }
		public TerminalNode BOOL() { return getToken(GLSLParser.BOOL, 0); }
		public TerminalNode VEC2() { return getToken(GLSLParser.VEC2, 0); }
		public TerminalNode VEC3() { return getToken(GLSLParser.VEC3, 0); }
		public TerminalNode VEC4() { return getToken(GLSLParser.VEC4, 0); }
		public TerminalNode BVEC2() { return getToken(GLSLParser.BVEC2, 0); }
		public TerminalNode BVEC3() { return getToken(GLSLParser.BVEC3, 0); }
		public TerminalNode BVEC4() { return getToken(GLSLParser.BVEC4, 0); }
		public TerminalNode IVEC2() { return getToken(GLSLParser.IVEC2, 0); }
		public TerminalNode IVEC3() { return getToken(GLSLParser.IVEC3, 0); }
		public TerminalNode IVEC4() { return getToken(GLSLParser.IVEC4, 0); }
		public TerminalNode MAT2() { return getToken(GLSLParser.MAT2, 0); }
		public TerminalNode MAT3() { return getToken(GLSLParser.MAT3, 0); }
		public TerminalNode MAT4() { return getToken(GLSLParser.MAT4, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public Constructor_identifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructor_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterConstructor_identifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitConstructor_identifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitConstructor_identifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Constructor_identifierContext constructor_identifier() throws RecognitionException {
		Constructor_identifierContext _localctx = new Constructor_identifierContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_constructor_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << FLOAT) | (1L << INT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << IDENTIFIER))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unary_expressionContext extends ParserRuleContext {
		public Postfix_expressionContext postfix_expression() {
			return getRuleContext(Postfix_expressionContext.class,0);
		}
		public Unary_expressionContext unary_expression() {
			return getRuleContext(Unary_expressionContext.class,0);
		}
		public TerminalNode INC_OP() { return getToken(GLSLParser.INC_OP, 0); }
		public TerminalNode DEC_OP() { return getToken(GLSLParser.DEC_OP, 0); }
		public Unary_operatorContext unary_operator() {
			return getRuleContext(Unary_operatorContext.class,0);
		}
		public Unary_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterUnary_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitUnary_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitUnary_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Unary_expressionContext unary_expression() throws RecognitionException {
		Unary_expressionContext _localctx = new Unary_expressionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_unary_expression);
		int _la;
		try {
			setState(187);
			switch (_input.LA(1)) {
			case BOOL:
			case BVEC2:
			case BVEC3:
			case BVEC4:
			case FLOAT:
			case INT:
			case IVEC2:
			case IVEC3:
			case IVEC4:
			case MAT2:
			case MAT3:
			case MAT4:
			case VEC2:
			case VEC3:
			case VEC4:
			case IDENTIFIER:
			case FLOATCONSTANT:
			case INTCONSTANT:
			case BOOLCONSTANT:
			case LEFT_PAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(181);
				postfix_expression(0);
				}
				break;
			case INC_OP:
			case DEC_OP:
				enterOuterAlt(_localctx, 2);
				{
				setState(182);
				_la = _input.LA(1);
				if ( !(_la==INC_OP || _la==DEC_OP) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(183);
				unary_expression();
				}
				break;
			case BANG:
			case DASH:
			case PLUS:
				enterOuterAlt(_localctx, 3);
				{
				setState(184);
				unary_operator();
				setState(185);
				unary_expression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Unary_operatorContext extends ParserRuleContext {
		public TerminalNode PLUS() { return getToken(GLSLParser.PLUS, 0); }
		public TerminalNode DASH() { return getToken(GLSLParser.DASH, 0); }
		public TerminalNode BANG() { return getToken(GLSLParser.BANG, 0); }
		public Unary_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unary_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterUnary_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitUnary_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitUnary_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Unary_operatorContext unary_operator() throws RecognitionException {
		Unary_operatorContext _localctx = new Unary_operatorContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_unary_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189);
			_la = _input.LA(1);
			if ( !(((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (BANG - 70)) | (1L << (DASH - 70)) | (1L << (PLUS - 70)))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Numeric_expressionContext extends ParserRuleContext {
		public Numeric_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numeric_expression; }
	 
		public Numeric_expressionContext() { }
		public void copyFrom(Numeric_expressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SubtractionExpressionContext extends Numeric_expressionContext {
		public List<Numeric_expressionContext> numeric_expression() {
			return getRuleContexts(Numeric_expressionContext.class);
		}
		public Numeric_expressionContext numeric_expression(int i) {
			return getRuleContext(Numeric_expressionContext.class,i);
		}
		public TerminalNode DASH() { return getToken(GLSLParser.DASH, 0); }
		public SubtractionExpressionContext(Numeric_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterSubtractionExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitSubtractionExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitSubtractionExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AdditiveExpressionContext extends Numeric_expressionContext {
		public List<Numeric_expressionContext> numeric_expression() {
			return getRuleContexts(Numeric_expressionContext.class);
		}
		public Numeric_expressionContext numeric_expression(int i) {
			return getRuleContext(Numeric_expressionContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(GLSLParser.PLUS, 0); }
		public AdditiveExpressionContext(Numeric_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterAdditiveExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitAdditiveExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitAdditiveExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryExpressionContext extends Numeric_expressionContext {
		public Unary_expressionContext unary_expression() {
			return getRuleContext(Unary_expressionContext.class,0);
		}
		public UnaryExpressionContext(Numeric_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitUnaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitUnaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DivisionExpressionContext extends Numeric_expressionContext {
		public List<Numeric_expressionContext> numeric_expression() {
			return getRuleContexts(Numeric_expressionContext.class);
		}
		public Numeric_expressionContext numeric_expression(int i) {
			return getRuleContext(Numeric_expressionContext.class,i);
		}
		public TerminalNode SLASH() { return getToken(GLSLParser.SLASH, 0); }
		public DivisionExpressionContext(Numeric_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterDivisionExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitDivisionExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitDivisionExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MultiplicationExpressionContext extends Numeric_expressionContext {
		public List<Numeric_expressionContext> numeric_expression() {
			return getRuleContexts(Numeric_expressionContext.class);
		}
		public Numeric_expressionContext numeric_expression(int i) {
			return getRuleContext(Numeric_expressionContext.class,i);
		}
		public TerminalNode STAR() { return getToken(GLSLParser.STAR, 0); }
		public MultiplicationExpressionContext(Numeric_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterMultiplicationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitMultiplicationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitMultiplicationExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Numeric_expressionContext numeric_expression() throws RecognitionException {
		return numeric_expression(0);
	}

	private Numeric_expressionContext numeric_expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Numeric_expressionContext _localctx = new Numeric_expressionContext(_ctx, _parentState);
		Numeric_expressionContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_numeric_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new UnaryExpressionContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(192);
			unary_expression();
			}
			_ctx.stop = _input.LT(-1);
			setState(208);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(206);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						_localctx = new MultiplicationExpressionContext(new Numeric_expressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_numeric_expression);
						setState(194);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(195);
						match(STAR);
						setState(196);
						numeric_expression(5);
						}
						break;
					case 2:
						{
						_localctx = new DivisionExpressionContext(new Numeric_expressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_numeric_expression);
						setState(197);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(198);
						match(SLASH);
						setState(199);
						numeric_expression(4);
						}
						break;
					case 3:
						{
						_localctx = new AdditiveExpressionContext(new Numeric_expressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_numeric_expression);
						setState(200);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(201);
						match(PLUS);
						setState(202);
						numeric_expression(3);
						}
						break;
					case 4:
						{
						_localctx = new SubtractionExpressionContext(new Numeric_expressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_numeric_expression);
						setState(203);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(204);
						match(DASH);
						setState(205);
						numeric_expression(2);
						}
						break;
					}
					} 
				}
				setState(210);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Logical_expressionContext extends ParserRuleContext {
		public Logical_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logical_expression; }
	 
		public Logical_expressionContext() { }
		public void copyFrom(Logical_expressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class LogicalInEqualityContext extends Logical_expressionContext {
		public List<Logical_expressionContext> logical_expression() {
			return getRuleContexts(Logical_expressionContext.class);
		}
		public Logical_expressionContext logical_expression(int i) {
			return getRuleContext(Logical_expressionContext.class,i);
		}
		public TerminalNode NE_OP() { return getToken(GLSLParser.NE_OP, 0); }
		public LogicalInEqualityContext(Logical_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterLogicalInEquality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitLogicalInEquality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitLogicalInEquality(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumericDelegatorContext extends Logical_expressionContext {
		public Numeric_expressionContext numeric_expression() {
			return getRuleContext(Numeric_expressionContext.class,0);
		}
		public NumericDelegatorContext(Logical_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterNumericDelegator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitNumericDelegator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitNumericDelegator(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicalAndContext extends Logical_expressionContext {
		public List<Logical_expressionContext> logical_expression() {
			return getRuleContexts(Logical_expressionContext.class);
		}
		public Logical_expressionContext logical_expression(int i) {
			return getRuleContext(Logical_expressionContext.class,i);
		}
		public TerminalNode AND_OP() { return getToken(GLSLParser.AND_OP, 0); }
		public LogicalAndContext(Logical_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterLogicalAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitLogicalAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitLogicalAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RelationalContext extends Logical_expressionContext {
		public List<Logical_expressionContext> logical_expression() {
			return getRuleContexts(Logical_expressionContext.class);
		}
		public Logical_expressionContext logical_expression(int i) {
			return getRuleContext(Logical_expressionContext.class,i);
		}
		public TerminalNode LEFT_ANGLE() { return getToken(GLSLParser.LEFT_ANGLE, 0); }
		public TerminalNode RIGHT_ANGLE() { return getToken(GLSLParser.RIGHT_ANGLE, 0); }
		public TerminalNode LE_OP() { return getToken(GLSLParser.LE_OP, 0); }
		public TerminalNode GE_OP() { return getToken(GLSLParser.GE_OP, 0); }
		public RelationalContext(Logical_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterRelational(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitRelational(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitRelational(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicalXorContext extends Logical_expressionContext {
		public List<Logical_expressionContext> logical_expression() {
			return getRuleContexts(Logical_expressionContext.class);
		}
		public Logical_expressionContext logical_expression(int i) {
			return getRuleContext(Logical_expressionContext.class,i);
		}
		public TerminalNode XOR_OP() { return getToken(GLSLParser.XOR_OP, 0); }
		public LogicalXorContext(Logical_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterLogicalXor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitLogicalXor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitLogicalXor(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicalEqualityContext extends Logical_expressionContext {
		public List<Logical_expressionContext> logical_expression() {
			return getRuleContexts(Logical_expressionContext.class);
		}
		public Logical_expressionContext logical_expression(int i) {
			return getRuleContext(Logical_expressionContext.class,i);
		}
		public TerminalNode EQ_OP() { return getToken(GLSLParser.EQ_OP, 0); }
		public LogicalEqualityContext(Logical_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterLogicalEquality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitLogicalEquality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitLogicalEquality(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicalOrContext extends Logical_expressionContext {
		public List<Logical_expressionContext> logical_expression() {
			return getRuleContexts(Logical_expressionContext.class);
		}
		public Logical_expressionContext logical_expression(int i) {
			return getRuleContext(Logical_expressionContext.class,i);
		}
		public TerminalNode OR_OP() { return getToken(GLSLParser.OR_OP, 0); }
		public LogicalOrContext(Logical_expressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterLogicalOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitLogicalOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitLogicalOr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Logical_expressionContext logical_expression() throws RecognitionException {
		return logical_expression(0);
	}

	private Logical_expressionContext logical_expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Logical_expressionContext _localctx = new Logical_expressionContext(_ctx, _parentState);
		Logical_expressionContext _prevctx = _localctx;
		int _startState = 26;
		enterRecursionRule(_localctx, 26, RULE_logical_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new NumericDelegatorContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(212);
			numeric_expression(0);
			}
			_ctx.stop = _input.LT(-1);
			setState(234);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(232);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						_localctx = new RelationalContext(new Logical_expressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(214);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(215);
						_la = _input.LA(1);
						if ( !(((((_la - 47)) & ~0x3f) == 0 && ((1L << (_la - 47)) & ((1L << (LE_OP - 47)) | (1L << (GE_OP - 47)) | (1L << (LEFT_ANGLE - 47)) | (1L << (RIGHT_ANGLE - 47)))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(216);
						logical_expression(7);
						}
						break;
					case 2:
						{
						_localctx = new LogicalEqualityContext(new Logical_expressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(217);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(218);
						match(EQ_OP);
						setState(219);
						logical_expression(6);
						}
						break;
					case 3:
						{
						_localctx = new LogicalInEqualityContext(new Logical_expressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(220);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(221);
						match(NE_OP);
						setState(222);
						logical_expression(5);
						}
						break;
					case 4:
						{
						_localctx = new LogicalAndContext(new Logical_expressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(223);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(224);
						match(AND_OP);
						setState(225);
						logical_expression(4);
						}
						break;
					case 5:
						{
						_localctx = new LogicalXorContext(new Logical_expressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(226);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(227);
						match(XOR_OP);
						setState(228);
						logical_expression(3);
						}
						break;
					case 6:
						{
						_localctx = new LogicalOrContext(new Logical_expressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(229);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(230);
						match(OR_OP);
						setState(231);
						logical_expression(2);
						}
						break;
					}
					} 
				}
				setState(236);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Conditional_expressionContext extends ParserRuleContext {
		public Logical_expressionContext logical_expression() {
			return getRuleContext(Logical_expressionContext.class,0);
		}
		public TerminalNode QUESTION() { return getToken(GLSLParser.QUESTION, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode COLON() { return getToken(GLSLParser.COLON, 0); }
		public Assignment_expressionContext assignment_expression() {
			return getRuleContext(Assignment_expressionContext.class,0);
		}
		public Conditional_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditional_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterConditional_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitConditional_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitConditional_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Conditional_expressionContext conditional_expression() throws RecognitionException {
		Conditional_expressionContext _localctx = new Conditional_expressionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_conditional_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(237);
			logical_expression(0);
			setState(243);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(238);
				match(QUESTION);
				setState(239);
				expression();
				setState(240);
				match(COLON);
				setState(241);
				assignment_expression();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Assignment_expressionContext extends ParserRuleContext {
		public Conditional_expressionContext conditional_expression() {
			return getRuleContext(Conditional_expressionContext.class,0);
		}
		public Unary_expressionContext unary_expression() {
			return getRuleContext(Unary_expressionContext.class,0);
		}
		public Assignment_operatorContext assignment_operator() {
			return getRuleContext(Assignment_operatorContext.class,0);
		}
		public Assignment_expressionContext assignment_expression() {
			return getRuleContext(Assignment_expressionContext.class,0);
		}
		public Assignment_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterAssignment_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitAssignment_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitAssignment_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Assignment_expressionContext assignment_expression() throws RecognitionException {
		Assignment_expressionContext _localctx = new Assignment_expressionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_assignment_expression);
		try {
			setState(250);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(245);
				conditional_expression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(246);
				unary_expression();
				setState(247);
				assignment_operator();
				setState(248);
				assignment_expression();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Assignment_operatorContext extends ParserRuleContext {
		public TerminalNode EQUAL() { return getToken(GLSLParser.EQUAL, 0); }
		public TerminalNode MUL_ASSIGN() { return getToken(GLSLParser.MUL_ASSIGN, 0); }
		public TerminalNode DIV_ASSIGN() { return getToken(GLSLParser.DIV_ASSIGN, 0); }
		public TerminalNode ADD_ASSIGN() { return getToken(GLSLParser.ADD_ASSIGN, 0); }
		public TerminalNode SUB_ASSIGN() { return getToken(GLSLParser.SUB_ASSIGN, 0); }
		public Assignment_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterAssignment_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitAssignment_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitAssignment_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Assignment_operatorContext assignment_operator() throws RecognitionException {
		Assignment_operatorContext _localctx = new Assignment_operatorContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_assignment_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			_la = _input.LA(1);
			if ( !(((((_la - 54)) & ~0x3f) == 0 && ((1L << (_la - 54)) & ((1L << (MUL_ASSIGN - 54)) | (1L << (DIV_ASSIGN - 54)) | (1L << (ADD_ASSIGN - 54)) | (1L << (SUB_ASSIGN - 54)) | (1L << (EQUAL - 54)))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public List<Assignment_expressionContext> assignment_expression() {
			return getRuleContexts(Assignment_expressionContext.class);
		}
		public Assignment_expressionContext assignment_expression(int i) {
			return getRuleContext(Assignment_expressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GLSLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GLSLParser.COMMA, i);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			assignment_expression();
			setState(259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(255);
				match(COMMA);
				setState(256);
				assignment_expression();
				}
				}
				setState(261);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Constant_expressionContext extends ParserRuleContext {
		public Conditional_expressionContext conditional_expression() {
			return getRuleContext(Conditional_expressionContext.class,0);
		}
		public Constant_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constant_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterConstant_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitConstant_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitConstant_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Constant_expressionContext constant_expression() throws RecognitionException {
		Constant_expressionContext _localctx = new Constant_expressionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_constant_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			conditional_expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationContext extends ParserRuleContext {
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
	 
		public DeclarationContext() { }
		public void copyFrom(DeclarationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class FunctionDeclarationContext extends DeclarationContext {
		public Function_prototypeContext function_prototype() {
			return getRuleContext(Function_prototypeContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(GLSLParser.SEMICOLON, 0); }
		public FunctionDeclarationContext(DeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFunctionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFunctionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFunctionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PrecisionDeclarationContext extends DeclarationContext {
		public TerminalNode PRECISION() { return getToken(GLSLParser.PRECISION, 0); }
		public Precision_qualifierContext precision_qualifier() {
			return getRuleContext(Precision_qualifierContext.class,0);
		}
		public Type_specifier_no_precContext type_specifier_no_prec() {
			return getRuleContext(Type_specifier_no_precContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(GLSLParser.SEMICOLON, 0); }
		public PrecisionDeclarationContext(DeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterPrecisionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitPrecisionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitPrecisionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableDeclarationContext extends DeclarationContext {
		public Init_declarator_listContext init_declarator_list() {
			return getRuleContext(Init_declarator_listContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(GLSLParser.SEMICOLON, 0); }
		public VariableDeclarationContext(DeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitVariableDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_declaration);
		try {
			setState(275);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				_localctx = new FunctionDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(264);
				function_prototype();
				setState(265);
				match(SEMICOLON);
				}
				break;
			case 2:
				_localctx = new VariableDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(267);
				init_declarator_list(0);
				setState(268);
				match(SEMICOLON);
				}
				break;
			case 3:
				_localctx = new PrecisionDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(270);
				match(PRECISION);
				setState(271);
				precision_qualifier();
				setState(272);
				type_specifier_no_prec();
				setState(273);
				match(SEMICOLON);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_prototypeContext extends ParserRuleContext {
		public Function_declaratorContext function_declarator() {
			return getRuleContext(Function_declaratorContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(GLSLParser.RIGHT_PAREN, 0); }
		public Function_prototypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_prototype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFunction_prototype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFunction_prototype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFunction_prototype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_prototypeContext function_prototype() throws RecognitionException {
		Function_prototypeContext _localctx = new Function_prototypeContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_function_prototype);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			function_declarator();
			setState(278);
			match(RIGHT_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_declaratorContext extends ParserRuleContext {
		public Function_headerContext function_header() {
			return getRuleContext(Function_headerContext.class,0);
		}
		public List<Parameter_declarationContext> parameter_declaration() {
			return getRuleContexts(Parameter_declarationContext.class);
		}
		public Parameter_declarationContext parameter_declaration(int i) {
			return getRuleContext(Parameter_declarationContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GLSLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GLSLParser.COMMA, i);
		}
		public Function_declaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFunction_declarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFunction_declarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFunction_declarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_declaratorContext function_declarator() throws RecognitionException {
		Function_declaratorContext _localctx = new Function_declaratorContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_function_declarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(280);
			function_header();
			setState(289);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ATTRIBUTE) | (1L << BOOL) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << CONST) | (1L << FLOAT) | (1L << HIGH_PRECISION) | (1L << IN) | (1L << INOUT) | (1L << INT) | (1L << INVARIANT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << LOW_PRECISION) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << MEDIUM_PRECISION) | (1L << OUT) | (1L << SAMPLER2D) | (1L << SAMPLERCUBE) | (1L << STRUCT) | (1L << UNIFORM) | (1L << VARYING) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << VOID) | (1L << IDENTIFIER))) != 0)) {
				{
				setState(281);
				parameter_declaration();
				setState(286);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
					setState(282);
					match(COMMA);
					setState(283);
					parameter_declaration();
					}
					}
					setState(288);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_headerContext extends ParserRuleContext {
		public Fully_specified_typeContext fully_specified_type() {
			return getRuleContext(Fully_specified_typeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(GLSLParser.LEFT_PAREN, 0); }
		public Function_headerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_header; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFunction_header(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFunction_header(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFunction_header(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_headerContext function_header() throws RecognitionException {
		Function_headerContext _localctx = new Function_headerContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_function_header);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(291);
			fully_specified_type();
			setState(292);
			match(IDENTIFIER);
			setState(293);
			match(LEFT_PAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Parameter_declarationContext extends ParserRuleContext {
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public Type_qualifierContext type_qualifier() {
			return getRuleContext(Type_qualifierContext.class,0);
		}
		public Array_specifierContext array_specifier() {
			return getRuleContext(Array_specifierContext.class,0);
		}
		public Parameter_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterParameter_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitParameter_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitParameter_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Parameter_declarationContext parameter_declaration() throws RecognitionException {
		Parameter_declarationContext _localctx = new Parameter_declarationContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_parameter_declaration);
		int _la;
		try {
			setState(307);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(296);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ATTRIBUTE) | (1L << CONST) | (1L << IN) | (1L << INOUT) | (1L << INVARIANT) | (1L << OUT) | (1L << UNIFORM) | (1L << VARYING))) != 0)) {
					{
					setState(295);
					type_qualifier();
					}
				}

				setState(298);
				type_specifier();
				setState(299);
				match(IDENTIFIER);
				setState(301);
				_la = _input.LA(1);
				if (_la==LEFT_BRACKET) {
					{
					setState(300);
					array_specifier();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(304);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ATTRIBUTE) | (1L << CONST) | (1L << IN) | (1L << INOUT) | (1L << INVARIANT) | (1L << OUT) | (1L << UNIFORM) | (1L << VARYING))) != 0)) {
					{
					setState(303);
					type_qualifier();
					}
				}

				setState(306);
				type_specifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Parameter_qualifierContext extends ParserRuleContext {
		public TerminalNode IN() { return getToken(GLSLParser.IN, 0); }
		public TerminalNode OUT() { return getToken(GLSLParser.OUT, 0); }
		public TerminalNode INOUT() { return getToken(GLSLParser.INOUT, 0); }
		public Parameter_qualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter_qualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterParameter_qualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitParameter_qualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitParameter_qualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Parameter_qualifierContext parameter_qualifier() throws RecognitionException {
		Parameter_qualifierContext _localctx = new Parameter_qualifierContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_parameter_qualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << IN) | (1L << INOUT) | (1L << OUT))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Init_declarator_listContext extends ParserRuleContext {
		public Single_declarationContext single_declaration() {
			return getRuleContext(Single_declarationContext.class,0);
		}
		public Init_declarator_listContext init_declarator_list() {
			return getRuleContext(Init_declarator_listContext.class,0);
		}
		public TerminalNode COMMA() { return getToken(GLSLParser.COMMA, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public Array_specifierContext array_specifier() {
			return getRuleContext(Array_specifierContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(GLSLParser.EQUAL, 0); }
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public Init_declarator_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init_declarator_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterInit_declarator_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitInit_declarator_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitInit_declarator_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Init_declarator_listContext init_declarator_list() throws RecognitionException {
		return init_declarator_list(0);
	}

	private Init_declarator_listContext init_declarator_list(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Init_declarator_listContext _localctx = new Init_declarator_listContext(_ctx, _parentState);
		Init_declarator_listContext _prevctx = _localctx;
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_init_declarator_list, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(312);
			single_declaration();
			}
			_ctx.stop = _input.LT(-1);
			setState(326);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new Init_declarator_listContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_init_declarator_list);
					setState(314);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(315);
					match(COMMA);
					setState(316);
					match(IDENTIFIER);
					setState(318);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
					case 1:
						{
						setState(317);
						array_specifier();
						}
						break;
					}
					setState(322);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
					case 1:
						{
						setState(320);
						match(EQUAL);
						setState(321);
						initializer();
						}
						break;
					}
					}
					} 
				}
				setState(328);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Array_specifierContext extends ParserRuleContext {
		public TerminalNode LEFT_BRACKET() { return getToken(GLSLParser.LEFT_BRACKET, 0); }
		public Constant_expressionContext constant_expression() {
			return getRuleContext(Constant_expressionContext.class,0);
		}
		public TerminalNode RIGHT_BRACKET() { return getToken(GLSLParser.RIGHT_BRACKET, 0); }
		public Array_specifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_specifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterArray_specifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitArray_specifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitArray_specifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_specifierContext array_specifier() throws RecognitionException {
		Array_specifierContext _localctx = new Array_specifierContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_array_specifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(329);
			match(LEFT_BRACKET);
			setState(330);
			constant_expression();
			setState(331);
			match(RIGHT_BRACKET);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Single_declarationContext extends ParserRuleContext {
		public TerminalNode INVARIANT() { return getToken(GLSLParser.INVARIANT, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public Fully_specified_typeContext fully_specified_type() {
			return getRuleContext(Fully_specified_typeContext.class,0);
		}
		public Array_specifierContext array_specifier() {
			return getRuleContext(Array_specifierContext.class,0);
		}
		public TerminalNode EQUAL() { return getToken(GLSLParser.EQUAL, 0); }
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public Single_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_single_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterSingle_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitSingle_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitSingle_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Single_declarationContext single_declaration() throws RecognitionException {
		Single_declarationContext _localctx = new Single_declarationContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_single_declaration);
		try {
			setState(345);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(333);
				match(INVARIANT);
				setState(334);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(335);
				fully_specified_type();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(336);
				fully_specified_type();
				setState(337);
				match(IDENTIFIER);
				setState(339);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
				case 1:
					{
					setState(338);
					array_specifier();
					}
					break;
				}
				setState(343);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
				case 1:
					{
					setState(341);
					match(EQUAL);
					setState(342);
					initializer();
					}
					break;
				}
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Fully_specified_typeContext extends ParserRuleContext {
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public Type_qualifierContext type_qualifier() {
			return getRuleContext(Type_qualifierContext.class,0);
		}
		public Fully_specified_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fully_specified_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFully_specified_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFully_specified_type(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFully_specified_type(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Fully_specified_typeContext fully_specified_type() throws RecognitionException {
		Fully_specified_typeContext _localctx = new Fully_specified_typeContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_fully_specified_type);
		try {
			setState(351);
			switch (_input.LA(1)) {
			case BOOL:
			case BVEC2:
			case BVEC3:
			case BVEC4:
			case FLOAT:
			case HIGH_PRECISION:
			case INT:
			case IVEC2:
			case IVEC3:
			case IVEC4:
			case LOW_PRECISION:
			case MAT2:
			case MAT3:
			case MAT4:
			case MEDIUM_PRECISION:
			case SAMPLER2D:
			case SAMPLERCUBE:
			case STRUCT:
			case VEC2:
			case VEC3:
			case VEC4:
			case VOID:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(347);
				type_specifier();
				}
				break;
			case ATTRIBUTE:
			case CONST:
			case IN:
			case INOUT:
			case INVARIANT:
			case OUT:
			case UNIFORM:
			case VARYING:
				enterOuterAlt(_localctx, 2);
				{
				setState(348);
				type_qualifier();
				setState(349);
				type_specifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_qualifierContext extends ParserRuleContext {
		public TerminalNode CONST() { return getToken(GLSLParser.CONST, 0); }
		public TerminalNode ATTRIBUTE() { return getToken(GLSLParser.ATTRIBUTE, 0); }
		public TerminalNode VARYING() { return getToken(GLSLParser.VARYING, 0); }
		public TerminalNode INVARIANT() { return getToken(GLSLParser.INVARIANT, 0); }
		public TerminalNode UNIFORM() { return getToken(GLSLParser.UNIFORM, 0); }
		public TerminalNode INOUT() { return getToken(GLSLParser.INOUT, 0); }
		public TerminalNode IN() { return getToken(GLSLParser.IN, 0); }
		public TerminalNode OUT() { return getToken(GLSLParser.OUT, 0); }
		public Type_qualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_qualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterType_qualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitType_qualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitType_qualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Type_qualifierContext type_qualifier() throws RecognitionException {
		Type_qualifierContext _localctx = new Type_qualifierContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_type_qualifier);
		try {
			setState(362);
			switch (_input.LA(1)) {
			case CONST:
				enterOuterAlt(_localctx, 1);
				{
				setState(353);
				match(CONST);
				}
				break;
			case ATTRIBUTE:
				enterOuterAlt(_localctx, 2);
				{
				setState(354);
				match(ATTRIBUTE);
				}
				break;
			case VARYING:
				enterOuterAlt(_localctx, 3);
				{
				setState(355);
				match(VARYING);
				}
				break;
			case INVARIANT:
				enterOuterAlt(_localctx, 4);
				{
				setState(356);
				match(INVARIANT);
				setState(357);
				match(VARYING);
				}
				break;
			case UNIFORM:
				enterOuterAlt(_localctx, 5);
				{
				setState(358);
				match(UNIFORM);
				}
				break;
			case INOUT:
				enterOuterAlt(_localctx, 6);
				{
				setState(359);
				match(INOUT);
				}
				break;
			case IN:
				enterOuterAlt(_localctx, 7);
				{
				setState(360);
				match(IN);
				}
				break;
			case OUT:
				enterOuterAlt(_localctx, 8);
				{
				setState(361);
				match(OUT);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_specifierContext extends ParserRuleContext {
		public Type_specifier_no_precContext type_specifier_no_prec() {
			return getRuleContext(Type_specifier_no_precContext.class,0);
		}
		public Precision_qualifierContext precision_qualifier() {
			return getRuleContext(Precision_qualifierContext.class,0);
		}
		public Type_specifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_specifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterType_specifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitType_specifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitType_specifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Type_specifierContext type_specifier() throws RecognitionException {
		Type_specifierContext _localctx = new Type_specifierContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_type_specifier);
		try {
			setState(368);
			switch (_input.LA(1)) {
			case BOOL:
			case BVEC2:
			case BVEC3:
			case BVEC4:
			case FLOAT:
			case INT:
			case IVEC2:
			case IVEC3:
			case IVEC4:
			case MAT2:
			case MAT3:
			case MAT4:
			case SAMPLER2D:
			case SAMPLERCUBE:
			case STRUCT:
			case VEC2:
			case VEC3:
			case VEC4:
			case VOID:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(364);
				type_specifier_no_prec();
				}
				break;
			case HIGH_PRECISION:
			case LOW_PRECISION:
			case MEDIUM_PRECISION:
				enterOuterAlt(_localctx, 2);
				{
				setState(365);
				precision_qualifier();
				setState(366);
				type_specifier_no_prec();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_specifier_no_precContext extends ParserRuleContext {
		public TerminalNode VOID() { return getToken(GLSLParser.VOID, 0); }
		public TerminalNode FLOAT() { return getToken(GLSLParser.FLOAT, 0); }
		public TerminalNode INT() { return getToken(GLSLParser.INT, 0); }
		public TerminalNode BOOL() { return getToken(GLSLParser.BOOL, 0); }
		public TerminalNode VEC2() { return getToken(GLSLParser.VEC2, 0); }
		public TerminalNode VEC3() { return getToken(GLSLParser.VEC3, 0); }
		public TerminalNode VEC4() { return getToken(GLSLParser.VEC4, 0); }
		public TerminalNode BVEC2() { return getToken(GLSLParser.BVEC2, 0); }
		public TerminalNode BVEC3() { return getToken(GLSLParser.BVEC3, 0); }
		public TerminalNode BVEC4() { return getToken(GLSLParser.BVEC4, 0); }
		public TerminalNode IVEC2() { return getToken(GLSLParser.IVEC2, 0); }
		public TerminalNode IVEC3() { return getToken(GLSLParser.IVEC3, 0); }
		public TerminalNode IVEC4() { return getToken(GLSLParser.IVEC4, 0); }
		public TerminalNode MAT2() { return getToken(GLSLParser.MAT2, 0); }
		public TerminalNode MAT3() { return getToken(GLSLParser.MAT3, 0); }
		public TerminalNode MAT4() { return getToken(GLSLParser.MAT4, 0); }
		public TerminalNode SAMPLER2D() { return getToken(GLSLParser.SAMPLER2D, 0); }
		public TerminalNode SAMPLERCUBE() { return getToken(GLSLParser.SAMPLERCUBE, 0); }
		public Struct_specifierContext struct_specifier() {
			return getRuleContext(Struct_specifierContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public Type_specifier_no_precContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_specifier_no_prec; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterType_specifier_no_prec(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitType_specifier_no_prec(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitType_specifier_no_prec(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Type_specifier_no_precContext type_specifier_no_prec() throws RecognitionException {
		Type_specifier_no_precContext _localctx = new Type_specifier_no_precContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_type_specifier_no_prec);
		try {
			setState(390);
			switch (_input.LA(1)) {
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(370);
				match(VOID);
				}
				break;
			case FLOAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(371);
				match(FLOAT);
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 3);
				{
				setState(372);
				match(INT);
				}
				break;
			case BOOL:
				enterOuterAlt(_localctx, 4);
				{
				setState(373);
				match(BOOL);
				}
				break;
			case VEC2:
				enterOuterAlt(_localctx, 5);
				{
				setState(374);
				match(VEC2);
				}
				break;
			case VEC3:
				enterOuterAlt(_localctx, 6);
				{
				setState(375);
				match(VEC3);
				}
				break;
			case VEC4:
				enterOuterAlt(_localctx, 7);
				{
				setState(376);
				match(VEC4);
				}
				break;
			case BVEC2:
				enterOuterAlt(_localctx, 8);
				{
				setState(377);
				match(BVEC2);
				}
				break;
			case BVEC3:
				enterOuterAlt(_localctx, 9);
				{
				setState(378);
				match(BVEC3);
				}
				break;
			case BVEC4:
				enterOuterAlt(_localctx, 10);
				{
				setState(379);
				match(BVEC4);
				}
				break;
			case IVEC2:
				enterOuterAlt(_localctx, 11);
				{
				setState(380);
				match(IVEC2);
				}
				break;
			case IVEC3:
				enterOuterAlt(_localctx, 12);
				{
				setState(381);
				match(IVEC3);
				}
				break;
			case IVEC4:
				enterOuterAlt(_localctx, 13);
				{
				setState(382);
				match(IVEC4);
				}
				break;
			case MAT2:
				enterOuterAlt(_localctx, 14);
				{
				setState(383);
				match(MAT2);
				}
				break;
			case MAT3:
				enterOuterAlt(_localctx, 15);
				{
				setState(384);
				match(MAT3);
				}
				break;
			case MAT4:
				enterOuterAlt(_localctx, 16);
				{
				setState(385);
				match(MAT4);
				}
				break;
			case SAMPLER2D:
				enterOuterAlt(_localctx, 17);
				{
				setState(386);
				match(SAMPLER2D);
				}
				break;
			case SAMPLERCUBE:
				enterOuterAlt(_localctx, 18);
				{
				setState(387);
				match(SAMPLERCUBE);
				}
				break;
			case STRUCT:
				enterOuterAlt(_localctx, 19);
				{
				setState(388);
				struct_specifier();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 20);
				{
				setState(389);
				match(IDENTIFIER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Precision_qualifierContext extends ParserRuleContext {
		public TerminalNode HIGH_PRECISION() { return getToken(GLSLParser.HIGH_PRECISION, 0); }
		public TerminalNode MEDIUM_PRECISION() { return getToken(GLSLParser.MEDIUM_PRECISION, 0); }
		public TerminalNode LOW_PRECISION() { return getToken(GLSLParser.LOW_PRECISION, 0); }
		public Precision_qualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_precision_qualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterPrecision_qualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitPrecision_qualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitPrecision_qualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Precision_qualifierContext precision_qualifier() throws RecognitionException {
		Precision_qualifierContext _localctx = new Precision_qualifierContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_precision_qualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(392);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << HIGH_PRECISION) | (1L << LOW_PRECISION) | (1L << MEDIUM_PRECISION))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Struct_specifierContext extends ParserRuleContext {
		public TerminalNode STRUCT() { return getToken(GLSLParser.STRUCT, 0); }
		public TerminalNode LEFT_BRACE() { return getToken(GLSLParser.LEFT_BRACE, 0); }
		public Struct_declaration_listContext struct_declaration_list() {
			return getRuleContext(Struct_declaration_listContext.class,0);
		}
		public TerminalNode RIGHT_BRACE() { return getToken(GLSLParser.RIGHT_BRACE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public Struct_specifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct_specifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterStruct_specifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitStruct_specifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitStruct_specifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Struct_specifierContext struct_specifier() throws RecognitionException {
		Struct_specifierContext _localctx = new Struct_specifierContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_struct_specifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(394);
			match(STRUCT);
			setState(396);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(395);
				match(IDENTIFIER);
				}
			}

			setState(398);
			match(LEFT_BRACE);
			setState(399);
			struct_declaration_list();
			setState(400);
			match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Struct_declaration_listContext extends ParserRuleContext {
		public List<Struct_declarationContext> struct_declaration() {
			return getRuleContexts(Struct_declarationContext.class);
		}
		public Struct_declarationContext struct_declaration(int i) {
			return getRuleContext(Struct_declarationContext.class,i);
		}
		public Struct_declaration_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct_declaration_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterStruct_declaration_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitStruct_declaration_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitStruct_declaration_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Struct_declaration_listContext struct_declaration_list() throws RecognitionException {
		Struct_declaration_listContext _localctx = new Struct_declaration_listContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_struct_declaration_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(402);
				struct_declaration();
				}
				}
				setState(405); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << FLOAT) | (1L << HIGH_PRECISION) | (1L << INT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << LOW_PRECISION) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << MEDIUM_PRECISION) | (1L << SAMPLER2D) | (1L << SAMPLERCUBE) | (1L << STRUCT) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << VOID) | (1L << IDENTIFIER))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Struct_declarationContext extends ParserRuleContext {
		public Type_specifierContext type_specifier() {
			return getRuleContext(Type_specifierContext.class,0);
		}
		public Struct_declarator_listContext struct_declarator_list() {
			return getRuleContext(Struct_declarator_listContext.class,0);
		}
		public TerminalNode SEMICOLON() { return getToken(GLSLParser.SEMICOLON, 0); }
		public Struct_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterStruct_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitStruct_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitStruct_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Struct_declarationContext struct_declaration() throws RecognitionException {
		Struct_declarationContext _localctx = new Struct_declarationContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_struct_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(407);
			type_specifier();
			setState(408);
			struct_declarator_list();
			setState(409);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Struct_declarator_listContext extends ParserRuleContext {
		public List<Struct_declaratorContext> struct_declarator() {
			return getRuleContexts(Struct_declaratorContext.class);
		}
		public Struct_declaratorContext struct_declarator(int i) {
			return getRuleContext(Struct_declaratorContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(GLSLParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(GLSLParser.COMMA, i);
		}
		public Struct_declarator_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct_declarator_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterStruct_declarator_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitStruct_declarator_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitStruct_declarator_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Struct_declarator_listContext struct_declarator_list() throws RecognitionException {
		Struct_declarator_listContext _localctx = new Struct_declarator_listContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_struct_declarator_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			struct_declarator();
			setState(416);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(412);
				match(COMMA);
				setState(413);
				struct_declarator();
				}
				}
				setState(418);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Struct_declaratorContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public TerminalNode LEFT_BRACKET() { return getToken(GLSLParser.LEFT_BRACKET, 0); }
		public Constant_expressionContext constant_expression() {
			return getRuleContext(Constant_expressionContext.class,0);
		}
		public TerminalNode RIGHT_BRACKET() { return getToken(GLSLParser.RIGHT_BRACKET, 0); }
		public Struct_declaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_struct_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterStruct_declarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitStruct_declarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitStruct_declarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Struct_declaratorContext struct_declarator() throws RecognitionException {
		Struct_declaratorContext _localctx = new Struct_declaratorContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_struct_declarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(419);
			match(IDENTIFIER);
			setState(424);
			_la = _input.LA(1);
			if (_la==LEFT_BRACKET) {
				{
				setState(420);
				match(LEFT_BRACKET);
				setState(421);
				constant_expression();
				setState(422);
				match(RIGHT_BRACKET);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InitializerContext extends ParserRuleContext {
		public Assignment_expressionContext assignment_expression() {
			return getRuleContext(Assignment_expressionContext.class,0);
		}
		public InitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitInitializer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitializerContext initializer() throws RecognitionException {
		InitializerContext _localctx = new InitializerContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_initializer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(426);
			assignment_expression();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Declaration_statementContext extends ParserRuleContext {
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public Declaration_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterDeclaration_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitDeclaration_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitDeclaration_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Declaration_statementContext declaration_statement() throws RecognitionException {
		Declaration_statementContext _localctx = new Declaration_statementContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_declaration_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(428);
			declaration();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Statement_no_new_scopeContext extends ParserRuleContext {
		public Compound_statement_with_scopeContext compound_statement_with_scope() {
			return getRuleContext(Compound_statement_with_scopeContext.class,0);
		}
		public Simple_statementContext simple_statement() {
			return getRuleContext(Simple_statementContext.class,0);
		}
		public Statement_no_new_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement_no_new_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterStatement_no_new_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitStatement_no_new_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitStatement_no_new_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Statement_no_new_scopeContext statement_no_new_scope() throws RecognitionException {
		Statement_no_new_scopeContext _localctx = new Statement_no_new_scopeContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_statement_no_new_scope);
		try {
			setState(432);
			switch (_input.LA(1)) {
			case LEFT_BRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(430);
				compound_statement_with_scope();
				}
				break;
			case ATTRIBUTE:
			case BOOL:
			case BREAK:
			case BVEC2:
			case BVEC3:
			case BVEC4:
			case CONST:
			case CONTINUE:
			case DISCARD:
			case DO:
			case FLOAT:
			case FOR:
			case HIGH_PRECISION:
			case IF:
			case IN:
			case INOUT:
			case INT:
			case INVARIANT:
			case IVEC2:
			case IVEC3:
			case IVEC4:
			case LOW_PRECISION:
			case MAT2:
			case MAT3:
			case MAT4:
			case MEDIUM_PRECISION:
			case OUT:
			case PRECISION:
			case RETURN:
			case SAMPLER2D:
			case SAMPLERCUBE:
			case STRUCT:
			case UNIFORM:
			case VARYING:
			case VEC2:
			case VEC3:
			case VEC4:
			case VOID:
			case WHILE:
			case IDENTIFIER:
			case FLOATCONSTANT:
			case INTCONSTANT:
			case BOOLCONSTANT:
			case INC_OP:
			case DEC_OP:
			case LEFT_PAREN:
			case SEMICOLON:
			case BANG:
			case DASH:
			case PLUS:
				enterOuterAlt(_localctx, 2);
				{
				setState(431);
				simple_statement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Simple_statementContext extends ParserRuleContext {
		public Declaration_statementContext declaration_statement() {
			return getRuleContext(Declaration_statementContext.class,0);
		}
		public Expression_statementContext expression_statement() {
			return getRuleContext(Expression_statementContext.class,0);
		}
		public Selection_statementContext selection_statement() {
			return getRuleContext(Selection_statementContext.class,0);
		}
		public Iteration_statementContext iteration_statement() {
			return getRuleContext(Iteration_statementContext.class,0);
		}
		public Jump_statementContext jump_statement() {
			return getRuleContext(Jump_statementContext.class,0);
		}
		public Simple_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simple_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterSimple_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitSimple_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitSimple_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Simple_statementContext simple_statement() throws RecognitionException {
		Simple_statementContext _localctx = new Simple_statementContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_simple_statement);
		try {
			setState(439);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(434);
				declaration_statement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(435);
				expression_statement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(436);
				selection_statement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(437);
				iteration_statement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(438);
				jump_statement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Compound_statement_with_scopeContext extends ParserRuleContext {
		public TerminalNode LEFT_BRACE() { return getToken(GLSLParser.LEFT_BRACE, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(GLSLParser.RIGHT_BRACE, 0); }
		public Statement_listContext statement_list() {
			return getRuleContext(Statement_listContext.class,0);
		}
		public Compound_statement_with_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound_statement_with_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterCompound_statement_with_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitCompound_statement_with_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitCompound_statement_with_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Compound_statement_with_scopeContext compound_statement_with_scope() throws RecognitionException {
		Compound_statement_with_scopeContext _localctx = new Compound_statement_with_scopeContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_compound_statement_with_scope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(441);
			match(LEFT_BRACE);
			setState(443);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ATTRIBUTE) | (1L << BOOL) | (1L << BREAK) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << CONST) | (1L << CONTINUE) | (1L << DISCARD) | (1L << DO) | (1L << FLOAT) | (1L << FOR) | (1L << HIGH_PRECISION) | (1L << IF) | (1L << IN) | (1L << INOUT) | (1L << INT) | (1L << INVARIANT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << LOW_PRECISION) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << MEDIUM_PRECISION) | (1L << OUT) | (1L << PRECISION) | (1L << RETURN) | (1L << SAMPLER2D) | (1L << SAMPLERCUBE) | (1L << STRUCT) | (1L << UNIFORM) | (1L << VARYING) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << VOID) | (1L << WHILE) | (1L << IDENTIFIER) | (1L << FLOATCONSTANT) | (1L << INTCONSTANT) | (1L << BOOLCONSTANT) | (1L << INC_OP) | (1L << DEC_OP) | (1L << LEFT_PAREN) | (1L << LEFT_BRACE))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (SEMICOLON - 69)) | (1L << (BANG - 69)) | (1L << (DASH - 69)) | (1L << (PLUS - 69)))) != 0)) {
				{
				setState(442);
				statement_list();
				}
			}

			setState(445);
			match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Statement_with_scopeContext extends ParserRuleContext {
		public Compound_statement_no_new_scopeContext compound_statement_no_new_scope() {
			return getRuleContext(Compound_statement_no_new_scopeContext.class,0);
		}
		public Simple_statementContext simple_statement() {
			return getRuleContext(Simple_statementContext.class,0);
		}
		public Statement_with_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement_with_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterStatement_with_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitStatement_with_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitStatement_with_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Statement_with_scopeContext statement_with_scope() throws RecognitionException {
		Statement_with_scopeContext _localctx = new Statement_with_scopeContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_statement_with_scope);
		try {
			setState(449);
			switch (_input.LA(1)) {
			case LEFT_BRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(447);
				compound_statement_no_new_scope();
				}
				break;
			case ATTRIBUTE:
			case BOOL:
			case BREAK:
			case BVEC2:
			case BVEC3:
			case BVEC4:
			case CONST:
			case CONTINUE:
			case DISCARD:
			case DO:
			case FLOAT:
			case FOR:
			case HIGH_PRECISION:
			case IF:
			case IN:
			case INOUT:
			case INT:
			case INVARIANT:
			case IVEC2:
			case IVEC3:
			case IVEC4:
			case LOW_PRECISION:
			case MAT2:
			case MAT3:
			case MAT4:
			case MEDIUM_PRECISION:
			case OUT:
			case PRECISION:
			case RETURN:
			case SAMPLER2D:
			case SAMPLERCUBE:
			case STRUCT:
			case UNIFORM:
			case VARYING:
			case VEC2:
			case VEC3:
			case VEC4:
			case VOID:
			case WHILE:
			case IDENTIFIER:
			case FLOATCONSTANT:
			case INTCONSTANT:
			case BOOLCONSTANT:
			case INC_OP:
			case DEC_OP:
			case LEFT_PAREN:
			case SEMICOLON:
			case BANG:
			case DASH:
			case PLUS:
				enterOuterAlt(_localctx, 2);
				{
				setState(448);
				simple_statement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Compound_statement_no_new_scopeContext extends ParserRuleContext {
		public TerminalNode LEFT_BRACE() { return getToken(GLSLParser.LEFT_BRACE, 0); }
		public TerminalNode RIGHT_BRACE() { return getToken(GLSLParser.RIGHT_BRACE, 0); }
		public Statement_listContext statement_list() {
			return getRuleContext(Statement_listContext.class,0);
		}
		public Compound_statement_no_new_scopeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound_statement_no_new_scope; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterCompound_statement_no_new_scope(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitCompound_statement_no_new_scope(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitCompound_statement_no_new_scope(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Compound_statement_no_new_scopeContext compound_statement_no_new_scope() throws RecognitionException {
		Compound_statement_no_new_scopeContext _localctx = new Compound_statement_no_new_scopeContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_compound_statement_no_new_scope);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
			match(LEFT_BRACE);
			setState(453);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ATTRIBUTE) | (1L << BOOL) | (1L << BREAK) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << CONST) | (1L << CONTINUE) | (1L << DISCARD) | (1L << DO) | (1L << FLOAT) | (1L << FOR) | (1L << HIGH_PRECISION) | (1L << IF) | (1L << IN) | (1L << INOUT) | (1L << INT) | (1L << INVARIANT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << LOW_PRECISION) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << MEDIUM_PRECISION) | (1L << OUT) | (1L << PRECISION) | (1L << RETURN) | (1L << SAMPLER2D) | (1L << SAMPLERCUBE) | (1L << STRUCT) | (1L << UNIFORM) | (1L << VARYING) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << VOID) | (1L << WHILE) | (1L << IDENTIFIER) | (1L << FLOATCONSTANT) | (1L << INTCONSTANT) | (1L << BOOLCONSTANT) | (1L << INC_OP) | (1L << DEC_OP) | (1L << LEFT_PAREN) | (1L << LEFT_BRACE))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (SEMICOLON - 69)) | (1L << (BANG - 69)) | (1L << (DASH - 69)) | (1L << (PLUS - 69)))) != 0)) {
				{
				setState(452);
				statement_list();
				}
			}

			setState(455);
			match(RIGHT_BRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Statement_listContext extends ParserRuleContext {
		public List<Statement_no_new_scopeContext> statement_no_new_scope() {
			return getRuleContexts(Statement_no_new_scopeContext.class);
		}
		public Statement_no_new_scopeContext statement_no_new_scope(int i) {
			return getRuleContext(Statement_no_new_scopeContext.class,i);
		}
		public Statement_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterStatement_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitStatement_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitStatement_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Statement_listContext statement_list() throws RecognitionException {
		Statement_listContext _localctx = new Statement_listContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_statement_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(458); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(457);
				statement_no_new_scope();
				}
				}
				setState(460); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ATTRIBUTE) | (1L << BOOL) | (1L << BREAK) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << CONST) | (1L << CONTINUE) | (1L << DISCARD) | (1L << DO) | (1L << FLOAT) | (1L << FOR) | (1L << HIGH_PRECISION) | (1L << IF) | (1L << IN) | (1L << INOUT) | (1L << INT) | (1L << INVARIANT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << LOW_PRECISION) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << MEDIUM_PRECISION) | (1L << OUT) | (1L << PRECISION) | (1L << RETURN) | (1L << SAMPLER2D) | (1L << SAMPLERCUBE) | (1L << STRUCT) | (1L << UNIFORM) | (1L << VARYING) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << VOID) | (1L << WHILE) | (1L << IDENTIFIER) | (1L << FLOATCONSTANT) | (1L << INTCONSTANT) | (1L << BOOLCONSTANT) | (1L << INC_OP) | (1L << DEC_OP) | (1L << LEFT_PAREN) | (1L << LEFT_BRACE))) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & ((1L << (SEMICOLON - 69)) | (1L << (BANG - 69)) | (1L << (DASH - 69)) | (1L << (PLUS - 69)))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Expression_statementContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(GLSLParser.SEMICOLON, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Expression_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterExpression_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitExpression_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitExpression_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Expression_statementContext expression_statement() throws RecognitionException {
		Expression_statementContext _localctx = new Expression_statementContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_expression_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(463);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << FLOAT) | (1L << INT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << IDENTIFIER) | (1L << FLOATCONSTANT) | (1L << INTCONSTANT) | (1L << BOOLCONSTANT) | (1L << INC_OP) | (1L << DEC_OP) | (1L << LEFT_PAREN))) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (BANG - 70)) | (1L << (DASH - 70)) | (1L << (PLUS - 70)))) != 0)) {
				{
				setState(462);
				expression();
				}
			}

			setState(465);
			match(SEMICOLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Selection_statementContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(GLSLParser.IF, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(GLSLParser.LEFT_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(GLSLParser.RIGHT_PAREN, 0); }
		public List<Statement_with_scopeContext> statement_with_scope() {
			return getRuleContexts(Statement_with_scopeContext.class);
		}
		public Statement_with_scopeContext statement_with_scope(int i) {
			return getRuleContext(Statement_with_scopeContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(GLSLParser.ELSE, 0); }
		public Selection_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selection_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterSelection_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitSelection_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitSelection_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Selection_statementContext selection_statement() throws RecognitionException {
		Selection_statementContext _localctx = new Selection_statementContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_selection_statement);
		try {
			setState(481);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(467);
				match(IF);
				setState(468);
				match(LEFT_PAREN);
				setState(469);
				expression();
				setState(470);
				match(RIGHT_PAREN);
				setState(471);
				statement_with_scope();
				setState(472);
				match(ELSE);
				setState(473);
				statement_with_scope();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(475);
				match(IF);
				setState(476);
				match(LEFT_PAREN);
				setState(477);
				expression();
				setState(478);
				match(RIGHT_PAREN);
				setState(479);
				statement_with_scope();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConditionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public Fully_specified_typeContext fully_specified_type() {
			return getRuleContext(Fully_specified_typeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public TerminalNode EQUAL() { return getToken(GLSLParser.EQUAL, 0); }
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_condition);
		try {
			setState(489);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,45,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(483);
				expression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(484);
				fully_specified_type();
				setState(485);
				match(IDENTIFIER);
				setState(486);
				match(EQUAL);
				setState(487);
				initializer();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Iteration_statementContext extends ParserRuleContext {
		public Iteration_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iteration_statement; }
	 
		public Iteration_statementContext() { }
		public void copyFrom(Iteration_statementContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class WhileIterationStatementContext extends Iteration_statementContext {
		public TerminalNode WHILE() { return getToken(GLSLParser.WHILE, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(GLSLParser.LEFT_PAREN, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(GLSLParser.RIGHT_PAREN, 0); }
		public Statement_no_new_scopeContext statement_no_new_scope() {
			return getRuleContext(Statement_no_new_scopeContext.class,0);
		}
		public WhileIterationStatementContext(Iteration_statementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterWhileIterationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitWhileIterationStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitWhileIterationStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ForIterationStatementContext extends Iteration_statementContext {
		public TerminalNode FOR() { return getToken(GLSLParser.FOR, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(GLSLParser.LEFT_PAREN, 0); }
		public For_init_statementContext for_init_statement() {
			return getRuleContext(For_init_statementContext.class,0);
		}
		public For_rest_statementContext for_rest_statement() {
			return getRuleContext(For_rest_statementContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(GLSLParser.RIGHT_PAREN, 0); }
		public Statement_no_new_scopeContext statement_no_new_scope() {
			return getRuleContext(Statement_no_new_scopeContext.class,0);
		}
		public ForIterationStatementContext(Iteration_statementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterForIterationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitForIterationStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitForIterationStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DoIterationStatementContext extends Iteration_statementContext {
		public TerminalNode DO() { return getToken(GLSLParser.DO, 0); }
		public Statement_with_scopeContext statement_with_scope() {
			return getRuleContext(Statement_with_scopeContext.class,0);
		}
		public TerminalNode WHILE() { return getToken(GLSLParser.WHILE, 0); }
		public TerminalNode LEFT_PAREN() { return getToken(GLSLParser.LEFT_PAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RIGHT_PAREN() { return getToken(GLSLParser.RIGHT_PAREN, 0); }
		public TerminalNode SEMICOLON() { return getToken(GLSLParser.SEMICOLON, 0); }
		public DoIterationStatementContext(Iteration_statementContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterDoIterationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitDoIterationStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitDoIterationStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Iteration_statementContext iteration_statement() throws RecognitionException {
		Iteration_statementContext _localctx = new Iteration_statementContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_iteration_statement);
		try {
			setState(512);
			switch (_input.LA(1)) {
			case WHILE:
				_localctx = new WhileIterationStatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(491);
				match(WHILE);
				setState(492);
				match(LEFT_PAREN);
				setState(493);
				condition();
				setState(494);
				match(RIGHT_PAREN);
				setState(495);
				statement_no_new_scope();
				}
				break;
			case DO:
				_localctx = new DoIterationStatementContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(497);
				match(DO);
				setState(498);
				statement_with_scope();
				setState(499);
				match(WHILE);
				setState(500);
				match(LEFT_PAREN);
				setState(501);
				expression();
				setState(502);
				match(RIGHT_PAREN);
				setState(503);
				match(SEMICOLON);
				}
				break;
			case FOR:
				_localctx = new ForIterationStatementContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(505);
				match(FOR);
				setState(506);
				match(LEFT_PAREN);
				setState(507);
				for_init_statement();
				setState(508);
				for_rest_statement();
				setState(509);
				match(RIGHT_PAREN);
				setState(510);
				statement_no_new_scope();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_init_statementContext extends ParserRuleContext {
		public Expression_statementContext expression_statement() {
			return getRuleContext(Expression_statementContext.class,0);
		}
		public Declaration_statementContext declaration_statement() {
			return getRuleContext(Declaration_statementContext.class,0);
		}
		public For_init_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_init_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFor_init_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFor_init_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFor_init_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final For_init_statementContext for_init_statement() throws RecognitionException {
		For_init_statementContext _localctx = new For_init_statementContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_for_init_statement);
		try {
			setState(516);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(514);
				expression_statement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(515);
				declaration_statement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class For_rest_statementContext extends ParserRuleContext {
		public TerminalNode SEMICOLON() { return getToken(GLSLParser.SEMICOLON, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public For_rest_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_rest_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFor_rest_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFor_rest_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFor_rest_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final For_rest_statementContext for_rest_statement() throws RecognitionException {
		For_rest_statementContext _localctx = new For_rest_statementContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_for_rest_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(519);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ATTRIBUTE) | (1L << BOOL) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << CONST) | (1L << FLOAT) | (1L << HIGH_PRECISION) | (1L << IN) | (1L << INOUT) | (1L << INT) | (1L << INVARIANT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << LOW_PRECISION) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << MEDIUM_PRECISION) | (1L << OUT) | (1L << SAMPLER2D) | (1L << SAMPLERCUBE) | (1L << STRUCT) | (1L << UNIFORM) | (1L << VARYING) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << VOID) | (1L << IDENTIFIER) | (1L << FLOATCONSTANT) | (1L << INTCONSTANT) | (1L << BOOLCONSTANT) | (1L << INC_OP) | (1L << DEC_OP) | (1L << LEFT_PAREN))) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (BANG - 70)) | (1L << (DASH - 70)) | (1L << (PLUS - 70)))) != 0)) {
				{
				setState(518);
				condition();
				}
			}

			setState(521);
			match(SEMICOLON);
			setState(523);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << FLOAT) | (1L << INT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << IDENTIFIER) | (1L << FLOATCONSTANT) | (1L << INTCONSTANT) | (1L << BOOLCONSTANT) | (1L << INC_OP) | (1L << DEC_OP) | (1L << LEFT_PAREN))) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (BANG - 70)) | (1L << (DASH - 70)) | (1L << (PLUS - 70)))) != 0)) {
				{
				setState(522);
				expression();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Jump_statementContext extends ParserRuleContext {
		public TerminalNode CONTINUE() { return getToken(GLSLParser.CONTINUE, 0); }
		public TerminalNode SEMICOLON() { return getToken(GLSLParser.SEMICOLON, 0); }
		public TerminalNode BREAK() { return getToken(GLSLParser.BREAK, 0); }
		public TerminalNode RETURN() { return getToken(GLSLParser.RETURN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DISCARD() { return getToken(GLSLParser.DISCARD, 0); }
		public Jump_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jump_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterJump_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitJump_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitJump_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Jump_statementContext jump_statement() throws RecognitionException {
		Jump_statementContext _localctx = new Jump_statementContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_jump_statement);
		int _la;
		try {
			setState(536);
			switch (_input.LA(1)) {
			case CONTINUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(525);
				match(CONTINUE);
				setState(526);
				match(SEMICOLON);
				}
				break;
			case BREAK:
				enterOuterAlt(_localctx, 2);
				{
				setState(527);
				match(BREAK);
				setState(528);
				match(SEMICOLON);
				}
				break;
			case RETURN:
				enterOuterAlt(_localctx, 3);
				{
				setState(529);
				match(RETURN);
				setState(531);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << BOOL) | (1L << BVEC2) | (1L << BVEC3) | (1L << BVEC4) | (1L << FLOAT) | (1L << INT) | (1L << IVEC2) | (1L << IVEC3) | (1L << IVEC4) | (1L << MAT2) | (1L << MAT3) | (1L << MAT4) | (1L << VEC2) | (1L << VEC3) | (1L << VEC4) | (1L << IDENTIFIER) | (1L << FLOATCONSTANT) | (1L << INTCONSTANT) | (1L << BOOLCONSTANT) | (1L << INC_OP) | (1L << DEC_OP) | (1L << LEFT_PAREN))) != 0) || ((((_la - 70)) & ~0x3f) == 0 && ((1L << (_la - 70)) & ((1L << (BANG - 70)) | (1L << (DASH - 70)) | (1L << (PLUS - 70)))) != 0)) {
					{
					setState(530);
					expression();
					}
				}

				setState(533);
				match(SEMICOLON);
				}
				break;
			case DISCARD:
				enterOuterAlt(_localctx, 4);
				{
				setState(534);
				match(DISCARD);
				setState(535);
				match(SEMICOLON);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class External_declarationContext extends ParserRuleContext {
		public Function_definitionContext function_definition() {
			return getRuleContext(Function_definitionContext.class,0);
		}
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public External_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_external_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterExternal_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitExternal_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitExternal_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final External_declarationContext external_declaration() throws RecognitionException {
		External_declarationContext _localctx = new External_declarationContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_external_declaration);
		try {
			setState(540);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,52,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(538);
				function_definition();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(539);
				declaration();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Function_definitionContext extends ParserRuleContext {
		public Function_prototypeContext function_prototype() {
			return getRuleContext(Function_prototypeContext.class,0);
		}
		public Compound_statement_no_new_scopeContext compound_statement_no_new_scope() {
			return getRuleContext(Compound_statement_no_new_scopeContext.class,0);
		}
		public Function_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterFunction_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitFunction_definition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitFunction_definition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Function_definitionContext function_definition() throws RecognitionException {
		Function_definitionContext _localctx = new Function_definitionContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_function_definition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(542);
			function_prototype();
			setState(543);
			compound_statement_no_new_scope();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Field_selectionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(GLSLParser.IDENTIFIER, 0); }
		public Field_selectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field_selection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).enterField_selection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof GLSLListener ) ((GLSLListener)listener).exitField_selection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof GLSLVisitor ) return ((GLSLVisitor<? extends T>)visitor).visitField_selection(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Field_selectionContext field_selection() throws RecognitionException {
		Field_selectionContext _localctx = new Field_selectionContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_field_selection);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(545);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 3:
			return postfix_expression_sempred((Postfix_expressionContext)_localctx, predIndex);
		case 12:
			return numeric_expression_sempred((Numeric_expressionContext)_localctx, predIndex);
		case 13:
			return logical_expression_sempred((Logical_expressionContext)_localctx, predIndex);
		case 25:
			return init_declarator_list_sempred((Init_declarator_listContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean postfix_expression_sempred(Postfix_expressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		case 2:
			return precpred(_ctx, 2);
		case 3:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean numeric_expression_sempred(Numeric_expressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 4);
		case 5:
			return precpred(_ctx, 3);
		case 6:
			return precpred(_ctx, 2);
		case 7:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean logical_expression_sempred(Logical_expressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 8:
			return precpred(_ctx, 6);
		case 9:
			return precpred(_ctx, 5);
		case 10:
			return precpred(_ctx, 4);
		case 11:
			return precpred(_ctx, 3);
		case 12:
			return precpred(_ctx, 2);
		case 13:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean init_declarator_list_sempred(Init_declarator_listContext _localctx, int predIndex) {
		switch (predIndex) {
		case 14:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3W\u0226\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\3\2\7\2t\n\2\f\2\16\2w\13"+
		"\2\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4\u0085\n\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\7\5\u0096\n\5\f"+
		"\5\16\5\u0099\13\5\3\6\3\6\5\6\u009d\n\6\3\7\3\7\3\b\3\b\5\b\u00a3\n\b"+
		"\3\b\3\b\3\b\7\b\u00a8\n\b\f\b\16\b\u00ab\13\b\5\b\u00ad\n\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00be\n\f\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\7\16\u00d1\n\16\f\16\16\16\u00d4\13\16\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\7\17\u00eb\n\17\f\17\16\17\u00ee\13\17\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\5\20\u00f6\n\20\3\21\3\21\3\21\3\21\3\21\5\21\u00fd\n"+
		"\21\3\22\3\22\3\23\3\23\3\23\7\23\u0104\n\23\f\23\16\23\u0107\13\23\3"+
		"\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u0116"+
		"\n\25\3\26\3\26\3\26\3\27\3\27\3\27\3\27\7\27\u011f\n\27\f\27\16\27\u0122"+
		"\13\27\5\27\u0124\n\27\3\30\3\30\3\30\3\30\3\31\5\31\u012b\n\31\3\31\3"+
		"\31\3\31\5\31\u0130\n\31\3\31\5\31\u0133\n\31\3\31\5\31\u0136\n\31\3\32"+
		"\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\5\33\u0141\n\33\3\33\3\33\5\33"+
		"\u0145\n\33\7\33\u0147\n\33\f\33\16\33\u014a\13\33\3\34\3\34\3\34\3\34"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\5\35\u0156\n\35\3\35\3\35\5\35\u015a\n"+
		"\35\5\35\u015c\n\35\3\36\3\36\3\36\3\36\5\36\u0162\n\36\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u016d\n\37\3 \3 \3 \3 \5 \u0173\n"+
		" \3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\5!\u0189"+
		"\n!\3\"\3\"\3#\3#\5#\u018f\n#\3#\3#\3#\3#\3$\6$\u0196\n$\r$\16$\u0197"+
		"\3%\3%\3%\3%\3&\3&\3&\7&\u01a1\n&\f&\16&\u01a4\13&\3\'\3\'\3\'\3\'\3\'"+
		"\5\'\u01ab\n\'\3(\3(\3)\3)\3*\3*\5*\u01b3\n*\3+\3+\3+\3+\3+\5+\u01ba\n"+
		"+\3,\3,\5,\u01be\n,\3,\3,\3-\3-\5-\u01c4\n-\3.\3.\5.\u01c8\n.\3.\3.\3"+
		"/\6/\u01cd\n/\r/\16/\u01ce\3\60\5\60\u01d2\n\60\3\60\3\60\3\61\3\61\3"+
		"\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\3\61\5\61\u01e4"+
		"\n\61\3\62\3\62\3\62\3\62\3\62\3\62\5\62\u01ec\n\62\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\3\63\3\63\5\63\u0203\n\63\3\64\3\64\5\64\u0207\n\64\3\65\5\65\u020a"+
		"\n\65\3\65\3\65\5\65\u020e\n\65\3\66\3\66\3\66\3\66\3\66\3\66\5\66\u0216"+
		"\n\66\3\66\3\66\3\66\5\66\u021b\n\66\3\67\3\67\5\67\u021f\n\67\38\38\3"+
		"8\39\39\39\2\6\b\32\34\64:\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$"+
		"&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhjlnp\2\t\n\2\4\4\6\b\16\16"+
		"\24\24\26\30\32\34&(++\3\2/\60\4\2HIKK\4\2\61\62OP\5\28:<<FF\4\2\22\23"+
		"\36\36\5\2\20\20\31\31\35\35\u024e\2u\3\2\2\2\4z\3\2\2\2\6\u0084\3\2\2"+
		"\2\b\u0086\3\2\2\2\n\u009c\3\2\2\2\f\u009e\3\2\2\2\16\u00a0\3\2\2\2\20"+
		"\u00b0\3\2\2\2\22\u00b3\3\2\2\2\24\u00b5\3\2\2\2\26\u00bd\3\2\2\2\30\u00bf"+
		"\3\2\2\2\32\u00c1\3\2\2\2\34\u00d5\3\2\2\2\36\u00ef\3\2\2\2 \u00fc\3\2"+
		"\2\2\"\u00fe\3\2\2\2$\u0100\3\2\2\2&\u0108\3\2\2\2(\u0115\3\2\2\2*\u0117"+
		"\3\2\2\2,\u011a\3\2\2\2.\u0125\3\2\2\2\60\u0135\3\2\2\2\62\u0137\3\2\2"+
		"\2\64\u0139\3\2\2\2\66\u014b\3\2\2\28\u015b\3\2\2\2:\u0161\3\2\2\2<\u016c"+
		"\3\2\2\2>\u0172\3\2\2\2@\u0188\3\2\2\2B\u018a\3\2\2\2D\u018c\3\2\2\2F"+
		"\u0195\3\2\2\2H\u0199\3\2\2\2J\u019d\3\2\2\2L\u01a5\3\2\2\2N\u01ac\3\2"+
		"\2\2P\u01ae\3\2\2\2R\u01b2\3\2\2\2T\u01b9\3\2\2\2V\u01bb\3\2\2\2X\u01c3"+
		"\3\2\2\2Z\u01c5\3\2\2\2\\\u01cc\3\2\2\2^\u01d1\3\2\2\2`\u01e3\3\2\2\2"+
		"b\u01eb\3\2\2\2d\u0202\3\2\2\2f\u0206\3\2\2\2h\u0209\3\2\2\2j\u021a\3"+
		"\2\2\2l\u021e\3\2\2\2n\u0220\3\2\2\2p\u0223\3\2\2\2rt\5l\67\2sr\3\2\2"+
		"\2tw\3\2\2\2us\3\2\2\2uv\3\2\2\2vx\3\2\2\2wu\3\2\2\2xy\7\2\2\3y\3\3\2"+
		"\2\2z{\7+\2\2{\5\3\2\2\2|\u0085\7-\2\2}\u0085\7,\2\2~\u0085\7.\2\2\177"+
		"\u0085\5\4\3\2\u0080\u0081\7=\2\2\u0081\u0082\5$\23\2\u0082\u0083\7>\2"+
		"\2\u0083\u0085\3\2\2\2\u0084|\3\2\2\2\u0084}\3\2\2\2\u0084~\3\2\2\2\u0084"+
		"\177\3\2\2\2\u0084\u0080\3\2\2\2\u0085\7\3\2\2\2\u0086\u0087\b\5\1\2\u0087"+
		"\u0088\5\n\6\2\u0088\u0097\3\2\2\2\u0089\u008a\f\6\2\2\u008a\u008b\7?"+
		"\2\2\u008b\u008c\5\f\7\2\u008c\u008d\7@\2\2\u008d\u0096\3\2\2\2\u008e"+
		"\u008f\f\5\2\2\u008f\u0090\7C\2\2\u0090\u0096\5p9\2\u0091\u0092\f\4\2"+
		"\2\u0092\u0096\7/\2\2\u0093\u0094\f\3\2\2\u0094\u0096\7\60\2\2\u0095\u0089"+
		"\3\2\2\2\u0095\u008e\3\2\2\2\u0095\u0091\3\2\2\2\u0095\u0093\3\2\2\2\u0096"+
		"\u0099\3\2\2\2\u0097\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\t\3\2\2\2"+
		"\u0099\u0097\3\2\2\2\u009a\u009d\5\6\4\2\u009b\u009d\5\16\b\2\u009c\u009a"+
		"\3\2\2\2\u009c\u009b\3\2\2\2\u009d\13\3\2\2\2\u009e\u009f\5$\23\2\u009f"+
		"\r\3\2\2\2\u00a0\u00ac\5\20\t\2\u00a1\u00a3\7)\2\2\u00a2\u00a1\3\2\2\2"+
		"\u00a2\u00a3\3\2\2\2\u00a3\u00ad\3\2\2\2\u00a4\u00a9\5 \21\2\u00a5\u00a6"+
		"\7D\2\2\u00a6\u00a8\5 \21\2\u00a7\u00a5\3\2\2\2\u00a8\u00ab\3\2\2\2\u00a9"+
		"\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00ad\3\2\2\2\u00ab\u00a9\3\2"+
		"\2\2\u00ac\u00a2\3\2\2\2\u00ac\u00a4\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae"+
		"\u00af\7>\2\2\u00af\17\3\2\2\2\u00b0\u00b1\5\22\n\2\u00b1\u00b2\7=\2\2"+
		"\u00b2\21\3\2\2\2\u00b3\u00b4\5\24\13\2\u00b4\23\3\2\2\2\u00b5\u00b6\t"+
		"\2\2\2\u00b6\25\3\2\2\2\u00b7\u00be\5\b\5\2\u00b8\u00b9\t\3\2\2\u00b9"+
		"\u00be\5\26\f\2\u00ba\u00bb\5\30\r\2\u00bb\u00bc\5\26\f\2\u00bc\u00be"+
		"\3\2\2\2\u00bd\u00b7\3\2\2\2\u00bd\u00b8\3\2\2\2\u00bd\u00ba\3\2\2\2\u00be"+
		"\27\3\2\2\2\u00bf\u00c0\t\4\2\2\u00c0\31\3\2\2\2\u00c1\u00c2\b\16\1\2"+
		"\u00c2\u00c3\5\26\f\2\u00c3\u00d2\3\2\2\2\u00c4\u00c5\f\6\2\2\u00c5\u00c6"+
		"\7L\2\2\u00c6\u00d1\5\32\16\7\u00c7\u00c8\f\5\2\2\u00c8\u00c9\7M\2\2\u00c9"+
		"\u00d1\5\32\16\6\u00ca\u00cb\f\4\2\2\u00cb\u00cc\7K\2\2\u00cc\u00d1\5"+
		"\32\16\5\u00cd\u00ce\f\3\2\2\u00ce\u00cf\7I\2\2\u00cf\u00d1\5\32\16\4"+
		"\u00d0\u00c4\3\2\2\2\u00d0\u00c7\3\2\2\2\u00d0\u00ca\3\2\2\2\u00d0\u00cd"+
		"\3\2\2\2\u00d1\u00d4\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3"+
		"\33\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d5\u00d6\b\17\1\2\u00d6\u00d7\5\32"+
		"\16\2\u00d7\u00ec\3\2\2\2\u00d8\u00d9\f\b\2\2\u00d9\u00da\t\5\2\2\u00da"+
		"\u00eb\5\34\17\t\u00db\u00dc\f\7\2\2\u00dc\u00dd\7\63\2\2\u00dd\u00eb"+
		"\5\34\17\b\u00de\u00df\f\6\2\2\u00df\u00e0\7\64\2\2\u00e0\u00eb\5\34\17"+
		"\7\u00e1\u00e2\f\5\2\2\u00e2\u00e3\7\65\2\2\u00e3\u00eb\5\34\17\6\u00e4"+
		"\u00e5\f\4\2\2\u00e5\u00e6\7\67\2\2\u00e6\u00eb\5\34\17\5\u00e7\u00e8"+
		"\f\3\2\2\u00e8\u00e9\7\66\2\2\u00e9\u00eb\5\34\17\4\u00ea\u00d8\3\2\2"+
		"\2\u00ea\u00db\3\2\2\2\u00ea\u00de\3\2\2\2\u00ea\u00e1\3\2\2\2\u00ea\u00e4"+
		"\3\2\2\2\u00ea\u00e7\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec"+
		"\u00ed\3\2\2\2\u00ed\35\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ef\u00f5\5\34\17"+
		"\2\u00f0\u00f1\7T\2\2\u00f1\u00f2\5$\23\2\u00f2\u00f3\7E\2\2\u00f3\u00f4"+
		"\5 \21\2\u00f4\u00f6\3\2\2\2\u00f5\u00f0\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6"+
		"\37\3\2\2\2\u00f7\u00fd\5\36\20\2\u00f8\u00f9\5\26\f\2\u00f9\u00fa\5\""+
		"\22\2\u00fa\u00fb\5 \21\2\u00fb\u00fd\3\2\2\2\u00fc\u00f7\3\2\2\2\u00fc"+
		"\u00f8\3\2\2\2\u00fd!\3\2\2\2\u00fe\u00ff\t\6\2\2\u00ff#\3\2\2\2\u0100"+
		"\u0105\5 \21\2\u0101\u0102\7D\2\2\u0102\u0104\5 \21\2\u0103\u0101\3\2"+
		"\2\2\u0104\u0107\3\2\2\2\u0105\u0103\3\2\2\2\u0105\u0106\3\2\2\2\u0106"+
		"%\3\2\2\2\u0107\u0105\3\2\2\2\u0108\u0109\5\36\20\2\u0109\'\3\2\2\2\u010a"+
		"\u010b\5*\26\2\u010b\u010c\7G\2\2\u010c\u0116\3\2\2\2\u010d\u010e\5\64"+
		"\33\2\u010e\u010f\7G\2\2\u010f\u0116\3\2\2\2\u0110\u0111\7\37\2\2\u0111"+
		"\u0112\5B\"\2\u0112\u0113\5@!\2\u0113\u0114\7G\2\2\u0114\u0116\3\2\2\2"+
		"\u0115\u010a\3\2\2\2\u0115\u010d\3\2\2\2\u0115\u0110\3\2\2\2\u0116)\3"+
		"\2\2\2\u0117\u0118\5,\27\2\u0118\u0119\7>\2\2\u0119+\3\2\2\2\u011a\u0123"+
		"\5.\30\2\u011b\u0120\5\60\31\2\u011c\u011d\7D\2\2\u011d\u011f\5\60\31"+
		"\2\u011e\u011c\3\2\2\2\u011f\u0122\3\2\2\2\u0120\u011e\3\2\2\2\u0120\u0121"+
		"\3\2\2\2\u0121\u0124\3\2\2\2\u0122\u0120\3\2\2\2\u0123\u011b\3\2\2\2\u0123"+
		"\u0124\3\2\2\2\u0124-\3\2\2\2\u0125\u0126\5:\36\2\u0126\u0127\7+\2\2\u0127"+
		"\u0128\7=\2\2\u0128/\3\2\2\2\u0129\u012b\5<\37\2\u012a\u0129\3\2\2\2\u012a"+
		"\u012b\3\2\2\2\u012b\u012c\3\2\2\2\u012c\u012d\5> \2\u012d\u012f\7+\2"+
		"\2\u012e\u0130\5\66\34\2\u012f\u012e\3\2\2\2\u012f\u0130\3\2\2\2\u0130"+
		"\u0136\3\2\2\2\u0131\u0133\5<\37\2\u0132\u0131\3\2\2\2\u0132\u0133\3\2"+
		"\2\2\u0133\u0134\3\2\2\2\u0134\u0136\5> \2\u0135\u012a\3\2\2\2\u0135\u0132"+
		"\3\2\2\2\u0136\61\3\2\2\2\u0137\u0138\t\7\2\2\u0138\63\3\2\2\2\u0139\u013a"+
		"\b\33\1\2\u013a\u013b\58\35\2\u013b\u0148\3\2\2\2\u013c\u013d\f\3\2\2"+
		"\u013d\u013e\7D\2\2\u013e\u0140\7+\2\2\u013f\u0141\5\66\34\2\u0140\u013f"+
		"\3\2\2\2\u0140\u0141\3\2\2\2\u0141\u0144\3\2\2\2\u0142\u0143\7F\2\2\u0143"+
		"\u0145\5N(\2\u0144\u0142\3\2\2\2\u0144\u0145\3\2\2\2\u0145\u0147\3\2\2"+
		"\2\u0146\u013c\3\2\2\2\u0147\u014a\3\2\2\2\u0148\u0146\3\2\2\2\u0148\u0149"+
		"\3\2\2\2\u0149\65\3\2\2\2\u014a\u0148\3\2\2\2\u014b\u014c\7?\2\2\u014c"+
		"\u014d\5&\24\2\u014d\u014e\7@\2\2\u014e\67\3\2\2\2\u014f\u0150\7\25\2"+
		"\2\u0150\u015c\7+\2\2\u0151\u015c\5:\36\2\u0152\u0153\5:\36\2\u0153\u0155"+
		"\7+\2\2\u0154\u0156\5\66\34\2\u0155\u0154\3\2\2\2\u0155\u0156\3\2\2\2"+
		"\u0156\u0159\3\2\2\2\u0157\u0158\7F\2\2\u0158\u015a\5N(\2\u0159\u0157"+
		"\3\2\2\2\u0159\u015a\3\2\2\2\u015a\u015c\3\2\2\2\u015b\u014f\3\2\2\2\u015b"+
		"\u0151\3\2\2\2\u015b\u0152\3\2\2\2\u015c9\3\2\2\2\u015d\u0162\5> \2\u015e"+
		"\u015f\5<\37\2\u015f\u0160\5> \2\u0160\u0162\3\2\2\2\u0161\u015d\3\2\2"+
		"\2\u0161\u015e\3\2\2\2\u0162;\3\2\2\2\u0163\u016d\7\t\2\2\u0164\u016d"+
		"\7\3\2\2\u0165\u016d\7%\2\2\u0166\u0167\7\25\2\2\u0167\u016d\7%\2\2\u0168"+
		"\u016d\7$\2\2\u0169\u016d\7\23\2\2\u016a\u016d\7\22\2\2\u016b\u016d\7"+
		"\36\2\2\u016c\u0163\3\2\2\2\u016c\u0164\3\2\2\2\u016c\u0165\3\2\2\2\u016c"+
		"\u0166\3\2\2\2\u016c\u0168\3\2\2\2\u016c\u0169\3\2\2\2\u016c\u016a\3\2"+
		"\2\2\u016c\u016b\3\2\2\2\u016d=\3\2\2\2\u016e\u0173\5@!\2\u016f\u0170"+
		"\5B\"\2\u0170\u0171\5@!\2\u0171\u0173\3\2\2\2\u0172\u016e\3\2\2\2\u0172"+
		"\u016f\3\2\2\2\u0173?\3\2\2\2\u0174\u0189\7)\2\2\u0175\u0189\7\16\2\2"+
		"\u0176\u0189\7\24\2\2\u0177\u0189\7\4\2\2\u0178\u0189\7&\2\2\u0179\u0189"+
		"\7\'\2\2\u017a\u0189\7(\2\2\u017b\u0189\7\6\2\2\u017c\u0189\7\7\2\2\u017d"+
		"\u0189\7\b\2\2\u017e\u0189\7\26\2\2\u017f\u0189\7\27\2\2\u0180\u0189\7"+
		"\30\2\2\u0181\u0189\7\32\2\2\u0182\u0189\7\33\2\2\u0183\u0189\7\34\2\2"+
		"\u0184\u0189\7!\2\2\u0185\u0189\7\"\2\2\u0186\u0189\5D#\2\u0187\u0189"+
		"\7+\2\2\u0188\u0174\3\2\2\2\u0188\u0175\3\2\2\2\u0188\u0176\3\2\2\2\u0188"+
		"\u0177\3\2\2\2\u0188\u0178\3\2\2\2\u0188\u0179\3\2\2\2\u0188\u017a\3\2"+
		"\2\2\u0188\u017b\3\2\2\2\u0188\u017c\3\2\2\2\u0188\u017d\3\2\2\2\u0188"+
		"\u017e\3\2\2\2\u0188\u017f\3\2\2\2\u0188\u0180\3\2\2\2\u0188\u0181\3\2"+
		"\2\2\u0188\u0182\3\2\2\2\u0188\u0183\3\2\2\2\u0188\u0184\3\2\2\2\u0188"+
		"\u0185\3\2\2\2\u0188\u0186\3\2\2\2\u0188\u0187\3\2\2\2\u0189A\3\2\2\2"+
		"\u018a\u018b\t\b\2\2\u018bC\3\2\2\2\u018c\u018e\7#\2\2\u018d\u018f\7+"+
		"\2\2\u018e\u018d\3\2\2\2\u018e\u018f\3\2\2\2\u018f\u0190\3\2\2\2\u0190"+
		"\u0191\7A\2\2\u0191\u0192\5F$\2\u0192\u0193\7B\2\2\u0193E\3\2\2\2\u0194"+
		"\u0196\5H%\2\u0195\u0194\3\2\2\2\u0196\u0197\3\2\2\2\u0197\u0195\3\2\2"+
		"\2\u0197\u0198\3\2\2\2\u0198G\3\2\2\2\u0199\u019a\5> \2\u019a\u019b\5"+
		"J&\2\u019b\u019c\7G\2\2\u019cI\3\2\2\2\u019d\u01a2\5L\'\2\u019e\u019f"+
		"\7D\2\2\u019f\u01a1\5L\'\2\u01a0\u019e\3\2\2\2\u01a1\u01a4\3\2\2\2\u01a2"+
		"\u01a0\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3K\3\2\2\2\u01a4\u01a2\3\2\2\2"+
		"\u01a5\u01aa\7+\2\2\u01a6\u01a7\7?\2\2\u01a7\u01a8\5&\24\2\u01a8\u01a9"+
		"\7@\2\2\u01a9\u01ab\3\2\2\2\u01aa\u01a6\3\2\2\2\u01aa\u01ab\3\2\2\2\u01ab"+
		"M\3\2\2\2\u01ac\u01ad\5 \21\2\u01adO\3\2\2\2\u01ae\u01af\5(\25\2\u01af"+
		"Q\3\2\2\2\u01b0\u01b3\5V,\2\u01b1\u01b3\5T+\2\u01b2\u01b0\3\2\2\2\u01b2"+
		"\u01b1\3\2\2\2\u01b3S\3\2\2\2\u01b4\u01ba\5P)\2\u01b5\u01ba\5^\60\2\u01b6"+
		"\u01ba\5`\61\2\u01b7\u01ba\5d\63\2\u01b8\u01ba\5j\66\2\u01b9\u01b4\3\2"+
		"\2\2\u01b9\u01b5\3\2\2\2\u01b9\u01b6\3\2\2\2\u01b9\u01b7\3\2\2\2\u01b9"+
		"\u01b8\3\2\2\2\u01baU\3\2\2\2\u01bb\u01bd\7A\2\2\u01bc\u01be\5\\/\2\u01bd"+
		"\u01bc\3\2\2\2\u01bd\u01be\3\2\2\2\u01be\u01bf\3\2\2\2\u01bf\u01c0\7B"+
		"\2\2\u01c0W\3\2\2\2\u01c1\u01c4\5Z.\2\u01c2\u01c4\5T+\2\u01c3\u01c1\3"+
		"\2\2\2\u01c3\u01c2\3\2\2\2\u01c4Y\3\2\2\2\u01c5\u01c7\7A\2\2\u01c6\u01c8"+
		"\5\\/\2\u01c7\u01c6\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8\u01c9\3\2\2\2\u01c9"+
		"\u01ca\7B\2\2\u01ca[\3\2\2\2\u01cb\u01cd\5R*\2\u01cc\u01cb\3\2\2\2\u01cd"+
		"\u01ce\3\2\2\2\u01ce\u01cc\3\2\2\2\u01ce\u01cf\3\2\2\2\u01cf]\3\2\2\2"+
		"\u01d0\u01d2\5$\23\2\u01d1\u01d0\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2\u01d3"+
		"\3\2\2\2\u01d3\u01d4\7G\2\2\u01d4_\3\2\2\2\u01d5\u01d6\7\21\2\2\u01d6"+
		"\u01d7\7=\2\2\u01d7\u01d8\5$\23\2\u01d8\u01d9\7>\2\2\u01d9\u01da\5X-\2"+
		"\u01da\u01db\7\r\2\2\u01db\u01dc\5X-\2\u01dc\u01e4\3\2\2\2\u01dd\u01de"+
		"\7\21\2\2\u01de\u01df\7=\2\2\u01df\u01e0\5$\23\2\u01e0\u01e1\7>\2\2\u01e1"+
		"\u01e2\5X-\2\u01e2\u01e4\3\2\2\2\u01e3\u01d5\3\2\2\2\u01e3\u01dd\3\2\2"+
		"\2\u01e4a\3\2\2\2\u01e5\u01ec\5$\23\2\u01e6\u01e7\5:\36\2\u01e7\u01e8"+
		"\7+\2\2\u01e8\u01e9\7F\2\2\u01e9\u01ea\5N(\2\u01ea\u01ec\3\2\2\2\u01eb"+
		"\u01e5\3\2\2\2\u01eb\u01e6\3\2\2\2\u01ecc\3\2\2\2\u01ed\u01ee\7*\2\2\u01ee"+
		"\u01ef\7=\2\2\u01ef\u01f0\5b\62\2\u01f0\u01f1\7>\2\2\u01f1\u01f2\5R*\2"+
		"\u01f2\u0203\3\2\2\2\u01f3\u01f4\7\f\2\2\u01f4\u01f5\5X-\2\u01f5\u01f6"+
		"\7*\2\2\u01f6\u01f7\7=\2\2\u01f7\u01f8\5$\23\2\u01f8\u01f9\7>\2\2\u01f9"+
		"\u01fa\7G\2\2\u01fa\u0203\3\2\2\2\u01fb\u01fc\7\17\2\2\u01fc\u01fd\7="+
		"\2\2\u01fd\u01fe\5f\64\2\u01fe\u01ff\5h\65\2\u01ff\u0200\7>\2\2\u0200"+
		"\u0201\5R*\2\u0201\u0203\3\2\2\2\u0202\u01ed\3\2\2\2\u0202\u01f3\3\2\2"+
		"\2\u0202\u01fb\3\2\2\2\u0203e\3\2\2\2\u0204\u0207\5^\60\2\u0205\u0207"+
		"\5P)\2\u0206\u0204\3\2\2\2\u0206\u0205\3\2\2\2\u0207g\3\2\2\2\u0208\u020a"+
		"\5b\62\2\u0209\u0208\3\2\2\2\u0209\u020a\3\2\2\2\u020a\u020b\3\2\2\2\u020b"+
		"\u020d\7G\2\2\u020c\u020e\5$\23\2\u020d\u020c\3\2\2\2\u020d\u020e\3\2"+
		"\2\2\u020ei\3\2\2\2\u020f\u0210\7\n\2\2\u0210\u021b\7G\2\2\u0211\u0212"+
		"\7\5\2\2\u0212\u021b\7G\2\2\u0213\u0215\7 \2\2\u0214\u0216\5$\23\2\u0215"+
		"\u0214\3\2\2\2\u0215\u0216\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u021b\7G"+
		"\2\2\u0218\u0219\7\13\2\2\u0219\u021b\7G\2\2\u021a\u020f\3\2\2\2\u021a"+
		"\u0211\3\2\2\2\u021a\u0213\3\2\2\2\u021a\u0218\3\2\2\2\u021bk\3\2\2\2"+
		"\u021c\u021f\5n8\2\u021d\u021f\5(\25\2\u021e\u021c\3\2\2\2\u021e\u021d"+
		"\3\2\2\2\u021fm\3\2\2\2\u0220\u0221\5*\26\2\u0221\u0222\5Z.\2\u0222o\3"+
		"\2\2\2\u0223\u0224\7+\2\2\u0224q\3\2\2\2\67u\u0084\u0095\u0097\u009c\u00a2"+
		"\u00a9\u00ac\u00bd\u00d0\u00d2\u00ea\u00ec\u00f5\u00fc\u0105\u0115\u0120"+
		"\u0123\u012a\u012f\u0132\u0135\u0140\u0144\u0148\u0155\u0159\u015b\u0161"+
		"\u016c\u0172\u0188\u018e\u0197\u01a2\u01aa\u01b2\u01b9\u01bd\u01c3\u01c7"+
		"\u01ce\u01d1\u01e3\u01eb\u0202\u0206\u0209\u020d\u0215\u021a\u021e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}