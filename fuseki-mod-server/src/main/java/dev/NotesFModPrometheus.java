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

public class NotesFModPrometheus {
    /*
     * [x] Copy
     *   All of org.apache.jena.fuseki.metrics (micrometer.io, prometheus)
     *   ActionMetrics
     * **** Hard wire micrometer into Fuseki.
     * [ ] Running OTel and Prometheus in the same server, different FMods.
     *     FMod for each as a provider.
     *   Prometheus is pull-based and needs /$/metrics.
     *     This action metric should be "prometheus only"
     *     Rename as "prometheus"?
     * [ ] Observability
     * [ ] stats MeterRegistry.
     */

    /** FMod
     * [ ] Delete fuseki-core PrometheusMetricsProvider
     * [ ] FusekiServer.build()
     * [ ] Remove InitPrometheus - do in the FMod.
     * [ ] fuseki-webapp: FusekiServerListener
     * [ ] MetricsProviderRegistry - remove deprecate
     * [ ] Adding or deleting datasets - need a "call back" (preserving counts?)
     * [ ] Configuration reload (no preserving?
     */

    /* [ ] Counter name to counter mapping -> preserved, need GC.
     *     Find once on "DataServiceBuild" -> no -> new DataAccessPoint or DataService.noteDataAccessPoint
     *     Counters: DataService
     *     For now - don't preserve counter values across reload.
     *     Have "in-progress counters
     */

    /* [ ] Entry control.
     *
     */

    // Notes
    /*
     *   micrometer a provider abstraction (c.f. slf4j) and could be used jena-fuseki-core.
     *     Could pull out Gauge setup in FusekiRequestsMetrics
     *     FusekiMetricsProvider.make....
     *
     *   OTel
     *   micrometer-registry-otlp - OpenTelemetry protocol.
     *     *   MeterRegistry registry = new OtlpMeterRegistry(otlpConfig, Clock.SYSTEM);
     *   OTel is push based.
     *   Prometheus is pull-based and need /$/metrics. text format only
     *
     *   Observability
     *
     *   SimpleMeterRegistry() - local only
     *   CompositeMeterRegistry
     *
     *   Metrics.globalRegistry
     *   Lowercase+dots naming. database.calls Micrometer converts to provider conventions.
     *
     *   Micrometer counter only goes up.
     *   Guage is a value that goes up and down.
     */

    /*
     * Jetty 12
     * https://docs.micrometer.io/micrometer/reference/reference/jetty.html
     *   Part of Micrometer core.
     *
     * Server server = new Server(0);
     * NetworkTrafficServerConnector connector = new NetworkTrafficServerConnector(server);
     * JettyConnectionMetrics metrics = new JettyConnectionMetrics(registry, connector);
     * connector.addBean(metrics);
     * connector.setNetworkTrafficListener(metrics);
     * server.setConnectors(new Connector[] { connector });
     *
     * Register general connection metrics
     *   Registers metrics for bytes in/out on this connector
     * Alternatively, you can apply the metrics instrumentation to all connectors on a Server as follows:
     * JettyConnectionMetrics.addToAllConnectors(server, registry);
     */

    /*
     * https://docs.micrometer.io/micrometer/reference/reference/jvm.html
     * new ClassLoaderMetrics().bindTo(registry);
     * new JvmMemoryMetrics().bindTo(registry);
     * new JvmGcMetrics().bindTo(registry);
     * new ProcessorMetrics().bindTo(registry);
     * new JvmThreadMetrics().bindTo(registry);
     */

    /*
     * Java HTTP client:
     * <dependency>
     *   <groupId>io.micrometer</groupId>
     *   <artifactId>micrometer-java11</artifactId>
     * </dependency>
     *
     *   HttpClient observedClient = MicrometerHttpClient.instrumentationBuilder(httpClient, meterRegistry)
     *           .observationRegistry(observationRegistry)
     *           .build();
     */
}
