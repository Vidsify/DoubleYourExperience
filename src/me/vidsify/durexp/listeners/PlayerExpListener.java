package me.vidsify.durexp.listeners;

import java.util.Calendar;
import java.util.List;
import me.vidsify.durexp.DurexpPlugin;
import me.vidsify.durexp.Perm;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

/**
 *
 * @author Vidsify
 */
public class PlayerExpListener implements Listener {

    DurexpPlugin plugin;

    public PlayerExpListener(DurexpPlugin reference) {
        this.plugin = reference;
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent e) {
        Calendar mydate = Calendar.getInstance();
        int dow = mydate.get(7);
        Boolean alreadyDone = Boolean.valueOf(false);
        List<String> EnabledDays = plugin.getConfig().getStringList("DaysToEnable");
        int radius = plugin.getConfig().getInt("CheckRadius");
        for (String Day : EnabledDays) {
            int currentDay = 0;
            if (Day.toLowerCase().contains("mon")) {
                currentDay = 2;
            } else if (Day.toLowerCase().contains("tue")) {
                currentDay = 3;
            } else if (Day.toLowerCase().contains("wed")) {
                currentDay = 4;
            } else if (Day.toLowerCase().contains("thu")) {
                currentDay = 5;
            } else if (Day.toLowerCase().contains("fri")) {
                currentDay = 6;
            } else if (Day.toLowerCase().contains("sat")) {
                currentDay = 7;
            } else if (Day.toLowerCase().contains("sun")) {
                currentDay = 1;
            }
            if ((dow == currentDay) && (!alreadyDone.booleanValue())
                    && (plugin.getConfig().getBoolean("Enable"))) {
                if (plugin.getConfig().getBoolean("CheckForSpawner")) {
                    if (!plugin.isLocationNearBlock(e.getPlayer().getLocation(), Material.MOB_SPAWNER, radius)) {
                        alreadyDone = Boolean.valueOf(true);
                        Player player = e.getPlayer();
                        int originalAmount = e.getAmount();
                        int newAmount = originalAmount;
                        if (plugin.getConfig().getBoolean("EnablePermMultiplier")) {
                            boolean found = false;
                            for (float temp = 0.0F; temp <= 10.0F; temp = (float) (temp + 0.1D)) {
                                if (!player.isOp() && player.hasPermission(Perm.MULTIPLIER + temp)) {
                                    newAmount = (int) (originalAmount * temp);
                                    found = true;
                                }
                            }
                            if ((!found)
                                    && (player.hasPermission(Perm.ALLOW))) {
                                newAmount = (int) (originalAmount * plugin.getConfig().getDouble("Multiplier"));
                            }
                        } else if (player.hasPermission(Perm.ALLOW)) {
                            newAmount = (int) (originalAmount * plugin.getConfig().getDouble("Multiplier"));
                        }
                        e.setAmount(newAmount);
                    }
                } else {
                    alreadyDone = Boolean.valueOf(true);
                    Player player = e.getPlayer();
                    int originalAmount = e.getAmount();
                    int newAmount = originalAmount;
                    if (plugin.getConfig().getBoolean("EnablePermMultiplier")) {
                        boolean found = false;
                        for (float temp = 0.0F; temp <= 10.0F; temp = (float) (temp + 0.1D)) {
                            if (!player.isOp() && player.hasPermission(Perm.MULTIPLIER + temp)) {
                                newAmount = (int) (originalAmount * temp);
                                found = true;
                            }
                        }
                        if ((!found)
                                && (player.hasPermission(Perm.ALLOW))) {
                            newAmount = (int) (originalAmount * plugin.getConfig().getDouble("Multiplier"));
                        }
                    } else if (player.hasPermission(Perm.ALLOW)) {
                        newAmount = (int) (originalAmount * plugin.getConfig().getDouble("Multiplier"));
                    }
                    e.setAmount(newAmount);
                }
            }
        }
    }

}
