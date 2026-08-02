package http;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpResponseData {

    private HttpStatus status;

    private final Map<String, List<String>> headers =
            new LinkedHashMap<>();

    private byte[] body;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
