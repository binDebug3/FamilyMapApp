package FakeData;

import DAO.DataAccessException;
import FakeData.LocationLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {
    private LocationLoader locLoad;
    private boolean print = true;

    @BeforeEach
    public void setUp() throws DataAccessException {
        if (print)
            System.out.println("\nLocationTest SetUp called");
        locLoad = new LocationLoader();
    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        if (print)
            System.out.println("Finished one test case");
    }

    @Test
    public void testLoad() throws DataAccessException {
        if (print)
            System.out.println("Test testLoad called");
        locLoad.loadData();
    }
}
