package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import server.Facade;
import server.JsonEncoder;
import server.results.ClearResult;

/**
 * Created by bondd on 10/30/2017.
 */

public class ClearHandler implements HttpHandler {

    Facade facade;

    public ClearHandler() {
        facade = new Facade();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("clear handler");
        boolean success = false;

        try
        {
            //only allow POST operations
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                ClearResult clearResult = facade.clear();
                String respData = JsonEncoder.encodeObject(clearResult);

                if (clearResult.getMessage() == ClearResult.successMessage) //operation was successful
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
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    writeString(respData, respBody);
                    respBody.close();
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
        catch (IOException e)
        {
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

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
