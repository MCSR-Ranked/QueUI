package com.mcsrranked.queui.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.opengl.GL11;

public class RenderSystemUtils {

    private static final ScissorTestState SCISSOR = new ScissorTestState();
    static class ScissorTestState {
        public final GlStateManager.CapabilityTracker capState = new GlStateManager.CapabilityTracker(GL11.GL_SCISSOR_TEST);

        ScissorTestState() {
        }
    }

    public static void enableScissor(int x, int y, int width, int height) {
        Window window = MinecraftClient.getInstance().getWindow();
        int i = window.getFramebufferHeight();
        double d = window.getScaleFactor();
        int sx = (int) (x * d);
        int sy = (int) (i - (y + height) * d);
        int sw = (int) (width * d);
        int sh = (int) (height * d);

        RenderSystem.assertThread(RenderSystem::isOnGameThreadOrInit);
        SCISSOR.capState.enable();
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        GL11.glScissor(sx, sy, sw, sh);
    }

    public static void disableScissor() {
        RenderSystem.assertThread(RenderSystem::isOnGameThreadOrInit);
        SCISSOR.capState.disable();
    }

}
