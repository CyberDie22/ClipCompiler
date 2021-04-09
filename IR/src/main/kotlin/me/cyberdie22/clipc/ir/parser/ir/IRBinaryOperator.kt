package me.cyberdie22.clipc.ir.parser.ir

import me.cyberdie22.clipc.ir.lexer.SyntaxKind

class IRBinaryOperator(val syntaxKind: SyntaxKind, val kind: IRBinaryOperatorKind, val leftType: Class<*>, val rightType: Class<*>, val resultType: Class<*>) {
    constructor(syntaxKind: SyntaxKind, kind: IRBinaryOperatorKind, type: Class<*>) : this(syntaxKind, kind, type, type, type)
    constructor(syntaxKind: SyntaxKind, kind: IRBinaryOperatorKind, type: Class<*>, resultType: Class<*>) : this(syntaxKind, kind, type, type, resultType)


    companion object {
        val Integer: Class<*> = Class.forName("java.lang.Integer")
        val Boolean: Class<*> = Class.forName("java.lang.Boolean")
        val Object : Class<*> = Class.forName("java.lang.Object" )
        inline val operators: Array<IRBinaryOperator>
        get() {
            return arrayOf(
                IRBinaryOperator(SyntaxKind.PlusToken, IRBinaryOperatorKind.Plus, Integer),
                IRBinaryOperator(SyntaxKind.MinusToken, IRBinaryOperatorKind.Minus, Integer),
                IRBinaryOperator(SyntaxKind.TimesToken, IRBinaryOperatorKind.Multiply, Integer),
                IRBinaryOperator(SyntaxKind.DivideToken, IRBinaryOperatorKind.Divide, Integer),
                IRBinaryOperator(SyntaxKind.ModuloToken, IRBinaryOperatorKind.Modulus, Integer),
                IRBinaryOperator(SyntaxKind.ExponentToken, IRBinaryOperatorKind.Exponent, Integer),

                IRBinaryOperator(SyntaxKind.EqualityToken, IRBinaryOperatorKind.Equals, Object, Boolean),
                IRBinaryOperator(SyntaxKind.InequalityToken, IRBinaryOperatorKind.NotEquals, Object, Boolean),

                IRBinaryOperator(SyntaxKind.LogicalAndToken, IRBinaryOperatorKind.LogicalAnd, Boolean),
                IRBinaryOperator(SyntaxKind.LogicalOrToken, IRBinaryOperatorKind.LogicalOr, Boolean),
            )
        }

        fun bind(syntaxKind: SyntaxKind, leftType: Class<*>, rightType: Class<*>): IRBinaryOperator? {
            for (op in operators) {
                when {
                    op.syntaxKind == syntaxKind && op.leftType == Object && op.rightType == Object -> return op
                    op.syntaxKind == syntaxKind && op.leftType == Object && op.rightType == rightType -> return op
                    op.syntaxKind == syntaxKind && op.leftType == leftType && op.rightType == Object -> return op
                    op.syntaxKind == syntaxKind && op.leftType == leftType && op.rightType == rightType -> return op
                }
            }
            return null
        }
    }
}