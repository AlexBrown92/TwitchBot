package db;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public final class DatabaseConnection {

    private static Connection conn = null;
    private static String url;
    private static String user;
    private static String pass;

    public DatabaseConnection(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public static Connection getConnection() {

        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl(url);
        bds.setUsername(user);
        bds.setPassword(pass);

        try {
            System.out.println("Attempting Database Connection");
            conn = bds.getConnection();
            System.out.println("Connected Successfully");
        } catch (SQLException e) {
            System.out.println("Caught SQL Exception: " + e);
        }
        return conn;
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }
}
