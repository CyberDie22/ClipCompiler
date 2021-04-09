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
    fun evaluate(): Int {
        return evaluateExpression(root)
    }

    private fun evaluateExpression(root: IRExpression): Int {

        if (root is IRLiteralExpression)
            return root.value as Int

        if (root is IRUnaryExpression) {
            val operand = evaluateExpression(root.operand)

            return when (root.operatorKind) {
                IRUnaryOperatorKind.Identity -> +operand
                IRUnaryOperatorKind.Negation -> -operand
                else -> throw Exception("Unexpected unary operator ${root.operatorKind}")
            }
        }

        if (root is IRBinaryExpression) {
            val left = evaluateExpression(root.left)
            val right = evaluateExpression(root.right)

            return when (root.operatorKind) {
                IRBinaryOperatorKind.Plus -> left + right
                IRBinaryOperatorKind.Minus -> left - right
                IRBinaryOperatorKind.Times -> left * right
                IRBinaryOperatorKind.Division -> left / right
                IRBinaryOperatorKind.Modulus -> left % right
                IRBinaryOperatorKind.Exponent -> left.toDouble().pow(right.toDouble()).toInt()
                else -> throw Exception(colorize("Unexpected binary operator '${root.operatorKind}'", ERROR))
            }
        }

        throw Exception(colorize("Unexpected node ${root.kind}", ERROR))
    }
}