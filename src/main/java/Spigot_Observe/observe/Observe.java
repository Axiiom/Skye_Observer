package Spigot_Observe.observe;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.UUID;

public class Observe
{
    private String FILE_LOCATION;
    private UUID uuid;

    private Config config;
    private Plugin plugin;
    private Player player;

    public Observe(Player _player, Config _config, Plugin _plugin) {
        FILE_LOCATION = _plugin.getDataFolder().toString() + "/inventory.yml";
        player = _player;
        config = _config;
        plugin = _plugin;
        uuid = _player.getUniqueId();
    }

    public boolean beginObservation(String _player_name) {
        boolean saved_player_state = savePlayerState();
        return false;
    }

    public boolean info() {
        //only runs loadPlayerState() for testing purposes
        return loadPlayerState();
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

    private boolean savePlayerState()
    {
        File input_file;
        FileConfiguration yaml;
        try {
            input_file = new File(FILE_LOCATION);
            yaml = YamlConfiguration.loadConfiguration(input_file);
            yaml.createSection(uuid.toString());
                yaml.addDefault(uuid.toString() + ".location", player.getLocation());
                yaml.addDefault(uuid.toString() + ".inventory", player.getInventory().getContents());
            yaml.options().copyDefaults(true);
            yaml.save(FILE_LOCATION);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public boolean loadPlayerState()
    {
        File input_file;
        FileConfiguration yaml;
        try {
            input_file = new File(FILE_LOCATION);
            yaml = YamlConfiguration.loadConfiguration(input_file);

            Location location = yaml.getObject(uuid.toString() + ".location", Location.class);
            ItemStack[] inventory = yaml.getObject(uuid.toString() + ".inventory", ItemStack[].class);

            player.teleport(location);
            player.getInventory().clear();
            //player.getInventory().setContents(inventory); THROWS NULL POINTER EXCEPTION LOL IDK WHY
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


}
