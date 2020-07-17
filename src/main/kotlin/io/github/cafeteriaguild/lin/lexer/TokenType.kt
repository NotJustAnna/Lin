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

    ASSIGN, // =
    QUESTION, // ?
    ELVIS, // ?:
    BANG, // !
    ARROW, // ->

    STRING,
    CHAR,
    IDENT,

    INT,
    LONG,
    FLOAT,
    DOUBLE,

    TRUE,
    FALSE,

    DOT,
    COMMA,
    COLON,
    DOUBLE_COLON,
    SEMICOLON,

    NL,

    STRUCT,
    USE,
    AS,
    IS,
    BREAK,
    EXTERNAL,
    MOD,
    MUT,
    RETURN,
    VAL,
    VAR,
    FUN,
    IF,
    ELSE,
    NULL,
    TYPE,
    WHILE,

    INVALID,
    RESERVED
}