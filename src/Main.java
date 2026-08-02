import proxyServer.ProxyServer;

public class Main {
    public static void main(String[] args) {

        ProxyServer server = new ProxyServer(8080);

        server.start();

    }
}