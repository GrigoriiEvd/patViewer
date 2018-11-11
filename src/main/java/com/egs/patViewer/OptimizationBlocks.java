package com.egs.patViewer;

import java.util.ArrayList;
import java.util.List;

public class OptimizationBlocks extends AbstractOptimization {
    public OptimizationBlocks(Configuration cfg) {
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
            PatRectangle k3 = l.get(0);
            boolean flpovt = true;
            boolean fpr = false;
            if (l1.size() > 1) {
                if ((l1.get(l1.size() - 1).getH() == l1.get(l1.size() - 2).getH()) || (l1.get(l1.size() - 1).getA() == l1.get(l1.size() - 2).getA())) {
                    if (((l1.get(l1.size() - 1).getX() == l1.get(l1.size() - 2).getX()) && (l1.get(l1.size() - 1).getY() != l1.get(l1.size() - 2).getY())) || ((l1.get(l1.size() - 1).getX() != l1.get(l1.size() - 2).getX()) && (l1.get(l1.size() - 1).getY() == l1.get(l1.size() - 2).getY()))) {
                        fpr = true;
                    }
                }
                if (fpr) {
                    for (PatRectangle i : l) {
                        if (minx > lengthPat(l1.get(l1.size() - 1), i)) {
                            minx = lengthPat(l1.get(l1.size() - 1), i);
                            k2 = i;
                        }
                        if ((l1.get(l1.size() - 2).getX() - l1.get(l1.size() - 1).getX() ==
                                l1.get(l1.size() - 1).getX() - i.getX()) &&
                                (l1.get(l1.size() - 2).getY() - l1.get(l1.size() - 1).getY() ==
                                        l1.get(l1.size() - 1).getY() - i.getY())) {
                            k3 = i;
                            flpovt = false;
                        }
                    }
                } else {
                    for (PatRectangle i : l) {
                        if (minx > lengthPat(l1.get(l1.size() - 1), i)) {
                            minx = lengthPat(l1.get(l1.size() - 1), i);
                            k2 = i;
                        }
                    }
                }
                if (flpovt) {
                    l1.add(k2);
                    l.remove(k2);
                } else {
                    if (lengthPat(l1.get(l1.size() - 1), k3) <= lengthPat(l1.get(l1.size() - 1), k2) * 1.1) {
                        l1.add(k3);
                        l.remove(k3);
                    } else {
                        l1.add(k2);
                        l.remove(k2);
                    }
                }
            } else {
                for (PatRectangle i : l) {
                    if (minx > lengthPat(l1.get(l1.size() - 1), i)) {
                        minx = lengthPat(l1.get(l1.size() - 1), i);
                        k2 = i;
                    }
                }
                l1.add(k2);
                l.remove(k2);
            }
        }
        return l1;
    }
}
