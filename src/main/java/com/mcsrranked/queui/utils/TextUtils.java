package com.mcsrranked.queui.utils;

import com.mcsrranked.queui.QueUI;
import com.mcsrranked.queui.gui.QueUIConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextUtils {

    public static void renderScrollText(MatrixStack matrices, Text text, int x, int y, int textColor, float alpha, int width, double speed) {
        int rawTextWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);

        ScissorStack.push(matrices, x, y, width, QueUIConstants.TEXT_HEIGHT);
        if (QueUI.DEBUG_MODE) DrawableHelper.fill(matrices, x, y, rawTextWidth, QueUIConstants.TEXT_HEIGHT, 0xFF0000FF);
        matrices.push();
        double progress = getLoopingSmooth((long) (8000L * speed));
        matrices.translate(Math.max((rawTextWidth - width), 0) * -progress, 0, 0);
        int newColor = ColorUtils.mixAlpha(textColor, alpha);
        if (BackgroundHelper.ColorMixer.getAlpha(newColor) > 3)
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, text, x, y, newColor);
        matrices.pop();
        ScissorStack.pop();
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
