/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex
 */
public class DB {
    private static Connection db;
    private String connStr;
    
    public DB(String dbUser, String dbPass) {
        this.connStr = "jdbc:mysql://localhost/KM_bot?"+ "user=" + dbUser + "&password=" + dbPass;
        try {
            db = DriverManager.getConnection(connStr);
            db.close();
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            System.exit(0);
        }
    }

    public Connection getDb() {
        try {
            return DriverManager.getConnection(connStr);
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    } 
}
