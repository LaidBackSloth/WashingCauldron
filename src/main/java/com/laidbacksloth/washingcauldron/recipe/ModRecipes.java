package com.laidbacksloth.washingcauldron.recipe;

import com.laidbacksloth.washingcauldron.WashingCauldron;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, WashingCauldron.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CauldronWashingRecipe>> CAULDRON_WASHING_SERIALIZER =
            SERIALIZERS.register("cauldron_washing", () -> CauldronWashingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}