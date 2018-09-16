/**
 * Preprocessor for GLSL 4.40
 */
grammar PP;

options {
    language = Java;
}

// Entry point
statement
  : declaration ('\r' | '\n')
  ;

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
  : VERSION INTCONSTANT (CORE | COMPATIBILITY | ES)?
  ;

line_declaration
  : LINE INTCONSTANT INTCONSTANT
  ;

conditional_declaration
  : IF const_expression         # if_expression
  | IFDEF IDENTIFIER            # idef_expression
  | IFNDEF IDENTIFIER           # ifndef_expression
  | ELSE                        # else_expression
  | ELIF const_expression       # else_if_expression
  | ENDIF                       # endif_expression
  ;

numeric_expression
  : INTCONSTANT                                                                       # integer_expression
  | IDENTIFIER                                                                        # identifier_expression
  | DEFINED (LEFT_PAREN)? IDENTIFIER (RIGHT_PAREN)?                                   # defined_expression
  | LEFT_PAREN const_expression RIGHT_PAREN                                           # parenthesis_expression
  | (PLUS | DASH | TILDE | BANG) numeric_expression                                   # unary_expression
  | numeric_expression (STAR | SLASH | PERCENT) numeric_expression                    # multiplicative_expression
  | numeric_expression (PLUS | DASH) numeric_expression                               # additive_expression
  | numeric_expression (LEFT_SHIFT | RIGHT_SHIFT) numeric_expression                  # shift_expression
  | numeric_expression (AMPERSAND | CARET | VERTICAL_BAR) numeric_expression          # bit_expression
  ;

const_expression
  : numeric_expression                                                                    # numerical_delegate
  | numeric_expression (LT_OP | LE_OP | GE_OP | GT_OP  EQ_OP | NE_OP) numeric_expression  # relational_expression
  | const_expression AND_OP const_expression                                              # and_expression
  | const_expression OR_OP const_expression                                               # or_expression
  ;

macro_declaration
  : DEFINE IDENTIFIER (parameter_declaration)? (WILDCARD)?
  ;

parameter_declaration
  : LEFT_PAREN RIGHT_PAREN
  | LEFT_PAREN IDENTIFIER (COMMA IDENTIFIER)* RIGHT_PAREN
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

// ----------------------------------------------------------------------
// Ignored elements
// ----------------------------------------------------------------------

WHITESPACE
  : ( ' ' | '\t' | '\f' ) -> skip
  ;

COMMENT
  : '//' (~('\n'|'\r'))* -> skip
  ;

MULTILINE_COMMENT
  : '/*' ( . )*? '*/'  -> skip
  ;

WILDCARD         : .+?;
