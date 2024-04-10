package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO extends SQLDAOParent implements GameDAO {
    @Override
    public void clear() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM game";
            try (PreparedStatement stmt = conn.prepareStatement(statement)){
                stmt.executeUpdate();
            }
                                                                                    //Unsure about this
//            statement = "ALTER TABLE game AUTO_INCREMENT = 1";
//            try (PreparedStatement stmt = conn.prepareStatement(statement)){
//                stmt.executeUpdate();
//            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in clear");
        }
    }

    @Override
    public int createGame(String name) throws DataAccessException{
        ChessGame newGame = ChessGame.CreateNew();
        var json = new Gson().toJson(newGame);
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement("INSERT INTO game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)", RETURN_GENERATED_KEYS)) {
                stmt.setString(1,null);
                stmt.setString(2,null);
                stmt.setString(3,name);
                stmt.setString(4, json);

                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                int gameID = 0;
                if (rs.next()) {
                    gameID = rs.getInt(1);
                }
                else{
                    throw new DataAccessException(500, "Create Game Error");
                }
                return gameID;
            }
        } catch (SQLException|DataAccessException e) {
            System.out.println("e: " + e.getMessage());
            throw new DataAccessException(500, "Create Game Error");
        }
    }

    @Override
    public GameData getGame(int gameID) throws BadRequestException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return getGameData(rs);
            } else {
                // Handle case where no rows were returned
                throw new BadRequestException("Username not found");
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in get Username");
        }
    }

    private static GameData getGameData(ResultSet rs) throws SQLException {
        int gameID = rs.getInt(1);
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        String json = rs.getString("game");
        ChessGame game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public GameData[] listGames() throws DataAccessException {
        int size = countRows();
        GameData[] gameList = new GameData[size];
        int count = 0;
//        List<GameData> gameList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
//                      gameList.add(getGameData(rs));
                        gameList[count] = getGameData(rs);
                        count += 1;
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in get Username");
        }
        return gameList;
    }

    @Override
    public void updateGame(int gameID, String color, String username) throws DataAccessException, BadRequestException{
        try (var conn = DatabaseManager.getConnection()) {
            GameData oldGame = getGame(gameID);

            try (var stmt = conn.prepareStatement("UPDATE game set whiteUsername = ?, blackUsername = ? WHERE gameID = ?")) {
                if (color.equals("WHITE")){
                    stmt.setString(1,username);
                    stmt.setString(2, oldGame.blackUsername());
                }
                else if (color.equals("BLACK")){
                    stmt.setString(1, oldGame.whiteUsername());
                    stmt.setString(2,username);
                }
                stmt.setInt(3,gameID);

                if(stmt.executeUpdate() != 1) {
                    throw new DataAccessException(500, "Update Game Error");
                }
            }
        } catch (SQLException|DataAccessException e) {
            throw new DataAccessException(500, "Update Game Error");
        }
    }

    public void updateGameBoard(int gameID, ChessGame game) throws DataAccessException, BadRequestException{
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement("UPDATE game set game = ? WHERE gameID = ?")) {
                var json = new Gson().toJson(game);
                stmt.setString(1, json);
                stmt.setInt(2,gameID);

                if(stmt.executeUpdate() != 1) {
                    throw new DataAccessException(500, "Update Game Board Error");
                }
            }
        } catch (SQLException|DataAccessException e) {
            throw new DataAccessException(500, "Create Game Board Error");
        }
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
//            System.out.println("rowCount: " + rowCount);
        } catch (SQLException|DataAccessException e) {
            throw new DataAccessException(500, "countRows Error");
        }
        return rowCount;
    }
}
