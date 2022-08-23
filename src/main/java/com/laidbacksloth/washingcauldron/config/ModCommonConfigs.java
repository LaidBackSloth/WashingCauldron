package com.laidbacksloth.washingcauldron.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DYNAMIC_RECIPES;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WASH_ALL_ITEMS;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOD_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> IGNORE_REGISTRY;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> UNDYED_ALTERNATIVES;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> DYING_RECIPES;

    static {
        BUILDER.push("Config for Washing Cauldron, look at the wiki for help");

        DYNAMIC_RECIPES = BUILDER.comment("Should recipes automatically be created? If turned off, you will have to add all recipes manually")
                .define("DynamicRecipes", true);
        WASH_ALL_ITEMS = BUILDER.comment("Should items in a hopper without a washing recipe be washed to get back the item? If turned on, the system is less likely to clog up")
                .define("WashAllItems", true);
        BLACKLIST = BUILDER.comment("Light gray types of items, for which type dynamic recipes won't be generated, for example: \"minecraft:light_gray_dye\" means that you can't wash any dye to get a white one")
                .defineList("Blacklist", Lists.newArrayList("minecraft:light_gray_dye"), o -> o instanceof String && ((String) o).contains(":"));
        MOD_BLACKLIST = BUILDER.comment("Mods for which dynamic recipes won't be generated, for example: \"quark\" means that you can't wash any item from quark (unless you add recipes manually)")
                .defineList("ModBlacklist", Lists.newArrayList(), o -> o instanceof String);
        IGNORE_REGISTRY = BUILDER.comment("Words that should be ignored after a color in an item's name to find the undyed item when generating dynamic recipes, for example \"stained\" means that the undyed version of \"minecraft:gray_stained_glass\" is \"minecraft:glass\" instead of \"minecraft:stained_glass\"")
                .defineList("IgnoreWords", Lists.newArrayList("stained", "dyed", "colored"), o -> o instanceof String && ((String) o).contains(":"));
        UNDYED_ALTERNATIVES = BUILDER.comment("Undyed items, you will get back instead of the generated one when washing dyed ones, for example \"minecraft:light_gray_wool=minecraft:red_wool\" means that the undyed version of dyed wool is \"minecraft:red_wool\" instead of \"minecraft:white_wool\" (usually not needed)")
                .defineList("UndyedAlternatives", Lists.newArrayList(), o -> o instanceof String && ((String) o).contains(":") && ((String) o).contains("="));
        DYING_RECIPES = BUILDER.comment("Recipes to redye the undyed items, for example \"minecraft:light_gray_concrete=8\" means that you can craft 8 \"minecraft:white_concrete\" together with a dye to get back 8 dyed concrete")
                .defineList("DyingRecipes", Lists.newArrayList("minecraft:light_gray_concrete=8", "minecraft:light_gray_concrete_powder=8", "minecraft:light_gray_glazed_terracotta=8"), o -> o instanceof String && ((String) o).contains(":") && ((String) o).contains("="));

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
