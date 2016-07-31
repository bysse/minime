grammar GLSL;

options {
    language = Java;
}

//@lexer::header  { package glsl_es; }
//@parser::header { package glsl_es; }

/* Main entry point */
translation_unit
  : ( external_declaration )* EOF
  ;

variable_identifier
  : IDENTIFIER
  ;

primary_expression
  : INTCONSTANT
  | FLOATCONSTANT
  | BOOLCONSTANT
  | variable_identifier
  | LEFT_PAREN expression RIGHT_PAREN
  ;

postfix_expression
  : primary_expression_or_function_call
  | postfix_expression LEFT_BRACKET integer_expression RIGHT_BRACKET
  | postfix_expression DOT field_selection
  | postfix_expression INC_OP
  | postfix_expression DEC_OP
  ;

primary_expression_or_function_call
  : primary_expression
  | function_call
  ;

integer_expression
  : expression
  ;

function_call
  : function_call_header
    (
        (VOID)?
      | assignment_expression (COMMA assignment_expression)*
    )
    RIGHT_PAREN
  ;

function_call_header
  : function_identifier LEFT_PAREN
  ;

// NOTE: change compared to GLSL ES grammar, because constructor_identifier
// has IDENTIFIER (=TYPE_NAME) as one of its arms.
function_identifier
  : constructor_identifier
//  | IDENTIFIER
  ;

// Grammar Note: Constructors look like functions, but lexical analysis recognized most of them as
// keywords.

constructor_identifier
  : FLOAT
  | INT
  | BOOL
  | VEC2
  | VEC3
  | VEC4
  | BVEC2
  | BVEC3
  | BVEC4
  | IVEC2
  | IVEC3
  | IVEC4
  | MAT2
  | MAT3
  | MAT4
//  | TYPE_NAME
  | IDENTIFIER
  ;

unary_expression
  : postfix_expression
  | (INC_OP | DEC_OP) unary_expression
  | unary_operator unary_expression
  ;

// Grammar Note:  No traditional style type casts.

unary_operator
  : PLUS
  | DASH
  | BANG
//| TILDE   // reserved
  ;

numeric_expression
  	: unary_expression																		# UnaryExpression
  	| numeric_expression STAR numeric_expression											# MultiplicationExpression
	| numeric_expression SLASH numeric_expression											# DivisionExpression
  	| numeric_expression PLUS numeric_expression											# AdditiveExpression
	| numeric_expression DASH numeric_expression											# SubtractionExpression
  	;

logical_expression
	: numeric_expression																	# NumericDelegator
	| logical_expression (LEFT_ANGLE | RIGHT_ANGLE | LE_OP | GE_OP) logical_expression		# Relational
	| logical_expression EQ_OP logical_expression											# LogicalEquality
	| logical_expression NE_OP logical_expression											# LogicalInEquality
	| logical_expression AND_OP logical_expression											# LogicalAnd
	| logical_expression XOR_OP logical_expression											# LogicalXor
  	| logical_expression OR_OP logical_expression											# LogicalOr
  ;

conditional_expression
  : logical_expression (QUESTION expression COLON assignment_expression)?
  ;

assignment_expression
  : conditional_expression
  | unary_expression assignment_operator assignment_expression
  ;

assignment_operator
  : EQUAL
  | MUL_ASSIGN
  | DIV_ASSIGN
//| MOD_ASSIGN   // reserved
  | ADD_ASSIGN
  | SUB_ASSIGN
//| LEFT_ASSIGN  // reserved
//| RIGHT_ASSIGN // reserved
//| AND_ASSIGN   // reserved
//| XOR_ASSIGN   // reserved
//| OR_ASSIGN    // reserved
  ;

expression
  : assignment_expression ( COMMA assignment_expression )*
  ;

constant_expression
  : conditional_expression
  ;

declaration
  : function_prototype SEMICOLON											# FunctionDeclaration
  | init_declarator_list SEMICOLON											# VariableDeclaration
  | PRECISION precision_qualifier type_specifier_no_prec SEMICOLON			# PrecisionDeclaration
  ;

function_prototype
  : function_declarator RIGHT_PAREN
  ;

function_declarator
  : function_header (parameter_declaration (COMMA parameter_declaration)* )?
  ;

function_header
  : fully_specified_type IDENTIFIER LEFT_PAREN
  ;

parameter_declaration
	: (type_qualifier)? type_specifier IDENTIFIER array_specifier?
  	| (type_qualifier)? type_specifier
	;

// NOTE: this originally had "empty" as one of the arms in the grammar

parameter_qualifier
  : IN
  | OUT
  | INOUT
  ;

init_declarator_list
  : single_declaration
  | init_declarator_list COMMA IDENTIFIER array_specifier? ( EQUAL initializer )?
  ;

array_specifier
  : LEFT_BRACKET constant_expression RIGHT_BRACKET
  ;

