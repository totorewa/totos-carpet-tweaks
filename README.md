# Toto's Carpet Extras

This mod extends the [carpet mod](https://github.com/gnembon/fabric-carpet) and adds a few changes to the survival experience.   
This mod came about while playing some SMP and there were a few quality of life changes we wanted to make. The mod is starting off with just a spectator mode feature and an anti-cat spawner, but will grow overtime as I get around to it.

## Features 

### catsNoSpawnInVillage
Prevents cats from spawning in villages.  
While hanging around the base, you may often find a pesky cat appear out of nowhere.  
Eventually I got so annoyed at them spawning, I decided to disable it.  
Village is explicitly stated because this does not prevent cats from spawning in witch huts or with the world generation.

* Type: `boolean`
* Default value: `false`
* Required options: `true`, `false`
* Categories: `SURVIVAL`, `TOTO`

### tpSpectatorsBackOnSurvivalChange 
Teleports players back to where they were when they changed to spectator.  
There was a scarpet script which offered this feature however the position did not persist and was forgotten when the server restarted. I decided to implement my own version which saves the survival position against the player.

* Type: `boolean`
* Default value: `false`
* Required options: `true`, `false`
* Categories: `SURVIVAL`, `TOTO`

