package com.laidbacksloth.washingcauldron.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

public class Util {
    public static void drainCauldron(Level level, BlockPos pos) {
        boolean north = level.getFluidState(pos.north()).is(Fluids.WATER);
        boolean east = level.getFluidState(pos.east()).is(Fluids.WATER);
        boolean south = level.getFluidState(pos.south()).is(Fluids.WATER);
        boolean west = level.getFluidState(pos.west()).is(Fluids.WATER);

        if (north ? east || south || west : east ? south || west : south && west) {
            return;
        }

        if (level.getBlockState(pos).getValue(LayeredCauldronBlock.LEVEL) > 1) {
            level.setBlock(pos, level.getBlockState(pos).setValue(LayeredCauldronBlock.LEVEL, level.getBlockState(pos).getValue(LayeredCauldronBlock.LEVEL) - 1), 3);
        } else {
            level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
        }
    }

    public static ItemStack getItemStack(String registryName) {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName)));
    }
}
