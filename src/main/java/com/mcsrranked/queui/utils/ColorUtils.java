package com.mcsrranked.queui.utils;

import net.minecraft.client.gui.hud.BackgroundHelper;

public class ColorUtils {
    public static int mixAlpha(int color, float alpha) {
        int newAlpha = (int) (BackgroundHelper.ColorMixer.getAlpha(color) * alpha);
        return BackgroundHelper.ColorMixer.getArgb(
                newAlpha,
                BackgroundHelper.ColorMixer.getRed(color),
                BackgroundHelper.ColorMixer.getGreen(color),
                BackgroundHelper.ColorMixer.getBlue(color)
        );
    }
}
