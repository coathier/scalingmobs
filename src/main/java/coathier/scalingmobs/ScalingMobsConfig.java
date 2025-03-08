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
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write("""
          # This is a example config. Each comment describes what the
          # setting/modifier under it does

          # All health modifiers get applied on top of vanilla/default attributes
          healthAddOnTopOfDefault = true

          # All damage and speed modifiers will be applied from 0, without the
          # vanilla attributes
          damageAddOnTopOfDefault = false
          speedAddOnTopOfDefault = false

          # Modifiers where \"timeWhenActive\" isn't set are only
          # active at night because its default value is \"NIGHT\".

          # For every block below Y=63 mobs gain hearts exponentially
          [[health]]
          start = 1
          scalingFactor = \"DEPTH\"
          scalingType = \"EXPONENTIAL\"
          increaseFactor = 1.04

          # https://minecraft.fandom.com/wiki/Moon
          # At new moon the scaling is 0, at full moon it's 4
          [[speed]]
          start = 0.15
          scalingFactor = \"LUNAR_PHASE\"
          scalingType = \"LINEAR\"
          increaseFactor = 0.05

          # As you can see below one attribute can have as many different
          # scaling modifiers as you want, here damage is both changed
          # by days and their distance from spawn

          # Everyday mobs do +0.5 more damage starting from 3 damage.
          # This scaling will at most only give 10 damage.
          # The scaling is only active every other day i.e day 1,3,5...
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
          timeWhenActive = \"BOTH\" # This value can be \"DAY\" or \"NIGHT\", it's default 
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
