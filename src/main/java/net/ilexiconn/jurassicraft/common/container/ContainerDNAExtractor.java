package net.ilexiconn.jurassicraft.common.container;

import net.ilexiconn.jurassicraft.common.api.IDNASource;
import net.ilexiconn.jurassicraft.common.container.slot.SlotDNASource;
import net.ilexiconn.jurassicraft.common.tileentity.TileDNAExtractor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerDNAExtractor extends AbstractContainerMenu {
    private final TileDNAExtractor dnaExtractor;

    public TileDNAExtractor getTileEntity() {
        return this.dnaExtractor;
    }
    private final ContainerData data;

    public ContainerDNAExtractor(int id, Inventory playerInventory, TileDNAExtractor tileEntity) {
        super(net.ilexiconn.jurassicraft.common.handler.GuiHandler.DNA_EXTRACTOR.get(), id); // TODO: pass the real MenuType once GuiHandler registers it
        this.dnaExtractor = tileEntity;
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                if (index == 0) {
                    return ContainerDNAExtractor.this.dnaExtractor.getExtractionTime();
                }
                return 0;
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) {
                    ContainerDNAExtractor.this.dnaExtractor.setExtractionTime((short) value);
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };

        this.addSlot(new SlotDNASource(dnaExtractor, 0, 29, 29));
        this.addSlot(new SlotDNASource(dnaExtractor, 1, 47, 29));
        this.addSlot(new SlotDNASource(dnaExtractor, 2, 29, 47));
        this.addSlot(new SlotDNASource(dnaExtractor, 3, 47, 47));
        // Old SlotFurnaceOutput (1.12.2) does not exist anymore; use a plain Slot.
        this.addSlot(new Slot(dnaExtractor, 4, 113, 29));
        this.addSlot(new Slot(dnaExtractor, 5, 131, 29));
        this.addSlot(new Slot(dnaExtractor, 6, 113, 47));
        this.addSlot(new Slot(dnaExtractor, 7, 131, 47));

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
    public ContainerDNAExtractor(Inventory playerInventory, TileDNAExtractor tileEntity) {
        this(0, playerInventory, tileEntity);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (!player.level().isClientSide) {
            dnaExtractor.closeInventory();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return dnaExtractor.isUseableByPlayer(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int itemSlot) {
        if (!player.level().isClientSide) {
            Slot slot = (Slot) this.slots.get(itemSlot);
            ItemStack stackFinal = ItemStack.EMPTY;

            if (slot != null && slot.hasItem()) {
                ItemStack stackInSlot = slot.getItem();
                stackFinal = stackInSlot.copy();

                if (itemSlot < 8) {
                    if (!this.moveItemStackTo(stackInSlot, 9, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }

                    slot.onQuickCraft(stackInSlot, stackFinal);
                } else if (itemSlot >= 8) {
                    if (stackInSlot.getItem() instanceof IDNASource) {
                        if (!this.moveItemStackTo(stackInSlot, 0, 4, false)) {
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
