package com.mcsrranked.queui.gui.widget;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mcsrranked.queui.gui.QueUIConstants;
import com.mcsrranked.queui.gui.overlay.TooltipOverlay;
import com.mcsrranked.queui.gui.screen.QueUIScreen;
import com.mcsrranked.queui.utils.ScissorStack;
import com.mcsrranked.queui.utils.TextUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class QueUICategoryListWidget extends AbstractParentElement implements Drawable, TickableElement {

    private final TextFieldWidget searchBox;
    private final TextRenderer textRenderer;
    private final ListWidget entryWidget;
    private final QueUIScreen screen;
    private final ListWidget categoryWidget;
    private int x;
    private int y;
    private int width;
    private int height;
    private int tooltipWidth;
    private final int margin;
    private final int groupTabWidth;
    private final List<Option> options;
    private boolean visible = true;
    private int[] clearBoxPos = null;
    private final List<Element> children = Lists.newArrayList();

    public QueUICategoryListWidget(QueUIScreen screen, int x, int y, int width, int height, int margin, int groupTabWidth) {
        this(screen, x, y, width, height, margin, groupTabWidth, new ArrayList<>());
    }

    public QueUICategoryListWidget(QueUIScreen screen, int x, int y, int width, int height, int margin, int groupTabWidth, List<Option> options) {
        this.screen = screen;
        this.textRenderer = screen.getClient().textRenderer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.setTooltipWidth(this.width / 2);
        this.height = height;
        this.margin = margin;
        this.groupTabWidth = groupTabWidth;
        this.options = options;
        this.searchBox = new TextFieldWidget(this.textRenderer, this.getX() + this.getMargin(), this.getY() + this.getMargin(), 20, 20, LiteralText.EMPTY);
        this.searchBox.setMaxLength(100);
        this.searchBox.setChangedListener(this::refreshWidgets);
        this.children.add(this.searchBox);

        this.categoryWidget = new ListWidget(this, this.searchBox.x, this.searchBox.y + this.searchBox.getHeight() + this.getMargin(), this.groupTabWidth, height - 40, true);
        this.entryWidget = new ListWidget(this, this.searchBox.x + (this.groupTabWidth > 0 ? this.groupTabWidth + this.getMargin() : 0), this.searchBox.y + this.searchBox.getHeight() + this.getMargin(), width - this.groupTabWidth - (this.getMargin() * (this.groupTabWidth > 0 ? 3 : 2)), height - 40, false);
        if (this.groupTabWidth > 0) this.children.add(this.categoryWidget);
        this.children.add(entryWidget);
        this.refreshWidgets("");
    }

    private void refreshWidgets(String keyword) {
        List<ListWidget.Entry> categories = Lists.newArrayList();
        List<ListWidget.Entry> entryList = Lists.newArrayList();
        Map<String, List<ListWidget.Entry>> widgetEntries = Maps.newLinkedHashMap();
        for (Option option : this.options) {
            if (option.category != null && !widgetEntries.containsKey(option.category)) {
                ListWidget.Entry categoryEntry = new ListWidget.Entry(screen, this.entryWidget, option, true, true);
                widgetEntries.put(option.category, Lists.newArrayList(categoryEntry));
                categories.add(new ListWidget.Entry(screen, categoryWidget, option, true, false).setLink(categoryEntry).setEntries(widgetEntries.get(option.category)));
            }
            if (option.category == null) {
                entryList.add(new ListWidget.Entry(screen, this.entryWidget, option, false, false));
            } else {
                widgetEntries.get(option.category).add(new ListWidget.Entry(screen, this.entryWidget, option, false, false));
            }
        }

        keyword = keyword.trim().toLowerCase(Locale.ROOT);

        // Filtering for non-category entries
        for (ListWidget.Entry entry : Lists.newArrayList(entryList)) {
            String trimTitle = entry.title.getString().toLowerCase(Locale.ROOT).trim();
            String trimDesc = entry.description == null ? null : entry.description.get().getString().toLowerCase(Locale.ROOT).trim();
            String trimTooltip = entry.tooltip == null ? null : entry.tooltip.get().getString().toLowerCase(Locale.ROOT).trim();
            if (!trimTitle.contains(keyword) && !(trimDesc != null && trimDesc.contains(keyword)) && !(trimTooltip != null && trimTooltip.contains(keyword)))
                entryList.remove(entry);
        }

        // Filtering for category entries
        for (Map.Entry<String, List<ListWidget.Entry>> mapEntry : widgetEntries.entrySet()) {
            for (ListWidget.Entry widgetEntry : mapEntry.getValue()) {
                if (widgetEntry.category) entryList.add(widgetEntry);
                else {
                    String lowCat = mapEntry.getKey() == null ? null : mapEntry.getKey().toLowerCase(Locale.ROOT).trim();
                    String trimTitle = widgetEntry.title.getString().toLowerCase(Locale.ROOT).trim();
                    String trimDesc = widgetEntry.description == null ? null : widgetEntry.description.get().getString().toLowerCase(Locale.ROOT).trim();
                    String trimTooltip = widgetEntry.tooltip == null ? null : widgetEntry.tooltip.get().getString().toLowerCase(Locale.ROOT).trim();
                    if ((lowCat != null && lowCat.contains(keyword)) || trimTitle.contains(keyword) || (trimDesc != null && trimDesc.contains(keyword)) || (trimTooltip != null && trimTooltip.contains(keyword)))
                        entryList.add(widgetEntry);
                }
            }
        }

        for (ListWidget.Entry child : Lists.newArrayList(categories)) {
            if (child.entries.stream().filter(entryList::contains).count() <= 1) {
                categories.remove(child);
                entryList.remove(child.linked);
            }
        }

        if (this.groupTabWidth > 0) {
            this.categoryWidget.updateEntry(categories);
            this.categoryWidget.scroll(0);
        }
        this.entryWidget.updateEntry(entryList);
        this.entryWidget.scroll(0);
    }

    public List<Option> getOptions() {
        return options;
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

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
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

    public int getMargin() {
        return margin;
    }

    public int getTooltipWidth() {
        return tooltipWidth;
    }

    public void setTooltipWidth(int tooltipWidth) {
        this.tooltipWidth = tooltipWidth;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!this.isVisible()) return;

        boolean hasSearchText = !this.searchBox.getText().isEmpty();
        this.searchBox.setWidth(this.getWidth() - (this.searchBox.x - this.x) - this.getMargin() - (hasSearchText ? (this.searchBox.getHeight() + 2) : 0));
        this.searchBox.setSuggestion(hasSearchText ? "" : I18n.translate("gui.recipebook.search_hint"));
        fill(matrices, this.searchBox.x, this.searchBox.y, this.searchBox.x + this.searchBox.getWidth(), this.searchBox.y + this.searchBox.getHeight(), 0x66000000);

        for (Element child : this.children) {
            if (child instanceof Drawable) ((Drawable) child).render(matrices, mouseX, mouseY, delta);
        }

        this.clearBoxPos = null;
        if (hasSearchText) {
            int cancelBoxX = this.searchBox.x + this.searchBox.getWidth() + 2;
            this.clearBoxPos = new int[] { cancelBoxX, this.searchBox.y };
            fill(matrices, this.clearBoxPos[0], this.clearBoxPos[1], this.clearBoxPos[0] + this.searchBox.getHeight(), this.clearBoxPos[1] + this.searchBox.getHeight(), 0x66000000);
            this.drawCenteredText(matrices, this.textRenderer, Text.of("×"), cancelBoxX + (this.searchBox.getHeight() / 2), this.searchBox.y + (this.searchBox.getHeight() - QueUIConstants.TEXT_HEIGHT) / 2, QueUIConstants.WHITE_COLOR);
        }
    }

    @Override
    public void tick() {
        for (Element child : this.children) {
            if (child instanceof TickableElement) ((TickableElement) child).tick();
        }
        this.searchBox.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean result = super.mouseClicked(mouseX, mouseY, button);
        if (this.clearBoxPos != null && button == 0) {
            if (mouseX >= this.clearBoxPos[0] && mouseX < this.clearBoxPos[0] + 20 && mouseY >= this.clearBoxPos[1] && mouseY < this.clearBoxPos[1] + 20) {
                QueUIConstants.EMPTY_BUTTON.playDownSound(MinecraftClient.getInstance().getSoundManager());
                this.searchBox.setText("");
                this.searchBox.setSelected(true);
                this.screen.setInitialFocus(this.searchBox);
            }
        }
        return result;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.x <= mouseX && this.x + this.width >= mouseX && this.y <= mouseY && this.y + this.height >= mouseY;
    }

    @Override
    public List<? extends Element> children() {
        return this.children;
    }

    private static class ListWidget extends EntryListWidget<ListWidget.Entry> implements TickableElement {

        private final QueUICategoryListWidget parent;
        private final boolean category;
        private final QueUIScreen screen;

        ListWidget(QueUICategoryListWidget parent, int x, int y, int width, int height, boolean category) {
            super(parent.screen.getClient(), width, height, 0, height, 10);
            this.parent = parent;
            this.category = category;
            this.screen = this.parent.screen;
            this.left = x;
            this.top = y;
            this.right = x + width;
            this.bottom = y + bottom;
            this.width = width;
        }

        public void updateEntry(Collection<Entry> entryCollection) {
            this.clearEntries();
            for (Entry entry : entryCollection) {
                this.addEntry(entry);
            }
        }

        @Override
        public void tick() {
            for (Entry child : this.children()) {
                child.tick();
            }
        }

        public int getRowHeight(int count) {
            int totalHeight = 0;
            for (int i = 0; i < count; i++) {
                totalHeight += this.getEntry(i).getEntryHeight();
            }
            return totalHeight;
        }

        @Override
        protected int getRowTop(int index) {
            return this.top + 4 - (int)this.getScrollAmount() + this.getRowHeight(index) + this.headerHeight;
        }

        @Override
        public int getRowBottom(int index) {
            return this.getRowTop(index) + this.getEntry(index).getEntryHeight();
        }

        @Override
        public int getRowWidth() {
            return this.width;
        }

        @Override
        protected @Nullable Entry getEntryAtPosition(double x, double y) {
            int currentOffset = MathHelper.floor(y - this.top) - this.headerHeight + (int)this.getScrollAmount() - 4;
            if (currentOffset >= 0 && x < this.getScrollbarPositionX() && x >= this.left && x <= this.right) {
                for (int i = 0; i < this.children().size(); i++) {
                    int rowTop = this.getRowTop(i);
                    int rowBottom = this.getRowBottom(i);
                    if (rowTop <= y && rowBottom >= y)
                        return this.children().get(i);
                }
            }
            return null;
        }

        @Override
        protected int getMaxPosition() {
            return this.getRowHeight(this.getItemCount()) + this.headerHeight;
        }

        @Override
        protected void centerScrollOn(Entry entry) {
            this.setScrollAmount(this.getRowBottom(this.children().indexOf(entry)) + entry.getEntryHeight() / 2.0 - (this.bottom - this.top) / 2.0);
        }

        @Override
        protected void ensureVisible(Entry entry) {
            int i = this.getRowTop(this.children().indexOf(entry));
            int j = i - this.top - 4 - entry.getEntryHeight();
            if (j < 0) this.scroll(j);

            int k = this.bottom - i - (entry.getEntryHeight() * 2);
            if (k < 0) this.scroll(-k);
        }

        @Override
        protected int getScrollbarPositionX() {
            return this.right - this.getScrollbarWidth();
        }

        public int getScrollbarWidth() {
            return 6;
        }

        @Override
        protected int getRowLeft() {
            return this.left;
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
            this.setScrollAmount(this.getScrollAmount() - amount * (this.category ? 10 : 25));
            return true;
        }

        @Override
        protected void renderBackground(MatrixStack matrices) {
            fill(matrices, left, top, right, bottom, 0x66000000);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            this.renderBackground(matrices);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            int k = this.getRowLeft();
            int l = this.top + 4 - (int)this.getScrollAmount();

            RenderSystem.pushMatrix();
            ScissorStack.push(matrices, left, top, right - left, bottom - top);
            this.renderList(matrices, k, l, mouseX, mouseY, delta);
            ScissorStack.pop();
            RenderSystem.popMatrix();
            RenderSystem.depthFunc(515);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
            RenderSystem.disableAlphaTest();
            RenderSystem.shadeModel(7425);
            RenderSystem.disableTexture();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(this.left, this.top + 4, 0.0).texture(0.0F, 1.0F).color(0, 0, 0, 0).next();
            bufferBuilder.vertex(this.right, this.top + 4, 0.0).texture(1.0F, 1.0F).color(0, 0, 0, 0).next();
            bufferBuilder.vertex(this.right, this.top, 0.0).texture(1.0F, 0.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.left, this.top, 0.0).texture(0.0F, 0.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.left, this.bottom, 0.0).texture(0.0F, 1.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.right, this.bottom, 0.0).texture(1.0F, 1.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(this.right, this.bottom - 4, 0.0).texture(1.0F, 0.0F).color(0, 0, 0, 0).next();
            bufferBuilder.vertex(this.left, this.bottom - 4, 0.0).texture(0.0F, 0.0F).color(0, 0, 0, 0).next();
            tessellator.draw();
            int o = this.getMaxScroll();
            if (o > 0 && this.isMouseOver(mouseX, mouseY)) {
                int scrollbarX = this.getScrollbarPositionX();
                int scrollbarEndX = scrollbarX + this.getScrollbarWidth();
                int p = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / this.getMaxPosition());
                p = MathHelper.clamp(p, 32, this.bottom - this.top - 8);
                int q = (int)this.getScrollAmount() * (this.bottom - this.top - p) / o + this.top;
                if (q < this.top) {
                    q = this.top;
                }

                bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
                bufferBuilder.vertex(scrollbarX, this.bottom, 0.0).texture(0.0F, 1.0F).color(0, 0, 0, 120).next();
                bufferBuilder.vertex(scrollbarEndX, this.bottom, 0.0).texture(1.0F, 1.0F).color(0, 0, 0, 120).next();
                bufferBuilder.vertex(scrollbarEndX, this.top, 0.0).texture(1.0F, 0.0F).color(0, 0, 0, 120).next();
                bufferBuilder.vertex(scrollbarX, this.top, 0.0).texture(0.0F, 0.0F).color(0, 0, 0, 120).next();
                bufferBuilder.vertex(scrollbarX, q + p, 0.0).texture(0.0F, 1.0F).color(128, 128, 128, 120).next();
                bufferBuilder.vertex(scrollbarEndX, q + p, 0.0).texture(1.0F, 1.0F).color(128, 128, 128, 120).next();
                bufferBuilder.vertex(scrollbarEndX, q, 0.0).texture(1.0F, 0.0F).color(128, 128, 128, 120).next();
                bufferBuilder.vertex(scrollbarX, q, 0.0).texture(0.0F, 0.0F).color(128, 128, 128, 120).next();
                bufferBuilder.vertex(scrollbarX, q + p - 1, 0.0).texture(0.0F, 1.0F).color(192, 192, 192, 120).next();
                bufferBuilder.vertex(scrollbarEndX - 1, q + p - 1, 0.0).texture(1.0F, 1.0F).color(192, 192, 192, 120).next();
                bufferBuilder.vertex(scrollbarEndX - 1, q, 0.0).texture(1.0F, 0.0F).color(192, 192, 192, 120).next();
                bufferBuilder.vertex(scrollbarX, q, 0.0).texture(0.0F, 0.0F).color(192, 192, 192, 120).next();
                tessellator.draw();
            }

            this.renderDecorations(matrices, mouseX, mouseY);
            RenderSystem.enableTexture();
            RenderSystem.shadeModel(7424);
            RenderSystem.enableAlphaTest();
            RenderSystem.disableBlend();
        }

        @SuppressWarnings("deprecation")
        protected void renderList(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
            int i = this.getItemCount();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();

            for (int j = 0; j < i; j++) {
                int k = this.getRowTop(j);
                int l = this.getRowBottom(j);
                this.getEntry(j).rendered = false;
                if (l >= this.top && k <= this.bottom) {
                    this.getEntry(j).rendered = true;
                    int m = y + this.getRowHeight(j) + this.headerHeight;
                    int n = this.getEntry(j).getEntryHeight() - 4;
                    Entry entry = this.getEntry(j);
                    int o = this.getRowWidth();
                    if (this.renderSelection && this.isSelectedItem(j)) {
                        int p = this.left + this.width / 2 - o / 2;
                        int q = this.left + this.width / 2 + o / 2;
                        RenderSystem.disableTexture();
                        float f = this.isFocused() ? 1.0F : 0.5F;
                        RenderSystem.color4f(f, f, f, 1.0F);
                        bufferBuilder.begin(7, VertexFormats.POSITION);
                        bufferBuilder.vertex(p, m + n + 2, 0.0).next();
                        bufferBuilder.vertex(q, m + n + 2, 0.0).next();
                        bufferBuilder.vertex(q, m - 2, 0.0).next();
                        bufferBuilder.vertex(p, m - 2, 0.0).next();
                        tessellator.draw();
                        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                        bufferBuilder.begin(7, VertexFormats.POSITION);
                        bufferBuilder.vertex(p + 1, m + n + 1, 0.0).next();
                        bufferBuilder.vertex(q - 1, m + n + 1, 0.0).next();
                        bufferBuilder.vertex(q - 1, m - 1, 0.0).next();
                        bufferBuilder.vertex(p + 1, m - 1, 0.0).next();
                        tessellator.draw();
                        RenderSystem.enableTexture();
                    }

                    int p = this.getRowLeft();
                    entry.render(
                            matrices, j, k, p, o, n, mouseX, mouseY, this.isMouseOver(mouseX, mouseY) && Objects.equals(this.getEntryAtPosition(mouseX, mouseY), entry), delta
                    );
                }
            }
        }

        private static class Entry extends EntryListWidget.Entry<Entry> implements ParentElement, TickableElement {

            private final MutableText title;
            private final Supplier<MutableText> description;
            private final Supplier<MutableText> tooltip;
            private final Element element;
            private final QueUIScreen screen;
            private final ListWidget parent;
            private final boolean category;
            private final Runnable onClick;
            private final Function<TooltipOverlay.Builder, TooltipOverlay.Builder> tooltipBuilder;
            private boolean rendered = false;
            private Entry linked;
            private List<Entry> entries;
            private long lastFocusTime = 0;
            private boolean dragging;
            private final List<Element> children = Lists.newArrayList();
            private @Nullable Element focusedElement;

            Entry(QueUIScreen screen, ListWidget parent, Option option, boolean category, boolean index) {
                this.screen = screen;
                this.parent = parent;
                this.category = category;
                if (this.category && option.category != null) {
                    this.title = new LiteralText((index ? "· " : "") + option.category).formatted(index ? Formatting.YELLOW : Formatting.WHITE);
                } else {
                    this.title = option.title;
                }

                if (this.category) {
                    this.description = null;
                    this.tooltip = null;
                    this.onClick = null;
                    this.element = null;
                    this.tooltipBuilder = null;
                } else {
                    this.description = option.description;
                    this.tooltip = option.tooltip;
                    this.onClick = option.onClick;
                    this.element = option.element;
                    this.tooltipBuilder = option.tooltipBuilder;
                }

                if (this.element != null) this.children.add(this.element);
            }

            public List<StringRenderable> getDescriptionTexts() {
                if (this.description == null) return Lists.newArrayList();
                return this.screen.getTextRenderer().wrapLines(this.description.get(), this.parent.width - this.getTextPadding() - this.parent.getScrollbarWidth() - this.getElementWidth());
            }

            public int getElementWidth() {
                if (this.element == null) return 0;
                if (this.element instanceof AbstractButtonWidget) return ((AbstractButtonWidget) this.element).getWidth() + 2;
                return 0;
            }

            public int getTextPadding() {
                return this.category ? 4 : 10;
            }

            public int getTitleHeight() {
                return this.category ? 16 : Math.max(24, this.element instanceof AbstractButtonWidget ? ((AbstractButtonWidget) this.element).getHeight() : 0);
            }

            public int getEntryHeight() {
                return this.getTitleHeight() + (this.description == null ? 0 : (2 + this.getDescriptionTexts().size() * 9));
            }

            @Override
            public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                if (this.isMouseOver(mouseX, mouseY)) this.lastFocusTime = System.currentTimeMillis();

                if (this.category && this.linked == null) {
                    fill(matrices, x, y, x + entryWidth, y - 1, 0xFF555555);
                    fill(matrices, x, y + getEntryHeight(), x + entryWidth, y + getEntryHeight() + 1, 0xFF555555);
                }

                if (this.category && this.entries != null && this.entries.stream().anyMatch(e -> e.rendered)) {
                    this.lastFocusTime = System.currentTimeMillis();
                }
                if (System.currentTimeMillis() - this.lastFocusTime < 200) {
                    fill(matrices, x, y, x + entryWidth, y + this.getEntryHeight(), BackgroundHelper.ColorMixer.getArgb((int) (((200 - (System.currentTimeMillis() - this.lastFocusTime)) / 200f) * 40), 255, 255, 255));
                }

                if (isMouseOver(mouseX, mouseY) && this.tooltip != null) {
                    TooltipOverlay.Builder builder = new TooltipOverlay.Builder()
                            .setText(this.tooltip.get())
                            .setWrapWidth(this.parent.parent.tooltipWidth)
                            .disableFirstLinePadding();
                    if (this.tooltipBuilder != null) builder = this.tooltipBuilder.apply(builder);
                    this.screen.addOverlay(builder.build(this.screen));
                }

                int textPaddingY = (this.getTitleHeight() - QueUIConstants.TEXT_HEIGHT) / 2;
                TextUtils.renderScrollText(matrices, this.title, x + this.getTextPadding(), y + textPaddingY, QueUIConstants.WHITE_COLOR, 1, entryWidth - 4 - this.parent.getScrollbarWidth() - this.getElementWidth(), 1.0);
                int lines = 0;
                for (StringRenderable text : this.getDescriptionTexts()) {
                    this.parent.drawTextWithShadow(matrices, this.parent.screen.getTextRenderer(), text, x + this.getTextPadding() + 2, y + textPaddingY + 2 + ((lines++ + 1) * (QueUIConstants.TEXT_HEIGHT + 1)), QueUIConstants.WHITE_COLOR);
                }

                if (this.element instanceof Drawable) {
                    if (this.element instanceof AbstractButtonWidget) {
                        ((AbstractButtonWidget) this.element).x = x + entryWidth - this.parent.getScrollbarWidth() - getElementWidth();
                        ((AbstractButtonWidget) this.element).y = y + (this.getTitleHeight() - ((AbstractButtonWidget) this.element).getHeight()) / 2;
                    }
                    ((Drawable) this.element).render(matrices, mouseX, mouseY, tickDelta);
                }
            }

            @Override
            public void tick() {
                if (this.element instanceof TickableElement) ((TickableElement) this.element).tick();
            }

            @Override
            public List<? extends Element> children() {
                return this.children;
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                boolean result = ParentElement.super.mouseClicked(mouseX, mouseY, button);
                if (this.category && this.linked != null) {
                    this.parent.parent.entryWidget.ensureVisible(this.linked);
                    this.linked.lastFocusTime = System.currentTimeMillis() + 500;
                }
                if (this.element == null || !this.element.isMouseOver(mouseX, mouseY)) {
                    QueUIConstants.EMPTY_BUTTON.playDownSound(MinecraftClient.getInstance().getSoundManager());
                    if (this.onClick != null) this.onClick.run();
                }
                return result;
            }

            @Override
            public boolean isDragging() {
                return this.dragging;
            }

            @Override
            public void setDragging(boolean dragging) {
                this.dragging = dragging;
            }

            @Override
            public @Nullable Element getFocused() {
                return this.focusedElement;
            }

            @Override
            public void setFocused(@Nullable Element focused) {
                this.focusedElement = focused;
            }

            public Entry setLink(Entry entry) {
                this.linked = entry;
                return this;
            }

            public Entry setEntries(List<Entry> entries) {
                this.entries = entries;
                return this;
            }
        }
    }

    public static class Option {
        private String category = null;
        private final MutableText title;
        private Supplier<MutableText> description = null;
        private Supplier<MutableText> tooltip = null;
        private Element element = null;
        private Runnable onClick = null;
        private Function<TooltipOverlay.Builder, TooltipOverlay.Builder> tooltipBuilder = null;

        public Option(MutableText title) {
            this.title = title;
        }

        public Option(String title) {
            this(new LiteralText(title));
        }

        public Option setCategory(String category) {
            this.category = category;
            return this;
        }

        public Option setDescription(String description) {
            return this.setDescription(new LiteralText(description));
        }

        public Option setDescription(MutableText description) {
            return this.setDescription(() -> description);
        }

        public Option setDescription(Supplier<MutableText> description) {
            this.description = description;
            return this;
        }

        public Option setElement(Element element) {
            this.element = element;
            return this;
        }

        public Option setTooltip(String tooltip) {
            return this.setTooltip(new LiteralText(tooltip));
        }

        public Option setTooltip(MutableText tooltip) {
            return this.setTooltip(() -> tooltip);
        }

        public Option setTooltip(Supplier<MutableText> tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Option setOnClick(Runnable onClick) {
            this.onClick = onClick;
            return this;
        }

        public Option setTooltipBuilder(Function<TooltipOverlay.Builder, TooltipOverlay.Builder> tooltipBuilder) {
            this.tooltipBuilder = tooltipBuilder;
            return this;
        }
    }

}
