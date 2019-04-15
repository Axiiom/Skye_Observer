package Spigot_Observe.observe;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ContingencyListener implements Listener
{
    @EventHandler
    public void onLogin(PlayerJoinEvent _event) {
        Player player = _event.getPlayer();

        if(wasObserving(player)) {
            player.sendMessage(ChatColor.GOLD + "You disconnected while observing - you have been sent back to your"
                            + " original location.");
            PlayerStateConfigurator.restorePlayerState(player);
        }
    }

    private boolean wasObserving(Player _player) {
        FileConfiguration yaml_file = PlayerStateConfigurator.getPlayerStateConfig();

        Object checker = yaml_file.getObject(_player.getUniqueId().toString() + ".location", Object.class);
        return checker != null;
    }
}
