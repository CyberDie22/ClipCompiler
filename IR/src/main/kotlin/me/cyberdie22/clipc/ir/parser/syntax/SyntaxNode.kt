package me.cyberdie22.clipc.ir.parser.syntax

import me.cyberdie22.clipc.ir.lexer.SyntaxKind

abstract class SyntaxNode {
    abstract val kind: SyntaxKind

    abstract fun getChildren(): List<SyntaxNode>
}