/**
 * GLSL 4.40
 */
grammar GLSL;

options {
    language = Java;
}

translation_unit
  : ( external_declaration )* EOF
  ;

external_declaration
  : function_definition
  | declaration
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
  : primary_expression
  | postfix_expression LEFT_BRACKET integer_expression RIGHT_BRACKET
  | function_call
  | postfix_expression DOT field_selection
  | postfix_expression INC_OP
  | postfix_expression DEC_OP
  ;

field_selection
  : IDENTIFIER
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

// Note: subroutines array calls are not supported. Constructors are catched through this rule as well.
function_identifier
  : type_specifier
  ;

unary_expression
  : postfix_expression
  | (INC_OP | DEC_OP) unary_expression
  | unary_operator unary_expression
  ;

unary_operator
  : PLUS
  | DASH
  | BANG
  | TILDE
  ;

numeric_expression
  	: unary_expression															        # unary_expression_delegate  // not pretty but makes the rule cleaner
  	| numeric_expression (STAR | SLASH | PERCENT) numeric_expression                    # multiplicative_expression
	| numeric_expression (PLUS | DASH) numeric_expression								# additive_expression
  	| numeric_expression (LEFT_OP | RIGHT_OP) numeric_expression						# shift_expression
  	;

logical_expression
	: numeric_expression																	# numeric_expression_delegate   // not pretty but makes the rule cleaner
	| logical_expression (LEFT_ANGLE | RIGHT_ANGLE | LE_OP | GE_OP) logical_expression		# relational_expression
	| logical_expression (EQ_OP | NE_OP) logical_expression									# equality_expression
	| logical_expression (AMPERSAND | CARET | VERTICAL_BAR) logical_expression              # bit_op_expression
	| logical_expression AND_OP logical_expression											# logical_and_expression
	| logical_expression XOR_OP logical_expression											# logical_xor_expression
  	| logical_expression OR_OP logical_expression											# logical_or_expression
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
  | MOD_ASSIGN
  | ADD_ASSIGN
  | SUB_ASSIGN
  | LEFT_ASSIGN
  | RIGHT_ASSIGN
  | AND_ASSIGN
  | XOR_ASSIGN
  | OR_ASSIGN
  ;

expression
  : assignment_expression ( COMMA assignment_expression )*
  ;

constant_expression
  : conditional_expression
  ;

declaration
  : function_prototype SEMICOLON											                                            # function_declaration
  | init_declarator_list SEMICOLON									    	            	                            # variable_declaration
  | PRECISION precision_qualifier type_specifier SEMICOLON		    	                                                # precision_declaration
  | type_qualifier IDENTIFIER LEFT_BRACE struct_declaration_list RIGHT_BRACE (IDENTIFIER)? (array_specifier)? SEMICOLON # struct_init_declaration
  | type_qualifier SEMICOLON                                                                                            # qualifier_declaration
  | type_qualifier IDENTIFIER (COMMA IDENTIFIER)* SEMICOLON                                                             # qualifier_declaration
  ;

function_definition
  : function_prototype compound_statement_no_new_scope
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
	: (type_qualifier)? type_specifier IDENTIFIER (array_specifier)?
  	| (type_qualifier)? type_specifier
	;

init_declarator_list
  : single_declaration
  | init_declarator_list COMMA IDENTIFIER (array_specifier)? ( EQUAL initializer )?
  ;

single_declaration
  : fully_specified_type
  | fully_specified_type IDENTIFIER (array_specifier)? ( EQUAL initializer )?
  ;

// Grammar Note:  No 'enum', or 'typedef'.

fully_specified_type
  : type_specifier
  | type_qualifier type_specifier
  ;

storage_qualifier
  : CONST
  | INOUT
  | IN
  | OUT
  | ATTRIBUTE
  | VARYING
  | CENTROID
  | PATCH
  | SAMPLE
  | UNIFORM
  | BUFFER
  | SHARED
  | SUBROUTINE (LEFT_PAREN type_name_list RIGHT_PAREN)?
  ;

memory_qualifier
  : COHERENT
  | VOLATILE
  | RESTRICT
  | READONLY
  | WRITEONLY
  ;

layout_qualifier
  : LAYOUT LEFT_PAREN layout_qualifier_id_list RIGHT_PAREN
  ;

