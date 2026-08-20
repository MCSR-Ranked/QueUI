package com.mcsrranked.queui.utils;

import net.minecraft.client.MinecraftClient;

public class PixelUtils {

    public static double snapToPhysicalPixel(double guiPosition) {
        double scaleFactor = MinecraftClient.getInstance().getWindow().getScaleFactor();
        return Math.round(guiPosition * scaleFactor) / scaleFactor;
    }
}
