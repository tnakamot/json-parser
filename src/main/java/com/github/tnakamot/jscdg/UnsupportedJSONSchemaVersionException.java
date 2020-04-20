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

public class UnsupportedJSONSchemaVersionException extends Exception {
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
            s.append("[");
            s.append(version.getCommonName());
            s.append("] ");
            s.append("(ID: ");
            s.append(version.getMetaSchemaID());
            s.append(")\n");
            for (String url: version.getURLs()) {
                s.append("  ");
                s.append(url);
                s.append("\n");
            }
        }
        return s.toString();
    }
}
