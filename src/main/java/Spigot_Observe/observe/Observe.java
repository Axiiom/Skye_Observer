package Spigot_Observe.observe;

import org.bukkit.*;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;


import java.io.IOException;
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

    public boolean beginObservation(UUID _target_uuid) {

        if(_target_uuid.equals(player.getUniqueId())) {
            player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "No, you can't spectate yourself xD");
            return true;
        }

        boolean saved_successfully = savePlayerState();
        if(saved_successfully) {
            Player target_player = Bukkit.getPlayer(_target_uuid);

            player.getInventory().clear();
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(target_player);
        }

        return false;
    }

    public boolean info() {
        return false;
    }

    public boolean back() {
        return restorePlayerState();
    }

    public boolean cooldown() {
        return false;
    }

    public boolean uses() {
        return false;
    }

    private boolean savePlayerState() {
        File input_file;
        FileConfiguration yaml;

        try {
            input_file = new File(FILE_LOCATION);
            yaml = YamlConfiguration.loadConfiguration(input_file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        yaml.createSection(uuid.toString());
            yaml.addDefault(uuid.toString() + ".name",      player.getName());
            yaml.addDefault(uuid.toString() + ".location",  player.getLocation());
            yaml.addDefault(uuid.toString() + ".inventory", getItems());
        yaml.options().copyDefaults(true);

        return saveYaml(yaml);
    }

    private boolean restorePlayerState() {
        File input_file;
        FileConfiguration yaml;
        try {
            input_file = new File(FILE_LOCATION);
            yaml = YamlConfiguration.loadConfiguration(input_file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(!input_file.equals(null))
        {
            MemorySection items;
            LinkedHashMap<String, ItemStack> hashed_items;
            Location location;
            try {
                items = yaml.getObject(uuid.toString() + ".inventory", MemorySection.class);
                hashed_items = (LinkedHashMap) items.getValues(true);
                location = yaml.getObject(uuid.toString() + ".location", Location.class);
            } catch (NullPointerException e) {
                player.sendMessage(ChatColor.RED + "You cannot go back at this time.");
                return false;
            }

            restoreItems(hashed_items);
            restoreLocation(location);
            deleteUnusedData(config.deleteUnusedData(), yaml);

            return saveYaml(yaml);
        }

        return false;
    }

    //TODO theres probably a better way to do this than just to catch all null pointer exceptions...
    private HashMap<Integer,ItemStack> getItems() {
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

    private boolean saveYaml(FileConfiguration _yaml) {
        try {
            _yaml.save(FILE_LOCATION);
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void leaveSpectator() {
        if(player.getGameMode().equals(GameMode.SPECTATOR)) {
            player.setSpectatorTarget(null);
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    private void deleteUnusedData(boolean delete, FileConfiguration yaml) {
        yaml.set(uuid.toString(), "");
    }

    private void restoreItems(LinkedHashMap<String, ItemStack> _hashed_items) {
        for(String item_slot : _hashed_items.keySet())
            player.getInventory().setItem(Integer.parseInt(item_slot), _hashed_items.get(item_slot));
    }

    private void restoreLocation(Location _location) {
        leaveSpectator();
        player.teleport(_location);
    }
}