single_declaration
  : INVARIANT IDENTIFIER
  | fully_specified_type
  | fully_specified_type IDENTIFIER array_specifier? ( EQUAL initializer )?
  ;

// Grammar Note:  No 'enum', or 'typedef'.

fully_specified_type
  : type_specifier
  | type_qualifier type_specifier
  ;

type_qualifier
  : CONST
  | ATTRIBUTE   // Vertex only.
  | VARYING
  | INVARIANT VARYING
  | UNIFORM
  | INOUT
  | IN
  | OUT
  ;

type_specifier
  : type_specifier_no_prec
  | precision_qualifier type_specifier_no_prec
  ;

type_specifier_no_prec
  : VOID
  | FLOAT
  | INT
  | BOOL
  | VEC2
  | VEC3
  | VEC4
  | BVEC2
  | BVEC3
  | BVEC4
  | IVEC2
  | IVEC3
  | IVEC4
  | MAT2
  | MAT3
  | MAT4
  | SAMPLER2D
  | SAMPLERCUBE
  | struct_specifier
//  | TYPE_NAME
  | IDENTIFIER
  ;

precision_qualifier
  : HIGH_PRECISION
  | MEDIUM_PRECISION
  | LOW_PRECISION
  ;

struct_specifier
  : STRUCT (IDENTIFIER)? LEFT_BRACE struct_declaration_list RIGHT_BRACE
  ;

struct_declaration_list
  : (struct_declaration)+
  ;

struct_declaration
  : type_specifier struct_declarator_list SEMICOLON
  ;

struct_declarator_list
  : struct_declarator (COMMA struct_declarator)*
  ;

struct_declarator
  : IDENTIFIER (LEFT_BRACKET constant_expression RIGHT_BRACKET)?
  ;

initializer
  : assignment_expression
  ;

declaration_statement
  : declaration
  ;

statement_no_new_scope
  : compound_statement_with_scope
  | simple_statement
  ;

simple_statement
  : declaration_statement
  | expression_statement
  | selection_statement
  | iteration_statement
  | jump_statement
  ;

compound_statement_with_scope
  : LEFT_BRACE (statement_list)? RIGHT_BRACE
  ;

statement_with_scope
  : compound_statement_no_new_scope
  | simple_statement
  ;

compound_statement_no_new_scope
  : LEFT_BRACE (statement_list)? RIGHT_BRACE
  ;

statement_list
  : (statement_no_new_scope)+
  ;

expression_statement
  : (expression)? SEMICOLON
  ;

selection_statement
  : IF LEFT_PAREN expression RIGHT_PAREN statement_with_scope ELSE statement_with_scope
  | IF LEFT_PAREN expression RIGHT_PAREN statement_with_scope
  ;

condition
  : expression
  | fully_specified_type IDENTIFIER EQUAL initializer
  ;

iteration_statement
  : WHILE LEFT_PAREN condition RIGHT_PAREN statement_no_new_scope									# whileIterationStatement
  | DO statement_with_scope WHILE LEFT_PAREN expression RIGHT_PAREN SEMICOLON						# doIterationStatement
  | FOR LEFT_PAREN for_init_statement for_rest_statement RIGHT_PAREN statement_no_new_scope			# forIterationStatement
  ;

for_init_statement
  : expression_statement
  | declaration_statement
  ;

for_rest_statement
  : (condition)? SEMICOLON (expression)?
  ;

jump_statement
  : CONTINUE SEMICOLON
  | BREAK SEMICOLON
  | RETURN (expression)? SEMICOLON
  | DISCARD SEMICOLON   // Fragment shader only.
  ;

external_declaration
  : function_definition
  | declaration
  ;

function_definition
  : function_prototype compound_statement_no_new_scope
  ;

// ----------------------------------------------------------------------
// Keywords

ATTRIBUTE        : 'attribute';
BOOL             : 'bool';
BREAK            : 'break';
BVEC2            : 'bvec2';
BVEC3            : 'bvec3';
BVEC4            : 'bvec4';
CONST            : 'const';
CONTINUE         : 'continue';
DISCARD          : 'discard';
DO               : 'do';
ELSE             : 'else';
fragment FALSE   : 'false';
FLOAT            : 'float';
FOR              : 'for';
HIGH_PRECISION   : 'highp';
IF               : 'if';
IN               : 'in';
INOUT            : 'inout';
INT              : 'int';
INVARIANT        : 'invariant';
IVEC2            : 'ivec2';
IVEC3            : 'ivec3';
IVEC4            : 'ivec4';
LOW_PRECISION    : 'lowp';
MAT2             : 'mat2';
MAT3             : 'mat3';
MAT4             : 'mat4';
MEDIUM_PRECISION : 'mediump';
OUT              : 'out';
PRECISION        : 'precision';
RETURN           : 'return';
SAMPLER2D        : 'sampler2D';
SAMPLERCUBE      : 'samplerCube';
STRUCT           : 'struct';
fragment TRUE    : 'true';
UNIFORM          : 'uniform';
VARYING          : 'varying';
VEC2             : 'vec2';
VEC3             : 'vec3';
VEC4             : 'vec4';
VOID             : 'void';
WHILE            : 'while';

