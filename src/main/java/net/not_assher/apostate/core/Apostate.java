package net.not_assher.apostate.core;

import eu.midnightdust.lib.config.MidnightConfig;
import net.acoyt.acornlib.api.ALib;
import net.acoyt.acornlib.api.event.BetterItemTooltipEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import net.not_assher.apostate.core.command.FlexCommand;
import net.not_assher.apostate.core.command.NicknameCommand;
import net.not_assher.apostate.core.command.StatusCommand;
import net.not_assher.apostate.core.command.VowbreakCommand;
import net.not_assher.apostate.core.event.ApplyApostateAdvancementEvent;
import net.not_assher.apostate.core.event.DebugRelogEvent;
import net.not_assher.apostate.core.event.block.CovenantBellEvents;
import net.not_assher.apostate.core.event.block.LightCrimsonCandleEvent;
import net.not_assher.apostate.core.index.*;
import net.not_assher.apostate.core.item.*;
import net.not_assher.apostate.core.networking.ModNetworking;
import net.not_assher.apostate.core.utilities.LootTableModifiers;
import net.not_assher.apostate.ext.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author Chemthunder
 */
public class Apostate implements ModInitializer {
    public static final String MOD_ID = "apostate";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final int MAIN_COLOR = 0xFF621414;

    public void onInitialize() {
        FabricLoader loader = FabricLoader.getInstance();
        Optional<ModContainer> container = loader.getModContainer(MOD_ID);

        LOGGER.info("Apostate Init");

        ModItems.init();
        ModComponentTypes.init();
        ModItemGroups.init();
        ModCriterions.init();
        ModBlocks.init();
        ModBlockEntityTypes.init();

        ModNetworking.init();
        ModNetworking.c2s();

        LootTableModifiers.init();

        BetterItemTooltipEvent.EVENT.register(new BountyPosterItem.Tooltip());
        BetterItemTooltipEvent.EVENT.register(new PactCrystalItem.Tooltip());
        BetterItemTooltipEvent.EVENT.register(new FlyerItem.Tooltip());
        BetterItemTooltipEvent.EVENT.register(new BountyBookItem.Tooltip());
        BetterItemTooltipEvent.EVENT.register(new CordialVowItem.Tooltip());

        CommandRegistrationCallback.EVENT.register(new NicknameCommand());
        CommandRegistrationCallback.EVENT.register(new StatusCommand());
        CommandRegistrationCallback.EVENT.register(new FlexCommand());
        CommandRegistrationCallback.EVENT.register(new VowbreakCommand());

        ServerPlayerEvents.JOIN.register(new DebugRelogEvent());
        ServerPlayerEvents.JOIN.register(new ApplyApostateAdvancementEvent());

        UseBlockCallback.EVENT.register(new LightCrimsonCandleEvent());

        UseBlockCallback.EVENT.register(new CovenantBellEvents.UseBlock());
        PlayerBlockBreakEvents.AFTER.register(new CovenantBellEvents.AfterBroken());

        container.ifPresent(mod ->
                ResourceLoader.registerBuiltinPack(
                        id("old_pact_crystal"),
                        mod,
                        PackActivationType.NORMAL
                )
        );

        ALib.registerModMenu(MOD_ID, MAIN_COLOR);

        MidnightConfig.init(MOD_ID, ModConfig.class);

        LOGGER.info("Init completed successfully!");
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
