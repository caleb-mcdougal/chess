package dataAccess;

import dataAccess.AuthDAO;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.Exceptions.UnauthorizedException;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO extends SQLDAOParent implements AuthDAO {
    @Override
    public String getUsername(String authToken) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT ? FROM auth WHERE column_name = authToken";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            var rs = stmt.executeQuery();
            return rs.getString("username");
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in get Username");
        }
    }

    @Override
    public boolean authExists(String authToken) throws UnauthorizedException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String sql = "SELECT ? FROM auth WHERE column_name = authToken";
            PreparedStatement stmt1 = conn.prepareStatement(sql);
            stmt1.setString(1, authToken);
            var rs = stmt1.executeQuery();
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
    public String createAuth(UserData ud) {
        String authToken = UUID.randomUUID().toString();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(statement)){
                stmt.setString(1,authToken);
                stmt.setString(2,ud.username());
            }
        } catch (SQLException | DataAccessException e) {
//            throw new DataAccessException("");                                    //dataaccess exception
            throw new RuntimeException();
        }

        return authToken;
    }

    @Override
    public void deleteAuth(String authToken) throws UnauthorizedException {

        try (var conn = DatabaseManager.getConnection()) {
            //Ensure auth token exists in DB
            authExists(authToken);

            //Delete auth
            var statement = "DELETE FROM auth WHERE authToken = ?";
            try (PreparedStatement stmt = conn.prepareStatement(statement)){
                stmt.setString(1,authToken);
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            System.out.println("here clear 1");
            var statement = "DELETE FROM auth";
            System.out.println("here clear 2");
            try (PreparedStatement stmt = conn.prepareStatement(statement)){
                System.out.println("here clea 3");
                stmt.executeUpdate();
                System.out.println("here clear 4");
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(500, "Error in clear");
        }
    }
}
