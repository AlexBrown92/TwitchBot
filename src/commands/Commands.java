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
    
    private Map<String, Command> selectAllCommands(){
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(Commands.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    public void addCommand() {

    }

    public void removeCommand() {

    }

    public String getCommand() {
        return null;
    }

    public String outputCommands() {
        String out = "";
        for (String c : commands.keySet()) {
            out = out.concat(", ^" + c);
        }
        return out;
    }

}
