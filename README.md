# Spigot_Observe #


## Configuration File ##
----


### Example config.yml ###
```yaml
enabled: true
cooldown-refund-enabled: true
    refund-percent: 100
player-detection: true
    radius: 10
cooldowns: true
    permission_level_1: 
        cooldown-time: 5m
        uses-per-day: 10
        observation-time: 5m
    permission_level_n:
resource-checker: true
    memory-time: 30m
 ```
 
 
 ### Settings ###
| Field                         | Type          | Description                                                       |
| ------                        | ------        |  ------                                                           |
| __enabled__                   | ```boolean``` | enables/disables the plugin                                       |
| __cooldown-refund-enabled__   | ```boolean``` | enable cooldown refund based on duration of observation session   |
| __refund-percent__            | ```integer``` | determines amount of cooldown to reduce depending on the amount<br>of time spent in the last observation session |
| | | 
| __player-detection__          | ```boolean``` | enable/disables player detection                                  |
| __radius__                    | ```integer``` | radius of player detection                                        | 
| __cooldowns__                 | ```boolean``` | enables/disables cooldown timers                                  |
| | | 
| __permission-level-n__        |               |                                                                   |
| __cooldown-time__             | ```string```  | cooldown timer length*  |
| __uses-per-day__              | ```integer``` | number of uses per day alotted                                    |
| __observation-time__          | ```string```  | amount of time alloted for each observation period*
| | | 
| __resource-checker__          | ```boolean``` | enables/disables resource checker                                 | 
| __memory-time__               | ```string```  | amount of time the resource checker will store mined resources*   |

*integer value followed by "s,m,h,d"<br> [seconds, minutes, hours, days]


## Commands ##
----
/obs and /observe can be used interchangeably

| Command | Description |
| ------ | ------ |
| __/obs <player_name>___ | observes <player_name> |
| __/obs target info__ | gets pertinent info about current target (can only be run when actively observing) |
| __/obs back__ | ends observation session and refunds any time remaining into cooldown |
| __/obs cd__ | gets cooldown time remaining |
| __/obs uses__ | gets number of uses remaining |

### /obs <player_name> ###
* Saves player state
* Replaces inventory with [Spectator Inventory](#spectator-inventory)
* Starts [Observation Session](#observation-session) and targets the player inputted
* Starts timer

### /obs info ###
* Gets number of precious resources mined by target in past "x" time (set in [config.xml](#configuration-file))
* Gets amount of time player has been online

### /obs back ###
* sends observer back to previous location and restores their player state
* refunds a percentage of the cooldown timer based off of time spent in observation ```refund = (time_spent_in_observation / cooldown-time) * ```
