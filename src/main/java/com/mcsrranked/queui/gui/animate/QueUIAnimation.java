package com.mcsrranked.queui.gui.animate;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public abstract class QueUIAnimation {

    private long startTime = 0;
    private final long duration;

    protected QueUIAnimation(long duration) {
        this.duration = duration;
    }

    public void start() {
        if (this.hasStarted()) this.cancel();
        this.startTime = System.currentTimeMillis();
    }

    public boolean hasStarted() {
        return this.startTime != 0;
    }

    public void cancel() {
        if (!this.hasStarted()) return;
        this.startTime += this.getFullDuration();
        this.onCancelled();
    }

    public abstract void render(MatrixStack matrixStack, int mouseX, int mouseY);

    public void onCancelled() {}

    public void onDone() {}

    public long getDuration() {
        return this.hasStarted() ? Math.min(System.currentTimeMillis() - this.startTime, this.getFullDuration()) : 0;
    }

    public float getProgress() {
        return MathHelper.clamp(this.getDuration() / (this.getFullDuration() * 1f), 0f, 1f);
    }

    public long getFullDuration() {
        return this.duration;
    }

}
