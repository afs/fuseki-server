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

public class NotesFusekiModServer {

    // [ ] run/configuration still has the junk in it.
    // [ ] Shaded jar not working - UI files return blank
    // [ ] Packages shiro.ini file
    //     Has /ds=authcBasic,user[user1] !!!!!!!!!!!

    // *** TestAdmin is all mods - change to FMod_dmin only.
    // [ ] some tests --> TestFusekiServer
    // [ ] Admin + user:password.

    // [x] Checked. jena=-fuseki-webapp:

    // [x] TestAdmin: ?state=offline
    //     Does offlining exist?

    // [ ] Configuration file storage.

    // *** Write tests.
    // Kill TestModShiro - local host only.
    // Test ModAdmin - inc localhost
    // FusekiTestLib.expect403 (copy?temp)

    // [ ] Put it all together
    // [ ] and test.
    // [ ] NOTICE

    // server.stop to call FMods.stop()

    // *** Command line customizers must be build modules

    // Passing setup:
    // [1] Builder.setServetAttibute? or Context passed through building (in builder).
    //     But arg processing is before FusekiServer.Builder.
    // [2] Customizers must be build modules

    // ==== Phase 1
    // [-] Store configuration file before assembler additions.
    // [ ] FMod_UI: Setting for the location: --ui (--base is different?)  FUSEKI_HOME?
    // [ ] jena-fuseki-server? jena-fuseki-fulljar?

    // [ ] test admin - present (ping)
    // [x] test shiro+localhost
    // [x] test shiro+password
    // [ ] test UI returns something
    // [ ] test metrics returns something

    //   First integration
    //     Mods to jena-fuseki-main
    //     New command to run Fuseki
    //     Assume right mod order
    // [ ] Reload?

    // ==== Phase 2
    // == FMod_localhost.
    // [ ] localhost
    // [ ] modes: Enforce no mixed setup.
    // Modes:
    // [ ] Plain/bare : no admin, no ui
    // [ ] config.ttl
    // [ ] Admin, start empty
    // Later - go with current chaos.
    // [ ] Take metrics output jena-fuseki-main?
    // org.apache.jena.fuseki.main



    // [ ] Template Lucene etc

    // [ ] Check Shiro documentation
    //     https://github.com/apache/jena/issues/2617 -- No plain passwords.
    //     Annotation style? To get POST and not GET

    // --------

    // Per server system lock. Put in server context? How to find the server from an action?
    //     Currently mgt.FusekiAdmin
    //  [ ] HttpAction::getContext
}
