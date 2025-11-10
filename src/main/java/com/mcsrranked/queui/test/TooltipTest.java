package com.mcsrranked.queui.test;

import com.mcsrranked.queui.gui.overlay.TooltipOverlay;
import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.gui.widget.QueUIButtonWidget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

class TooltipTest {
    static void init(QueUIScreen screen) {
        screen.getPagination().addElement(4, new QueUIButtonWidget<>(20, 50, 100, 20)
                .setText("test")
                .setIconDimension(16, 16)
                .setIconRenderer((b, matrices, mouseX, mouseY) -> {
                    screen.getClient().getTextureManager().bindTexture(new Identifier("textures/item/ender_eye.png"));
                    DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 16, 16, 16, 16);
                })
                .setOnMouseHover((button, matrices, mouseX, mouseY) -> {
                    screen.addOverlay(new TooltipOverlay.Builder().setText(new TranslatableText("selectWorld.versionWarning", "Test")).build(screen));
                })
        );
        screen.getPagination().addElement(4, new QueUIButtonWidget<>(140, 50, 200, 20)
                .setText("test")
                .setIconDimension(16, 16)
                .setIconRenderer((b, matrices, mouseX, mouseY) -> {
                    screen.getClient().getTextureManager().bindTexture(new Identifier("textures/item/ender_eye.png"));
                    DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 16, 16, 16, 16);
                })
                .setOnMouseHover((button, matrices, mouseX, mouseY) -> {
                    screen.addOverlay(new TooltipOverlay.Builder().setText(new TranslatableText("selectWorld.versionWarning", "Test")).disableFirstLinePadding().build(screen));
                })
        );
        screen.getPagination().addElement(4, new QueUIButtonWidget<>(140, 80, 200, 20)
                .setText("original (vanilla)")
                .setIconDimension(16, 16)
                .setIconRenderer((b, matrices, mouseX, mouseY) -> {
                    screen.getClient().getTextureManager().bindTexture(new Identifier("textures/item/ender_eye.png"));
                    DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 16, 16, 16, 16);
                })
                .setOnMouseHover((button, matrices, mouseX, mouseY) -> {
                    screen.renderTooltip(matrices, screen.getTextRenderer().wrapLines(new TranslatableText("selectWorld.versionWarning", "Test"), screen.width / 2), mouseX, mouseY);
                })
        );
        screen.getPagination().addElement(4, new QueUIButtonWidget<>(0, screen.height - 50, 100, 50)
                .setText("test")
                .setIconDimension(16, 16)
                .setIconRenderer((b, matrices, mouseX, mouseY) -> {
                    screen.getClient().getTextureManager().bindTexture(new Identifier("textures/item/ender_eye.png"));
                    DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 16, 16, 16, 16);
                })
                .setOnMouseHover((button, matrices, mouseX, mouseY) -> {
                    screen.addOverlay(new TooltipOverlay.Builder().setText(new TranslatableText("selectWorld.versionWarning", "Test")).build(screen));
                })
        );
        screen.getPagination().addElement(4, new QueUIButtonWidget<>(screen.width - 100, screen.height - 50, 100, 50)
                .setText("test")
                .setIconDimension(16, 16)
                .setIconRenderer((b, matrices, mouseX, mouseY) -> {
                    screen.getClient().getTextureManager().bindTexture(new Identifier("textures/item/ender_eye.png"));
                    DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 16, 16, 16, 16);
                })
                .setOnMouseHover((button, matrices, mouseX, mouseY) -> {
                    screen.addOverlay(new TooltipOverlay.Builder().setText(new TranslatableText("selectWorld.versionWarning", "Test")).build(screen));
                })
        );

    }
}
