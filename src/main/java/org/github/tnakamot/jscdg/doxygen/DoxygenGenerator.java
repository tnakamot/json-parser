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

package org.github.tnakamot.jscdg.doxygen;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.github.tnakamot.jscdg.JSONSchemaFile;
import org.github.tnakamot.jscdg.JSONSchemaVersion;
import org.github.tnakamot.jscdg.SubCommand;
import org.github.tnakamot.jscdg.UnsupportedJSONSchemaVersionException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DoxygenGenerator extends SubCommand {
    private static final JSONSchemaVersion[] SUPPORTED_SCHEMA_VERSIONS = {
            JSONSchemaVersion.DRAFT_07
    };

    private void checkSchemaVersion(JSONSchemaFile schemaFile)
            throws UnsupportedJSONSchemaVersionException
    {
        JSONSchemaVersion version = schemaFile.getSchemaVersion();

        for (JSONSchemaVersion supportedVersion: SUPPORTED_SCHEMA_VERSIONS) {
            if (supportedVersion.equals(version)) {
                return;
            }
        }

        throw new UnsupportedJSONSchemaVersionException(
                version,
                SUPPORTED_SCHEMA_VERSIONS);
    }

    private String getOutputFileName(String inputFileName,
                                     String outputFileExt) {
        int i = inputFileName.lastIndexOf(".");
        String base = (i == -1 ? inputFileName : inputFileName.substring(0,i));
        return base + "." + outputFileExt;
    }

    private void generate(List<JSONSchemaFile> schemaFiles,
                          Path outputDir,
                          String outputFileExt)
    {
        // TODO: check the duplicate output file names.

        for (JSONSchemaFile schemaFile: schemaFiles) {
            // Create the output file.
            String inputFileName = schemaFile.getFileName();
            String outputFileName =
                    getOutputFileName(inputFileName, outputFileExt);
            Path outputFilePath = outputDir.resolve(outputFileName).toAbsolutePath();

            System.out.println(schemaFile.getPrintableLocation() +
                    " ==>> " + outputFilePath.toString());
        }
    }

    private ArgumentParser getArgumentParser() {
        ArgumentParser parser = ArgumentParsers.newFor(getName()).build()
                .defaultHelp(true)
                .description("Generate doxygen contents.");
        parser.addArgument("-o", "--output")
                .setDefault(".")
                .help("output directory");
        parser.addArgument("-e", "--extension")
                .setDefault("dox")
                .help("output file extension");
        parser.addArgument("file").nargs("+")
                .help("JSON Schema file to convert");

        return parser;
    }

    @Override
    public void main(String[] args) throws Exception {
        ArgumentParser argParser = getArgumentParser();

        try {
            Namespace opts = argParser.parseArgs(args);
            List<JSONSchemaFile> jsonSchemaFiles =
                    getJSONSchemaFiles(opts.getList("file"));
            for (JSONSchemaFile jsonSchemaFile: jsonSchemaFiles) {
                checkSchemaVersion(jsonSchemaFile);
            }

            Path outputDir = Paths.get(opts.getString("output"));
            Files.createDirectories(outputDir);

            String outputFileExt = opts.getString("extension");
            generate(jsonSchemaFiles,
                    outputDir,
                    outputFileExt);
        } catch (ArgumentParserException e) {
            argParser.handleError(e);
        }
    }

    public DoxygenGenerator(String name) { super(name); }
}
