package coathier.scalingmobs;

// import java.util.LinkedList;
// import java.util.List;
import java.util.Optional;

import net.minecraft.util.math.Vec3d;

public class ScalingValue {
  private static final int WATER_LEVEL = 63;

  public Float start;
  public Float max;
  public Float min;
  public ScalingFactor scalingFactor;
  public ScalingType scalingType;
  public Float increaseFactor;
  public Integer startingFrom;

  public Integer activeNth;
  public Time timeWhenActive;
  // public List<String> dimensionFilter;

  public enum ScalingType {
    LINEAR, EXPONENTIAL, CONSTANT
  }

  public enum ScalingFactor {
    DAYS, DISTANCE_FROM_SPAWN, LUNAR_PHASE, DEPTH, HEIGHT
  }

  public enum Time {
    DAY, NIGHT, BOTH
  }

  public Optional<Float> calculateValue(Vec3d position, long time, String dimension) {
    // boolean isInFilter = false;
    // for (String filter : dimensionFilter) {
    //   if (dimension == filter) {
    //     isInFilter = true;
    //     break;
    //   }
    // }
    // if (!isInFilter) return Optional.empty();

    switch (this.timeWhenActive) {
      case DAY:
        if (Util.isNight(time)) return Optional.empty();
        break;
      case NIGHT:
        if (!Util.isNight(time)) return Optional.empty();
        break;
      case BOTH:
    }

    int scalingValue = 1;
    switch (this.scalingFactor) {
      case DAYS:
        scalingValue = Util.daysPassed(time);
        break;
      case DISTANCE_FROM_SPAWN:
        scalingValue = (int)Math.sqrt(position.x * position.x + position.z * position.z);
        break;
      case LUNAR_PHASE:
        // 0 = FULL_MOON
        // SHRINKING
        // 4 = NEW_MOON / NO MOON
        // GROWING
        scalingValue = Math.abs((Util.daysPassed(time) % 8) - 4);
        break;
      case DEPTH:
        if ((int)position.y < WATER_LEVEL) {
          scalingValue = Math.abs((int)position.y - WATER_LEVEL);
        } else {
          return Optional.empty();
        }
        break;
      case HEIGHT:
        if ((int)position.y > WATER_LEVEL) {
          scalingValue = (int)position.y - WATER_LEVEL;
        } else {
          return Optional.empty();
        }
      }

    if (scalingValue % activeNth != 0) return Optional.empty();
    if (scalingValue < startingFrom) return Optional.empty();
    scalingValue -= startingFrom;

    float value = this.start;
    switch (this.scalingType) {
      case LINEAR:
        value = this.start + this.increaseFactor * scalingValue;
        break;
      case EXPONENTIAL:
        value = this.start * (float)Math.pow(this.increaseFactor, scalingValue);
        break;
      case CONSTANT:
        value = this.increaseFactor;
    }
    if (value > this.max) {
      return Optional.of(this.max);
    } else if (value < this.min) {
      return Optional.of(this.min);
    } else {
      return Optional.of(value);
    }
  }

  public void setDefaults() {
    if (this.scalingType == null) {
      this.scalingType = ScalingType.LINEAR;
      Scalingmobs.LOGGER.info("scalingType is either not set or incorrectly configured! (defaulting to \"LINEAR\")");
    }
    if (this.activeNth == null) {
      this.activeNth = 1;
    }
    if (this.activeNth == 0) {
      this.activeNth = 1;
      Scalingmobs.LOGGER.info("activeNth is incorrectly configured, it can't be \"0\"! (defaulting to \"1\")");
    }
    if (this.scalingFactor == null) {
      this.scalingFactor = ScalingFactor.DAYS;
      Scalingmobs.LOGGER.info("scalingFactor is either not set or incorrectly configured! (defaulting to \"DAYS\")");
    }
    // if (this.dimensionFilter == null) {
    //   this.dimensionFilter = new LinkedList<>();
    //   this.dimensionFilter.add("minecraft:overworld");
    //   Scalingmobs.LOGGER.info("dimensionFilter is incorrectly configured!");
    // }
    if (this.timeWhenActive == null) {
      this.timeWhenActive = Time.NIGHT;
      Scalingmobs.LOGGER.info("timeWhenActive is either not set or incorrectly configured! (defaulting to \"NIGHT\")");
    }
    if (this.start == null) {
      this.start = 0f;
    }
    if (this.max == null) {
      this.max = Float.MAX_VALUE;
    }
    if (this.min == null) {
      this.min = 0f;
    }
    if (this.startingFrom == null) {
      this.startingFrom = 0;
    }
    if (this.increaseFactor == null) {
      this.increaseFactor = 1.1f;
    }
  }
}
