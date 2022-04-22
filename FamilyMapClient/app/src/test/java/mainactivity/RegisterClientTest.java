package mainactivity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Request.RegisterRequest;
import Result.EventAllResult;
import Result.PersonAllResult;
import Result.RegisterResult;
import edu.byu.cs240.dallinstewart.familymapclient.Global;
import network.ServerProxy;

public class RegisterClientTest {

    private final boolean print = true;
    private static final String SERVER_HOST = "localhost";
    private static final String SERVER_PORT = "8080";
    private RegisterRequest registerRequest;

    public RegisterClientTest() {}

    @BeforeEach
    public void setUp() {
        //make a new register request and clear the data base
        if (print)
            System.out.println("\nRegisterClientTest SetUp called");
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("dallin");
        registerRequest.setPassword("securePassword");
        registerRequest.setFirstName("Dallin");
        registerRequest.setLastName("Stewart");
        registerRequest.setEmail("dallin@byu.edu");
        registerRequest.setGender("m");
        ServerProxy.postClear(SERVER_HOST, SERVER_PORT);
    }
    @AfterEach
    public void tearDown() {
        if (print)
            System.out.println("Test successful. Close called");
    }
    @Test
    public void registerPass() {
        //test a normal register request
        if (print)
            System.out.println("Test registerPass called");
        RegisterResult result = ServerProxy.postRegisterUser(SERVER_HOST,SERVER_PORT,registerRequest);
        assertNotNull(result);
        assertNotNull(result.getPersonID());
    }
    @Test
    public void registerFailMissing() {
        //test a register request that is missing a value
        if (print)
            System.out.println("Test registerFailMissing called");
        registerRequest.setFirstName("");
        assertFalse(Global.registerIsValid(registerRequest));
    }
    @Test
    public void registerFailUniqueUser() {
        //test a register request that has already been registered in the data base
        if (print)
            System.out.println("Test registerFailUniqueUser called");
        ServerProxy.postRegisterUser(SERVER_HOST,SERVER_PORT, registerRequest);
        RegisterResult result = ServerProxy.postRegisterUser(SERVER_HOST, SERVER_PORT, registerRequest);
        assertNotNull(result);
        assertNull(result.getPersonID());
    }

    @Test
    public void fillRegisterPeople() {
        //Tests successful person fill when registering
        if (print)
            System.out.println("Test fillRegisterPeople called");
        registerRequest.setUsername("more different");
        RegisterResult result = ServerProxy.postRegisterUser(SERVER_HOST, SERVER_PORT, registerRequest);
        assertNotNull(result);
        String personID = result.getPersonID();
        String authToken = result.getAuthToken();
        PersonAllResult personAllResult = ServerProxy.getPersons(SERVER_HOST, SERVER_PORT,
                personID, authToken);
        assertNotNull(personAllResult);
        assertTrue(personAllResult.isSuccess());
    }
    @Test
    public void fillRegisterPeopleFail() {
        if (print)
            System.out.println("Test fillRegisterPeopleFail called");
        RegisterResult result = ServerProxy.postRegisterUser(SERVER_HOST,SERVER_PORT,registerRequest);
        assertNotNull(result);
        String personID = result.getPersonID();
        String authToken = "fake authtoken";
        PersonAllResult personAllResult = ServerProxy.getPersons(SERVER_HOST, SERVER_PORT,
                personID, authToken);
        assertNotNull(personAllResult);
        assertFalse(personAllResult.isSuccess());
    }
    @Test
    public void fillRegisterEvents() {
        //Tests successful event fill when registering
        if (print)
            System.out.println("Test fillRegisterEvents called");
        registerRequest.setUsername("different");
        RegisterResult result = ServerProxy.postRegisterUser(SERVER_HOST,SERVER_PORT, registerRequest);
        assertNotNull(result);
        String authToken = result.getAuthToken();
        EventAllResult eventAllResult = ServerProxy.getEvents(SERVER_HOST, SERVER_PORT, authToken);
        assertNotNull(eventAllResult);
        assertTrue(eventAllResult.isSuccess());
    }
    @Test
    public void fillRegisterEventsFail() {
        if (print)
            System.out.println("Test fillRegisterEventsFail called");
        RegisterResult result = ServerProxy.postRegisterUser(SERVER_HOST,SERVER_PORT,registerRequest);
        String authToken = "fake authtoken";
        EventAllResult eventAllResult = ServerProxy.getEvents(SERVER_HOST, SERVER_PORT, authToken);
        assertNotNull(eventAllResult);
        assertFalse(eventAllResult.isSuccess());
    }
}
