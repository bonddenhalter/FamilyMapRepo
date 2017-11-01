package server.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import server.Facade;
import server.JsonEncoder;
import server.results.FillResult;

/**
 * Created by bondd on 10/30/2017.
 */

public class FillHandler implements HttpHandler
{
    Facade facade;

    public FillHandler()
    {
        facade = new Facade();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                // Start sending the HTTP response to the client, starting with
                // the status code and any defined headers.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                // get the url
                String url = exchange.getRequestURI().toString();
                //split the string on the "/"
                String[] urlParts = url.split("/");

                FillResult fillResult;
                if (urlParts.length == 3) //generations not specified
                {
                    String username = urlParts[2];
                    fillResult = facade.fill(username);
                }
                else if (urlParts.length == 4)  //generations specified
                {
                    int generations = Integer.parseInt(urlParts[3]);
                    String username = urlParts[2];
                    fillResult = facade.fill(username, generations);
                }
                else
                    throw new IOException(); //invalid url

                String respData = JsonEncoder.encodeObject(fillResult);

                // Get the response body output stream.
                OutputStream respBody = exchange.getResponseBody();
                // Write the JSON string to the output stream.
                writeString(respData, respBody);
                // Close the output stream.
                respBody.close();

                success = true;

            }

            if (!success) {
                // The HTTP request was invalid somehow, so we return a "bad request"
                // status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                // We are not sending a response body, so close the response body
                // output stream, indicating that the response is complete.
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();

            // Display/log the stack trace
            e.printStackTrace();
        }
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException
    {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
