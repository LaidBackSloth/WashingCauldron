package com.laidbacksloth.washingcauldron.event;

import com.laidbacksloth.washingcauldron.WashingCauldron;
import com.laidbacksloth.washingcauldron.recipe.CauldronWashingRecipe;
import com.laidbacksloth.washingcauldron.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = WashingCauldron.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RightClickCauldronEvent {
    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getWorld().getBlockState(event.getPos()).is(Blocks.WATER_CAULDRON) || event.getPlayer().isShiftKeyDown()) {
            return;
        }

        if (!event.getWorld().isClientSide) {
            ItemStack output;
            HopperBlockEntity hopperBlockEntity = new HopperBlockEntity(event.getPos(), Blocks.HOPPER.defaultBlockState());
            hopperBlockEntity.setItem(1, event.getItemStack());
            Optional<CauldronWashingRecipe> match = event.getWorld().getRecipeManager().getRecipeFor(CauldronWashingRecipe.Type.INSTANCE, hopperBlockEntity, event.getWorld());
            if (match.isPresent()) {
                output = match.get().getResultItem();
            } else {
                return;
            }

            if (event.getItemStack().hasTag()) {
                CompoundTag nbt = event.getItemStack().getTag();
                output.setTag(nbt);
            }

            Util.drainCauldron(event.getWorld(), event.getPos());

            Player player = event.getPlayer();
            if (!player.isCreative()) {
                if (event.getHand() == InteractionHand.MAIN_HAND) {
                    player.getInventory().removeFromSelected(false);
                } else {
                    player.getInventory().removeItem(Inventory.SLOT_OFFHAND, 1);
                }
            }

            player.getInventory().placeItemBackInInventory(output, true);
        }
        event.setCanceled(true);
    }
}