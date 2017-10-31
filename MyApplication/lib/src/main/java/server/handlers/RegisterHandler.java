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
import java.util.List;

import server.Facade;
import server.JsonEncoder;
import server.requests.RegisterRequest;
import server.results.RegisterResult;

/**
 * Created by bondd on 10/30/2017.
 */

public class RegisterHandler implements HttpHandler
{
    Facade facade;

    public RegisterHandler()
    {
        facade = new Facade();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.println("Entering register handler");
        boolean success = false;

        try {
            // Determine the HTTP request type (GET, POST, etc.).
            // Only allow POST requests for this operation.
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();

                // Extract the JSON string from the HTTP request body

                // Get the request body input stream
                InputStream reqBody = exchange.getRequestBody();
                // Read JSON string from the input stream
                String reqData = readString(reqBody);

                // Display/log the request JSON data
                System.out.println(reqData);

                //decode the JSON data
                RegisterRequest registerRequest = JsonEncoder.loadRegisterRequest(reqData);

                //adapt the gender string to match how it is used in the server
                String gender = registerRequest.getGender().toLowerCase();
                if (gender.equals("male") || gender.equals("m"))
                    gender = "Male";
                if (gender.equals("female") || gender.equals("f"))
                    gender = "Female";
                registerRequest.setGender(gender);

                // This is the JSON data we will return in the HTTP response body
                RegisterResult registerResult = facade.register(registerRequest);
                String respData = JsonEncoder.encodeObject(registerResult);

                if (registerResult.getErrorMsg() == null) //if there was not an error
                {
                    // Start sending the HTTP response to the client, starting with
                    // the status code and any defined headers.
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    // Now that the status code and headers have been sent to the client,
                    // next we send the JSON data in the HTTP response body.

                    // Get the response body output stream.
                    OutputStream respBody = exchange.getResponseBody();
                    // Write the JSON string to the output stream.
                    writeString(respData, respBody);
                    // Close the output stream.  This is how Java knows we are done
                    // sending data and the response is complete/
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

    /*
    The readString method shows how to read a String from an InputStream.
*/
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

}
