package Spigot_Observe.observe;

import org.bukkit.entity.Player;

public class Observe
{
    Player player;
    Config config;

    public Observe(Player _player, Config _config) {
        player  = _player;
        config  = _config;
    }

    public boolean info() {
        return false;
    }

    public boolean back() {
        return false;
    }

    public boolean cooldown() {
        return false;
    }

    public boolean uses() {
        return false;
    }
}
