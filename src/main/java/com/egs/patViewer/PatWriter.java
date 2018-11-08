package com.egs.patViewer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PatWriter {

    public static void save(File file, List<PatRectangle> data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            PatRectangle rectangle = new PatRectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

            for (PatRectangle rect : data) {
                StringBuilder s = new StringBuilder();
                if (rectangle.getX() != rect.getX()) {
                    s.append("X").append(rect.getX());
                }
                if (rectangle.getY() != rect.getY()) {
                    s.append("Y").append(rect.getY());
                }
                if (rectangle.getH() != rect.getH()) {
                    s.append("H").append(rect.getH());
                }
                if (rectangle.getW() != rect.getW()) {
                    s.append("W").append(rect.getW());
                }
                if (rectangle.getA() != rect.getA()) {
                    s.append("A").append(rect.getA());
                }
                s.append(';');

                bw.write(s.toString());

                bw.newLine();
                rectangle = rect;
            }

            bw.write("$;");
        }
    }

}
