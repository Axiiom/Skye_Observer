package Spigot_Observe.observe.Configurators;

import Spigot_Observe.observe.Main.PluginHead;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;


public class Cooldowns
{
    private PluginHead plugin;
    private Config config;
    private HashMap<UUID, Long> players_on_cooldown;
    private HashMap<UUID, Long> time_until_kick;


    public Cooldowns(Config _config, PluginHead _plugin) {
        config = _config;
        plugin = _plugin;
        players_on_cooldown = new HashMap<>();
        time_until_kick = new HashMap<>();
    }

    public void add(Player _player)
    {
        long global_cd = config.getCooldownTime();
        long total_cd  = global_cd*1000 + System.currentTimeMillis();

        players_on_cooldown.put(_player.getUniqueId(), total_cd);
    }

    public void endTimer(Player _player) {
        time_until_kick.remove(_player.getUniqueId());
    }

    public HashMap getPlayersOnCooldown() {
        return players_on_cooldown;
    }

    public HashMap getTimeUntilKick() {
        return time_until_kick;
    }

    public void startTimer(Player _player)
    {
        long global_timer = config.getObservationTime();
        long time_up = global_timer * 1000 + System.currentTimeMillis();

        time_until_kick.put(_player.getUniqueId(),time_up);
    }
}
