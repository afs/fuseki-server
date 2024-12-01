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

    // [ ] Put it all together
    // [ ] and test.
    // [ ] NOTICE



    // *** Command line customizers must be build modules
    // **** FMod_Shiro

    // Passing setup:
    // [1] Builder.setServetAttibute? or Context passed through building (in builder).
    //     But args is before FusekiServer.Builder.
    // [2] Customizers must be build modules
    // [3] Check Shiro documentation
    //     https://github.com/apache/jena/issues/2617 -- No plain passwords.

    // [ ] jena-fuseki-server? jena-fuseki-fulljar?
    // [x] De-AutoModule
    // == FMod_Shiro : phase 1
    // [ ] Change logging setting to WARN on shiro
    // [ ] FusekiShiroFilter to take the decided shiro file as argument.
    // [ ] --shiro=filename // test
    // [ ] Log shiro.ini found
    // [-] FusekiServer.Builder.setShiroFile
    // [ ] Validate shiro.ini

    // [ ] shiro.ini delivery. localhost vs user/password.
    //     How can --admin work? Overwrite after localhost version?
    // [ ] test admin present (ping)
    // [ ] test shiro+localhost
    // [ ] test shiro+password
    // [ ] test UI returns something
    // [ ] test metrics returns something

    // [ ] FusekiLogging.setLogging(true)

    // [ ] Resources: => org.apache.jena.fuseki.server.admin.*

    //   First integration
    //     Mods to jena-fuseki-main
    //     New command to run Fuseki
    //     Assume right mod order
    // ToDo
    //  [ ] --admin : alternative to Shiro.
    //  [x] No FUSEKI_HOME, FUSEKI_BASE
    //  [x] UI assets [done]
    //  [?] Shiro setup. Done?
    //  [ ] FMod_Shiro.initShiro - test alt Shiro.

    // ==== Future
    // [ ] modes: Enforce no mixed setup.
    // [ ] FMod_Admin, --admin use/password.
    // [ ] Template Lucene

    // == Modes
    //   if admin, config.ttl to admin.
    // Modes:
    // [ ] Plain/bare : no admin, no ui
    // [ ] config.ttl
    // [ ] Admin, start empty
    // Later - go with current chaos.

    // == Phase 2:
    // [ ] Context in builder, pass to server. Read only context.
    //
    // [ ] Do we need a "context" in the args-builder phase?
    //     (servletAttr could serve this purpose - it is passed to the server.)
    //     Context. available to the running server.
    //       Check context in FusekiConfig - server attach context goes into global.
    //       Currently, no server Context - it uses global.

    // == Phase 1
    // [ ] UI asset management.
    // [ ] revisit/revise copy of ContextAccumulator
    //     ExecUpdateHTTPBuilder, RDFParserBuilder, others need to pass in a base that is a copy.
    //     base always exists - copy on create not supplier/delayed.
    //     OR baseContext is never updated. Check.

    // [ ] FMod admin sets FUSEKI_SHIRO
    // [ ] FMod admin args

    // ==== Phase one : Repackaging and no more.
    // [ ] Reload

    // == Server
    // Sort out "FUSEKI_BASE", "FUSEKI_HOME" -- check javadoc
    //    ** "FUSEKI_HOME" commented out everywhere
    //    "FUSEKI_BASE"
    // Per server system lock. Put in server context? How to find the server from an action?
    //     Currently mgt.FusekiAdmin
    //  [ ] HttpAction::getContext

    // == FMod_Admin
    // [ ] See allowStartEmpty
    //
    // = Phase 1
    // [ ] Indicate in-use. Set FUSEKI_SHIRO?
    // [-] Store configuration file before assembler additions.
    // Static resources
    // [ ] AdminAllowed:
    //     "--admin user=password" or "--admin localhost"
    //     "--admin-base run/"
    //
    // = Phase 2
    // Server basics => readonly - stats, ping(, metrics), GET datasets
    //   --readonly
    // Split into
    //   dataset add/delete
    //   data update
    //   stats, and ds-list

    // Admin: localhost on certain URLs
    // /$/status  = anon
    // /$/server  = anon
    // /$/ping    = anon
    // /$/metrics = anon
    //   Does this override /$/*? in a filter?

    // == FMod_localhost.
    // [ ] localhost

    // == FMod_UI
    // [ ] Pages from a jar file.
    // [ ] Setting for the location: --ui (--base is different?)  FUSEKI_HOME?

    // [ ] Setting fusekiModules when --modules.
    // [ ] FMod_UI - finding resources in a jar file. (current file directory, hard coded)
    // [ ] FMod_Admin - security
    // [ ] Two copies of ServerMgtConst
}
