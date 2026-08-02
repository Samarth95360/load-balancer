package forword;

import java.util.Set;

public class RequestHeaderFilter {

    private static final Set<String> HOP_BY_HOP_HEADERS = Set.of(
            "connection",
            "keep-alive",
            "proxy-authenticate",
            "proxy-authorization",
            "te",
            "trailer",
            "transfer-encoding",
            "upgrade",
            "host"
    );

    private RequestHeaderFilter() {
    }

    public static boolean shouldForward(String headerName) {

        return !HOP_BY_HOP_HEADERS.contains(
                headerName.toLowerCase()
        );
    }

}
