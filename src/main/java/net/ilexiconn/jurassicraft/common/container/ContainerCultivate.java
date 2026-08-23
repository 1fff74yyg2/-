package net.ilexiconn.jurassicraft.common.container;

import net.ilexiconn.jurassicraft.common.container.slot.SlotBucket;
import net.ilexiconn.jurassicraft.common.container.slot.SlotDNASampleAndEgg;
import net.ilexiconn.jurassicraft.common.data.enums.JurassiCraftFoodNutrients;
import net.ilexiconn.jurassicraft.common.tileentity.TileCultivate;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;

public class ContainerCultivate extends AbstractContainerMenu {
    private final TileCultivate cultivator;

    public TileCultivate getTileEntity() {
        return this.cultivator;
    }
    private final ContainerData data;

    public ContainerCultivate(int id, Inventory inventory, TileCultivate tileEntity) {
        super(net.ilexiconn.jurassicraft.common.handler.GuiHandler.CULTIVATE.get(), id); // TODO: pass the real MenuType once GuiHandler registers it
        this.cultivator = tileEntity;
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0:
                        return ContainerCultivate.this.cultivator.getWaterStored();
                    case 1:
                        return ContainerCultivate.this.cultivator.getProximateValue();
                    case 2:
                        return ContainerCultivate.this.cultivator.getMineralValue();
                    case 3:
                        return ContainerCultivate.this.cultivator.getVitaminValue();
                    case 4:
                        return ContainerCultivate.this.cultivator.getLipidValue();
                    default:
                        return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        ContainerCultivate.this.cultivator.setWaterStored((byte) value);
                        break;
                    case 1:
                        ContainerCultivate.this.cultivator.setProximateValue((short) value);
                        break;
                    case 2:
                        ContainerCultivate.this.cultivator.setMineralValue((short) value);
                        break;
                    case 3:
                        ContainerCultivate.this.cultivator.setVitaminValue((short) value);
                        break;
                    case 4:
                        ContainerCultivate.this.cultivator.setLipidValue((short) value);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public int getCount() {
                return 5;
            }
        };

        this.addSlot(new SlotBucket(cultivator, 0, 12, 20));
        this.addSlot(new SlotBucket(cultivator, 1, 12, 68));
        this.addSlot(new SlotDNASampleAndEgg(cultivator, 2, 122, 44));
        this.addSlot(new Slot(cultivator, 3, 208, 20));

        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 9; k++) {
                this.addSlot(new Slot(inventory, k + i * 9 + 9, k * 18 + 8, i * 18 + 106));
            }
        }

        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, i * 18 + 8, 164));
        }

        this.addDataSlots(this.data);
    }

    // Legacy constructor kept so old callers (e.g. client GUI classes) still compile.
    public ContainerCultivate(Inventory inventory, TileCultivate tileEntity) {
        this(0, inventory, tileEntity);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (!player.level().isClientSide) {
            cultivator.closeInventory();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return cultivator.isUseableByPlayer(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        if (!player.level().isClientSide) {
            Slot slot = (Slot) this.slots.get(i);
            ItemStack stackFinal = ItemStack.EMPTY;

            if (slot != null && slot.hasItem()) {
                ItemStack stackInSlot = slot.getItem();

                stackFinal = stackInSlot.copy();

                if (i < 4) {
                    if (!this.moveItemStackTo(stackInSlot, 4, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }

                    slot.onQuickCraft(stackInSlot, stackFinal);
                } else if (i >= 4) {
                    if (stackInSlot.getItem() instanceof BucketItem) {
                        if (!this.moveItemStackTo(stackInSlot, 0, 2, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (JurassiCraftFoodNutrients.isValidFood(stackInSlot.getItem())) {
                        if (!this.moveItemStackTo(stackInSlot, 3, 4, false)) {
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
