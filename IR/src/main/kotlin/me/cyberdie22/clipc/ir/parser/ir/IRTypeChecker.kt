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
        val value = syntax.literalToken.value as Int?
            ?: 0
        return IRLiteralExpression(value)
    }

    private fun bindUnaryExpression(syntax: UnaryExpressionSyntax): IRExpression {
        val boundOperand = bindExpression(syntax.operand)
        val boundOperatorKind = bindUnaryOperatorKind(syntax.operatorToken.kind, boundOperand.type)
        if (boundOperatorKind == null) {
            diagnostics.add(colorize("Unary operator '${syntax.operatorToken.text}' is not defined for type ${boundOperand.type.toGenericString()}", ERROR))
            return boundOperand
        }
        return IRUnaryExpression(boundOperatorKind, boundOperand)
    }


    private fun bindBinaryExpression(syntax: BinaryExpressionSyntax): IRExpression {
        val boundLeft = bindExpression(syntax.left)
        val boundRight = bindExpression(syntax.right)
        val boundOperatorKind = bindBinaryOperatorKind(syntax.operatorToken.kind, boundLeft.type, boundRight.type)
        if (boundOperatorKind == null) {
            diagnostics.add(colorize("Unary operator '${syntax.operatorToken.text}' is not defined for types ${boundLeft.type.toGenericString()} and ${boundRight.type.toGenericString()}", ERROR))
            return boundLeft
        }
        return IRBinaryExpression(boundLeft, boundOperatorKind, boundRight)
    }

    private fun bindUnaryOperatorKind(kind: SyntaxKind, operandType: Class<*>): IRUnaryOperatorKind? {
        if (operandType != Int::class.java)
            return null
        return when (kind) {
            SyntaxKind.PlusToken -> IRUnaryOperatorKind.Identity
            SyntaxKind.MinusToken -> IRUnaryOperatorKind.Negation
            else -> throw Exception(colorize("Unexpected unary operator $kind", ERROR))
        }
    }

    private fun bindBinaryOperatorKind(kind: SyntaxKind, leftType: Class<*>, rightType: Class<*>): IRBinaryOperatorKind? {
        if (leftType != Int::class.java || rightType != Int::class.java)
            return null
        return when (kind) {
            SyntaxKind.PlusToken -> IRBinaryOperatorKind.Plus
            SyntaxKind.MinusToken -> IRBinaryOperatorKind.Minus
            SyntaxKind.TimesToken -> IRBinaryOperatorKind.Times
            SyntaxKind.DivideToken -> IRBinaryOperatorKind.Division
            SyntaxKind.ModuloToken -> IRBinaryOperatorKind.Modulus
            SyntaxKind.ExponentToken -> IRBinaryOperatorKind.Exponent
            else -> throw Exception(colorize("Unexpected binary operator $kind", ERROR))
        }
    }
}