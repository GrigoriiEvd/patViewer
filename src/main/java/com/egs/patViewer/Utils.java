package com.egs.patViewer;

public class Utils {

    private Utils() {
    }

    public static String extractFileName(String path) {
        int idx = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        if (idx < 0)
            return path;

        return path.substring(idx + 1);
    }

    public static boolean insidePolygon(double[] vertx, double[] verty, double x, double y) {
        assert vertx.length == verty.length;

        boolean c = false;
        for (int i = 0, j = vertx.length - 1; i < vertx.length; j = i++) {
            if (((verty[i] > y) != (verty[j] > y)) &&
                    (x < (vertx[j] - vertx[i]) * (y - verty[i]) / (verty[j] - verty[i]) + vertx[i])) {
                c = !c;
            }
        }

        return c;
    }
}
