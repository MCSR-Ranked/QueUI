package com.mcsrranked.queui.gl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class StencilFramebuffer {
    @SuppressWarnings("DataFlowIssue")
    public static @NotNull Framebuffer INSTANCE = null;

    public static void push(boolean useMaskInside) {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);

        INSTANCE.beginWrite(true);

        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilMask(0xFF);

        // 블렌딩 활성화
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        if (useMaskInside) {
            // 내부만 그리기용
            GL11.glColorMask(false, false, false, false);
            GL11.glDepthMask(false);
        } else {
            // 외부용
            GL11.glColorMask(true, true, true, true);
            GL11.glDepthMask(true);
        }
    }

    public static void pop(boolean useMaskInside) {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);

        // 스텐실 동작 설정
        if (useMaskInside) {
            GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
        } else {
            GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, 0xFF);
        }

        GL11.glStencilMask(0x00);
        GL11.glColorMask(true, true, true, true);
        GL11.glDepthMask(true);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // FBO에 있는 컬러 버퍼 복사
        INSTANCE.endWrite();
        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);

        INSTANCE.draw(MinecraftClient.getInstance().getFramebuffer().textureWidth, MinecraftClient.getInstance().getFramebuffer().textureHeight, false);

        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }
}