package DAO;

import Model.User;
import java.sql.*;

/**
 * Accesses and interacts with the User table
 */
public class UserDAO {
    private final Connection conn;

    /**
     * UserDAO constructor
     * @param conn variable to connect with the database
     */
    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Insert a new row into the User table
     * @param user new user object to add to the database
     * @throws DataAccessException if there was an error accessing the database
     */
    public void addUser(User user) throws DataAccessException {
        String sql = "INSERT INTO Users (username, password, email, firstname, lastname, " +
                "gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Check the User table for a row with username and password
     * @param username username of the user to search for
     * @return the user that was found
     * @throws DataAccessException if there was an error accessing the database
     */
    public User checkUser(String username) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("personID"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Remove the row with personID from the User table
     * @param userName associated with the User to remove
     */
    public void removeUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM Users WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while removing a user");
        }
    }
    /**
     * Clear the User table
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing User table\n" + e);
        }
    }
}
