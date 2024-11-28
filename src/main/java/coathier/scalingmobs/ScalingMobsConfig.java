package coathier.scalingmobs;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name="scalingmobs")
public class ScalingMobsConfig implements ConfigData {
  public int activeNthDay = 7;

  public ScalingValue health = new ScalingValue(20, 150, 0, ScalingValue.ScalingType.LINEAR, 1.1f, 2, true); // 26 health at 21 days
  public ScalingValue damage = new ScalingValue(3, 20, 0, ScalingValue.ScalingType.LINEAR, 1.1f, 1, true); // 6 damage at 21 days
  public ScalingValue speed = new ScalingValue(0.20f, 0.3f, 0.1f, ScalingValue.ScalingType.LINEAR, 1.1f, 0.01f, true); // 0.23 speed at 23 days

  public class ScalingValue {
    public ScalingValue(float start, float max, float min, ScalingType scalingType, float exponentialIncrease, float linearIncrease, boolean scaleByActiveDays) {
      this.start = start;
      this.max = max;
      this.min = min;
      this.scalingType = scalingType;
      this.exponentialIncrease = exponentialIncrease;
      this.linearIncrease = linearIncrease;
      this.scaleByActiveDays = scaleByActiveDays;
    }

    public float start = 20;
    public float max = 150.0f;
    public float min = 0.0f;
    public ScalingType scalingType = ScalingType.LINEAR;
    public float exponentialIncrease = 1.1f;
    public float linearIncrease = 2;
    public boolean scaleByActiveDays = false;

    public enum ScalingType {
      LINEAR, EXPONENTIAL
    }

    public float calculateValue(long time) {
      int scalingValue;
      if (this.scaleByActiveDays) {
        scalingValue = Util.daysPassed(time) / ScalingMobsConfig.this.activeNthDay;
      } else {
        scalingValue = Util.daysPassed(time);
      }

      float value = this.start;
      switch (this.scalingType) {
        case LINEAR:
          value = this.start + this.linearIncrease * scalingValue;
          break;
        case EXPONENTIAL:
          value = this.start * (float)Math.pow(this.exponentialIncrease, scalingValue);
      }
      if (value > this.max) {
        return this.max;
      } else if (value < this.min) {
        return this.min;
      } else {
        return value;
      }
    }
  }
}
