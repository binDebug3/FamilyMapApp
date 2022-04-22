package Handler;

import java.io.*;
import java.net.HttpURLConnection;

import Result.EventAllResult;
import Result.EventResult;
import Service.EventService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.*;

public class EventHandler extends Handler {
    /**
     * Default constructor
     */
    public EventHandler() {
        super();
    }

    /**
     * Handle the Event Web API
     * @param exchange http magic variable
     * @throws IOException if there is an error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authtoken = reqHeaders.getFirst("Authorization");
                    try {
                        String urlPath = exchange.getRequestURI().toString();
                        EventService service = new EventService();
                        EventResult singleResult = null;
                        EventAllResult multResult = null;
                        boolean successful;

                        if (urlPath.length() > 6) {
                            String eventID = urlPath.substring(7);
                            singleResult = service.event(eventID, authtoken);
                            successful = singleResult.isSuccess();
                        } else {
                            multResult = service.event(authtoken);
                            successful = multResult.isSuccess();
                        }

                        if (successful) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }

                        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                        if (urlPath.length() > 6) {
                            gson.toJson(singleResult, resBody);
                        } else {
                            gson.toJson(multResult, resBody);
                        }
                        resBody.close();
                        success = true;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
