package Service;

import DAO.*;
import FakeData.LocationLoader;
import FakeData.NameLoader;
import FakeData.PersonGenerator;
import Model.*;
import Request.RegisterRequest;
import Result.RegisterResult;

/**
 * Converts the register request to a register result
 */
public class RegisterService {
    private final int DEFGEN = 4;

    /**
     * Default constructor
     */
    public RegisterService() {}

    /**
     * Performs the register operation
     * @param request RegisterRequest
     * @return RegisterResult
     */
    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        Database db = new Database();
        try {
            db.openConnection();

            //declare objects needed
            User holdUser = new User(request.getUsername(), request.getPassword(), request.getEmail(),
                    request.getFirstName(), request.getLastName(), request.getGender());
            AuthToken holdAuthToken = new AuthToken(request.getUsername());
            if (holdAuthToken == null)
                throw new Exception("Error: Unable to authenticate username");
            //chech to make sure the username is not already taken
//            User checkUser = new UserDAO(db.getConnection()).checkUser(request.getUsername());
//            if (!(checkUser == null))
//                return new RegisterResult("Error: Username already registered");
            //"register" the user by adding them to the users database
            new UserDAO(db.getConnection()).addUser(holdUser);
            Person holdPerson = new Person(holdUser.getPersonID(), request.getUsername(), request.getFirstName(),
                    request.getLastName(), request.getGender());
            //"fill" the ancestor data
            fillAncestorData(db, request, holdPerson);
            //"login" the user by checking that they are in the users database
            User checkUserDiff = new UserDAO(db.getConnection()).checkUser(request.getUsername());
            if (!holdUser.equals(checkUserDiff))
                throw new Exception("Error: Incorrect username");
            //generate a new authToken
            new AuthTokenDAO(db.getConnection()).insert(holdAuthToken);

            db.closeConnection(true);
            return new RegisterResult(holdAuthToken.getAuthToken(), holdAuthToken.getUsername(), holdUser.getPersonID());
        } catch (Exception ex) {
            System.out.println("\tException: " + ex);
            db.closeConnection(false);
            return new RegisterResult("Error: Registration failed");
        }
    }

    /**
     * Calls and parses the fill helper class
     * @param db Database that is already open
     * @param request Register Request to base the fill command on
     * @throws DataAccessException if there is an error accessing the database
     */
    private void fillAncestorData(Database db, RegisterRequest request, Person holdPerson) throws DataAccessException {
        NameLoader nameLoader = new NameLoader();
        LocationLoader locLoader = new LocationLoader();
        nameLoader.loadData();
        locLoader.loadData();
        PersonGenerator generator = new PersonGenerator(db, request, DEFGEN, holdPerson);
        generator.generatePerson(request.getGender(),  DEFGEN);
    }
}
