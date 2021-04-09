package me.cyberdie22.clipc.ir.parser

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.AnsiFormat
import com.diogonunes.jcolor.Attribute
import me.cyberdie22.clipc.*
import me.cyberdie22.clipc.ir.lexer.Lexer
import me.cyberdie22.clipc.ir.lexer.SyntaxKind
import me.cyberdie22.clipc.ir.lexer.SyntaxToken

val INFO = AnsiFormat(Attribute.TEXT_COLOR(169, 169, 169))
val WARN = AnsiFormat(Attribute.YELLOW_TEXT())
val ERROR = AnsiFormat(Attribute.TEXT_COLOR(139, 0, 0))

class Parser(text: String) {
    private val tokens: Array<SyntaxToken>
    private var position = 0

    val diagnostics = mutableListOf<String>()

    init {
        val lexer = Lexer(text)
        val tokens = lexer.getAllTokensUntilEOF()
        this.tokens = tokens.stream().filter {
            it.kind != SyntaxKind.WhitespaceToken &&
                    it.kind != SyntaxKind.BadToken
        }.toTypedArray()
        diagnostics.addAll(lexer.diagnostics)
    }

    private fun peek(offset: Int): SyntaxToken {
        val idx = position + offset
        if (idx >= tokens.size)
            return tokens[tokens.size - 1]

        return tokens[idx]
    }

    private fun nextToken(): SyntaxToken {
        val current = current
        position++
        return current
    }

    private fun matchToken(kind: SyntaxKind): SyntaxToken {
        if (current.kind == kind)
            return nextToken()

        diagnostics.add(colorize("ERROR: Unexpected token <${current.kind}>, expected <$kind>", ERROR))
        position++
        return SyntaxToken(kind, current.position, null, null)
    }

    private val current: SyntaxToken
        get() = peek(0)

    fun parse(): SyntaxTree {
        val expression = parseExpression()
        val eofToken = matchToken(SyntaxKind.EndOfFileToken)
        return SyntaxTree(diagnostics, expression, eofToken)
    }

    private fun parseExpression(parentPrecedence: Int = 0): ExpressionSyntax {
        var left = parsePrimaryExpression()

        while (true) {
            val precedence = current.kind.getBinaryOperatorPrecedence()
            if (precedence == 0 || precedence <= parentPrecedence)
                break

            val operatorToken = nextToken()
            val right = parseExpression(precedence)
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }

        return left
    }

    private fun parsePrimaryExpression(): ExpressionSyntax {
        if (current.kind == SyntaxKind.OpenParenthesisToken) {
            val left = nextToken()
            val expression = parseExpression()
            val right = matchToken(SyntaxKind.CloseParenthesisToken)
            return ParenthesizedExpressionSyntax(left, expression, right)
        }
        val numberToken = matchToken(SyntaxKind.NumberToken)
        return LiteralExpressionSyntax(numberToken)
    }

}

fun SyntaxKind.getBinaryOperatorPrecedence(): Int {
    return when (this) {
        in listOf(SyntaxKind.ExponentToken) -> 3
        in listOf(SyntaxKind.TimesToken, SyntaxKind.DivideToken, SyntaxKind.ModuloToken) -> 2
        in listOf(SyntaxKind.PlusToken, SyntaxKind.MinusToken) -> 1
        else -> 0
    }
}