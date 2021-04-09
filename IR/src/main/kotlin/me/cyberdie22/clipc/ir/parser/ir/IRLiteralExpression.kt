package me.cyberdie22.clipc.ir.parser.ir

class IRLiteralExpression(val value: Any) : IRExpression() {
    override val type: Class<*>
        get() = value.javaClass
    override val kind: IRNodeKind
        get() = IRNodeKind.LiteralExpression
}