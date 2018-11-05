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

}
