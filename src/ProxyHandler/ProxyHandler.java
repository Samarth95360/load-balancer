package ProxyHandler;

import backend.BackendServer;
import config.ProxyConfiguration;
import forword.RequestForwarder;
import http.HttpRequestData;
import http.HttpRequestParser;
import http.HttpResponseData;
import http.HttpResponseMapper;
import response.HttpResponseWriter;

import java.io.IOException;
import java.net.Socket;
import java.net.http.HttpResponse;

public class ProxyHandler {

    private final Socket clientSocket;

    private final ProxyConfiguration configuration;

    public ProxyHandler(Socket clientSocket, ProxyConfiguration configuration){
        this.clientSocket = clientSocket;
        this.configuration = configuration;
    }

    public void handle(){

        try {

//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            HttpRequestParser parser = configuration.getRequestParser();

            HttpRequestData request = parser.parse(clientSocket.getInputStream());

            BackendServer server = configuration.getBackendSelector().selectBackend();

            RequestForwarder forwarder = configuration.getRequestForwarder();

            HttpResponse<byte[]> backendResponse = forwarder.forward(request,server);

            HttpResponseMapper mapper = configuration.getResponseMapper();

            HttpResponseData response = mapper.map(backendResponse);

            HttpResponseWriter writer = configuration.getResponseWriter();

            writer.write(clientSocket.getOutputStream(),response);

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }

    }

}
