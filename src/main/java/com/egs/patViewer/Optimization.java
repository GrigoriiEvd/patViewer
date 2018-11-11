package com.egs.patViewer;

import java.util.ArrayList;
import java.util.List;

public class Optimization extends AbstractOptimization {
    public Optimization(Configuration cfg) {
        super(cfg);
    }

    @Override
    public List<PatRectangle> apply(List<PatRectangle> l) {
        ArrayList<PatRectangle> l1 = new ArrayList<>();
        PatRectangle min = l.get(0);
        for (PatRectangle i : l) {
            if (min.getA() > i.getA()) {
                min = i;
            } else {
                if (((min.getH() + min.getW()) * 100 + min.getX() + min.getY()) > ((i.getH() + i.getW()) * 100 + i.getX() + i.getY())) {
                    min = i;
                }
            }
        }
        l1.add(min);
        l.remove(min);
        int k = l.size();
        for (int j = 0; j < k; j++) {
            double minx = lengthPat(l1.get(l1.size() - 1), l.get(0));
            PatRectangle k2 = l.get(0);
            for (PatRectangle i : l) {
                if (minx > lengthPat(l1.get(l1.size() - 1), i)) {
                    minx = lengthPat(l1.get(l1.size() - 1), i);
                    k2 = i;
                }
            }
            l1.add(k2);
            l.remove(k2);
        }
        return l1;
    }

}
