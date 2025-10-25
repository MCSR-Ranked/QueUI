package com.mcsrranked.queui.gui.overlay;

import com.google.common.collect.Lists;
import com.mcsrranked.queui.gui.QueUIConstants;
import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringRenderable;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.List;


public class TooltipOverlay implements QueUIOverlay {

    private final List<StringRenderable> textLines;
    private final @Nullable Integer tooltipX;
    private final @Nullable Integer tooltipY;
    private final int firstLinePadding;

    private TooltipOverlay(List<StringRenderable> textLines, @Nullable Integer tooltipX, @Nullable Integer tooltipY, int firstLinePadding) {
        this.textLines = textLines;
        this.tooltipX = tooltipX;
        this.tooltipY = tooltipY;
        this.firstLinePadding = firstLinePadding;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void render(QueUIScreen screen, MatrixStack matrices, int mouseX, int mouseY) {
        if (this.textLines.isEmpty()) return;

        int maxTextWidth = 0;
        for (StringRenderable stringRenderable : this.textLines) {
            maxTextWidth = Math.max(maxTextWidth, screen.getTextRenderer().getWidth(stringRenderable));
        }

        int x = this.tooltipX != null ? this.tooltipX : mouseX;
        int y = this.tooltipY != null ? this.tooltipY : mouseY;

        int topLeft = x + 12;
        int bottomRight = y - 12;

        int textsHeight = 8;
        if (this.textLines.size() > 1) {
            textsHeight += 2 + (this.textLines.size() - 1) * 10;
        }

        if (topLeft + maxTextWidth > screen.width) {
            topLeft -= 28 + topLeft;
        }

        if (bottomRight + textsHeight + 6 > screen.height) {
            bottomRight = screen.height - textsHeight - 6;
        }

        int backgroundColor = 0xF0100010;
        int lineGradationStart = 0x5050000F;
        int lineGradationEnd = 0x5010000F;
        int zLayer = 400;

        matrices.push();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR);
        Matrix4f matrix4f = matrices.peek().getModel();

        DrawableHelper.fillGradient(matrix4f, bufferBuilder, topLeft - 3, bottomRight - 4, topLeft + maxTextWidth + 3, bottomRight - 3, zLayer, backgroundColor, backgroundColor);
        DrawableHelper.fillGradient(matrix4f, bufferBuilder, topLeft - 3, bottomRight + textsHeight + 3, topLeft + maxTextWidth + 3, bottomRight + textsHeight + 4, zLayer, backgroundColor, backgroundColor);
        DrawableHelper.fillGradient(matrix4f, bufferBuilder, topLeft - 3, bottomRight - 3, topLeft + maxTextWidth + 3, bottomRight + textsHeight + 3, zLayer, backgroundColor, backgroundColor);
        DrawableHelper.fillGradient(matrix4f, bufferBuilder, topLeft - 4, bottomRight - 3, topLeft - 3, bottomRight + textsHeight + 3, zLayer, backgroundColor, backgroundColor);
        DrawableHelper.fillGradient(matrix4f, bufferBuilder, topLeft + maxTextWidth + 3, bottomRight - 3, topLeft + maxTextWidth + 4, bottomRight + textsHeight + 3, zLayer, backgroundColor, backgroundColor);
        DrawableHelper.fillGradient(matrix4f, bufferBuilder, topLeft - 3, bottomRight - 3 + 1, topLeft - 3 + 1, bottomRight + textsHeight + 3 - 1, zLayer, lineGradationStart, lineGradationEnd);
        DrawableHelper.fillGradient(matrix4f, bufferBuilder, topLeft + maxTextWidth + 2, bottomRight - 3 + 1, topLeft + maxTextWidth + 3, bottomRight + textsHeight + 3 - 1, zLayer, lineGradationStart, lineGradationEnd);
        DrawableHelper.fillGradient(matrix4f, bufferBuilder, topLeft - 3, bottomRight - 3, topLeft + maxTextWidth + 3, bottomRight - 3 + 1, zLayer, lineGradationStart, lineGradationStart);
        DrawableHelper.fillGradient(matrix4f, bufferBuilder, topLeft - 3, bottomRight + textsHeight + 2, topLeft + maxTextWidth + 3, bottomRight + textsHeight + 3, zLayer, lineGradationEnd, lineGradationEnd);

        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        matrices.translate(0, 0, zLayer);

        boolean renderedFirstLine = false;
        for (StringRenderable text : this.textLines) {
            screen.getTextRenderer().draw(text, topLeft, bottomRight, QueUIConstants.WHITE_COLOR, true, matrix4f, immediate, false, QueUIConstants.TRANSPARENT_COLOR, QueUIConstants.MAX_LIGHT);

            if (!renderedFirstLine) {
                renderedFirstLine = true;
                bottomRight += this.firstLinePadding;
            }

            bottomRight += 10;
        }

        immediate.draw();
        matrices.pop();
    }


    public static final class Builder {

        private List<? extends StringRenderable> textLines = Lists.newArrayList();
        private @Nullable Integer tooltipX = null;
        private @Nullable Integer tooltipY = null;
        private int firstLinePadding = 2;
        private int wrapWidth = -1;

        public <T extends StringRenderable> Builder setText(T text) {
            this.textLines = Lists.newArrayList(text);
            return this;
        }

        public <T extends StringRenderable> Builder setTexts(List<T> texts) {
            this.textLines = texts;
            return this;
        }

        public Builder setWrapWidth(int width) {
            assert width > 0;
            this.wrapWidth = width;
            return this;
        }

        public Builder setAutoWrap() {
            this.wrapWidth = -1;
            return this;
        }

        public Builder disableWrap() {
            this.wrapWidth = 0;
            return this;
        }

        public Builder setPosition(int x, int y) {
            this.tooltipX = x;
            this.tooltipY = y;
            return this;
        }

        public Builder disableFirstLinePadding() {
            this.firstLinePadding = 0;
            return this;
        }

        public Builder setFirstLinePaddingHeight(int height) {
            assert height >= 0;
            this.firstLinePadding = height;
            return this;
        }

        public TooltipOverlay build(QueUIScreen screen) {
            List<StringRenderable> texts = Lists.newArrayList();
            if (this.wrapWidth != 0) {
                int wrapWidth = this.wrapWidth == -1 ? (int) (screen.width / 0.5f) : this.wrapWidth;
                for (StringRenderable text : this.textLines) {
                    texts.addAll(screen.getTextRenderer().wrapLines(text, wrapWidth));
                }
            } else {
                texts.addAll(this.textLines);
            }

            return new TooltipOverlay(texts, this.tooltipX, this.tooltipY, this.firstLinePadding);
        }
    }

}