layout_qualifier_id_list
  : layout_qualifier_id (COMMA layout_qualifier_id)*
  ;

layout_qualifier_id
  : IDENTIFIER (EQUAL constant_expression)?
  | SHARED
  ;

precision_qualifier
  : HIGH_PRECISION
  | MEDIUM_PRECISION
  | LOW_PRECISION
  ;

interpolation_qualifier
  : SMOOTH
  | FLAT
  | NOPERSPECTIVE
  ;

invariant_qualifier
  : INVARIANT
  ;

precise_qualifier
  : PRECISE
  ;

type_qualifier
  : single_type_qualifier
  | type_qualifier single_type_qualifier
  ;

single_type_qualifier
  : storage_qualifier
  | memory_qualifier
  | layout_qualifier
  | precision_qualifier
  | interpolation_qualifier
  | invariant_qualifier
  | precise_qualifier
  ;

type_name_list
  : IDENTIFIER (COMMA IDENTIFIER)*
  ;

type_specifier
  : type_specifier_no_array
  | type_specifier_no_array array_specifier
  ;

array_specifier
  : LEFT_BRACKET RIGHT_BRACKET
  | LEFT_BRACKET constant_expression RIGHT_BRACKET
  | array_specifier LEFT_BRACKET RIGHT_BRACKET
  | array_specifier LEFT_BRACKET constant_expression RIGHT_BRACKET
  ;

type_specifier_no_array
  : VOID
  | FLOAT
  | DOUBLE
  | INT
  | UINT
  | BOOL
  | VEC2   | VEC3   | VEC4
  | DVEC2  | DVEC3  | DVEC4
  | BVEC2  | BVEC3  | BVEC4
  | IVEC2  | IVEC3  | IVEC4
  | UVEC2  | UVEC3  | UVEC4
  | MAT2   | MAT3   | MAT4
  | MAT2X2 | MAT2X3 | MAT2X4
  | MAT3X2 | MAT3X3 | MAT3X4
  | MAT4X2 | MAT4X3 | MAT4X4
  | DMAT2  | DMAT3  | DMAT4
  | DMAT2X2| DMAT2X3| DMAT2X4
  | DMAT3X2| DMAT3X3| DMAT3X4
  | DMAT4X2| DMAT4X3| DMAT4X4
  | ATOMIC_UINT
  | SAMPLER1D            | SAMPLER2D         | SAMPLER3D
  | SAMPLERCUBE          | ISAMPLERCUBE      | USAMPLERCUBE
  | SAMPLER1DSHADOW      | SAMPLER2DSHADOW   | SAMPLERCUBESHADOW
  | SAMPLER1DARRAY       | SAMPLER2DARRAY
  | SAMPLER1DARRAYSHADOW | SAMPLER2DARRAYSHADOW
  | SAMPLERCUBEARRAY     | SAMPLERCUBEARRAYSHADOW
  | ISAMPLER1D           | ISAMPLER2D        | ISAMPLER3D
  | ISAMPLER1DARRAY      | ISAMPLER2DARRAY   | ISAMPLERCUBEARRAY
  | USAMPLER1D           | USAMPLER2D        | USAMPLER3D
  | USAMPLER1DARRAY      | USAMPLER2DARRAY   | USAMPLERCUBEARRAY
  | SAMPLER2DRECT        | SAMPLER2DRECTSHADOW
  | ISAMPLER2DRECT       | USAMPLER2DRECT
  | SAMPLERBUFFER        | ISAMPLERBUFFER    | USAMPLERBUFFER
  | SAMPLER2DMS          | ISAMPLER2DMS      | USAMPLER2DMS
  | SAMPLER2DMSARRAY     | ISAMPLER2DMSARRAY | USAMPLER2DMSARRAY
  | IMAGE1D        | IIMAGE1D        | UIMAGE1D
  | IMAGE2D        | IIMAGE2D        | UIMAGE2D
  | IMAGE3D        | IIMAGE3D        | UIMAGE3D
  | IMAGE2DRECT    | IIMAGE2DRECT    | UIMAGE2DRECT
  | IMAGECUBE      | IIMAGECUBE      | UIMAGECUBE
  | IMAGEBUFFER    | IIMAGEBUFFER    | UIMAGEBUFFER
  | IMAGE1DARRAY   | IIMAGE1DARRAY   | UIMAGE1DARRAY
  | IMAGE2DARRAY   | IIMAGE2DARRAY   | UIMAGE2DARRAY
  | IMAGECUBEARRAY | IIMAGECUBEARRAY | UIMAGECUBEARRAY
  | IMAGE2DMS      | IIMAGE2DMS      | UIMAGE2DMS
  | IMAGE2DMSARRAY | IIMAGE2DMSARRAY | UIMAGE2DMSARRAY
  | struct_specifier
  | IDENTIFIER          // this is the same as TYPE_NAME used by subroutines
  ;

