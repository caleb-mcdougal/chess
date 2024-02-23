package UnitTests;

import chess.ChessGame;
import dataAccess.MemoryGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

public class CreateGameTests {

    @Test
    @DisplayName("Create Game Positive Case")
    public void Pos(){
        GameService gs = new GameService();
        ChessGame game = new ChessGame();
        MemoryGameDAO mgd = new MemoryGameDAO();

        int GameSizePre = mgd.getDBSize();
        System.out.println("DB size before new game:");
        System.out.println(GameSizePre);

        GameData gd = new GameData(0, "user1", "user2", "game1", game);
        gs.createGame(gd);

        int GameSizePost = mgd.getDBSize();
        System.out.println("DB size after new game:");
        System.out.println(GameSizePost);

        Assertions.assertEquals(GameSizePre + 1,GameSizePost);
    }
}
