package config;

import backend.BackendRegistry;
import backend.BackendServer;
import forword.RequestForwarder;
import health.HealthCheckScheduler;
import health.HealthCheckTask;
import health.HealthChecker;
import http.HttpRequestParser;
import http.HttpResponseMapper;
import response.HttpResponseWriter;
import selector.BackendSelector;
import selector.RoundRobinSelector;
import selector.SingleBackendSelector;

import java.net.http.HttpClient;
import java.time.Duration;

public class ProxyConfiguration {

    private final HttpRequestParser requestParser;

    private final RequestForwarder requestForwarder;

    private final HttpResponseWriter responseWriter;

    private final BackendSelector backendSelector;

    private final HttpResponseMapper responseMapper;

    private final HttpClient httpClient;

    private final BackendRegistry registry;

    private final HealthCheckTask healthCheckTask;

    private final HealthChecker healthChecker;

    private final HealthCheckScheduler healthCheckScheduler;


    public ProxyConfiguration(ProxyPropertyConfig config) {

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(config.getConnectTimeout()))
                .followRedirects(HttpClient.Redirect.NEVER)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        this.requestParser = new HttpRequestParser();
        this.requestForwarder = new RequestForwarder(httpClient);
        this.responseMapper = new HttpResponseMapper();
        this.responseWriter = new HttpResponseWriter();

        this.registry = new BackendRegistry(config.getBackends());
        this.backendSelector = new RoundRobinSelector(registry);

        this.healthCheckTask = new HealthCheckTask(httpClient,Duration.ofSeconds(config.getHealthTimeout()));
        this.healthChecker = new HealthChecker(registry,healthCheckTask);

        this.healthCheckScheduler = new HealthCheckScheduler(healthChecker,config.getHealthInterval());
    }

    public void startHealthCheck(){
        this.healthCheckScheduler.start();
    }

    public HttpResponseMapper getResponseMapper() {
        return responseMapper;
    }

    public HttpRequestParser getRequestParser() {
        return requestParser;
    }

    public RequestForwarder getRequestForwarder() {
        return requestForwarder;
    }

    public HttpResponseWriter getResponseWriter() {
        return responseWriter;
    }

    public BackendSelector getBackendSelector() {
        return backendSelector;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public BackendRegistry getRegistry() {
        return registry;
    }

    public HealthCheckTask getHealthCheckTask() {
        return healthCheckTask;
    }

    public HealthChecker getHealthChecker() {
        return healthChecker;
    }

    public HealthCheckScheduler getHealthCheckScheduler() {
        return healthCheckScheduler;
    }
}
