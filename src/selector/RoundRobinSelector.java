package selector;

import backend.BackendRegistry;
import backend.BackendServer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinSelector implements BackendSelector{

    private final BackendRegistry registry;

    private final AtomicInteger counter = new AtomicInteger();

    public RoundRobinSelector(BackendRegistry registry){
        this.registry = registry;
    }

    @Override
    public BackendServer selectBackend() {

        List<BackendServer> backends = this.registry.getBackends();

        if(backends.isEmpty()){
            throw new IllegalStateException(
                    "No backend configured."
            );
        }

        int index = Math.floorMod(
                counter.getAndIncrement(),
                backends.size()
        );

        return backends.get(index);

    }
}
