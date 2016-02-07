/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blacklist;

import commands.Command;
import commands.Commands;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static twitchbot.TwitchBot.db;

/**
 *
 * @author Alex
 */
public class Blacklist {

    private ArrayList<String> blacklist;

    public Blacklist() {
        blacklist = getAllBlacklist();
    }

    public void addBlacklist(String message) {
        String[] msgSplit = message.split("\\s+");
        String prefix = msgSplit[0].substring(1, msgSplit[0].length()).toLowerCase(); // Remove +
        int start = 1;
        String term = "";
        for (int i = start; i < msgSplit.length; i++) {
            term = term + " " + msgSplit[i];
        }
        blacklist.add(term.toLowerCase());
        int termId = 0;
        try {
            try (Connection conn = db.getConnection()) {
                PreparedStatement addBlacklist = conn.prepareStatement(""
                        + "INSERT INTO `blacklist`(`blacklistTerm`,`blacklistCreator`)"
                        + "VALUES(?,-1);", Statement.RETURN_GENERATED_KEYS);
                addBlacklist.setString(1, term);
                addBlacklist.executeUpdate();
                try (ResultSet rs = addBlacklist.getGeneratedKeys()) {
                    if (rs.next()) {
                        termId = rs.getInt(1);
                    }
                }
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void removeBlacklist(String message){
        String[] msgSplit = message.split("\\s+");
        String prefix = msgSplit[0].substring(1, msgSplit[0].length()).toLowerCase(); // Remove +
        int start = 1;
        String term = "";
        for (int i = start; i < msgSplit.length; i++) {
            term = term + " " + msgSplit[i];
        }
        blacklist.remove(term.toLowerCase());
        
        try {
            try (Connection conn = db.getConnection()) {
                PreparedStatement removeBlacklist = conn.prepareStatement(""
                + "DELETE FROM `blacklist`"
                + "WHERE `blacklistTerm` = ?"
                + "LIMIT 1;");
                removeBlacklist.setString(1, term);
                removeBlacklist.executeUpdate();
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String outputBlacklist() {
        String out = "";
        for (String t : blacklist) {
            out = out.concat(", " + t);
        }
        return out;
    }

    public boolean isBlacklisted(String term) {
        term = term.toLowerCase();
        if (blacklist.contains(term)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean containsBlacklistedTerm(String message) {
        for (String term : blacklist) {
            if (message.toLowerCase().contains(term)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<String> getAllBlacklist() {
        ArrayList<String> bl = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            PreparedStatement selectAllCommands = conn.prepareStatement(""
                    + "SELECT *"
                    + "FROM `blacklist`;");
            try (ResultSet rs = selectAllCommands.executeQuery()) {
                while (rs.next()) {
                    bl.add(rs.getString("blacklistTerm"));
                }
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bl;
    }
}
