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

    public abstract void main(String[] args) throws Exception;
}
