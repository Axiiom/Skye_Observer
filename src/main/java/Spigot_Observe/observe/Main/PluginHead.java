package Spigot_Observe.observe.Main;

import Spigot_Observe.observe.Configurators.Config;
import Spigot_Observe.observe.Listeners.ContingencyListener;
import Spigot_Observe.observe.Configurators.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginHead extends JavaPlugin
{
    private Observe observe;
    private Config config;
    private Cooldowns cooldowns;
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
        loadMainConfiguration();
        loadConfigurators();
        startListeners();
    }

    @Override
    public boolean onCommand(CommandSender _sender, Command _command, String _label, String[] _args)
    {
        if(inputInvalid(_sender, _args))
            return true;

        Player player = (Player) _sender;
        if(_command.getName().equalsIgnoreCase("obs"))
        {
            String subcommand = _args[0];
            observe.setPlayer(player);

            if(subcommand.equalsIgnoreCase("help")) { return observe.help(); }
            if(subcommand.equalsIgnoreCase("info")) { return observe.info(); }
            if(subcommand.equalsIgnoreCase("back")) { return observe.back(); }
            if(subcommand.equalsIgnoreCase("uses")) { return observe.uses(); }
            if(subcommand.equalsIgnoreCase("cd"))   { return checkCooldown(player); }

            Player target = Bukkit.getPlayer(subcommand);
            if(target == null)
                return failed(ChatColor.RED + "Invalid target player.", player);

            if(canObserve(player))
            {
                observe.setTarget(target);
                if(observe.beginObservation())
                {
                    cooldowns.add(player);
                    cooldowns.startTimer(player);
                    contingency_listener.updateCooldowns(cooldowns);

                    player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You can now observe "
                            + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " for " + ChatColor.GOLD
                            + config.getObservationTime() + " seconds!");
                }
                else
                    System.out.println("Player could not begin observing...");
            }
        } else if(_command.getName().equalsIgnoreCase("ulist")) {
            startListeners();
        }

        return true;
    }

    private boolean canObserve(Player _player)
    {
        if(config.isCooldownsEnabled() && cooldowns.getPlayersOnCooldown().containsKey(_player.getUniqueId()))
        {
            long delta_cd = checkCooldownTime(_player);

            if(delta_cd > 0) {
                return !failed(ChatColor.RED + "You cannot use this command for another "
                        + delta_cd/1000 + " seconds.", _player);
            }
        }

        return true;
    }

    private boolean checkCooldown(Player _player)
    {
        if(cooldowns.getPlayersOnCooldown().containsKey(_player.getUniqueId()))
        {
            long delta_cd = checkCooldownTime(_player);

            if(delta_cd > 0) {
                _player.sendMessage(ChatColor.GRAY + "You can observe another player in "
                        + ChatColor.GOLD + delta_cd / 1000 + ChatColor.GRAY + " seconds.");
                return true;
            }
        }

        _player.sendMessage(ChatColor.GRAY + "You do not have a cooldown.");
        return true;
    }

    private long checkCooldownTime(Player _player)
    {
        long cd_time  = (Long) cooldowns.getPlayersOnCooldown().get(_player.getUniqueId());
        return cd_time - System.currentTimeMillis();
    }

    private boolean inputInvalid(CommandSender _sender, String[] _args)
    {
        if(!config.isConstructed() || !config.isEnabled())
            return true;

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

            return true;
        }

        if(_args.length != 1)
            return failed(VALID_COMMANDS, player);

        return false;
    }

    private boolean failed(String _message, CommandSender _sender)
    {
        _sender.sendMessage(_message);
        return true;
    }

    static String getValidCommands() {
        return VALID_COMMANDS;
    }

    private void loadConfigurators() {
        observe = new Observe(config);
        cooldowns = new Cooldowns(config);
    }

    private void loadMainConfiguration() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        config = new Config(this.getConfig());
    }

    private void startListeners() {
        contingency_listener = new ContingencyListener(cooldowns, config,this);
        getServer().getPluginManager().registerEvents(contingency_listener, this);
    }
}
