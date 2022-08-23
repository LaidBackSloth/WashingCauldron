package com.laidbacksloth.washingcauldron.integration;

import com.laidbacksloth.washingcauldron.WashingCauldron;
import com.laidbacksloth.washingcauldron.recipe.CauldronWashingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIWashingCauldronPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(WashingCauldron.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipeCatalysts (IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Items.CAULDRON), CauldronWashingRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CauldronWashingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<CauldronWashingRecipe> recipes = rm.getAllRecipesFor(CauldronWashingRecipe.Type.INSTANCE);
        registration.addRecipes(CauldronWashingRecipeCategory.RECIPE_TYPE, recipes);
    }
}
