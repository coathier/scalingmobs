package coathier.scalingmobs;

import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class EventHandler {
  public static void onEntityLoad(Entity entity , ServerWorld level) {
    if (level.isClient) return;
    if (!(entity instanceof HostileEntity mob)) return;

    ScalingMobsConfig config = ScalingMobsConfig.INSTANCE;

    Vec3d position = mob.getPos();
    long time = mob.getWorld().getTimeOfDay();
    String dimension = mob.getWorld().getRegistryKey().getValue().toString();
    String name = Registries.ENTITY_TYPE.getId(mob.getType()).toString();

    float value;
    boolean anyApplied = false;

    if (!config.health.isEmpty()) {
      value = config.healthAddOnTopOfDefault ? (float)mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).getBaseValue() : 0;

      for (ScalingValue health : config.health) {
        Optional<Float> scaledHealth = health.calculateValue(position, time, dimension, name);
        if (scaledHealth.isEmpty()) continue;
        anyApplied = true;
        value += scaledHealth.get();
      }
      if (anyApplied) {
        mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(value);
        mob.setHealth(mob.getMaxHealth());
      }
    }

    anyApplied = false;
    if (!config.damage.isEmpty()) {
      value = config.damageAddOnTopOfDefault ? (float)mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getBaseValue() : 0;

      for (ScalingValue damage: config.damage) {
        Optional<Float> scaledDamage = damage.calculateValue(position, time, dimension, name);
        if (scaledDamage.isEmpty()) continue;
        anyApplied = true;
        value += scaledDamage.get();
      }

      if (anyApplied) {
        mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).getValue();
        mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(value);
      }
    }

    anyApplied = false;
    if (!config.speed.isEmpty()) {
      value = config.speedAddOnTopOfDefault ? (float)mob.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).getBaseValue() : 0;

      for (ScalingValue speed: config.speed) {
        Optional<Float> scaledSpeed = speed.calculateValue(position, time, dimension, name);
        if (scaledSpeed.isEmpty()) continue;
        anyApplied = true;
        value += scaledSpeed.get();
      }

      if (anyApplied) {
        mob.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(value);
      }
    }
  }
}


