package com.mcsrranked.queui.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

public class MouseUtils {

    public static double getGuiX() {
        Window window = MinecraftClient.getInstance().getWindow();
        return MinecraftClient.getInstance().mouse.getX() * window.getScaledWidth() / window.getWidth();
    }

    public static double getGuiY() {
        Window window = MinecraftClient.getInstance().getWindow();
        return MinecraftClient.getInstance().mouse.getY() * window.getScaledHeight() / window.getHeight();
    }
}
