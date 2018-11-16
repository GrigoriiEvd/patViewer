package com.egs.patViewer;

import java.util.Collection;
import java.util.stream.Stream;

public class Statistic {

    private int count;

    private final long symProbeg;
    private final long kolIzmStor;
    private final long kolIzmYgl;

    private final int minX;
    private final int minY;
    private final int maxX;
    private final int maxY;

    private final int invalidCount;

    private Statistic(int count, long symProbeg, long kolIzmStor, long kolIzmYgl,
                     int minX, int minY, int maxX, int maxY,
                     int invalidCount) {
        this.count = count;
        this.symProbeg = symProbeg;
        this.kolIzmStor = kolIzmStor;
        this.kolIzmYgl = kolIzmYgl;

        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        this.invalidCount = invalidCount;
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

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getInvalidCount() {
        return invalidCount;
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

        private int minX = Integer.MAX_VALUE;
        private int minY = Integer.MAX_VALUE;
        private int maxX = Integer.MIN_VALUE;
        private int maxY = Integer.MIN_VALUE;

        private int low4 = 0;
        private int low16 = 0;

        void add(PatRectangle rect) {
            count++;

            minX = Math.min(minX, rect.getX());
            minY = Math.min(minY, rect.getY());
            maxX = Math.max(maxX, rect.getX());
            maxY = Math.max(maxY, rect.getY());

            if (rect.getH() < 4 || rect.getW() < 4)
                low4++;

            if (rect.getH() < 16 || rect.getW() < 16)
                low16++;
            
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
            int invalidCount = maxX > 100_000 || maxY > 100_000 ? low16 : low4;

            return new Statistic(count, symProbeg, kolIzmStor, kolIzmYgl, minX, minY, maxX, maxY, invalidCount);
        }
    }

}
