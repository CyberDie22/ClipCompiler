package me.cyberdie22.clipc.ir.parser

import me.cyberdie22.clipc.ir.lexer.SyntaxKind
import me.cyberdie22.clipc.ir.lexer.SyntaxToken

class NumberExpressionSyntax(val numberToken: SyntaxToken) : ExpressionSyntax() {
    override val kind: SyntaxKind
        get() = SyntaxKind.NumberExpression

    override fun getChildren(): List<SyntaxNode> {
        return listOf(numberToken)
    }

}