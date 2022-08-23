package com.laidbacksloth.washingcauldron.event;

import com.laidbacksloth.washingcauldron.WashingCauldron;
import com.laidbacksloth.washingcauldron.recipe.CauldronWashingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WashingCauldron.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterRecipeSerializerEvent {
    @SubscribeEvent
    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, CauldronWashingRecipe.Type.ID, CauldronWashingRecipe.Type.INSTANCE);
    }
}
