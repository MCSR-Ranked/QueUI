package com.mcsrranked.queui.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.Deque;

public class ScissorStack {
    private static final Deque<int[]> STACK = new ArrayDeque<>();
    private static final GlStateManager.CapabilityTracker SCISSOR_CAP = new GlStateManager.CapabilityTracker(GL11.GL_SCISSOR_TEST);

    public static void push(int x, int y, int width, int height) {
        Window window = MinecraftClient.getInstance().getWindow();
        int fbHeight = window.getFramebufferHeight();
        double scale = window.getScaleFactor();

        int sx = (int) (x * scale);
        int sy = (int) (fbHeight - (y + height) * scale);
        int sw = (int) (width * scale);
        int sh = (int) (height * scale);

        if (!STACK.isEmpty()) {
            int[] prev = STACK.peek();
            int nx = Math.max(prev[0], sx);
            int ny = Math.max(prev[1], sy);
            int nx2 = Math.min(prev[0] + prev[2], sx + sw);
            int ny2 = Math.min(prev[1] + prev[3], sy + sh);
            if (nx2 <= nx || ny2 <= ny) {
                sw = sh = 0;
            } else {
                sx = nx;
                sy = ny;
                sw = nx2 - nx;
                sh = ny2 - ny;
            }
        }

        STACK.push(new int[]{sx, sy, sw, sh});

        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        SCISSOR_CAP.enable();
        GL11.glScissor(sx, sy, sw, sh);
    }

    public static void pop() {
        if (STACK.isEmpty()) return;
        STACK.pop();
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);

        if (STACK.isEmpty()) {
            SCISSOR_CAP.disable();
        } else {
            int[] rect = STACK.peek();
            GL11.glScissor(rect[0], rect[1], rect[2], rect[3]);
        }
    }
}