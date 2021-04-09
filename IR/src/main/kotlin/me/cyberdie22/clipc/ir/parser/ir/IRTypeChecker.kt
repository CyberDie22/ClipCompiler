package me.cyberdie22.clipc.ir.parser.ir

import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.AnsiFormat
import com.diogonunes.jcolor.Attribute
import me.cyberdie22.clipc.ir.lexer.SyntaxKind
import me.cyberdie22.clipc.ir.parser.syntax.BinaryExpressionSyntax
import me.cyberdie22.clipc.ir.parser.syntax.ExpressionSyntax
import me.cyberdie22.clipc.ir.parser.syntax.LiteralExpressionSyntax
import me.cyberdie22.clipc.ir.parser.syntax.UnaryExpressionSyntax

val INFO = AnsiFormat(Attribute.TEXT_COLOR(169, 169, 169))
val WARN = AnsiFormat(Attribute.YELLOW_TEXT())
val ERROR = AnsiFormat(Attribute.TEXT_COLOR(139, 0, 0))

class IRTypeChecker {
    val diagnostics = mutableListOf<String>()

    fun bindExpression(syntax: ExpressionSyntax): IRExpression {
        return when (syntax.kind) {
            SyntaxKind.LiteralExpression -> bindLiteralExpression(syntax as LiteralExpressionSyntax)
            SyntaxKind.UnaryExpression -> bindUnaryExpression(syntax as UnaryExpressionSyntax)
            SyntaxKind.BinaryExpression -> bindBinaryExpression(syntax as BinaryExpressionSyntax)
            else -> throw Exception(colorize("Unexpected syntax ${syntax.kind}", ERROR))
        }
    }

    private fun bindLiteralExpression(syntax: LiteralExpressionSyntax): IRExpression {
        val value = syntax.value
            ?: 0
        return IRLiteralExpression(value)
    }

    private fun bindUnaryExpression(syntax: UnaryExpressionSyntax): IRExpression {
        val boundOperand = bindExpression(syntax.operand)
        val boundOperator = IRUnaryOperator.bind(syntax.operatorToken.kind, boundOperand.type)
        if (boundOperator == null) {
            diagnostics.add(colorize("Unary operator '${syntax.operatorToken.text}' is not defined for type ${boundOperand.type.name}", ERROR))
            return boundOperand
        }
        return IRUnaryExpression(boundOperator, boundOperand)
    }


    private fun bindBinaryExpression(syntax: BinaryExpressionSyntax): IRExpression {
        val boundLeft = bindExpression(syntax.left)
        val boundRight = bindExpression(syntax.right)
        val boundOperator = IRBinaryOperator.bind(syntax.operatorToken.kind, boundLeft.type, boundRight.type)
        if (boundOperator == null) {
            diagnostics.add(colorize("Binary operator '${syntax.operatorToken.text}' is not defined for types ${boundLeft.type.name} and ${boundRight.type.name}", ERROR))
            return boundLeft
        }
        return IRBinaryExpression(boundLeft, boundOperator, boundRight)
    }
}