struct_specifier
  : STRUCT (IDENTIFIER)? LEFT_BRACE struct_declaration_list RIGHT_BRACE
  ;

struct_declaration_list
  : struct_declaration
  | struct_declaration_list struct_declaration
  ;

struct_declaration
  : (type_qualifier)? type_specifier struct_declarator_list SEMICOLON
  ;

struct_declarator_list
  : struct_declarator (COMMA struct_declarator)*
  ;

struct_declarator
  : IDENTIFIER (array_specifier)?
  ;

initializer
  : assignment_expression
  | LEFT_BRACE initializer (COMMA initializer)* COMMA? RIGHT_BRACE
  ;

statement_no_new_scope
  : compound_statement_no_new_scope
  | simple_statement
  ;

statement_with_scope
  : compound_statement_no_new_scope
  | simple_statement
  ;

statement_list
  : (statement_no_new_scope)+
  ;

compound_statement_no_new_scope
  : LEFT_BRACE (statement_list)? RIGHT_BRACE
  ;

simple_statement
  : declaration_statement
  | expression_statement
  | selection_statement
  | switch_statement
  | iteration_statement
  | jump_statement
  ;

declaration_statement
  : declaration
  ;

expression_statement
  : (expression)? SEMICOLON
  ;

selection_statement
  : IF LEFT_PAREN expression RIGHT_PAREN statement_with_scope (ELSE statement_with_scope)?
  ;

condition
  : expression
  | fully_specified_type IDENTIFIER EQUAL initializer
  ;

switch_statement
  : SWITCH LEFT_PAREN expression RIGHT_PAREN LEFT_BRACE (switch_case_statement)+ RIGHT_BRACE
  ;

switch_case_statement
  : CASE expression COLON statement_with_scope
  | DEFAULT COLON statement_with_scope
  ;

iteration_statement
  : WHILE LEFT_PAREN condition RIGHT_PAREN statement_no_new_scope									# iteration_while_statement
  | DO statement_with_scope WHILE LEFT_PAREN expression RIGHT_PAREN SEMICOLON						# iteration_do_while_statement
  | FOR LEFT_PAREN for_init_statement for_rest_statement RIGHT_PAREN statement_no_new_scope			# iteration_for_statement
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
  | DISCARD SEMICOLON               // fragment shader only.
  ;

// ----------------------------------------------------------------------
// Keywords
// ----------------------------------------------------------------------

ATTRIBUTE        : 'attribute';
BREAK            : 'break';
BUFFER           : 'buffer';
CASE             : 'case';
CENTROID         : 'centroid';
COHERENT         : 'coherent';
CONST            : 'const';
CONTINUE         : 'continue';
DEFAULT          : 'default';
DISCARD          : 'discard';
DO               : 'do';
ELSE             : 'else';
fragment FALSE   : 'false';
FLAT             : 'flat';
FOR              : 'for';
HIGH_PRECISION   : 'highp';
IF               : 'if';
IN               : 'in';
INOUT            : 'inout';
INVARIANT        : 'invariant';
LAYOUT           : 'layout';
LOW_PRECISION    : 'lowp';
MEDIUM_PRECISION : 'mediump';
NOPERSPECTIVE    : 'noperspective';
OUT              : 'out';
PATCH            : 'patch';
PRECISE          : 'precise';
PRECISION        : 'precision';
READONLY         : 'readonly';
RESTRICT         : 'restrict';
RETURN           : 'return';
SAMPLE           : 'sample';
SHARED           : 'shared';
SMOOTH           : 'smooth';
STRUCT           : 'struct';
SUBROUTINE       : 'subroutine';
SWITCH           : 'switch';
fragment TRUE    : 'true';
UNIFORM          : 'uniform';
VARYING          : 'varying';
VOLATILE         : 'volatile';
WHILE            : 'while';
WRITEONLY        : 'writeonly';

