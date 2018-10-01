/**
 * Preprocessor for GLSL 4.40
 *
 * Grammar for a preprocessor with the extension
 * of detecting "#pragma include(path)" statements.
 */
grammar Stage1;

options {
    language = Java;
}

preprocessor
  : macro_call
  | token preprocessor
  ;

macro_call
  : IDENTIFIER LEFT_PAREN RIGHT_PAREN
  | IDENTIFIER argument_list
  ;

argument_list
  : LEFT_PAREN argument (COMMA argument)* RIGHT_PAREN
  ;

argument
  : LEFT_PAREN argument RIGHT_PAREN
  | token
  ;

token
  : IDENTIFIER | STRING | INTCONSTANT | WHITESPACE
  | COMMA
  | PUNCTUATOR
  ;

ints
  : INTCONSTANT | 'apan';

// GLSL specifci keywords
CORE             : 'core';
COMPATIBILITY    : 'compatibility';
DISABLE          : 'disable';
ENABLE           : 'enable';
ES               : 'es';
EXTENSION        : 'extension';
REQUIRE          : 'require';
WARN             : 'warn';

// Preprocessor keywords
DEFINE           : 'define';
DEFINED          : 'defined';
ELIF             : 'elif';
ELSE             : 'else';
ENDIF            : 'endif';
ERROR            : 'error';
IF               : 'if';
IFDEF            : 'ifdef';
IFNDEF           : 'ifndef';
LINE             : 'line';
PRAGMA           : 'pragma';
UNDEF            : 'undef';
VERSION          : 'version';


// ----------------------------------------------------------------------
// Tokens
// ----------------------------------------------------------------------

IDENTIFIER          : LETTER (LETTER|DIGIT)*;
/*
 * String literals are string constants, character constants, and header file names (the argument of ‘#include’).
 * String constants and character constants are straightforward: "…" or '…'. In either case embedded quotes should be
 * escaped with a backslash: '\'' is the character constant for ‘'’. There is no limit on the length of a character
 * constant, but the value of a character constant that contains more than one character is implementation-defined.
 * No string literal may extend past the end of a line. You may use continued lines instead, or string constant
 * concatenation.
 */
STRING              : '"'  ('\\' [btnr0"\\]|.)*? '"'
                    | '\'' ('\\' [btnr0'\\]|.)*? '\''
                    ;

/*
 * A preprocessing number has a rather bizarre definition. The category includes all the normal integer and floating
 * point constants one expects of C, but also a number of other things one might not initially recognize as a number.
 * Formally, preprocessing numbers begin with an optional period, a required decimal digit, and then continue with any
 * sequence of letters, digits, underscores, periods, and exponents. Exponents are the two-character sequences ‘e+’,
 * ‘e-’, ‘E+’, ‘E-’, ‘p+’, ‘p-’, ‘P+’, and ‘P-’. (The exponents that begin with ‘p’ or ‘P’ are used for hexadecimal
 * floating-point constants.)
 */
INTCONSTANT         : PERIOD? DIGIT (LETTER | DIGIT | EXPONENT | PERIOD)*;

/*
 * Punctuators are all the usual bits of punctuation which are meaningful to C and C++. All but three of the
 * punctuation characters in ASCII are C punctuators. The exceptions are ‘@’, ‘$’, and ‘`’. In addition, all the
 * two- and three-character operators are punctuators. There are also six digraphs, which the C++ standard calls
 * alternative tokens, which are merely alternate ways to spell other punctuators. This is a second attempt to work
 * around missing punctuation in obsolete systems. It has no negative side effects, unlike trigraphs, but does not
 * cover as much ground.
 */
LEFT_PAREN          : '(';
RIGHT_PAREN         : ')';
COMMA               : ',';
PUNCTUATOR          : [!"#%&'+-/:;<=>?[\]\\_{|}~]
                    | '*' | '.' | '^' | '##' | '<%' | '%>' | '<:' | ':>' | '%:' | '%:%:'
                    ;

WHITESPACE          : ( ' ' | '\t' | '\f');

/*
 * Any other single character is considered “other”. It is passed on to the preprocessor’s output unmolested.
 * The C compiler will almost certainly reject source code containing “other” tokens. In ASCII, the only other
 * characters are ‘@’, ‘$’, ‘`’, and control characters other than NUL.
 */
OTHER               : . ;

fragment PERIOD     : '.';
fragment EXPONENT   : [eEpP][+-];
fragment LETTER     : [a-zA-Z_$];
fragment DIGIT      : [0-9];
fragment STRING_ESC : '\\' [btnr0"\\];
fragment NL         : '\r'? '\n' | '\r';
fragment NOT_NL     : ~[\r\n];

