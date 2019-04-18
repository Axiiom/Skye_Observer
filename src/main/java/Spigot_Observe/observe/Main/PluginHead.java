package Spigot_Observe.observe.Main;

import Spigot_Observe.observe.Configurators.Config;
import Spigot_Observe.observe.Configurators.PlayerStateConfigurator;
import Spigot_Observe.observe.Listeners.ContingencyListener;
import Spigot_Observe.observe.Configurators.Cooldowns;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class PluginHead extends JavaPlugin
{
    private Observe observe;
    private Config config;
    private Cooldowns cooldowns;
    private KickTimer kick_timer;
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
        start();
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
            if(subcommand.equalsIgnoreCase("help")) { return help(player); }
            if(subcommand.equalsIgnoreCase("info")) { return info(player); }
            if(subcommand.equalsIgnoreCase("back")) { return back(player); }
            if(subcommand.equalsIgnoreCase("uses")) { return uses(player); }
            if(subcommand.equalsIgnoreCase("cd"))   { return cldn(player); }

            Player target = Bukkit.getPlayer(subcommand);
            if(target == null)
                return failed(ChatColor.RED + "Invalid target player.", player);

            if(cldn(player))
                beginObservation(player, target);
        }

        return true;
    }

    private boolean uses(Player _player) {
        observe.setPlayer(_player);
        return observe.uses();
    }

    private boolean info(Player _player) {
        observe.setPlayer(_player);
        return observe.info();
    }

    private boolean help(Player _player) {
        _player.sendMessage(VALID_COMMANDS);
        return true;
    }

    private boolean back(Player _player) {
        observe.setPlayer(_player);
        if(!observe.back())
            return false;

        cooldowns.remove(_player);
        observe.updateCooldowns(cooldowns);
        return true;
    }


    private void beginObservation(Player _player, Player _target)
    {
        observe.setPlayer(_player);
        observe.setTarget(_target);
        if(observe.beginObservation())
        {
            if(config.isObservationTimerEnabled()) {
                cooldowns.add(_player);
                cooldowns.startTimer(_player);
                observe.updateCooldowns(cooldowns);

                _player.sendMessage(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "You can now observe "
                                + ChatColor.GOLD + "" + ChatColor.ITALIC +  _target.getName()
                                + ChatColor.GRAY + "" + ChatColor.ITALIC + " for "
                                + ChatColor.GOLD + "" + ChatColor.ITALIC + config.getObservationTime()
                                + ChatColor.GRAY + "" + ChatColor.ITALIC + " seconds!"
                );

            } else {
                _player.sendMessage(
                        ChatColor.GRAY + "" + ChatColor.ITALIC + "You are now observing "
                                + ChatColor.GOLD + "" + ChatColor.ITALIC +  _target.getName()
                                + ChatColor.GRAY + "" + ChatColor.ITALIC + "!"
                );
            }

            contingency_listener.updateCooldowns(cooldowns);
        }
        else
            System.out.println("Player could not begin observing...");
    }

    private boolean cldn(Player _player)
    {
        if(config.isCooldownsEnabled() && cooldowns.getPlayersOnCooldown().containsKey(_player.getUniqueId()))
        {
            long cd_time  = (Long) cooldowns.getPlayersOnCooldown().get(_player.getUniqueId());
            long delta_cd = cd_time - System.currentTimeMillis();
            String time = timeString(delta_cd);

            if(delta_cd > 0) {
                _player.sendMessage(ChatColor.RED + "You must wait: " + time);
                return false;
            }
        }

        return true;
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

    private void loadMainConfiguration() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        config = new Config(this.getConfig());
    }

    private void start() {
        cooldowns = new Cooldowns(config);
        contingency_listener = new ContingencyListener(cooldowns, config,this);

        observe = new Observe(config, contingency_listener, this);
        kick_timer = new KickTimer();

        getServer().getPluginManager().registerEvents(contingency_listener, this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, kick_timer, 0L, 2L);
    }

    public static String timeString(long _delta_time)
    {
        String time = "";
        _delta_time /= 1000;

        int hours   = (int)(_delta_time / 3600);
        int minutes = (int)(_delta_time / 60) - hours*60;
        int seconds = (int)(_delta_time) - minutes*60;

        if(hours > 0)
            time += ChatColor.GOLD + "" + hours + ChatColor.GRAY + " hr";
        if(minutes > 0) {
            if(!time.equals(""))
                time += ", ";
            time += ChatColor.GOLD + "" + minutes + ChatColor.GRAY + " min ";
        }
        if(seconds > 0) {
            if(!time.equals(""))
                time += "and ";
            time += ChatColor.GOLD + "" + seconds + ChatColor.GRAY + " sec ";
        }

        return time;
    }

    public void updateCooldowns(Cooldowns _cooldowns) {
        cooldowns = _cooldowns;
    }

    class KickTimer extends BukkitRunnable
    {
        @Override
        public void run()
        {
            for(Player p : getServer().getOnlinePlayers())
            {
                boolean is_observing = cooldowns.getTimeUntilKick().containsKey(p.getUniqueId());
                if(is_observing)
                {
                    long current_time    = System.currentTimeMillis();
                    long time_until_kick = (Long) cooldowns.getTimeUntilKick().get(p.getUniqueId());

                    if(current_time >= time_until_kick)
                    {
                        PlayerStateConfigurator player_state = new PlayerStateConfigurator(config);
                        player_state.setPlayer(p);
                        if(player_state.restorePlayerState()) {
                            p.sendMessage(ChatColor.GOLD + "Your observation period has ended.");
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 4, 1);
                            cooldowns.remove(p);
                            observe.updateCooldowns(cooldowns);
                        }
                    }
                }
            }
        }
    }

}
