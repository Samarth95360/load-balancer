package health;

public class HealthCheckResult {

    private final HealthStatus status;

    private final long responseTime;

    public HealthCheckResult(HealthStatus status, long responseTime) {
        this.status = status;
        this.responseTime = responseTime;
    }

    public HealthStatus getStatus() {
        return status;
    }

    public long getResponseTime() {
        return responseTime;
    }
}
