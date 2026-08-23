package net.ilexiconn.jurassicraft.common.container;

import net.ilexiconn.jurassicraft.common.api.IDNASample;
import net.ilexiconn.jurassicraft.common.container.slot.SlotDNASample;
import net.ilexiconn.jurassicraft.common.tileentity.TileDNACombinator;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerDNACombinator extends AbstractContainerMenu {
    private final TileDNACombinator dnaCombinator;

    public TileDNACombinator getTileEntity() {
        return this.dnaCombinator;
    }
    private final ContainerData data;

    public ContainerDNACombinator(int id, Inventory playerInventory, TileDNACombinator tileEntity) {
        super(net.ilexiconn.jurassicraft.common.handler.GuiHandler.DNA_COMBINATOR.get(), id); // TODO: pass the real MenuType once GuiHandler registers it
        this.dnaCombinator = tileEntity;
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                if (index == 0) {
                    return ContainerDNACombinator.this.dnaCombinator.getCombinationTime();
                }
                return 0;
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) {
                    ContainerDNACombinator.this.dnaCombinator.setCombinationTime((short) value);
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };

        this.addSlot(new SlotDNASample(dnaCombinator, 0, 55, 20));
        this.addSlot(new SlotDNASample(dnaCombinator, 1, 105, 20));
        // Old SlotFurnaceOutput (1.12.2) does not exist anymore; use a plain Slot.
        this.addSlot(new Slot(dnaCombinator, 2, 81, 67));

        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 9; k++) {
                this.addSlot(new Slot(playerInventory, k + i * 9 + 9, 8 + k * 18, 106 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 164));
        }

        this.addDataSlots(this.data);
    }

    // Legacy constructor kept so old callers (e.g. client GUI classes) still compile.
    public ContainerDNACombinator(Inventory playerInventory, TileDNACombinator tileEntity) {
        this(0, playerInventory, tileEntity);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (!player.level().isClientSide) {
            dnaCombinator.closeInventory();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return dnaCombinator.isUseableByPlayer(player);
    }

    @Override
    public ItemStack quickMoveStack(Player entityPlayer, int slotId) {
        if (!entityPlayer.level().isClientSide) {
            Slot slot = (Slot) this.slots.get(slotId);
            ItemStack stackFinal = ItemStack.EMPTY;

            if (slot != null && slot.hasItem()) {
                ItemStack stackInSlot = slot.getItem();
                stackFinal = stackInSlot.copy();

                if (slotId < 3) {
                    if (!this.moveItemStackTo(stackInSlot, 9, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }

                    slot.onQuickCraft(stackInSlot, stackFinal);
                } else if (slotId >= 3) {
                    if (stackInSlot.getItem() instanceof IDNASample) {
                        if (!this.moveItemStackTo(stackInSlot, 0, 2, false)) {
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
