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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class JSONSchemaVersion {
    private static final String DRAFT_06_ID = "draft-06";
    private static final String DRAFT_07_ID = "draft-07";
    private static final String DRAFT_2019_09_ID = "2019-09";

    /*
     * Valid JSON Schema versions
     *
     * References:
     *   https://json-schema.org/specification-links.html
     *   https://json-schema.org/understanding-json-schema/reference/schema.html
     */
    public static final JSONSchemaVersion DRAFT_06 = new JSONSchemaVersion(
            DRAFT_06_ID,
            "Draft 6",
            new String[] {
                    "http://json-schema.org/draft-06/schema#",
                    "https://json-schema.org/draft-06/schema#",
            }
    );

    public static final JSONSchemaVersion DRAFT_07 = new JSONSchemaVersion(
            DRAFT_07_ID,
            "Draft 7",
            new String[] {
                    "http://json-schema.org/draft-07/schema#",
                    "https://json-schema.org/draft-07/schema#"
            }
    );

    public static final JSONSchemaVersion DRAFT_2019_09 = new JSONSchemaVersion(
            DRAFT_2019_09_ID,
            "Draft 8",
            new String[] {
                    "http://json-schema.org/draft/2019-09/schema#",
                    "https://json-schema.org/draft/2019-09/schema#"
            }
    );

    public static final JSONSchemaVersion[] VALID_VERSIONS = {
            DRAFT_06,
            DRAFT_07,
            DRAFT_2019_09
    };

    public static JSONSchemaVersion getFromURL(String url)
            throws UnsupportedJSONSchemaVersionException
    {
        return getFromURL(url, VALID_VERSIONS);
    }

    public static JSONSchemaVersion getFromURL(String url,
                                               JSONSchemaVersion[] supportedVersions)
            throws UnsupportedJSONSchemaVersionException
    {
        for (JSONSchemaVersion version: supportedVersions) {
            for (String verUrl: version.getURLs()) {
                if (verUrl.equals(url)) {
                    return version;
                }
            }
        }

        throw new UnsupportedJSONSchemaVersionException(
                "Unknown JSON Schema URL: " + url,
                supportedVersions);
    }

    private final String metaSchemaId;
    private final String commonName;
    private final Collection<String> urls;

    private JSONSchemaVersion(String metaSchemaId,
                             String commonName,
                             String[] urls) {
        this.metaSchemaId = metaSchemaId;
        this.commonName = commonName;
        this.urls = Collections.unmodifiableCollection(Arrays.asList(urls));
    }

    public String getMetaSchemaID() {
        return metaSchemaId;
    }

    public String getCommonName() {
        return commonName;
    }

    public String[] getURLs(){
        return urls.toArray(new String[0]);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof JSONSchemaVersion) {
            JSONSchemaVersion version = (JSONSchemaVersion) obj;
            return version.metaSchemaId.equals(this.metaSchemaId);
        } else {
            return false;
        }
    }
}
