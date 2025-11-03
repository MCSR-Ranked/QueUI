package com.mcsrranked.queui.test;

import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.gui.widget.QueUIButtonWidget;
import com.mcsrranked.queui.type.AlignmentDirection;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;

class ButtonAlignmentTest {
    static void init(QueUIScreen screen) {
        screen.getPagination().addElement(0, new QueUIButtonWidget(10, 10, 60, 30)
                .setContentAlignment(AlignmentDirection.LEFT, 4)
                .setText(new LiteralText("TEST"))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(80, 10, 60, 30)
                .setContentAlignment(AlignmentDirection.CENTER, 4)
                .setText(new LiteralText("TEST"))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(150, 10, 60, 30)
                .setContentAlignment(AlignmentDirection.RIGHT, 4)
                .setText(new LiteralText("TEST"))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(220, 10, 60, 30)
                .setContentAlignment(AlignmentDirection.TOP, 4)
                .setText(new LiteralText("TEST"))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(290, 10, 60, 30)
                .setContentAlignment(AlignmentDirection.BOTTOM, 4)
                .setText(new LiteralText("TEST"))
        );

        screen.getPagination().addElement(0, new QueUIButtonWidget(10, 50, 60, 80)
                .setContentAlignment(AlignmentDirection.LEFT, 4)
                .setIconTextAlignment(AlignmentDirection.LEFT, AlignmentDirection.CENTER)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
                .setText(new LiteralText("TEST"))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(80, 50, 60, 80)
                .setContentAlignment(AlignmentDirection.CENTER, 4)
                .setIconTextAlignment(AlignmentDirection.LEFT, AlignmentDirection.CENTER)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
                .setText(new LiteralText("TEST"))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(150, 50, 60, 80)
                .setContentAlignment(AlignmentDirection.RIGHT, 4)
                .setIconTextAlignment(AlignmentDirection.RIGHT, AlignmentDirection.CENTER)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
                .setText(new LiteralText("TEST"))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(220, 50, 60, 80)
                .setContentAlignment(AlignmentDirection.TOP, 4)
                .setIconTextAlignment(AlignmentDirection.TOP, AlignmentDirection.CENTER)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
                .setText(new LiteralText("TEST"))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(290, 50, 60, 80)
                .setContentAlignment(AlignmentDirection.CENTER, 4)
                .setIconTextAlignment(AlignmentDirection.TOP, AlignmentDirection.CENTER)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
                .setText(new LiteralText("TEST"))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(360, 50, 60, 80)
                .setContentAlignment(AlignmentDirection.BOTTOM, 4)
                .setIconTextAlignment(AlignmentDirection.BOTTOM, AlignmentDirection.CENTER)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
                .setText(new LiteralText("TEST"))
        );

        screen.getPagination().addElement(0, new QueUIButtonWidget(10, 140, 60, 30)
                .setContentAlignment(AlignmentDirection.LEFT, 4)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(80, 140, 60, 30)
                .setContentAlignment(AlignmentDirection.CENTER, 4)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(150, 140, 60, 30)
                .setContentAlignment(AlignmentDirection.RIGHT, 4)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(220, 140, 60, 30)
                .setContentAlignment(AlignmentDirection.TOP, 4)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
        );
        screen.getPagination().addElement(0, new QueUIButtonWidget(290, 140, 60, 30)
                .setContentAlignment(AlignmentDirection.BOTTOM, 4)
                .setIconDimension(16, 16)
                .setIconRenderer((button, matrices, mouseX, mouseY) -> screen.getClient().getItemRenderer().renderGuiItemIcon(new ItemStack(Items.ENDER_EYE), 0, 0))
        );
    }
}
