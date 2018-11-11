package com.egs.patViewer;

import java.util.List;
import java.util.function.Function;

public abstract class AbstractOptimization implements Function<List<PatRectangle>, List<PatRectangle>> {

    protected final Configuration cfg;

    public AbstractOptimization(Configuration cfg) {
        this.cfg = cfg;
    }

    public double lengthPat(PatRectangle x1, PatRectangle y1) {
        return ((cfg.getExelA() * Math.abs(x1.getA() - y1.getA()))
                + (cfg.getExelHW() * (Math.abs(x1.getW() - y1.getW()) + Math.abs(x1.getH() - y1.getH())))
                + (cfg.getExelXY() * (Math.abs(x1.getX() - y1.getX()) + Math.abs(x1.getY() - y1.getY()))));
    }

}
