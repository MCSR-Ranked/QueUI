package com.mcsrranked.queui.test;

import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.gui.widget.QueUIButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class TestScreen extends QueUIScreen {

    private static final int INITIAL_PAGE = 0;

    public TestScreen() {
        super(null, new LiteralText("TEST"));
        this.getPagination().setPage(INITIAL_PAGE);
    }

    @Override
    protected void init() {
        this.addButton(new QueUIButtonWidget<>(this.width / 2 - 50, this.height - 30, 100, 20)
                .setTextAsString(() -> "Page: " + this.getPagination().getCurrentPage())
                .setOnPress(button -> this.getPagination().setPage(this.getPagination().getCurrentPage() + (Screen.hasShiftDown() ? -1 : 1))));

        ButtonAlignmentTest.init(this);
        ScissorRenderTest.init(this);
        ButtonVariationTest.init(this);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
