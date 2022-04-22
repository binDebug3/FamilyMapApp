package Handler;

import java.io.*;
import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;
import java.nio.file.Files;


public class FileHandler extends Handler {
    /**
     * Default constructor
     */
    public FileHandler() {
        super();
    }

    /**
     * Handle the Default Web API
     * @param exchange http magic variable
     * @throws IOException if there is an error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String filePath;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String urlPath = exchange.getRequestURI().toString();
                if (urlPath.equals("/")) {
                    urlPath = "/index.html";
                }
                filePath = "web" + urlPath;
                File file = new File(filePath);
                if (file.exists()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                } else {
                    urlPath = "web/html/404.html";
                    file = new File(urlPath);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            exchange.getResponseBody().close();
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
