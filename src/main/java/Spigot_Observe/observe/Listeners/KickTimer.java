package Spigot_Observe.observe.Listeners;

import Spigot_Observe.observe.Configurators.Cooldowns;
import Spigot_Observe.observe.Configurators.PlayerStateConfigurator;
import Spigot_Observe.observe.Main.PluginHead;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KickTimer extends BukkitRunnable
{
    PluginHead plugin;
    Cooldowns  cooldowns;
    PlayerStateConfigurator player_state;

    public KickTimer(PluginHead _plugin, Cooldowns _cooldowns, PlayerStateConfigurator _player_state) {
        plugin = _plugin;
        cooldowns = _cooldowns;
        player_state = _player_state;
    }

    public void updateCooldowns(Cooldowns _cooldowns) {
        cooldowns = _cooldowns;
    }

    @Override
    public void run()
    {
        for(Player p : plugin.getServer().getOnlinePlayers())
        {
            boolean is_observing = cooldowns.getTimeUntilKick().containsKey(p.getUniqueId());
            if(is_observing)
            {
                long current_time    = System.currentTimeMillis();
                long time_until_kick = (Long) cooldowns.getTimeUntilKick().get(p.getUniqueId());

                if(current_time >= time_until_kick)
                {
                    player_state.setPlayer(p);
                    if(player_state.restorePlayerState()) {
                        p.sendMessage(ChatColor.GOLD + "You have exited your observation period and have been sent back!");
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 4, 1);
                    }
                }
            }
        }

    }
}
