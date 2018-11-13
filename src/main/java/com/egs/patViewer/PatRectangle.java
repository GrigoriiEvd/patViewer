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

    public boolean isInside(int x, int y) {
        double mx[] = new double[4];
        double my[] = new double[4];

        loadCornerDots(mx, my);

        return Utils.insidePolygon(mx, my, x, y);
    }

    public void loadCornerDots(double mx[], double my[]) {
        mx[0] = mx[3] = - w / 2d;
        mx[1] = mx[2] = w / 2d;

        my[0] = my[1] = h / 2d;
        my[2] = my[3] = - h / 2d;

        if (a != 0) {
            double radians = Math.toRadians(a / 10d);

            for (int j = 0; j < 4; j++) {
                double x1 = mx[j];
                double y1 = my[j];

                mx[j] = ((x1 * (Math.cos(radians))) - (y1 * (Math.sin(radians))));
                my[j] = ((x1 * (Math.sin(radians))) + (y1 * (Math.cos(radians))));
            }
        }

        for (int j = 0; j < 4; j++) {
            mx[j] += this.x;
            my[j] += this.y;
        }
    }

}
