package com.egs.patViewer;

import java.util.Collection;
import java.util.stream.Stream;

public class Statistic {

    private int count;

    private long symProbeg;
    private long kolIzmStor;
    private long kolIzmYgl;

    public Statistic(int count, long symProbeg, long kolIzmStor, long kolIzmYgl) {
        this.count = count;
        this.symProbeg = symProbeg;
        this.kolIzmStor = kolIzmStor;
        this.kolIzmYgl = kolIzmYgl;
    }

    public int getCount() {
        return count;
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

    public static Statistic calculate(Collection<PatRectangle> pat) {
        return calculate(pat.stream());
    }

    public static Statistic calculate(Stream<PatRectangle> pat) {
        StatCollector collector = new StatCollector();
        pat.forEach(collector::add);
        return collector.toStat();
    }

    private static class StatCollector {
        private PatRectangle predRect = null;

        private int count = 0;

        private long symProbeg = 0;
        private long kolIzmStor = 0;
        private long kolIzmYgl = 0;

        void add(PatRectangle rect) {
            count++;

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

        Statistic toStat() {
            return new Statistic(count, symProbeg, kolIzmStor, kolIzmYgl);
        }
    }
}
