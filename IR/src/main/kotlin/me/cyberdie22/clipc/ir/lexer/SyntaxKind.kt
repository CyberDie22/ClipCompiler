package me.cyberdie22.clipc.ir.lexer

enum class SyntaxKind {
    EndOfFileToken,
    NumberToken,
    WhitespaceToken,
    PlusToken,
    MinusToken,
    TimesToken,
    DivideToken,
    ModuloToken,
    ExponentToken,
    OpenParenthesisToken,
    CloseParenthesisToken,
    BadToken,
    LiteralExpression,
    BinaryExpression,
    ParenthesizedExpression,
    UnaryExpression
}