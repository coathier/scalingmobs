package coathier.scalingmobs;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;

public class EventHandler {
  public static void onEntityLoad(Entity entity , ServerWorld level) {
    if (entity.getWorld().isClient) return;
    if (!(entity instanceof HostileEntity mob)) return;
    long daysPassed = Util.daysPassed(mob.getWorld().getTime());
    if (!(daysPassed % 7 == 0)) return;

    SMConfig config = AutoConfig.getConfigHolder(SMConfig.class).getConfig();

    // TODO: Create a function to calculated the scaled values.
    float scaledHealth = config.startingHealth;

    mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(scaledHealth);
    mob.setHealth(mob.getMaxHealth());

    float scaledDamage = config.startingDamage;

    mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(scaledDamage);
  }
}


