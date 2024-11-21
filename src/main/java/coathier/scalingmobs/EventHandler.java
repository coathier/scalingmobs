package coathier.scalingmobs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;

public class EventHandler {
  public static void onEntityLoad(Entity entity , ServerWorld level) {
    if (!(entity instanceof HostileEntity mob)) return;
    if (!(Util.isSeventhNight(mob.getWorld().getTime()))) return;

    mob.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(100.0f);
    mob.setHealth(mob.getMaxHealth());
  }
}


