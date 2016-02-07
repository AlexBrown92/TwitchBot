/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static twitchbot.TwitchBot.db;


/**
 *
 * @author Alex
 */
public class Message {
    private String sender;
    private boolean sub;
    private boolean mod;
    private String channel;
    private int timestamp;
    private String body;
    private final String FLAG_MOD = "mod=1";
    private final String FLAG_SUBSCRIBER = "subscriber=1";
    private final String FLAG_TURBO = "turbo=1";

    public Message(String sender, String channel, int timestamp, String body) {
        this.sender = sender;
        this.channel = channel;
        this.timestamp = timestamp;
        this.body = body;
    }

    public Message(String wholeText) {
        // Positions in msgSplit
        int INFO = 0;
        int CHANNEL = 3;
        int BODY_START = 4;
        
        // Positions in infoSplit
        int NAME = 1;
        int SUB = 3;
        int MOD = 5;
        
        String[] msgSplit = wholeText.split(" ");
        if (wholeText.startsWith("@color=")){
            String[] infoSplit = msgSplit[INFO].split(";");
            this.sender = infoSplit[NAME].substring(13);
            this.sub = msgSplit[INFO].contains(FLAG_SUBSCRIBER);
            this.mod = msgSplit[INFO].contains(FLAG_MOD);
            this.channel = msgSplit[CHANNEL];
            if (msgSplit.length > BODY_START){
                this.body = msgSplit[BODY_START].substring(1);
                for (int i = BODY_START+1; i < msgSplit.length; i++) {
                    this.body = this.body.concat(" " + msgSplit[i]);
                }
            } else {
                this.body = "";
            }
            logMessage();
        }
    }
    
    private void logMessage(){
        try (Connection conn = db.getConnection()) {
            PreparedStatement addMessage = conn.prepareStatement(""
                    + "INSERT INTO `message`(`messageSender`,`messageSub`,`messageMod`, `messageBody`)"
                    + "VALUES(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            addMessage.setString(1, this.sender);
            addMessage.setBoolean(2, this.sub);
            addMessage.setBoolean(3, this.mod);
            addMessage.setString(4, this.body);
            addMessage.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isSub() {
        return sub;
    }

    public boolean isMod() {
        return mod;
    }
    
    public String getSender() {
        return sender;
    }

    public String getChannel() {
        return channel;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getBody() {
        return body;
    }
    
}
