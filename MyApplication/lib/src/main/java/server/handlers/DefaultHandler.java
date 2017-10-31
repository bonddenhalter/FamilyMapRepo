package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import server.JsonEncoder;
import server.results.ClearResult;

/**
 * Created by bondd on 10/30/2017.
 */

public class DefaultHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        boolean success = false;

        try
        {
            //read in html file
            String respData = "";
            BufferedReader in = new BufferedReader(new FileReader("web/index.html"));
            String str;
            while ((str = in.readLine()) != null) {
                respData +=str;
            }
            in.close();

            // Start sending the HTTP response to the client, starting with
            // the status code and any defined headers.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            // Get the response body output stream.
            OutputStream respBody = exchange.getResponseBody();
            // Write the JSON string to the output stream.
            writeString(respData, respBody);
            // Close the output stream.  This is how Java knows we are done
            // sending data and the response is complete/
            respBody.close();

            success = true;
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
