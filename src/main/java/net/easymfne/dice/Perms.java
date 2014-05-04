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

import org.bukkit.permissions.Permissible;

/**
 * This method provides a static way to check user permissions.
 * 
 * @author Eric Hildebrand
 */
public class Perms {
    
    /** Is the user allowed to broadcast the results of their roll? */
    public static boolean broadcast(Permissible p) {
        return p.hasPermission("dice.roll.broadcast");
    }
    
    /** Is the user allowed to reload the plugin's configuration? */
    public static boolean canReload(Permissible p) {
        return p.hasPermission("dice.reload");
    }
    
    /** Can the user roll dice with any number of sides? */
    public static boolean canRollAnyDice(Permissible p) {
        return p.hasPermission("dice.roll.any");
    }
    
    /** Can the user roll multiple dice at once? */
    public static boolean canRollMultiple(Permissible p) {
        return p.hasPermission("dice.roll.multiple");
    }
    
}
