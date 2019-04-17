package Spigot_Observe.observe.Main;

import Spigot_Observe.observe.Configurators.Config;
import Spigot_Observe.observe.Configurators.PlayerStateConfigurator;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Observe
{
    private Config config;
    private Player player;
    private Player target;
    private PlayerStateConfigurator player_state;
    private HashMap<UUID, Boolean> is_observing;

    public Observe(Config _config) {
        player = null;
        target = null;

        config = _config;
        player_state = new PlayerStateConfigurator(_config);
        is_observing = new HashMap<>();
    }

    boolean back() {
        try {
            if(!is_observing.get(player.getUniqueId()))
                return failed();
        } catch (Exception e) {
            return failed();
        }

        is_observing.put(player.getUniqueId(), false);
        return player_state.restorePlayerState();
    }

    boolean beginObservation() {
        if(target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "YoU CaNnOt SpEcTaTe YoUrSeLf");
            return false;
        }

        if(player_state.savePlayerState())
        {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 4, 1);
            is_observing.put(player.getUniqueId(), true);
            player.getInventory().clear();
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(target);
            return true;
        }

        return false;
    }

    private boolean failed() {
        player.sendMessage(ChatColor.RED + "You must be actively observing someone in order to use this command.");
        return false;
    }

    private Player getSpectator() {
        try {
            return (Player) player.getSpectatorTarget();
        } catch (ClassCastException e) {
            return null;
        }
    }

    boolean help() {
        player.sendMessage(PluginHead.getValidCommands());
        return true;
    }

    boolean info() {
        if(!is_observing.get(player.getUniqueId()))
            return failed();

        Player target = getSpectator();
        if(target != null) {
            String name = target.getName();
            UUID uuid = target.getUniqueId();

            player.sendMessage(ChatColor.GRAY + "You are spectating player: " + ChatColor.GOLD + name);
            player.sendMessage(ChatColor.GRAY + "UUID: " + ChatColor.ITALIC + "" + ChatColor.GOLD + uuid.toString());
            return true;
        }

        return false;
    }

    public void setPlayer(Player _player) {
        player = _player;
        player_state.setPlayer(_player);
    }

    void setTarget(Player _target) {
        target = _target;
    }

    boolean uses() {
        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Unimplemented");
        return false;
    }
}
