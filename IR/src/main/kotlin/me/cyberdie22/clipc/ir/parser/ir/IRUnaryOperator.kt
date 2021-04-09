package me.cyberdie22.clipc.ir.parser.ir

import me.cyberdie22.clipc.ir.lexer.SyntaxKind

class IRUnaryOperator(val syntaxKind: SyntaxKind, val kind: IRUnaryOperatorKind, val operandType: Class<*>, val resultType: Class<*>) {
    constructor(syntaxKind: SyntaxKind, kind: IRUnaryOperatorKind, operandType: Class<*>) : this(syntaxKind, kind, operandType, operandType)

    companion object {
        inline val operators: Array<IRUnaryOperator>
        get() {
            return arrayOf(
                IRUnaryOperator(SyntaxKind.LogicalNotToken, IRUnaryOperatorKind.LogicalNegation, Class.forName("java.lang.Boolean")),

                IRUnaryOperator(SyntaxKind.PlusToken, IRUnaryOperatorKind.Identity, Class.forName("java.lang.Integer")),
                IRUnaryOperator(SyntaxKind.MinusToken, IRUnaryOperatorKind.Negation, Class.forName("java.lang.Integer")),
            )
        }

        fun bind(syntaxKind: SyntaxKind, operandType: Class<*>): IRUnaryOperator? {
            for (op in operators) {
                if (op.syntaxKind == syntaxKind && op.operandType == operandType)
                    return op
            }
            return null
        }
    }
}