package me.vidsify.durexp;

import me.vidsify.durexp.commands.DurexpExecutor;
import me.vidsify.durexp.gravity.Updater;
import me.vidsify.durexp.listeners.PlayerExpListener;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author Vidsify
 */
public class DurexpPlugin extends JavaPlugin implements Listener {
    
    public Permission permission;
    public Economy economy;
    public Chat chat;
    private static final int bukkitId = 80039;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        PluginDescriptionFile pdffile = getDescription();
        getServer().getPluginManager().registerEvents(new PlayerExpListener(this), this);
        getCommand("durexp").setExecutor(new DurexpExecutor(this));
        
        //AutoUpdater
        if (getConfig().getBoolean("AutoUpdate", false)) {
            Updater localUpdater = new Updater(this, bukkitId, getFile(), Updater.UpdateType.DEFAULT, true);
        } else {
            getLogger().info("AutoUpdate is turned off.");
        }
        
        //Load Vault
        setupPermissions();
        setupEconomy();
        setupChat();
        
        getLogger().info("Linked to Vault!");
        getLogger().info("by Vidsify");
        getLogger().info("" + pdffile.getVersion() + " has been enabled()");
    }

    @Override
    public void onDisable() {
        reloadConfig();//If this isnt here, the plugin will not save config changes a user has made, unless you reload the config in game with a command, thus reverting(which has been your glitch)
        saveConfig();//if reload isnt above, you will overwrite all changes a user has made and essentially rollback their changes
    }

    public void loadConfiguration() {
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer()
                .getServicesManager().getRegistration(
                        net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer()
                .getServicesManager().getRegistration(
                        net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer()
                .getServicesManager().getRegistration(
                        net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    @SuppressWarnings("deprecation")
    public boolean isLocationNearBlock(Location loc, Material type, int radius) {
        
        int x1 = loc.getBlockX() - radius;
        int y1 = loc.getBlockY() - radius;
        int z1 = loc.getBlockZ() - radius;
        
        int x2 = loc.getBlockX() + radius;
        int y2 = loc.getBlockY() + radius;
        int z2 = loc.getBlockZ() + radius;
        
        for (int x = x1; x <= x2; x++) {
            for (int y = y1; y <= y2; y++) {
                for (int z = z1; z <= z2; z++) {
                    Block block = loc.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == type) {
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
        }
        return false;
    }
}
