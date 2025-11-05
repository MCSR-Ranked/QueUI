package com.mcsrranked.queui.gui.widget;

import com.mcsrranked.queui.QueUI;
import com.mcsrranked.queui.gui.QueUIConstants;
import com.mcsrranked.queui.type.AlignmentDirection;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class QueUIButtonWidget extends AbstractPressableButtonWidget implements AnimateWidget {

    private static final int BG_COLOR = BackgroundHelper.ColorMixer.getArgb(150, 0, 0, 0);
    private static final int BG_INACTIVE_COLOR = BackgroundHelper.ColorMixer.getArgb(80, 0, 0, 0);
    private static final int SELECTION_COLOR = BackgroundHelper.ColorMixer.getArgb(220, 255, 255, 0);

    private Supplier<Text> textUpdater;
    private RenderSupplier iconRenderer;
    private AlignmentDirection iconAlignment = AlignmentDirection.LEFT;
    private AlignmentDirection textAlignment = AlignmentDirection.CENTER;
    private int[] iconDimension = new int[] { 8, 8 };
    private int iconPadding = 2;
    private AlignmentDirection contentAlignment = AlignmentDirection.CENTER;
    private int contentMargin = 0;
    private int messageColor = QueUIConstants.WHITE_COLOR;
    private Consumer<QueUIButtonWidget> pressAction;
    private RenderSupplier mouseEnter;
    private RenderSupplier mouseExit;
    private RenderSupplier mouseHover;
    private boolean wasHovered = false;
    private boolean selected = false;

    public QueUIButtonWidget(int x, int y, int width, int height) {
        super(x, y, width, height, LiteralText.EMPTY);
    }

    public QueUIButtonWidget setText(Supplier<Text> textSupplier) {
        this.textUpdater = textSupplier;
        this.updateMessage();
        return this;
    }

    public QueUIButtonWidget setText(Text text) {
        return this.setText(() -> text);
    }

    public QueUIButtonWidget setTextAsString(Supplier<String> text) {
        return this.setText(() -> new LiteralText(text.get()));
    }

    public QueUIButtonWidget setText(String text) {
        return this.setText(new LiteralText(text));
    }

    public QueUIButtonWidget setIconRenderer(RenderSupplier renderer) {
        this.iconRenderer = renderer;
        return this;
    }

    public QueUIButtonWidget setIconTextAlignment(AlignmentDirection iconAlignment, AlignmentDirection textAlignment) {
        assert iconAlignment != textAlignment;
        this.iconAlignment = iconAlignment;
        this.textAlignment = textAlignment;
        return this;
    }

    public QueUIButtonWidget setIconDimension(int width, int height) {
        assert width >= 0 && height >= 0;
        this.iconDimension = new int[] { width, height };
        return this;
    }

    public QueUIButtonWidget setIconPadding(int padding) {
        assert padding >= 0;
        this.iconPadding = padding;
        return this;
    }

    public QueUIButtonWidget setContentAlignment(AlignmentDirection contentAlignment, int margin) {
        assert margin >= 0;
        this.contentAlignment = contentAlignment;
        this.contentMargin = margin;
        return this;
    }

    public QueUIButtonWidget setOnPress(Consumer<QueUIButtonWidget> pressAction) {
        this.pressAction = pressAction;
        return this;
    }

    public QueUIButtonWidget setOnMouseEnter(RenderSupplier mouseEnter) {
        this.mouseEnter = mouseEnter;
        return this;
    }

    public QueUIButtonWidget setOnMouseHover(RenderSupplier mouseHover) {
        this.mouseHover = mouseHover;
        return this;
    }

    public QueUIButtonWidget setOnMouseExit(RenderSupplier mouseExit) {
        this.mouseExit = mouseExit;
        return this;
    }

    @Override
    public void onPress() {
        if (this.pressAction != null) this.pressAction.accept(this);
        this.updateMessage();
    }

    public void updateMessage() {
        if (this.textUpdater != null) this.setMessage(this.textUpdater.get());
    }

    public void setMessageColor(int color) {
        this.messageColor = color;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        this.renderBg(matrices, minecraftClient, mouseX, mouseY);
        this.renderContents(matrices, minecraftClient, mouseX, mouseY);

        boolean isHovered = this.isHovered();
        if (!this.wasHovered && isHovered && this.mouseEnter != null) this.mouseEnter.run(this, matrices, mouseX, mouseY);
        if (isHovered && this.mouseHover != null) this.mouseHover.run(this, matrices, mouseX, mouseY);
        if (this.wasHovered && !isHovered && this.mouseExit != null) this.mouseExit.run(this, matrices, mouseX, mouseY);
        this.wasHovered = isHovered;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        client.getTextureManager().bindTexture(WIDGETS_LOCATION);
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, 3, 3);
        drawTexture(matrices, this.x + 3, this.y, this.getWidth() - 6, 3, 3, 46 + i * 20, 1, 3, 256, 256);
        this.drawTexture(matrices, this.x + this.getWidth() - 3, this.y, 197, 46 + i * 20, 3, 3);
        drawTexture(matrices, this.x, this.y + 3, 3, this.getHeight() - 6, 0, 49 + i * 20, 3, 1, 256, 256);
        this.drawTexture(matrices, this.x, this.y + this.getHeight() - 3, 0, 46 + 17 + i * 20, 3, 3);
        drawTexture(matrices, this.x + 3, this.y + this.getHeight() - 3, this.getWidth() - 6, 3, 3, 46 + 17 + i * 20, 1, 3, 256, 256);
        this.drawTexture(matrices, this.x + this.getWidth() - 3, this.y + this.getHeight() - 3, 197, 46 + 17 + i * 20, 3, 3);
        drawTexture(matrices, this.x + this.getWidth() - 3, this.y + 3, 3, this.getHeight() - 6, 197, 49 + i * 20, 3, 1, 256, 256);
        fill(matrices, this.x + 3, this.y + 3, this.x + this.getWidth() - 3, this.y + this.getHeight() - 3, this.active ? BG_COLOR : BG_INACTIVE_COLOR);
        this.renderSelectionMark(matrices, client, mouseX, mouseY);
        RenderSystem.popMatrix();
    }

    protected void renderSelectionMark(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        if (this.isSelected()) {
            int div = this.getWidth() / 5;
            fill(matrices, this.x + div, this.y + this.getHeight() - 3, this.x + this.getWidth() - div, this.y + this.getHeight() - 1, SELECTION_COLOR);
        }
    }

    @SuppressWarnings("deprecation")
    public void renderContents(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        TextRenderer textRenderer = client.textRenderer;
        int textColor = this.active ? this.messageColor : 0xA0A0A0;

        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        Text text = this.getMessage();
        int textWidth = textRenderer.getWidth(text);
        Runnable renderText = () -> this.drawTextWithShadow(matrices, textRenderer, text, 0, 0, textColor | MathHelper.ceil(this.alpha * 255.0f) << 24);

        if (this.iconRenderer != null && this.textUpdater != null) {
            if (this.iconAlignment.getY() == this.textAlignment.getY()) {
                int totalWidth = textWidth + this.iconPadding + this.iconDimension[0];
                int startX = Math.max(0, this.getWidth() - totalWidth) / 2, endX = this.getWidth() - startX;
                boolean iconFirst = this.iconAlignment.getX() < this.textAlignment.getX();

                int offset = (startX - this.contentMargin) * this.contentAlignment.getX();

                RenderSystem.pushMatrix();
                RenderSystem.translatef(this.x + offset, this.y, 0);

                RenderSystem.pushMatrix();
                int iconY = (this.getHeight() - this.iconDimension[1]) / 2;
                RenderSystem.translatef(startX + (iconFirst ? 0 : (textWidth + this.iconPadding)), iconY, 0);
                if (QueUI.DEBUG_MODE) DrawableHelper.fill(matrices, 0, 0, this.iconDimension[0], this.iconDimension[1], 0xFF00FF00);
                this.iconRenderer.run(this, matrices, mouseX, mouseY);
                RenderSystem.popMatrix();

                RenderSystem.pushMatrix();
                int textY = (this.getHeight() - QueUIConstants.TEXT_HEIGHT) / 2;
                RenderSystem.translatef(startX + (iconFirst ? (this.iconDimension[0] + this.iconPadding) : 0), textY, 0);
                if (QueUI.DEBUG_MODE) DrawableHelper.fill(matrices, 0, 0, textWidth, QueUIConstants.TEXT_HEIGHT, 0xFF0000FF);
                renderText.run();
                RenderSystem.popMatrix();

                RenderSystem.popMatrix();
            } else {
                int totalHeight = QueUIConstants.TEXT_HEIGHT + this.iconPadding + this.iconDimension[1];
                int startY = Math.max(0, this.getHeight() - totalHeight) / 2, endY = this.getHeight() - startY;
                boolean iconFirst = this.iconAlignment.getY() < this.textAlignment.getY();

                int offset = (startY - this.contentMargin) * this.contentAlignment.getY();

                RenderSystem.pushMatrix();
                RenderSystem.translatef(this.x, this.y + offset, 0);

                RenderSystem.pushMatrix();
                int iconX = (this.getWidth() - this.iconDimension[0]) / 2;
                RenderSystem.translatef(iconX, startY + (iconFirst ? 0 : (QueUIConstants.TEXT_HEIGHT + this.iconPadding)), 0);
                if (QueUI.DEBUG_MODE) DrawableHelper.fill(matrices, 0, 0, this.iconDimension[0], this.iconDimension[1], 0xFF00FF00);
                this.iconRenderer.run(this, matrices, mouseX, mouseY);
                RenderSystem.popMatrix();

                RenderSystem.pushMatrix();
                int textX = (this.getWidth() - textWidth) / 2;
                RenderSystem.translatef(textX, startY + (iconFirst ? (this.iconDimension[1] + this.iconPadding) : 0), 0);
                if (QueUI.DEBUG_MODE) DrawableHelper.fill(matrices, 0, 0, textWidth, QueUIConstants.TEXT_HEIGHT, 0xFF0000FF);
                renderText.run();
                RenderSystem.popMatrix();

                RenderSystem.popMatrix();
            }
        } else if (this.textUpdater != null) {
            RenderSystem.pushMatrix();
            int textX = (this.getWidth() - textWidth) / 2;
            textX += (textX - this.contentMargin) * this.contentAlignment.getX();
            int textY = (this.getHeight() - QueUIConstants.TEXT_HEIGHT) / 2;
            textY += (textY - this.contentMargin) * this.contentAlignment.getY();
            RenderSystem.translatef(this.x + textX, this.y + textY, 0);
            renderText.run();
            RenderSystem.popMatrix();
        } else if (this.iconRenderer != null) {
            RenderSystem.pushMatrix();
            int iconX = (this.getWidth() - this.iconDimension[0]) / 2;
            iconX += (iconX - this.contentMargin) * this.contentAlignment.getX();
            int iconY = (this.getHeight() - this.iconDimension[1]) / 2;
            iconY += (iconY - this.contentMargin) * this.contentAlignment.getY();
            RenderSystem.translatef(this.x + iconX, this.y + iconY, 0);
            this.iconRenderer.run(this, matrices, mouseX, mouseY);
            RenderSystem.popMatrix();
        }

        RenderSystem.popMatrix();
    }

    public interface RenderSupplier {
        void run(QueUIButtonWidget button, MatrixStack matrices, int mouseX, int mouseY);
    }
}
