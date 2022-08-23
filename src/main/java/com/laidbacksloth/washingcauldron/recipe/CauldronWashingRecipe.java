package com.laidbacksloth.washingcauldron.recipe;

import com.google.gson.JsonObject;
import com.laidbacksloth.washingcauldron.WashingCauldron;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CauldronWashingRecipe implements Recipe<HopperBlockEntity> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final Ingredient input;

    public CauldronWashingRecipe(ResourceLocation id, ItemStack output, Ingredient input) {
        this.id = id;
        this.output = output;
        this.input = input;
    }

    @Override
    public boolean matches(HopperBlockEntity pContainer, @NotNull Level pLevel) {
        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            if (input.test(pContainer.getItem(i))) {
                return true;
            }
        }
        return false;
    }

    public int getMatchingSlot(HopperBlockEntity pContainer) {
        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            if (input.test(pContainer.getItem(i))) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull HopperBlockEntity pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public Ingredient getInput() {
        return input;
    }

    public static class Type implements RecipeType<CauldronWashingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "cauldron_washing";
    }

    public static class Serializer implements RecipeSerializer<CauldronWashingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(WashingCauldron.MOD_ID,"cauldron_washing");

        @Override
        public @NotNull CauldronWashingRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            return new CauldronWashingRecipe(id, output, input);
        }

        @Override
        public CauldronWashingRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);
            ItemStack output = buf.readItem();
            return new CauldronWashingRecipe(id, output, input);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, CauldronWashingRecipe recipe) {
            recipe.getInput().toNetwork(buf);
            buf.writeItemStack(recipe.getResultItem(), false);
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass();
        }

        @SuppressWarnings("unchecked")
        private static <G> Class<G> castClass() {
            return (Class<G>) RecipeSerializer.class;
        }
    }
}
