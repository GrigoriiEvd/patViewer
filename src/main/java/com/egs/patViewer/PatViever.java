package com.egs.patViewer;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EGS on 03.10.2015.
 */


public class PatViever extends JComponent {

    private List<List<PatRectangle>> list = new ArrayList<>();
    private float factor = 1f / 500;
    private int x = 0;
    private int y = 0;
    private int maxX = 0;
    private int maxY = 0;
    private int minX = 0;
    private int minY = 0;
    private boolean reverseX = true;
    private boolean reverseY = true;
    private boolean fOneOutput = false;
    private boolean ffill = false;
    private int sizeOutput = 0;
    private int max = 0;
    private boolean vf = false;
    private int ScreenX;
    private int ScreenY;
    private String SaveImag = "-";
    private static Color[] colors = {Color.black, Color.red, Color.green, Color.yellow, Color.blue, Color.pink, Color.orange, Color.darkGray, Color.cyan, Color.magenta};

    public int getListSize() {
        return list.size();
    }

    public int getOneOutSize() {
        return max;
    }

    public List<List<PatRectangle>> getList() {
        return list;
    }

    public void setList(int i5, List<PatRectangle> l5) {
        list.set(i5, l5);
    }

    public void setSizeProcent(Double d){
        int max2=0;
        for (List<PatRectangle> list1 : list) {
            if (max2<list1.size()){
                max2=list1.size();
            }
        }
        sizeOutput= (int)(max*d);
        repaint();
    }

    public void setFfill(boolean ffill) {
        this.ffill = ffill;
        repaint();
    }

    public void oppositefOutput() {
        if (fOneOutput) {
            fOneOutput = false;
        } else {
            fOneOutput = true;
            sizeOutput = 0;
        }
        repaint();
    }

    public float decSizeVisible() {
        if (sizeOutput > 0) {
            sizeOutput--;
            repaint();
        }
        return sizeOutput;
    }

    public float incSizeVisible() {
        sizeOutput++;
        repaint();
        return sizeOutput;
    }

    public void setReverseX(boolean reverseX) {
        this.reverseX = reverseX;
        repaint();
    }

