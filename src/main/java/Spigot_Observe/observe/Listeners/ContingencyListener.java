package Spigot_Observe.observe.Listeners;

import Spigot_Observe.observe.Configurators.PlayerStateConfigurator;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ContingencyListener implements Listener
{
    @EventHandler
    public void onLogin(PlayerJoinEvent _event) {
        Player player = _event.getPlayer();
        PlayerStateConfigurator player_state = new PlayerStateConfigurator(player);

        if(wasObserving(player)) {
            player.sendMessage(ChatColor.GOLD + "You disconnected while observing - you have been sent back to your"
                            + " original location.");
            player_state.restorePlayerState();
        }
    }

    private boolean wasObserving(Player _player) {
        return _player.getGameMode().equals(GameMode.SPECTATOR);
    }
}
