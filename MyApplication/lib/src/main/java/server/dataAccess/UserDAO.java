package server.dataAccess;
import java.sql.*;

import server.models.User;

/**
 * Interfaces with the Users database table and User objects.
 */

public class UserDAO {

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
     * Creates the Users table
     */
    public void createTable() throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String create  = "create table if not exists users(username text not null primary key, " +
                "password text not null, " +
                "email text not null, " +
                "firstName text not null, " +
                "lastName text not null, " +
                "gender text not null, " +
                "personID text not null);";
        PreparedStatement stmt = connection.prepareStatement(create);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }


    /**
     * Adds a user row to the users table
     * @param u The user to add
     */
    public void addUser(User u) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);

        String add = "insert into users (username, password, email, firstName, lastName, gender, personID)" +
                " values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(add);
        stmt.setString(1, u.getUsername());
        stmt.setString(2, u.getPassword());
        stmt.setString(3, u.getEmail());
        stmt.setString(4, u.getFirstName());
        stmt.setString(5, u.getLastName());
        stmt.setString(6, u.getGender());
        stmt.setString(7, u.getPersonID());

        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    /**
     * Retrieves a row from the users table
     * @param username the username of the row you want
     * @return the User object that the row represents
     */
    public User getUser(String username) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String query = "select * from users where username = ? ";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username); //search for the row with the matching personID

        ResultSet results = stmt.executeQuery();
        if (!results.isBeforeFirst()) //if nothing found in database
            return null;
        String usernameResult = results.getString(1);
        String password = results.getString(2);
        String email = results.getString(3);
        String firstName = results.getString(4);
        String lastName = results.getString(5);
        String gender = results.getString(6);
        String personID = results.getString(7);

        User u = new User(usernameResult, password, email, firstName, lastName, gender, personID);
        results.close(); //DO I HAVE TO CLOSE EVERYTHING, INCLUDING THE CONNECTION?
        stmt.close();
        connection.close();
        return u;
    }
}
