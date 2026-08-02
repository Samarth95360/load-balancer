package http;

import http.io.HttpInputReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestParser {

    public HttpRequestData parse(InputStream inputStream) throws IOException{

        HttpInputReader reader = new HttpInputReader(inputStream);

        HttpRequestData request = parseRequestLine(reader);

        if (request == null) {
            return null;
        }

        parseHeaders(reader, request);

        parseBody(reader, request);

        return request;

    }

    private void parseBody(
            HttpInputReader reader,
            HttpRequestData request)
            throws IOException {

        List<String> values =
                request.getHeaders()
                        .get("Content-Length");

        if (values == null) {
            return;
        }

        int length =
                Integer.parseInt(values.get(0));

        request.setBody(
                reader.readBytes(length)
        );
    }

    private void parseHeaders(
            HttpInputReader reader,
            HttpRequestData request)
            throws IOException {

        String line;

        while ((line = reader.readLine()) != null) {

            if (line.isBlank()) {
                break;
            }

            int separator = line.indexOf(':');

            if (separator < 0) {
                continue;
            }

            String name =
                    line.substring(0, separator).trim();

            String value =
                    line.substring(separator + 1).trim();

            request.getHeaders()
                    .computeIfAbsent(
                            name,
                            k -> new ArrayList<>())
                    .add(value);
        }
    }

    private HttpRequestData parseRequestLine(
            HttpInputReader reader)
            throws IOException {

        String requestLine = reader.readLine();

        if (requestLine == null || requestLine.isBlank()) {
            return null;
        }

        String[] parts = requestLine.split(" ");

        if (parts.length != 3) {
            throw new IOException("Invalid HTTP Request Line");
        }

        HttpRequestData request =
                new HttpRequestData();

        request.setMethod(parts[0]);

        request.setPath(parts[1]);

        request.setVersion(parts[2]);

        return request;
    }
}
