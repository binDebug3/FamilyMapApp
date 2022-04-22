package network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;
import Request.*;
import Result.EventAllResult;
import Result.LoginResult;
import Result.PersonAllResult;
import Result.RegisterResult;

public class ServerProxy {

    public static RegisterResult postRegisterUser(String serverHost, String serverPort, RegisterRequest request) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);    // There is a request body
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Register successful.");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                return gson.fromJson(respData, RegisterResult.class);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                System.out.println(respData);
                return gson.fromJson(respData, RegisterResult.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LoginResult postUserLogin(String serverHost, String serverPort, LoginRequest request) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);    // There is a request body
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Login successful.");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                return gson.fromJson(respData, LoginResult.class);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                System.out.println(respData);
                return gson.fromJson(respData, LoginResult.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void postClear(String serverHost, String serverPort) {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/clear");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);    // There is a request body
            http.addRequestProperty("Authorization", "afj232hj2332");
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            String reqData = "";
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Clear successful.");
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                System.out.println(respData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PersonAllResult getPersons(String serverHost, String serverPort, String personID, String authToken) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);    // There is a request body
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                //System.out.println(respData);
                PersonAllResult result = gson.fromJson(respData, PersonAllResult.class);
                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                System.out.println(respData);
                return gson.fromJson(respData, PersonAllResult.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static EventAllResult getEvents(String serverHost, String serverPort, String authToken) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);    // There is a request body
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                //System.out.println(respData);
                return gson.fromJson(respData, EventAllResult.class);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream respBody = http.getErrorStream();
                String respData = readString(respBody);
                System.out.println(respData);
                return gson.fromJson(respData, EventAllResult.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
