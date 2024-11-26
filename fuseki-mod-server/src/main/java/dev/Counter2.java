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

public class Counter2 {
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
    /*
fuseki [--config=FILE] [--mem|--loc=TDBdatabase|--file=FILE] [--port PORT] /DatasetPathName
  Fuseki
      --desc=                Assembler description file
      --mem                  Create an in-memory, non-persistent dataset for the server
      --file=FILE            Create an in-memory, non-persistent dataset for the server, initialised with the contents of the file
      --tdb1                 Use TDB1 for command line persistent datasets (default is TDB2)
      --tdb2                 Use TDB2 for command line persistent datasets (default is TDB2)
      --loc=DIR              Use an existing TDB database (or create if does not exist)
      --memTDB               Create an in-memory, non-persistent dataset using TDB (testing only)
      --rdfs=FILE            Apply RDFS on top of the dataset
      --port                 Listen on this port number
      --localhost            Listen only on the localhost interface
      --timeout=             Global timeout applied to queries (value in ms) -- format is X[,Y]
      --update               Allow updates (via SPARQL Update and SPARQL HTTP Update)
      --contextPath=PATH     Set up the server context (root) path
      --config=              Use a configuration file to determine the services
      --jetty-config=FILE    Set up the server (not services) with a Jetty XML file
      --gzip=on|off          Enable GZip compression (HTTP Accept-Encoding) if request header set
  Symbol definition
      --set                  Set a configuration symbol to a value
  General
      -v   --verbose         Verbose
      -q   --quiet           Run with minimal output
      --debug                Output information for debugging
      --help
      --version              Version information
      --strict               Operate in strict SPARQL mode (no extensions of any kind)

     */