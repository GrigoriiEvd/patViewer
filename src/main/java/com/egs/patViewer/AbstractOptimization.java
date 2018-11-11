package com.egs.patViewer;

import java.util.List;

public abstract class AbstractOptimization {

    protected final Configuration cfg;

    public AbstractOptimization(Configuration cfg) {
        this.cfg = cfg;
    }

    public abstract List<PatRectangle> apply(List<PatRectangle> patRectangles);

    public double lengthPat(PatRectangle x1, PatRectangle y1) {
        return ((cfg.getExelA() * Math.abs(x1.getA() - y1.getA()))
                + (cfg.getExelHW() * (Math.abs(x1.getW() - y1.getW()) + Math.abs(x1.getH() - y1.getH())))
                + (cfg.getExelXY() * (Math.abs(x1.getX() - y1.getX()) + Math.abs(x1.getY() - y1.getY()))));
    }

}
