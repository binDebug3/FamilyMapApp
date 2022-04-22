package mainactivity;

import Request.LoginRequest;
import Result.EventAllResult;
import Result.LoginResult;
import Result.PersonAllResult;
import network.ServerProxy;

//import static org.junit.Assert.*;
//import org.junit.BeforeClass;
//import org.junit.AfterClass;
//import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginClientTest {
    private final boolean print = true;
    private static final String SERVER_HOST = "localhost";
    private static final String SERVER_PORT = "8080";
    private LoginRequest loginRequest;

    public LoginClientTest() {}

//    @BeforeClass
    @BeforeEach
    public void setUp() {
        //create a new login request for every test
        if (print)
            System.out.println("\nLoginFragment2Test SetUp called");
        loginRequest = new LoginRequest();
        loginRequest.setUsername("sheila");
        loginRequest.setPassword("parker");
    }
    @AfterEach
    public void tearDown() {
        if (print)
            System.out.println("Test successful. Close called");
    }

    @Test
    public void loginPass() {
        //test successful login
        if (print)
            System.out.println("Test loginPass called");
        LoginResult result = ServerProxy.postUserLogin(SERVER_HOST,SERVER_PORT,loginRequest);
        assertNotNull(result);
        assertNotNull(result.getPersonID());
    }
    @Test
    public void loginFailPassword() {
        //test login with incorrect password
        if (print)
            System.out.println("Test loginFailPassword called");
        loginRequest.setPassword("fake");
        LoginResult result = ServerProxy.postUserLogin(SERVER_HOST,SERVER_PORT,loginRequest);
        assertNotNull(result);
        assertNull(result.getPersonID());
    }

    @Test
    public void fillLoginPeople() {
        //Tests successful person fill when logging in
        if (print)
            System.out.println("Test fillLoginPeople called");
        LoginResult result = ServerProxy.postUserLogin(SERVER_HOST,SERVER_PORT,loginRequest);
        assertNotNull(result);
        String personID = result.getPersonID();
        String authToken = result.getAuthToken();
        PersonAllResult personAllResult = ServerProxy.getPersons(SERVER_HOST, SERVER_PORT,
                personID, authToken);
        assertNotNull(personAllResult);
        assertTrue(personAllResult.isSuccess());
    }
    @Test
    public void fillLoginPeopleFail() {
        //Tests the person retrieval with an incorrect authtoken
        if (print)
            System.out.println("Test fillLoginPeopleFail called");
        LoginResult result = ServerProxy.postUserLogin(SERVER_HOST,SERVER_PORT,loginRequest);
        assertNotNull(result);
        String personID = result.getPersonID();
        String authToken = "wrong authtoken";
        PersonAllResult personAllResult = ServerProxy.getPersons(SERVER_HOST, SERVER_PORT,
                personID, authToken);
        assertNotNull(personAllResult);
        assertFalse(personAllResult.isSuccess());
    }
    @Test
    public void fillLoginEvents() {
        //Tests successful event fill when logging in
        if (print)
            System.out.println("Test fillLoginEvents called");
        LoginResult result = ServerProxy.postUserLogin(SERVER_HOST,SERVER_PORT,loginRequest);
        assertNotNull(result);
        String authToken = result.getAuthToken();
        EventAllResult eventAllResult = ServerProxy.getEvents(SERVER_HOST, SERVER_PORT, authToken);
        assertNotNull(eventAllResult);
        assertTrue(eventAllResult.isSuccess());
    }
    @Test
    public void fillLoginEventsFail() {
        //Tests the event retrieval with an incorrect authtoken
        if (print)
            System.out.println("Test fillLoginEventsFail called");
        LoginResult result = ServerProxy.postUserLogin(SERVER_HOST,SERVER_PORT,loginRequest);
        assertNotNull(result);
        String authToken = "wrong authtoken";
        result.setAuthToken(authToken);
        EventAllResult eventAllResult = ServerProxy.getEvents(SERVER_HOST, SERVER_PORT, authToken);
        assertNotNull(eventAllResult);
        assertFalse(eventAllResult.isSuccess());
    }
}
