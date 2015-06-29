/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitchbot;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.Message;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

/**
 *
 * @author Alex
 */
public class Bot extends PircBot {

    private String channel;
    private String username;
    private String pass;
    private String server;
    private int port;
    private boolean verbose;

    public Bot(String twitchUser, String twitchPass, String server, String port, boolean verbose, String channel) {
        this.channel = channel;
        this.username = twitchUser;
        this.pass = twitchPass;
        this.server = server;
        this.port = Integer.parseInt(port);
        initBot();
    }

    private void initBot() {
        this.setName(this.username);
        this.setVerbose(this.verbose);
        this.setMessageDelay(1000);
        try {
            this.connect(this.server, this.port, this.pass);
            this.joinChannel(this.channel);
            this.sendRawLine("CAP REQ :twitch.tv/commands");
            this.sendRawLine("CAP REQ :twitch.tv/tags");
        } catch (Exception ex) {
            Logger.getLogger(TwitchBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void onUnknown(String line) {
        Message m = new Message(line);
        m.toString();
    }

    @Override
    protected void onDisconnect() {
        try {
            this.reconnect();
            initBot();
            System.out.println("Reconnected");
        } catch (IOException ex) {
            System.out.println("Unable to reconnect: " + ex.getMessage());
            Logger.getLogger(TwitchBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IrcException ex) {
            System.out.println("Unable to reconnect: " + ex.getMessage());
            Logger.getLogger(TwitchBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
