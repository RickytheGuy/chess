package ServerFacade;

import com.google.gson.Gson;
import requests.LoginRequest;
import requests.RegisterRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class ServerFacade {
    private URI uri;
    public ServerFacade(int port)  throws Exception{
        uri = new URI("http://localhost:" + Integer.toString(port));
    }
    public String register(String username, String password, String email) throws Exception {
        // Send a http request to the server to register a user
        uri = URI.create(uri + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        RegisterRequest body = new RegisterRequest(username, password, email);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }
        http.connect();

        try (InputStream respbody = http.getInputStream()) {
            InputStreamReader isr = new InputStreamReader(respbody);
            System.out.println(new Gson().fromJson(isr, String.class));
            return respbody.toString();
        }
    }

    public String login() {
        // Login a user
        return null;
    }

    public void logout() {
        // Logout a user
    }
}
