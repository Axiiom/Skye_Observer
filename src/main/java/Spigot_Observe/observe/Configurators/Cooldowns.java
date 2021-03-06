package Spigot_Observe.observe.Configurators;

import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.UUID;


public class Cooldowns
{
    private Config config;
    private HashMap<UUID, Long> players_on_cooldown;
    private HashMap<UUID, Long> time_until_kick;


    public Cooldowns(Config _config) {
        config = _config;
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

    public void kickFromObservation(Player _player) {
        if(time_until_kick.containsKey(_player.getUniqueId()))
            time_until_kick.put(_player.getUniqueId(), 0L);
    }

    public void remove(Player _player) {
        time_until_kick.remove(_player.getUniqueId());
    }

    public void startTimer(Player _player)
    {
        long global_timer = config.getObservationTime();
        long time_up = global_timer * 1000 + System.currentTimeMillis();

        time_until_kick.put(_player.getUniqueId(),time_up);
    }
}
