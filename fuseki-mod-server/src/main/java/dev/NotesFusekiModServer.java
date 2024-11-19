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

import java.util.concurrent.atomic.LongAdder;

public class NotesFusekiModServer {

    // WIP:: FusekiModServer.main

    // ActionStats: UI or Admin.
    //   "Read only server"
    // [ ] FMod_Shiro - dft localhost
    // [ ] fusekiCmd -> cmdLine
    // [ ] Finding the UI resources

    // -- Cmd
    // [ ] cmdLine.getUsage().startCategory("Fuseki") for
    // [ ] --file very long
    //     Handler multiple line descriptions?
    // [ ] Rename "CmdGeneral" as "CommandLine" or "CmdLine"

    // ==
    // "Workbench"
    // "Desktop development"
    //   Split admin  and stats
    // @args

    // ==== Phase one : Repackaging and no more.
    //   Admin by localhost
    //   OR shiro.

    // [ ] Reload

    // == Server
    // Sort out "FUSEKI_BASE", "FUSEKI_HOME" -- check javadoc
    //    ** "FUSEKI_HOME" commented out everywhere
    //    "FUSEKI_BASE"
    // Per server system lock. Put in server context? How to find the server from an action?
    //     Currently mgt.FusekiAdmin
    //  [ ] HttpAction::getContext

    // == FMod_UI
    // [ ] Pages from a jar file.
    // [ ] Setting for the location: --ui (--base is different?)  FUSEKI_HOME?

    // [ ] Setting fusekiModules when --modules.
    // [ ] FMod_UI - finding resources in a jar file. (current file directory, hard coded)
    // [ ] FMod_Admin - security
    // [ ] Two copies of ServerMgtConst

    // == FMod_Admin
    // Server basics => readonly - stats, ping(, metrics), GET datasets
    //   --readonly
    // Split into
    //   dataset add/delete
    //   data update
    //   stats, and ds-list

    // [ ] Separate admin general security but default admin to localhost unless Shiro.
    //     Pass in the modules or make accessible somehow.
    // [ ] Store configuration file before assembler additions.
    // Static resources
    // [ ] AdminAllowed:
    //     "--admin user=password" or "--admin localhost"
    //     "--admin-base run/"
    // ?? No FUSEKI_BASE?

    // Modules add command line args?

    // [ ] Startup - inject arguments ArgModuleAdmin.
    // Future - disk builds a configuration.
    // [ ] Localhost - put in ActionCtl.
    // [ ] Split "datasets" into read and write functions. Or test

    // == FMod_localhost.

    // == FMod_Shiro
    // [ ] --shiro=filename
    //     IF arg absent && run/shiro.ini exists ("$FUSEKI_BASE"/shiro.ini
    //
    // [ ] mod_shiro uses FUSEKI_SHIRO
    // [ ] --shiro (and no run/shiro.ini?)
    // [ ] SHIRO_FILE environment variable
    // [ ] (optional) Log access failures.
    // Shiro on/off
    // Config only version (no create datasets)
    // [ ] localhost

    static public class Counter2 {
        // Not for synchronization
        private LongAdder counter = new LongAdder();

        public Counter2()   {}

        public void inc()   { counter.increment(); }
        public void dec()   { counter.decrement(); }
        public long value() { return counter.longValue(); }

        @Override
        public String toString() {
            return counter.toString();
        }
    }
}
