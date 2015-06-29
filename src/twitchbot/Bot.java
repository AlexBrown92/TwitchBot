/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitchbot;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jibble.pircbot.PircBot;

/**
 *
 * @author Alex
 */
public class Bot extends PircBot{

    public Bot(String twitchUser, String twitchPass, String server, String port, boolean verbose, String channel) {
        this.setName(twitchUser);
        this.setVerbose(verbose);
        this.setMessageDelay(1000);
        try {
            this.connect(server, Integer.parseInt(port), twitchPass);
            this.joinChannel(channel);
            this.sendRawLine("CAP REQ :twitch.tv/commands");
            this.sendRawLine("CAP REQ :twitch.tv/tags");
        } catch (Exception ex){
            Logger.getLogger(TwitchBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
