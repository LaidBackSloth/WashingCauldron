package com.laidbacksloth.washingcauldron.mixin;

import com.laidbacksloth.washingcauldron.config.ModCommonConfigs;
import com.laidbacksloth.washingcauldron.recipe.CauldronWashingRecipe;
import com.laidbacksloth.washingcauldron.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
    @Inject(method = "ejectItems", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onEjectItems(Level pLevel, BlockPos pPos, BlockState pState, HopperBlockEntity pSourceContainer, CallbackInfoReturnable<Boolean> info) {
        if (net.minecraftforge.items.VanillaInventoryCodeHooks.insertHook(pSourceContainer)) {
            info.setReturnValue(true);
        }
        BlockPos cPos = pPos.relative(pState.getValue(HopperBlock.FACING));
        if (pLevel.getBlockState(cPos).getBlock() == Blocks.WATER_CAULDRON && pLevel.getBlockState(cPos.below()).getBlock() == Blocks.HOPPER) {
            ItemStack output = null;
            int matchingSlot = -1;
            Optional<CauldronWashingRecipe> match = pLevel.getRecipeManager().getRecipeFor(CauldronWashingRecipe.Type.INSTANCE, pSourceContainer, pLevel);

            if (match.isPresent()) {
                output = match.get().getResultItem();
                matchingSlot = match.get().getMatchingSlot(pSourceContainer);

            } else if (ModCommonConfigs.WASH_ALL_ITEMS.get()) {
                for (int i = 0; i < pSourceContainer.getContainerSize(); ++i) {
                    if (!pSourceContainer.getItem(i).isEmpty()) {
                        output = pSourceContainer.getItem(i).getItem().getDefaultInstance();
                        matchingSlot = i;
                        break;
                    }
                }
            }

            if (output == null) {
                return;
            }

            if (pSourceContainer.getItem(matchingSlot).hasTag()) {
                CompoundTag nbt = pSourceContainer.getItem(matchingSlot).getTag();
                output.setTag(nbt);
            }

            Container container = HopperBlockEntity.getContainerAt(pLevel, cPos.below());
            for (int i = 0; i < container.getContainerSize(); ++i) {
                ItemStack itemstack = container.getItem(i);

                if (itemstack.isEmpty() || (itemstack.getItem() == output.getItem() && itemstack.getCount() < itemstack.getMaxStackSize())) {
                    pSourceContainer.removeItem(matchingSlot, 1);
                    Util.drainCauldron(pLevel, cPos);
                    if (itemstack.getItem() == output.getItem() && output.getCount() < itemstack.getMaxStackSize()) {
                        container.getItem(i).setCount(container.getItem(i).getCount() + 1);
                    } else {
                        container.setItem(i, output);
                    }
                    container.setChanged();
                    pSourceContainer.setChanged();
                    info.setReturnValue(true);
                    return;
                }
            }
        }
    }
}