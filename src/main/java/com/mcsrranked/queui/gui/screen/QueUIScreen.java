package com.mcsrranked.queui.gui.screen;

import com.google.common.collect.Lists;
import com.mcsrranked.queui.gui.overlay.QueUIOverlay;
import com.mcsrranked.queui.gui.widget.PaginationWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class QueUIScreen extends Screen {

    private final @Nullable Screen parent;

    private List<QueUIOverlay> overlays = Lists.newArrayList();
    private final PaginationWidget paginationWidget = new PaginationWidget();

    private int ticks = 0;
    private boolean isClosed = false;

    public QueUIScreen(@Nullable Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void init(MinecraftClient client, int width, int height) {
        this.paginationWidget.clear();
        super.init(client, width, height);
        this.addChild(this.paginationWidget);
        this.paginationWidget.setPage(this.paginationWidget.getCurrentPage());
    }

    public int getScreenCenter() {
        return this.width / 2;
    }

    public void close() {
        this.onClose();
    }

    @Override
    public void onClose() {
        if (this.client == null) { return; }
        if (this.isClosed) return;
        this.isClosed = true;
        this.client.openScreen(this.parent);
    }

    public void openScreen(Screen screen) {
        if (this.client != null) this.client.openScreen(screen);
    }

    public @Nullable Screen getParent() {
        return parent;
    }

    public int getTicks() {
        return ticks;
    }

    public MinecraftClient getClient() {
        return Objects.requireNonNull(this.client);
    }

    public TextRenderer getTextRenderer() {
        return this.textRenderer;
    }

    public PaginationWidget getPagination() {
        return paginationWidget;
    }

    public void addOverlay(QueUIOverlay overlay) {
        this.overlays.add(overlay);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.paginationWidget.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void postRender(MatrixStack matrices, int mouseX, int mouseY) {
        if (!this.overlays.isEmpty()) {
            for (QueUIOverlay overlay : this.overlays) {
                overlay.render(this, matrices, mouseX, mouseY);
            }
            this.overlays = Lists.newArrayList();
        }
    }

    @Override
    public void tick() {
        this.ticks++;
        this.paginationWidget.tick();
        super.tick();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        this.paginationWidget.mouseScrolled(mouseX, mouseY, amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

}
