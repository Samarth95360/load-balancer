package backend;

public class BackendServer {

    private final String host;
    private final int port;

    private volatile boolean healthy = true;

    public BackendServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getBaseUrl() {
        return "http://" + host + ":" + port;
    }

    @Override
    public String toString() {
        return getBaseUrl();
    }

}
