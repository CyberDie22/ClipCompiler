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
        get() = peek(0)
    private val lookahead: Char
        get() = peek(1)

    private fun peek(offset: Int): Char {
        return if (position + offset >= text.length) '\u0000' else text[position + offset]
    }

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

        when {
            current == '+' -> return returnSyntaxTokenForCharacterLiteral('+', SyntaxKind.PlusToken)
            current == '-' -> return returnSyntaxTokenForCharacterLiteral('-', SyntaxKind.MinusToken)
            current == '*' -> return returnSyntaxTokenForCharacterLiteral('*', SyntaxKind.TimesToken)
            current == '/' -> return returnSyntaxTokenForCharacterLiteral('/', SyntaxKind.DivideToken)
            current == '%' -> return returnSyntaxTokenForCharacterLiteral('%', SyntaxKind.ModuloToken)
            current == '^' -> return returnSyntaxTokenForCharacterLiteral('^', SyntaxKind.ExponentToken)
            current == '(' -> return returnSyntaxTokenForCharacterLiteral('(', SyntaxKind.OpenParenthesisToken)
            current == ')' -> return returnSyntaxTokenForCharacterLiteral(')', SyntaxKind.CloseParenthesisToken)
            current == '!' && lookahead != '=' -> return returnSyntaxTokenForCharacterLiteral('!', SyntaxKind.LogicalNotToken)
            current == '&' && lookahead == '&' -> return returnSyntaxTokenForCharacterLiteral("&&", SyntaxKind.LogicalAndToken, 2)
            current == '|' && lookahead == '|' -> return returnSyntaxTokenForCharacterLiteral("||", SyntaxKind.LogicalOrToken, 2)
            current == '=' && lookahead == '=' -> return returnSyntaxTokenForCharacterLiteral("==", SyntaxKind.EqualityToken, 2)
            current == '!' && lookahead == '=' -> return returnSyntaxTokenForCharacterLiteral("!=", SyntaxKind.InequalityToken, 2)
            else -> {
                diagnostics.add(colorize("ERROR: Bad character input: '$current'", ERROR))
                return SyntaxToken(SyntaxKind.BadToken, position++, text.subSequence(position - 1, position) as String, null)
            }
        }
    }

    private fun returnSyntaxTokenForCharacterLiteral(char: Char, kind: SyntaxKind): SyntaxToken {
        return SyntaxToken(kind, position++, char.toString(), null)
    }

    private fun returnSyntaxTokenForCharacterLiteral(char: String, kind: SyntaxKind): SyntaxToken {
        return SyntaxToken(kind, position++, char, null)
    }

    private fun returnSyntaxTokenForCharacterLiteral(char: Char, kind: SyntaxKind, posInc: Int): SyntaxToken {
        val position = position
        this.position += posInc
        return SyntaxToken(kind, position, char.toString(), null)
    }

    private fun returnSyntaxTokenForCharacterLiteral(char: String, kind: SyntaxKind, posInc: Int): SyntaxToken {
        val position = position
        this.position += posInc
        return SyntaxToken(kind, position, char, null)
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