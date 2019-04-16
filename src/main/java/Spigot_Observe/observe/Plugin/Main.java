package Spigot_Observe.observe.Plugin;

import Spigot_Observe.observe.Configurators.Config;
import Spigot_Observe.observe.Configurators.PlayerStateConfigurator;
import Spigot_Observe.observe.Listeners.ContingencyListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin
{
    private Observe observe;
    private Config config;
    private ContingencyListener contingency_listener;
    private static final String VALID_COMMANDS =
              ChatColor.GRAY + "" + ChatColor.ITALIC + "Observe Commands:\n"
            + ChatColor.GOLD + "/obs"                + ChatColor.GRAY + ": prints help menu\n"
            + ChatColor.GOLD + "/obs <player name>"  + ChatColor.GRAY + ": observes <player name>\n"
            + ChatColor.GOLD + "/obs back"           + ChatColor.GRAY + ": ends observation session\n"
            + ChatColor.GOLD + "/obs cd"             + ChatColor.GRAY + ": gets cooldown time remaining\n"
            + ChatColor.GOLD + "/obs uses"           + ChatColor.GRAY + ": gets number of uses remaining in the day\n";

    @Override
    public void onEnable() {
        loadConfiguration();
        startListeners();
    }

    @Override
    public boolean onCommand(CommandSender _sender, Command _command, String _label, String[] _args)
    {
        if(!inputIsValid(_sender, _args))
            return true;

        Player player = (Player) _sender;
        if(_command.getName().equalsIgnoreCase("obs"))
        {
            String subcommand = _args[0];
            observe = new Observe(player, config, this);

            if(subcommand.equalsIgnoreCase("help")) { return observe.help(); }
            if(subcommand.equalsIgnoreCase("info")) { return observe.info(); }
            if(subcommand.equalsIgnoreCase("back")) { return observe.back(); }
            if(subcommand.equalsIgnoreCase("cd"))   { return observe.cldn(); }
            if(subcommand.equalsIgnoreCase("uses")) { return observe.uses(); }

            Player target = Bukkit.getPlayer(subcommand);
            if(target == null)
                return failed(ChatColor.RED + "Invalid target player.", player);

            observe.setTarget(target);
            return observe.beginObservation();
        }

        return true;
    }

    private boolean inputIsValid(CommandSender _sender, String[] _args)
    {
        if(!config.isConstructed() || !config.isEnabled())
            return false;

        Player player;

        try {
            player = (Player) _sender;
        } catch (Exception e) {
            if(e.getClass().equals(ClassCastException.class))
                _sender.sendMessage("You must be a player to use this command");
            else if(e.getClass().equals(NullPointerException.class))
                e.printStackTrace();
            else {
                System.out.println("UNHANDLED ERROR THROWN WHILE CHECKING FOR VALID INPUT 'MAIN.JAVA'");
                e.printStackTrace();
            }

            return false;
        }

        if(_args.length != 1)
            return !failed(VALID_COMMANDS, player);

        return true;
    }


    private void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        config = new Config(this.getConfig());
    }

    private void startListeners() {
        contingency_listener = new ContingencyListener();
        getServer().getPluginManager().registerEvents(contingency_listener, this);
    }

    private boolean failed(String _message, CommandSender _sender) {
        _sender.sendMessage(_message);
        return true;
    }

    static String getValidCommands() {
        return VALID_COMMANDS;
    }
}
