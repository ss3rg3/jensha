package jensha.commands

class CcliCommand extends Command {

    CcliCommand(String clusterName) {
        // Don't use setBaseCommand() in constructor, causes CPS exception
        this.command.add("python3 ccli.py")
        this.command.add(clusterName)
    }

    CcliCommand enableDebug() {
        this.setBaseCommand("${this.command.get(0)} --debug")
        return this
    }

    CcliCommand build() {
        this.add("build")
        return this
    }

}
