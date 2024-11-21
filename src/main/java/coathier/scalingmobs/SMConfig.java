package coathier.scalingmobs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name="scalingmobs")
public class SMConfig implements ConfigData {
  public float startingHealth;
  public float startingDamage;
  public ScalingType scalingType;

  public enum ScalingType {
    LINEAR, EXPONENTIAL, LOGARITHMIC
  }
}
