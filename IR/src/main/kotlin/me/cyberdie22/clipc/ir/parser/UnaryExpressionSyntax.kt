package me.cyberdie22.clipc.ir.parser

import me.cyberdie22.clipc.ir.lexer.SyntaxKind
import me.cyberdie22.clipc.ir.lexer.SyntaxToken

class UnaryExpressionSyntax(val operatorToken: SyntaxToken, val operand: ExpressionSyntax) : ExpressionSyntax() {
    override val kind: SyntaxKind
        get() = SyntaxKind.UnaryExpression

    override fun getChildren(): List<SyntaxNode> = listOf(operatorToken, operand)
}