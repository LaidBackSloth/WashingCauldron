package com.laidbacksloth.washingcauldron;

import com.laidbacksloth.washingcauldron.config.ModCommonConfigs;
import com.laidbacksloth.washingcauldron.recipe.ModRecipes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("washingcauldron")
public class WashingCauldron {
    public static final String MOD_ID = "washingcauldron";

    public WashingCauldron() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModRecipes.register(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfigs.SPEC, "washingcauldron-common.toml");
    }
}
