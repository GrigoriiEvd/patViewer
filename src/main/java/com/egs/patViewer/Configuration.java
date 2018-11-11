package com.egs.patViewer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Configuration {
    private int exelA = 10000;
    private int exelHW = 100;
    private int exelXY = 1;
    private String saveImag = "-";
    private int timerSpeed = 500;
    private String workCatalog = System.getProperty("user.dir");
    private String saveCatalog = System.getProperty("user.dir");

    private int fontSize = 25;
    private int fontX = 1000;
    private int fontY = 100;

    private String printer;

    private boolean writeName;
    private boolean writeData;

    private boolean showNonVisible;
    private Integer nonVisibleColor;

    public int getExelA() {
        return exelA;
    }

    public void setExelA(int exelA) {
        this.exelA = exelA;
    }

    public int getExelHW() {
        return exelHW;
    }

    public void setExelHW(int exelHW) {
        this.exelHW = exelHW;
    }

    public int getExelXY() {
        return exelXY;
    }

    public void setExelXY(int exelXY) {
        this.exelXY = exelXY;
    }

    public String getSaveImag() {
        return saveImag;
    }

    public void setSaveImag(String saveImag) {
        this.saveImag = saveImag;
    }

    public int getTimerSpeed() {
        return timerSpeed;
    }

    public void setTimerSpeed(int timerSpeed) {
        this.timerSpeed = timerSpeed;
    }

    public String getWorkCatalog() {
        return workCatalog;
    }

    public void setWorkCatalog(String workCatalog) {
        this.workCatalog = workCatalog;
    }

    public String getSaveCatalog() {
        return saveCatalog;
    }

    public void setSaveCatalog(String saveCatalog) {
        this.saveCatalog = saveCatalog;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getFontX() {
        return fontX;
    }

    public void setFontX(int fontX) {
        this.fontX = fontX;
    }

    public int getFontY() {
        return fontY;
    }

    public void setFontY(int fontY) {
        this.fontY = fontY;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public boolean isWriteName() {
        return writeName;
    }

    public boolean isWriteData() {
        return writeData;
    }

    public boolean isShowNonVisible() {
        return showNonVisible;
    }

    public Integer getNonVisibleColor() {
        return nonVisibleColor;
    }

    private static Integer readInt(Element elj, String attrName, Integer defValue) {
        String s = elj.getAttribute(attrName).trim();
        if (s.trim().isEmpty()) {
            return defValue;
        }

        return Integer.decode(s);
    }

    private static String readStr(Element elj, String attrName, String defValue) {
        if (elj.hasAttribute(attrName)) {
            return elj.getAttribute(attrName);
        }

        return defValue;
    }

    private static boolean readBool(Element elj, String attrName, boolean defValue) {
        String s = elj.getAttribute(attrName).trim();
        if (s.trim().isEmpty()) {
            return defValue;
        }

        return "+".equals(s) || "true".equalsIgnoreCase(s);
    }

    public static Configuration read() {
        Configuration cfg = new Configuration();

        try {
            File fXml = new File("config.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(fXml);

            doc.getDocumentElement().normalize();

            NodeList nodeLst = doc.getElementsByTagName("point");
            Node fstNode = nodeLst.item(0);
            if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elj = (Element) fstNode;

                cfg.setExelA(Integer.parseInt(elj.getAttribute("A")));
                cfg.setExelHW(Integer.parseInt(elj.getAttribute("HW")));
                cfg.setExelXY(Integer.parseInt(elj.getAttribute("XY")));
                cfg.setSaveImag(elj.getAttribute("SaveImage"));

                int timerSpeed;
                try {
                    timerSpeed = Integer.parseInt(elj.getAttribute("SpeedPrint"));
                } catch (Exception e2) {
                    timerSpeed = 250;
                }
                cfg.setTimerSpeed(timerSpeed);

                try {
                    cfg.setWorkCatalog(elj.getAttribute("WorkCatalog"));
                } catch (Exception ignored) {

                }
                try {
                    cfg.setSaveCatalog(elj.getAttribute("SaveCatalog"));
                } catch (Exception ignored) {

                }

                cfg.setFontSize(Integer.parseInt(elj.getAttribute("FontSize")));
                cfg.setFontX(Integer.parseInt(elj.getAttribute("FontX")));
                cfg.setFontY(Integer.parseInt(elj.getAttribute("FontY")));

                cfg.setPrinter(elj.getAttribute("Printer"));

                cfg.writeName = readBool(elj, "WriteName", true);
                cfg.writeData = readBool(elj, "WriteData", true);

                cfg.showNonVisible = readBool(elj, "ShowNonVisible", true);
                cfg.nonVisibleColor = readInt(elj, "NonVisibleColor", null);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при открытии Xml файла с настройками, применены стандартные");
        }

        return cfg;
    }

}
