package backend;

import health.HealthStatus;

public class BackendServer {

    private final String host;
    private final int port;

    private volatile HealthStatus status = HealthStatus.UNKNOWN;

    private volatile long lastHealthCheckTime;

    private volatile long lastResponseTime;

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

    public HealthStatus getStatus() {
        return status;
    }

    public void setStatus(HealthStatus status) {
        this.status = status;
    }

    public long getLastHealthCheckTime() {
        return lastHealthCheckTime;
    }

    public void setLastHealthCheckTime(
            long lastHealthCheckTime) {

        this.lastHealthCheckTime =
                lastHealthCheckTime;
    }

    public long getLastResponseTime() {
        return lastResponseTime;
    }

    public void setLastResponseTime(
            long lastResponseTime) {

        this.lastResponseTime =
                lastResponseTime;
    }

    public boolean isHealthy() {

        return status ==
                HealthStatus.HEALTHY;
    }

    @Override
    public String toString() {
        return getBaseUrl();
    }

}
