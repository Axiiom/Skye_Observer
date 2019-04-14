package Spigot_Observe.observe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;

public final class Main extends JavaPlugin
{
    private Observe observe;
    private Config config;
    private final String PROPER_USAGE = ChatColor.RED
            + "Improper usage of Observe. Valid commands\n"
            + ChatColor.GOLD + "/obs <player name>" + ChatColor.GRAY + ": observes <player name>\n"
            + ChatColor.GOLD + "/obs back"          + ChatColor.GRAY + ": ends observation session\n"
            + ChatColor.GOLD + "/obs cd"            + ChatColor.GRAY + ": gets cooldown time remaining\n"
            + ChatColor.GOLD + "/obs uses"          + ChatColor.GRAY + ": gets number of uses remaining in the day\n";

    @Override
    public void onEnable() {
        initializeConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!config.isConstructed() || !config.isEnabled())
            return true;

        if(args.length != 1) {
            sender.sendMessage(PROPER_USAGE);
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
            observe = new Observe(observer, config);
            if(cmd.equalsIgnoreCase("info"))
                return observe.info();
            if(cmd.equalsIgnoreCase("back"))
                return observe.back();
            if(cmd.equalsIgnoreCase("cd"))
                return observe.cooldown();
            if(cmd.equalsIgnoreCase("uses"))
                return observe.uses();
        }

        return true;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initializeConfig() {
        try {
         InputStream is = this.getClassLoader().getResourceAsStream("config.yml");
         config = new Config(is);
         is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