    public void setReverseY(boolean reverseY) {
        this.reverseY = reverseY;
        repaint();
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public PatViever() {
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getUnitsToScroll() == 3) {
                    x = x + (int) ((e.getX() - x) * 0.1);
                    y = y + (int) ((e.getY() - y) * 0.1);
                    factor *= 0.9;
                    repaint();
                } else {
                    x = x - (int) ((e.getX() - x) * 0.1);
                    y = y - (int) ((e.getY() - y) * 0.1);
                    factor *= 1.1;
                    repaint();
                }
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                x = x - (int) ((e.getX() - x) * 0.1);
                y = y - (int) ((e.getY() - y) * 0.1);
                incF();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    public void paint(Graphics g) {
        BufferedImage bufferedImage = null;
        Graphics2D ig2 = null;
        if (vf) {
            bufferedImage = new BufferedImage(ScreenX, ScreenY, BufferedImage.TYPE_BYTE_GRAY);
            ig2 = bufferedImage.createGraphics();
            ig2.setColor(Color.white);
            ig2.fillRect(0, 0, ScreenX, ScreenY);
            ig2.setColor(Color.BLACK);
        }

        int color = 0;
        for (List<PatRectangle> list1 : list) {
            g.setColor(colors[color++]);
            if (color == 10) {
                color = 0;
            }
            Rectangle bounds = g.getClipBounds();
            double mx[] = new double[4];
            double my[] = new double[4];
            int mx1[] = new int[4];
            int my1[] = new int[4];
            int k = 0;
            boolean flcol = true;
            for (PatRectangle i : list1) {
                if ((sizeOutput > k) || (!fOneOutput)) {
                    k++;
                    mx[0] = i.getX() - (i.getW() / 2);
                    mx[1] = i.getX() + (i.getW() / 2);
                    mx[2] = i.getX() + (i.getW() / 2);
                    mx[3] = i.getX() - (i.getW() / 2);

                    my[0] = i.getY() + (i.getH() / 2);
                    my[1] = i.getY() + (i.getH() / 2);
                    my[2] = i.getY() - (i.getH() / 2);
                    my[3] = i.getY() - (i.getH() / 2);


                    if (reverseX) {
                        for (int j = 0; j < 4; j++) {
                            mx[j] = maxX - mx[j];
                        }
                    }

                    if (reverseY) {
                        for (int j = 0; j < 4; j++) {
                            my[j] = maxY - my[j];
                        }
                    }

                    if (i.getA() != 0) {
                        double radians;
                        double x1, y1, x2, y2;
                        for (int j = 0; j < 4; j++) {
                            radians = Math.toRadians(i.getA() / 10);
                            if (reverseX) {
                                x1 = mx[j] - (maxX - i.getX());
                            } else {
                                x1 = mx[j] - i.getX();
                            }
                            if (reverseY) {
                                y1 = my[j] - (maxY - i.getY());
                            } else {
                                y1 = my[j] - i.getY();
                            }
                            x2 = (x1 * (Math.cos(radians))) - (y1 * (Math.sin(radians)));
                            y2 = (x1 * (Math.sin(radians))) + (y1 * (Math.cos(radians)));
                            if (reverseX) {
                                mx1[j] = (int) (((maxX - i.getX()) + x2) * factor + x);
                            } else {
                                mx1[j] = (int) ((i.getX() + x2) * factor + x);
                            }
                            if (reverseY) {
                                my1[j] = (int) (((maxY - i.getY()) + y2) * factor + y);
                            } else {
                                my1[j] = (int) ((i.getY() + y2) * factor + y);
                            }
                        }
                    } else {
                        for (int j = 0; j < 4; j++) {
                            mx1[j] = (int) (mx[j] * factor + x);
                            my1[j] = (int) (my[j] * factor + y);
                        }
                    }
                    if (vf) {
                        if (ffill) {
                            ig2.fillPolygon(mx1, my1, 4);
                        } else {
                            ig2.drawPolygon(mx1, my1, 4);
                        }
                    } else {
                        if (ffill) {
                            g.fillPolygon(mx1, my1, 4);
                        } else {
                            g.drawPolygon(mx1, my1, 4);
                        }
                    }

                } else {
                    if (flcol) {
                        flcol = false;
                        k++;
                        mx[0] = i.getX() - (i.getW() / 2);
                        mx[1] = i.getX() + (i.getW() / 2);
                        mx[2] = i.getX() + (i.getW() / 2);
                        mx[3] = i.getX() - (i.getW() / 2);

                        my[0] = i.getY() + (i.getH() / 2);
                        my[1] = i.getY() + (i.getH() / 2);
                        my[2] = i.getY() - (i.getH() / 2);
                        my[3] = i.getY() - (i.getH() / 2);


                        if (reverseX) {
                            for (int j = 0; j < 4; j++) {
                                mx[j] = maxX - mx[j];
                            }
                        }

                        if (reverseY) {
                            for (int j = 0; j < 4; j++) {
                                my[j] = maxY - my[j];
                            }
                        }

                        if (i.getA() != 0) {
                            double radians;
                            double x1, y1, x2, y2;
                            for (int j = 0; j < 4; j++) {
                                radians = Math.toRadians(i.getA() / 10);
                                if (reverseX) {
                                    x1 = mx[j] - (maxX - i.getX());
                                } else {
                                    x1 = mx[j] - i.getX();
                                }
                                if (reverseY) {
                                    y1 = my[j] - (maxY - i.getY());
                                } else {
                                    y1 = my[j] - i.getY();
                                }
                                x2 = (x1 * (Math.cos(radians))) - (y1 * (Math.sin(radians)));
                                y2 = (x1 * (Math.sin(radians))) + (y1 * (Math.cos(radians)));
                                if (reverseX) {
                                    mx1[j] = (int) (((maxX - i.getX()) + x2) * factor + x);
                                } else {
                                    mx1[j] = (int) ((i.getX() + x2) * factor + x);
                                }
                                if (reverseY) {
                                    my1[j] = (int) (((maxY - i.getY()) + y2) * factor + y);
                                } else {
                                    my1[j] = (int) ((i.getY() + y2) * factor + y);
                                }
                            }
                        } else {
                            for (int j = 0; j < 4; j++) {
                                mx1[j] = (int) (mx[j] * factor + x);
                                my1[j] = (int) (my[j] * factor + y);
                            }
                        }
                        g.setColor(colors[color++]);
                        if (ffill) {
                            g.fillPolygon(mx1, my1, 4);
                        } else {
                            g.drawPolygon(mx1, my1, 4);
                        }

                        g.setColor(colors[--color]);
                    }
                }
            }
        }
        if (vf) {
            vf = false;
            try {
                ImageIO.write(bufferedImage, "png", new File("image.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

    public void clearList() {
        this.list.clear();
        repaint();
    }

    public void incX() {
        this.x = this.x + 100;
        repaint();
    }

    public void decX() {
        this.x = this.x - 100;
        repaint();
    }

    public void decY() {
        this.y = this.y - 100;
        repaint();
    }

    public void incY() {
        this.y = this.y + 100;
        repaint();
    }

    public void incF() {
        this.factor = (float) (this.factor * 1.1);
        repaint();
    }

    public void decF() {
        this.factor = (float) (this.factor * 0.9);
        repaint();
    }

    public void newList(List<PatRectangle> list1) {
        list.add(list1);
        for (List<PatRectangle> list2 : list) {
            if (list2.size() > max) {
                max = list2.size();
            }
        }
        optimumPosition();
    }

    public void optimumPosition() {
        maxX = -1000000;
        maxY = -1000000;
        minX = 1000000;
        minY = 1000000;
        for (List<PatRectangle> list1 : list) {
            for (PatRectangle i : list1) {
                if (maxX < (i.getX() + i.getW() / 2)) {
                    maxX = i.getX() + i.getW() / 2;
                }
                if (maxY < (i.getY() + i.getH() / 2)) {
                    maxY = i.getY() + i.getH() / 2;
                }
                if (minX > (i.getX() - i.getW() / 2)) {
                    minX = i.getX() - i.getW() / 2;
                }
                if (minY > (i.getY() - i.getH() / 2)) {
                    minY = i.getY() - i.getH() / 2;
                }
            }
        }
        int mx = maxX - minX;
        int my = maxY - minY;
        if ((mx / getSize().getWidth()) > (my / getSize().getHeight())) {
            factor = (float) (-0.00003 + getSize().getWidth() / mx);
            x = 0;
            y = (int) (getSize().getHeight() - (mx * factor)) / 2;
            //  y=0;
        } else {
            factor = (float) (-0.00003 + getSize().getHeight() / my);
            x = (int) (getSize().getWidth() - (mx * factor)) / 2;
            //  x=0;
            y = 0;
        }
        repaint();
    }

    public void setSaveImag(String SvIm) {
        SaveImag = SvIm;
    }

    public void printing() {
        ScreenX = (int) getSize().getWidth();
        ScreenY = (int) getSize().getHeight();
        vf = true;
        repaint();

        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));
        PrintService pss[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.GIF, pras);
        if (pss.length == 0)
            throw new RuntimeException("No printer services available.");
        String s7 = "";
        for (int i = 0; i < pss.length; i++) {
            s7 = s7 + pss[i].getName() + " Введите " + Integer.toString(i) + "\n";
        }
        String input = JOptionPane.showInputDialog(s7);
        PrintService ps = pss[Integer.decode(input)];
        DocPrintJob job = ps.createPrintJob();
        FileInputStream fin = null;
        try {
            fin = new FileInputStream("image.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.GIF, null);
        try {
            job.print(doc, pras);
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PrintException e) {
            e.printStackTrace();
        }
        if (!SaveImag.equals("+")) {
            new File("image.png").delete();
        }
    }


}
