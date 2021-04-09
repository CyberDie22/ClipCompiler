package me.cyberdie22.clipc.ir.lexer

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.AnsiFormat
import com.diogonunes.jcolor.Attribute
import me.cyberdie22.clipc.*

val INFO = AnsiFormat(Attribute.TEXT_COLOR(169, 169, 169))
val WARN = AnsiFormat(Attribute.YELLOW_TEXT())
val ERROR = AnsiFormat(Attribute.TEXT_COLOR(139, 0, 0))

class Lexer(private val text: String) {
    private var position = 0
    val diagnostics = mutableListOf<String>()
    fun lex() {
        println(colorize("Performing IR Lexical Analysis of $text", INFO))
    }

    private val current: Char
        get() = if (position >= text.length) '\u0000' else text[position]

    private fun next() {
        position++
    }

    private fun nextUntil(check: () -> Boolean) {
        while (!check())
            next()
    }

    fun nextToken(): SyntaxToken {
        // <numbers>
        // + - * / % ( )
        // <whitespace>

        if (position >= text.length)
            return SyntaxToken(SyntaxKind.EndOfFileToken, position, "\u0000", null)

        if (current.isDigit()) {
            val start = position

            nextUntil { !current.isDigit() }

            val text = text.subSequence(start, position) as String
            val value = text.toIntOrNull()
            if (value == null) {
                diagnostics.add(colorize("${text} cannot be represented by an integer", ERROR))
            }
            return SyntaxToken(SyntaxKind.NumberToken, start, text, value)
        }

        if (current.isWhitespace()) {
            val start = position

            nextUntil { !current.isWhitespace() }

            val text = text.subSequence(start, position) as String
            return SyntaxToken(SyntaxKind.WhitespaceToken, start, text, null)
        }

        if (current.isLetter()) {
            val start = position

            nextUntil { !current.isLetter() }

            val text = text.subSequence(start, position) as String
            val kind = getKeywordKind(text)
            return SyntaxToken(kind, start, text, null)
        }

        return when (current) {
            '+' -> SyntaxToken(SyntaxKind.PlusToken, position++, "+", null)
            '-' -> SyntaxToken(SyntaxKind.MinusToken, position++, "-", null)
            '*' -> SyntaxToken(SyntaxKind.TimesToken, position++, "*", null)
            '/' -> SyntaxToken(SyntaxKind.DivideToken, position++, "/", null)
            '%' -> SyntaxToken(SyntaxKind.ModuloToken, position++, "%", null)
            '^' -> SyntaxToken(SyntaxKind.ExponentToken, position++, "^", null)
            '(' -> SyntaxToken(SyntaxKind.OpenParenthesisToken, position++, "(", null)
            ')' -> SyntaxToken(SyntaxKind.CloseParenthesisToken, position++, ")", null)
            else -> {
                diagnostics.add(colorize("ERROR: Bad character input: '$current'", ERROR))
                SyntaxToken(SyntaxKind.BadToken, position++, text.subSequence(position - 1, position) as String, null)
            }
        }
    }

    private fun getKeywordKind(keyword: String): SyntaxKind {
        return when (keyword) {
            "true" -> SyntaxKind.TrueKeyword
            "false" -> SyntaxKind.FalseKeyword
            else -> SyntaxKind.IdentifierToken
        }
    }

    fun getAllTokensUntilEOF(): List<SyntaxToken> {
        val tokens = mutableListOf<SyntaxToken>()
        while (true) {
            val token = nextToken()
            tokens.add(token)
            if (token.kind == SyntaxKind.EndOfFileToken)
                break
        }
        return tokens.toImmutableList()
    }
}