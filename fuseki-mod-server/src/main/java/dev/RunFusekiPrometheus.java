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

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jetty.JettyConnectionMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import org.apache.jena.atlas.lib.FileOps;
import org.apache.jena.fuseki.Fuseki;
import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.main.sys.FusekiModules;
import org.apache.jena.fuseki.metrics.FusekiRequestsMetrics;
import org.apache.jena.fuseki.metrics.MetricsProviderRegistry;
import org.apache.jena.fuseki.metrics.SimpleMetricsProvider;
import org.apache.jena.fuseki.mod.admin.FMod_Admin;
import org.apache.jena.fuseki.mod.blank.FMod_BLANK;
import org.apache.jena.fuseki.mod.prometheus.FMod_Prometheus;
import org.apache.jena.fuseki.mod.shiro.FMod_Shiro;
import org.apache.jena.fuseki.mod.ui.FMod_UI;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.http.HttpOp;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.exec.http.GSP;
import org.apache.jena.sparql.exec.http.Params;

public class RunFusekiPrometheus {
    public static void main(String ... a) {
        try {
            run();
        } catch (Throwable th) {
            th.printStackTrace();
        } finally { System.exit(0); }
    }

    public static void run() {

        FileOps.clearDirectory("run");
        FusekiLogging.setLogging();
        Fuseki.init();

        // Prune system modules

        FusekiModules modulesAll = FusekiModules.create(new FMod_BLANK()
                                                        , new FMod_Prometheus(), new FMod_Shiro()
                                                        , new FMod_Admin(), new FMod_UI());

        FusekiModules modules = FusekiModules.create(new FMod_BLANK(), new FMod_Prometheus());

//        MetricsProviderRegistry.put(new SimpleMetricsProvider(), 10);
//        MeterRegistry meterRegistry = MetricsProviderRegistry.get().getMeterRegistry();
//        new JvmMemoryMetrics().bindTo(meterRegistry);
//        new JvmGcMetrics().bindTo(meterRegistry);
        /*
         * https://docs.micrometer.io/micrometer/reference/reference/jvm.html
         * new ClassLoaderMetrics().bindTo(registry);
         * new JvmMemoryMetrics().bindTo(registry);
         * new JvmGcMetrics().bindTo(registry);
         * new ProcessorMetrics().bindTo(registry);
         * new JvmThreadMetrics().bindTo(registry);
         */

        //FusekiModules.add(new FMod_Admin());
        DatasetGraph dsg = DatasetGraphFactory.createTxnMem();
        FusekiServer server = FusekiServer.create()
                .fusekiModules(modules)
                //.verbose(true)
                .add("/ds", dsg)
                .port(0)
                .build()
                .start();

        @SuppressWarnings("resource")
        JvmGcMetrics jvmGcMetrics = new JvmGcMetrics();

        if ( false ) {
//            CompositeMeterRegistry reg = new CompositeMeterRegistry();
//            reg.add(MetricsProviderRegistry.get().getMeterRegistry());
//            reg.add(new SimpleMetricsProvider().getMeterRegistry());

            // Reset
            MetricsProviderRegistry.set(new SimpleMetricsProvider());
            MeterRegistry meterRegistry = MetricsProviderRegistry.get().getMeterRegistry();
            // Register.
            server.getDataAccessPointRegistry().accessPoints().forEach(dap->new FusekiRequestsMetrics(dap).bindTo(meterRegistry));
            new JvmMemoryMetrics().bindTo(meterRegistry);
            jvmGcMetrics.bindTo(meterRegistry);
        }

        MeterRegistry meterRegistry = MetricsProviderRegistry.get().getMeterRegistry();
        JettyConnectionMetrics.addToAllConnectors(server.getJettyServer(), meterRegistry);


        String HOST = "localhost";
        GSP.service("http://"+HOST+":"+server.getHttpPort()+"/ds").defaultGraph().GET();

        //http://localhost:3030/$/datasets
        // Body: dbName=%2Fds&dbType=mem

        // Create!
        Params params = Params.create().add("dbName", "/ds2").add("dbType", "mem");
        String baseURL = "http://"+HOST+":"+server.getHttpPort();

        String x = HttpOp.httpPostRtnString(baseURL+"/$/metrics");
        //System.out.println(x);

        System.out.println("DONE");
    }
}
