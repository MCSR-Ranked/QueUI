package com.mcsrranked.queui.gui;

import com.mcsrranked.queui.gui.widget.QueUIButtonWidget;
import net.minecraft.client.render.LightmapTextureManager;

public class QueUIConstants {
    public static final int WHITE_COLOR = 0xFFFFFFFF;
    public static final int BLACK_COLOR = 0xFF000000;
    public static final int TRANSPARENT_COLOR = 0x00000000;
    public static final int MAX_LIGHT = LightmapTextureManager.pack(15, 15);
    public static final int TEXT_HEIGHT = 8;
    public static final QueUIButtonWidget<?> EMPTY_BUTTON = new QueUIButtonWidget<>(0, 0, 0, 0);
}
