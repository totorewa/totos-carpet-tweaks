# Toto's Carpet Tweaks

This mod extends the [carpet mod](https://github.com/gnembon/fabric-carpet) and adds a few changes to the survival experience.   
This mod came about while playing some SMP and there were a few quality of life changes we wanted to make. The mod is starting off with just a spectator mode feature and an anti-cat spawner, but will grow overtime as I get around to it.

## Features 

### reduceCatSpawnChance
When spawning a cat in a village, determine the percentage chance of it succeeding.   
While hanging around the base, you may often find a pesky cat appear out of nowhere.  
I was fed up with them spawning all the time but I also don't want to completely get rid of them, as they may be wanted for anti-phantom purposes and I didn't want to have to go to a witch hut just to get one so instead I opted to reduce the spawning instead. Spawning can still be disabled with a value of 100.

* Type: `integer`
* Default value: `0`
* Categories: `SURVIVAL`, `TOTO`

### returnSpectators 
Teleports players back to where they were when they changed to spectator.  
There was a scarpet script which offered this feature however the position did not persist and was forgotten when the server restarted. I decided to implement my own version which saves the survival position against the player.

* Type: `boolean`
* Default value: `false`
* Required options: `true`, `false`
* Categories: `SURVIVAL`, `TOTO`

### piglinsAngerOnChestUse
Piglins anger on a player who interacts a chest.  
Ever just minding your own business in the nether when you thoughtlessly place down a shulker box and open it experience the wrath of piglin bystanders? Well not anymore!

* Type: `boolean`
* Default value: `true`
* Required options: `true`, `false`
* Categories: `SURVIVAL`, `TOTO`


