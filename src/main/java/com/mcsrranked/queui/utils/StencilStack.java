package com.mcsrranked.queui.utils;

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

        // 깊이/스텐실 버퍼 클리어
        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT, true);
        GL11.glEnable(GL11.GL_STENCIL_TEST);

        RenderSystem.stencilMask(0xFF);
        RenderSystem.clearStencil(0);
        RenderSystem.stencilFunc(GL11.GL_ALWAYS, 0, 0xFF);
    }

    /**
     * 새로운 스텐실 레벨 푸시 (중첩 가능)
     * @param mode 마스크 모드 (INSIDE 또는 OUTSIDE)
     */
    public static void push(Mode mode) {
        ensureInitialized();
        int level = stencilStack.size() + 1;
        stencilStack.push(level);

        // 새 마스크 기록 시작
        RenderSystem.colorMask(false, false, false, false);
        RenderSystem.depthMask(false);
        RenderSystem.stencilMask(0xFF);
        RenderSystem.stencilFunc(GL11.GL_NEVER, level, 0xFF);
        RenderSystem.stencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
    }

    /** 현재 스텐실 마스크 기록 종료 및 모드 적용 */
    public static void endWrite(Mode mode) {
        Integer level = stencilStack.peek();
        if (level == null || level == 0) return;

        // 일반 렌더링 복구
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.depthMask(true);
        RenderSystem.stencilMask(0x00);

        // 내부 / 외부 조건
        if (mode == Mode.INSIDE)
            RenderSystem.stencilFunc(GL11.GL_EQUAL, level, 0xFF);
        else
            RenderSystem.stencilFunc(GL11.GL_NOTEQUAL, level, 0xFF);
    }

    /** 마지막 스텐실 레벨 제거 */
    public static void pop() {
        if (stencilStack.isEmpty()) return;
        stencilStack.pop();

        if (stencilStack.isEmpty()) {
            // 모든 스텐실 종료
            RenderSystem.stencilMask(0xFF);
            GL11.glDisable(GL11.GL_STENCIL_TEST);
            active = false;
        } else {
            // 이전 레벨로 복귀
            int prev = stencilStack.peek();
            RenderSystem.stencilFunc(GL11.GL_EQUAL, prev, 0xFF);
        }
    }

    /** 전체 스텐실 완전 종료 (스택 초기화) */
    public static void clearAll() {
        stencilStack.clear();
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        active = false;
    }
}
