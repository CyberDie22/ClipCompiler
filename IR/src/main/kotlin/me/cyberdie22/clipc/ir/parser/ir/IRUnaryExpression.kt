package me.cyberdie22.clipc.ir.parser.ir

class IRUnaryExpression(val operatorKind: IRUnaryOperatorKind, val operand: IRExpression) : IRExpression() {
    override val type: Class<*>
        get() = operand.type
    override val kind: IRNodeKind
        get() = IRNodeKind.UnaryExpression

}