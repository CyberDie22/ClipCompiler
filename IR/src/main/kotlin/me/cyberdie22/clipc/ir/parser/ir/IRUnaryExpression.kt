package me.cyberdie22.clipc.ir.parser.ir

class IRUnaryExpression(val operator: IRUnaryOperator, val operand: IRExpression) : IRExpression() {
    override val type: Class<*>
        get() = operand.type
    override val kind: IRNodeKind
        get() = IRNodeKind.UnaryExpression

}