package me.cyberdie22.clipc.ir.lexer

enum class SyntaxKind {

    // Tokens
    EndOfFileToken,
    BadToken,
    WhitespaceToken,
    NumberToken,
    PlusToken,
    MinusToken,
    TimesToken,
    DivideToken,
    ModuloToken,
    ExponentToken,
    OpenParenthesisToken,
    CloseParenthesisToken,
    IdentifierToken,
    LogicalNotToken,
    LogicalAndToken,
    LogicalOrToken,
    EqualityToken,
    InequalityToken,

    // Expressions
    LiteralExpression,
    BinaryExpression,
    ParenthesizedExpression,
    UnaryExpression,

    // Keywords
    TrueKeyword,
    FalseKeyword,
}