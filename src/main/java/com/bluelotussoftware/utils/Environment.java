/*
 * Copyright 2021 John Yeary <jyeary@bluelotussoftware.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluelotussoftware.utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to read the environment variables from the server/OS.
 *
 * @author John Yeary <jyeary@bluelotussoftware.com>
 */
public class Environment {

    private static final String SQLITE_PU = "sqlitePU";

    private Map<String, String> env;

    /**
     * Default constructor.
     */
    public Environment() {
        this.env = System.getenv();
    }

    /**
     * Returns an entity manager factory using the defined environment variables
     * that this class has access to.
     *
     * @return EntityManagerFactory
     */
    public EntityManagerFactory getEntityManagerFactory() {
        if (getDatabaseURL() != null) {
            Map<String, String> configOverrides = new HashMap<>();
            configOverrides.put("javax.persistence.jdbc.url", getDatabaseURL());
            return Persistence.createEntityManagerFactory(SQLITE_PU, configOverrides);
        } else {
            return Persistence.createEntityManagerFactory(SQLITE_PU);
        }
    }

    /**
     * Get the SQLite database URL.
     *
     * @return the URL from the environment variable
     * {@literal SQLITE_DATABASE_URL}.
     */
    public String getDatabaseURL() {
        return env.get("SQLITE_DATABASE_URL");
    }

}
