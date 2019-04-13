# Spigot_Observe #

## Configuration File ##
------
```yaml
enabled: (true/false: is plugin enabled)
player-detection: (true/false: is player-detection enabled)
  - radius: 10
cooldowns: (true/false are cooldowns enabled)
  - permission_level_1: (permission based cooldowns)
      cooldown-time: 5m (time for cooldown: integer values followed by "s,m,h,d")
      uses-per-day: 10 (integer value for uses/day)
  - permission_level_n:
 ```
 
 - - - -

## Commands ##
(/obs and /observe can both be used)
<table class="tg">
  <tr>
    <th class="tg-yw41"><b>Command</b></th>
    <th class="tg-yw41"><b>Description</b></th>
  </tr>
  <tr>
    <td class="tg-yw41"><b>/obs [player_name]</b></td>
    <td class="tg-yw41">observes <player_name></td>
  </tr>
  <tr>
    <td class="tg-yw41"><b>/obs target info</b></td>
    <td class="tg-yw41">gets pertinent info about current target (can only be run when actively observing)</td>
  </tr>
  <tr>
    <td class="tg-yw41"><b>/obs back</b></td>
    <td class="tg-yw41">ends observation session and refunds any time remaining into cooldown</td>
  </tr>
  <tr>
    <td class="tg-yw41"><b>/obs cd</b></td>
    <td class="tg-yw41">gets cooldown time remaining</td>
  </tr> 
  <tr>
    <td class="tg-yw41"><b>/obs uses</b></td>
    <td class="tg-yw41">gets number of uses remaining</td>
  </tr> 
</table>

### /obs <player_name> ###
1) Check Permissions/Cooldown/Uses Left
1) Save state
2) Set inventory to [Spectator Inventory](#spectator-inventory) 
3) Set gamemode to spectator
4) Set spectator target
5) Start observe timer
6) Start observe cooldown timer

### Spectator Inventory ###
1) [Diamond Finder](#diamond-finder)
2) Each hotbar slot after [Diamond Finder](#diamond-finder) and until slot #9 contains the player head of every player within the distance specified in [config.yml](#configuration-file)
3) Slot #9 contains an item to [Exit Observer Mode](#exit-observer-mode)

### Diamond Finder ###
1) OnRightClick - finds the closest Diamond Ore to the spectator
2) Sets compass target to that diamond
3) Cooldown as specified in [config.yml](#configuration-file)

### Exit Observer Mode ###
1) Set observer cooldown to cooldown specified in [config.yml](#configuration-file)
2) Restore player state


  
