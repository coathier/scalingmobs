package coathier.scalingmobs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name="scalingmobs")
public class SMConfig implements ConfigData {
  public int activeNthDay = 1;

  public float startingHealth = 20;
  public ScalingType healthScalingType = ScalingType.LINEAR;
  public float healthExponentialIncrease = 1.1f;
  public float healthlinearIncrease = 2;
  public float healthValueMax = 150.0f;
  public boolean healthScaleByActiveDays = false;
  

  public float startingDamage = 3;
  public ScalingType damageScalingType = ScalingType.LINEAR;
  public float damageExponentialIncrease = 1.1f;
  public float damagelinearIncrease = 2;
  public float damageValueMax = 150.0f;
  public boolean damageScaleByActiveDays = false;

  public enum ScalingType {
    LINEAR, EXPONENTIAL
  }

  public float calculateScalingHealth(long time) {
    int scalingValue;
    if (this.healthScaleByActiveDays) {
      scalingValue = Util.daysPassed(time) / this.activeNthDay;
    } else {
      scalingValue = Util.daysPassed(time);
    }

    switch (this.healthScalingType) {
      case LINEAR:
        return this.startingHealth + this.healthlinearIncrease * scalingValue;
      case EXPONENTIAL:
        return this.startingHealth + this.startingHealth * (float)Math.pow(this.healthExponentialIncrease, scalingValue);
    }
    return this.startingHealth;
  }

  public float calculateScalingDamage(long time) {
    int scalingValue;
    if (this.damageScaleByActiveDays) {
      scalingValue = Util.daysPassed(time) / this.activeNthDay;
    } else {
      scalingValue = Util.daysPassed(time);
    }

    switch (this.damageScalingType) {
      case LINEAR:
        return this.startingDamage + this.damagelinearIncrease * scalingValue;
      case EXPONENTIAL:
        return this.startingDamage + this.startingDamage * (float)Math.pow(this.damageExponentialIncrease, scalingValue);
    }
    return this.startingDamage;
  }
}
