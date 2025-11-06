package com.mcsrranked.queui.gui.animate;

import com.mcsrranked.queui.gui.animate.type.EasingType;
import com.mcsrranked.queui.gui.widget.AnimatableWidget;
import net.minecraft.client.util.math.MatrixStack;

public class WidgetPositionAnimation extends QueUIAnimation {

    private final AnimatableWidget animatableWidget;
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    private final EasingType easingType;
    private final boolean updatePosition;
    private float easingStrength = 1f;

    public WidgetPositionAnimation(AnimatableWidget animatableWidget, long duration, int startX, int startY, int endX, int endY, EasingType easingType, boolean updatePosition) {
        super(duration);
        this.animatableWidget = animatableWidget;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.easingType = easingType;
        this.updatePosition = updatePosition;
    }

    public WidgetPositionAnimation setEasingStrength(float easingStrength) {
        this.easingStrength = easingStrength;
        return this;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
        float xDiff = this.endX - this.startX;
        float yDiff = this.endY - this.startY;
        float ease = this.easingType.ease(this.getProgress(), this.easingStrength);
        this.animatableWidget.setWidgetPosition(this.startX + (xDiff * ease), this.startY + (yDiff * ease), this.updatePosition);
    }

    @Override
    public void onCancelled() {
        this.animatableWidget.setWidgetPosition(this.startX, this.startY, this.updatePosition);
    }
}
