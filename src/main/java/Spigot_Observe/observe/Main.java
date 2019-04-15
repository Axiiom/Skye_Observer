package Spigot_Observe.observe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class Main extends JavaPlugin
{
    private Observe observe;
    private Config config;
    private final String VALID_COMMANDS =
              ChatColor.GRAY + "" + ChatColor.ITALIC + "Observe Commands:\n"
            + ChatColor.GOLD + "/obs"                + ChatColor.GRAY + ": prints help menu\n"
            + ChatColor.GOLD + "/obs <player name>"  + ChatColor.GRAY + ": observes <player name>\n"
            + ChatColor.GOLD + "/obs back"           + ChatColor.GRAY + ": ends observation session\n"
            + ChatColor.GOLD + "/obs cd"             + ChatColor.GRAY + ": gets cooldown time remaining\n"
            + ChatColor.GOLD + "/obs uses"           + ChatColor.GRAY + ": gets number of uses remaining in the day\n";

    @Override
    public void onEnable() {
        loadConfiguration();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!config.isConstructed() || !config.isEnabled())
            return true;

        if(args.length != 1) {
            sender.sendMessage(VALID_COMMANDS);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use Observe");
            return true;
        }

        Player observer = (Player) sender;
        String cmd = command.getName();

        if(cmd.equalsIgnoreCase("obs"))
        {
            cmd = args[0];
            observe = new Observe(observer, config, this);

            if(cmd.equalsIgnoreCase("help"))
                observer.sendMessage(VALID_COMMANDS);

            if(cmd.equalsIgnoreCase("info"))
                return observe.info();
            if(cmd.equalsIgnoreCase("back"))
                return observe.back();
            if(cmd.equalsIgnoreCase("cd"))
                return observe.cooldown();
            if(cmd.equalsIgnoreCase("uses"))
                return observe.uses();

            else {
                UUID target_uuid = Bukkit.getPlayer(cmd).getUniqueId();
                return observe.beginObservation(target_uuid);
            }
        }

        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        config = new Config(this.getConfig());
    }

}
