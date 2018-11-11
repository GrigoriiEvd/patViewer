package com.egs.patViewer;

import java.util.List;

public class Statistic {

    private long symProbeg;
    private long kolIzmStor;
    private long kolIzmYgl;

    public Statistic(long symProbeg, long kolIzmStor, long kolIzmYgl) {
        this.symProbeg = symProbeg;
        this.kolIzmStor = kolIzmStor;
        this.kolIzmYgl = kolIzmYgl;
    }

    public long getSymProbeg() {
        return symProbeg;
    }

    public long getKolIzmStor() {
        return kolIzmStor;
    }

    public long getKolIzmYgl() {
        return kolIzmYgl;
    }

    public static Statistic calculate(List<PatRectangle> pat) {
        PatRectangle predRect = null;

        long symProbeg = 0;
        long kolIzmStor = 0;
        long kolIzmYgl = 0;

        for (PatRectangle rect : pat) {
            if (predRect == null) {
                predRect = rect;
            }
            else {
                symProbeg = symProbeg + Math.abs(predRect.getX() - rect.getX()) + Math.abs(predRect.getY() - rect.getY());
                if ((predRect.getH() != rect.getH()) || (predRect.getW() != rect.getW())) {
                    kolIzmStor++;
                }
                if ((predRect.getA() != rect.getA())) {
                    kolIzmYgl++;
                }
                predRect = rect;
            }
        }

        return new Statistic(symProbeg, kolIzmStor, kolIzmYgl);
    }
}
