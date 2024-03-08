package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.Exceptions.BadRequestException;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.UserDAO;
import model.GameData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLUserDAO extends SQLDAOParent implements UserDAO {
    @Override
    public void clear() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM user";
            try (PreparedStatement stmt = conn.prepareStatement(statement)){
                stmt.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in clear");
        }
    }

    @Override
    public void createUser(UserData ud) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            try (var stmt = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES (?, ?, ?)")) {
                stmt.setString(1,ud.username());
                stmt.setString(2,ud.password());
                stmt.setString(3,ud.email());

                if (stmt.executeUpdate() != 1) {
                    throw new DataAccessException(500,"createAuth Error");
                }
            }
        } catch (SQLException|DataAccessException e) {
            throw new DataAccessException(500, "Create Game Error");
        }
    }

    @Override
    public UserData getUser(String username) throws BadRequestException, DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT username, password, email FROM user WHERE username = ?";
            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try(ResultSet rs = stmt.executeQuery()){
                    if (rs.next()){
                        return getUserData(rs);
                    }
                    else {
                        throw new BadRequestException("Username not found");
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in get Username");
        }
    }

    private static UserData getUserData(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    @Override
    public Boolean userExists(String username) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT username, password, email FROM user WHERE username = ?";
            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try(ResultSet rs = stmt.executeQuery()){
                    return rs.next();
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in get Username");
        }
    }

    public int countRows() throws DataAccessException{
        int rowCount = 0;

        try(var conn = DatabaseManager.getConnection()) {
            // Create a statement
            Statement stmt = conn.createStatement();

            // Execute a SQL query to count the number of rows in the table
            String sql = "SELECT COUNT(*) AS row_count FROM user";
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
