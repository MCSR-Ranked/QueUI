package com.mcsrranked.queui.gui.animate;

import com.google.common.collect.Lists;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class AnimationPackage {
    private final List<QueUIAnimation> currentStack;
    private final @Nullable AnimationPackage nextPackage;
    private AnimationPackage targetPackage = this;
    private boolean started = false;

    AnimationPackage(List<QueUIAnimation> currentStack, @Nullable AnimationPackage nextPackage) {
        this.currentStack = currentStack;
        this.nextPackage = nextPackage;
    }

    public void start() {
        for (QueUIAnimation queUIAnimation : this.currentStack) {
            queUIAnimation.start();
        }
        this.targetPackage = this;
        this.started = true;
    }

    public boolean isStarted() {
        return started;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
        boolean isDone = true;
        for (QueUIAnimation queUIAnimation : this.targetPackage.currentStack) {
            if (queUIAnimation.hasStarted()) queUIAnimation.render(matrixStack, mouseX, mouseY);
            if (queUIAnimation.getProgress() != 1.0f) isDone = false;
        }
        if (isDone && this.targetPackage.nextPackage != null) {
            this.targetPackage = this.targetPackage.nextPackage;
            this.targetPackage.start();
        }
    }

    public static class Builder {
        private final List<QueUIAnimation> currentStack = Lists.newArrayList();
        private @Nullable Builder nextPackage = null;

        public Builder add(QueUIAnimation... animations) {
            this.currentStack.addAll(Arrays.asList(animations));
            return this;
        }

        public Builder then(Consumer<Builder> consumer) {
            return this.then(consumer, 0);
        }

        public Builder then(Consumer<Builder> consumer, long delay) {
            Builder newBuilder = new Builder();
            this.nextPackage = newBuilder;
            if (delay > 0) {
                newBuilder.add(new EmptyAnimation(delay));
                newBuilder.then(consumer);
            } else {
                consumer.accept(newBuilder);
            }
            return this;
        }

        public AnimationPackage build() {
            return new AnimationPackage(this.currentStack, this.nextPackage == null ? null : this.nextPackage.build());
        }
    }

}
