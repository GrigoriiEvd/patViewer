package com.egs.patViewer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EGS on 03.10.2015.
 */
public class PatParser {
    public List<PatRectangle> parser(String name) throws IOException {
        List<PatRectangle> pr = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(name));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        line = line.trim();
        String lastLine = "";
        int X = 0;
        int Y = 0;
        int H = 0;
        int W = 0;
        int A = 0; // todo Pattern.compile("X\\d+")
        while (!line.equals("$;")) {
            if (!line.equals(lastLine)) {
                int i = 1;
                while (line.charAt(i - 1) != ';') {
                    int x = i;
                    i++;
                    while (isNumeric(line.substring(x, i))) {
                        i++;
                    }
                    switch (line.charAt(x-1)) {
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
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
            pr.add(new PatRectangle(X, Y, H, W, A));
            lastLine = line;
            line = br.readLine();
            line =line.trim();
        }
        return pr;
    }
        public static boolean isNumeric(String s) throws NumberFormatException {
            try {
                Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
}
