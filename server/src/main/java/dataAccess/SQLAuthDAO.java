package dataAccess;

import dataAccess.AuthDAO;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import model.UserData;
import org.junit.jupiter.api.Assertions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class SQLAuthDAO extends SQLDAOParent implements AuthDAO {
    @Override
    public String getUsername(String authToken) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT username FROM auth WHERE authToken = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            } else {
                // Handle case where no rows were returned
                throw new DataAccessException(500, "Username not found");
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in get Username");
        }
    }

    @Override
    public boolean authExists(String authToken) throws UnauthorizedException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT username FROM auth WHERE authToken = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            var rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("Value does not exist in the database.");
                throw new UnauthorizedException("authtoken DNE");
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in get authExists");
        }
        return true;
    }

    @Override
    public String createAuth(UserData ud) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(statement)) {
                stmt.setString(1, authToken);
                stmt.setString(2, ud.username());
                if (stmt.executeUpdate() == 1) {
                    System.out.println("created auth: " + authToken);
                } else {
                    System.out.println("Failed to create auth");
                }
            }
            } catch (SQLException e) {
                throw new DataAccessException(500, "createAuth error");
            }
            return authToken;
    }

        @Override
    public void deleteAuth(String authToken) throws UnauthorizedException, DataAccessException {

        try (var conn = DatabaseManager.getConnection()) {
            //Ensure auth token exists in DB
            authExists(authToken);

            //Delete auth
            var statement = "DELETE FROM auth WHERE authToken = ?";
            try (PreparedStatement stmt = conn.prepareStatement(statement)){
                stmt.setString(1,authToken);

                if(stmt.executeUpdate() == 1) {
                    System.out.println("deleted auth");
                } else {
                    System.out.println("Failed to delete auth");
                    throw new UnauthorizedException("deleteAuth no match");
                }

            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500,"deleteAuth Error");
        }
    }

    @Override
    public void clear() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM auth";
            try (PreparedStatement stmt = conn.prepareStatement(statement)){
                stmt.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in clear");
        }
    }

    public int countRows() throws DataAccessException{
        int rowCount = 0;

        try(var conn = DatabaseManager.getConnection()) {
            // Create a statement
            Statement stmt = conn.createStatement();

            // Execute a SQL query to count the number of rows in the table
            String sql = "SELECT COUNT(*) AS row_count FROM auth";
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
