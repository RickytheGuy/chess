package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.GameData;
import requests.ChessResponse;
import requests.ErrorResponse;
import requests.JoinGameRequest;
import requests.ListGameResponse;
import services.ListGameService;
import spark.Request;
import spark.Response;

public class ListGameHandler {
    private final ListGameService listGameService;
    private final Gson serializer = new Gson();
    public ListGameHandler(AuthDAO authData, GameDAO gameData) {
        listGameService = new ListGameService(authData, gameData);
    }
    public Object handleRequest(Request request, Response response) {
        String authToken = request.headers("Authorization");
        JoinGameRequest req =  serializer.fromJson(request.body(), JoinGameRequest.class);
        ChessResponse res = listGameService.listGames(req, authToken);

        if (res instanceof ErrorResponse) {
            response.status(((ErrorResponse) res).status());
        }
        else {
            response.status(200);
            String[] arr_res;
            StringBuilder builder = new StringBuilder("{\"games\":[");
            if (!((ListGameResponse) res).games().isEmpty()) {
                for (GameData game : ((ListGameResponse) res).games()) {
                    arr_res = serializer.toJson(game).split(",");
                    for (int i = 0; i < arr_res.length - 1; i++) {
                        builder.append(arr_res[i]);
                        if (i != arr_res.length - 2) {
                            builder.append(",");
                        }
                    }
                    builder.append("},");
                }
                builder.replace(builder.length() - 1, builder.length(), "]}");
            } else {return serializer.toJson(res);}
            //builder.append("}]}");
            //return builder.toString();
        }
        return serializer.toJson(res);

    }
}
