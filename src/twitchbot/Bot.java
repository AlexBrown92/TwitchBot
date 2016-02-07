/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitchbot;

import blacklist.Blacklist;
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
    private Blacklist blacklist;

    public Bot(String twitchUser, String channel) {
        this.channel = channel;
        this.username = twitchUser;
        this.setName(this.username);
        this.setMessageDelay(1000);
        this.commands = new Commands();
        this.blacklist = new Blacklist();
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
                            this.outputMessage(this.channel, toUser + "The commands available are: " + commands.outputCommands());
                            break;
                        case "mt":
                            this.outputMessage(this.channel, toUser + "multitwitch.tv/" + this.channel.substring(1) + "/" + msgSplit[1]);
                            break;
                        case "dsn":
                            this.outputMessage(this.channel, toUser + "dualstreamnow.com/dual.htm?stream1=" + this.channel.substring(1) + "&stream2=" + msgSplit[1]);
                            break;
                        case "blacklist":
                        case "blacklistadd":
                            if (m.isMod()) {
                                blacklist.addBlacklist(m.getBody());
                                this.outputMessage(this.channel, toUser + "Blacklist updated");
                            }
                            break;
                        case "blacklistremove":
                            if (m.isMod()) {
                                blacklist.removeBlacklist(m.getBody());
                                this.outputMessage(this.channel, toUser + "Blacklist updated");
                            }
                            break;
                        case "blacklistshow":
                        case "blacklistlist":
                        case "blacklistdisplay":
                            //TODO
                            if (m.isMod()) {
                                this.outputMessage(this.channel, toUser + "Blacklist: " + blacklist.outputBlacklist());
                            }
                            break;
                        case "newsubs":
                            //TODO
                            this.outputMessage(this.channel, toUser + "Not yet implemented (sorry)");
                            break;
                        case "p":
                            if (m.isMod()){
                                String target = msgSplit[1];
                                if (target.startsWith("@")){
                                    target = target.substring(1);
                                }
                                this.timeout(this.channel, target, "Moderator Decision", 1);
                            }
                        default:
                            String out = commands.getCommandText(prefix);
                            if ((out != null)&& (!out.isEmpty())) {
                                this.outputMessage(channel, toUser + out);
                            }
                            break;
                    }
                } else if (m.getBody().startsWith("++") && m.isMod()) {
                    // Add commands
                    String prefix = msgSplit[0].substring(2, msgSplit[0].length()).toLowerCase();
                    commands.addCommand(m.getBody());
                    this.outputMessage(this.channel, toUser + "Command Added: (^"+prefix+") " + commands.getCommandText(prefix));
                } else if (m.getBody().startsWith("--") && m.isMod()) {
                    // Remove command
                    String prefix = msgSplit[0].substring(2, msgSplit[0].length()).toLowerCase();
                    commands.removeCommand(m.getBody());
                    this.outputMessage(this.channel, toUser + "Command Removed: ^"+prefix);
                } else {
                    // Easter eggs :)
                    switch (m.getBody().toLowerCase()) {
                        case "i love you km_bot":
                            this.outputMessage(this.channel, toUser + "I <3 you too!");
                            break;
                        case "km_bot is a dog":
                            this.outputMessage(this.channel, toUser + "Woof!");
                            break;
                        case "km_bot is a cat":
                            this.outputMessage(this.channel, toUser + "Meow!");
                            break;
                        case "km_bot is a beatle":
                            this.outputMessage(this.channel, toUser + "All you need is love!");
                            break;
                        case "km_bot is cameron poe":
                            this.outputMessage(this.channel, toUser + "Put the bunny back in the box!");
                            break;
                        case "what is love?":
                            this.outputMessage(this.channel, toUser + "Baby don't hurt me, don't hurt me, no more.");
                            break;
                    }
                }

            } // End of Mod/sub only stuff
            // Mod Excluded stuff
            if (!m.isMod()) {
                if (blacklist.containsBlacklistedTerm(m.getBody())){
                    this.timeout(this.channel, m.getSender(), "Your message contained a word that has been blacklisted.", 1);
                }
            } // End of Mod Excluded stuff

        }
    }
    
    private void timeout(String channel, String name, String reason, int duration) {
        this.sendMessage(channel, ".timeout " + name + " " + duration);
        this.sendMessage(channel, "Timeout (" + duration + "s) ->" + name + "-> " + reason);
    }

    private void outputMessage(String channel, String text) {
        //TODO - Logging
        this.sendMessage(channel, text);
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
