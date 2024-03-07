package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO extends SQLDAOParent implements GameDAO {
    @Override
    public void clear() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM game";
            try (PreparedStatement stmt = conn.prepareStatement(statement)){
                stmt.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in clear");
        }
    }

    @Override
    public int createGame(String name) throws  DataAccessException{
        ChessGame newGame = new ChessGame();
        var json = new Gson().toJson(newGame);
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement("INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)", RETURN_GENERATED_KEYS)) {
//                stmt.setString(1,authToken);
                stmt.setString(1,null);
                stmt.setString(2,null);
                stmt.setString(3,name);
                stmt.setString(4, json);

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                var gameID = 0;
                if (rs.next()) {
                    gameID = rs.getInt(1);
                }
                return gameID;
            }
        } catch (SQLException|DataAccessException e) {
            throw new DataAccessException(500, "countRows Error");
        }
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String json = rs.getString("game");
                ChessGame game = new Gson().fromJson(json, ChessGame.class);
                return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
            } else {
                // Handle case where no rows were returned
                throw new BadRequestException("Username not found");
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in get Username");
        }
    }

    @Override
    public GameData[] listGames() {
        return new GameData[0];
    }

    @Override
    public void updateGame(int gameID, String color, String username) {

    }

    public int countRows() throws DataAccessException{
        int rowCount = 0;

        try(var conn = DatabaseManager.getConnection()) {
            // Create a statement
            Statement stmt = conn.createStatement();

            // Execute a SQL query to count the number of rows in the table
            String sql = "SELECT COUNT(*) AS row_count FROM game";
            ResultSet rs = stmt.executeQuery(sql);

            // Retrieve the row count from the result set
            if (rs.next()) {
                rowCount = rs.getInt("row_count");
            }
            System.out.println("rowCount: " + rowCount);
        } catch (SQLException|DataAccessException e) {
            throw new DataAccessException(500, "countRows Error");
        }
        return rowCount;
    }
}
