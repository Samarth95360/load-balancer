package selector;

import backend.BackendServer;

public interface BackendSelector {

    BackendServer selectBackend();

}
