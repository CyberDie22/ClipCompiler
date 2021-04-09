package me.cyberdie22.clipc.ir.parser.ir

class IRBinaryExpression(val left: IRExpression, val operatorKind: IRBinaryOperatorKind, val right: IRExpression) : IRExpression() {
    override val type: Class<*>
        get() = left.javaClass
    override val kind: IRNodeKind
        get() = IRNodeKind.BinaryExpression
}