# Scaling Mobs
This is a server side mod that allows for scaling of the attributes health, damage and speed. You can configure it through a file and choose on what days the attributes should be applied/active.

Features include, filtering by mobs and dimensions, scaling by days passed, moon cycle, distance from spawn, distance under sea level. You can scale linearly, exponentially or just apply a constant. You also configure when it is active, for exaple only at nights or every 7 days.

Here's a list of the configuration options that can be found in the `scalingmobs.toml` file within the configuration directory.
| Name                | Value                                                                         | Description                                                                                                                                                                                                                                                                                                             |
| ------------------- | ----------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| start               | Float *(default 0)*                                                             | The start value of scaling                                                                                                                                                                                                                                                                                              |
| max                 | Float *(default MAX_FLOAT)*                                                     | The maximum value it can scale to                                                                                                                                                                                                                                                                                       |
| min                 | Float *(default 0)*                                                             | The minimum value it can scale to                                                                                                                                                                                                                                                                                       |
| scalingFactor       | "DAYS","DISTANCE_FROM_SPAWN","LUNAR_PHASE","DEPTH","HEIGHT" *(default "DAYS")* | If scaling by "DAYS" it will increase the scaling factor/variable once per day. "DISTANCE_FROM_SPAWN" will scale by blocks from (0, 0) of where the mob spawned. (Useful to pull out a graphing calculator since it's hard to get right)      |
| scalingType         | "LINEAR","EXPONENTIAL","CONSTANT" *(default "LINEAR")*                          | Should it scale linearly or exponentially                                                                                                                                                                                                                                                                               |
| increaseFactor      | Float *(default 1.1)*                                                           | If scalingType is linear you can calculate final value approcimately by (start + scalingFactor * increaseFactor). If exponential then (start * increaseFactor ^ scalingFactor). If constant (increaseFactor) |
| startingFrom        | Integer *(default 0)*                                                           | Determines when the scaling modifier start and is applied depending on the scalingFactor |
| activeNth           | Integer *(default 1)*                                                           | A number of 3 means that the attributes will be applied every 3rd value of the scalingFactor. For scaling to  be applied this number should be 1. (Do not put it to 0, that will crash the game.)                                                                                                                                        |
| timeWhenActive      | "DAY","NIGHT","BOTH" *(default "NIGHT")*                                                          | Determines when the modifier should be active, If "DAY" then it will only be applied at day time. |
| mobFilterType/dimensionFilterType | "INCLUDE","EXCLUDE" *(default "EXCLUDE")*                         | Determines if the filters should include or exlude for their respective category. |
| mobFilter/dimensionFilter| String List *(default empty)*                         | What shoud the filter contain e.g ["minecraft:wither", "minecraft:zombie"] |

```toml
# This is a example config. Each comment describes what the
# setting/modifier under it does

# All health modifiers get applied on top of vanilla/default
# attributes if this variable is set to true
healthAddOnTopOfDefault = true

# All damage and speed modifiers will be applied from 0, without the
# vanilla attributes
damageAddOnTopOfDefault = false
speedAddOnTopOfDefault = false

# Modifiers where "timeWhenActive" isn't set are only
# active at night because that variables default value is "NIGHT"
# and is set implicitly.

# For every block below Y=63 mobs gain hearts exponentially
# This health modifier follows the formula:
# health = start * (increaseFactor ^ blocksBelowSeaLevel)
# health = 1 * (1.04 ^ blocksBelowSeaLevel)
[[health]]
start = 1
scalingFactor = "DEPTH"
scalingType = "EXPONENTIAL"
increaseFactor = 1.04

# https://minecraft.fandom.com/wiki/Moon
# At new moon the scaling is 0, at full moon it's 4
# You probably want to limit this to the overworld
# since there no actual moon in the nether or end dimension
[[speed]]
dimensionFilterType = "INCLUDE"
dimensionFilter = [ "minecraft:overworld" ]
start = 0.15
scalingFactor = "LUNAR_PHASE"
scalingType = "LINEAR"
increaseFactor = 0.05

# As you can see below one attribute can have as many different
# scaling modifiers as you want, here damage is both changed
# by days passed and the mobs distance from spawn

# Everyday mobs do +0.5 more damage starting from 3 damage.
# This scaling will at most only make mobs do 10 extra damage.
# The scaling is only active every other day i.e day 1,3,5...
# This damage modifier follows the formula:
# damage = start + daysPassed * increaseFactor
# damage = 3.0 + daysPassed * 0.5
# Don't forget that this damage is added to the mobs default
# damage attribute because we set the "damageAddOnTopOfDefault" variable to true
[[damage]]
activeNth = 2
start = 3.0
max = 10.0
scalingFactor = "DAYS"
scalingType = "LINEAR"
increaseFactor = 0.5

# Beyond 500 blocks from spawn (X=0, Z=0) mobs do 10 extra damage
# both day and night
[[damage]]
scalingFactor = "DISTANCE_FROM_SPAWN"
scalingType = "CONSTANT"
increaseFactor = 10
startingFrom = 500
timeWhenActive = "BOTH"

# Makes zombies and creepers move quite fast, anything above 0.7 is
# ridiculously fast
[[speed]]
mobFilterType = "INCLUDE"
mobFilter = [ "minecraft:zombie", "minecraft:creeper" ]
scalingType = "CONSTANT"
increaseFactor = 0.4
timeWhenActive = "BOTH"

# Makes mobs stand still in all dimensions except for the overworld.
# Since scalingFactor and activeNth default to "DAYS" and 1 this will
# always be active
[[speed]]
dimensionFilterType = "EXCLUDE"
dimensionFilter = [ "minecraft:overworld" ]
scalingType = "CONSTANT"
scalingFactor = "DAYS"
increaseFactor = 0
timeWhenActive = "BOTH" # This value can be "DAY" or "NIGHT". "NIGHT" is default
```
