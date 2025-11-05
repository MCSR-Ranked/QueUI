package com.mcsrranked.queui.utils;

import com.mcsrranked.queui.gl.StencilFramebuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.Deque;

public class StencilStack {

    public enum Mode {
        INSIDE,
        OUTSIDE
    }

    private static final Deque<Integer> stencilStack = new ArrayDeque<>();
    private static boolean active = false;

    private static void ensureInitialized() {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        if (active) return;
        active = true;

        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT, true);
        GL11.glEnable(GL11.GL_STENCIL_TEST);

        RenderSystem.stencilMask(0xFF);
        RenderSystem.clearStencil(0);
        RenderSystem.stencilFunc(GL11.GL_ALWAYS, 0, 0xFF);
    }

    public static void push() {
        if (stencilStack.isEmpty()) StencilFramebuffer.begin();
        ensureInitialized();
        int level = stencilStack.size() + 1;
        stencilStack.push(level);

        RenderSystem.colorMask(false, false, false, false);
        RenderSystem.depthMask(false);
        RenderSystem.stencilMask(0xFF);
        RenderSystem.stencilFunc(GL11.GL_NEVER, level, 0xFF);
        RenderSystem.stencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
    }

    public static void endWrite(Mode mode) {
        Integer level = stencilStack.peek();
        if (level == null || level == 0) return;

        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.depthMask(true);
        RenderSystem.stencilMask(0x00);

        if (mode == Mode.INSIDE) RenderSystem.stencilFunc(GL11.GL_EQUAL, level, 0xFF);
        else RenderSystem.stencilFunc(GL11.GL_NOTEQUAL, level, 0xFF);
    }

    public static void pop() {
        if (stencilStack.isEmpty()) return;
        stencilStack.pop();

        if (stencilStack.isEmpty()) {
            RenderSystem.stencilMask(0xFF);
            clearAll();
            StencilFramebuffer.end();
        } else {
            int prev = stencilStack.peek();
            RenderSystem.stencilFunc(GL11.GL_EQUAL, prev, 0xFF);
        }
    }

    public static void clearAll() {
        stencilStack.clear();
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        active = false;
    }
}
