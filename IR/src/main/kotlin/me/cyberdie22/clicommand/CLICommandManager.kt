package me.cyberdie22.clicommand

class CLICommandManager(val prefix: String) {
    private val commands = mutableListOf<CLICommand>()

    fun runCommand(cmd: String): Boolean {
        if (cmd.startsWith(prefix)) {
            commands.forEach {
                it.invokeIfCommand(cmd, prefix)
            }
            return true
        }
        return false
    }

    fun registerCommand(cmd: CLICommand) {
        commands.add(cmd)
    }

    fun unregisterCommand(cmd: CLICommand) {
        commands.remove(cmd)
    }

    fun unregisterCommand(cmd: String) {
        commands.forEach {
            if (it.name == cmd)
                commands.remove(it)
        }
    }
}