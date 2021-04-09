package me.cyberdie22.clipc.ir.parser.ir

import me.cyberdie22.clipc.ir.lexer.SyntaxKind

class IRBinaryOperator(val syntaxKind: SyntaxKind, val kind: IRBinaryOperatorKind, val leftType: Class<*>, val rightType: Class<*>, val resultType: Class<*>) {
    constructor(syntaxKind: SyntaxKind, kind: IRBinaryOperatorKind, type: Class<*>) : this(syntaxKind, kind, type, type, type)

    companion object {
        inline val operators: Array<IRBinaryOperator>
        get() {
            return arrayOf(
                IRBinaryOperator(SyntaxKind.PlusToken, IRBinaryOperatorKind.Plus, Class.forName("java.lang.Integer")),
                IRBinaryOperator(SyntaxKind.MinusToken, IRBinaryOperatorKind.Minus, Class.forName("java.lang.Integer")),
                IRBinaryOperator(SyntaxKind.TimesToken, IRBinaryOperatorKind.Multiply, Class.forName("java.lang.Integer")),
                IRBinaryOperator(SyntaxKind.DivideToken, IRBinaryOperatorKind.Divide, Class.forName("java.lang.Integer")),
                IRBinaryOperator(SyntaxKind.ModuloToken, IRBinaryOperatorKind.Modulus, Class.forName("java.lang.Integer")),
                IRBinaryOperator(SyntaxKind.ExponentToken, IRBinaryOperatorKind.Exponent, Class.forName("java.lang.Integer")),

                IRBinaryOperator(SyntaxKind.LogicalAndToken, IRBinaryOperatorKind.LogicalAnd, Class.forName("java.lang.Boolean")),
                IRBinaryOperator(SyntaxKind.LogicalOrToken, IRBinaryOperatorKind.LogicalOr, Class.forName("java.lang.Boolean")),
            )
        }

        fun bind(syntaxKind: SyntaxKind, leftType: Class<*>, rightType: Class<*>): IRBinaryOperator? {
            for (op in operators) {
                if (op.syntaxKind == syntaxKind && op.leftType == leftType && op.rightType == rightType)
                    return op
            }
            return null
        }
    }
}