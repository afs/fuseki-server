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

    // [ ] FusekiModServer has special zero args!

    // INTEGRATION
    //     Mods to jena-fuseki-main
    //     New command to run Fuseki

    // ==== Phase 1
    // [x] Store configuration file before assembler additions.
    // [ ] FMod_UI: Setting for the location: --ui (--base is different?)  FUSEKI_HOME?
    // [ ] jena-fuseki-server? jena-fuseki-fulljar?

    // [x] test admin - present (ping)
    // [ ] Test ModAdmin for localhost

    // [x] test shiro+localhost
    // [x] test shiro+password
    // [ ] test UI returns something
    // [x] test metrics returns something
    // [ ] TestFusekiServer : integration tests
    // [ ] Update NOTICE

    // [ ] Reload?

    // ==== Passing setup:
    // [1] Builder.setServetAttibute? or Context passed through building (in builder).
    //     But arg processing is before FusekiServer.Builder.
    // [2] Customizers must be build modules
    // *** Command line customizers must be build modules

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
