package Spigot_Observe.observe.Configurators;

import org.bukkit.configuration.file.FileConfiguration;

public class Config
{
    //TODO: SETTERS

    private boolean is_enabled;
    private boolean cooldown_refund_enabled;
    private boolean player_detection_enabled;
    private boolean cooldowns_enabled;
    private boolean resource_checker_enabled;
    private boolean delete_unused_data;

    private long cooldown_time;
    private long observation_time;
    private long memory_time;

    private int uses_per_day;
    private int radius;
    private int refund_percent;

    private boolean read_yaml_successfully = false;
    public Config(FileConfiguration yaml)
    {
        try {
            is_enabled = yaml.getBoolean("enabled");
            cooldown_refund_enabled = yaml.getBoolean("cooldown-refund.enabled");
            player_detection_enabled = yaml.getBoolean("player-detection.enabled");
            cooldowns_enabled = yaml.getBoolean("cooldowns.enabled");
            resource_checker_enabled = yaml.getBoolean("resource-checker.enabled");
            delete_unused_data = yaml.getBoolean("delete-unused-data");

            cooldown_time = toSeconds(yaml.getString("cooldowns.cooldown-time"));
            observation_time = toSeconds(yaml.getString("cooldowns.observation-time"));
            memory_time = toSeconds(yaml.getString("resource-checker.memory-time"));

            uses_per_day = yaml.getInt("cooldowns.uses-per-day");
            radius = yaml.getInt("player-detection.radius");
            refund_percent = yaml.getInt("cooldown-refund.refund-percent");

            read_yaml_successfully = true;
        } catch (Exception e) {
            System.out.println("YAML DID NOT PARSE - SEE \'CONFIG.JAVA\' CONSTRUCTOR");
            e.printStackTrace();
        }
    }

    private long toSeconds(String _time_str)
    {
        String[] split = _time_str.split("-");
        long time = (long) Integer.parseInt(split[0]);
        char modifier = split[1].charAt(0);

        switch (modifier) {
            case 'm':
                time *= (60);
                break;
            case 'h':
                time *= (60 * 60);
                break;
            case 'd':
                time *= (60 * 60 * 24);
                break;
        }

        return time;
    }

    public boolean deleteUnusedData() { return delete_unused_data; }

    public boolean isConstructed() {
        return read_yaml_successfully;
    }

    public boolean isEnabled() {
        return is_enabled;
    }

    public boolean isCooldownRefundEnabled() {
        return cooldown_refund_enabled;
    }

    public boolean isPlayerDetectionEnabled() {
        return player_detection_enabled;
    }

    public boolean isCooldownsEnabled() {
        return cooldowns_enabled;
    }

    public boolean isResourceCheckerEnabled() {
        return resource_checker_enabled;
    }

    public long getCooldownTime() {
        return cooldown_time;
    }

    public long getObservationTime() {
        return observation_time;
    }

    public long getMemoryTime() {
        return memory_time;
    }

    public int getUses() {
        return uses_per_day;
    }

    public int getRadius() {
        return radius;
    }

    public int getRefund() {
        return refund_percent;
    }

}
