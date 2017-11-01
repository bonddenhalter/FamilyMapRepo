package server;

import com.sun.net.httpserver.HttpServer;
import java.net.*;
import java.io.*;

import server.handlers.ClearHandler;
import server.handlers.DefaultHandler;
import server.handlers.EventHandler;
import server.handlers.FillHandler;
import server.handlers.LoadHandler;
import server.handlers.LoginHandler;
import server.handlers.PersonHandler;
import server.handlers.RegisterHandler;


/**
 * The main server class
 */

public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    // This method initializes and runs the server.
    // The "portNumber" parameter specifies the port number on which the
    // server should accept incoming client connections.
    private void run(String portNumber)
    {
        System.out.println("Initializing HTTP Server");
        try {
            // Create a new HttpServer object.
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Indicate that we are using the default "executor".
        server.setExecutor(null);

        //create the handlers
        System.out.println("Creating contexts");
        server.createContext("/", new DefaultHandler()); //create the default handler
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());

        //start the server
        System.out.println("Starting server");
        // Tells the HttpServer to start accepting incoming client connections.
        // This method call will return immediately, and the "main" method
        // for the program will also complete.
        // Even though the "main" method has completed, the program will continue
        // running because the HttpServer object we created is still running
        // in the background.
        server.start();
        System.out.println("Server started");

    }



    public static void main(String[] args)
    {
        server_init();

        String portNumber = args[0];
        new Server().run(portNumber);
//        EventResult eventResult = new EventResult(new Event(null, null, null, null, null, null, null, null, null));
//        eventResult.setMessage("error");
//        System.out.println(JsonEncoder.encodeObject(eventResult));
    }

    private static void server_init()
    {
        RandomNames randomNames = RandomNames.getInstance(); //creates class and loads lists from JSON

        Facade facade = new Facade();
        facade.initDatabase(); //create the database tables
    }


}
