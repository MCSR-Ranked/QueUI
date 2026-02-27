package com.mcsrranked.queui.gui.widget;

import com.mcsrranked.queui.QueUI;
import com.mcsrranked.queui.gui.QueUIConstants;
import com.mcsrranked.queui.type.AlignmentDirection;
import com.mcsrranked.queui.utils.ColorUtils;
import com.mcsrranked.queui.utils.ScissorStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.widget.AbstractPressableButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class QueUIButtonWidget<T extends QueUIButtonWidget<T>> extends AbstractPressableButtonWidget implements AnimatableWidget {

    private static final int BG_COLOR = BackgroundHelper.ColorMixer.getArgb(150, 0, 0, 0);
    private static final int BG_INACTIVE_COLOR = BackgroundHelper.ColorMixer.getArgb(80, 0, 0, 0);
    private static final int SELECTION_COLOR = BackgroundHelper.ColorMixer.getArgb(220, 255, 255, 0);

    private Supplier<Text> textUpdater;
    private RenderSupplier<T> iconRenderer;
    private AlignmentDirection iconAlignment = AlignmentDirection.LEFT;
    private AlignmentDirection textAlignment = AlignmentDirection.CENTER;
    private int[] iconDimension = new int[] { 8, 8 };
    private int iconPadding = 2;
    private AlignmentDirection contentAlignment = AlignmentDirection.CENTER;
    private int contentMargin = 4;
    private double scrollSpeed = 1;
    private int messageColor = QueUIConstants.WHITE_COLOR;
    private Consumer<T> pressAction;
    private RenderSupplier<T> mouseEnter;
    private RenderSupplier<T> mouseExit;
    private RenderSupplier<T> mouseHover;
    private boolean wasHovered = false;
    private boolean selected = false;
    private float[] renderOffset = new float[] { 0, 0 };

    public QueUIButtonWidget(int x, int y, int width, int height, boolean updateMessage) {
        super(x, y, width, height, LiteralText.EMPTY);
        if (updateMessage) this.updateMessage();
    }

    public QueUIButtonWidget(int x, int y, int width, int height) {
        this(x, y, width, height, true);
    }

    @SuppressWarnings("unchecked")
    public T setText(Supplier<Text> textSupplier) {
        this.textUpdater = textSupplier;
        this.updateMessage();
        return (T) this;
    }

    public T setText(Text text) {
        return this.setText(() -> text);
    }

    public T setTextAsString(Supplier<String> text) {
        return this.setText(() -> new LiteralText(text.get()));
    }

    public T setText(String text) {
        return this.setText(new LiteralText(text));
    }

    @SuppressWarnings("unchecked")
    public T setIconRenderer(RenderSupplier<T> renderer) {
        this.iconRenderer = renderer;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setIconTextAlignment(AlignmentDirection iconAlignment, AlignmentDirection textAlignment) {
        assert iconAlignment != textAlignment;
        this.iconAlignment = iconAlignment;
        this.textAlignment = textAlignment;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setIconDimension(int width, int height) {
        assert width >= 0 && height >= 0;
        this.iconDimension = new int[] { width, height };
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setIconPadding(int padding) {
        assert padding >= 0;
        this.iconPadding = padding;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setContentAlignment(AlignmentDirection contentAlignment, int margin) {
        assert margin >= 0;
        this.contentAlignment = contentAlignment;
        this.contentMargin = margin;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setScrollSpeed(double scrollSpeed) {
        assert scrollSpeed > 0;
        this.scrollSpeed = scrollSpeed;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setOnPress(Consumer<T> pressAction) {
        this.pressAction = pressAction;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setOnMouseEnter(RenderSupplier<T> mouseEnter) {
        this.mouseEnter = mouseEnter;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setOnMouseHover(RenderSupplier<T> mouseHover) {
        this.mouseHover = mouseHover;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setOnMouseExit(RenderSupplier<T> mouseExit) {
        this.mouseExit = mouseExit;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onPress() {
        if (this.pressAction != null) this.pressAction.accept((T) this);
        this.updateMessage();
    }

    public Supplier<Text> getTextUpdater() {
        return textUpdater;
    }

    public RenderSupplier<T> getIconRenderer() {
        return iconRenderer;
    }

    public void updateMessage() {
        if (this.getTextUpdater() != null) this.setMessage(this.getTextUpdater().get());
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
        matrices.push();
        matrices.translate(this.renderOffset[0], this.renderOffset[1], 0);
        this.renderBg(matrices, minecraftClient, mouseX, mouseY);
        this.renderContents(matrices, minecraftClient, mouseX, mouseY);
        matrices.pop();

        boolean isHovered = this.isHovered();
        if (!this.wasHovered && isHovered && this.mouseEnter != null) this.mouseEnter.run(this, matrices, mouseX, mouseY, getMatrixVector(matrices).getLeft(), getMatrixVector(matrices).getRight());
        if (isHovered && this.mouseHover != null) this.mouseHover.run(this, matrices, mouseX, mouseY, getMatrixVector(matrices).getLeft(), getMatrixVector(matrices).getRight());
        if (this.wasHovered && !isHovered && this.mouseExit != null) this.mouseExit.run(this, matrices, mouseX, mouseY, getMatrixVector(matrices).getLeft(), getMatrixVector(matrices).getRight());
        this.wasHovered = isHovered;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        client.getTextureManager().bindTexture(WIDGETS_LOCATION);
        matrices.push();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, 3, 3);
        drawTexture(matrices, this.x + 3, this.y, this.getWidth() - 6, 3, 3, 46 + i * 20, 1, 3, 256, 256);
        drawTexture(matrices, this.x + this.getWidth() - 3, this.y, 197, 46 + i * 20, 3, 3);
        drawTexture(matrices, this.x, this.y + 3, 3, this.getHeight() - 6, 0, 49 + i * 20, 3, 1, 256, 256);
        drawTexture(matrices, this.x, this.y + this.getHeight() - 3, 0, 46 + 17 + i * 20, 3, 3);
        drawTexture(matrices, this.x + 3, this.y + this.getHeight() - 3, this.getWidth() - 6, 3, 3, 46 + 17 + i * 20, 1, 3, 256, 256);
        drawTexture(matrices, this.x + this.getWidth() - 3, this.y + this.getHeight() - 3, 197, 46 + 17 + i * 20, 3, 3);
        drawTexture(matrices, this.x + this.getWidth() - 3, this.y + 3, 3, this.getHeight() - 6, 197, 49 + i * 20, 3, 1, 256, 256);
        fill(matrices, this.x + 3, this.y + 3, this.x + this.getWidth() - 3, this.y + this.getHeight() - 3, ColorUtils.mixAlpha(this.active ? BG_COLOR : BG_INACTIVE_COLOR, this.alpha));
        this.renderSelectionMark(matrices, client, mouseX, mouseY);
        matrices.pop();
    }

    protected void renderSelectionMark(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        if (this.isSelected()) {
            int div = this.getWidth() / 4;
            fill(matrices, this.x + div, this.y + this.getHeight() - 3, this.x + this.getWidth() - div, this.y + this.getHeight() - 1, SELECTION_COLOR);
        }
    }

    @SuppressWarnings("deprecation")
    public void renderContents(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        TextRenderer textRenderer = client.textRenderer;
        int textColor = this.active ? this.messageColor : 0xA0A0A0;

        matrices.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        Text text = this.getMessage();
        int rawTextWidth = textRenderer.getWidth(text);
        Runnable renderText = () -> {
            int newColor = ColorUtils.mixAlpha(textColor, this.alpha);
            if (BackgroundHelper.ColorMixer.getAlpha(newColor) > 3) {
                this.drawTextWithShadow(matrices, textRenderer, text, 0, 0, newColor);
            }
        };

        if (this.getIconRenderer() != null && this.getTextUpdater() != null) {
            if (this.iconAlignment.getY() == this.textAlignment.getY()) {
                int totalWidth = MathHelper.clamp(rawTextWidth + this.iconPadding + this.iconDimension[0], 1, this.getWidth() - (this.contentMargin * 2));
                int allowTextWidth = totalWidth - (this.iconPadding + this.iconDimension[0]);
                int startX = Math.max(0, this.getWidth() - totalWidth) / 2, endX = this.getWidth() - startX;
                boolean iconFirst = this.iconAlignment.getX() < this.textAlignment.getX();

                int offset = (startX - this.contentMargin) * this.contentAlignment.getX();

                matrices.push();
                matrices.translate(this.x + offset, this.y, 0);

                matrices.push();
                int iconY = (this.getHeight() - this.iconDimension[1]) / 2;
                matrices.translate(startX + (iconFirst ? 0 : (allowTextWidth + this.iconPadding)), iconY, 0);
                if (QueUI.DEBUG_MODE) DrawableHelper.fill(matrices, 0, 0, this.iconDimension[0], this.iconDimension[1], 0xFF00FF00);
                matrices.push();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableDepthTest();
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, this.alpha);
                RenderSystem.enableBlend();
                this.getIconRenderer().run(this, matrices, mouseX, mouseY, getMatrixVector(matrices).getLeft(), getMatrixVector(matrices).getRight());
                matrices.pop();
                matrices.pop();

                matrices.push();
                int textY = (this.getHeight() - QueUIConstants.TEXT_HEIGHT) / 2;
                matrices.translate(startX + (iconFirst ? (this.iconDimension[0] + this.iconPadding) : 0), textY, 0);
                ScissorStack.push(matrices, 0, 0, allowTextWidth, QueUIConstants.TEXT_HEIGHT);
                if (QueUI.DEBUG_MODE) DrawableHelper.fill(matrices, 0, 0, rawTextWidth, QueUIConstants.TEXT_HEIGHT, 0xFF0000FF);
                matrices.push();
                double progress = getLoopingSmooth((long) (8000L * this.scrollSpeed));
                matrices.translate((rawTextWidth - allowTextWidth) * -progress, 0, 0);
                renderText.run();
                matrices.pop();
                ScissorStack.pop();
                matrices.pop();

                matrices.pop();
            } else {
                int allowTextWidth = this.getWidth() - (this.contentMargin * 2);
                int totalHeight = QueUIConstants.TEXT_HEIGHT + this.iconPadding + this.iconDimension[1];
                int startY = Math.max(0, this.getHeight() - totalHeight) / 2, endY = this.getHeight() - startY;
                boolean iconFirst = this.iconAlignment.getY() < this.textAlignment.getY();

                int offset = (startY - this.contentMargin) * this.contentAlignment.getY();

                matrices.push();
                matrices.translate(this.x, this.y + offset, 0);

                matrices.push();
                int iconX = (this.getWidth() - this.iconDimension[0]) / 2;
                matrices.translate(iconX, startY + (iconFirst ? 0 : (QueUIConstants.TEXT_HEIGHT + this.iconPadding)), 0);
                if (QueUI.DEBUG_MODE) DrawableHelper.fill(matrices, 0, 0, this.iconDimension[0], this.iconDimension[1], 0xFF00FF00);
                matrices.push();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableDepthTest();
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, this.alpha);
                this.getIconRenderer().run(this, matrices, mouseX, mouseY, getMatrixVector(matrices).getLeft(), getMatrixVector(matrices).getRight());
                matrices.pop();
                matrices.pop();

                matrices.push();
                int textX = allowTextWidth < rawTextWidth ? this.contentMargin : ((this.getWidth() - rawTextWidth) / 2);
                matrices.translate(textX, startY + (iconFirst ? (this.iconDimension[1] + this.iconPadding) : 0), 0);
                ScissorStack.push(matrices, 0, 0, allowTextWidth, QueUIConstants.TEXT_HEIGHT);
                if (QueUI.DEBUG_MODE) DrawableHelper.fill(matrices, 0, 0, rawTextWidth, QueUIConstants.TEXT_HEIGHT, 0xFF0000FF);
                matrices.push();
                double progress = getLoopingSmooth((long) (8000L * this.scrollSpeed));
                matrices.translate(Math.max((rawTextWidth - allowTextWidth), 0) * -progress, 0, 0);
                renderText.run();
                matrices.pop();
                ScissorStack.pop();
                matrices.pop();

                matrices.pop();
            }
        } else if (this.getTextUpdater() != null) {
            int allowTextWidth = this.getWidth() - (this.contentMargin * 2);
            matrices.push();
            int textX = allowTextWidth < rawTextWidth ? this.contentMargin : ((this.getWidth() - rawTextWidth) / 2);
            textX += (textX - this.contentMargin) * this.contentAlignment.getX();
            int textY = (this.getHeight() - QueUIConstants.TEXT_HEIGHT) / 2;
            textY += (textY - this.contentMargin) * this.contentAlignment.getY();
            matrices.translate(this.x + textX, this.y + textY, 0);
            ScissorStack.push(matrices, 0, 0, allowTextWidth, QueUIConstants.TEXT_HEIGHT);
            if (QueUI.DEBUG_MODE) DrawableHelper.fill(matrices, 0, 0, rawTextWidth, QueUIConstants.TEXT_HEIGHT, 0xFF0000FF);
            matrices.push();
            double progress = getLoopingSmooth((long) (8000L * this.scrollSpeed));
            matrices.translate(Math.max((rawTextWidth - allowTextWidth), 0) * -progress, 0, 0);
            renderText.run();
            matrices.pop();
            ScissorStack.pop();
            matrices.pop();
        } else if (this.getIconRenderer() != null) {
            matrices.push();
            int iconX = (this.getWidth() - this.iconDimension[0]) / 2;
            iconX += (iconX - this.contentMargin) * this.contentAlignment.getX();
            int iconY = (this.getHeight() - this.iconDimension[1]) / 2;
            iconY += (iconY - this.contentMargin) * this.contentAlignment.getY();
            matrices.translate(this.x + iconX, this.y + iconY, 0);
            matrices.push();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, this.alpha);
            this.getIconRenderer().run(this, matrices, mouseX, mouseY, getMatrixVector(matrices).getLeft(), getMatrixVector(matrices).getRight());
            matrices.pop();
            matrices.pop();
        }

        matrices.pop();
    }

    @Override
    public void setWidgetAlpha(float alpha) {
        this.setAlpha(alpha);
    }

    @Override
    public void setWidgetPosition(float x, float y, boolean updatePosition) {
        if (updatePosition) {
            this.x = (int) x;
            this.y = (int) y;
        }
        this.renderOffset = new float[] { x - this.x, y - this.y };
    }

    public interface RenderSupplier<T extends QueUIButtonWidget<T>> {
        /**
         * @param rawX due to conflict with legacy OpenGL methods (RenderSystem.translatef) it is providing the translated X from matrix
         * @param rawY due to conflict with legacy OpenGL methods (RenderSystem.translatef) it is providing the translated Y from matrix
         */
        void run(QueUIButtonWidget<T> button, MatrixStack matrices, int mouseX, int mouseY, int rawX, int rawY);
    }

    private static Pair<Integer, Integer> getMatrixVector(MatrixStack matrixStack) {
        Matrix4f model = matrixStack.peek().getModel();
        Vector4f vector = new Vector4f(0, 0, 0, 1);
        vector.transform(model);

        return new Pair<>((int) vector.getX(), (int) vector.getY());
    }

    private static double getLoopingSmooth(long duration) {
        double pauseRatio = 0.2;
        double progress = (double) (System.currentTimeMillis() % duration) / duration;

        double v = progress < 0.5 ? progress * 2 : (1 - progress) * 2;
        double expanded = v * (1 + 2 * pauseRatio) - pauseRatio;
        double clamped = Math.max(0.0, Math.min(1.0, expanded));

        return clamped * clamped * (3 - 2 * clamped);
    }
}
