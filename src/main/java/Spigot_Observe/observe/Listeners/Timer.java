package Spigot_Observe.observe.Listeners;

import Spigot_Observe.observe.Configurators.Config;
import Spigot_Observe.observe.Plugin.Observe;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable
{
    Observe plugin;
    Config config;

    public Timer(Observe _plugin, Config _config) {
        this.plugin = _plugin;
        this.config = _config;
    }

    @Override
    public void run() {

    }
}
