/*
 *  Copyright (C) 2020 Takashi Nakamoto <nyakamoto@gmail.com>.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.tnakamot.jscdg;

import com.github.tnakamot.jscdg.doxygen.DoxygenGenerator;
import com.github.tnakamot.jscdg.lexer.JSONTokenType;

import java.util.*;
import java.util.stream.Collectors;

public class CLIMain {
    private static final List<SubCommand> SUB_COMMANDS = new ArrayList<>() {{
        add(new DoxygenGenerator("doxygen"));
    }};

    private static String getHelp() {
        StringBuilder s = new StringBuilder();
        s.append("Usage: SUB_COMMAND [OPTIONS]\n");
        s.append("\n");
        s.append(" Valid sub commands: \n");
        SUB_COMMANDS.forEach((subCmd) -> {
            s.append(" * ");
            s.append(subCmd.getName());
            s.append("\n");
        });
        s.append("\n");
        s.append("Run a sub command with -h option to see more options.\n");
        return s.toString();
    }

    private static void printHelp() {
        System.out.println(getHelp());
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            printHelp();
            throw new InvalidCommandLineArgumentException("Need to specify a sub command");
        }

        List<SubCommand> matchedSubCommand =
                SUB_COMMANDS.stream()
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

        System.out.println(JSONTokenType.BEGIN_ARRAY);
    }
}
