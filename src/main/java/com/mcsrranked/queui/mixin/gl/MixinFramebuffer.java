package com.mcsrranked.queui.mixin.gl;

import com.mcsrranked.queui.gl.StencilFramebuffer;
import net.minecraft.client.gl.Framebuffer;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Framebuffer.class)
public class MixinFramebuffer {
    @ModifyArgs(method = "initFbo",
            at = @At(value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/platform/GlStateManager;texImage2D(IIIIIIIILjava/nio/IntBuffer;)V",
                    ordinal = 0))
    public void init(Args args) {
        if (StencilFramebuffer.IS_INITIALIZING) {
            args.set(2, GL30.GL_DEPTH24_STENCIL8);
            args.set(6, GL30.GL_DEPTH_STENCIL);
            args.set(7, GL30.GL_UNSIGNED_INT_24_8);
        System.out.println("GL30 1 part initialised");
        }
    }

    @ModifyArgs (method = "initFbo",
            at = @At (value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;framebufferTexture2D(IIIII)V"),
            slice = @Slice(from = @At (value = "FIELD", target = "Lnet/minecraft/client/gl/Framebuffer;useDepthAttachment:Z", ordinal = 1)))
    public void init2(Args args) {
        if (StencilFramebuffer.IS_INITIALIZING) {
            args.set(1, GL30.GL_DEPTH_STENCIL_ATTACHMENT);
        System.out.println("GL30 2 part initialised");
        }
    }
}
