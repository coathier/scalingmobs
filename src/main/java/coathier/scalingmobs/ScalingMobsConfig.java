package coathier.scalingmobs;

import coathier.scalingmobs.ScalingMobsConfig.ScalingValue.ScalingFactor;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.util.math.Vec3d;

@Config(name="scalingmobs")
public class ScalingMobsConfig implements ConfigData {
  public int activeNthDay = 1;

  public ScalingValue health = new ScalingValue(20, 150, 0, ScalingValue.ScalingType.LINEAR, ScalingFactor.DAYS, 1.1f, 2, true); // 26 health at 21 days
  public ScalingValue damage = new ScalingValue(3, 20, 0, ScalingValue.ScalingType.LINEAR, ScalingFactor.DAYS, 1.1f, 1, true); // 6 damage at 21 days
  public ScalingValue speed = new ScalingValue(0.20f, 0.3f, 0.1f, ScalingValue.ScalingType.LINEAR, ScalingFactor.DAYS, 1.1f, 0.01f, true); // 0.23 speed at 23 days

  public class ScalingValue {
    public ScalingValue(float start, float max, float min, ScalingType scalingType, ScalingFactor scalingFactor, float exponentialIncrease, float linearIncrease, boolean scaleByActiveDays) {
      this.start = start;
      this.max = max;
      this.min = min;
      this.scalingType = scalingType;
      this.exponentialIncrease = exponentialIncrease;
      this.linearIncrease = linearIncrease;
    }

    public float start = 20;
    public float max = 150.0f;
    public float min = 0.0f;
    public ScalingFactor scalingFactor = ScalingFactor.DAYS;
    public ScalingType scalingType = ScalingType.LINEAR;
    public float exponentialIncrease = 1.1f;
    public float linearIncrease = 2;

    public enum ScalingType {
      LINEAR, EXPONENTIAL
    }

    public enum ScalingFactor {
      DAYS, ACTIVE_DAYS, DISTANCE_FROM_SPAWN
    }

    public float calculateValue(Vec3d position, long time, int activeNthDay) {
      int scalingValue = 1;
      switch (this.scalingFactor) {
        case DAYS:
          scalingValue = (int)time;
          break;
        case ACTIVE_DAYS:
          scalingValue = Util.daysPassed(time) / activeNthDay;
          break;
        case DISTANCE_FROM_SPAWN:
          scalingValue = (int)Math.sqrt(position.x * position.x + position.z * position.z);
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
