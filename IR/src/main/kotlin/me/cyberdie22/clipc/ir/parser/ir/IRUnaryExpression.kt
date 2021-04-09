package me.cyberdie22.clipc.ir.parser.ir

import sun.jvm.hotspot.asm.Operand
import java.lang.reflect.Type

class IRUnaryExpression(val operatorKind: IRUnaryOperatorKind, val operand: IRExpression) : IRExpression() {
    override val type: Class<*>
        get() = operand.type
    override val kind: IRNodeKind
        get() = IRNodeKind.UnaryExpression

}