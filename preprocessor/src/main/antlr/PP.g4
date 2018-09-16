/**
 * Preprocessor for GLSL 4.40
 */
grammar PP;

options {
    language = Java;
}


//
declaration
  : extension_declaration
  | version_declaration
  | line_declaration
  | conditional_declaration
  | macro_declaration
  ;

extension_declaration
  : EXTENTION IDENTIFIER COLON (REQUIRE | ENABLE | DISABLE | WARN)
  ;

version_declaration
  : VERSION DECIMAL_CONSTANT (CORE | COMPATIBILITY | ES)?
  ;

line_declaration
  : LINE DECIMAL_CONSTANT DECIMAL_CONSTANT
  ;

conditional_declaration
  : IF const_expression         # if_expression
  | IFDEF IDENTIFIER            # idef_expression
  | IFNDEF IDENTIFIER           # ifndef_expression
  | ELSE                        # else_expression
  | ELIF const_expression       # else_if_expression
  | ENDIF                       # endif_expression
  ;

const_expression
  : INTCONSTANT                                                             # integer_expression
  | IDENTIFIER                                                              # identifier_expression
  | LEFT_PAREN const_expression RIGHT_PAREN                                 # parenthesis_expression
  | DEFINED (LEFT_PAREN)? IDENTIFIER (RIGHT_PAREN)?                         # defined_expression
  | (PLUS | DASH | TILDE | BANG) const_expression                           # unary_expression
  | const_expression (STAR | SLASH | PERCENT) const_expression              # multiplicative_expression
  | const_expression (PLUS | DASH) const_expression                         # additive_expression
  | const_expression (LEFT_SHIFT | RIGHT_SHIFT) const_expression            # shift_expression
  | const_expression (LT_OP | LE_OP | GE_OP | GT_OP) const_expression       # relational_expression
  | const_expression (EQ_OP | NE_OP) const_expression                       # relational_expression
  | const_expression (AMPERSAND | CARET | VERTICAL_BAR) const_expression    # bit_expression
  | const_expression AND_OP const_expression                                # and_expression
  | const_expression OR_OP const_expression                                 # op_expression
  ;

macro_declaration
  : DEFINE IDENTIFIER (parameter_declaration)? (WILDCARD)?
  ;

parameter_declaration
  : LEFT_PAREN RIGHT_PAREN
  | LEFT_PAREN IDENTIFIER (COMMA IDENTIFIER)* RIGHT_PAREN
  ;

// ----------------------------------------------------------------------
// Identifier
// ----------------------------------------------------------------------

IDENTIFIER
  : ('a'..'z'|'A'..'Z'|'_')('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
  ;

INTCONSTANT
  : DECIMAL_CONSTANT
  | OCTAL_CONSTANT
  | HEXADECIMAL_CONSTANT
  ;

DECIMAL_CONSTANT
  : ('1'..'9')('0'..'9')*
  ;

OCTAL_CONSTANT
  : '0' ('0'..'7')*
  ;

HEXADECIMAL_CONSTANT
  : '0' ('x'|'X') HEXDIGIT+
  ;

fragment HEXDIGIT
  : ('0'..'9'|'a'..'f'|'A'..'F')
  ;

// ----------------------------------------------------------------------
// Macros
// ----------------------------------------------------------------------

MACRO_LINE       : '__LINE__';
MACRO_FILE       : '__FILE__';
MACRO_VERSION    : '__VERSION__';

// ----------------------------------------------------------------------
// Keywords
// ----------------------------------------------------------------------
CORE             : 'core';
COMPATIBILITY    : 'compatibility';
DEFINE           : 'define';
DEFINED          : 'defined';
ELIF             : 'elif';
ELSE             : 'else';
ENDIF            : 'endif';
ERROR            : 'error';
ES               : 'es';
EXTENTION        : 'extension';
IF               : 'if';
IFDEF            : 'ifdef';
IFNDEF           : 'ifndef';
LINE             : 'line';
PRAGMA           : 'pragma';
UNDEF            : 'undef';
VERSION          : 'version';

REQUIRE          : 'require';
ENABLE           : 'enable';
DISABLE          : 'disable';
WARN             : 'warn';


// ----------------------------------------------------------------------
// Operators & symbols
// ----------------------------------------------------------------------

LEFT_SHIFT       : '<<';
RIGHT_SHIFT      : '>>';
LE_OP            : '<=';
GE_OP            : '>=';
EQ_OP            : '==';
NE_OP            : '!=';
AND_OP           : '&&';
OR_OP            : '||';
LT_OP            : '<';
GT_OP            : '>';
LEFT_PAREN       : '(';
RIGHT_PAREN      : ')';
BANG             : '!';
DASH             : '-';
TILDE            : '~';
PLUS             : '+';
STAR             : '*';
SLASH            : '/';
PERCENT          : '%';
VERTICAL_BAR     : '|';
CARET            : '^';
AMPERSAND        : '&';
STINGIZING       : '##';
COMMA            : ',';
COLON            : ':';

WILDCARD         : .+?;

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
