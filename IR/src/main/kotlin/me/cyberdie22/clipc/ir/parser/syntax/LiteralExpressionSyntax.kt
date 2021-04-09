package me.cyberdie22.clipc.ir.parser.syntax

import me.cyberdie22.clipc.ir.lexer.SyntaxKind
import me.cyberdie22.clipc.ir.lexer.SyntaxToken

class LiteralExpressionSyntax(val literalToken: SyntaxToken, val value: Any?) : ExpressionSyntax() {
    constructor(literalToken: SyntaxToken) : this(literalToken, literalToken.value)
    override val kind: SyntaxKind
        get() = SyntaxKind.LiteralExpression

    override fun getChildren(): List<SyntaxNode> {
        return listOf(literalToken)
    }

}