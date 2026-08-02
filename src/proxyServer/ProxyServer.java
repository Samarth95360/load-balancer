package proxyServer;
import ProxyHandler.ProxyHandler;
import config.ProxyConfiguration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProxyServer {

    private final int port;

    private final ProxyConfiguration configuration;

    public ProxyServer(int port){
        this.port = port;
        this.configuration = new ProxyConfiguration();
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
