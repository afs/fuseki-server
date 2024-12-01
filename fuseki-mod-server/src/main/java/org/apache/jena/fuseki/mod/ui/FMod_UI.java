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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import org.apache.jena.atlas.logging.FmtLog;
import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.sys.FusekiModule;
import org.apache.jena.fuseki.mod.admin.FusekiApp;
import org.apache.jena.fuseki.validation.DataValidator;
import org.apache.jena.fuseki.validation.IRIValidator;
import org.apache.jena.fuseki.validation.QueryValidator;
import org.apache.jena.fuseki.validation.UpdateValidator;
import org.apache.jena.rdf.model.Model;
import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.slf4j.Logger;

public class FMod_UI implements FusekiModule {

    // Only one module needed - it is stateless.
    private static FusekiModule singleton = new FMod_UI();
    public static FusekiModule get() {
        return singleton;
    }

    public FMod_UI() {}

    private static Logger LOG = Fuseki.configLog;

//    // After FMod_admin
//    @Override
//    public int level() {
//        return FusekiApp.levelFModUI;
//    }

    @Override
    public String name() {
        return "FMod UI";
    }


    @Override
    public void prepare(FusekiServer.Builder builder, Set<String> datasetNames, Model configModel) {

        //LOG.info("Fuseki UI loading");

        if ( builder.staticFileBase() != null ) {
            LOG.info("Static content location has already been set: " + builder.staticFileBase());
            return;
        }

        // Find the static content and set all resource lookups to this location.
        // Places to look:
        //    $FUSEKI_BASE/webapp (i.e. run/webapp)
        //    Web resource

        String resourceNameUI = "webapp";

        // Format  jar:file:///.../jena-fuseki-ui-VERSION.jar!/webapp/"
        String uiAppLocation = getResource(resourceNameUI);
        if ( uiAppLocation == null ) {
            LOG.warn("No Static content location has been found");
            return;
        }

        // Simplify name.
        String displayName = uiAppLocation;
        if ( uiAppLocation.startsWith("jar:") ) {
            displayName = displayName.replaceFirst("jar:.*/([^/!]*!.*)", "jar:$1");
        }

        FmtLog.info(this.getClass(), "UI Base = %s", displayName);

        // To have custom content and the UI, unpack the jar for the UI,
        // place under $FUSEKI_BASE/webapp, then add custom content files.

        if ( false ) {
            // Trying to have a
            // Ideal - add a separate serveBetter : add a servlet at "/$/"
            DefaultServlet staticServlet = new DefaultServlet();
            ServletHolder staticContent = new ServletHolder(staticServlet);
//        staticContent.setInitParameter("baseResource", staticContentDir);
//        //staticContent.setInitParameter("cacheControl", "false");
//        context.addServlet(staticContent, "/");
        }

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



    /** Find the UI files */
    private static String getResource(String resourceName) {
        // Try the FUSEKI_BASE area ($FUSEKI_BASE/webapp) to allow an override.
        //
        // This does not exist unless an existing area already exists and has had UI files added.
        // If the FUSEKI_BASE does not exists, it is created later in FMod_admin.prepare
        // and does not include webapp.

        String x = fromPath(FusekiApp.FUSEKI_BASE, resourceName);
        if ( x != null ) {
            LOG.info("Fuseki UI - path resource: "+x);
            return x;
        }

        String r = fromClasspath(resourceName);
        if ( r != null ) {
            LOG.info("Fuseki UI - class loader resource");
            return r;
        }

        // ?? FUSEKI_HOME - no longer used.
        return null;
    }

    // Look for "$resourceName" on the classpath.
    private static String fromClasspath(String resourceName) {
        // Jetty 12.0.15  => warning "Leaked mount"
        // Logger : "org.eclipse.jetty.util.resource.ResourceFactory"
        //ResourceFactory resourceFactory = ResourceFactory.root();

        ResourceFactory resourceFactory = ResourceFactory.closeable();
        Resource resource = resourceFactory.newClassLoaderResource(resourceName);
        if ( resource != null )
            return resource.getURI().toString();
        return null;
    }

    // Look for "$path/$resourceName"
    private static String fromPath(Path path, String resourceName) {
        if ( path != null ) {
            Path path2 = path.resolve(resourceName);
            if ( Files.exists(path2) )
                return path2.toAbsolutePath().toString();
        }
        return null;
    }
}
