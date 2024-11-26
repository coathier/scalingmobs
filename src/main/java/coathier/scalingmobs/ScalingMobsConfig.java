package coathier.scalingmobs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name="scalingmobs")
public class ScalingMobsConfig implements ConfigData {
  public int activeNthDay = 1;

  public float startingHealth = 20;
  public ScalingType healthScalingType = ScalingType.LINEAR;
  public float healthExponentialIncrease = 1.1f;
  public float healthLinearIncrease = 2;
  public float healthValueMax = 150.0f;
  public float healthValueMin = 0.0f;
  public boolean healthScaleByActiveDays = false;
  

  public float startingDamage = 3;
  public ScalingType damageScalingType = ScalingType.LINEAR;
  public float damageExponentialIncrease = 1.1f;
  public float damageLinearIncrease = 1;
  public float damageValueMax = 19.0f;
  public float damageValueMin = 0.0f;
  public boolean damageScaleByActiveDays = false;

  public float startingSpeed = 0.25f;
  public ScalingType speedScalingType = ScalingType.LINEAR;
  public float speedExponentialIncrease = 1.1f;
  public float speedLinearIncrease = -0.1f;
  public float speedValueMax = 19.0f;
  public float speedValueMin = 0.1f;
  public boolean speedScaleByActiveDays = false;

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

    float value = this.startingHealth;
    switch (this.healthScalingType) {
      case LINEAR:
        value = this.startingHealth + this.healthLinearIncrease * scalingValue;
        break;
      case EXPONENTIAL:
        value = this.startingHealth * (float)Math.pow(this.healthExponentialIncrease, scalingValue);
    }
    return Math.min(value, Math.max(this.healthValueMin, this.healthValueMax));
  }

  public float calculateScalingDamage(long time) {
    int scalingValue;
    if (this.damageScaleByActiveDays) {
      scalingValue = Util.daysPassed(time) / this.activeNthDay;
    } else {
      scalingValue = Util.daysPassed(time);
    }

    float value = this.startingDamage;
    switch (this.damageScalingType) {
      case LINEAR:
        value = this.startingDamage + this.damageLinearIncrease * scalingValue;
        break;
      case EXPONENTIAL:
        value = this.startingDamage * (float)Math.pow(this.damageExponentialIncrease, scalingValue);
    }
    return Math.min(value, Math.max(this.damageValueMin, this.damageValueMax));
  }

  public float calculateScalingSpeed(long time) {
    int scalingValue;
    if (this.speedScaleByActiveDays) {
      scalingValue = Util.daysPassed(time) / this.activeNthDay;
    } else {
      scalingValue = Util.daysPassed(time);
    }

    float value = this.startingSpeed;
    switch (this.speedScalingType) {
      case LINEAR:
        value = this.startingSpeed + this.speedLinearIncrease * scalingValue;
        break;
      case EXPONENTIAL:
        value = this.startingSpeed * (float)Math.pow(this.speedExponentialIncrease, scalingValue);
    }
    return Math.min(value, Math.max(this.speedValueMin, this.speedValueMax));
  }
}
