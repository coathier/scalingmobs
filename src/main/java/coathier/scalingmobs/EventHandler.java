package coathier.scalingmobs;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;

public class EventHandler {
  public static void onEntityLoad(Entity entity , ServerWorld level) {
    long time = entity.getWorld().getTimeOfDay();
    int daysPassed = Util.daysPassed(time);

    ScalingMobsConfig config = AutoConfig.getConfigHolder(ScalingMobsConfig.class).getConfig();

    if (daysPassed == 0 && config.activeNthDay != 1) return;
    if (!(daysPassed % config.activeNthDay == 0)) return;

    if (level.isClient) return;
    if (!(entity instanceof HostileEntity mob)) return;
    if (entity instanceof WardenEntity) return;

    float scaledHealth = config.health.calculateValue(mob.getPos(), time, config.activeNthDay);
    mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(scaledHealth);
    mob.setHealth(mob.getMaxHealth());

    float scaledDamage = config.damage.calculateValue(mob.getPos(), time, config.activeNthDay);
    mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(scaledDamage);

    float scaledSpeed = config.speed.calculateValue(mob.getPos(), time, config.activeNthDay);
    mob.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(scaledSpeed);

    // Scalingmobs.LOGGER.info(
	   //  "Day: " + daysPassed + "\n" +
	   //  "Active: " + (daysPassed % config.activeNthDay == 0 ? "True" : "False") + "\n" +
	   //  "Health: " + mob.getMaxHealth() + "\n" +
	   //  "Damage: " + scaledDamage + "\n" +
	   //  "Speed: " + scaledSpeed);
  }
}


