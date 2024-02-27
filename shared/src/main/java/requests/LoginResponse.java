package requests;

public record LoginResponse(int playerID, String authToken, String username, String message, int status) {
}
