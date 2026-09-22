package com.mcsrranked.queui.test;

import com.google.common.collect.Lists;
import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.gui.widget.QueUICategoryListWidget;
import com.mcsrranked.queui.gui.widget.QueUITabListWidget;
import com.mcsrranked.queui.gui.widget.QueUIToggleButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.List;

class TabListTest {

    static void init(QueUIScreen screen) {
        QueUITabListWidget tabList = new QueUITabListWidget(screen, 0, 0, 100, screen.height - 40);
        tabList.addPageTab(new LiteralText("No Search"), 7);
        tabList.addPageTab(new LiteralText("Category Tabs"), 8);
        tabList.addPageTab(new LiteralText("Spaced Headers"), 9);
        tabList.addBottomTab(new LiteralText("Bottom Tab"), () -> System.out.println("Bottom tab clicked"))
                .setTooltip(new LiteralText("Bottom tab tooltip"));
        for (int page = 7; page <= 9; page++) {
            screen.getPagination().addElement(page, tabList);
        }

        screen.getPagination().addElement(7, new QueUICategoryListWidget(screen, 100, 0, screen.width - 100, screen.height - 40, 8, 0, createOptions(), false));
        screen.getPagination().addElement(8, new QueUICategoryListWidget(screen, 100, 0, screen.width - 100, screen.height - 40, 8, 60, createOptions()));
        screen.getPagination().addElement(9, new QueUICategoryListWidget(screen, 100, 0, screen.width - 100, screen.height - 40, 8, 0, createOptions())
                .setCategoryStyle(Formatting.AQUA, "» ")
                .setCategorySpacing(8));
    }

    private static List<QueUICategoryListWidget.Option> createOptions() {
        List<QueUICategoryListWidget.Option> options = Lists.newArrayList();
        for (int category = 1; category <= 5; category++) {
            for (int option = 1; option <= 4; option++) {
                options.add(new QueUICategoryListWidget.Option("Option " + category + "-" + option)
                        .setCategory("Category " + category)
                        .setTooltip("Tooltip for option " + category + "-" + option)
                        .setElement(new QueUIToggleButtonWidget<>(0, 0, 40, 20)));
            }
        }
        return options;
    }
}
