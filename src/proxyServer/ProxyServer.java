package proxyServer;
import ProxyHandler.ProxyHandler;
import config.ConfigurationLoader;
import config.ConfigurationValidator;
import config.ProxyConfiguration;
import config.ProxyPropertyConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer {

    private final int port;

    private final ProxyConfiguration configuration;

    public ProxyServer(){
        ConfigurationLoader loader = new ConfigurationLoader();

        ProxyPropertyConfig property = loader.load();

        new ConfigurationValidator().validate(property);
        this.configuration = new ProxyConfiguration(property);
        this.configuration.startHealthCheck();

        this.port = property.getProxyPort();
    }

    public void start(){

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Reverse Proxy Started on Port " + port);

            while (true) {

                Socket clientSocket = serverSocket.accept();

                Thread.startVirtualThread(() -> handleClient(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleClient(Socket clientSocket) {

        try {

            new ProxyHandler(clientSocket,configuration).handle();

        } catch (Exception e) {

            System.err.println("Failed to handle client: "
                    + e.getMessage());

            e.printStackTrace();

        } finally {

            try {
                clientSocket.close();
            } catch (IOException ignored) {
            }

        }
    }

}
