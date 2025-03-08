package coathier.scalingmobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

public class Scalingmobs implements ModInitializer {
	public static final String MOD_ID = "scalingmobs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		new ScalingMobsConfig().load();
		ServerEntityEvents.ENTITY_LOAD.register(EventHandler::onEntityLoad);
	}
}
