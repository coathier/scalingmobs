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

    int daysPassed = Util.daysPassed(mob.getWorld().getTime());

    SMConfig config = AutoConfig.getConfigHolder(SMConfig.class).getConfig();

    if (!(daysPassed % config.activeNthDay == 0)) return;

    float scaledHealth = config.calculateScalingHealth(mob.getWorld().getTime());
    mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(scaledHealth);
    mob.setHealth(mob.getMaxHealth());

    float scaledDamage = config.calculateScalingDamage(mob.getWorld().getTime());
    mob.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(scaledDamage);
  }
}


