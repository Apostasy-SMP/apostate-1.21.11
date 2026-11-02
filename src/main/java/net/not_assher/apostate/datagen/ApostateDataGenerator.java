package net.not_assher.apostate.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.not_assher.apostate.core.index.data.ModDamageTypes;
import net.not_assher.apostate.datagen.providers.*;

/**
 * @author Chemthunder
 */
public class ApostateDataGenerator implements DataGeneratorEntrypoint {
	public void onInitializeDataGenerator(FabricDataGenerator fdg) {
        var pack = fdg.createPack();

        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModLanguageProvider::new);

        pack.addProvider(ModAdvancementProvider::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModBlockLootTableProvider::new);

        pack.addProvider(ModDynamicRegistryProvider::new);

        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModDamageTypeTagProvider::new);
	}

    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, ModDamageTypes.builder::bootstrap);
    }
}
