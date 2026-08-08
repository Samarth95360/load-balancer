package health;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HealthCheckScheduler {

    private final ScheduledExecutorService scheduled;

    private final HealthChecker healthChecker;

    private final long intervalSeconds;

    public HealthCheckScheduler(HealthChecker healthChecker, long intervalSeconds) {
        this.scheduled = Executors.newSingleThreadScheduledExecutor();
        this.healthChecker = healthChecker;
        this.intervalSeconds = intervalSeconds;
    }

    public void start(){

        scheduled.scheduleAtFixedRate(
                healthChecker::checkAll,
                0,
                intervalSeconds,
                TimeUnit.SECONDS
        );

    }

    public void stop(){
        scheduled.shutdown();
    }

}
