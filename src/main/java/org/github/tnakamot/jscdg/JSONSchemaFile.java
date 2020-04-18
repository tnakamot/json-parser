package org.github.tnakamot.jscdg;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class JSONSchemaFile {
    private URL url;
    private JSONObject jsonObject;
    private JSONSchemaVersion schemaVersion;

    public JSONSchemaFile(URL url)
            throws
            IOException,
            ParseException,
            UnsupportedJSONSchemaVersionException
    {
        this.url = url;

        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        Object obj = new JSONParser().parse(in);
        this.jsonObject = (JSONObject)obj;
        this.schemaVersion = JSONSchemaVersion.getFromURL((String) jsonObject.get("$schema"));
    }

    public URL getURL() { return url; }
    public JSONObject getJSONObject() { return jsonObject; }
    public JSONSchemaVersion getSchemaVersion() { return this.schemaVersion; }

    /**
     * File name of this JSON Schema file.
     *
     * @return Non-null, non-empty string
     */
    public String getFileName() {
        String path = url.getFile();
        String[] elements = path.split("/");
        if (elements.length == 0 || elements[elements.length - 1].isEmpty()) {
            String host = url.getHost();
            if (host == null || host.isEmpty()) {
                return "_default_.json";
            } else {
                return host;
            }
        } else {
            return elements[elements.length - 1];
        }
    }

    /**
     * Return a string to represent the location of this schema file.
     *
     * @return a string to represent the location of this schema file.
     */
    public String getPrintableLocation() {
        if (url.getProtocol().equals("file")) {
            return url.getPath();
        } else {
            return url.toString();
        }
    }
}
