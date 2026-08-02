package forword;

import backend.BackendServer;
import config.ProxyPropertyConfig;
import http.HttpRequestData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class RequestForwarder {

    private final HttpClient httpClient;
    private final RequestBodyPublisherFactory publisherFactory;

    public RequestForwarder(ProxyPropertyConfig config) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(config.getConnectTimeout()))
                .followRedirects(HttpClient.Redirect.NEVER)
                .version(HttpClient.Version.HTTP_1_1).build();

        this.publisherFactory = new RequestBodyPublisherFactory();
    }

    public HttpResponse<byte[]> forward(HttpRequestData requestData,BackendServer backend) throws IOException, InterruptedException {

        String url = backend.getBaseUrl()+requestData.getPath();

        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url));

        builder.method(requestData.getMethod(), publisherFactory.create(requestData));

        copyHeaders(requestData, builder);

        HttpRequest request = builder.build();

        return httpClient.send(request,HttpResponse.BodyHandlers.ofByteArray());

    }

    private void copyHeaders(
            HttpRequestData request,
            HttpRequest.Builder builder) {

        request.getHeaders().forEach((name, values) -> {

            if (!RequestHeaderFilter.shouldForward(name)) {
                return;
            }

            for (String value : values) {
                builder.header(name, value);
            }

        });

    }

}
