package ServerFacade;

import client.Repl;
import com.google.gson.Gson;
import requests.ErrorResponse;
import requests.LoginRequest;
import requests.RegisterRequest;
import requests.RegisterResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade {
    private URI uri;
    private Repl repl;
    public ServerFacade(int port, Repl repl)  throws Exception{
        uri = new URI("http://localhost:" + Integer.toString(port));
        this.repl = repl;
    }
    public String register(String username, String password, String email) {
        RegisterRequest request = new RegisterRequest(username, password, email);
        RegisterResponse response;
        try {
            response = makeRequest("POST", "/user", request, RegisterResponse.class);
        }
        catch (Exception e) {
            repl.printRegisterFail();
            return null;
        }
        return response.authToken();
    }

    public String login() {
        // Login a user
        return null;
    }

    public void logout() {
        // Logout a user
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = URI.create(uri + path).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception("Failed to make request: " + ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception(String.valueOf(status));
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
