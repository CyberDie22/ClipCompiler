package me.cyberdie22.clipc.ir


import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.AnsiFormat
import com.diogonunes.jcolor.Attribute
import me.cyberdie22.clipc.ir.lexer.SyntaxKind
import me.cyberdie22.clipc.ir.parser.ir.*
import me.cyberdie22.clipc.ir.parser.syntax.*
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

            return when (root.operatorKind) {
                IRUnaryOperatorKind.Identity -> +(operand as Int)
                IRUnaryOperatorKind.Negation -> -(operand as Int)
                IRUnaryOperatorKind.LogicalNegation -> !(operand as Boolean)
                else -> throw Exception("Unexpected unary operator ${root.operatorKind}")
            }
        }

        if (root is IRBinaryExpression) {
            val left = evaluateExpression(root.left)
            val right = evaluateExpression(root.right)

            return when (root.operatorKind) {
                IRBinaryOperatorKind.Plus -> (left as Int) + (right as Int)
                IRBinaryOperatorKind.Minus -> (left as Int) - (right as Int)
                IRBinaryOperatorKind.Times -> (left as Int) * (right as Int)
                IRBinaryOperatorKind.Division -> (left as Int) / (right as Int)
                IRBinaryOperatorKind.Modulus -> (left as Int) % (right as Int)
                IRBinaryOperatorKind.Exponent -> (left as Double).pow(right as Double).toInt()
                IRBinaryOperatorKind.LogicalAnd -> (left as Boolean) && (right as Boolean)
                IRBinaryOperatorKind.LogicalOr -> (left as Boolean) || (right as Boolean)
                else -> throw Exception(colorize("Unexpected binary operator '${root.operatorKind}'", ERROR))
            }
        }

        throw Exception(colorize("Unexpected node ${root.kind}", ERROR))
    }
}