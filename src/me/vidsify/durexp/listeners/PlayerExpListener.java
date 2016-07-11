package me.vidsify.durexp.listeners;

import me.vidsify.durexp.DurexpPlugin;
import me.vidsify.durexp.Perm;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Main plugin listener class
 * @author Vidsify
 */
public class PlayerExpListener implements Listener {
    private DurexpPlugin plugin;

    public PlayerExpListener(DurexpPlugin reference) {
        this.plugin = reference;
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        if(!plugin.getConfig().getBoolean("Enable")) return; // Don't waste computation power if we're not even enabled

        // Gather config variables
        List<String> enabledDays = plugin.getConfig().getStringList("DaysToEnable");
        int radius = plugin.getConfig().getInt("CheckRadius");

        // Get today's day string (US locale) and check if it's enabled in the config, return if it's not
        String currentDay = Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
        boolean enabled = false;
        for(String day : enabledDays) {
            if(day.toLowerCase().contains(currentDay.toLowerCase())) {
                enabled = true;
                break;
            }
        }
        if(!enabled) return; // Not enabled for today

        // Check for spawners nearby and return if we find one
        if(plugin.getConfig().getBoolean("CheckForSpawner")
                && plugin.isLocationNearBlock(event.getPlayer().getLocation(), Material.MOB_SPAWNER, radius)) return;

        // Multiply the experience gain
        Player player = event.getPlayer();
        int originalAmount = event.getAmount();
        int newAmount = originalAmount;
        boolean found = false;
        if (plugin.getConfig().getBoolean("EnablePermMultiplier")) {
            /* TODO:
             * Consider making this go backwards so a permission with a higher multiplier
             * will be caught before a permission with a lower multiplier. This is useful
             * for servers which use inherited permissions. - Exloki
             */
            for (float temp = 0.0F; temp <= 10.0F; temp += 0.1F) {
                if (!player.isOp() && player.hasPermission(Perm.MULTIPLIER + temp)) {
                    newAmount = (int) (originalAmount * temp);
                    found = true;
                    break;
                }
            }
        }
        if (!found && player.hasPermission(Perm.ALLOW)) {
            newAmount = (int) (originalAmount * plugin.getConfig().getDouble("Multiplier"));
        }

        event.setAmount(newAmount);
     }
}