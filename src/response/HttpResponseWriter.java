package response;

import http.HttpResponseData;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpResponseWriter {

    public void write(OutputStream outputStream, HttpResponseData responseData) throws IOException {

        StringBuilder headers = new StringBuilder();

        headers.append("HTTP/1.1 ")
                .append(responseData.getStatus().getCode())
                .append(" ")
                .append(responseData.getStatus().getReasonPhrase())
                .append("\r\n");

//        headers.append("HTTP/1.1 ")
//                .append(responseData.getStatus().getCode())
//                .append(" OK\r\n");

        for (Map.Entry<String, List<String>> header :
                responseData.getHeaders().entrySet()) {

            for (String value : header.getValue()) {

                headers.append(header.getKey())
                        .append(": ")
                        .append(value)
                        .append("\r\n");
            }
        }

        headers.append("\r\n");

        outputStream.write(
                headers.toString().getBytes(StandardCharsets.UTF_8));

        if (responseData.getBody() != null) {

            outputStream.write(responseData.getBody());

        }

        outputStream.flush();

    }

}
