package com.egs.patViewer;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by EGS on 03.10.2015.
 */


public class PatViewer extends JComponent {

    private List<List<PatRectangle>> list = new ArrayList<>();
    private float factor = 1f / 500;
    private int x = 0;
    private int y = 0;
    private int maxX = 0;
    private int maxY = 0;
    private int minX = 0;
    private int minY = 0;
    private int FontSize;
    private int FontX;
    private int FontY;
    private boolean reverseX = true;
    private boolean reverseY = true;
    private boolean fOneOutput = false;
    private boolean ffill = false;
    private int sizeOutput = 0;
    private int max = 0;
    private String SaveImag = "-";
    private String nameFile = "";
    private static SimpleDateFormat formatdata = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private String Printer = "";
    private Boolean writeName = true;
    private Boolean writeData = true;
    private static Color[] colors = {Color.black, Color.red, Color.green, Color.yellow, Color.blue, Color.pink, Color.orange, Color.darkGray, Color.cyan, Color.magenta};
    private boolean flagLable;

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

    public void setSizePercent(Double d) {
        int max2 = 0;
        for (List<PatRectangle> list1 : list) {
            if (max2 < list1.size()) {
                max2 = list1.size();
            }
        }
        sizeOutput = (int) (max * d);
        repaint();
    }

    public boolean isFfill() {
        return ffill;
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

    public boolean isReverseX() {
        return reverseX;
    }

    public boolean isReverseY() {
        return reverseY;
    }

    public void setReverseX(boolean reverseX) {
        this.reverseX = reverseX;
        repaint();
    }

    public void setReverseY(boolean reverseY) {
        this.reverseY = reverseY;
        repaint();
    }

    public void lableFlag() {
        flagLable = !flagLable;
        repaint();
    }

    public boolean getLableFlag() {
        return flagLable;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public void setNameFile(String NameFile) {
        this.nameFile = NameFile;
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

    public void setFontSize(int FontSize) {
        this.FontSize = FontSize;
    }

    public void setFontX(int FontX) {
        this.FontX = FontX;
    }

    public void setFontY(int FontY) {
        this.FontY = FontY;
    }

    public void setPrinter(String Printer1) {
        this.Printer = Printer1;
    }

    public void setWriteName(Boolean wrna) {
        this.writeName = wrna;
    }

    public void setWriteData(Boolean wrda) {
        this.writeData = wrda;
    }

    public void incCenter(){
        x = x - (int) (((getSize().getWidth()/2) - x) * 0.1);
        y = y - (int) (((getSize().getHeight()/2) - y) * 0.1);
        factor *= 1.1;
        repaint();
    }

    public void decCenter(){
        x = x + (int) (((getSize().getWidth()/2) - x) * 0.1);
        y = y + (int) (((getSize().getHeight()/2) - y) * 0.1);
        factor *= 0.9;
        repaint();
    }

    public PatViewer() {
        addMouseWheelListener(e -> {
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
        });

        AtomicReference<Point> mouseDrugStart = new AtomicReference<>();

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point start = mouseDrugStart.get();
                if (start == null)
                    return;

                Point point = e.getPoint();
                if (!start.equals(point)) {
                    x += point.x - start.x;
                    y += point.y - start.y;
                    mouseDrugStart.set(point);
                    repaint();
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
                    mouseDrugStart.set(e.getPoint());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseDrugStart.set(null);
                }
            }
        });
    }

    public BufferedImage paintToImage() {
        Dimension size = getSize();

        BufferedImage bufferedImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = bufferedImage.createGraphics();

        g.setColor(Color.white);
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        paint0(g);

        g.setColor(Color.BLACK);

        Font font = new Font("Serif", Font.PLAIN, FontSize);

        g.setFont(font);
        if(writeName || flagLable) {
            g.drawString(nameFile.substring(0, nameFile.lastIndexOf(".")), FontX, FontY);
            g.drawString(formatdata.format(new Date().getTime()), size.width / 2, size.height - 20);
        }

        return bufferedImage;
    }

