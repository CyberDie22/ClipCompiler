import com.diogonunes.jcolor.AnsiFormat
import com.diogonunes.jcolor.Ansi
import com.diogonunes.jcolor.Ansi.colorize
import com.diogonunes.jcolor.Attribute.*
import me.cyberdie22.clicommand.CLICommand
import me.cyberdie22.clicommand.CLICommandManager
import me.cyberdie22.clipc.ir.Evaluator
import me.cyberdie22.clipc.ir.lexer.Lexer
import me.cyberdie22.clipc.ir.lexer.SyntaxKind
import me.cyberdie22.clipc.ir.lexer.SyntaxToken
import me.cyberdie22.clipc.ir.parser.Parser
import me.cyberdie22.clipc.ir.parser.SyntaxNode
import me.cyberdie22.clipc.ir.parser.SyntaxTree
import java.util.*

val INFO = AnsiFormat(TEXT_COLOR(169, 169, 169))
val WARN = AnsiFormat(YELLOW_TEXT())
val ERROR = AnsiFormat(TEXT_COLOR(139, 0, 0))

fun main(args: Array<String>) {
    val console = Scanner(System.`in`)

    val commandManager = CLICommandManager("#")
    commandManager.registerCommand(CLICommand(
        "forceShowParseTree",
        """
            Usage:
                #forceParseTree on      - Forces the parse tree to be shown.
                #forceParseTree off     - Disables forcing the parse tree being shown.
        """.trimIndent()
    ) { cmd, usage ->
        val arg1: String
        try {
            arg1 = cmd.split(' ')[1]
        } catch (e: Exception) {
            println(colorize(usage, ERROR))
            return@CLICommand
        }
        when (arg1) {
            "on" -> {
                forceShowParseTree = true
                println(colorize("Forcing parse tree to be shown.", INFO))
            }
            "off" -> {
                forceShowParseTree = false
                println(colorize("No longer forcing parse tree to be shown.", INFO))
            }
            else -> println(colorize(usage, ERROR))
        }
    })

    commandManager.registerCommand(CLICommand(
        "showParseTree",
        """
            Usage:
                #showParseTree on      - Shows the parse tree.
                #showParseTree off     - Disables showing the parse tree.
        """.trimIndent()
    ) { cmd, usage ->
        val arg1: String
        try {
            arg1 = cmd.split(' ')[1]
        } catch (e: Exception) {
            println(colorize(usage, ERROR))
            return@CLICommand
        }
        when (arg1) {
            "on" -> {
                showParseTree = true
                println(colorize("Showing parse tree.", INFO))
            }
            "off" -> {
                showParseTree = false
                println(colorize("No longer showing parse tree.", INFO))
            }
            else -> println(colorize(usage, ERROR))
        }
    })

    commandManager.registerCommand(CLICommand(
        "clear",
        """
            Usage:
                #clear      - Clears the console
        """.trimIndent()
    ) { _, _ ->
        println(System.lineSeparator().repeat(100))
    })

    while (true) {
        print("> ")
        val line = console.nextLine()
        if (line.isNullOrEmpty())
            break
        if (line == "#quit" || line == "#exit")
            break
        if (commandManager.runCommand(line))
            continue

        val syntaxTree = SyntaxTree.parse(line)

        if (forceShowParseTree)
            prettyPrint(syntaxTree.root)
        if (!syntaxTree.diagnostics.any() && showParseTree)
            prettyPrint(syntaxTree.root)

        if (syntaxTree.diagnostics.any())
            syntaxTree.diagnostics.forEach(::println)
        else {
            val evaluator = Evaluator(syntaxTree.root)
            val result = evaluator.evaluate()
            println(colorize(result.toString(), INFO))
        }
    }
}

var forceShowParseTree = false
var showParseTree = false

fun prettyPrint(node: SyntaxNode, indent: String = "", isLast: Boolean = true) {
    // └──
    // ├──
    // │

    val marker = if (isLast) "└──" else "├──"
    var indent = indent
    var output = indent + marker + node.kind.toString()

    if (node is SyntaxToken && node.value != null) {
        output += " ${node.value}"
    }

    println(colorize(output, INFO))

    indent += if (isLast) "    " else "│   "

    val lastChild = node.getChildren().lastOrNull()

    node.getChildren().forEach {
        prettyPrint(it, indent, it == lastChild)
    }
}