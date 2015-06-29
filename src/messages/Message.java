/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

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
            this.sub = infoSplit[SUB].endsWith("1");
            this.mod = infoSplit[MOD].endsWith("mod");
            this.channel = msgSplit[CHANNEL];
            this.body = msgSplit[BODY_START].substring(1);
            for (int i = BODY_START+1; i < msgSplit.length; i++) {
                this.body = this.body.concat(" " + msgSplit[i]);
            }
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
