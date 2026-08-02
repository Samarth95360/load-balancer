package http;

public enum HttpStatus {

    OK(200, "OK"),

    CREATED(201, "Created"),

    NO_CONTENT(204, "No Content"),

    MOVED_PERMANENTLY(301, "Moved Permanently"),

    FOUND(302, "Found"),

    BAD_REQUEST(400, "Bad Request"),

    UNAUTHORIZED(401, "Unauthorized"),

    FORBIDDEN(403, "Forbidden"),

    NOT_FOUND(404, "Not Found"),

    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    REQUEST_TIMEOUT(408, "Request Timeout"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    BAD_GATEWAY(502, "Bad Gateway"),

    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    GATEWAY_TIMEOUT(504, "Gateway Timeout");

    private final int code;

    private final String reasonPhrase;

    HttpStatus(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public int getCode() {
        return code;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public static HttpStatus fromCode(int code) {

        for (HttpStatus status : values()) {

            if (status.code == code) {
                return status;
            }

        }

        throw new IllegalArgumentException(
                "Unknown HTTP Status Code : " + code);
    }

}