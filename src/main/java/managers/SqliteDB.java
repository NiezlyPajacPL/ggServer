package managers;

import helpers.ClientLoginInfo;

import java.sql.*;

public class SqliteDB implements DataBase {

    String path;
    Connection connection;

    public SqliteDB(String path) {
        this.path = path;
        try {
            connection = DriverManager.getConnection(path);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ClientLoginInfo getClient(String nickname) {
        return null;
    }

    @Override
    public void saveClient(ClientLoginInfo clientLoginInfo) {

    }

    public void createDataBase() {
        try {
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
                createUsersTable();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createUsersTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id integer PRIMARY KEY,\n"
                + " name text NOT NULL,\n"
                + " password text NOT NULL,\n"
                + " salt text NOT NULL\n"
                + ");";

        try {
            Connection conn = DriverManager.getConnection(path);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertUser(ClientLoginInfo clientLoginInfo) {
        String sql = "INSERT INTO users(name, capacity) VALUES(?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, clientLoginInfo.getNickname());
            statement.setString(2, clientLoginInfo.getSecuredPassword().getPassword());
            statement.setString(3, clientLoginInfo.getSecuredPassword().getSalt());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
