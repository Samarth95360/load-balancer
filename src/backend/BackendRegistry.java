package backend;

import config.BackendConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackendRegistry {

    private final List<BackendServer> backends;

    public BackendRegistry(List<BackendConfiguration> configurations){

        List<BackendServer> servers = new ArrayList<>();

        for(BackendConfiguration config : configurations){
            servers.add(new BackendServer(config.getHost(), config.getPort()));
        }

        this.backends = Collections.unmodifiableList(servers);

    }

    public List<BackendServer> getBackends(){
        return this.backends;
    }

}