IDENTIFIER
  : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
  ;

/*
// TODO(kbr): it isn't clear whether we need to support the TYPE_NAME
// token type; that may only be needed if typedef is supported
TYPE_NAME
  : IDENTIFIER
  ;
*/

// NOTE difference in handling of leading minus sign compared to HLSL
// grammar

fragment EXPONENT_PART : ('e'|'E') (PLUS | DASH)? ('0'..'9')+ ;

FLOATCONSTANT
  : ('0'..'9')+ '.' ('0'..'9')* (EXPONENT_PART)?
  | '.' ('0'..'9')+ (EXPONENT_PART)?
  | ('0'..'9')+ EXPONENT_PART
  ;

fragment DECIMAL_CONSTANT
  : ('1'..'9')('0'..'9')*
  ;

fragment OCTAL_CONSTANT
  : '0' ('0'..'7')*
  ;

fragment HEXADECIMAL_CONSTANT
  : '0' ('x'|'X') HEXDIGIT+
  ;

fragment HEXDIGIT
  : ('0'..'9'|'a'..'f'|'A'..'F')
  ;

INTCONSTANT
  : DECIMAL_CONSTANT
  | OCTAL_CONSTANT
  | HEXADECIMAL_CONSTANT
  ;

BOOLCONSTANT
  : TRUE
  | FALSE
  ;

field_selection
  : IDENTIFIER
  ;

//LEFT_OP  : '<<';      - reserved
//RIGHT_OP : '>>';      - reserved

INC_OP           : '++';
DEC_OP           : '--';
LE_OP            : '<=';
GE_OP            : '>=';
EQ_OP            : '==';
NE_OP            : '!=';

AND_OP           : '&&';
OR_OP            : '||';
XOR_OP           : '^^';
MUL_ASSIGN       : '*=';
DIV_ASSIGN       : '/=';
ADD_ASSIGN       : '+=';
MOD_ASSIGN       : '%=';
// LEFT_ASSIGN   : '<<=';  - reserved
// RIGHT_ASSIGN  : '>>=';  - reserved
// AND_ASSIGN    : '&=';   - reserved
// XOR_ASSIGN    : '^=';   - reserved
// OR_ASSIGN     : '|=';   - reserved
SUB_ASSIGN       : '-=';

LEFT_PAREN       : '(';
RIGHT_PAREN      : ')';
LEFT_BRACKET     : '[';
RIGHT_BRACKET    : ']';
LEFT_BRACE       : '{';
RIGHT_BRACE      : '}';
DOT              : '.';

COMMA            : ',';
COLON            : ':';
EQUAL            : '=';
SEMICOLON        : ';';
BANG             : '!';
DASH             : '-';
TILDE            : '~';
PLUS             : '+';
STAR             : '*';
SLASH            : '/';
PERCENT          : '%';

LEFT_ANGLE       : '<';
RIGHT_ANGLE      : '>';
VERTICAL_BAR     : '|';
CARET            : '^';
AMPERSAND        : '&';
QUESTION         : '?';

// ----------------------------------------------------------------------
// skipped elements

WHITESPACE
  : ( ' ' | '\t' | '\f' | '\r' | '\n' ) -> skip
  ;

COMMENT
  : '//' (~('\n'|'\r'))* -> skip
  ;

MULTILINE_COMMENT
  : '/*' ( . )*? '*/'  -> skip
  ;

// ----------------------------------------------------------------------
// Keywords reserved for future use

//RESERVED_KEYWORDS
//  : 'asm'
//  | 'cast'
//  | 'class'
//  | 'default'
//  | 'double'
//  | 'dvec2'
//  | 'dvec3'
//  | 'dvec4'
//  | 'enum'
//  | 'extern'
//  | 'external'
//  | 'fixed'
//  | 'flat'
//  | 'fvec2'
//  | 'fvec3'
//  | 'fvec4'
//  | 'goto'
//  | 'half'
//  | 'hvec2'
//  | 'hvec3'
//  | 'hvec4'
//  | 'inline'
//  | 'input'
//  | 'interface'
//  | 'long'
//  | 'namespace'
//  | 'noinline'
//  | 'output'
//  | 'packed'
//  | 'public'
//  | 'sampler1D'
//  | 'sampler1DShadow'
//  | 'sampler2DRect'
//  | 'sampler2DRectShadow'
//  | 'sampler2DShadow'
//  | 'sampler3D'
//  | 'sampler3DRect'
//  | 'short'
//  | 'sizeof'
//  | 'static'
//  | 'superp'
//  | 'switch'
//  | 'template'
//  | 'this'
//  | 'typedef'
//  | 'union'
//  | 'unsigned'
//  | 'using'
//  | 'volatile'
//  ;