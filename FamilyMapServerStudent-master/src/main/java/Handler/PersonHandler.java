package Handler;

import java.io.*;
import java.net.HttpURLConnection;

import Result.PersonAllResult;
import Result.PersonResult;
import Service.PersonService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.*;

public class PersonHandler extends Handler {
    /**
     * Default constructor
     */
    public PersonHandler() {
        super();
    }

    /**
     * Handle the Person Web API
     *
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
                    String authToken = reqHeaders.getFirst("Authorization");
                    try {
                        String urlPath = exchange.getRequestURI().toString();
                        String personID = "";
                        boolean successful;
                        if (urlPath.length() >= 8) {
                            personID = urlPath.substring(8);
                        }
                        PersonService service = new PersonService();
                        String respData;
                        if (personID.length() > 0) {
                            PersonResult result = service.person(personID, authToken);
                            successful = result.isSuccess();
                            respData = gson.toJson(result);
                        } else {
                            PersonAllResult result = service.person(authToken);
                            successful = result.isSuccess();
                            respData = gson.toJson(result);
                        }

                        if (successful)
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        else
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                        OutputStream respBody = exchange.getResponseBody();
                        Handler.writeString(respData, respBody);
                        respBody.close();
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
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}