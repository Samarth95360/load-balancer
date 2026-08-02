package http.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HttpInputReader {

    private final InputStream inputStream;

    public HttpInputReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String readLine() throws IOException{

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int current;

        while ((current = inputStream.read()) != -1) {

            if (current == '\r') {

                int next = inputStream.read();

                if (next == '\n') {
                    break;
                }

                buffer.write(current);

                if (next != -1) {
                    buffer.write(next);
                }

            } else {

                buffer.write(current);

            }
        }
        if (current == -1 && buffer.size() == 0) {
            return null;
        }

        return buffer.toString(StandardCharsets.UTF_8);

    }

    public byte[] readBytes(int length)
            throws IOException {

        byte[] data = new byte[length];

        int totalRead = 0;

        while (totalRead < length) {

            int read = inputStream.read(
                    data,
                    totalRead,
                    length - totalRead
            );

            if (read == -1) {
                throw new IOException(
                        "Unexpected end of stream."
                );
            }

            totalRead += read;
        }

        return data;
    }

}
