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

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletContext;
import org.apache.jena.atlas.lib.Lib;
import org.apache.jena.cmd.ArgDecl;
import org.apache.jena.cmd.CmdException;
import org.apache.jena.cmd.CmdGeneral;
import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.FusekiConfigException;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.cmds.ServerArgs;
import org.apache.jena.fuseki.main.sys.FusekiAutoModule;
import org.apache.jena.fuseki.main.sys.FusekiModule;
import org.apache.jena.rdf.model.Model;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fuseki Module for Apache Shiro.
 * <p>
 * TODO
 * Configuration
 */
public class FMod_Shiro implements FusekiAutoModule {

    private static FusekiModule singleton = new FMod_Shiro();
    public static FusekiModule get() {
        return singleton;
    }

    // Assumes the whole system is "Shiro".
    // No setup?

    public static final Logger shiroConfigLog = LoggerFactory.getLogger(Fuseki.PATH + ".Shiro");

    private static List<String> defaultIniFileLocations = List.of("file:shiro.ini", "file:run/shiro.ini", "file:/etc/fuseki/shiro.ini");
    private static List<String> iniFileLocations = null;
    public static void setShiroIniLocations(List<String> shiroIniLocations) {
        iniFileLocations = shiroIniLocations;
    }

    private static ArgDecl argShiroIni = new ArgDecl(true, "shiro", "shiro-ini");
    private String shiroFile = null;

    public FMod_Shiro() {}

    // ---- If used from the command line
    @Override
    public void serverArgsModify(CmdGeneral fusekiCmd, ServerArgs serverArgs) {
        fusekiCmd.add(argShiroIni);
    }

    @Override
    public void serverArgsPrepare(CmdGeneral fusekiCmd, ServerArgs serverArgs) {
        if ( fusekiCmd.contains(argShiroIni) ) {
            shiroFile = fusekiCmd.getValue(argShiroIni);
            Path path = Path.of(shiroFile);
            try {
                if ( !fileIsUseable(path) ) {
                    throw new CmdException("No such file: "+path);
                }
            } catch (FusekiConfigException ex) {
                throw new CmdException(ex.getMessage());
            }
        }
    }

    // The filter is added in  prepare(FusekiServer.Builder serverBuilder, Set<String> datasetNames, Model configModel)
    // This allows other Fuseki modules, such as FMod_Admin, to setup shiro.ini.
    // FMod_Admin unpacks a default one to $FUSEKI_BASE/shiro.ini (usually "run/shiro.ini")

//    @Override
//    public void serverArgsBuilder(FusekiServer.Builder serverBuilder, Model configModel) {
//        //Add filter.
//    }

    // ----


    // ---- Pro-tem
    // Add "possible value"
    /**
     * Determine the Shiro configuration file.
     * If none found, return null.
     */
    private static String determineShiroConfigFile() {
        // 1: Model: server??
        // 2: Lib.getenv("FUSEKI_SHIRO");

        // Not needed if FMod_admin has set FUSEKI_SHIRO
        //   Context for build.
        // 3: Lib.getenv("FUSEKI_BASE")+"run/shiro.ini"; -- library code: existing (in main) FusekiLib

        String envFusekiShiro = Lib.getenv("FUSEKI_SHIRO");
        if ( envFusekiShiro != null ) {
            Path path = Path.of(envFusekiShiro);
            if ( ! fileIsUseable(path) )
                throw new FusekiConfigException("No such file: "+path);
            return envFusekiShiro;
        }

//        String envFusekiBase = Lib.getenv("FUSEKI_BASE");
//        if ( envFusekiBase != null ) {
//            Path path= Path.of(envFusekiBase, "shiro.ini");
//            if ( fileIsUseable(path) )
//                return path.toString();
//        }
        return null;
    }
    // ---- Pro-tem

    // XXX To IOX
    /**
     * Return true if the path exists, it names a regular file, and that file is readable.
     * <p>
     * Return false if it does not exist.
     * <p>
     * Throw {@link CmdException} if it exists but is not a regular file or is not readable.
     */
    private static boolean fileIsUseable(Path path) {
        if ( ! Files.exists(path) )
            return false;
        if ( ! Files.isRegularFile(path) )
            throw new FusekiConfigException("Path exists but is not a file: "+path);
        if ( ! Files.isReadable(path) ) {
            throw new FusekiConfigException("File exists but is not readable: "+path);
        }
        return true;
    }

    /* In jena-fuseki-webapp (WAR file) and jena-fuseki-full-jar: FUSEKI_HOME - used
     * to find "webapp" FUSEKI_BASE - used as the administration area
     * jena-fuseki-webapp: WAR (null, "/etc/fuseki") , STANDALONE (".", "run") , */

    @Override
    public void start() {
        Fuseki.serverLog.info("FMod Shiro");
    }

    @Override
    public String name() {
        return "FMod Shiro";
    }

    @Override
    public int level() {
        // Early - so adding the filter puts it at the start of the chain
        // if there is any filter overlap.
        return 100;
    }

    @Override
    public void prepare(FusekiServer.Builder serverBuilder, Set<String> datasetNames, Model configModel) {
        if ( shiroFile == null )
            shiroFile = determineShiroConfigFile();

        // XXX
        // serverBuilder.shiroFile("")

        if ( shiroFile != null ) {
            Filter filter = new FusekiShiroFilter();
            // This is a "before" filter.
            serverBuilder.addFilter("/*", filter);
        }
    }

    /**
     * FusekiShiroFilter, includes Shiro initialization. Fuseki is a not a webapp
     * e.g. not a WAR file so it needs to trigger off servlet initialization.
     */
    private class FusekiShiroFilter extends ShiroFilter {
        boolean runnng = false;
        @Override
        public void init() throws Exception {
            // Can not initShiro in serverBeforeStarting()
            // and serverAfterStarting() is too late.
            initShiro(getServletContext());
            super.init();
        }
    }

    // TODO Use defined name.
    private static void initShiro(ServletContext servletContext) {
        List<String> locations = (iniFileLocations == null) ? defaultIniFileLocations : iniFileLocations;
        String fusekiBase = Lib.getenv("FUSEKI_BASE");
        if ( fusekiBase != null ) {
            Path pathShiroIni = Path.of(fusekiBase).resolve("shiro.ini");
            if ( !Files.exists(pathShiroIni) )
                throw new FusekiConfigException("No such file: $FUSEKI_BASE/shiro.ini (" + pathShiroIni + ")");
            String resource = "file:" + pathShiroIni.toString();
            locations = List.of(resource);
        }
        FusekiShiroLib.shiroEnvironment(servletContext, locations);
    }

    @Override
    public void serverBeforeStarting(FusekiServer server) {

        // shiro servlet.

        // Shiro requires a session handler.
        // This needs the Jetty server to have been created.
        org.eclipse.jetty.server.Server jettyServer = server.getJettyServer();
        try {
            ServletContextHandler servletContextHandler = (ServletContextHandler)(jettyServer.getHandler());
            if ( servletContextHandler.getSessionHandler() == null ) {
                SessionHandler sessionsHandler = new SessionHandler();
                servletContextHandler.setHandler(sessionsHandler);
            }
        } catch (RuntimeException ex) {
            shiroConfigLog.error("Failed to set a session manager - server aborted");
            throw ex;
        }
    }

    @Override
    public void serverAfterStarting(FusekiServer server) {}

// @Override public void serverStopped(FusekiServer server) { }
}
