package com.mcsrranked.queui.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.BackgroundHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class QueUIToggleButtonWidget<T extends QueUIToggleButtonWidget<T>> extends QueUIButtonWidget<T> {

    public enum DecorationType { NONE, FULL_BORDER, UNDER_BORDER }

    private static final int ON_COLOR = BackgroundHelper.ColorMixer.getArgb(255, 20, 214, 20);
    private static final int OFF_COLOR = BackgroundHelper.ColorMixer.getArgb(255, 220, 38, 38);
    public static final MutableText ON_TEXT = new TranslatableText("options.on").formatted(Formatting.GREEN);
    public static final MutableText OFF_TEXT = new TranslatableText("options.off").styled(style -> style.withItalic(true).withColor(Formatting.RED));

    private Supplier<Text> textOnUpdater;
    private Supplier<Text> textOffUpdater;
    private Supplier<Boolean> toggleGetter;
    private Consumer<Boolean> toggleSetter;
    private boolean toggle = false;
    private DecorationType decorationType = DecorationType.NONE;

    public QueUIToggleButtonWidget(int x, int y, int width, int height) {
        super(x, y, width, height, false);
        this.textOnUpdater = () -> ON_TEXT;
        this.textOffUpdater = () -> OFF_TEXT;
        this.toggleGetter = () -> this.toggle;
        this.toggleSetter = (bool) -> this.toggle = bool;
        this.updateMessage();
    }

    @SuppressWarnings("unchecked")
    public T setToggleUpdater(Supplier<Boolean> toggleGetter, Consumer<Boolean> toggleSetter) {
        this.toggleGetter = toggleGetter;
        this.toggleSetter = toggleSetter;
        this.updateMessage();
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
    public T setDecorationType(DecorationType decorationType) {
        this.decorationType = decorationType;
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

    public DecorationType getDecorationType() {
        return decorationType;
    }

    @Override
    public void onPress() {
        this.getToggleSetter().accept(!this.getToggleGetter().get());
        super.onPress();
    }

    @Override
    public Function<T, Text> getTextUpdater() {
        return this.isToggle() ? button -> this.getTextOnUpdater().get() : button -> this.getTextOffUpdater().get();
    }

    @Override
    protected void renderSelectionMark(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        int color = this.isToggle() ? ON_COLOR : OFF_COLOR;
        if (this.getDecorationType() == DecorationType.FULL_BORDER) {
            fill(matrices, this.x + 1, this.y + 1, this.x + this.getWidth() - 1, this.y + 3, color);
            fill(matrices, this.x + 1, this.y + 3, this.x + 3, this.y + this.getHeight() - 1, color);
            fill(matrices, this.x + this.getWidth() - 3, this.y + 1, this.x + this.getWidth() - 1, this.y + this.getHeight() - 1, color);
            fill(matrices, this.x + 1, this.y + this.getHeight() - 3, this.x + this.getWidth() - 1, this.y + this.getHeight() - 1, color);
            super.renderSelectionMark(matrices, client, mouseX, mouseY);
        } else if (this.getDecorationType() == DecorationType.UNDER_BORDER) {
            super.renderSelectionMark(matrices, client, mouseX, mouseY);
            int div = 3;
            fill(matrices, this.x + div, this.y + this.getHeight() - 3, this.x + this.getWidth() - div, this.y + this.getHeight() - 1, color);
        }
    }
}
