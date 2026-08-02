package forword;

import http.HttpRequestData;

import java.net.http.HttpRequest;

public class RequestBodyPublisherFactory {

    public HttpRequest.BodyPublisher create(HttpRequestData request) {

        byte[] body = request.getBody();

        if (body == null || body.length == 0) {
            return HttpRequest.BodyPublishers.noBody();
        }

        return HttpRequest.BodyPublishers.ofByteArray(body);
    }
}