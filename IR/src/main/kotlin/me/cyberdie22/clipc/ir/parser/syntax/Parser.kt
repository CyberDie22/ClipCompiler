package me.cyberdie22.clipc.ir.parser.syntax

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
        var left: ExpressionSyntax
        val unaryOperatorPrecedence = current.kind.getUnaryOperatorPrecedence()
        if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
            val operatorToken = nextToken()
            val operand = parseExpression(unaryOperatorPrecedence)
            left = UnaryExpressionSyntax(operatorToken, operand)
        } else
            left = parsePrimaryExpression()

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
        when (current.kind) {
            SyntaxKind.OpenParenthesisToken -> {
                val left = nextToken()
                val expression = parseExpression()
                val right = matchToken(SyntaxKind.CloseParenthesisToken)
                return ParenthesizedExpressionSyntax(left, expression, right)
            }
            SyntaxKind.FalseKeyword, SyntaxKind.TrueKeyword -> {
                val keywordToken = nextToken()
                val value = keywordToken.kind == SyntaxKind.TrueKeyword
                return LiteralExpressionSyntax(keywordToken, value)
            }
            else -> {
                val numberToken = matchToken(SyntaxKind.NumberToken)
                return LiteralExpressionSyntax(numberToken)
            }
        }
    }

}

fun SyntaxKind.getUnaryOperatorPrecedence(): Int {
    return when (this) {
        SyntaxKind.PlusToken, SyntaxKind.MinusToken, SyntaxKind.LogicalNotToken -> 7
        else -> 0
    }
}
fun SyntaxKind.getBinaryOperatorPrecedence(): Int {
    return when (this) {
        SyntaxKind.ExponentToken -> 6

        SyntaxKind.TimesToken, SyntaxKind.DivideToken, SyntaxKind.ModuloToken -> 5

        SyntaxKind.PlusToken, SyntaxKind.MinusToken -> 4

        SyntaxKind.EqualityToken, SyntaxKind.InequalityToken -> 3

        SyntaxKind.LogicalAndToken -> 2

        SyntaxKind.LogicalOrToken -> 1
        else -> 0
    }
}
