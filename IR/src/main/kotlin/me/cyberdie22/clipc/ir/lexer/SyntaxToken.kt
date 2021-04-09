package me.cyberdie22.clipc.ir.lexer

import me.cyberdie22.clipc.ir.parser.syntax.SyntaxNode

data class SyntaxToken(override val kind: SyntaxKind, val position: Int, val text: String?, val value: Any?) : SyntaxNode() {
    override fun getChildren(): List<SyntaxNode> {
        return listOf()
    }

}
