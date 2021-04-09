package me.cyberdie22.clicommand

class CLICommand(val name: String, val usage: String, val runner: (cmd: String, usage: String) -> Unit) {
    internal fun invoke(cmd: String) {
        runner(cmd, usage)
    }

    internal fun invokeIfCommand(cmd: String, prefix: String) {
        if (cmd.split(' ')[0] == "$prefix$name")
            runner(cmd, usage)
    }
}