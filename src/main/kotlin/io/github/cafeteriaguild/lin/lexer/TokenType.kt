package io.github.cafeteriaguild.lin.lexer

enum class TokenType {
    L_BRACE, // {
    R_BRACE, // }

    L_PAREN, // (
    R_PAREN, // )

    L_BRACKET, // [
    R_BRACKET, // ]

    LT, // <
    GT, // >
    LTE, // <=
    GTE, // >=

    EQ, // ==
    NEQ, // !=

    RANGE, // ..

    AND, // &&
    OR, // ||
    AMP, // &
    PIPE, // |

    PLUS, // +
    MINUS, // -
    ASTERISK, // *
    SLASH, // /
    BACKSLASH, // \
    REM, // %

    ASSIGN, // =
    PLUS_ASSIGN, // +=
    MINUS_ASSIGN, // -=
    ASTERISK_ASSIGN, // *=
    SLASH_ASSIGN, // /=
    REM_ASSIGN, // %=
    QUESTION, // ?
    QUESTION_DOT, // ?.
    ELVIS, // ?:
    DOUBLE_BANG, // !!
    BANG, // !
    ARROW, // ->
    INCREMENT, // ++
    DECREMENT, // --

    STRING,
    CHAR,
    IDENTIFIER,

    INT,
    LONG,
    FLOAT,
    DOUBLE,

    TRUE,
    FALSE,
    NULL,

    DOT,
    COMMA,
    COLON,
    DOUBLE_COLON,
    SEMICOLON,

    NL,

    AS,
    BREAK,
    CLASS,
    CONTINUE,
    DO,
    ELSE,
    FOR,
    FUN,
    IF,
    IN,
    INTERFACE,
    IS,
    OBJECT,
    PACKAGE,
    RETURN,
    SUPER,
    THIS,
    THROW,
    TRY,
    TYPEALIAS,
    TYPEOF,
    VAL,
    VAR,
    WHEN,
    WHILE,

    IMPORT,

    INVALID,
    RESERVED
}