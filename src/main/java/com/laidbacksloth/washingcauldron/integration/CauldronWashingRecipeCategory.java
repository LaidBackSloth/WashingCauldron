package com.laidbacksloth.washingcauldron.integration;

import com.laidbacksloth.washingcauldron.WashingCauldron;
import com.laidbacksloth.washingcauldron.recipe.CauldronWashingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class CauldronWashingRecipeCategory implements IRecipeCategory<CauldronWashingRecipe> {
    public static final RecipeType<CauldronWashingRecipe> RECIPE_TYPE = RecipeType.create(WashingCauldron.MOD_ID, "cauldron_washing", CauldronWashingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public CauldronWashingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(120, 20);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.CAULDRON));
    }

    @Override
    public @NotNull RecipeType<CauldronWashingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.literal("Cauldron Washing");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CauldronWashingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 2).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 52, 2).addItemStack(new ItemStack(Items.CAULDRON));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 92, 2).addItemStack(recipe.getResultItem());
    }
}