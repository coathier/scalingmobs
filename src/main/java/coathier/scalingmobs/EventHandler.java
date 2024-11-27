package coathier.scalingmobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;

public class EventHandler {
	public static final String MOD_ID = "scalingmobs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  public static void onEntityLoad(Entity entity , ServerWorld level) {
    if (level.isClient) return;
    if (!(entity instanceof HostileEntity mob)) return;
    if (entity instanceof WardenEntity) return;

    long time = mob.getWorld().getTimeOfDay();
    int daysPassed = Util.daysPassed(time);
    LOGGER.info("Day: " + daysPassed);

    ScalingMobsConfig config = AutoConfig.getConfigHolder(ScalingMobsConfig.class).getConfig();

    LOGGER.info("Active: " + (daysPassed % config.activeNthDay == 0 ? "True" : "False"));

    if (!(daysPassed % config.activeNthDay == 0)) return;

    float scaledHealth = config.health.calculateValue(time);
    mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(scaledHealth);
    mob.setHealth(mob.getMaxHealth());

    LOGGER.info("Health: " + mob.getMaxHealth());

    float scaledDamage = config.damage.calculateValue(time);
    mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(scaledDamage);

    LOGGER.info("Damage: " + scaledDamage);

    float scaledSpeed = config.speed.calculateValue(time);
    mob.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(scaledSpeed);

    LOGGER.info("Speed: " + scaledSpeed);
  }
}


