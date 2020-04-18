package org.github.tnakamot.jscdg;

import org.github.tnakamot.jscdg.doxygen.DoxygenGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class CLIMain {
    private static List<SubCommand> subCommands = new ArrayList<SubCommand>() {{
        add(new DoxygenGenerator("doxygen"));
    }};

    private static String getHelp() {
        StringBuilder s = new StringBuilder();
        s.append("Usage: SUB_COMMAND [OPTIONS]\n");
        s.append("\n");
        s.append(" Valid sub commands: \n");
        subCommands.forEach((subCmd) -> s.append(" * " + subCmd.getName() + "\n"));
        s.append("\n");
        s.append("Run a sub command with -h option to see more options.\n");
        return s.toString();
    }

    private static void printHelp() {
        System.out.println(getHelp());
    }

    public static void main(String args[]) throws Exception {
        if (args.length == 0) {
            printHelp();
            throw new InvalidCommandLineArgumentException("Need to specify a sub command");
        }

        List<SubCommand> matchedSubCommand =
                subCommands.stream()
                        .filter(sc -> sc.getName().equals(args[0]))
                        .collect(Collectors.toList());

        switch (matchedSubCommand.size()) {
            case 0:
                printHelp();
                throw new InvalidCommandLineArgumentException(args[0] + " is not a valid sub command");
            case 1:
                SubCommand subCmd = matchedSubCommand.get(0);
                subCmd.main(Arrays.copyOfRange(args, 1, args.length));
                break;
            default:
                throw new RuntimeException("There are two or more sub commands of the same name '" +
                                           args[0] + "' are defined.");
        }
    }
}
