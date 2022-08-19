package managers;

import helpers.ClientLoginInfo;
import helpers.SecuredPassword;

import java.io.File;
import java.sql.*;

public class DataBaseImpl implements DataBase {

    private final String url;
    private Connection connection;
    private final String filePath = "src/main/resources/dataBase/dataBase.db";

    public DataBaseImpl(String url) {
        this.url = url;
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        File dbFile = new File(filePath);
        if (!dbFile.exists()) {
            createDataBase();
            createUsersTable();
        }
    }

    @Override
    public ClientLoginInfo getClient(String nickname) {
        return new ClientLoginInfo(nickname, getSecuredPasswordFromDB(nickname));
    }

    @Override
    public void saveClient(ClientLoginInfo clientLoginInfo) {
        insertUser(clientLoginInfo);
    }


    private void insertUser(ClientLoginInfo clientLoginInfo) {
        String sql = "INSERT INTO users(name,password,salt) VALUES(?,?,?)";
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

    private SecuredPassword getSecuredPasswordFromDB(String nickname) {
        String sql = "SELECT * FROM users";
        String hashedPassword = null;
        String salt = null;

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("name").contains(nickname)) {
                    hashedPassword = rs.getString("password");
                    salt = rs.getString("salt");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new SecuredPassword(hashedPassword, salt);
    }

    private void createDataBase() {
        try {
            if (connection != null) {
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
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
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
