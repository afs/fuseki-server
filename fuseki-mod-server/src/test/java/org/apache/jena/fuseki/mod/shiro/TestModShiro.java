/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.fuseki.mod.shiro;


import static org.junit.jupiter.api.Assertions.fail;

import java.net.URI;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.apache.jena.atlas.web.HttpException;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.http.auth.AuthEnv;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.exec.http.GSP;
import org.apache.jena.web.HttpSC;

public class TestModShiro {
    static {
        //FusekiLogging.setLogging();
    }

    private static String originalSetting = null;

    @BeforeAll public static void beforeClass() {
        originalSetting = System.getProperty("FUSEKI_BASE");
    }

    @BeforeAll public void before() {
        System.setProperty("FUSEKI_BASE", "src/test/files");
    }

    @AfterAll public static void afterClass() {
        System.setProperty("FUSEKI_BASE", originalSetting);
    }

    private static void run(Runnable before, Runnable action, Runnable after) {
        if ( before != null )
            before.run();
        try {
            action.run();
        } finally {
            if ( after != null )
                after.run();
        }
    }

    @Test public void testModShiro_1() {
        String x = System.getProperty("FUSEKI_BASE");
        run(()->System.setProperty("FUSEKI_BASE", "src/test/files"),
            ()->runModShiro_1(),
            ()->System.setProperty("FUSEKI_BASE", x)
            );
        }

    private void runModShiro_1() {
        FusekiServer server = FusekiServer.create()
                .port(0)
                .add("/ds", DatasetGraphFactory.createTxnMem())
                .start();

        String URL = "http://localhost:"+server.getPort()+"/ds";

        // Works.
        AuthEnv.get().registerUsernamePassword(URI.create(URL), "user1", "passwd1");
        GSP.service(URL).defaultGraph().GET();

        // Necessary for Jena <= 4.7.0
        // AuthEnv.get().clearAuthEnv();
        try {
            // Fails - wrong password.
            AuthEnv.get().registerUsernamePassword(URI.create(URL), "user1", "passwd2");
            GSP.service(URL).defaultGraph().GET();
            fail("Bad ** Expected  401");
        } catch (HttpException httpEx) {
            if ( httpEx.getStatusCode() != HttpSC.UNAUTHORIZED_401 )
                throw httpEx;
            // Ignore 401.
        }
    }
}
