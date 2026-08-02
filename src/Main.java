import config.ConfigurationLoader;
import config.ConfigurationValidator;
import config.ProxyPropertyConfig;
import proxyServer.ProxyServer;

public class Main {
    public static void main(String[] args) {

        ProxyServer server = new ProxyServer();

        server.start();

    }
}