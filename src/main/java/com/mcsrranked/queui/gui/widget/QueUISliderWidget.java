package com.mcsrranked.queui.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class QueUISliderWidget<T extends QueUISliderWidget<T, U>, U extends Number> extends QueUIButtonWidget<T> {

    private final Class<U> type;
    private double value = 0.0;
    private double maxValue = 1.0;
    private double minValue = 0.0;
    private double stepValue = 0;
    private Consumer<U> valueSetter;

    public QueUISliderWidget(int x, int y, int width, int height, Class<U> type) {
        super(x, y, width, height);
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public T setValueUpdater(Supplier<U> valueGetter, Consumer<U> valueSetter) {
        this.valueSetter = valueSetter;
        this.setValue(valueGetter.get());
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setValueRange(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.setValue(this.getValue());
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T setValueStep(double stepValue) {
        this.stepValue = stepValue;
        this.setValue(this.getValue());
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public U getValue() {
        if (this.type == Integer.class) {
            return (U) Integer.valueOf((int) Math.round(this.value));
        } else if (this.type == Long.class) {
            return (U) Long.valueOf(Math.round(this.value));
        } else if (this.type == Float.class) {
            return (U) Float.valueOf((float) this.value);
        }

        return (U) Double.valueOf(this.value);
    }

    public String getValueString() {
        if (this.type == Integer.class || this.type == Long.class) {
            return this.getValue().toString();
        }

        int decimalPlaces = 2;

        if (this.stepValue > 0) {
            if (this.stepValue < 1.0) {
                decimalPlaces = (int) Math.max(0, Math.ceil(-Math.log10(this.stepValue)));
            } else {
                double fractionalPart = this.stepValue - Math.floor(this.stepValue);
                if (fractionalPart > 0) decimalPlaces = (int) Math.max(0, Math.ceil(-Math.log10(fractionalPart)));
                else decimalPlaces = 0;
            }
        }
        String format = "%." + decimalPlaces + "f";
        return String.format(format, this.getValue());
    }

    public void setValue(double value) {
        value = MathHelper.clamp(value, this.minValue, this.maxValue);
        if (this.stepValue > 0) {
            value = Math.round(value / this.stepValue) * this.stepValue;
            value = Math.max(this.minValue, Math.min(this.maxValue, value));
        }

        this.value = value;
        if (this.valueSetter != null) this.valueSetter.accept(this.getValue());
        this.updateMessage();
    }

    public void setValue(U targetValue) {
        this.setValue(targetValue.doubleValue());
    }

    private void setValueFromMouse(double mouseX) {
        double percentage = (mouseX - (this.x + 4)) / (this.getWidth() - 8);
        this.setValue((this.maxValue - this.minValue) * percentage + this.minValue);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean bl = keyCode == GLFW.GLFW_KEY_LEFT;
        if (bl || keyCode == GLFW.GLFW_KEY_RIGHT) {
            float f = bl ? -1.0F : 1.0F;
            if ((modifiers & (1 << 1)) != 0) {
                this.setValue(bl ? this.minValue : this.maxValue);
            } else {
                if (this.stepValue > 0) {
                    this.setValue(this.getValue().doubleValue() + (this.stepValue * f));
                } else {
                    this.setValue(this.getValue().doubleValue() + f / (this.width - 8));
                }
            }
        }

        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_RIGHT) {
            super.playDownSound(MinecraftClient.getInstance().getSoundManager());
        }
        return true;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.setValueFromMouse(mouseX);
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.playDownSound(MinecraftClient.getInstance().getSoundManager());
        super.onRelease(mouseX, mouseY);
    }

    @Override
    public void renderBg(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
        super.renderBg(matrices, client, mouseX, mouseY);

        int hoverOffset = (this.isHovered() ? 2 : 1) * 20;
        double targetValue = this.getValue().doubleValue();
        int sliderOffset = (int) (((targetValue - this.minValue) / (this.maxValue - this.minValue)) * (this.getWidth() - 8));
        this.drawTexture(matrices, this.x + sliderOffset, this.y, 0, 46 + hoverOffset, 4, 3);
        this.drawTexture(matrices, this.x + sliderOffset + 4, this.y, 196, 46 + hoverOffset, 4, 3);
        this.drawTexture(matrices, this.x + sliderOffset, this.y + this.getHeight() - 3, 0, 63 + hoverOffset, 4, 3);
        this.drawTexture(matrices, this.x + sliderOffset + 4, this.y + this.getHeight() - 3, 196, 63 + hoverOffset, 4, 3);
        drawTexture(matrices, this.x + sliderOffset + 1, this.y + 3, 3, this.getHeight() - 6, 0, 50 + hoverOffset, 3, 1, 256, 256);
        drawTexture(matrices, this.x + sliderOffset + 4, this.y + 3, 3, this.getHeight() - 6, 197, 50 + hoverOffset, 3, 1, 256, 256);
    }
}
