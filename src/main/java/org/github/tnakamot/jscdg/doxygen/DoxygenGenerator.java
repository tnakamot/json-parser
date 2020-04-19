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
import org.github.tnakamot.jscdg.*;
import org.github.tnakamot.jscdg.definition.property.JSONIntegerProperty;
import org.github.tnakamot.jscdg.definition.property.JSONProperty;
import org.github.tnakamot.jscdg.definition.value.JSONIntegerValue;
import org.github.tnakamot.jscdg.table.TableBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.github.tnakamot.jscdg.table.TableBuilder.*;

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

    private static String getAndValidateAsString(JSONObject j, Object key)
            throws InvalidJSONSchemaException {
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }

        Object val = j.get(key);
        if (val == null)
            return null;

        if (!(val instanceof String)) {
            StringBuilder msg = new StringBuilder();
            msg.append("'");
            msg.append(key.toString());
            msg.append("' must be a string. Currently, the data type is '");
            msg.append(val.getClass().toString());
            msg.append("' and the value is '");
            msg.append(val.toString());
            msg.append("'.");
            throw new InvalidJSONSchemaException(msg.toString());
        }

        return (String)val;
    }

    private static void setCaption(TableBuilder t, JSONObject j)
            throws InvalidJSONSchemaException {
        String id = getAndValidateAsString(j, "$id");
        String title = getAndValidateAsString(j, "title");

        t.setCaption(id, title);
    }

    private TableBuilder buildTable(JSONObject j)
            throws InvalidJSONSchemaException {
        TableBuilder t = new TableBuilder();

        setCaption(t, j);
        t.addColumn(NAME_COLUMN, "Name");
        t.addColumn(TYPE_COLUMN, "Type");
        t.addColumn(REQUIRE_COLUMN, "Required");
        t.addColumn(DESC_COLUMN, "Description");
        t.addColumn(DEFAULT_COLUMN, "Default");
        t.addColumn(EXAMPLE_COLUMN, "Example");

        JSONObject props = (JSONObject) j.get("properties");
        JSONObject name  = (JSONObject) props.get("name");

        JSONProperty prop = new JSONIntegerProperty("num", (JSONObject) props.get("num"));
        System.out.println(prop.get(JSONProperty.DESCRIPTION));

        System.out.println(prop.get(JSONIntegerProperty.MINIMUM));

        // TODO: build table from JSON object here.

        return t;
    }

    private void generate(List<JSONSchemaFile> schemaFiles,
                          Path outputDir,
                          String outputFileExt)
            throws IOException, InvalidJSONSchemaException {
        // TODO: check the duplicate output file names.

        for (JSONSchemaFile schemaFile: schemaFiles) {
            // Create the output file.
            String inputFileName = schemaFile.getFileName();
            String outputFileName =
                    getOutputFileName(inputFileName, outputFileExt);
            Path outputFilePath = outputDir.resolve(outputFileName).toAbsolutePath();

            OutputStream os = Files.newOutputStream(outputFilePath, CREATE, TRUNCATE_EXISTING);
            OutputStreamWriter writer = new OutputStreamWriter(os, UTF_8);
            TableBuilder table = buildTable(schemaFile.getJSONObject());

            table.writeHTML(writer);
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
