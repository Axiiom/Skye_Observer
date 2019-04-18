package Spigot_Observe.observe.Main;

import Spigot_Observe.observe.Configurators.Config;
import Spigot_Observe.observe.Configurators.Cooldowns;
import Spigot_Observe.observe.Configurators.PlayerStateConfigurator;
import Spigot_Observe.observe.Listeners.ContingencyListener;
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
    private Cooldowns cooldowns;
    private PlayerStateConfigurator player_state;
    private ContingencyListener contingency_listener;

    public Observe(Config _config, ContingencyListener _contingency_listener) {
        player = null;
        target = null;
        cooldowns = null;

        config = _config;
        player_state = new PlayerStateConfigurator(_config);
        contingency_listener = _contingency_listener;
    }

    boolean back() {
        try {
            if(!cooldowns.getTimeUntilKick().containsKey(player.getUniqueId()))
                return failed();
        } catch (Exception e) {
            return failed();
        }

        if(player_state.restorePlayerState()) {
            player.sendMessage(ChatColor.GOLD + "You have exited your observation period and have been sent back!");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 4, 1);
        }

        return true;
    }

    boolean beginObservation() {
        if(target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "YoU CaNnOt SpEcTaTe YoUrSeLf");
            return false;
        }

        if(player_state.savePlayerState())
        {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 4, 1);
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
        if(!cooldowns.getTimeUntilKick().containsKey(player.getUniqueId()))
            return failed();

        Player target = getSpectator();
        if(target != null)
        {
            String name = target.getName();
            UUID uuid = target.getUniqueId();

            long join_time = contingency_listener.getJoinTime().get(uuid);
            long online_for = (System.currentTimeMillis() - join_time);
            String time = PluginHead.timeString(online_for);

            player.sendMessage(ChatColor.GRAY + "You are spectating player: " + ChatColor.GOLD + name);
            player.sendMessage(ChatColor.GRAY + "UUID: " + ChatColor.ITALIC + "" + ChatColor.GOLD + uuid.toString());
            player.sendMessage(ChatColor.GRAY + "Online for: " + ChatColor.GOLD + time + ChatColor.GRAY);

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

    public void updateCooldowns(Cooldowns _cooldowns) {
        cooldowns = _cooldowns;
    }
}
