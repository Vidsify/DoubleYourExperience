package me.vidsify.durexp;

import me.vidsify.durexp.commands.DurexpExecutor;
import me.vidsify.durexp.gravity.Updater;
import me.vidsify.durexp.listeners.PlayerExpListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class
 * @author Vidsify
 */
public class DurexpPlugin extends JavaPlugin implements Listener {
    private Permission permission;
    private Economy economy;
    private Chat chat;

    @Override
    public void onEnable() {
        // Configuration
        getConfig().options().copyDefaults(true);
        saveConfig();

        // Listener & Command registration
        getServer().getPluginManager().registerEvents(new PlayerExpListener(this), this);
        getCommand("durexp").setExecutor(new DurexpExecutor(this));
        
        // AutoUpdater
        if (getConfig().getBoolean("AutoUpdate", false)) {
			Updater localUpdater = new Updater(this, 80039, getFile(), Updater.UpdateType.DEFAULT, true);
        } else {
            getLogger().info("AutoUpdate is turned off.");
        }
        
        // Load Vault
        setupPermissions();
        setupEconomy();
        setupChat();
        
        // Console Enable Info
        getLogger().info("Linked to Vault!");
        getLogger().info("by Vidsify");
        getLogger().info("" + getDescription().getVersion() + " has been enabled");
    }

    @Override
    public void onDisable() {
        reloadConfig();
        saveConfig();
    
        // Console Disable Info
        PluginDescriptionFile pdffile = getDescription();
        getLogger().info("by Vidsify");
        getLogger().info("" + pdffile.getVersion() + " has been disabled");
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer()
                .getServicesManager()
                .getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer()
                .getServicesManager()
                .getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer()
                .getServicesManager()
                .getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public boolean isLocationNearBlock(Location loc, Material type, int radius) {
        for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
            for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; y++) {
                for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                    if (loc.getWorld().getBlockAt(x, y, z).getType() == type) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
