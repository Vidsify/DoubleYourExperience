package me.vidsify.durexp.commands;

import me.vidsify.durexp.DurexpPlugin;
import me.vidsify.durexp.Perm;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Main plugin command executor class
 * @author Vidsify
 */
public class DurexpExecutor implements CommandExecutor {
    private static final String PREFIX_RED = ChatColor.GOLD + "[DurEXP] " + ChatColor.RED;
    private static final String PREFIX_GREEN = ChatColor.GOLD + "[DurEXP] " + ChatColor.GREEN;

    private DurexpPlugin plugin;

    public DurexpExecutor(DurexpPlugin reference) {
        this.plugin = reference;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission(Perm.ADMIN)) {
            sender.sendMessage(PREFIX_RED + "You do not have permission for this command!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(PREFIX_RED + "Use /durexp help, for more information");
        } // Help Command
        else if (args[0].equalsIgnoreCase("help")) {
            help(sender);
        } // Multiplier Command
        else if (args[0].equalsIgnoreCase("multiplier")) {
            multiplier(sender, args);
        } // Toggle Command
        else if (args[0].equalsIgnoreCase("toggle")) {
            toggle(sender);
        } //Update Command
        else if (args[0].equalsIgnoreCase("update")) {
            update(sender);
        } // Reload Command
        else if (args[0].equalsIgnoreCase("reload")) {
        	plugin.reloadConfig();
        	plugin.saveConfig();
        	sender.sendMessage(PREFIX_GREEN + "Config has been Reloaded!");
        } // Spawner Command
        else if (args[0].equalsIgnoreCase("spawner")) {
            spawner(sender);
        } else {
            sender.sendMessage(PREFIX_RED + "Invalid command");
        }

        return true;
    }

    // Help Command
    private void help(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "===================" + ChatColor.DARK_PURPLE + " DurEXP By Vidsify " + ChatColor.GOLD + "================");
        sender.sendMessage(ChatColor.GOLD + "/durexp help" + ChatColor.RED + " - Displays this help menu");
        sender.sendMessage(ChatColor.GOLD + "/durexp toggle" + ChatColor.RED + " - Toggles this plugin on or off");
        sender.sendMessage(ChatColor.GOLD + "/durexp multiplier <amount>" + ChatColor.RED + " - Amount xp is multiplied by, e.g. 2 is double");
        sender.sendMessage(ChatColor.GOLD + "/durexp update" + ChatColor.RED + " - Toggles Updater");
        sender.sendMessage(ChatColor.GOLD + "/durexp reload" + ChatColor.RED + " - Reloads Config if editted manually");
        sender.sendMessage(ChatColor.GOLD + "/durexp spawner" + ChatColor.RED + " - Toggles CheckForSpawner on or off");
        sender.sendMessage(ChatColor.GOLD + "=====================================================");
    }

    // Multiplier Command
    private void multiplier(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(PREFIX_RED + "You must specify a number!");
            return;
        }

        if (plugin.isDouble(args[1])) {
            plugin.getConfig().set("Multiplier", Double.parseDouble(args[1]));
            sender.sendMessage(PREFIX_RED + "Multiplier set to: " + args[1]);
            plugin.saveConfig();
        } else {
            sender.sendMessage(PREFIX_RED + "Only enter a number!");
        }
    }

    // Toggle Command
    private void toggle(CommandSender sender) {
        plugin.getConfig().set("Enable", !plugin.getConfig().getBoolean("Enable"));
        plugin.saveConfig();
        sender.sendMessage(plugin.getConfig().getBoolean("Enable") ? PREFIX_GREEN + "Plugin enabled!" : PREFIX_RED + "Plugin disabled!");
    }

    // Update Command
    private void update(CommandSender sender) {
        plugin.getConfig().set("AutoUpdate", !plugin.getConfig().getBoolean("AutoUpdate"));
        plugin.saveConfig();
        sender.sendMessage(plugin.getConfig().getBoolean("AutoUpdate") ? PREFIX_GREEN + "AutoUpdate enabled!" : PREFIX_RED + "AutoUpdate disabled!");
    }

    // Spawner Command
    private void spawner(CommandSender sender) {
        plugin.getConfig().set("CheckForSpawner", !plugin.getConfig().getBoolean("CheckForSpawner"));
        plugin.saveConfig();
        sender.sendMessage(plugin.getConfig().getBoolean("CheckForSpawner") ? PREFIX_GREEN + "Checking for Spawner enabled!" : PREFIX_RED + "Checking for Spawner disabled!");
    }
}
