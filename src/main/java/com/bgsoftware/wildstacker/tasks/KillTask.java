package com.bgsoftware.wildstacker.tasks;

import com.bgsoftware.wildstacker.Locale;
import com.bgsoftware.wildstacker.WildStackerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class KillTask extends BukkitRunnable {

    private static WildStackerPlugin plugin = WildStackerPlugin.getPlugin();

    private static int taskID = -1;
    private long timeLeft;

    private KillTask(){
        timeLeft = plugin.getSettings().killTaskInterval;
        taskID = runTaskTimerAsynchronously(plugin, 20L,20L).getTaskId();
    }

    public static void start(){
        if(Bukkit.getScheduler().isCurrentlyRunning(taskID) || Bukkit.getScheduler().isQueued(taskID))
            Bukkit.getScheduler().cancelTask(taskID);
        new KillTask();
    }

    @Override
    public void run() {
        if (plugin.getSettings().killTaskInterval > 0) {
            if (timeLeft == 0) {
                if (Bukkit.getOnlinePlayers().size() > 0) {
                    plugin.getSystemManager().performKillAll();
                    for(Player player : Bukkit.getOnlinePlayers())
                        Locale.KILL_ALL_ANNOUNCEMENT.send(player);
                }

                timeLeft = plugin.getSettings().killTaskInterval;
                return;
            }

            if (timeLeft == 10 || timeLeft == 30 || timeLeft == 60) {
                for(Player player : Bukkit.getOnlinePlayers())
                    Locale.KILL_ALL_REMAINING_TIME.send(player, timeLeft);
            }

            timeLeft -= 1;
        }
    }

}
