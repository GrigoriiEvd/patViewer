package com.egs.patViewer;

/**
 * Created by EGS on 03.10.2015.
 */
public class PatRectangle {
    private int x, y, h, w, a;

    public PatRectangle(int x, int y, int h, int w, int a) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
        this.a = a;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public int getA() {
        return a;
    }
}
