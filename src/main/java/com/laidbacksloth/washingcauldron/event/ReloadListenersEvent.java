package com.laidbacksloth.washingcauldron.event;

import com.laidbacksloth.washingcauldron.WashingCauldron;
import com.laidbacksloth.washingcauldron.config.ModCommonConfigs;
import com.laidbacksloth.washingcauldron.recipe.CauldronWashingRecipe;
import com.laidbacksloth.washingcauldron.util.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = WashingCauldron.MOD_ID)
public class ReloadListenersEvent {
    private static RecipeManager recipeManager;

    @SubscribeEvent
    public static void reloadListeners(AddReloadListenerEvent event) {
        event.addListener(new SimplePreparableReloadListener<Void>() {
            @Override
            protected @NotNull Void prepare(@NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
                return null;
            }

            @Override
            protected void apply(@NotNull Void pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
                recipeManager = event.getServerResources().getRecipeManager();
                String[] colors = new String[]{"orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};
                List<Recipe<?>> allNewRecipes = new ArrayList<>();

                for (ResourceLocation registryItem : ForgeRegistries.ITEMS.getKeys()) {
                    String registryName = registryItem.toString();
                    if (registryName.contains("light_gray_") || registryName.contains("_light_gray")) {
                        if (ModCommonConfigs.BLACKLIST.get().contains(registryName) || ModCommonConfigs.MOD_BLACKLIST.get().contains(registryName.substring(0, registryName.indexOf(":")))) {
                            continue;
                        }

                        String newRegistryName = null;
                        int occurrence = -1;
                        for (int i = 0; i < StringUtils.countMatches(registryName, "light_gray"); ++i) {
                            occurrence = registryName.indexOf("light_gray", registryName.indexOf("light_gray") + occurrence + 1);
                            String nameToReplace = (occurrence > 0 ? registryName.substring(0, occurrence) : "") + "§" + (occurrence + "light_gray".length() < registryName.length() ? registryName.substring(occurrence + "light_gray".length()) : "");
                            boolean inRegistry = true;
                            for (String color : colors) {
                                if (!ForgeRegistries.ITEMS.getKeys().contains(new ResourceLocation(nameToReplace.replace("§", color)))) {
                                    inRegistry = false;
                                }
                            }
                            if (inRegistry) {
                                newRegistryName = nameToReplace;
                                break;
                            }
                        }
                        if (newRegistryName == null) {
                            continue;
                        }

                        String correctName = null;
                        for (String string : ModCommonConfigs.UNDYED_ALTERNATIVES.get()) {
                            if (string.substring(0, string.indexOf("=")).equals(registryName) && ForgeRegistries.ITEMS.getKeys().contains(new ResourceLocation(string.substring(string.indexOf("=") + 1)))) {
                                correctName = string.substring(string.indexOf("=") + 1);
                            }
                        }

                        if (correctName == null) {
                            correctName = newRegistryName;
                            for (String ignore : ModCommonConfigs.IGNORE_REGISTRY.get()) {
                                correctName = correctName.replace("§_" + ignore + "_", "");
                                correctName = correctName.replace("_§_" + ignore, "");
                                correctName = correctName.replace(ignore + "_§_", "");
                                correctName = correctName.replace("_" + ignore + "_§", "");
                            }
                            correctName = correctName.replace("§_", "");
                            correctName = correctName.replace("_§", "");
                        }

                        List<ItemStack> inputs = new ArrayList<>();
                        ItemStack output = null;

                        if (ForgeRegistries.ITEMS.getKeys().contains(new ResourceLocation(correctName))) {
                            output = Util.getItemStack(correctName);
                        }

                        if (ForgeRegistries.ITEMS.getKeys().contains(new ResourceLocation(newRegistryName.replace("§", "white")))) {
                            if (output == null) {
                                output = Util.getItemStack(newRegistryName.replace("§", "white"));
                            } else {
                                inputs.add(Util.getItemStack(newRegistryName.replace("§", "white")));
                            }
                            for (String color : colors) {
                                inputs.add(Util.getItemStack(newRegistryName.replace("§", color)));
                            }
                        }

                        if (output != null && !inputs.isEmpty()) {
                            allNewRecipes.add(new CauldronWashingRecipe(new ResourceLocation(WashingCauldron.MOD_ID, "undying-" + newRegistryName.replace(":", "-").replace("§", "dyed")), output, Ingredient.of(inputs.toArray(new ItemStack[0]))));
                        }

                        for (String string : ModCommonConfigs.DYING_RECIPES.get()) {
                            if (string.substring(0, string.indexOf("=")).equals(registryName)) {
                                List<String> realColors = new ArrayList<>(Arrays.stream(colors).toList());
                                if (inputs.get(0).getItem().toString().equals(newRegistryName.replace("§", "white"))) {
                                    realColors.add("white");
                                }
                                for (String color : realColors) {
                                    ItemStack outputItem = Util.getItemStack(newRegistryName.replace("§", color));
                                    int count = Integer.parseInt(string.substring(string.indexOf("=") + 1));
                                    outputItem.setCount(count);
                                    NonNullList<Ingredient> ingredients = NonNullList.create();
                                    ingredients.add(Ingredient.of(Util.getItemStack("minecraft:" + color + "_dye")));
                                    for (int i = 0; i < count; ++i) {
                                        ingredients.add(Ingredient.of(output.getItem()));
                                    }

                                    allNewRecipes.add(new ShapelessRecipe(new ResourceLocation(WashingCauldron.MOD_ID, "dying-" + newRegistryName.replace(":", "-").replace("§", color)), WashingCauldron.MOD_ID + ".dying", outputItem, ingredients));
                                }
                            }
                        }
                    }
                }

                allNewRecipes.addAll(recipeManager.getRecipes());
                recipeManager.replaceRecipes(allNewRecipes);
                allNewRecipes.clear();
            }
        });
    }
}