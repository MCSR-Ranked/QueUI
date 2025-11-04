package com.mcsrranked.queui.test;

import com.mcsrranked.queui.gui.QueUIConstants;
import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.utils.ScissorStack;
import com.mcsrranked.queui.utils.StencilStack;
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

            matrices.push();
            StencilStack.push(StencilStack.Mode.INSIDE);
            DrawableHelper.fill(matrices, 190, 30, 190 + 60, 30 + 30, QueUIConstants.BLACK_COLOR);
            DrawableHelper.fill(matrices, 230, 20, 230 + 20, 20 + 60, QueUIConstants.BLACK_COLOR);
            StencilStack.endWrite(StencilStack.Mode.INSIDE);
            DrawableHelper.drawTexture(matrices, 160, 20, 128, 128, 0, 0, 32, 32, 32, 32);
            StencilStack.pop();
            matrices.pop();

            matrices.push();
            StencilStack.push(StencilStack.Mode.OUTSIDE);
            DrawableHelper.fill(matrices, 350, 30, 350 + 60, 30 + 30, QueUIConstants.BLACK_COLOR);
            DrawableHelper.fill(matrices, 390, 20, 390 + 20, 20 + 60, QueUIConstants.BLACK_COLOR);
            StencilStack.endWrite(StencilStack.Mode.OUTSIDE);
            DrawableHelper.drawTexture(matrices, 320, 20, 128, 128, 0, 0, 32, 32, 32, 32);
            StencilStack.pop();
            matrices.pop();
        });
    }
}
