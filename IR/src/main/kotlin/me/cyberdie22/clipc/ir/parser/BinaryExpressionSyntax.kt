package me.cyberdie22.clipc.ir.parser

import me.cyberdie22.clipc.ir.lexer.SyntaxKind
import me.cyberdie22.clipc.ir.lexer.SyntaxToken

class BinaryExpressionSyntax(val left: ExpressionSyntax, val operatorToken: SyntaxToken, val right: ExpressionSyntax) : ExpressionSyntax() {
    override val kind: SyntaxKind
        get() = SyntaxKind.BinaryExpression

    override fun getChildren(): List<SyntaxNode> {
        return listOf(left, operatorToken, right)
    }
}