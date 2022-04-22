package Handler;

import java.io.*;

import DAO.DataAccessException;
import Request.LoginRequest;
import Result.LoginResult;
import Service.LoginService;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import java.net.HttpURLConnection;

public class LoginHandler extends Handler {
    /**
     * Default constructor
     */
    public LoginHandler() {
        super();
    }

    /**
     * Handle the Login Web API
     * @param exchange http magic variable
     * @throws IOException if there is an error
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                LoginRequest request = (LoginRequest)gson.fromJson(reqData, LoginRequest.class);
                LoginService service = new LoginService();
                LoginResult result = service.login(request);

                if (result.isSuccess())
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                else
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
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
