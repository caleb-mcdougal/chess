package model.RequestResponse;

import model.GameData;

public record ListGamesResponse(GameData[] games, String message) {
}
