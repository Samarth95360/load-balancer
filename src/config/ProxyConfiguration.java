package config;

import backend.BackendServer;
import forword.RequestForwarder;
import http.HttpRequestParser;
import http.HttpResponseMapper;
import response.HttpResponseWriter;
import selector.BackendSelector;
import selector.SingleBackendSelector;

public class ProxyConfiguration {

    private final HttpRequestParser requestParser;

    private final RequestForwarder requestForwarder;

    private final HttpResponseWriter responseWriter;

    private final BackendSelector backendSelector;

    private final HttpResponseMapper responseMapper;

    public ProxyConfiguration(ProxyPropertyConfig config) {

        this.requestParser = new HttpRequestParser();
        this.requestForwarder = new RequestForwarder(config);
        this.responseMapper = new HttpResponseMapper();
        this.responseWriter = new HttpResponseWriter();

        BackendConfiguration backend = config.getBackends().getFirst();

        this.backendSelector = new SingleBackendSelector(new BackendServer(backend.getHost(), backend.getPort()));
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
}
