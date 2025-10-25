package com.mcsrranked.queui.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.mcsrranked.queui.gui.screen.QueUIScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", shift = At.Shift.AFTER))
    public void onPostScreenRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci, @Local(ordinal = 0) int mouseX, @Local(ordinal = 1) int mouseY, @Local MatrixStack matrixStack) {
        if (this.client.currentScreen instanceof QueUIScreen) {
            ((QueUIScreen) this.client.currentScreen).postRender(matrixStack, mouseX, mouseY);
        }
    }

}
