package selector;

import backend.BackendServer;

public class SingleBackendSelector implements BackendSelector{

    private final BackendServer backendServer;

    public SingleBackendSelector(BackendServer backendServer) {
        this.backendServer = backendServer;
    }

    @Override
    public BackendServer selectBackend() {
        return backendServer;
    }
}
