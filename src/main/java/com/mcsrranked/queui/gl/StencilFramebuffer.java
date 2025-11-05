package com.mcsrranked.queui.gl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class StencilFramebuffer {
    @SuppressWarnings("DataFlowIssue")
    public static @NotNull Framebuffer INSTANCE = null;
    public static boolean IS_INITIALIZING = false;

    public static void init(int width, int height) {
        INSTANCE = new Framebuffer(width, height, true, MinecraftClient.IS_SYSTEM_MAC);
        INSTANCE.checkFramebufferStatus();
    }

    public static void begin() {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        MinecraftClient.getInstance().getFramebuffer().endWrite();
        INSTANCE.beginWrite(false);
        RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
        RenderSystem.enableTexture();
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public static void end() {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        INSTANCE.endWrite();

        int fbw = MinecraftClient.getInstance().getWindow().getFramebufferWidth();
        int fbh = MinecraftClient.getInstance().getWindow().getFramebufferHeight();

        RenderSystem.enableTexture();
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        INSTANCE.draw(fbw, fbh, MinecraftClient.IS_SYSTEM_MAC);
    }
}
