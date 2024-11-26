package coathier.scalingmobs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class Scalingmobs implements ModInitializer {
	@Override
	public void onInitialize() {
		AutoConfig.register(ScalingMobsConfig.class, Toml4jConfigSerializer::new);
		ServerEntityEvents.ENTITY_LOAD.register(EventHandler::onEntityLoad);
	}
}