// ----------------------------------------------------------------------
// Types
// ----------------------------------------------------------------------
VOID                   : 'void';
FLOAT                  : 'float';
DOUBLE                 : 'double';
INT                    : 'int';
UINT                   : 'uint';
BOOL                   : 'bool';
VEC2                   : 'vec2';
VEC3                   : 'vec3';
VEC4                   : 'vec4';
DVEC2                  : 'dvec2';
DVEC3                  : 'dvec3';
DVEC4                  : 'dvec4';
BVEC2                  : 'bvec2';
BVEC3                  : 'bvec3';
BVEC4                  : 'bvec4';
IVEC2                  : 'ivec2';
IVEC3                  : 'ivec3';
IVEC4                  : 'ivec4';
UVEC2                  : 'uvec2';
UVEC3                  : 'uvec3';
UVEC4                  : 'uvec4';
MAT2                   : 'mat2';
MAT3                   : 'mat3';
MAT4                   : 'mat4';
MAT2X2                 : 'mat2x2';
MAT2X3                 : 'mat2x3';
MAT2X4                 : 'mat2x4';
MAT3X2                 : 'mat3x2';
MAT3X3                 : 'mat3x3';
MAT3X4                 : 'mat3x4';
MAT4X2                 : 'mat4x2';
MAT4X3                 : 'mat4x3';
MAT4X4                 : 'mat4x4';
DMAT2                  : 'dmat2';
DMAT3                  : 'dmat3';
DMAT4                  : 'dmat4';
DMAT2X2                : 'dmat2x2';
DMAT2X3                : 'dmat2x3';
DMAT2X4                : 'dmat2x4';
DMAT3X2                : 'dmat3x2';
DMAT3X3                : 'dmat3x3';
DMAT3X4                : 'dmat3x4';
DMAT4X2                : 'dmat4x2';
DMAT4X3                : 'dmat4x3';
DMAT4X4                : 'dmat4x4';
ATOMIC_UINT            : 'atomic_uint';
SAMPLER1D              : 'sampler1D';
SAMPLER2D              : 'sampler2D';
SAMPLER3D              : 'sampler3D';
SAMPLERCUBE            : 'samplerCube';
SAMPLER1DSHADOW        : 'sampler1DShadow';
SAMPLER2DSHADOW        : 'sampler2DShadow';
SAMPLERCUBESHADOW      : 'samplerCubeShadow';
SAMPLER1DARRAY         : 'sampler1DArray';
SAMPLER2DARRAY         : 'sampler2DArray';
SAMPLER1DARRAYSHADOW   : 'sampler1DArrayShadow';
SAMPLER2DARRAYSHADOW   : 'sampler2DArrayShadow';
SAMPLERCUBEARRAY       : 'samplerCubeArray';
SAMPLERCUBEARRAYSHADOW : 'samplerCubeArrayShadow';
ISAMPLER1D             : 'isampler1D';
ISAMPLER2D             : 'isampler2D';
ISAMPLER3D             : 'isampler3D';
ISAMPLERCUBE           : 'isamplerCube';
ISAMPLER1DARRAY        : 'isampler1DArray';
ISAMPLER2DARRAY        : 'isampler2DArray';
ISAMPLERCUBEARRAY      : 'isamplerCubeArray';
USAMPLER1D             : 'usampler1D';
USAMPLER2D             : 'usampler2D';
USAMPLER3D             : 'usampler3D';
USAMPLERCUBE           : 'usamplerCube';
USAMPLER1DARRAY        : 'usampler1DArray';
USAMPLER2DARRAY        : 'usampler2DArray';
USAMPLERCUBEARRAY      : 'usamplerCubeArray';
SAMPLER2DRECT          : 'sampler2DRect';
SAMPLER2DRECTSHADOW    : 'sampler2DRectShadow';
ISAMPLER2DRECT         : 'isampler2DRect';
USAMPLER2DRECT         : 'usampler2DRect';
SAMPLERBUFFER          : 'samplerBuffer';
ISAMPLERBUFFER         : 'isamplerBuffer';
USAMPLERBUFFER         : 'usamplerBuffer';
SAMPLER2DMS            : 'sampler2DMS';
ISAMPLER2DMS           : 'isampler2DMS';
USAMPLER2DMS           : 'usampler2DMS';
SAMPLER2DMSARRAY       : 'sampler2DMSArray';
ISAMPLER2DMSARRAY      : 'isampler2DMSArray';
USAMPLER2DMSARRAY      : 'usampler2DMSArray';
IMAGE1D                : 'image1D';
IIMAGE1D               : 'iimage1D';
UIMAGE1D               : 'uimage1D';
IMAGE2D                : 'image2D';
IIMAGE2D               : 'iimage2D';
UIMAGE2D               : 'uimage2D';
IMAGE3D                : 'image3D';
IIMAGE3D               : 'iimage3D';
UIMAGE3D               : 'uimage3D';
IMAGE2DRECT            : 'image2DRect';
IIMAGE2DRECT           : 'iimage2DRect';
UIMAGE2DRECT           : 'uimage2DRect';
IMAGECUBE              : 'imageCube';
IIMAGECUBE             : 'iimageCube';
UIMAGECUBE             : 'uimageCube';
IMAGEBUFFER            : 'imageBuffer';
IIMAGEBUFFER           : 'iimageBuffer';
UIMAGEBUFFER           : 'uimageBuffer';
IMAGE1DARRAY           : 'image1DArray';
IIMAGE1DARRAY          : 'iimage1DArray';
UIMAGE1DARRAY          : 'uimage1DArray';
IMAGE2DARRAY           : 'image2DArray';
IIMAGE2DARRAY          : 'iimage2DArray';
UIMAGE2DARRAY          : 'uimage2DArray';
IMAGECUBEARRAY         : 'imageCubeArray';
IIMAGECUBEARRAY        : 'iimageCubeArray';
UIMAGECUBEARRAY        : 'uimageCubeArray';
IMAGE2DMS              : 'image2DMS';
IIMAGE2DMS             : 'iimage2DMS';
UIMAGE2DMS             : 'uimage2DMS';
IMAGE2DMSARRAY         : 'image2DMSArray';
IIMAGE2DMSARRAY        : 'iimage2DMSArray';
UIMAGE2DMSARRAY        : 'uimage2DMSArray';

