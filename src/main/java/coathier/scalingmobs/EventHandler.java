package coathier.scalingmobs;

import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class EventHandler {
  public static void onEntityLoad(Entity entity , ServerWorld level) {
    if (level.isClient) return;
    if (!(entity instanceof HostileEntity mob)) return;
    if (entity instanceof WardenEntity) return;
    if (entity instanceof WitherEntity) return;

    ScalingMobsConfig config = ScalingMobsConfig.INSTANCE;

    Vec3d position = mob.getPos();
    long time = mob.getWorld().getTimeOfDay();
    String dimension = mob.getWorld().getDimensionEntry().getIdAsString();

    float value;
    boolean anyApplied = false;

    if (!config.health.isEmpty()) {
      value = config.healthAddOnTopOfDefault ? (float)mob.getAttributeInstance(EntityAttributes.MAX_HEALTH).getBaseValue() : 0;

      for (ScalingValue health : config.health) {
        Optional<Float> scaledHealth = health.calculateValue(position, time, dimension);
        if (scaledHealth.isEmpty()) continue;
        anyApplied = true;
        value += scaledHealth.get();
      }
      if (anyApplied) {
        mob.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(value);
        mob.setHealth(mob.getMaxHealth());
      }
    }

    anyApplied = false;
    if (!config.damage.isEmpty()) {
      value = config.damageAddOnTopOfDefault ? (float)mob.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).getBaseValue() : 0;

      for (ScalingValue damage: config.damage) {
        Optional<Float> scaledDamage = damage.calculateValue(position, time, dimension);
        if (scaledDamage.isEmpty()) continue;
        anyApplied = true;
        value += scaledDamage.get();
      }

      if (anyApplied) {
        mob.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).getValue();
        mob.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(value);
      }
    }

    anyApplied = false;
    if (!config.speed.isEmpty()) {
      value = config.speedAddOnTopOfDefault ? (float)mob.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).getBaseValue() : 0;

      for (ScalingValue speed: config.speed) {
        Optional<Float> scaledSpeed = speed.calculateValue(position, time, dimension);
        if (scaledSpeed.isEmpty()) continue;
        anyApplied = true;
        value += scaledSpeed.get();
      }

      if (anyApplied) {
        mob.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(value);
      }
    }
  }
}


