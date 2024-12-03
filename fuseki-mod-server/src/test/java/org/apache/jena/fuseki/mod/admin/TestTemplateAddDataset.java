/**
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

package org.apache.jena.fuseki.mod.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.apache.jena.atlas.lib.FileOps;
import org.apache.jena.atlas.logging.LogCtl;
import org.apache.jena.atlas.web.HttpException;
import org.apache.jena.atlas.web.TypedInputStream;
import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.sys.FusekiModules;
import org.apache.jena.http.HttpOp;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.exec.http.Params;
import org.apache.jena.web.HttpSC;
import org.awaitility.Awaitility;

/**
 * Tests of the admin functionality on an empty server and using the template mechanism.
 * See also {@link TestAdmin}.
 */
public class TestTemplateAddDataset {

    private String serverURL = null;
    private FusekiServer server = null;

    @BeforeEach public void startServer() {
        System.setProperty("FUSEKI_BASE", "target/run");
        FileOps.clearAll("target/run");

        server = createServerForTest();
        serverURL = server.serverURL();
    }

    // Exactly the module under test
    private static FusekiModules moduleSetup() {
        return FusekiModules.create(FMod_Admin.create());
    }

    private FusekiServer createServerForTest() {
        FusekiModules modules = moduleSetup();
        DatasetGraph dsg = DatasetGraphFactory.createTxnMem();
        FusekiServer testServer = FusekiServer.create()
                .fusekiModules(modules)
                .port(0)
                .build()
                .start();
        return testServer;
    }

    @AfterEach public void stopServer() {
        if ( server != null )
            server.stop();
        serverURL = null;
        // Clearup FMod_Shiro.
        System.getProperties().remove(FusekiApp.envFusekiShiro);
    }

    protected String urlRoot() {
        return serverURL;
    }

    protected String adminURL() {
        return serverURL+"$/";
    }

    @BeforeEach public void setLogging() {
        LogCtl.setLevel(Fuseki.backupLogName, "ERROR");
        LogCtl.setLevel(Fuseki.compactLogName,"ERROR");
        Awaitility.setDefaultPollDelay(20,TimeUnit.MILLISECONDS);
        Awaitility.setDefaultPollInterval(50,TimeUnit.MILLISECONDS);
    }

    @AfterEach public void unsetLogging() {
        LogCtl.setLevel(Fuseki.backupLogName, "WARN");
        LogCtl.setLevel(Fuseki.compactLogName,"WARN");
    }


    @Test public void add_delete_api_1() throws Exception {
        if ( org.apache.jena.tdb1.sys.SystemTDB.isWindows )
            return;
        testAddDelete("db_mem", "mem", false);
    }

    private void testAddDelete(String dbName, String dbType, boolean hasFiles) {
        String datasetURL = server.datasetURL(dbName);
        Params params = Params.create().add("dbName", dbName).add("dbType", dbType);

        assertFalse(exists(datasetURL));

        // Use the template
        HttpOp.httpPostForm(adminURL()+"datasets", params);

        RDFConnection conn = RDFConnection.connect(server.datasetURL(dbName));
        conn.update("INSERT DATA { <x:s> <x:p> 123 }");
        int x1 = count(conn);
        assertEquals(1, x1);

        Path pathDB = FusekiApp.dirDatabases.resolve(dbName);

        if ( hasFiles )
            assertTrue(Files.exists(pathDB));

        HttpOp.httpDelete(adminURL()+"datasets/"+dbName);

        assertFalse(exists(datasetURL));

        //if ( hasFiles )
            assertFalse(Files.exists(pathDB));

        // Recreate : no contents.
        HttpOp.httpPostForm(adminURL()+"datasets", params);
        assertTrue(exists(datasetURL), ()->"false: exists("+datasetURL+")");
        int x2 = count(conn);
        assertEquals(0, x2);
        if ( hasFiles )
            assertTrue(Files.exists(pathDB));
    }

    private static boolean exists(String url) {
        try ( TypedInputStream in = HttpOp.httpGet(url) ) {
            return true;
        } catch (HttpException ex) {
            if ( ex.getStatusCode() == HttpSC.NOT_FOUND_404 )
                return false;
            throw ex;
        }
    }

    static int count(RDFConnection conn) {
        try ( QueryExecution qExec = conn.query("SELECT (count(*) AS ?C) { ?s ?p ?o }")) {
            return qExec.execSelect().next().getLiteral("C").getInt();
        }
    }
}

