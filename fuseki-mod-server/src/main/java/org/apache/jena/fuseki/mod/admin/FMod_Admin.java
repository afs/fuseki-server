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

package org.apache.jena.fuseki.mod.admin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.apache.jena.atlas.logging.FmtLog;
import org.apache.jena.cmd.ArgDecl;
import org.apache.jena.cmd.ArgModuleGeneral;
import org.apache.jena.cmd.CmdArgModule;
import org.apache.jena.cmd.CmdGeneral;
import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.FusekiConfigException;
import org.apache.jena.fuseki.build.FusekiConfig;
import org.apache.jena.fuseki.ctl.ActionCtl;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.cmds.ServerArgs;
import org.apache.jena.fuseki.main.sys.FusekiModule;
import org.apache.jena.fuseki.mgt.ActionBackup;
import org.apache.jena.fuseki.mgt.ActionBackupList;
import org.apache.jena.fuseki.mgt.ActionDatasets;
import org.apache.jena.fuseki.server.DataAccessPoint;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;

public class FMod_Admin implements FusekiModule {

    private static FusekiModule singleton = new FMod_Admin();
    // ----

//    @Override
//    public void start() {}
//
//    @Override
//    public int level() {
//        return FusekiApp.levelFModAdmin;
//    }

    @Override
    public String name() {
        return "FMod Admin";
    }

    public static FMod_Admin create() {
        return new FMod_Admin();
    }

    public FMod_Admin() {}

    private static Logger LOG = Fuseki.configLog;

    private ArgDecl argAdmin = new ArgDecl(true, "admin");
    private ArgDecl argAdminArea = new ArgDecl(true, "adminArea", "adminBase");

    @Override
    public void serverArgsModify(CmdGeneral fusekiCmd, ServerArgs serverArgs) {

        fusekiCmd.getUsage().startCategory("Admin");

        ArgModuleGeneral argModule = new ArgModuleGeneral() {
            @Override
            public void registerWith(CmdGeneral cmdLine) {
                cmdLine.add(argAdmin, "--admin", "Enable server admin with user:password");
                cmdLine.add(argAdminArea,"--adminRun", "Directory for server configuration");
            }
            @Override
            public void processArgs(CmdArgModule cmdLine) {}
        };
        argModule.registerWith(fusekiCmd);
    }

    @Override
    public void serverArgsPrepare(CmdGeneral fusekiCmd, ServerArgs serverArgs) {
        String admin = fusekiCmd.getValue(argAdmin);
        if ( admin == null ) {
            return;
        }

        Path directory = null;
        String dirStr = fusekiCmd.getValue(argAdminArea);
        if ( dirStr != null )
            directory = Path.of(dirStr);

        if ( admin.equals("localhost") ) {}
        else {
            String pwFile = admin;
        }

        if ( directory != null ) {
            if ( ! Files.isDirectory(directory) )
                throw new FusekiConfigException("Not a directory: "+dirStr);

            if ( ! Files.isWritable(directory)  )
                throw new FusekiConfigException("Not writable: "+dirStr);
        }
        FusekiApp.FUSEKI_BASE = directory;
    }

//    @Override
//    public void serverArgsBuilder(FusekiServer.Builder serverBuilder, Model configModel) {
//    }

    // ----

    @Override
    public void prepare(FusekiServer.Builder builder, Set<String> datasetNames, Model configModel) {
        // Unpack
        Path path = FusekiApp.setup();

        FmtLog.info(LOG, "Fuseki Admin: %s", path);

        // Shiro.
        Path shiroIni = path.resolve(FusekiApp.DFT_SHIRO_INI);
        if ( Files.exists(shiroIni) ) {
            System.setProperty(FusekiApp.envFusekiShiro, shiroIni.toString());
        } else {
            FmtLog.info(LOG, "No shiro.ini: dir=%s", path);
        }

        String configDir = FusekiApp.dirConfiguration.toString();
        List<DataAccessPoint> directoryDatabases = FusekiConfig.readConfigurationDirectory(configDir);

        if ( directoryDatabases.isEmpty() )
            FmtLog.info(LOG, "No databases: dir=%s", configDir);
        else {
            directoryDatabases.forEach(dap -> FmtLog.info(Fuseki.configLog, "Database: %s", dap.getName()));
        }

        directoryDatabases.forEach(db -> {
            String dbName = db.getName();
            if ( datasetNames.contains(dbName) ) {
                FmtLog.warn(LOG, "Database '%s' already added to the Fuseki server builder", dbName);
                // ?? builder.remove(dbName);
            }
            builder.add(dbName, db.getDataService());
            // ** builder.add(DataAccessPoint);
        });

        // Modify the server to include the admin operations.
        // Security is performed by FMod_Shiro.
        ActionCtl actionBackup = new ActionBackup();
        builder
                .addServlet("/$/datasets/*", new ActionDatasets())
                .addServlet("/$/server", new ActionServerStatus())
                .addServlet("/$/backup/*", actionBackup)
                .addServlet("/$/backups/*", actionBackup)
                .addServlet("/$/backups-list", new ActionBackupList())

                // Enables the task subsystem and is also called by enableCompact
                .enableTasks(true)

                // Can also be enabled by FMod_UI
                .enableStats(true)
                .enablePing(true)
                .enableCompact(true)
                ;
    }
}
