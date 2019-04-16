package Spigot_Observe.observe.Plugin;

import Spigot_Observe.observe.Configurators.Config;
import Spigot_Observe.observe.Configurators.PlayerStateConfigurator;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class Observe
{
    private boolean is_observing;

    private Config config;
    private Plugin plugin;
    private Player player;
    private Player target;
    private PlayerStateConfigurator player_state;

    public Observe(Player _player, Config _config, Plugin _plugin) {
        player = _player;
        config = _config;
        plugin = _plugin;
        player_state = new PlayerStateConfigurator(player);

        try {
            is_observing = _player.getSpectatorTarget() != null;
        } catch (NullPointerException e) {
            is_observing = false;
        }
    }

    void setTarget(Player _target) {
        target = _target;
    }

    boolean beginObservation() {
        if(target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "YoU CaNnOt SpEcTaTe YoUrSeLf");
            return true;
        }

        if(player_state.savePlayerState()) {
            player.getInventory().clear();
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(target);
            return true;
        }

        return false;
    }

    boolean help() {
        player.sendMessage(Main.getValidCommands());
        return true;
    }

    boolean info() {
        if(!is_observing)
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

    boolean back() {
        if(!is_observing)
            return failed();

        is_observing = false;
        return player_state.restorePlayerState();
    }

    boolean cldn() {
        return false;
    }

    boolean uses() {
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
}
