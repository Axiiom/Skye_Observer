package Spigot_Observe.observe;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerStateConfigurator
{
    private static final String FILE_LOCATION = "plugins/Observe/player_data.yml";
    private static Config config;

    public static void setConfig(Config _config) {
        config = _config;
    }

    public static FileConfiguration getPlayerStateConfig() {
        File input_file;
        FileConfiguration yaml;
        try {
            input_file = new File(FILE_LOCATION);
            yaml = YamlConfiguration.loadConfiguration(input_file);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }

        return yaml;
    }

    public static boolean savePlayerState(Player _player, Player _target) {
        UUID uuid = _player.getUniqueId();
        FileConfiguration yaml = getPlayerStateConfig();

        yaml.addDefault(_target.getUniqueId().toString(), _player.getUniqueId().toString());
        yaml.createSection(uuid.toString());
            yaml.addDefault(uuid.toString() + ".name",      _player.getName());
            yaml.addDefault(uuid.toString() + ".location",  _player.getLocation());
            yaml.addDefault(uuid.toString() + ".inventory", getItems(_player));
        yaml.options().copyDefaults(true);

        return saveYaml(yaml);
    }

    public static boolean restorePlayerState(Player _player) {
        UUID uuid = _player.getUniqueId();
        FileConfiguration yaml = getPlayerStateConfig();

        if(!yaml.equals(null))
        {
            MemorySection items;
            LinkedHashMap<String, ItemStack> hashed_items;
            Location location;
            try {
                items = yaml.getObject(uuid.toString() + ".inventory", MemorySection.class);
                hashed_items = (LinkedHashMap) items.getValues(true);
                location = yaml.getObject(uuid.toString() + ".location", Location.class);
            } catch (NullPointerException e) {
                _player.sendMessage(ChatColor.RED + "You cannot go back at this time.");
                return false;
            }

            restoreItems(hashed_items, _player);
            restoreLocation(location, _player);
            yaml.createSection(uuid.toString());

            return saveYaml(yaml);
        }

        return false;
    }

    //TODO theres probably a better way to do this than just to catch all null pointer exceptions...
    private static HashMap<Integer,ItemStack> getItems(Player _player) {
        HashMap<Integer,ItemStack> items = new HashMap<>();
        ArrayList<ItemStack> items_array = new ArrayList<>(Arrays.asList(_player.getInventory().getContents()));

        for(int i = 0; i < items_array.size(); i++)
        {
            try {
                if(!items_array.get(i).equals(null))
                    items.put(i, items_array.get(i));
            } catch (NullPointerException e) { }
        }

        return items;
    }

    private static boolean saveYaml(FileConfiguration _yaml) {
        try {
            _yaml.save(FILE_LOCATION);
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static void leaveSpectator(Player _player) {
        if(_player.getGameMode().equals(GameMode.SPECTATOR)) {
            _player.setSpectatorTarget(null);
            _player.setGameMode(GameMode.SURVIVAL);
        }
    }

    private static void restoreItems(LinkedHashMap<String, ItemStack> _hashed_items, Player _player) {
        for(String item_slot : _hashed_items.keySet())
            _player.getInventory().setItem(Integer.parseInt(item_slot), _hashed_items.get(item_slot));
    }

    private static void restoreLocation(Location _location, Player _player) {
        leaveSpectator(_player);
        _player.teleport(_location);
    }

}
