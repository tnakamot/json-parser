package org.github.tnakamot.jscdg;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {
    private final String name;

    public SubCommand(String name) { this.name = name; }
    public String getName() { return name; }

    protected List<JSONSchemaFile> getJSONSchemaFiles(List<String> urlStrs)
            throws
            IOException,
            ParseException,
            UnsupportedJSONSchemaVersionException {
        List<JSONSchemaFile> jsonSchemaFiles = new ArrayList<>();
        for (String urlStr: urlStrs) {
            URL url;
            try {
                url = new URL(urlStr);
            } catch (MalformedURLException ex) {
                // If the given URL cannot be interpreted as a valid URL,
                // take it as a local file path.
                Path path = Paths.get(urlStr);
                url = path.toUri().toURL();
            }

            jsonSchemaFiles.add(new JSONSchemaFile(url));
        }
        return jsonSchemaFiles;
    }

    public abstract void main(String args[]) throws Exception;
}
