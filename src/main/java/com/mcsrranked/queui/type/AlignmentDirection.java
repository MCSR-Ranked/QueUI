package com.mcsrranked.queui.type;

public enum AlignmentDirection {
    TOP(0, -1), LEFT(-1, 0), CENTER(0, 0), BOTTOM(0, 1), RIGHT(1, 0);

    private final int xLevel;
    private final int yLevel;

    AlignmentDirection(int xLevel, int yLevel) {
        this.xLevel = xLevel;
        this.yLevel = yLevel;
    }

    public int getX() {
        return xLevel;
    }

    public int getY() {
        return yLevel;
    }
}
