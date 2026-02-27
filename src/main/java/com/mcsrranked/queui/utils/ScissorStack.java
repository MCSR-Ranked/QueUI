package com.mcsrranked.queui.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.Deque;

public class ScissorStack {
    private static final Deque<int[]> STACK = new ArrayDeque<>();
    private static final GlStateManager.CapabilityTracker SCISSOR_CAP = new GlStateManager.CapabilityTracker(GL11.GL_SCISSOR_TEST);

    public static void push(MatrixStack matrices, int x, int y, int width, int height) {
        Window window = MinecraftClient.getInstance().getWindow();
        int fbHeight = window.getFramebufferHeight();
        double scale = window.getScaleFactor();

        Matrix4f matrix = matrices.peek().getModel();
        Vector4f vector = new Vector4f(x, y, 0, 1);
        vector.transform(matrix);

        float transformedX = vector.getX();
        float transformedY = vector.getY();

        int sx = (int) (transformedX * scale);
        int sy = fbHeight - (int) ((transformedY + height) * scale);
        int sw = (int) (width * scale);
        int sh = (int) (height * scale);

        if (!STACK.isEmpty()) {
            int[] prev = STACK.peek();
            int nx = Math.max(prev[0], sx);
            int ny = Math.max(prev[1], sy);
            int nx2 = Math.min(prev[0] + prev[2], sx + sw);
            int ny2 = Math.min(prev[1] + prev[3], sy + sh);

            sx = nx;
            sy = ny;
            sw = Math.max(0, nx2 - nx);
            sh = Math.max(0, ny2 - ny);
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