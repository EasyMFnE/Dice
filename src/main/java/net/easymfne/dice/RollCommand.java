/*
 * This file is part of the Dice plugin by EasyMFnE.
 * 
 * Dice is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 * 
 * Dice is distributed in the hope that it will be useful, but without any
 * warranty; without even the implied warranty of merchantability or fitness for
 * a particular purpose. See the GNU General Public License for details.
 * 
 * You should have received a copy of the GNU General Public License v3 along
 * with Dice. If not, see <http://www.gnu.org/licenses/>.
 */
package net.easymfne.dice;

import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The class that handles all console and player commands for the plugin.
 * 
 * @author Eric Hildebrand
 */
public class RollCommand implements CommandExecutor {
    
    private Dice plugin = null;
    private Random random;
    
    /**
     * Instantiate by getting a reference to the plugin instance, creating a new
     * Random, and registering this class to handle the '/roll' command.
     * 
     * @param plugin
     *            Reference to Dice plugin instance
     */
    public RollCommand(Dice plugin) {
        this.plugin = plugin;
        random = new Random();
        plugin.getCommand("roll").setExecutor(this);
    }
    
    /**
     * Broadcast the results of a dice roll to the players of the server.
     * Configuration can be set so that messages are only set within the world
     * that the player resides, and also within a certain distance of them. Dice
     * rolled by non-players (e.g. the Console) are sent to all players.
     * 
     * @param sender
     *            The user rolling the dice
     * @param message
     *            The fully-formatted message to display
     */
    private void broadcast(CommandSender sender, String message) {
        if (message == null) {
            return;
        }
        Player p1 = (sender instanceof Player ? (Player) sender : null);
        
        if (plugin.getPluginConfig().isLogging()) {
            plugin.getLogger().info(message);
        }
        
        for (Player p2 : plugin.getServer().getOnlinePlayers()) {
            if (plugin.getPluginConfig().isCrossworld() || p1 == null
                    || p1.getWorld() == p2.getWorld()) {
                if (plugin.getPluginConfig().getBroadcastRange() < 0
                        || p1 == null
                        || getDSquared(p1, p2) < square(plugin
                                .getPluginConfig().getBroadcastRange())) {
                    p2.sendMessage(message);
                }
            }
        }
    }
    
    /**
     * Release the '/roll' command from its ties to this class.
     */
    public void close() {
        plugin.getCommand("roll").setExecutor(null);
    }
    
    /**
     * Format and return a String that will be used to display the roll results.
     * This method replaces tags: {PLAYER}, {RESULT}, {COUNT}, {SIDES}, {TOTAL}.
     * This method also replaces '&' style color codes with proper ChatColors.
     * 
     * @param sender
     *            The user that rolled the dice
     * @param roll
     *            The results of the roll, as an array
     * @param sides
     *            The number of sides on the dice
     * @return The fancy-formatted message
     */
    private String formatString(CommandSender sender, Integer[] roll, int sides) {
        String result;
        if (Perms.broadcast(sender)) {
            result = plugin.getPluginConfig().getBroadcastMessage();
        } else {
            result = plugin.getPluginConfig().getPrivateMessage();
        }
        if (result == null || result.length() == 0) {
            return null;
        }
        result = result.replaceAll("\\{PLAYER}", sender.getName());
        result = result.replaceAll("\\{RESULT}", StringUtils.join(roll, ", "));
        result = result.replaceAll("\\{COUNT}", "" + roll.length);
        result = result.replaceAll("\\{SIDES}", "" + sides);
        result = result.replaceAll("\\{TOTAL}", "" + sum(roll));
        return ChatColor.translateAlternateColorCodes('&', result);
    }
    
    /**
     * Get the squared distance between two players.
     * 
     * @param p1
     *            Player one
     * @param p2
     *            Player two
     * @return The distance^2
     */
    private int getDSquared(Player p1, Player p2) {
        int dx = p1.getLocation().getBlockX() - p2.getLocation().getBlockX();
        int dy = p1.getLocation().getBlockY() - p2.getLocation().getBlockY();
        int dz = p1.getLocation().getBlockZ() - p2.getLocation().getBlockZ();
        return dx * dx + dy * dy + dz * dz;
    }
    
