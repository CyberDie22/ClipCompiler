package me.cyberdie22.clipc.ir.parser

import me.cyberdie22.clipc.ir.lexer.SyntaxToken

data class SyntaxTree(val diagnostics: List<String>, val root: ExpressionSyntax, val EOFToken: SyntaxToken) {
    companion object {
        fun parse(text: String): SyntaxTree {
            val parser = Parser(text)
            return parser.parse()
        }
    }
}