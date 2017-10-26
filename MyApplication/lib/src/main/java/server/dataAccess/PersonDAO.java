package server.dataAccess;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import server.models.Person;


/**
 * Interfaces with the Persons database table and Person objects.
 */

public class PersonDAO {

    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    final String connectionURL = "jdbc:sqlite:database.sqlite";

    /**
     * Creates the Persons table in the database
     * @throws SQLException
     */
    public void createTable() throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String create  = "create table if not exists persons ( personID text not null primary key, " +
                "descendant text not null, " +
                "firstName text not null, " +
                "lastName text not null, " +
                "gender text not null, " +
                "father text not null, " +
                "mother text not null, " +
                "spouse text not null);";
        PreparedStatement stmt = connection.prepareStatement(create);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }


    /**
     * Adds a person row to the persons table
     * @param p The person object to add to the table
     */
    public void addPerson (Person p) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);

        String add = "insert into persons (personID, descendant, firstName, lastName, gender, father, mother, spouse)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(add);
        stmt.setString(1, p.getPersonID());
        stmt.setString(2, p.getDescendant());
        stmt.setString(3, p.getFirstName());
        stmt.setString(4, p.getLastName());
        stmt.setString(5, p.getGender());
        stmt.setString(6, p.getFather());
        stmt.setString(7, p.getMother());
        stmt.setString(8, p.getSpouse());

        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    /**
     * Retrieves a row from the persons table
     * @param personID The personID of the row you want from the table
     * @return The Person object that the row represents. Null if not found
     */
    public Person getPerson(String personID) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String query = "select * from persons where personID = ? ";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, personID); //search for the row with the matching personID

        ResultSet results = stmt.executeQuery();
        if (!results.isBeforeFirst()) //if nothing found in database
            return null;
        String personIDResult = results.getString(1);
        String descendant = results.getString(2);
        String firstName = results.getString(3);
        String lastName = results.getString(4);
        String gender = results.getString(5);
        String father = results.getString(6);
        String mother = results.getString(7);
        String spouse = results.getString(8);

        Person p = new Person(personIDResult, descendant, firstName, lastName, gender, father, mother, spouse);
        results.close(); //DO I HAVE TO CLOSE EVERYTHING, INCLUDING THE CONNECTION?
        stmt.close();
        connection.close();
        return p;
    }

    /**
     * Returns an array of the ancestors of the person with the given username
     * @param username the username to match with the "descendant" field in the persons database
     * @return list of all of the People objects that are ancestors of the given username.
     */
    public List<Person> getPeople (String username) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String query = "select * from persons where descendant = ? ";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username); //search for the row with the matching descendant
        ResultSet results = stmt.executeQuery();

        List<Person> people = new ArrayList<>();
        while (results.next()) //next() returns false when there's no data left
        {
            String personID = results.getString(1);
            String descendant = results.getString(2);
            String firstName = results.getString(3);
            String lastName= results.getString(4);
            String gender= results.getString(5);
            String father = results.getString(6);
            String mother = results.getString(7);
            String spouse = results.getString(8);

            Person p = new Person(personID, descendant, firstName, lastName, gender, father, mother, spouse);
            people.add(p);
        }

        return people;
    }

    /**
     * Deletes all rows where the descendant matches the username
     * @param username the username to delete
     */
    public void delete(String username) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String clear = "delete from persons where descendant = ?";
        PreparedStatement stmt = connection.prepareStatement(clear);
        stmt.setString(1, username); //search for the row with the matching username

        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    public void clear() throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String clear = "delete from persons";
        PreparedStatement stmt = connection.prepareStatement(clear);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
}
