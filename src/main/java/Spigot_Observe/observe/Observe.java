package Spigot_Observe.observe;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import java.util.*;

public class Observe
{
    private UUID uuid;

    private Config config;
    private Plugin plugin;
    private Player player;
    private Player target;

    public Observe(Player _player, Config _config, Plugin _plugin) {
        player = _player;
        config = _config;
        plugin = _plugin;
        uuid = _player.getUniqueId();
    }

    public boolean beginObservation(Player _target) {

        if(_target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "No, you can't spectate yourself xD");
            return true;
        }

        target = _target;
        boolean saved_successfully = PlayerStateConfigurator.savePlayerState(player, _target);
        if(saved_successfully) {
            player.getInventory().clear();
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(target);
        }

        return false;
    }

    public boolean info() {
        return false;
    }

    public boolean back() {
        return PlayerStateConfigurator.restorePlayerState(player);
    }

    public boolean cooldown() {
        return false;
    }

    public boolean uses() {
        return false;
    }

    public Player getTarget() {
        return target;
    }
}
