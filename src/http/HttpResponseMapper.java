package http;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HttpResponseMapper {

    public HttpResponseData map(HttpResponse<byte[]> response){

        HttpResponseData data = new HttpResponseData();

        data.setStatus(HttpStatus.fromCode(response.statusCode()));

        data.setBody(response.body());

        response.headers().map().forEach((key,values) -> data.getHeaders().put(key,values));

        return data;

    }

}
