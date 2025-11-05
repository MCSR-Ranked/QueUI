package com.mcsrranked.queui.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class QueUIToggleButtonWidget<T extends QueUIToggleButtonWidget<T>> extends QueUIButtonWidget<T> {

    private static final int ON_COLOR = BackgroundHelper.ColorMixer.getArgb(220, 0, 255, 0);
    private static final int OFF_COLOR = BackgroundHelper.ColorMixer.getArgb(220, 255, 0, 0);

    private Supplier<Text> textOnUpdater;
    private Supplier<Text> textOffUpdater;
    private Supplier<Boolean> toggleGetter;
    private Consumer<Boolean> toggleSetter;
    private boolean toggle = false;
    private boolean fullBordered = true;

    public QueUIToggleButtonWidget(int x, int y, int width, int height) {
        super(x, y, width, height, false);
        this.textOnUpdater = () -> new TranslatableText("options.on");
        this.textOffUpdater = () -> new TranslatableText("options.off");
        this.toggleGetter = () -> this.toggle;
        this.toggleSetter = (bool) -> this.toggle = bool;
        this.updateMessage();
    }

    @SuppressWarnings("unchecked")
    public T setToggleUpdater(Supplier<Boolean> toggleGetter, Consumer<Boolean> toggleSetter) {
        this.toggleGetter = toggleGetter;
        this.toggleSetter = toggleSetter;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setOnOffMessage(Supplier<Text> onMessage, Supplier<Text> offMessage) {
        this.textOnUpdater = onMessage;
        this.textOffUpdater = offMessage;
        this.updateMessage();
        return (T) this;
    }

    public T setOnOffMessage(Text onMessage, Text offMessage) {
        return this.setOnOffMessage(() -> onMessage, () -> offMessage);
    }

    public T setOnOffMessage(String onMessage, String offMessage) {
        return this.setOnOffMessage(() -> new LiteralText(onMessage), () -> new LiteralText(offMessage));
    }

    public T setOnOffMessageAsString(Supplier<String> onMessage, Supplier<String> offMessage) {
        return this.setOnOffMessage(() -> new LiteralText(onMessage.get()), () -> new LiteralText(offMessage.get()));
    }

    @SuppressWarnings("unchecked")
    public T setFullBordered(boolean fullBordered) {
        this.fullBordered = fullBordered;
        return (T) this;
    }

    public Supplier<Text> getTextOnUpdater() {
        return textOnUpdater;
    }

    public Supplier<Text> getTextOffUpdater() {
        return textOffUpdater;
    }

    public Consumer<Boolean> getToggleSetter() {
        return toggleSetter;
    }

    public Supplier<Boolean> getToggleGetter() {
        return toggleGetter;
    }

    public boolean isToggle() {
        return this.getToggleGetter().get();
    }

    public void setToggle(boolean toggle) {
        this.getToggleSetter().accept(toggle);
    }

    public boolean isFullBordered() {
        return fullBordered;
    }

    @Override
    public void onPress() {
        this.getToggleSetter().accept(!this.getToggleGetter().get());
        super.onPress();
    }

    @Override
    public Supplier<Text> getTextUpdater() {
        return this.isToggle() ? this.getTextOnUpdater() : this.getTextOffUpdater();
    }

    @Override
    protected void renderSelectionMark(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        int color = this.isToggle() ? ON_COLOR : OFF_COLOR;
        if (this.isFullBordered()) {
            fill(matrices, this.x + 1, this.y + 1, this.x + this.getWidth() - 1, this.y + 3, color);
            fill(matrices, this.x + 1, this.y + 3, this.x + 3, this.y + this.getHeight() - 1, color);
            fill(matrices, this.x + this.getWidth() - 3, this.y + 1, this.x + this.getWidth() - 1, this.y + this.getHeight() - 1, color);
            fill(matrices, this.x + 1, this.y + this.getHeight() - 3, this.x + this.getWidth() - 1, this.y + this.getHeight() - 1, color);
            super.renderSelectionMark(matrices, client, mouseX, mouseY);
        } else {
            super.renderSelectionMark(matrices, client, mouseX, mouseY);
            int div = this.getWidth() / 4;
            fill(matrices, this.x + div, this.y + this.getHeight() - 3, this.x + this.getWidth() - div, this.y + this.getHeight() - 1, color);
        }
    }
}
