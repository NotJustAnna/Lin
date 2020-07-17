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

    PLUS, // +
    MINUS, // -
    ASTERISK, // *
    F_SLASH, // /
    B_SLASH, // \
    REM, // %

    ASSIGN, // =
    QUESTION, // ?
    ELVIS, // ?:
    BANG, // !
    ARROW, // ->

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

    INVALID,
    RESERVED
}