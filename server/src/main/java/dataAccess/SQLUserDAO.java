package dataAccess;

import dataAccess.Exceptions.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    public void createUser(UserData ud) {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public Boolean userExists(String username) {
        return null;
    }
}