    @Override
    public void paint(Graphics g) {
        paint0(g);
    }

    private void paint0(Graphics g) {
        int color = 0;
        if(flagLable) {
     //       Font font = new Font("Serif", Font.ROMAN_BASELINE, 25);
    //        g.setFont(font);
    //        g.drawString(nameFile.substring(0, nameFile.indexOf(".")), (int) getSize().getWidth() / 2, 35);
            Font font = new Font("Serif", Font.ROMAN_BASELINE, FontSize);
            g.setFont(font);
            g.drawString(nameFile.substring(0, nameFile.lastIndexOf(".")), FontX, FontY);
            g.drawString(formatdata.format(new Date().getTime()), (int) getSize().getWidth() / 2, (int) getSize().getHeight() - 20);
        }

        for (List<PatRectangle> list1 : list) {
            g.setColor(colors[color++]);
            if (color == 10) {
                color = 0;
            }

            double mx[] = new double[4];
            double my[] = new double[4];
            int mx1[] = new int[4];
            int my1[] = new int[4];

            for (int k = 0; k < list1.size(); k++) {
                if ((sizeOutput > k) || (!fOneOutput)) {
                    PatRectangle i = list1.get(k);
                    boolean endFlag = false;

                    if ((sizeOutput - 1 == k) && (fOneOutput)) {
                        endFlag = true;
                    }

                    mx[0] = mx[3] = i.getX() - (i.getW() / 2);
                    mx[1] = mx[2] = i.getX() + (i.getW() / 2);

                    my[0] = my[1] = i.getY() + (i.getH() / 2);
                    my[2] = my[3] = i.getY() - (i.getH() / 2);

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
                        double radians = Math.toRadians(i.getA() / 10d);
                        double x1, y1, x2, y2;
                        for (int j = 0; j < 4; j++) {
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
                    if (endFlag) {
                        g.setColor(colors[color++]);
                    }

                    g.drawPolygon(mx1, my1, 4);
                    if (ffill) {
                        g.fillPolygon(mx1, my1, 4);
                    }

                    if (endFlag) {
                        g.setColor(colors[--color]);
                    }
                }
            }
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

    public float getFactor() {
        return factor;
    }


    public void setX(int x74) {
        this.x=x74;
    }

    public void setY(int y74) {
        this.y=y74;
    }

    public void setFactor(float factor74) {
        this.factor=factor74;
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

    public void addFile(File file) throws IOException {
        if (file == null)
            return;

        List<PatRectangle> list;

        if (file.getName().endsWith(".xls")) {
            list = ExelParser.parser(file);
        } else {
            list = PatParser.parser(file);
        }

        add(list);
    }

    public void add(List<PatRectangle> list1) {
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
        BufferedImage bufferedImage = paintToImage();

        File tempFile = new File("image.png");

        try {
            ImageIO.write(bufferedImage, "png", tempFile);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save image to " + tempFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));
        PrintService pss[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.GIF, pras);
        if (pss.length == 0) {
            JOptionPane.showMessageDialog(null, "Printer not found, screenshot has been saved to  " + tempFile.getAbsolutePath());
            return;
        }

        String input ="";
        for (int i = 0; i < pss.length; i++) {
            if (Printer.equals(pss[i].getName())) {
                input = Integer.toString(i);
            }
        }
        if(input.equals("")){
            StringBuilder s7 = new StringBuilder();
            for (int i = 0; i < pss.length; i++) {
                s7.append(pss[i].getName()).append(" Введите ").append(Integer.toString(i)).append("\n");
            }
            input = JOptionPane.showInputDialog(s7.toString());
        }

        PrintService ps = pss[Integer.parseInt(input)];
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
        } catch (IOException | PrintException e) {
            e.printStackTrace();
        }
        if (!SaveImag.equals("+")) {
            new File("image.png").delete();
        }
    }


}
