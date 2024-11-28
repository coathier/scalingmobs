package coathier.scalingmobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class Scalingmobs implements ModInitializer {
	public static final String MOD_ID = "scalingmobs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		AutoConfig.register(ScalingMobsConfig.class, Toml4jConfigSerializer::new);
		ServerEntityEvents.ENTITY_LOAD.register(EventHandler::onEntityLoad);
	}
}
