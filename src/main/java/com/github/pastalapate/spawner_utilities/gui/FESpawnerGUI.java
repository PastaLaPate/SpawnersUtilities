package com.github.pastalapate.spawner_utilities.gui;

import com.github.pastalapate.spawner_utilities.Main;
import com.github.pastalapate.spawner_utilities.gui.utils.MouseUtils;
import com.github.pastalapate.spawner_utilities.init.ModContainerType;
import com.github.pastalapate.spawner_utilities.tiles_entities.FESpawnerTE;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Objects;

public class FESpawnerGUI extends Container {

    public final FESpawnerTE tileEntity;
    public int x, y, z;
    public final PlayerEntity entity;
    public final World world;
    private IItemHandler internal;


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
        this.internal = new ItemStackHandler(1);

        int si;
        int sj;
        for (si = 0; si < 3; ++si)
            for (sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 0 + 84 + si * 18));
        for (si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 8 + si * 18, 142));

        this.addSlot(new SlotItemHandler(this.tileEntity.itemHandler, 0, 86, 60));
    }

    @Override
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
        private World world;
        private int x, y, z;
        private int xSize, ySize;
        private PlayerEntity entity;
        private FESpawnerTE tileentity;

        public FESpawnerScreen(FESpawnerGUI container, PlayerInventory inventory, ITextComponent text) {
            super(container, inventory, text);
            this.world = container.world;
            this.x = container.x;
            this.y = container.y;
            this.z = container.z;
            this.entity = container.entity;
            this.xSize = 176;
            this.ySize = 166;
            this.tileentity = container.tileEntity;
        }

        private static final ResourceLocation texture = new ResourceLocation(Main.MOD_ID, "textures/screens/fespawner.png");

        @Override
        public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
            this.renderBackground(ms);
            super.render(ms, mouseX, mouseY, partialTicks);
            this.renderTooltip(ms, mouseX, mouseY);
        }

        @Override
        protected void renderBg(MatrixStack ms, float partialTicks, int gx, int gy) {
            RenderSystem.color4f(1, 1, 1, 1);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            this.minecraft.getTextureManager().bind(texture);
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize) / 2;
            blit(ms, k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
            RenderSystem.disableBlend();
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
        protected void renderLabels(MatrixStack ms, int mouseX, int mouseY) {
            int energyStored = tileentity.energyStorage.getEnergyStored();
            int maxEnergyStored = tileentity.energyStorage.getMaxEnergyStored();
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
            int offsetX = (width - imageWidth) / 2;
            int offsetY = (height - imageHeight) / 2;
            if (isMouseAboveArea(mouseX, mouseY, x, y, offsetX, offsetY, progressBarWidth, progressBarHeight)) {
                String tooltip = energyStored + "/" + maxEnergyStored + " FE";
                renderTooltip(ms, new StringTextComponent(tooltip), mouseX - offsetX, mouseY - offsetY);
            }
        }

        private boolean isInRect(int x, int y, int width, int height, int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }

        @Override
        public void onClose() {
            super.onClose();
            Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
        }

        @Override
        public void init(Minecraft minecraft, int width, int height) {
            super.init(minecraft, width, height);
            minecraft.keyboardHandler.setSendRepeatsToGui(true);
        }

        private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
            return MouseUtils.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
        }


    }
}