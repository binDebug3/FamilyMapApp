package DAO;

import Model.Person;
import java.sql.*;

/**
 * Accesses and interacts the person table
 */
public class PersonDAO {
    private final Connection conn;

    /**
     * PersonDAO constructor
     * @param conn connection variable for database
     */
    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Insert a new row into the Person table
     * @param person person to add to the database
     * @throws DataAccessException if there is an error accessing the database
     */
    public void addPerson(Person person) throws DataAccessException {
        String sql = "INSERT INTO Persons (personID, associatedUsername, firstname, lastname, " +
                "gender, fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Checks the Person table for personID
     * @param searchTerm unique ID of the person to check for
     * @return the person that was found
     * @throws DataAccessException if there was an error accessing the database
     */
    public Person checkPerson(String searchKey, String searchTerm) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE " + searchKey + " = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, searchTerm);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
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

    public Person[] checkAllData(int size, String searchKey, String searchTerm) throws DataAccessException {
        Person[] person = new Person[size];
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE " + searchKey + " = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, searchTerm);
            rs = stmt.executeQuery();
            for (int i = 0; i < size; i++) {
                if (rs.next()) {
                    person[i] = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                            rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                            rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                }
            }
            return person;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding all corresponding persons");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateSpouse(String personID, String spouseID) {
        String sql = "UPDATE Persons SET spouseID = ? WHERE personID = '" + personID + "'";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, spouseID);
            stmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public int getSubSize(String searchKey, String searchTerm) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM Persons WHERE " + searchKey + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, searchTerm);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while getting size of subset of persons");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Removes the row with personID from the Person table
     * @param personID unique ID of the person to remove
     * @throws DataAccessException if there was an error accessing the database
     */
    public void removePerson(String personID) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE personID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while removing a person");
        }
    }

    /**
     * Clears the Person table
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Persons";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing Person table");
        }
    }
}