// ----------------------------------------------------------------------
// Identifier
// ----------------------------------------------------------------------

IDENTIFIER
  : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
  ;

// ----------------------------------------------------------------------
// Numerical definitions
// ----------------------------------------------------------------------
BOOLCONSTANT
  : TRUE
  | FALSE
  ;

INTCONSTANT
  : DECIMAL_CONSTANT (INT_SUFFIX)?
  | OCTAL_CONSTANT (INT_SUFFIX)?
  | HEXADECIMAL_CONSTANT (INT_SUFFIX)?
  ;

FLOATCONSTANT
  : FRACTIONAL_CONSTANT (EXPONENT_PART)? (FLOAT_SUFFIX)?
  | DIGIT_SEQUENCE EXPONENT_PART (FLOAT_SUFFIX)?
  ;

fragment FRACTIONAL_CONSTANT
    : DIGIT_SEQUENCE '.' DIGIT_SEQUENCE
    | DIGIT_SEQUENCE '.'
    | '.' DIGIT_SEQUENCE
    ;

fragment EXPONENT_PART
    : ('e'|'E') (PLUS | DASH)? DIGIT_SEQUENCE
    ;

fragment DIGIT_SEQUENCE
    : ('0'..'9')+
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

// ----------------------------------------------------------------------
// Simple tokens
// ----------------------------------------------------------------------

INT_SUFFIX      : 'u' | 'U';
FLOAT_SUFFIX    : 'f' | 'F' | 'lf' | 'LF';

// ----------------------------------------------------------------------
// Symbols and operators
// ----------------------------------------------------------------------

LEFT_OP          : '<<';
RIGHT_OP         : '>>';

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
LEFT_ASSIGN      : '<<=';
RIGHT_ASSIGN     : '>>=';
AND_ASSIGN       : '&=';
XOR_ASSIGN       : '^=';
OR_ASSIGN        : '|=';
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
// Ignored elements
// ----------------------------------------------------------------------

WHITESPACE
  : ( ' ' | '\t' | '\f' | '\r' | '\n' | '\\n' ) -> skip
  ;

COMMENT
  : '//' (~('\n'|'\r'))* -> skip
  ;

MULTILINE_COMMENT
  : '/*' ( . )*? '*/'  -> skip
  ;
