package Spigot_Observe.observe.Configurators;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerStateConfigurator
{
    private static final String FILE_LOCATION = "plugins/Observe/player_data.yml";
    private Config config;
    private Player player;
    private FileConfiguration yaml;

    public PlayerStateConfigurator(Config _config) {
        config = _config;
        loadConfig();
    }

    private HashMap<Integer,ItemStack> getItems(Player _player) {
        HashMap<Integer,ItemStack> items = new HashMap<>();
        ArrayList<ItemStack> items_array = new ArrayList<>(Arrays.asList(_player.getInventory().getContents()));

        for(int i = 0; i < items_array.size(); i++)
        {
            try {
                if(items_array.get(i) !=null)
                    items.put(i, items_array.get(i));
            } catch (NullPointerException e) { /*do not add to output*/ }
        }

        return items;
    }

    private void leaveSpectator() {
        if(player.getGameMode().equals(GameMode.SPECTATOR)) {
            player.setSpectatorTarget(null);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    private void loadConfig() {
        File input_file;
        try {
            input_file = new File(FILE_LOCATION);
            yaml = YamlConfiguration.loadConfiguration(input_file);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void restoreItems(LinkedHashMap<String, ItemStack> _hashed_items) {
        if(_hashed_items == null)
            return;

        for(String item_slot : _hashed_items.keySet())
            player.getInventory().setItem(Integer.parseInt(item_slot), _hashed_items.get(item_slot));
    }

    private void restoreLocation(Location _location) {
        if(_location == null)
            return;

        player.teleport(_location);
        leaveSpectator();
    }

    public boolean restorePlayerState()
    {
        loadConfig();
        if(yaml != null)
        {
            MemorySection items;
            LinkedHashMap<String, ItemStack> hashed_items = null;
            Location location = null;

            if(yaml.getString(player.getUniqueId().toString()) == null)
                return false;

            try {
                location = yaml.getObject(player.getUniqueId().toString() + ".location", Location.class);
                items = yaml.getObject(player.getUniqueId().toString() + ".inventory", MemorySection.class);
                hashed_items = (LinkedHashMap) items.getValues(true);
            } catch (NullPointerException e) { }

            restoreItems(hashed_items);
            restoreLocation(location);

            yaml.createSection(player.getUniqueId().toString());
            return saveYaml(yaml);
        }

        return false;
    }

    public boolean savePlayerState() {
        yaml.createSection(player.getUniqueId().toString());
        yaml.addDefault(player.getUniqueId().toString() + ".name",      player.getName());
        yaml.addDefault(player.getUniqueId().toString() + ".location",  player.getLocation());
        yaml.addDefault(player.getUniqueId().toString() + ".inventory", getItems(player));
        yaml.options().copyDefaults(true);

        return saveYaml(yaml);
    }

    private boolean saveYaml(FileConfiguration _yaml) {
        try {
            _yaml.save(FILE_LOCATION);
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void setPlayer(Player _player) {
        player = _player;
    }
}
