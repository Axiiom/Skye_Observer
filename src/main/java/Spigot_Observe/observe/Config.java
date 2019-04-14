package Spigot_Observe.observe;

import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class Config
{
    //TODO: SETTERS

    Yaml yaml;

    private boolean is_enabled;
    private boolean cooldown_refund_enabled;
    private boolean player_detection_enabled;
    private boolean cooldowns_enabled;
    private boolean resource_checker_enabled;

    private String cooldown_time;
    private String observation_time;
    private String memory_time;

    private int uses_per_day;
    private int radius;
    private int refund_percent;

    private boolean read_yaml_successfully = false;
    public Config(InputStream is)
    {
        yaml = new Yaml();
        Map<String, Object> map = yaml.load(is);

        System.out.println(map);
        try {
            is_enabled = (Boolean) map.get("enabled");
            cooldown_refund_enabled = (Boolean) map.get("cooldown-refund-enabled");
            player_detection_enabled = (Boolean) map.get("player-detection-enabled");
            cooldowns_enabled = (Boolean) map.get("cooldowns-enabled");
            resource_checker_enabled = (Boolean) map.get("resource-checker-enabled");

            cooldown_time = ((Map<String, String>) map.get("cooldowns-enabled")).get("cooldown-time");
            observation_time = ((Map<String, String>) map.get("cooldowns-enabled")).get("observation-time");
            memory_time = ((Map<String, String>) map.get("resource-checker-enabled")).get("memory-time");

            uses_per_day = ((Map<String, Integer>) map.get("cooldowns-enabled")).get("uses-per-day");
            radius = ((Map<String, Integer>) map.get("player-detection-enabled")).get("radius");
            refund_percent = ((Map<String, Integer>) map.get("cooldown-refund-enabled")).get("refund-percent");

            read_yaml_successfully = true;
        } catch (Exception e) {
            System.out.println("YAML DID NOT PARSE");
            e.printStackTrace();
        }

        if(this.isEnabled())
            System.out.println("OBSERVE IS ENABLED");
    }

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

    public String getCooldownTime() {
        return cooldown_time;
    }

    public String getObservationTime() {
        return observation_time;
    }

    public String getMemoryTime() {
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
