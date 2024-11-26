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

package org.apache.jena.fuseki.mod.ui;

import java.net.URI;
import java.util.Set;

import org.apache.jena.atlas.logging.FmtLog;
import org.apache.jena.atlas.logging.LogCtl;
import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.sys.FusekiAutoModule;
import org.apache.jena.fuseki.main.sys.FusekiModule;
import org.apache.jena.fuseki.validation.DataValidator;
import org.apache.jena.fuseki.validation.IRIValidator;
import org.apache.jena.fuseki.validation.QueryValidator;
import org.apache.jena.fuseki.validation.UpdateValidator;
import org.apache.jena.rdf.model.Model;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FMod_UI implements FusekiAutoModule {

    private static FusekiModule singleton = new FMod_UI();
    public static FusekiModule get() {
        return singleton;
    }

    public FMod_UI() {}

    private static Logger LOG = Fuseki.configLog;

    @Override
    public void start() {
        Fuseki.serverLog.info("FMod UI");
    }

    @Override
    public String name() {
        return "FMod UI";
    }

    // Before FMod_admin
    @Override
    public int level() {
        return 500;
    }

    @Override
    public void prepare(FusekiServer.Builder builder, Set<String> datasetNames, Model configModel) {

        //LOG.info("Fuseki UI loading");

        if ( builder.staticFileBase() != null ) {
            LOG.info("Static content location has already been set: " + builder.staticFileBase());
            return;
        }

        // Find the static content and set all resource lookups to this location.

//        String resourceNameUI = "webapp";
//        //String uiAppLocation = "jar:/home/afs/.m2/repository/org/apache/jena/jena-fuseki-ui/5.3.0-SNAPSHOT/jena-fuseki-ui-5.3.0-SNAPSHOT.jar!";
//
//        String uiAppLocation = getResource(resourceNameUI);
        String uiAppLocation = "/home/afs/ASF/fuseki-server/UI/app";
        FmtLog.info(this.getClass(), "UI Base = %s", uiAppLocation);

        // If no admin ....
        // Check admin available.
        builder.staticFileBase(uiAppLocation)

                .addServlet("/$/validate/query", new QueryValidator())
                .addServlet("/$/validate/update", new UpdateValidator())
                .addServlet("/$/validate/iri", new IRIValidator())
                .addServlet("/$/validate/data", new DataValidator())
                .enableStats(true);

        // LOG.info("Fuseki UI loaded");
    }

//    if ( staticContentDir != null ) {
//        DefaultServlet staticServlet = new DefaultServlet();
//        ServletHolder staticContent = new ServletHolder(staticServlet);
//        staticContent.setInitParameter("baseResource", staticContentDir);
//        //staticContent.setInitParameter("cacheControl", "false");
//        context.addServlet(staticContent, "/");
//    } else {
//        // Backstop servlet
//        // Jetty default is 404 on GET and 405 otherwise
//        HttpServlet staticServlet = new Servlet404();
//        ServletHolder staticContent = new ServletHolder(staticServlet);
//        context.addServlet(staticContent, "/");
//    }

    // https://schneide.blog/2024/02/05/serving-static-files-from-a-jar-with-with-jetty/

    // LogCtl calcWithLevel.
    private static String getResource(String resourceName) {

        if ( true ) {
            URI uri = URI.create("file:/home/afs/.m2/repository/org/apache/jena/jena-fuseki-ui/5.3.0-SNAPSHOT/jena-fuseki-ui-5.3.0-SNAPSHOT.jar");
            try (ResourceFactory.Closeable resourceFactory = ResourceFactory.closeable()) {
                org.eclipse.jetty.util.resource.Resource uiApp = resourceFactory.newJarFileResource(uri);
                if ( uiApp == null )
                    return null;
                return  uiApp.toString();
            }
        }

//        URIUtil.toJarFileUri(uri)
//        ResourceFactory.root().newJarFileResource(null);

        Logger logger = LoggerFactory.getLogger("org.eclipse.jetty.util.resource.ResourceFactory");
        String currentLevel = LogCtl.getLevel(logger);
        try {
            LogCtl.setLevel(logger, "ERROR");
            org.eclipse.jetty.util.resource.Resource uiApp = ResourceFactory.closeable().newClassLoaderResource(resourceName, false);
            if ( uiApp == null )
                return null;
            return  uiApp.toString();
        } finally {
            LogCtl.setLevel(logger, currentLevel);
        }
    }
}
