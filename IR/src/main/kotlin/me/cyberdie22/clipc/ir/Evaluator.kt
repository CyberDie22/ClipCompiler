package me.cyberdie22.clipc.ir


import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.AnsiFormat
import com.diogonunes.jcolor.Attribute
import me.cyberdie22.clipc.ir.parser.ir.*
import kotlin.math.pow

val INFO = AnsiFormat(Attribute.TEXT_COLOR(169, 169, 169))
val WARN = AnsiFormat(Attribute.YELLOW_TEXT())
val ERROR = AnsiFormat(Attribute.TEXT_COLOR(139, 0, 0))

class Evaluator(private val root: IRExpression) {
    fun evaluate(): Any {
        return evaluateExpression(root)
    }

    private fun evaluateExpression(root: IRExpression): Any {

        if (root is IRLiteralExpression)
            return root.value

        if (root is IRUnaryExpression) {
            val operand = evaluateExpression(root.operand)

            return when (root.operator.kind) {
                IRUnaryOperatorKind.Identity -> +(operand as Int)
                IRUnaryOperatorKind.Negation -> -(operand as Int)
                IRUnaryOperatorKind.LogicalNegation -> !(operand as Boolean)
                else -> throw Exception("Unexpected unary operator ${root.operator.kind}")
            }
        }

        if (root is IRBinaryExpression) {
            val left = evaluateExpression(root.left)
            val right = evaluateExpression(root.right)

            return when (root.operator.kind) {
                IRBinaryOperatorKind.Plus -> (left as Int) + (right as Int)
                IRBinaryOperatorKind.Minus -> (left as Int) - (right as Int)
                IRBinaryOperatorKind.Multiply -> (left as Int) * (right as Int)
                IRBinaryOperatorKind.Divide -> (left as Int) / (right as Int)
                IRBinaryOperatorKind.Modulus -> (left as Int) % (right as Int)
                IRBinaryOperatorKind.Exponent -> (left as Double).pow(right as Double).toInt()
                IRBinaryOperatorKind.LogicalAnd -> (left as Boolean) && (right as Boolean)
                IRBinaryOperatorKind.LogicalOr -> (left as Boolean) || (right as Boolean)
                IRBinaryOperatorKind.Equals -> left == right
                IRBinaryOperatorKind.NotEquals -> left != right
                else -> throw Exception(colorize("Unexpected binary operator '${root.operator.kind}'", ERROR))
            }
        }

        throw Exception(colorize("Unexpected node ${root.kind}", ERROR))
    }
}