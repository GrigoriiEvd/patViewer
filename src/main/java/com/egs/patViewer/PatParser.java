package com.egs.patViewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EGS on 03.10.2015.
 */
public class PatParser {
    public static List<PatRectangle> parser(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<PatRectangle> pr = new ArrayList<>();

            String line = br.readLine();
            line = line.trim();
            String lastLine = "";
            int X = 0;
            int Y = 0;
            int H = 0;
            int W = 0;
            int A = 0;
            boolean F900 = false;
            while (!line.equals("$;")) {
                if (!line.equals(lastLine)) {
                    int i = 1;
                    while (line.charAt(i - 1) != ';') {
                        int x = i;
                        i++;
                        while (isNumeric(line.substring(x, i))) {
                            i++;
                        }
                        switch (line.charAt(x - 1)) {
                            case 'X': {
                                X = Integer.parseInt(line.substring(x, i - 1));
                                break;
                            }
                            case 'Y': {
                                Y = Integer.parseInt(line.substring(x, i - 1));
                                break;
                            }
                            case 'H': {
                                H = Integer.parseInt(line.substring(x, i - 1));
                                break;
                            }
                            case 'W': {
                                W = Integer.parseInt(line.substring(x, i - 1));
                                break;
                            }
                            case 'A': {
                                A = Integer.parseInt(line.substring(x, i - 1));
                                if (A == 900) {
                                    F900 = true;
                                } else {
                                    F900 = false;
                                }
                                break;
                            }
                            default:
                                break;
                        }
                    }
                }
                if (F900) {
                    pr.add(new PatRectangle(X, Y, W, H, 0));
                } else {
                    pr.add(new PatRectangle(X, Y, H, W, A));
                }
                lastLine = line;
                line = br.readLine();
                line = line.trim();
            }

            return pr;
        }
    }

    public static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
