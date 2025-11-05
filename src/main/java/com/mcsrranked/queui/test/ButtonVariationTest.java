package com.mcsrranked.queui.test;

import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.gui.widget.QueUIToggleButtonWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

class ButtonVariationTest {
    static void init(QueUIScreen screen) {
        screen.getPagination().addElement(2, new QueUIToggleButtonWidget<>(20, 20, 80, 20));
        screen.getPagination().addElement(2, new QueUIToggleButtonWidget<>(20, 50, 80, 20));
        screen.getPagination().addElement(2, new QueUIToggleButtonWidget<>(120, 20, 80, 20)
                .setOnOffMessage(new TranslatableText("options.on").formatted(Formatting.GREEN), new TranslatableText("options.off").formatted(Formatting.RED)));
        screen.getPagination().addElement(2, new QueUIToggleButtonWidget<>(120, 50, 80, 20)
                .setOnOffMessage(new TranslatableText("options.on").formatted(Formatting.GREEN), new TranslatableText("options.off").formatted(Formatting.RED)));
        screen.getPagination().addElement(2, new QueUIToggleButtonWidget<>(220, 20, 80, 20)
                .setFullBordered(false));
        screen.getPagination().addElement(2, new QueUIToggleButtonWidget<>(220, 50, 80, 20)
                .setFullBordered(false));
        screen.getPagination().addElement(2, new QueUIToggleButtonWidget<>(320, 20, 80, 20)
                .setOnOffMessage(new TranslatableText("options.on").formatted(Formatting.GREEN), new TranslatableText("options.off").formatted(Formatting.RED))
                .setFullBordered(false));
        screen.getPagination().addElement(2, new QueUIToggleButtonWidget<>(320, 50, 80, 20)
                .setOnOffMessage(new TranslatableText("options.on").formatted(Formatting.GREEN), new TranslatableText("options.off").formatted(Formatting.RED))
                .setFullBordered(false));
    }
}
