package requests;

public record LoginResponse(String username, String authToken) implements ChessResponse {
}
