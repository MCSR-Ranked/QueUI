package com.mcsrranked.queui.gui.animate;

import net.minecraft.client.util.math.MatrixStack;

public class EmptyAnimation extends QueUIAnimation {

    public EmptyAnimation(long duration) {
        super(duration);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY) {

    }
}
