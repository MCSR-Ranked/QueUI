package com.mcsrranked.queui.gui.widget;

import com.google.common.collect.Lists;
import com.mcsrranked.queui.gui.QueUIConstants;
import com.mcsrranked.queui.gui.overlay.TooltipOverlay;
import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.utils.TextUtils;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class QueUITabListWidget extends AbstractParentElement implements Drawable {

    public static final int ROW_HEIGHT = 16;

    private final QueUIScreen screen;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean visible = true;
    private final List<TabButton> tabs = Lists.newArrayList();
    private final List<TabButton> bottomTabs = Lists.newArrayList();
    private final List<Element> children = Lists.newArrayList();

    public QueUITabListWidget(QueUIScreen screen, int x, int y, int width, int height) {
        this.screen = screen;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public TabButton addTab(Text title, Supplier<Boolean> selected, Runnable onPress) {
        TabButton tab = new TabButton(this, title, selected, onPress);
        this.tabs.add(tab);
        this.children.add(tab);
        return tab;
    }

    public TabButton addPageTab(Text title, int page) {
        PaginationWidget pagination = this.screen.getPagination();
        return this.addTab(title, () -> pagination.getCurrentPage() == page, () -> pagination.setPage(page));
    }

    public TabButton addBottomTab(Text title, Runnable onPress) {
        TabButton tab = new TabButton(this, title, () -> false, onPress);
        this.bottomTabs.add(tab);
        this.children.add(tab);
        return tab;
    }

    private void layoutTabs() {
        for (int i = 0; i < this.tabs.size(); i++) {
            this.tabs.get(i).setPosition(this.x, this.y + 4 + i * ROW_HEIGHT, this.width);
        }
        for (int i = 0; i < this.bottomTabs.size(); i++) {
            this.bottomTabs.get(i).setPosition(this.x, this.y + this.height - 4 - (this.bottomTabs.size() - i) * ROW_HEIGHT, this.width);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!this.visible) return;

        fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, 0x66000000);
        this.fillGradient(matrices, this.x, this.y, this.x + this.width, this.y + 4, 0xFF000000, 0x00000000);
        this.fillGradient(matrices, this.x, this.y + this.height - 4, this.x + this.width, this.y + this.height, 0x00000000, 0xFF000000);

        this.layoutTabs();
        for (Element child : this.children) {
            ((Drawable) child).render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.visible && super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible && this.x <= mouseX && this.x + this.width > mouseX && this.y <= mouseY && this.y + this.height > mouseY;
    }

    @Override
    public List<? extends Element> children() {
        return this.children;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public static class TabButton extends ButtonWidget {

        private final QueUITabListWidget parent;
        private final Supplier<Boolean> selected;
        private @Nullable Supplier<StringRenderable> tooltip = null;
        private long lastHoverTime = 0;

        private TabButton(QueUITabListWidget parent, Text title, Supplier<Boolean> selected, Runnable onPress) {
            super(0, 0, 0, ROW_HEIGHT, title, button -> onPress.run());
            this.parent = parent;
            this.selected = selected;
        }

        public TabButton setTooltip(Supplier<StringRenderable> tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public TabButton setTooltip(StringRenderable tooltip) {
            return this.setTooltip(() -> tooltip);
        }

        public boolean isSelected() {
            return this.selected.get();
        }

        private void setPosition(int x, int y, int width) {
            this.x = x;
            this.y = y;
            this.width = width;
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            boolean selected = this.isSelected();
            this.active = !selected;
            if (this.isHovered()) this.lastHoverTime = System.currentTimeMillis();
            long sinceHover = System.currentTimeMillis() - this.lastHoverTime;
            int highlightAlpha = selected ? 70 : sinceHover < 200 ? (int) (((200 - sinceHover) / 200f) * 40) : 0;
            if (highlightAlpha > 0) {
                fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, BackgroundHelper.ColorMixer.getArgb(highlightAlpha, 255, 255, 255));
            }
            if (selected) {
                fill(matrices, this.x, this.y, this.x + 2, this.y + this.height, QueUIConstants.WHITE_COLOR);
            }
            TextUtils.renderScrollText(matrices, this.getMessage(), this.x + (selected ? 6 : 4), this.y + (this.height - QueUIConstants.TEXT_HEIGHT) / 2, QueUIConstants.WHITE_COLOR, 1, this.width - (selected ? 12 : 10), 1.0);

            if (this.isHovered() && this.tooltip != null) {
                this.parent.screen.addOverlay(new TooltipOverlay.Builder().setText(this.tooltip.get()).build(this.parent.screen));
            }
        }
    }
}
