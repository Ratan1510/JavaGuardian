package com.ratan.guardian;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import java.util.concurrent.Callable;
@Command(name = "guardian", mixinStandardHelpOptions =  true, version = "Guardian CLI 1.0", description = "A tool to manage and audit Java Project Dependencies.",  subcommands ={ ScanCommand.class, UpdateCommand.class})

public class GuardianCli implements Callable<Integer>{
    @Override
    public Integer call () throws Exception {
        System.out.println("\uD83D\uDE80 Java Guardian is ready to go!");
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GuardianCli()).execute(args);
        System.exit(exitCode);
    }

}
