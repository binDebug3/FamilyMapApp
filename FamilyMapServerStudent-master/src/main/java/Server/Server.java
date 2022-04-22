package Server;

import java.io.*;
import java.net.*;

import Handler.*;
import com.sun.net.httpserver.*;

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    /**
     * Starts the server and calls the corresponding handler for each context
     * @param portNumber the portnumber from the configurations
     */
    private void run(String portNumber) {
        System.out.println("Initializing HTTP Server");
        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);

        System.out.println("Creating contexts");
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/", new FileHandler());


        System.out.println("Starting server");
        server.start();
        System.out.println("Server started");
    }

    // "main" method for the server program
    // "args" should contain one command-line argument, which is the port number
    // on which the server should accept incoming client connections.
    /**
     * Main class runs the Web APIs in the background
     * @param args port number
     * @throws Exception if there is a problem
     */
    public static void main(String[] args) throws Exception {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}

