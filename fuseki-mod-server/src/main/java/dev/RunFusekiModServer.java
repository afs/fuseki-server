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

package dev;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;

import org.apache.jena.atlas.lib.FileOps;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.sys.FusekiModules;
import org.apache.jena.fuseki.mod.admin.FMod_Admin;
import org.apache.jena.fuseki.mod.prometheus.FMod_Prometheus;
import org.apache.jena.fuseki.mod.shiro.FMod_Shiro;
import org.apache.jena.fuseki.mod.ui.FMod_UI;
import org.apache.jena.fuseki.run.FusekiModServer;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.http.HttpOp;
import org.apache.jena.http.auth.AuthEnv;
import org.apache.jena.irix.SystemIRIx;
import org.apache.jena.rdflink.RDFLink;
import org.apache.jena.riot.WebContent;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.exec.RowSet;
import org.apache.jena.sparql.exec.RowSetOps;
import org.apache.jena.sys.JenaSystem;

public class RunFusekiModServer {

    static {
        System.getProperties().setProperty(SystemIRIx.sysPropertyProvider, "IRI3986");
        FusekiLogging.setLogging();
        JenaSystem.init();
    }

//    /** Current working directory - without a trailing slash */
//    private static String currentDirectory() {
//        //return System.getProperty("user.dir");
//        String x = Paths.get(".").toAbsolutePath().normalize().toString();
//        System.out.println(x);
//        return x;
//    }

    public static void main(String ... a) throws Exception {
        try {
            mainFusekiModServer(a);
            //mainx(a);
        } catch(Throwable th) { th.printStackTrace(); }
        finally { System.exit(0); }
    }

    public static void mainFusekiModServer(String ... a) {
        FusekiModServer.main("--shiro=shiro.ini", "--mem", "/ds");
    }

    public static void mainx(String ... a) {

        final boolean cleanStart = true;

        // --- Setup

        //FileOps.
        //System.setProperty("FUSEKI_HOME", "run");

        System.setProperty("FUSEKI_BASE", "run");
        if ( cleanStart ) {
            FileOps.clearAll("run");
        }

        // << ---- FusekiModServer
        FusekiModules modules = FusekiModules.create( FMod_Admin.get()
                                                    , FMod_UI.get()
                                                    , FMod_Shiro.get()
                                                    , FMod_Prometheus.get()
                   );

        DatasetGraph dsg = DatasetGraphFactory.createTxnMem();
        FusekiServer server = FusekiServer.create()
                .fusekiModules(modules)
                //.add("/ds", dsg) -- not with a later create
                .port(4040)
                .build()
                .start();
        // >> ---- FusekiModServer
        client(server, cleanStart);
    }

    public static void client(FusekiServer server, boolean cleanStart) {
        // Try out admin functions.

        String serverURL = server.serverURL();
        String local = Host.getLocalHostLANAddress().getHostAddress();
        serverURL = serverURL.replace("localhost", local);

        String datasetURL = serverURL+"ds";
        String name = URLEncoder.encode("/ds", StandardCharsets.UTF_8);
        System.out.println(serverURL);
        // ----

        // Create dataset
        //http://localhost:4040/$/datasets
        //dbName=%2Fds&dbType=mem

        // basic auth. admin-pw
        String adminURL = serverURL+"$";
        // ***************************************************
        AuthEnv.get().registerUsernamePassword(URI.create(adminURL), "admin","pw");

        if ( cleanStart ) {
            HttpOp.httpPost(adminURL+"/datasets",
                            WebContent.contentTypeHTMLForm,
                            BodyPublishers.ofString("dbName="+name+"&dbType=mem", StandardCharsets.UTF_8));
        }

//        String x = HttpOp.httpGetString(adminURL+"/datasets");
//        System.out.print(x);

        try ( RDFLink link = RDFLink.connectPW(datasetURL, "user1", "passwd1") ) {
            link.update("INSERT DATA { <x:s> <x:p> 123 }");
            RowSet rs = link.query("SELECT * {?s ?p ?o}").select();
            RowSetOps.out(rs);
        }

        if ( false ) {
            try {
                server.join();
            } catch (Throwable th) {
                th.printStackTrace();
            } finally {
                System.exit(0);
            }
        }
    }
}
