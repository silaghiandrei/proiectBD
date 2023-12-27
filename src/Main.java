import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (Exception ex){
            System.err.println("An exception occured during JDBC Driver loading" +
                    " Details are provided below:");
            ex.printStackTrace(System.err);
        }
        Connection connection = null;
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost/mydb?user=root&password=mysqlpass");
        }
        catch(SQLException sqlex){
            System.err.println("An SQL Exception occured. Details are provided below:");
            sqlex.printStackTrace(System.err);
        }
        finally{
            if(connection != null){
                try {
                    connection.close();
                }
                catch(SQLException e){}
                connection = null;
            }
        }
    }
}