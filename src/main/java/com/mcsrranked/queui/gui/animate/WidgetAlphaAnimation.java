package com.mcsrranked.queui.gui.animate;

import com.mcsrranked.queui.gui.animate.type.EasingType;
import com.mcsrranked.queui.gui.widget.AnimatableWidget;
import net.minecraft.client.util.math.MatrixStack;

public class WidgetAlphaAnimation extends QueUIAnimation {

    private final AnimatableWidget animatableWidget;
    private final float startAlpha;
    private final float endAlpha;
    private final EasingType easingType;

    public WidgetAlphaAnimation(AnimatableWidget animatableWidget, long duration, float startAlpha, float endAlpha, EasingType easingType) {
        super(duration);
        this.animatableWidget = animatableWidget;
        this.startAlpha = startAlpha;
        this.endAlpha = endAlpha;
        this.easingType = easingType;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
        float alphaDiff = this.endAlpha - this.startAlpha;
        float ease = this.easingType.ease(this.getProgress());
        this.animatableWidget.setWidgetAlpha(this.startAlpha + (alphaDiff * ease));
    }

    @Override
    public void onCancelled() {
        this.animatableWidget.setWidgetAlpha(this.startAlpha);
    }
}
