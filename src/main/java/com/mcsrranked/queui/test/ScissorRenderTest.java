package com.mcsrranked.queui.test;

import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.utils.ScissorStack;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.Identifier;

class ScissorRenderTest {
    private static final Identifier SUN_TEXTURE = new Identifier("textures/environment/sun.png");

    static void init(QueUIScreen screen) {
        screen.getPagination().setRenderPage(1, (matrices, mouseX, mouseY, delta) -> {
            screen.getClient().getTextureManager().bindTexture(SUN_TEXTURE);
            matrices.push();
            ScissorStack.push(30, 30, 60, 30);
            ScissorStack.push(60, 20, 30, 60);
            DrawableHelper.drawTexture(matrices, 20, 20, 128, 128, 0, 0, 32, 32, 32, 32);
            ScissorStack.pop();
            ScissorStack.pop();
            matrices.pop();
        });
    }
}
