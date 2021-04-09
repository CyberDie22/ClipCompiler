package me.cyberdie22.clipc.ir


import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.AnsiFormat
import com.diogonunes.jcolor.Attribute
import me.cyberdie22.clipc.ir.lexer.SyntaxKind
import me.cyberdie22.clipc.ir.parser.BinaryExpressionSyntax
import me.cyberdie22.clipc.ir.parser.ExpressionSyntax
import me.cyberdie22.clipc.ir.parser.NumberExpressionSyntax
import me.cyberdie22.clipc.ir.parser.ParenthesizedExpressionSyntax
import java.lang.Math.pow
import kotlin.math.pow

val INFO = AnsiFormat(Attribute.TEXT_COLOR(169, 169, 169))
val WARN = AnsiFormat(Attribute.YELLOW_TEXT())
val ERROR = AnsiFormat(Attribute.TEXT_COLOR(139, 0, 0))

class Evaluator(private val root: ExpressionSyntax) {
    fun evaluate(): Int {
        return evaluateExpression(root)
    }

    private fun evaluateExpression(root: ExpressionSyntax): Int {
        // BinaryExpression
        // NumberExpression
        // ParenthesizedExpression

        if (root is NumberExpressionSyntax)
            return root.numberToken.value as Int

        if (root is BinaryExpressionSyntax) {
            val left = evaluateExpression(root.left)
            val right = evaluateExpression(root.right)

            return when (root.operatorToken.kind) {
                SyntaxKind.PlusToken -> left + right
                SyntaxKind.MinusToken -> left - right
                SyntaxKind.TimesToken -> left * right
                SyntaxKind.DivideToken -> left / right
                SyntaxKind.ModuloToken -> left % right
                SyntaxKind.ExponentToken -> left.toDouble().pow(right.toDouble()).toInt()
                else -> throw Exception(colorize("Unexpected binary operator '${root.operatorToken}'", ERROR))
            }
        }

        if (root is ParenthesizedExpressionSyntax)
            return evaluateExpression(root.expression)

        throw Exception(colorize("Unexpected node ${root.kind}", ERROR))
    }
}