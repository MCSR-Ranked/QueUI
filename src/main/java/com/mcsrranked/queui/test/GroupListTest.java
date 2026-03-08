package com.mcsrranked.queui.test;

import com.google.common.collect.Lists;
import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.gui.widget.QueUICategoryListWidget;
import com.mcsrranked.queui.gui.widget.QueUISliderWidget;
import com.mcsrranked.queui.gui.widget.QueUIToggleButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

class GroupListTest {

    /**
     * Used Page: 6
     */
    static void init(QueUIScreen screen) {
        screen.getPagination().addElement(6, new QueUICategoryListWidget(screen, 0, 0, screen.width, screen.height - 40, 8, 60, Lists.newArrayList(
                new QueUICategoryListWidget.Option("Click Test")
                        .setDescription("something testing")
                        .setTooltip("something testing")
                        .setOnClick(() -> {
                            System.out.println("HEY YOU CLICKED ME!");
                            MinecraftClient.getInstance().getToastManager().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, new LiteralText("Test"), new LiteralText("Hello World!")));
                        }),
                new QueUICategoryListWidget.Option("Test2")
                        .setCategory("Cat2")
                        .setTooltip("something testing")
                        .setElement(new QueUISliderWidget<>(0, 0, 60, 20, Double.class)
                                .setText(value -> Text.of(value.getValueString()))),
                new QueUICategoryListWidget.Option("Test3")
                        .setCategory("Cat2")
                        .setDescription("something testing"),
                new QueUICategoryListWidget.Option("Test4")
                        .setCategory("Cat3")
                        .setDescription("something testing"),
                new QueUICategoryListWidget.Option("BUTTON TEST")
                        .setCategory("Cat2")
                        .setTooltip("something testing")
                        .setElement(new QueUIToggleButtonWidget<>(0, 0, 40, 20)),
                new QueUICategoryListWidget.Option("Test4")
                        .setCategory("Cat3")
                        .setDescription("something testing"),
                new QueUICategoryListWidget.Option("Test555555555555")
                        .setCategory("Cat3")
                        .setDescription("something testing"),
                new QueUICategoryListWidget.Option("Test6")
                        .setCategory("Cat4444444444444444444444")
                        .setDescription("something testing"),
                new QueUICategoryListWidget.Option("Test1")
                        .setCategory("Cat1")
                        .setTooltip("something testing"),
                new QueUICategoryListWidget.Option("Test2")
                        .setCategory("Cat2")
                        .setDescription("something testing")
                        .setElement(new QueUIToggleButtonWidget<>(0, 0, 40, 20)),
                new QueUICategoryListWidget.Option("Test3")
                        .setCategory("Cat2")
                        .setDescription("something testing"),
                new QueUICategoryListWidget.Option("Test4")
                        .setCategory("Cat3")
                        .setTooltip("something testing"),
                new QueUICategoryListWidget.Option("Test555555555555")
                        .setCategory("Cat3")
                        .setTooltip("something testing"),
                new QueUICategoryListWidget.Option("Test6")
                        .setCategory("Cat4444444444444444444444")
                        .setTooltip("something testing")
        )));
    }
}
