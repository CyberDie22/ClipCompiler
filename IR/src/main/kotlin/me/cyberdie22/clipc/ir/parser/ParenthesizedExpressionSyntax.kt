package me.cyberdie22.clipc.ir.parser

import me.cyberdie22.clipc.ir.lexer.SyntaxKind
import me.cyberdie22.clipc.ir.lexer.SyntaxToken

class ParenthesizedExpressionSyntax(val openParenthesisToken: SyntaxToken, val expression: ExpressionSyntax, val closeParenthesisToken: SyntaxToken) : ExpressionSyntax() {
    override val kind: SyntaxKind
        get() = SyntaxKind.ParenthesizedExpression

    override fun getChildren(): List<SyntaxNode> {
        return listOf(openParenthesisToken, expression, closeParenthesisToken)
    }

}