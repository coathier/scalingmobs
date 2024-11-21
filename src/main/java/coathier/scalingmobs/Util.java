package coathier.scalingmobs;

public class Util {
  public static int daysPassed(long time) {
    final long DAY_TICKS = 24000;
    final long TICKS_PASSED = time - time % DAY_TICKS;
    return (int)(TICKS_PASSED / DAY_TICKS);
  }
}

