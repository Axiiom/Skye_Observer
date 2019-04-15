package Spigot_Observe.observe;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


import java.util.*;
import java.io.File;

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
        HashMap<Integer, ItemStack> items = getItems();
        try {
            input_file = new File(FILE_LOCATION);
            yaml = YamlConfiguration.loadConfiguration(input_file);
            yaml.createSection(uuid.toString());
                yaml.addDefault(uuid.toString() + ".location", player.getLocation());
                yaml.addDefault(uuid.toString() + ".inventory", items);
            yaml.options().copyDefaults(true);
            yaml.save(FILE_LOCATION);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private HashMap<Integer,ItemStack> getItems()
    {
        HashMap<Integer,ItemStack> items = new HashMap<>();
        ArrayList<ItemStack> items_array = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));

        for(int i = 0; i < items_array.size(); i++)
        {
            try {
                if(!items_array.get(i).equals(null))
                    items.put(i, items_array.get(i));
            } catch (NullPointerException e) { }
        }

        return items;
    }

    private HashMap<Integer,ItemStack> getItemsHash(ItemStack[] items) {
        return null;
    }


    public boolean loadPlayerState()
    {
        File input_file;
        FileConfiguration yaml;
        try {
            input_file = new File(FILE_LOCATION);
            yaml = YamlConfiguration.loadConfiguration(input_file);

            MemorySection items = yaml.getObject(uuid.toString() + ".inventory", MemorySection.class);
            LinkedHashMap<String, ItemStack> hashed_items = (LinkedHashMap) items.getValues(true);
            Location location   = yaml.getObject(uuid.toString() + ".location", Location.class);

            for(String item_slot : hashed_items.keySet())
                player.getInventory().setItem(Integer.parseInt(item_slot), hashed_items.get(item_slot));

            player.teleport(location);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
