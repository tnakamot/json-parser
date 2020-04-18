package org.github.tnakamot.jscdg;

public class UnsupportedJSONSchemaVersionException extends Exception {
    public UnsupportedJSONSchemaVersionException (String msg) {
        super(msg);
    }

    public UnsupportedJSONSchemaVersionException (String msg,
                                                  JSONSchemaVersion[] supportedVersions) {
        super(msg + "\nSupported JSON Schema versions are \n" +
                getPrintableVersions(supportedVersions));
    }

    public UnsupportedJSONSchemaVersionException (JSONSchemaVersion version,
                                                  JSONSchemaVersion[] supportedVersions) {
        super("JSON Scheme " + version.getCommonName() + " is not supported.\n" +
              "Supported JSON Schema versions are \n" +
              getPrintableVersions(supportedVersions));
    }

    private static String getPrintableVersions(JSONSchemaVersion[] versions) {
        StringBuilder s = new StringBuilder();
        for (JSONSchemaVersion version: versions) {
            s.append("[" + version.getCommonName() + "] ");
            s.append("(ID: " + version.getMetaSchemaID() + ")\n");
            for (String url: version.getURLs()) {
                s.append("  " + url + "\n");
            }
        }
        return s.toString();
    }
}
