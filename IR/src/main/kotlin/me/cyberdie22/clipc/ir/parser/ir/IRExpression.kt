package me.cyberdie22.clipc.ir.parser.ir

abstract class IRExpression : IRNode() {
    abstract val type: Class<*>
}