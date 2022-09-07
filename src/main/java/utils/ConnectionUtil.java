package utils;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionUtil {

    private static Connection conn;

    private ConnectionUtil() {
        conn = null;
    }

    public static Connection getConn() {

        try {
            if (conn != null && !conn.isClosed()) {
                return conn;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url, user, pass;
        Properties props = new Properties();

        try {
            Class.forName("org.postgresql.Driver");

            props.load(new FileReader("/home/matt/IdeaProjects/McCain-Matthew-P1/src/main/resources/database.properties"));
            url = props.getProperty("url");
            user = props.getProperty("user");
            pass = props.getProperty("pass");

            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
