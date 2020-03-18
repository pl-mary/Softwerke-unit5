package news.utils.myHttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MyHttpRequest {

    private URL url;

    public MyHttpRequest(String link) throws MalformedURLException {
        url = new URL(link);
    }

    private String readResponse(InputStream is) throws IOException {
        String response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            while ((response = in.readLine()) != null) {
                content.append(response);
            }
            response = content.toString();
        }
        return response;
    }


    public String execute() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int timeout = 5000;
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);

        try {
            connection.connect();
            return readResponse(connection.getInputStream());
        } finally {
            connection.disconnect();
        }
    }
}
