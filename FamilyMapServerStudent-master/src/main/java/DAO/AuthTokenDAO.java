package DAO;

import Model.AuthToken;

import java.sql.*;

/**
 * Access and interact with the AuthToken table
 */
public class AuthTokenDAO {
    private final Connection conn;

    /**
     * AuthTokenDAO constructor
     * @param conn connection variable
     */
    public AuthTokenDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Insert a new authToken into the AuthToken table
     * @param authToken identifies the authToken
     * @throws DataAccessException if there is a data access error
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        String sql = "INSERT INTO AuthTokens (AuthToken, Username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAuthToken());
            stmt.setString(2, authToken.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the AuthToken table");
        }
    }

    /**
     * Check for an authToken in the AuthToken table
     * @param authToken identifies the authToken
     * @return true if the authToken is in the table, false otherwise
     */
    public AuthToken checkToken(String authToken) throws DataAccessException {
        AuthToken authToken1;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken1 = new AuthToken(rs.getString("authToken"), rs.getString("username"));
                return authToken1;
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
     * Remove an authToken from the AuthToken table
     * @param authToken identifies the authToken
     */
    public void removeToken(String authToken) throws DataAccessException {
        String sql = "DELETE FROM AuthTokens WHERE authToken = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while removing an authToken\n" + e);
        }
    }

    /**
     * Clear the AuthToken table
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM AuthTokens";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing AuthToken table\n" + e);
        }
    }
}
