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

class Parser(val text: String) {
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

    private fun match(kind: SyntaxKind): SyntaxToken {
        if (current.kind == kind)
            return nextToken()

        diagnostics.add(colorize("ERROR: Unexpected token <${current.kind}>, expected <$kind>", ERROR))
        position++
        return SyntaxToken(kind, current.position, null, null)
    }

    private val current: SyntaxToken
        get() = peek(0)

    fun parse(): SyntaxTree {
        val expression = parseTerm()
        val eofToken = match(SyntaxKind.EndOfFileToken)
        return SyntaxTree(diagnostics, expression, eofToken)
    }

    private fun parseExpression(): ExpressionSyntax {
        return parseTerm()
    }

    private fun parseTerm(): ExpressionSyntax {
        var left = parseFactor()

        while (current.kind == SyntaxKind.PlusToken ||
            current.kind == SyntaxKind.MinusToken) {
            val operatorToken = nextToken()
            val right = parseFactor()
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }
        return left
    }

    private fun parseFactor(): ExpressionSyntax {
        var left = parsePower()

        while ( current.kind == SyntaxKind.TimesToken  ||
                current.kind == SyntaxKind.DivideToken ||
                current.kind == SyntaxKind.ModuloToken) {
            val operatorToken = nextToken()
            val right = parsePower()
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }
        return left
    }

    private fun parsePower(): ExpressionSyntax {
        var left = parsePrimaryExpression()

        while ( current.kind == SyntaxKind.ExponentToken) {
            val operatorToken = nextToken()
            val right = parsePrimaryExpression()
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }
        return left
    }

    private fun parsePrimaryExpression(): ExpressionSyntax {
        if (current.kind == SyntaxKind.OpenParenthesisToken) {
            val left = nextToken()
            val expression = parseExpression()
            val right = match(SyntaxKind.CloseParenthesisToken)
            return ParenthesizedExpressionSyntax(left, expression, right)
        }
        val numberToken = match(SyntaxKind.NumberToken)
        return NumberExpressionSyntax(numberToken)
    }

}