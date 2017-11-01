package server.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import server.Facade;
import server.JsonEncoder;
import server.results.EventResult;
import server.results.EventsResult;

/**
 * Created by bondd on 10/30/2017.
 */

public class EventHandler implements HttpHandler
{

    Facade facade;

    public EventHandler() {
        facade = new Facade();
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("entered event handler");
        boolean success = false;

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow GET requests for this operation.
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization"))
                {

                    // Extract the auth token from the "Authorization" header
                    String authToken = reqHeaders.getFirst("Authorization");
                    System.out.println("Token is: " + authToken);

                    // get the url
                    String url = exchange.getRequestURI().toString();
                    //split the string on the "/"
                    String[] urlParts = url.split("/");
                    //look at number of elements in array to determine service
                    boolean error = true;
                    String respData;
                    if (urlParts.length == 3) //it passed an eventID so called event service
                    {
                        String eventID = urlParts[2];
                        // This is the JSON data we will return in the HTTP response body
                        EventResult eventResult = facade.event(eventID, authToken);
                        respData = JsonEncoder.encodeObject(eventResult);
                        error = (eventResult.getMessage() != null); //error if the message is not null
                    }
                    else if (urlParts.length == 2)//events service
                    {
                        EventsResult eventsResult = facade.events(authToken);
                        respData = JsonEncoder.encodeObject(eventsResult);
                        error = (eventsResult.getMessage() != null); //error if the message is not null
                    }
                    else
                        throw new IOException();

                    if (!error) //if there was no error
                    {
                        // Start sending the HTTP response to the client, starting with
                        // the status code and any defined headers.
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                        // Get the response body output stream.
                        OutputStream respBody = exchange.getResponseBody();
                        // Write the JSON string to the output stream.
                        writeString(respData, respBody);
                        // Close the output stream.
                        respBody.close();

                        success = true;
                    }
                    else
                    {
                        // Start sending the HTTP response to the client, starting with
                        // the status code and any defined headers.
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);

                        // Get the response body output stream.
                        OutputStream respBody = exchange.getResponseBody();
                        // Write the JSON string to the output stream.
                        writeString(respData, respBody);
                        // Close the output stream.
                        respBody.close();
                    }

                }
            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // Since the client request was invalid, they will not receive the
                // list of games, so we close the response body output stream,
                // indicating that the response is complete.
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            // Since the server is unable to complete the request, the client will
            // not receive the list of games, so we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
