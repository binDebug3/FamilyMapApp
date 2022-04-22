package Handler;

import java.io.*;
import java.net.HttpURLConnection;

import DAO.DataAccessException;
import Result.ClearResult;
import Service.ClearService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.*;

public class ClearHandler extends Handler {
    /**
     * Default constructor
     */
    public ClearHandler() {
        super();
    }

    /**
     * Handle the Clear Web API
     * @param exchange http magic variable
     * @throws IOException if there is an error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                ClearService service = new ClearService();
                ClearResult result = service.clear();

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(result, resBody);
                resBody.close();
                success = true;
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
