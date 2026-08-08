package health;

import backend.BackendServer;
import http.HttpResponseData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HealthCheckTask {

    private final HttpClient httpClient;

    private final Duration timeOut;

    public HealthCheckTask(HttpClient httpClient, Duration timeOut) {
        this.httpClient = httpClient;
        this.timeOut = timeOut;
    }

    public HealthCheckResult check(BackendServer server){

        long startTime = System.nanoTime();

        try{

            String healthUrl = server.getBaseUrl()+"/actuator/health";

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(healthUrl))
                    .GET()
                    .timeout(timeOut)
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,HttpResponse.BodyHandlers.ofString()
            );

            long responseTime = (System.nanoTime() - startTime) / 1_000_000;

            if(response.statusCode() != 200){
                return new HealthCheckResult(HealthStatus.UNKNOWN,responseTime);
            }

            String body = response.body();

            if (!body.contains("\"status\":\"UP\"")
                    && !body.contains("\"status\": \"UP\"")) {

                return new HealthCheckResult(
                        HealthStatus.UNHEALTHY,
                        responseTime
                );
            }

            return new HealthCheckResult(HealthStatus.HEALTHY,responseTime);


        }catch (IOException | InterruptedException e){

            if(e instanceof InterruptedException){
                Thread.currentThread().interrupt();
            }

            long responseTime = (System.nanoTime() - startTime) / 1_000_000;

            return new HealthCheckResult(HealthStatus.UNHEALTHY,responseTime);

        }

    }

}
