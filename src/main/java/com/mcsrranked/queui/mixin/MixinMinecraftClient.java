package com.mcsrranked.queui.mixin;

import com.mcsrranked.queui.gl.StencilFramebuffer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Shadow public abstract Framebuffer getFramebuffer();

    @Shadow public abstract Window getWindow();

    @Shadow @Final public static boolean IS_SYSTEM_MAC;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;setClearColor(FFFF)V", shift = At.Shift.AFTER))
    public void onInitFrameBuffer(RunArgs args, CallbackInfo ci) {
        StencilFramebuffer.init(this.getWindow().getFramebufferWidth(), this.getWindow().getFramebufferHeight());
    }

    @Inject(method = "onResolutionChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/Framebuffer;resize(IIZ)V", shift = At.Shift.AFTER))
    public void onResizeFrameBuffer(CallbackInfo ci) {
        StencilFramebuffer.resize(this.getWindow().getFramebufferWidth(), this.getWindow().getFramebufferHeight());
    }
}