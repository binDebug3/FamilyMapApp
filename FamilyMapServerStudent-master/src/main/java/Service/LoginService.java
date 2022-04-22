package Service;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;

import java.sql.Connection;

/**
 * Converts the login request to a login result
 */
public class LoginService {

    /**
     * Default constructor
     */
    public LoginService() {

    }

    /**
     * Performs the login operation
     * @param request request object with the information for login
     * @return LoginResult
     */
    public LoginResult login(LoginRequest request) throws DataAccessException {
        Database db = new Database();
        try {
            Connection conn = db.getConnection();

            AuthToken holdAuthToken = new AuthToken(request.getUsername());
            User holdUser = new UserDAO(conn).checkUser(request.getUsername());

            if (holdUser != null) {
                new AuthTokenDAO(conn).insert(holdAuthToken);
                if (!holdUser.getPassword().equals(request.getPassword()))
                    throw new Exception("Incorrect password");
            } else {
                throw new Exception("Username not found");
            }

            db.closeConnection(true);
            return new LoginResult(holdAuthToken.getAuthToken(), request.getUsername(), holdUser.getPersonID());
        } catch (Exception ex) {
            System.out.println("\tException: " + ex);
            db.closeConnection(false);
            return new LoginResult("Error: Invalid username/password");
        }
    }
}
