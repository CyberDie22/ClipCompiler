package me.cyberdie22.clipc.ir.parser.ir

class IRBinaryExpression(val left: IRExpression, val operator: IRBinaryOperator, val right: IRExpression) : IRExpression() {
    override val type: Class<*>
        get() = left.type
    override val kind: IRNodeKind
        get() = IRNodeKind.BinaryExpression
}