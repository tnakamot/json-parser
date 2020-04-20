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

package com.github.tnakamot.jscdg.doxygen;

import com.github.tnakamot.jscdg.*;
import com.github.tnakamot.jscdg.definition.property.*;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.github.tnakamot.jscdg.*;
import org.github.tnakamot.jscdg.definition.property.*;
import com.github.tnakamot.jscdg.table.TableBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.charset.StandardCharsets.UTF_8;
import static com.github.tnakamot.jscdg.table.TableBuilder.*;

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

    private List<TableBuilder> buildTables(JSONProperty property) {
        if (property instanceof JSONObjectProperty) {
            JSONObjectProperty objProperty = (JSONObjectProperty) property;
            List<String> required = Arrays.asList(objProperty.get(JSONObjectProperty.REQUIRED));

            TableBuilder t = new TableBuilder();
            t.setCaption(property.get(JSONObjectProperty.ID),
                    property.get(JSONProperty.TITLE));

            t.addColumn(NAME_COLUMN, "Name");
            t.addColumn(TYPE_COLUMN, "Type");
            t.addColumn(REQUIRE_COLUMN, "Required");
            t.addColumn(DESC_COLUMN, "Description");
            t.addColumn(DEFAULT_COLUMN, "Default");
            t.addColumn(EXAMPLE_COLUMN, "Example");

            objProperty.getPropertyNames().forEach(name -> {
                JSONProperty child = objProperty.getProperty(name);

                t.addRow(name);
                t.addCell(name, NAME_COLUMN, name);
                t.addCell(name, TYPE_COLUMN, child.get(JSONProperty.TYPE));

                String title = child.get(JSONProperty.TITLE);
                String desc  = child.get(JSONProperty.DESCRIPTION);
                if (title == null) {
                    if (desc !=  null) {
                        t.addCell(name, DESC_COLUMN, desc);
                    }
                } else {
                    // TODO: emphasize title
                    if (desc ==  null) {
                        t.addCell(name, DESC_COLUMN, title);
                    } else {
                        t.addCell(name, DESC_COLUMN, title + " - " + desc);
                    }
                }

                if (required.contains(name)) {
                    t.addCell(name, REQUIRE_COLUMN, "Yes");
                } else {
                    t.addCell(name, REQUIRE_COLUMN, "No");
                }

                if (child instanceof JSONStringProperty) {
                    JSONStringProperty strProp = (JSONStringProperty) child;
                    t.addCell(name, DEFAULT_COLUMN,
                            strProp.get(JSONStringProperty.DEFAULT));
                    t.addCell(name, EXAMPLE_COLUMN,
                            strProp.get(JSONStringProperty.EXAMPLES));

                    // TODO: handle specific restrictions
                } else if (child instanceof JSONIntegerProperty) {
                    JSONIntegerProperty intProp = (JSONIntegerProperty) child;
                    Long def = intProp.get(JSONIntegerProperty.DEFAULT);
                    if (def != null){
                        t.addCell(name, DEFAULT_COLUMN, def.toString());
                    }

                    Long[] examples = intProp.get(JSONIntegerProperty.EXAMPLES);
                    t.addCell(name, EXAMPLE_COLUMN,
                            Arrays.asList(examples).stream()
                                    .filter(Objects::nonNull)
                                    .map(l -> l.toString())
                                    .collect(Collectors.toList())
                                    .toArray(new String[0]));

                    // TODO: handle specific restrictions
                } else if (child instanceof JSONNumberProperty) {
                    JSONNumberProperty numProp = (JSONNumberProperty) child;
                    Double def = numProp.get(JSONNumberProperty.DEFAULT);
                    if (def != null){
                        t.addCell(name, DEFAULT_COLUMN, def.toString());
                    }

                    Double[] examples = numProp.get(JSONNumberProperty.EXAMPLES);
                    t.addCell(name, EXAMPLE_COLUMN,
                            Arrays.asList(examples).stream()
                                    .filter(Objects::nonNull)
                                    .map(d -> d.toString())
                                    .collect(Collectors.toList())
                                    .toArray(new String[0]));

                    // TODO: handle specific restrictions
                } else if (child instanceof JSONBooleanProperty) {
                    JSONBooleanProperty boolProp = (JSONBooleanProperty) child;

                    Boolean bool = boolProp.get(JSONBooleanProperty.DEFAULT);
                    if (bool != null){
                        t.addCell(name, DEFAULT_COLUMN, bool.toString());
                    }

                    Boolean[] examples = boolProp.get(JSONBooleanProperty.EXAMPLES);
                    t.addCell(name, EXAMPLE_COLUMN,
                            Arrays.asList(examples).stream()
                                    .filter(Objects::nonNull)
                                    .map(b -> b.toString())
                                    .collect(Collectors.toList())
                                    .toArray(new String[0]));

                    // TODO: handle specific restrictions
                } else if (child instanceof JSONObjectProperty) {
                    JSONObjectProperty objProp = (JSONObjectProperty) child;
                    // TODO: make another table
                } else {
                    // TODO: throw an exception
                }
            });

            return Arrays.asList(t);
        } else {
            return new ArrayList<TableBuilder>();
        }
    }

    private void generate(List<JSONSchemaFile> schemaFiles,
                          Path outputDir,
                          String outputFileExt)
            throws IOException, InvalidJSONSchemaException {
        // TODO: check the duplicate output file names.

        for (JSONSchemaFile schemaFile: schemaFiles) {
            // Convert the format
            JSONProperty root = JSONProperty.convert("_root_", schemaFile.getJSONObject());

            // Create the output file.
            String inputFileName = schemaFile.getFileName();
            String outputFileName =
                    getOutputFileName(inputFileName, outputFileExt);
            Path outputFilePath = outputDir.resolve(outputFileName).toAbsolutePath();

            OutputStream os = Files.newOutputStream(outputFilePath, CREATE, TRUNCATE_EXISTING);
            OutputStreamWriter writer = new OutputStreamWriter(os, UTF_8);

            List<TableBuilder> tables = buildTables(root);
            for (TableBuilder table : tables) {
                table.writeHTML(writer);
            }
            writer.close();

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
