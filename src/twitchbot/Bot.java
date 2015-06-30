/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitchbot;

import commands.Commands;
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
    private Commands commands;

    public Bot(String twitchUser, String channel) {
        this.channel = channel;
        this.username = twitchUser;
        this.setName(this.username);
        this.setMessageDelay(1000);
        this.commands = new Commands();
    }

    @Override
    protected void onUnknown(String line) {
        Message m = new Message(line);
        if (m.getSender() != null) {
            // Mod / Sub Only stuff
            if (m.isMod() || m.isSub()) {
                String toUser = "-> " + m.getSender() + " -> ";
                String[] msgSplit = m.getBody().split(" ");
                if (m.getBody().startsWith("^") || m.getBody().startsWith("!")) {
                    // Standard commands
                    String prefix = msgSplit[0].substring(1, msgSplit[0].length()).toLowerCase();
                    switch (prefix) {
                        case "help":
                            this.sendMessage(this.channel, toUser + "The commands available are: " + commands.outputCommands());
                            break;
                        case "mt":
                            this.sendMessage(this.channel, toUser + "multitwitch.tv/" + this.channel.substring(1) + "/" + msgSplit[1]);
                            break;
                        case "dsn":
                            this.sendMessage(this.channel, toUser + "dualstreamnow.com/dual.htm?stream1=" + this.channel.substring(1) + "&stream2=" + msgSplit[1]);
                            break;
                        case "blacklist":
                        case "blacklistadd":
                            //TODO
                            if (m.isMod()) {
                                this.sendMessage(this.channel, toUser + "Not yet implemented (sorry)");
                            }
                            break;
                        case "blacklistremove":
                            //TODO
                            if (m.isMod()) {
                                this.sendMessage(this.channel, toUser + "Not yet implemented (sorry)");
                            }
                            break;
                        case "blacklistshow":
                        case "blacklistlist":
                        case "blacklistdisplay":
                            //TODO
                            if (m.isMod()) {
                                this.sendMessage(this.channel, toUser + "Not yet implemented (sorry)");
                            }
                            break;
                        case "newsubs":
                            //TODO
                            this.sendMessage(this.channel, toUser + "Not yet implemented (sorry)");
                            break;
                        default:
                            //TODO
                            this.sendMessage(this.channel, toUser + "Not yet implemented (sorry)");
                            break;
                    }
                } else if (m.getBody().startsWith("+") && m.isMod()) {
                    // Add commands
                    String prefix = msgSplit[0].substring(1, msgSplit[0].length()).toLowerCase();
                    //TODO
                    this.sendMessage(this.channel, toUser + "Not yet implemented (sorry)");
                } else if (m.getBody().startsWith("--") && m.isMod()) {
                    // Remove command
                    String prefix = msgSplit[0].substring(2, msgSplit[0].length()).toLowerCase();
                    //TODO
                    this.sendMessage(this.channel, toUser + "Not yet implemented (sorry)");
                } else {
                    // Easter eggs :)
                    switch (m.getBody().toLowerCase()) {
                        case "i love you km_bot":
                            this.sendMessage(this.channel, toUser + "I <3 you too!");
                            break;
                        case "km_bot is a dog":
                            this.sendMessage(this.channel, toUser + "Woof!");
                            break;
                        case "km_bot is a cat":
                            this.sendMessage(this.channel, toUser + "Meow!");
                            break;
                        case "km_bot is a beatle":
                            this.sendMessage(this.channel, toUser + "All you need is love!");
                            break;
                    }
                }

            } // End of Mod/sub only stuff
            // Mod Excluded stuff
            if (!m.isMod()) {

            } // End of Mod Excluded stuff

        }
    }

    @Override
    protected void onDisconnect() {
        try {
            this.reconnect();
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
