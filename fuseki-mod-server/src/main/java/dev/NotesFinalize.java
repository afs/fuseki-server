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

public class NotesFinalize {
    /*
     * Look for XXX and TODO
     * Move command to org.apache.jena.fuseki.main.cmds
     * Artifact names; automatic module names.
     *   Currently: extra .fuseki.
     *     org.apache.jena.fuseki.fuseki-mod-server
     */

    /* Command line:
fuseki [--config=FILE|--mem|--loc=DIR|--file=FILE] [--port PORT] /DatasetPathName
  Fuseki Main

  --config=FILE          Use a configuration file to determine the services


** Section: Single Dataset

      --mem                  Create an in-memory, non-persistent dataset for the server
      --file=FILE            Create an in-memory, non-persistent dataset for the server, initialised with the contents of the file
      --tdb2                 Use TDB2 for command line persistent datasets
      --tdb1                 Use TDB1 for command line persistent datasets (default is TDB2)
      --loc=DIR              Use an existing TDB database (or create if does not exist)
      --memTDB               Create an in-memory, non-persistent dataset using TDB (testing only)
      --rdfs=FILE            Apply RDFS on top of the dataset
      --desc=                Assembler description file
==> Hide:
      --timeout=             Global timeout applied to queries (value in ms) -- format is X[,Y]
      --update               Allow updates (via SPARQL Update and SPARQL HTTP Update)

** Section: Full server
      --config=FILE          Use a configuration file to determine the services
      --admin
      --ui

** Section: Server
      --port                 Listen on this port number
      --localhost            Listen only on the localhost interface
      --gzip=on|off          Enable GZip compression (HTTP Accept-Encoding) if request header set
      --base=DIR             Directory for static content
      --contextPath=PATH     Context path for the server
      --auth=[basic|digest]   Run the server using basic or digest authentication
      --https=CONF           https certificate access details. JSON file { "cert":FILE , "passwd"; SECRET }
      --httpsPort=NUM        https port (default port is 3043)
      --passwd=FILE          Password file
      --jetty=FILE           jetty.xml server configuration
   Ensure alt name:
 --jetty-config=FILE    Set up the server (not services) with a Jetty XML file
      --cors=FILE            Configure CORS settings from file
      (remove now?)

      --no-cors              Disable CORS
      --modules=true|false   Enable Fuseki modules
       Reword description

** Section: Other
      --sparqler=DIR         Run with SPARQLer services Directory for static content
==> Reword
      --ping                 Enable /$/ping
      --stats                Enable /$/stats
      --metrics              Enable /$/metrics
      --compact              Enable /$/compact/*
      --validators           Install validators
      --general=PATH         Add a general SPARQL endpoint (without a dataset) at /PATH

  Symbol definition
      --set                  Set a configuration symbol to a value
  General
      -v   --verbose         Verbose
      -q   --quiet           Run with minimal output
      --debug                Output information for debugging
      --help
      --version              Version information
      --strict               Operate in strict SPARQL mode (no extensions of any kind)

Fuseki-full::

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
}
