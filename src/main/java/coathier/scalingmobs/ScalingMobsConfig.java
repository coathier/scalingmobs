package coathier.scalingmobs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.moandjiezana.toml.Toml;

public class ScalingMobsConfig {
  
  public static ScalingMobsConfig INSTANCE;

  public List<ScalingValue> health;
  public List<ScalingValue> damage;
  public List<ScalingValue> speed;

  public Boolean healthAddOnTopOfDefault = false;
  public Boolean damageAddOnTopOfDefault = false;
  public Boolean speedAddOnTopOfDefault = false;

  public void load() {
    health = new LinkedList<>();
    damage = new LinkedList<>();
    speed = new LinkedList<>();

    File file = new File("config/" + Scalingmobs.MOD_ID + ".toml");
    if (!file.exists()) {
      try {
        File dir = new File("config");
        if (!dir.exists()) dir.mkdir();

        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write("""
        # EDIT THIS FILE, IT'S ONLY A EXAMPLE!
        # This is a example config. Each comment describes what the
        # setting/modifier under it does

        # All health modifiers get applied on top of vanilla/default
        # attributes if this variable is set to true
        healthAddOnTopOfDefault = true

        # All damage and speed modifiers will be applied from 0, without the
        # vanilla attributes
        damageAddOnTopOfDefault = false
        speedAddOnTopOfDefault = false

        # Modifiers where \"timeWhenActive\" isn't set are only
        # active at night because that variables default value is \"NIGHT\"
        # and is set implicitly.

        # For every block below Y=63 mobs gain hearts exponentially
        # This health modifier follows the formula:
        # health = start * (increaseFactor ^ blocksBelowSeaLevel)
        # health = 1 * (1.04 ^ blocksBelowSeaLevel)
        [[health]]
        start = 1
        scalingFactor = \"DEPTH\"
        scalingType = \"EXPONENTIAL\"
        increaseFactor = 1.04

        # https://minecraft.fandom.com/wiki/Moon
        # At new moon the scaling is 0, at full moon it's 4
        # You probably want to limit this to the overworld
        # since there no actual moon in the nether or end dimension
        [[speed]]
        dimensionFilterType = \"INCLUDE\"
        dimensionFilter = [ \"minecraft:overworld\" ]
        start = 0.15
        scalingFactor = \"LUNAR_PHASE\"
        scalingType = \"LINEAR\"
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
        # damage attribute because we set the \"damageAddOnTopOfDefault\" variable to true
        [[damage]]
        activeNth = 2
        start = 3.0
        max = 10.0
        scalingFactor = \"DAYS\"
        scalingType = \"LINEAR\"
        increaseFactor = 0.5

        # Beyond 500 blocks from spawn (X=0, Z=0) mobs do 10 extra damage
        # both day and night
        [[damage]]
        scalingFactor = \"DISTANCE_FROM_SPAWN\"
        scalingType = \"CONSTANT\"
        increaseFactor = 10
        startingFrom = 500
        timeWhenActive = \"BOTH\"

        # Makes zombies and creepers move quite fast, 0.4 is
        # already ridiculously fast
        [[speed]]
        mobFilterType = \"INCLUDE\"
        mobFilter = [ \"minecraft:zombie\", \"minecraft:creeper\" ]
        scalingType = \"CONSTANT\"
        increaseFactor = 0.2
        timeWhenActive = \"BOTH\"

        # Makes mobs stand still in all dimensions except for the overworld.
        # Since scalingFactor and activeNth default to \"DAYS\" and 1 this will
        # always be active
        [[speed]]
        dimensionFilterType = \"EXCLUDE\"
        dimensionFilter = [ \"minecraft:overworld\" ]
        scalingType = \"CONSTANT\"
        scalingFactor = \"DAYS\"
        increaseFactor = 0
        timeWhenActive = \"BOTH\" # This value can be \"DAY\" or \"NIGHT\". \"NIGHT\" is default
        """);
        writer.close();
      } catch (IOException e) {
        Scalingmobs.LOGGER.error(e.toString());
        Scalingmobs.LOGGER.error("Error while writing trying to write default config");
      }
    }

    Toml config =  new Toml().read(file);

    healthAddOnTopOfDefault = config.getBoolean("healthAddOnTopOfDefault", false);
    damageAddOnTopOfDefault = config.getBoolean("damageAddOnTopOfDefault", false);
    speedAddOnTopOfDefault = config.getBoolean("speedAddOnTopOfDefault", false);

    try {
      List<Toml> tables = config.getTables("health");
      if (tables != null) {
        for (Toml table : tables) {
          ScalingValue scalingValue = table.to(ScalingValue.class);
          scalingValue.setDefaults();
          health.add(scalingValue);
        }
      }

      tables = config.getTables("damage");
      if (tables != null) {
        for (Toml table : tables) {
          ScalingValue scalingValue = table.to(ScalingValue.class);
          scalingValue.setDefaults();
          damage.add(scalingValue);
        }
      }

      tables = config.getTables("speed");
      if (tables != null) {
        for (Toml table : tables) {
          ScalingValue scalingValue = table.to(ScalingValue.class);
          scalingValue.setDefaults();
          speed.add(scalingValue);
        }
      }
    } catch (Exception e) {
      Scalingmobs.LOGGER.error(e.toString());
      Scalingmobs.LOGGER.error("Error reading config file, something is probably formatted incorrectly.");
    }

    ScalingMobsConfig.INSTANCE = this;
  }
}
