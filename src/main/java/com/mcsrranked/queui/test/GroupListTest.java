package com.mcsrranked.queui.test;

import com.google.common.collect.Lists;
import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.gui.widget.QueUICategoryListWidget;
import com.mcsrranked.queui.gui.widget.QueUIToggleButtonWidget;

class GroupListTest {

    /**
     * Used Page: 6
     */
    static void init(QueUIScreen screen) {
        screen.getPagination().addElement(6, new QueUICategoryListWidget(screen, 0, 0, screen.width, screen.height - 40, 8, 60, Lists.newArrayList(
                new QueUICategoryListWidget.Option("Test1")
                        .setCategory("Cat1")
                        .setDescription("something testing")
                        .setTooltipDescription(true),
                new QueUICategoryListWidget.Option("Test2")
                        .setCategory("Cat2")
                        .setDescription("something testing")
                        .setTooltipDescription(false),
                new QueUICategoryListWidget.Option("Test3")
                        .setCategory("Cat2")
                        .setDescription("something testing")
                        .setTooltipDescription(true),
                new QueUICategoryListWidget.Option("Test4")
                        .setCategory("Cat3")
                        .setDescription("something testing")
                        .setTooltipDescription(false),
                new QueUICategoryListWidget.Option("BUTTON TEST")
                        .setCategory("Cat2")
                        .setDescription("something testing")
                        .setTooltipDescription(true)
                        .setElement(new QueUIToggleButtonWidget<>(0, 0, 40, 20)
                                .setOnOffMessage("ON", "OFF")),
                new QueUICategoryListWidget.Option("Test4")
                        .setCategory("Cat3")
                        .setDescription("something testing")
                        .setTooltipDescription(false),
                new QueUICategoryListWidget.Option("Test555555555555")
                        .setCategory("Cat3")
                        .setDescription("something testing")
                        .setTooltipDescription(false),
                new QueUICategoryListWidget.Option("Test6")
                        .setCategory("Cat4444444444444444444444")
                        .setDescription("something testing")
                        .setTooltipDescription(true),
                new QueUICategoryListWidget.Option("Test1")
                        .setCategory("Cat1")
                        .setDescription("something testing")
                        .setTooltipDescription(true),
                new QueUICategoryListWidget.Option("Test2")
                        .setCategory("Cat2")
                        .setDescription("something testing")
                        .setTooltipDescription(false)
                        .setElement(new QueUIToggleButtonWidget<>(0, 0, 40, 20)
                                .setOnOffMessage("ON", "OFF")),
                new QueUICategoryListWidget.Option("Test3")
                        .setCategory("Cat2").
                        setDescription("something testing")
                        .setTooltipDescription(true),
                new QueUICategoryListWidget.Option("Test4")
                        .setCategory("Cat3")
                        .setDescription("something testing")
                        .setTooltipDescription(false),
                new QueUICategoryListWidget.Option("Test555555555555")
                        .setCategory("Cat3")
                        .setDescription("something testing")
                        .setTooltipDescription(false),
                new QueUICategoryListWidget.Option("Test6")
                        .setCategory("Cat4444444444444444444444")
                        .setDescription("something testing")
                        .setTooltipDescription(true)
        )));
    }
}
