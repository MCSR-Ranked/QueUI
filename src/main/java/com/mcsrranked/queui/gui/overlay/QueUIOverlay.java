package com.mcsrranked.queui.gui.overlay;

import com.mcsrranked.queui.gui.screen.QueUIScreen;
import net.minecraft.client.util.math.MatrixStack;

public interface QueUIOverlay {

    void render(QueUIScreen screen, MatrixStack matrices, int mouseX, int mouseY);

}
