package me.vidsify.durexp.commands;

import me.vidsify.durexp.DurexpPlugin;
import me.vidsify.durexp.Perm;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Vidsify
 */
public class DurexpExecutor implements CommandExecutor {

    DurexpPlugin plugin;

    public DurexpExecutor(DurexpPlugin reference) {
        this.plugin = reference;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission(Perm.ADMIN)) {
            sender.sendMessage(ChatColor.GOLD + "[DurEXP]" + ChatColor.RED + "You do not have permission for this command!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "[DurEXP]" + ChatColor.RED + "Use /durexp help, for more information");
        } // Help Command
        else if (args[0].equalsIgnoreCase("help")) {
            return help(sender);
        } //Multipler Command
        else if (args[0].equalsIgnoreCase("multiplier")) {
            return multiplier(sender, args);
        } //Toggle Command
        else if (args[0].equalsIgnoreCase("toggle")) {
            return toggle(sender);
        } //Update Command
        else if (args[0].equalsIgnoreCase("update")) {
            return update(sender);
        } 
        else if (args[0].equalsIgnoreCase("reload")) {
        	plugin.reloadConfig();
        	plugin.saveConfig();
        	sender.sendMessage(ChatColor.GOLD + "[DurEXP]" + ChatColor.GREEN + " Config Reloaded!");
            return true;
        } else {
            sender.sendMessage(ChatColor.GOLD + "[DUREXP]" + ChatColor.RED + " Invalid command");
        }
        return true;
    }
    
    public boolean help(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "===================" + ChatColor.DARK_PURPLE + " DoubleXP By Vidsify " + ChatColor.GOLD + "================");
        sender.sendMessage(ChatColor.GOLD + "/durexp help" + ChatColor.RED + " - Displays this help menu");
        sender.sendMessage(ChatColor.GOLD + "/durexp toggle" + ChatColor.RED + " - Toggles this plugin on or off");
        sender.sendMessage(ChatColor.GOLD + "/durexp multiplier <amount>" + ChatColor.RED + " - Amount xp is multiplied by, e.g. 2 is double");
        sender.sendMessage(ChatColor.GOLD + "=====================================================");
        return true;
    }
    
    public boolean multiplier(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.GOLD + "[DurEXP]" + ChatColor.RED + " You must specify a number!");
            return false;
        }
        if (plugin.isDouble(args[1])) {
            plugin.getConfig().set("Multiplier", Double.valueOf(Double.parseDouble(args[1])));
            sender.sendMessage(ChatColor.GOLD + "[DurEXP]" + ChatColor.RED + "Multiplier set to: " + args[1]);
            plugin.saveConfig();
        } else {
            sender.sendMessage(ChatColor.GOLD + "[DurEXP]" + ChatColor.RED + " Only enter a number!");
        }
        return true;
    }

    public boolean toggle(CommandSender sender) {
        if (plugin.getConfig().getBoolean("Enable")) {
            plugin.getConfig().set("Enable", Boolean.valueOf(false));
            sender.sendMessage(ChatColor.GOLD + "[DurEXP]" + ChatColor.RED + " Plugin disabled!");
        } else {
            plugin.getConfig().set("Enable", Boolean.valueOf(true));
            sender.sendMessage(ChatColor.GOLD + "[DurEXP]" + ChatColor.GREEN + " Plugin enabled!");
        }
        return true;
    }

    public boolean update(CommandSender sender) {
        if (plugin.getConfig().getBoolean("AutoUpdate")) {
            plugin.getConfig().set("AutoUpdate", Boolean.valueOf(false));
            sender.sendMessage(ChatColor.GOLD + "[DurEXP]" + ChatColor.RED + " AutoUpdate Disabled!");
        } else {
            plugin.getConfig().set("AutoUpdate", Boolean.valueOf(true));
            sender.sendMessage(ChatColor.GOLD + "[DurEXP]" + ChatColor.GREEN + " AutoUpdate Enabled!");
        }
        plugin.saveConfig();
        return true;
    }

}
