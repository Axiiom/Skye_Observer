## Commands ##
<details>
    <summary><b>/obs [player_name]</b>: observes [player_name]</summary>
    <p>
        1) Saves player state <br>
        2) Replaces inventory with [Spectator Inventory](#spectator-inventory) <br> 
        3) Starts [Observation Session](#observation-session) and targets the player inputted <br>
        4) Starts timer
    </p>
</details>

<details>
    <summary><b>/obs info</b>: gets pertinent info about current target</summary>
    <p>
        1) Gets number of precious resources mined by target in past "memory-time" time <br>
        2) Gets amount of time player has been online
    </p>
</details>

<details>
    <summary><b>/obs back</b>: ends observation session and refunds any time remaining into cooldown</summary>
    <p>
        1) Sends observer back to previous location and restores their player state <br>
        2) refunds a percentage of the cooldown timer based off of time spent in observation <br> <code> refund = refund-percent * ((time_spent_observing / observation-time) * cooldown-time)) </code>
    </p>
</details>

<details>
    <summary><b>/obs cd</b>: gets cooldown time remaining</summary>
    <p>Straightforward lol. Gets the amount of time left in the user's cooldown timer</p>
</details>

## Configuration File ##


### Example config.yml ###
```yaml
enabled: true
cooldown-refund:
  enabled: true
  refund-percent: 100
player-detection:
  enabled: true
  radius: 10
cooldown:
  enabled: true
  length: 5-m
observation-timer:
  enabled: true
  length: 10-s
resource-checker:
  enabled: true
  memory-time: 30-m
 ```
 
 
 ### Settings ###
| Field                             | Type          | Description |
| ------                            | ------        |  ------     |
| __enabled__                       | ```boolean``` | enables/disables the plugin |
| __cooldown-refund.enabled__       | ```boolean``` | enable cooldown refund based on duration of observation session |
| __cooldown-refund.refund-percent__| ```integer``` | determines amount of cooldown to reduce depending on                   the amount<br>of time spent in the last observation session |
| | | 
| __player-detection.enabled__      | ```boolean``` | enable/disables player detection |
| __player-detection.radius__        | ```integer``` | radius of player detection | 
| | |
| __cooldown.enabled__              | ```boolean``` | enables/disables cooldown timer |       
| __cooldown.length__               | ```string```  | cooldown timer length*  |
| | |
| __observation-timer.enabled__     | ```boolean``` | enables/disables observation timer |
| __observation-timer.length__      | ```string```  | amount of time alloted for each observation period* |
| | | 
| __resource-checker.enabled__      | ```boolean``` | enables/disables resource checker |                              
| __resource-checker.memory-time__  | ```string```  | amount of time the resource checker will store mined resources* |

*integer value followed by "-s,-m,-h,-d" [seconds, minutes, hours, days]
 THE DASH IS VERY IMPORTANT
