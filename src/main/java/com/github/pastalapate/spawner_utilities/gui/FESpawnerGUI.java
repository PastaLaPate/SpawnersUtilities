package com.github.pastalapate.spawner_utilities.gui;

import com.github.pastalapate.spawner_utilities.SpawnerUtilities;
import com.github.pastalapate.spawner_utilities.gui.utils.MouseUtils;
import com.github.pastalapate.spawner_utilities.init.ModContainerType;
import com.github.pastalapate.spawner_utilities.tiles_entities.FESpawnerTE;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.logging.log4j.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FESpawnerGUI extends Container {

    public final FESpawnerTE tileEntity;
    public int x, y, z;
    public final PlayerEntity entity;
    public final World world;


    public FESpawnerGUI(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data), data);
    }

    public FESpawnerGUI(final int windowId, final PlayerInventory inv, final FESpawnerTE tileEntity, final PacketBuffer extraData) {
        super(ModContainerType.FESpawnerGUI.get(), windowId);
        this.tileEntity = tileEntity;
        this.entity = inv.player;
        this.x = (int) entity.getX();
        this.y = (int) entity.getY();
        this.z = (int) entity.getZ();
        this.world = entity.level;

        int si;
        int sj;
        for (si = 0; si < 3; ++si)
            for (sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 8 + si * 18, 142));

        // Soul
        this.addSlot(new SlotItemHandler(this.tileEntity.itemHandler, 0, 126, 40));

        // Additional slots
        int sX = 50;
        int sY = 25;
        int upgradeSlotNumber = tileEntity.upgradeLimit;
        for (int i = 0; i < upgradeSlotNumber; i++) {
            this.addSlot(new SlotItemHandler(this.tileEntity.itemHandler, i + 1, sX, sY));
            sX += 18;
            if ((i + 1) % 3 == 0) {
                sX = 40;
                sY += 18;
            }
        }
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    @Override
    @MethodsReturnNonnullByDefault
    @ParametersAreNonnullByDefault
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        final int TE_INVENTORY_SLOT_COUNT = 1 + tileEntity.upgradeLimit;
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return true;
    }

    private static FESpawnerTE getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null!");
        Objects.requireNonNull(data, "data cannot be null!");
        final TileEntity tileAtPos = playerInventory.player.getCommandSenderWorld().getBlockEntity(data.readBlockPos());
        if (tileAtPos instanceof FESpawnerTE)
            return (FESpawnerTE) tileAtPos;
        throw new IllegalStateException("Tile entity is not correct! " + tileAtPos);
    }

    @OnlyIn(Dist.CLIENT)
    public static class FESpawnerScreen extends ContainerScreen<FESpawnerGUI> {
        private final int xSize, ySize;
        private int offsetX;
        private int offsetY;
        private final FESpawnerTE tileEntity;
        private final List<Slot> slots;

        public FESpawnerScreen(FESpawnerGUI container, PlayerInventory inventory) {
            super(container, inventory, new StringTextComponent("FE Spawner GUI"));
            this.xSize = 176;
            this.ySize = 166;
            this.tileEntity = container.tileEntity;
            this.slots = container.slots;
        }

        private static final ResourceLocation texture = new ResourceLocation(SpawnerUtilities.MOD_ID, "textures/screens/fespawner.png");

        @Override
        @ParametersAreNonnullByDefault
        public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
            offsetX = (width - imageWidth) / 2;
            offsetY = (height - imageHeight) / 2;
            this.renderBackground(ms);
            super.render(ms, mouseX, mouseY, partialTicks);
            this.renderTooltip(ms, mouseX, mouseY);
        }

        @Override
        @ParametersAreNonnullByDefault
        protected void renderBg(MatrixStack ms, float partialTicks, int gx, int gy) {
            RenderSystem.color4f(1, 1, 1, 1);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            assert this.minecraft != null;
            this.minecraft.getTextureManager().bind(texture);
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize) / 2;
            blit(ms, k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
            drawSlots(ms, slots);
            RenderSystem.disableBlend();
        }

        public void drawSlots(MatrixStack ms, List<Slot> slots) {
            for (Slot slot : slots) {
                if (slot instanceof SlotItemHandler) {
                    int slotX = slot.x + offsetX;
                    int slotY = slot.y + offsetY;
                    int slotSize = 16;
                    fill(ms, slotX, slotY, slotX + slotSize, slotY + slotSize, 0xFF9F9F9F);
                }
            }
        }

        /* FILL
        p_238467_0_ : Il s'agit d'un objet MatrixStack utilisé pour effectuer des transformations matricielles lors du rendu.
        p_238467_1_ : C'est la coordonnée x du coin supérieur gauche du rectangle à remplir.
        p_238467_2_ : C'est la coordonnée y du coin supérieur gauche du rectangle à remplir.
        p_238467_3_ : C'est la coordonnée x du coin inférieur droit du rectangle à remplir.
        p_238467_4_ : C'est la coordonnée y du coin inférieur droit du rectangle à remplir.
        p_238467_5_ : C'est la couleur avec laquelle remplir le rectangle. La couleur est représentée sous forme entière (ARGB) où les 8 bits les plus significatifs représentent l'opacité, suivi de 8 bits pour la composante rouge, verte et bleue.
        */

        @Override
        public boolean keyPressed(int key, int b, int c) {
            if (key == 256) {
                assert Objects.requireNonNull(this.minecraft).player != null;
                assert this.minecraft.player != null;
                this.minecraft.player.closeContainer();
                return true;
            }
            return super.keyPressed(key, b, c);
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        @ParametersAreNonnullByDefault
        protected void renderLabels(MatrixStack ms, int mouseX, int mouseY) {
            this.font.draw(ms, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
            int energyStored = tileEntity.energyStorage.getEnergyStored();
            int maxEnergyStored = tileEntity.energyStorage.getMaxEnergyStored();
            int progressBarWidth = 10; // Width of the progress bar
            int progressBarHeight = 60; // Height of the progress bar

            // Calculate the progress bar fill percentage
            int fillPercentage = energyStored * progressBarHeight / maxEnergyStored;

            // Render the progress bar background
            int x = 30;
            int y = 21;
            fill(ms, x, y, x + progressBarWidth, y + progressBarHeight, 0xFF808080);

            // Render the filled portion of the progress bar from bottom to top
            int filledY = y + progressBarHeight - fillPercentage;
            fillGradient(ms, x, filledY, x + progressBarWidth, y + progressBarHeight, 0xFFFF0000, 0xFF7F0505);
            // Check if the mouse is hovering over the progress bar
            if (isMouseAboveArea(mouseX, mouseY, x, y, offsetX, offsetY, progressBarWidth, progressBarHeight)) {
                String tooltip = energyStored + "/" + maxEnergyStored + " FE";
                renderTooltip(ms, new StringTextComponent(tooltip), mouseX - offsetX, mouseY - offsetY);
            }
        }

        @Override
        public void onClose() {
            super.onClose();
            Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void init(Minecraft minecraft, int width, int height) {
            super.init(minecraft, width, height);
            minecraft.keyboardHandler.setSendRepeatsToGui(true);
        }

        private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
            return MouseUtils.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
        }

    }
}