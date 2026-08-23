package net.ilexiconn.jurassicraft.common.container;

import net.ilexiconn.jurassicraft.common.container.slot.SlotFence;
import net.ilexiconn.jurassicraft.common.tileentity.fence.TileSecurityFenceLowCorner;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ContainerSecurityFenceLow extends AbstractContainerMenu {
    private final TileSecurityFenceLowCorner fence;

    public TileSecurityFenceLowCorner getTileEntity() {
        return this.fence;
    }

    public ContainerSecurityFenceLow(int id, Inventory playerInventory, TileSecurityFenceLowCorner tileEntity) {
        super(net.ilexiconn.jurassicraft.common.handler.GuiHandler.SECURITY_FENCE_LOW.get(), id); // TODO: pass the real MenuType once GuiHandler registers it
        this.fence = tileEntity;

        this.addSlot(new SlotFence(this.fence, 0, 128, 43));
        this.addSlot(new SlotFence(this.fence, 1, 146, 43));
        this.addSlot(new SlotFence(this.fence, 2, 164, 43));
        this.addSlot(new SlotFence(this.fence, 3, 182, 43));
        this.addSlot(new SlotFence(this.fence, 4, 200, 43));
        this.addSlot(new SlotFence(this.fence, 5, 218, 43));

        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 9; k++) {
                this.addSlot(new Slot(playerInventory, k + i * 9 + 9, 48 + k * 18, 174 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 48 + i * 18, 232));
        }
    }

    // Legacy constructor kept so old callers (e.g. client GUI classes) still compile.
    public ContainerSecurityFenceLow(Inventory playerInventory, TileSecurityFenceLowCorner tileEntity) {
        this(0, playerInventory, tileEntity);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (!player.level().isClientSide) {
            this.fence.closeInventory();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.fence.isUseableByPlayer(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        if (!player.level().isClientSide) {
            Slot slot = (Slot) this.slots.get(slotIndex);
            ItemStack stackFinal = ItemStack.EMPTY;

            if (slot != null && slot.hasItem()) {
                ItemStack stackInSlot = slot.getItem();
                stackFinal = stackInSlot.copy();

                if (slotIndex < 6) {
                    if (!this.moveItemStackTo(stackInSlot, 9, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                    slot.onQuickCraft(stackInSlot, stackFinal);
                } else if (slotIndex >= 6) {
                    if (stackInSlot.getItem() == Items.IRON_INGOT || stackInSlot.getItem() == Items.REDSTONE) {
                        if (!this.moveItemStackTo(stackInSlot, 0, 6, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }

                if (stackInSlot.getCount() == 0) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }

                return stackFinal;
            }

            return ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    }
}
