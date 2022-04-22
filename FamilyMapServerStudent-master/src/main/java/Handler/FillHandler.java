package Handler;

import java.io.*;
import java.net.HttpURLConnection;

import DAO.DataAccessException;
import Result.FillResult;
import Service.FillService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.*;

public class FillHandler extends Handler {
    /**
     * Default constructor
     */
    public FillHandler() {
        super();
    }

    /**
     * Handle the Fill Web API
     * @param exchange http magic variable
     * @throws IOException if there is an error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                String urlPath = exchange.getRequestURI().toString();
                String username = "";
                int numGen = -1;
                if (urlPath.length() > 5) {
                    username = urlPath.substring(6);
                }
                if (username.contains("/")) {
                    int partPoint = username.indexOf("/");
                    String numGenString = username.substring(partPoint + 1);
                    username = username.substring(0, partPoint);
                    numGen = Integer.parseInt(numGenString);
                }
                FillService service = new FillService();
                FillResult result = service.fill(username, numGen);

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
