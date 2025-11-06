package com.mcsrranked.queui.test;

import com.mcsrranked.queui.gui.animate.AnimationPackage;
import com.mcsrranked.queui.gui.animate.WidgetAlphaAnimation;
import com.mcsrranked.queui.gui.animate.WidgetPositionAnimation;
import com.mcsrranked.queui.gui.animate.type.EasingType;
import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.gui.widget.QueUIButtonWidget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.util.Identifier;

class WidgetAnimationTest {
    static void init(QueUIScreen screen) {
        QueUIButtonWidget<?> button1 = screen.getPagination().addElement(3, new QueUIButtonWidget<>(20, 50, 100, 20)
                .setText("test")
                .setIconDimension(16, 16)
                .setIconRenderer((b, matrices, mouseX, mouseY) -> {
                    screen.getClient().getTextureManager().bindTexture(new Identifier("textures/item/ender_eye.png"));
                    DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 16, 16, 16, 16);
                })
        );
        QueUIButtonWidget<?> button2 = screen.getPagination().addElement(3, new QueUIButtonWidget<>(140, 50, 100, 20)
                .setText("test")
                .setIconDimension(16, 16)
                .setIconRenderer((b, matrices, mouseX, mouseY) -> {
                    screen.getClient().getTextureManager().bindTexture(new Identifier("textures/item/ender_eye.png"));
                    DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 16, 16, 16, 16);
                })
        );
        QueUIButtonWidget<?> button3 = screen.getPagination().addElement(3, new QueUIButtonWidget<>(240, 50, 100, 20)
                .setText("test")
                .setIconDimension(16, 16)
                .setIconRenderer((b, matrices, mouseX, mouseY) -> {
                    screen.getClient().getTextureManager().bindTexture(new Identifier("textures/item/ender_eye.png"));
                    DrawableHelper.drawTexture(matrices, 0, 0, 0, 0, 16, 16, 16, 16);
                })
        );

        AnimationPackage animation = new AnimationPackage.Builder().add(
                new WidgetAlphaAnimation(button1, 2000, 0, 1, EasingType.EASE_OUT),
                new WidgetPositionAnimation(button2, 2000, button2.x, button2.y - 100, button2.x, button2.y, EasingType.EASE_OUT, false),
                new WidgetAlphaAnimation(button2, 2000, 0, 1, EasingType.EASE_OUT),
                new WidgetPositionAnimation(button3, 2000, button3.x, button3.y - 100, button3.x, button3.y, EasingType.EASE_OUT, true)
        )
                .then(builder -> builder.add(new WidgetAlphaAnimation(button2, 2000, 1, 0, EasingType.EASE_IN)), 1000)
                .build();

        screen.addAnimation(animation);

        screen.getPagination().addElement(3, new QueUIButtonWidget<>(20, 20, 80, 20)
                .setText("Animate")
                .setOnPress(b -> animation.start())
        );

    }
}
