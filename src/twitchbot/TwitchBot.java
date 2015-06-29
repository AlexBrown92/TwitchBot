/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitchbot;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex
 */
public class TwitchBot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Properties config = loadConfig();
        Bot kmBot = new Bot(config.getProperty("twitchUser"),config.getProperty("twitchPass"),config.getProperty("ircServer"),config.getProperty("ircPort"),config.getProperty("channel"));

        try {
            kmBot.setVerbose(true);
            kmBot.connect(config.getProperty("ircServer"), Integer.parseInt(config.getProperty("ircPort")), config.getProperty("twitchPass"));
            kmBot.joinChannel(config.getProperty("channel"));
            kmBot.sendRawLine("CAP REQ :twitch.tv/commands");
            kmBot.sendRawLine("CAP REQ :twitch.tv/tags");
        } catch (Exception ex) {
            Logger.getLogger(TwitchBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Properties loadConfig(){
        Properties config = new Properties();
        try {
        FileInputStream in = new FileInputStream("config.properties");
        config.load(in);
        in.close();
        
        } catch (Exception ex){
            Logger.getLogger(TwitchBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return config;
    }
    
}
