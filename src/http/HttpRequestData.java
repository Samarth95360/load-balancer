package http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestData {

    private String method;
    private String path;
    private String version;
    private final Map<String, List<String>> headers = new HashMap<>();
    private byte[] body;

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

}
