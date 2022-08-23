package com.laidbacksloth.washingcauldron.event;

import com.laidbacksloth.washingcauldron.WashingCauldron;
import com.laidbacksloth.washingcauldron.recipe.CauldronWashingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = WashingCauldron.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterRecipeSerializerEvent {
    @SubscribeEvent
    public static void registerRecipeTypes(final RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_TYPES, helper -> helper.register(new ResourceLocation(WashingCauldron.MOD_ID, CauldronWashingRecipe.Type.ID), CauldronWashingRecipe.Type.INSTANCE));
    }
}
