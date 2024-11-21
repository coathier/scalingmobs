package coathier.scalingmobs;

import net.fabricmc.api.ModInitializer;

public class Scalingmobs implements ModInitializer {
	public static final String MOD_ID = "scalingmobs";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
    ServerEntityEvents.ENTITY_LOAD.register(EventHandler::onEntityLoad);
	}
}
