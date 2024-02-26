package model.Response;

import model.GameData;

public record ListGamesResponse(GameData[] games, String message) {
}
