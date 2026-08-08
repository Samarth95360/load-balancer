package health;

import backend.BackendRegistry;
import backend.BackendServer;

public class HealthChecker {

    private final BackendRegistry registry;
    private final HealthCheckTask healthCheckTask;

    public HealthChecker(BackendRegistry registry, HealthCheckTask healthCheckTask) {
        this.registry = registry;
        this.healthCheckTask = healthCheckTask;
    }

    public void checkAll(){

        for(BackendServer backend : registry.getBackends()){
            HealthCheckResult result = healthCheckTask.check(backend);

            backend.setStatus(result.getStatus());
            backend.setLastHealthCheckTime(System.currentTimeMillis());
            backend.setLastResponseTime(result.getResponseTime());

            System.out.println(
                    "Health Check | "
                            + backend
                            + " | "
                            + result.getStatus()
                            + " | "
                            + result.getResponseTime()
                            + "ms"
            );

        }

    }

}
