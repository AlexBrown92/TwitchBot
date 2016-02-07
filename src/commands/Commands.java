package commands;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static twitchbot.TwitchBot.db;

/**
 *
 * @author Alex Brown
 */
public class Commands {

    private Map<String, Command> commands;

    public Commands() {
        commands = selectAllCommands();
    }

    public void addCommand(String message) {
        String[] msgSplit = message.split("\\s+");
        String prefix = msgSplit[0].substring(2, msgSplit[0].length()).toLowerCase(); // Remove +
        int start = 1;
        String text = "";
        for (int i = start; i < msgSplit.length; i++) {
            text = text + " " + msgSplit[i];
        }
        boolean audience = true;
        if (msgSplit[1].equalsIgnoreCase("s")) {
            start = 2;
        } else {
            if (msgSplit[1].equalsIgnoreCase("m")) {
                audience = false;
                start = 2;
            }
        }
        // If this command doesn't exist already
        if (this.commands.get(prefix) == null) {
            int commandId = 0;
            try {
                try (Connection conn = db.getConnection()) {
                    PreparedStatement addCommand = conn.prepareStatement(""
                            + "INSERT INTO `command`(`commandPrefix`,`commandText`,`commandAudience`)"
                            + "VALUES(?,?,?);", Statement.RETURN_GENERATED_KEYS);
                    addCommand.setString(1, prefix);
                    addCommand.setString(2, text);
                    addCommand.setBoolean(3, audience);
                    addCommand.executeUpdate();
                    try (ResultSet rs = addCommand.getGeneratedKeys()) {
                        if (rs.next()) {
                            commandId = rs.getInt(1);
                        }
                    }
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.commands.put(prefix, new Command(commandId, prefix, text, audience));
        } else { // This is an update to an existing command
            try {
                try (Connection conn = db.getConnection()) {
                    PreparedStatement updateCommand = conn.prepareStatement(""
                            + "UPDATE `command` "
                            + "SET `commandText` = ?, `commandAudience` = ? "
                            + "WHERE `commandPrefix` = ? "
                            + "LIMIT 1;");
                    updateCommand.setString(1, text);
                    updateCommand.setBoolean(2, audience);
                    updateCommand.setString(3, prefix);
                    updateCommand.executeUpdate();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.commands.get(prefix).setText(text);
            this.commands.get(prefix).setAudience(audience);
        }
    }

    public void removeCommand(String message) {    
        String[] msgSplit = message.split("\\s+");
        String prefix = msgSplit[0].substring(2, msgSplit[0].length()).toLowerCase(); // Remove +
        this.commands.remove(prefix);
        try (Connection conn = db.getConnection()) {
            PreparedStatement removeCommand = conn.prepareStatement(""
                    + "DELETE FROM `command`"
                    + "WHERE `commandPrefix` = ?"
                    + "LIMIT 1;");
            removeCommand.setString(1, prefix);
            int n = removeCommand.executeUpdate();
            if (n == 0) {
                throw new SQLException("Remove Command Failed.");
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCommandText(String prefix) {
        if (this.commands.get(prefix) == null){
            return "";
        }
        return this.commands.get(prefix).getText();
    }

    public String outputCommands() {
        String out = "";
        for (String c : commands.keySet()) {
            out = out.concat(", ^" + c);
        }
        return out;
    }

    private Map<String, Command> selectAllCommands() {
        Map<String, Command> c = new HashMap<>();
        try {
            try (Connection conn = db.getConnection()) {
                PreparedStatement selectAllCommands = conn.prepareStatement(""
                        + "SELECT *"
                        + "FROM `command`;");
                try (ResultSet rs = selectAllCommands.executeQuery()) {
                    while (rs.next()) {
                        c.put(rs.getString(2), new Command(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4)));
                    }
                }
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }
}
