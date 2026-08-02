package config;

import backend.BackendServer;

import java.util.ArrayList;
import java.util.List;

public class ProxyPropertyConfig {

    private int proxyPort;

    private int connectTimeout;

    private int requestTimeout;

    private int healthInterval;

    private int healthTimeout;

    private final List<BackendServer> backends =
            new ArrayList<>();

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getHealthInterval() {
        return healthInterval;
    }

    public void setHealthInterval(int healthInterval) {
        this.healthInterval = healthInterval;
    }

    public int getHealthTimeout() {
        return healthTimeout;
    }

    public void setHealthTimeout(int healthTimeout) {
        this.healthTimeout = healthTimeout;
    }

    public List<BackendServer> getBackends() {
        return backends;
    }

}
