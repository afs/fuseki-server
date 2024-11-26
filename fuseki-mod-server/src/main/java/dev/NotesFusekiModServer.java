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

    // WIP:: FusekiModServer.main

    // [ ] Put it all together
    // [ ] and test.

    // [ ] Enforce no mixed setup.

    // [x] Check vs Fuseki-full arguments.
    // [ ] Context in builder, pass to server. Read only context.
    //
    // [ ] Do we need a "context" in the args-builder phase?
    //     (servletAttr could serve this purpose - it is passed to the server.)
    //     Context. available to the running server.
    //       Check context in FusekiConfig - server attach context goes into global.
    //       Currently, no server Context - it uses global.

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

    // == FMod_Shiro
    // [ ] --shiro=filename
    // [ ] Log shiro.ini found
    // [x] Setup admin area before FMod_Shiro?.
    //     Set filter during prepare whenb admin setup.
    // [x] Error if no FUSEKI_SHIRO file.
    // [ ] FusekiServer.Builder.setShiroFile

    // == FMod_UI
    // [ ] Pages from a jar file.
    // [ ] Setting for the location: --ui (--base is different?)  FUSEKI_HOME?

    // [ ] Setting fusekiModules when --modules.
    // [ ] FMod_UI - finding resources in a jar file. (current file directory, hard coded)
    // [ ] FMod_Admin - security
    // [ ] Two copies of ServerMgtConst
}
