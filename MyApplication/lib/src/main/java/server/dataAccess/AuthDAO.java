package server.dataAccess;


import java.sql.Statement;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.lang.Object;

import server.models.Auth;

/**
 * Interfaces with the Auth database table and Auth objects.
 */

public class AuthDAO {

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

    public void createTable() throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String create  = "create table if not exists auth(token text not null primary key," +
                "username text not null," +
                "loginTime integer not null);";
        PreparedStatement stmt = connection.prepareStatement(create);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
    /**
     * Adds an auth row to the auth table
     */
    public void addAuth(Auth a) throws SQLException
    {
        String token = a.getToken();
        String username = a.getUsername();
        int loginTime = a.getLoginTime();
        Connection connection = DriverManager.getConnection(connectionURL);

        String add = "insert into auth (token, username, loginTime) values (?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(add);
        stmt.setString(1, token);
        stmt.setString(2, username);
        stmt.setInt(3, loginTime);
        stmt.executeUpdate();
        connection.close();;
    }

    /**
     * Retrieves a row from the auth table
     * @param token the token of the row you want
     * @return the Auth object that the row represents
     */
    public Auth getAuth(String token) throws SQLException
    {
        Connection connection = DriverManager.getConnection(connectionURL);
        String query = "select * from auth where token = ? ";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, token); //search for the row with the matching token

        ResultSet results = stmt.executeQuery();
        String tok = results.getString(1);
        String username = results.getString(2);
        int loginTime = results.getInt(3);

        Auth a = new Auth(tok, username, loginTime);
        results.close(); //DO I HAVE TO CLOSE EVERTHING, INCLUDING THE CONNECTION?
        stmt.close();
        connection.close();
        return a;
    }


    /**
     * Joins a list of strings into a comma delimited string
     * @param values the list of strings
     * @return the combined string
     */
//    public String joinList(List<String> values)
//    {
//        StringBuilder builder = new StringBuilder();
//        for (String s : values)
//        {
//            builder.append(s);
//            builder.append(", ");
//        }
//        builder.delete(builder.lastIndexOf(","), builder.length()); //remove the extra comma and space at the end
//        return builder.toString();
//    }

}