    /**
     * Show the results of a roll to a player privately.
     * 
     * @param sender
     *            The user rolling the dice
     * @param message
     *            The fully-formatted message to display
     */
    private void message(CommandSender sender, String message) {
        if (message == null) {
            return;
        }
        sender.sendMessage(message);
    }
    
    /**
     * This method handles user commands. Usage: "/roll <help,reload>" which
     * either shows help or reloads config. Usage: "/roll [count] [d<sides>]"
     * where the order of the arguments does not matter, but the number of sides
     * must be prefixed with 'd'.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")
                    || args[0].equalsIgnoreCase("?")) {
                showHelp(sender);
                return true;
            }
            if (Perms.canReload(sender) && args[0].equalsIgnoreCase("reload")) {
                plugin.reload();
                sender.sendMessage("Configuration reloaded");
                return true;
            }
        }
        
        int count = plugin.getPluginConfig().getDefaultCount();
        int sides = plugin.getPluginConfig().getDefaultSides();
        
        /* Check for arguments representing dice count */
        if (args.length > 0 && Perms.canRollMultiple(sender)) {
            for (String arg : args) {
                if (arg.matches("^[0-9]+$")) {
                    count = Integer.parseInt(arg);
                    break;
                }
            }
        }
        /* Check for arguments representing dice sides */
        if (args.length > 0 && Perms.canRollAnyDice(sender)) {
            for (String arg : args) {
                if (arg.matches("^d[0-9]+$")) {
                    sides = Integer.parseInt(arg.substring(1));
                    break;
                }
            }
        }
        
        /* Check the loaded or parsed values against the defined maximums. */
        if (count > plugin.getPluginConfig().getMaximumCount()) {
            sender.sendMessage(ChatColor.RED
                    + "You can't roll that many dice at once");
            return false;
        }
        if (sides > plugin.getPluginConfig().getMaximumSides()) {
            sender.sendMessage(ChatColor.RED
                    + "You can't roll dice with that many sides");
            return false;
        }
        
        /* Roll the dice and handle the outcome */
        roll(sender, Math.max(1, count), Math.max(2, sides));
        return true;
    }
    
    /**
     * Roll a set of dice for a user, and either broadcast the results publicly
     * or send them privately, depending on the user's permissions.
     * 
     * @param sender
     *            The user rolling the dice
     * @param count
     *            The number of dice to roll
     * @param sides
     *            The number of sides per die
     */
    private void roll(CommandSender sender, int count, int sides) {
        Integer[] result = new Integer[count];
        for (int i = 0; i < count; i++) {
            result[i] = random.nextInt(sides) + 1;
        }
        if (Perms.broadcast(sender)) {
            broadcast(sender, formatString(sender, result, sides));
        } else {
            message(sender, formatString(sender, result, sides));
        }
    }
    
    /**
     * Show personalized usage help to the user, taking into account his or her
     * permissions.
     * 
     * @param sender
     *            The user to help
     */
    private void showHelp(CommandSender sender) {
        /* Treat the pair of booleans as 2^0 and 2^1 bits */
        int perms = (Perms.canRollMultiple(sender) ? 1 : 0)
                + (Perms.canRollAnyDice(sender) ? 2 : 0);
        switch (perms) {
        case 1:
            sender.sendMessage(ChatColor.RED + "Usage: /roll [count]");
            return;
        case 2:
            sender.sendMessage(ChatColor.RED + "Usage: /roll [d<sides>]");
            return;
        case 3:
            sender.sendMessage(ChatColor.RED
                    + "Usage: /roll [count] [d<sides>]");
            return;
        default:
            sender.sendMessage(ChatColor.RED + "Usage: /roll");
        }
    }
    
    /**
     * Square an input. Useful for decluttering the code.
     * 
     * @param input
     *            The number to be squared
     * @return The result
     */
    private int square(int input) {
        return input * input;
    }
    
    /**
     * Calculate the sum of an array of numbers.
     * 
     * @param roll
     *            The array of numbers
     * @return The sum
     */
    private int sum(Integer[] roll) {
        int t = 0;
        for (int i : roll) {
            t += i;
        }
        return t;
    }
    
}
