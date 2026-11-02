package net.not_assher.apostate.core.cca;

import net.not_assher.apostate.core.cca.entity.PlayerComponent;
import net.not_assher.apostate.core.cca.entity.VowbreakComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

/**
 * @author Chemthunder
 */
public class ModCCA implements EntityComponentInitializer {
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry cca) {
        cca.registerForPlayers(
                PlayerComponent.KEY,
                PlayerComponent::new,
                RespawnCopyStrategy.ALWAYS_COPY
        );

        cca.registerForPlayers(
                VowbreakComponent.KEY,
                VowbreakComponent::new,
                RespawnCopyStrategy.NEVER_COPY
        );
    }
}
