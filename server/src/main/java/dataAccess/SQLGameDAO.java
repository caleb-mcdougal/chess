package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO extends SQLDAOParent implements GameDAO {
    @Override
    public void clear() {

    }

    @Override
    public int createGame(String name) {
        ChessGame newGame = new ChessGame();
        var json = new Gson().toJson(newGame);
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement("INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)", RETURN_GENERATED_KEYS)) {
//                stmt.setString(1,authToken);
                stmt.setString(2,null);
                stmt.setString(3,null);
                stmt.setString(4,name);
                stmt.setString(5, json);

                stmt.executeUpdate();

                var rs = stmt.getGeneratedKeys();
                var ID = 0;
                if (rs.next()) {
                    ID = rs.getInt(1);
                }
                return ID;
            }
        } catch (SQLException | DataAccessException e) {
//            throw new DataAccessException("");                                    //datAccess exception
            throw new RuntimeException();
        }
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException {
//        var json = new Gson();
//        try (var conn = DatabaseManager.getConnection()) {
//            String sql = "SELECT ? FROM game WHERE column_name = gameID";
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            stmt.setString(1, gameID);
//            var rs = stmt.executeQuery();
//            rs.getBlob("game");
//            return
//        } catch (SQLException | DataAccessException e) {
//            throw new RuntimeException(e);
//        }
        return null;
    }

    @Override
    public GameData[] listGames() {
        return new GameData[0];
    }

    @Override
    public void updateGame(int gameID, String color, String username) {

    }
}
