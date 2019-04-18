package Spigot_Observe.observe.Listeners;

import Spigot_Observe.observe.Configurators.Config;
import Spigot_Observe.observe.Configurators.Cooldowns;
import Spigot_Observe.observe.Configurators.PlayerStateConfigurator;
import Spigot_Observe.observe.Main.PluginHead;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ContingencyListener implements Listener
{
    private PluginHead plugin;
    private Cooldowns cooldowns;
    private PlayerStateConfigurator player_state;
    private ArrayList<UUID> restore_my_inventory;

    private HashMap<UUID, Long> join_time;

    public ContingencyListener(Cooldowns _cooldowns, Config _config, PluginHead _plugin) {
        plugin = _plugin;
        cooldowns = _cooldowns;
        player_state = new PlayerStateConfigurator(_config);
        restore_my_inventory = new ArrayList<>();
        join_time = new HashMap<>();
    }

    public HashMap<UUID, Long> getJoinTime() {
        return join_time;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent _event) {
        Player player = _event.getEntity();

        if(cooldowns.getPlayersOnCooldown().containsKey(player.getUniqueId())
                && (long) cooldowns.getPlayersOnCooldown().get(player.getUniqueId()) >= System.currentTimeMillis())
        {
            player.sendMessage(ChatColor.GOLD  + "You were observing on death, upon respawn you will be sent back!");
            cooldowns.endTimer(player);
            restore_my_inventory.add(player.getUniqueId());
            _event.setDeathMessage("");
            return;
        }
    }

    @EventHandler
    public void onExitFromObserveMode(PlayerToggleSneakEvent _event) {
        Player player = _event.getPlayer();
        if(cooldowns.getTimeUntilKick().containsKey(player.getUniqueId()))
        {
            _event.setCancelled(true);
            player.sendMessage(ChatColor.GRAY + "You cannot leave your target, in order to exit use:\n"
                    + ChatColor.GOLD + "/obs back");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent _event) {
        Player player = _event.getPlayer();
        join_time.put(player.getUniqueId(), System.currentTimeMillis());

        if(player.getGameMode().equals(GameMode.SPECTATOR))
        {
            player_state.setPlayer(player);
            if(player_state.restorePlayerState()) {
                player.sendMessage(ChatColor.GOLD + "You disconnected while observing "
                        + " - you have been sent back to your original location.");
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent _event) {
        Player player = _event.getPlayer();

        if(restore_my_inventory.contains(player.getUniqueId()))
        {
            restore_my_inventory.remove(player.getUniqueId());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    player_state.setPlayer(player);
                    player_state.restorePlayerState();
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 4, 1);
                }
            }, 10L);
        }
    }

    public void updateCooldowns(Cooldowns _cooldowns) {
        cooldowns = _cooldowns;
    }
}
