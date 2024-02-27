package requests;

public record ErrorResponse(int status, String message) implements ChessResponse {
}
