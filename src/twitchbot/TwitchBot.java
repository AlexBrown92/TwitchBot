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
