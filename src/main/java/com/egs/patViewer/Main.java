package com.egs.patViewer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class Main {

    private static int timerSpeed = 500;
    private static String workCatalog;
    private static String saveCatalog;
    private static PatViewer patViewer;
    private static JProgressBar x;
    private static boolean fpor = true;
    private static int ExelA = 10000;
    private static int ExelHW = 100;
    private static int ExelXY = 1;
    private static int FontSize = 25;
    private static int FontX = 1000;
    private static int FontY = 100;
    private static int symProbeg = 0;
    private static int kolIzmStor = 0;
    private static int kolIzmYgl = 0;

    private static PatRectangle predRect;

    private static File openFile() {
        JFileChooser fileopen = new JFileChooser(workCatalog);
        fileopen.setSize(1700, 1600);
        
        fileopen.setFileFilter(new PatFileFilter());

        int ret = fileopen.showDialog(null, "Open File");
        if (ret == JFileChooser.APPROVE_OPTION) {
            return fileopen.getSelectedFile();
        } else {
            return null;
        }
    }

    private static JMenuBar createMenu(PatViewer patViewer, JComboBox<String> comboBox) {
        JMenuItem openFile = new JMenuItem("Open file", KeyEvent.VK_O);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openFile.addActionListener(e -> {
            File file12 = openFile();
            if (file12 != null) {
                try {
                    patViewer.addFile(file12);
                    comboBox.addItem(file12.getAbsolutePath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to opne file: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem saveFile = new JMenuItem("Save as...", KeyEvent.VK_S);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveFile.addActionListener(e -> {
            JFileChooser fileopen = new JFileChooser(saveCatalog);
            fileopen.addChoosableFileFilter(new PatFileFilter());
            fileopen.setAcceptAllFileFilterUsed(false);
            int ret = fileopen.showDialog(null, "Save File");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file1 = fileopen.getSelectedFile();

                if (file1.getName().indexOf('.') < 0) {
                    file1 = new File(file1.getPath() + ".opg");
                }

                try {
                    PatWriter.save(file1, patViewer.getList().stream().flatMap(Collection::stream).collect(Collectors.toList()));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem clear = new JMenuItem("Close All");
        clear.addActionListener(e -> {
            if (patViewer.getLableFlag()) {
                patViewer.lableFlag();
            }
            patViewer.clearList();
            comboBox.removeAllItems();
        });

        JMenuItem print = new JMenuItem("Print", KeyEvent.VK_P);
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        print.addActionListener(e -> {
            patViewer.setNameFile(Utils.extractFileName(comboBox.getItemAt(comboBox.getSelectedIndex())));
            patViewer.printing();
        });

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(print);
        fileMenu.add(clear);

        JMenuItem invertX = new JCheckBoxMenuItem("Инвертировать по оси X");
        invertX.setMnemonic(KeyEvent.VK_X);
        invertX.setSelected(patViewer.isReverseX());
        invertX.addChangeListener(e -> patViewer.setReverseX(invertX.isSelected()));

        JMenuItem invertY = new JCheckBoxMenuItem("Инвертировать по оси Y");
        invertY.setMnemonic(KeyEvent.VK_Y);
        invertY.setSelected(patViewer.isReverseY());
        invertY.addChangeListener(e -> patViewer.setReverseY(invertY.isSelected()));

        JMenuItem fill = new JCheckBoxMenuItem("Fill rectangles");
        fill.setMnemonic(KeyEvent.VK_F);
        fill.setSelected(patViewer.isFfill());
        fill.addChangeListener(e -> patViewer.setFfill(fill.isSelected()));

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        viewMenu.add(invertX);
        viewMenu.add(invertY);
        viewMenu.add(fill);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        return menuBar;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        JLabel label1 = new JLabel("");
        JLabel label2 = new JLabel("");
        JLabel label3 = new JLabel("");
        JLabel label4 = new JLabel("");

        patViewer = new PatViewer();
        JComboBox<String> comboBox = new JComboBox<>();

        ReadXml();

        File file;

        if (args.length > 0) {
            file = new File(args[0]);
        } else {
            file = openFile();
        }

        if (file != null) {
            try {
                patViewer.addFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to read file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        JFrame f = new JFrame();

        f.setJMenuBar(createMenu(patViewer, comboBox));

        f.setTitle("Drawing Graphics in Frames");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel viewerPanel = new JPanel(new BorderLayout());
        viewerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        viewerPanel.add(patViewer, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(viewerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JPanel menuPanel = new JPanel();
        BoxLayout buttonPaneLayout = new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS);
        buttonPanel.setLayout(buttonPaneLayout);

        comboBox.addActionListener(e -> {
            if (comboBox.getItemCount() > 0) {
                label1.setText("В выбранном файле " + Integer.toString(patViewer.getList().get(comboBox.getSelectedIndex()).size()) + " элементов   ");
                int symProbeg = 0;
                int kolIzmStor = 0;
                int kolIzmYgl = 0;
                PatRectangle predRect = patViewer.getList().get(comboBox.getSelectedIndex()).get(0);
                for (PatRectangle i : patViewer.getList().get(comboBox.getSelectedIndex())) {
                    symProbeg = symProbeg + Math.abs(predRect.getX() - i.getX()) + Math.abs(predRect.getY() - i.getY());
                    if ((predRect.getH() != i.getH()) || (predRect.getW() != i.getW())) {
                        kolIzmStor++;
                    }
                    if ((predRect.getA() != i.getA())) {
                        kolIzmYgl++;
                    }
                    predRect = i;
                }
                label2.setText("Cуммарный пробег по X/Y " + String.format("%.1f", ((float) symProbeg / 1000000f)) + "   ");
                label3.setText("Изменение шторки " + Integer.toString(kolIzmStor) + "   ");
                label4.setText("Изменение угла поворота " + Integer.toString(kolIzmYgl) + "   ");
            } else {
                label1.setText("");
                label2.setText("");
                label3.setText("");
                label4.setText("");
            }
        });

        buttonPanel.add(Box.createVerticalStrut(20));

        JButton btnLable = new JButton("Название файла");
        btnLable.addActionListener(e -> {
            patViewer.setNameFile(Utils.extractFileName(comboBox.getItemAt(comboBox.getSelectedIndex())));

            patViewer.lableFlag();
        });

        menuPanel.add(btnLable);

        menuPanel.add(comboBox);
        JButton btnOptimKv = new JButton("Оптимизировать файл с блоками");
        btnOptimKv.addActionListener(e -> {
            symProbeg = 0;
            kolIzmStor = 0;
            kolIzmYgl = 0;
            patViewer.setList(comboBox.getSelectedIndex(), OptimumPat1(patViewer.getList().get(comboBox.getSelectedIndex())));
            patViewer.optimumPosition();
            for (PatRectangle i : patViewer.getList().get(comboBox.getSelectedIndex())) {
                symProbeg = symProbeg + Math.abs(predRect.getX() - i.getX()) + Math.abs(predRect.getY() - i.getY());
                if ((predRect.getH() != i.getH()) || (predRect.getW() != i.getW())) {
                    kolIzmStor++;
                }
                if ((predRect.getA() != i.getA())) {
                    kolIzmYgl++;
                }
                predRect = i;
            }
            label2.setText("Cуммарный пробег по X/Y " + String.format("%.1f", (symProbeg / 1000000f)) + "   ");
            label3.setText("Изменение шторки " + Integer.toString(kolIzmStor) + "   ");
            label4.setText("Изменение угла поворота " + Integer.toString(kolIzmYgl) + "   ");
        });

        JButton btnOptim = new JButton("Оптимизировать файл");
        btnOptim.addActionListener(e -> {
            symProbeg = 0;
            kolIzmStor = 0;
            kolIzmYgl = 0;
            patViewer.setList(comboBox.getSelectedIndex(), OptimumPat(patViewer.getList().get(comboBox.getSelectedIndex())));
            patViewer.optimumPosition();
            for (PatRectangle i : patViewer.getList().get(comboBox.getSelectedIndex())) {
                symProbeg = symProbeg + Math.abs(predRect.getX() - i.getX()) + Math.abs(predRect.getY() - i.getY());
                if ((predRect.getH() != i.getH()) || (predRect.getW() != i.getW())) {
                    kolIzmStor++;
                }
                if ((predRect.getA() != i.getA())) {
                    kolIzmYgl++;
                }
                predRect = i;
            }
            label2.setText("Cуммарный пробег по X/Y " + String.format("%.1f", (symProbeg / 1000000f)) + "   ");
            label3.setText("Изменение шторки " + Integer.toString(kolIzmStor) + "   ");
            label4.setText("Изменение угла поворота " + Integer.toString(kolIzmYgl) + "   ");
        });

        menuPanel.add(btnOptim);

        if (file != null) {
            comboBox.addItem(file.getAbsolutePath());
        }
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createVerticalStrut(20));
        JPanel PanelPointer = new JPanel();
        JPanel PanelPointer1 = new JPanel();
        JButton btn1 = new JButton("◄");
        btn1.setToolTipText("Переместить изображение влево");
        btn1.addActionListener(e -> patViewer.incX());
        PanelPointer.add(btn1);
        //   buttonPanel.add(Box.createVerticalStrut(20));
        JButton btn4 = new JButton("►");
        btn4.setToolTipText("Переместить изображение вправо");
        btn4.addActionListener(e -> patViewer.decX());
        PanelPointer.add(btn4);
        // buttonPanel.add(Box.createVerticalStrut(20));
        JButton btn2 = new JButton("▲");
        btn2.setToolTipText("Переместить изображение вверх");
        btn2.addActionListener(e -> patViewer.incY());
        PanelPointer1.add(btn2);
        //  buttonPanel.add(Box.createVerticalStrut(20));
        JButton btn3 = new JButton("▼");
        btn3.setToolTipText("Переместить изображение вниз");
        btn3.addActionListener(e -> patViewer.decY());
        PanelPointer1.add(btn3);
        //  buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(btnOptimKv);
        //    buttonPanel.add(btnPovorot);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(PanelPointer);
        buttonPanel.add(PanelPointer1);
        JPanel PanelSize = new JPanel();
        JButton btn5 = new JButton("+");
        btn5.setToolTipText("Приблизить изображение");
        btn5.addActionListener(e -> patViewer.incCenter());
        PanelSize.add(btn5);
        //   buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnOpt = new JButton("⌂");
        btnOpt.setToolTipText("Установить оптимальную позицию чертежа");
        btnOpt.addActionListener(e -> patViewer.optimumPosition());
        PanelSize.add(btnOpt);
        //  buttonPanel.add(Box.createVerticalStrut(20));
        JButton btn6 = new JButton("-");
        btn6.setToolTipText("Отдалить изображение");
        btn6.addActionListener(e -> patViewer.decCenter());
        PanelSize.add(btn6);
        buttonPanel.add(PanelSize);
        // buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnTmer = new JButton("Выводить по одному");
        btnTmer.setToolTipText("Вывод изображения последовательно по одному прямоугольнику, с регулируемой скоростью");
        btnTmer.addActionListener(e -> {
            if (btnTmer.getText().equals("Выводить по одному")) {
                btnTmer.setText("Вывести всё");
                if (patViewer.getListSize() > 0) {
                    fpor = true;
                    timer.start();
                } else {
                    x.setValue(100);
                }
            } else {
                btnTmer.setText("Выводить по одному");
                timer.stop();
            }
            patViewer.oppositefOutput();
        });

        buttonPanel.add(btnTmer);
        buttonPanel.add(Box.createVerticalStrut(20));
        x = new JProgressBar(0);
        x.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                patViewer.setSizePercent((double) e.getX() / x.getWidth());
            }
        });
        buttonPanel.add(x);
        buttonPanel.add(Box.createVerticalStrut(20));
        JLabel label = new JLabel("Скорость " + Double.toString(1000d / timerSpeed) + " в секунду");
        //  JLabel label = new JLabel("");
        JPanel PanelPlay = new JPanel();
        JPanel PanelPlay1 = new JPanel();
        JButton btnFast = new JButton(">>");
        btnFast.setToolTipText("Ускорить вывод элементов");
        btnFast.addActionListener(e -> {
            String s = Float.toString(1000f / timerFast());
            int si = s.indexOf('.');
            if (s.length() - si > 1) {
                s = s.substring(0, si + 2);
            }
            label.setText("Скорость " + s + " в секунду");
        });

        //  buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnslow = new JButton("<<");
        btnslow.setToolTipText("Замедлить вывод элементов");
        btnslow.addActionListener(e -> {
            timerSlow();
            String s = Float.toString(1000f / timerSpeed);
            int si = s.indexOf('.');
            if (s.length() - si > 1) {
                s = s.substring(0, si + 2);
            }
            label.setText("Скорость " + s + " в секунду");
        });

        PanelPlay.add(btnslow);
        PanelPlay.add(btnFast);
        //   buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnStop = new JButton("Остановить вывод");
        btnStop.setToolTipText("Остановить вывод элементов");
        btnStop.addActionListener(e -> {
            if (btnStop.getText().equals("Остановить вывод")) {
                timerSpeed = 1000;
                label.setText("Скорость " + Float.toString(1000f / timerSpeed) + " в секунду");
                timer.stop();
                btnStop.setText("Возобновить вывод");
            } else {
                timer.setDelay(timerSpeed);
                timer.start();
                btnStop.setText("Остановить вывод");
            }
        });

        PanelPlay1.add(btnStop);
        //   buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnNaz = new JButton("Ɔ/с");
        btnNaz.setToolTipText("Вывод элементов задом наперед");
        btnNaz.addActionListener(e -> {
            if (fpor) {
                fpor = false;
                if (patViewer.incSizeVisible() == patViewer.getOneOutSize()) {
                    timer.start();
                }
                //         btnTmer.setText("Выводить в обычном порядке");
            } else {
                fpor = true;
                //         btnTmer.setText("Выводить в обратном порядке");
            }
        });

        PanelPlay1.add(btnNaz);
        // buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(label);
        buttonPanel.add(PanelPlay);
        buttonPanel.add(PanelPlay1);
        label1.setText("В выбранном файле " + Integer.toString(patViewer.getList().get(comboBox.getSelectedIndex()).size()) + " элементов");
        predRect = patViewer.getList().get(comboBox.getSelectedIndex()).get(0);
        for (PatRectangle i : patViewer.getList().get(comboBox.getSelectedIndex())) {
            symProbeg = symProbeg + Math.abs(predRect.getX() - i.getX()) + Math.abs(predRect.getY() - i.getY());
            if ((predRect.getH() != i.getH()) || (predRect.getW() != i.getW())) {
                kolIzmStor++;
            }
            if ((predRect.getA() != i.getA())) {
                kolIzmYgl++;
            }
            predRect = i;
        }
        label2.setText("Cуммарный пробег по X/Y " + String.format("%.1f", (symProbeg / 1000000f)) + "   ");
        //  label2.setText("Cуммарный пробег по X/Y " + Float.toString(symProbeg)+"   ");
        label3.setText("Изменение шторки " + Integer.toString(kolIzmStor) + "   ");
        label4.setText("Изменение угла поворота " + Integer.toString(kolIzmYgl) + "   ");
        buttonPanel.add(label1);
        buttonPanel.add(label2);
        buttonPanel.add(label3);
        buttonPanel.add(label4);
        //buttonPanel.add(label);
        panel.add(menuPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.EAST);
        f.setContentPane(panel);
        f.setVisible(true);

        Thread.sleep(100);
        SwingUtilities.invokeLater(() -> patViewer.optimumPosition());
    }

    static javax.swing.Timer timer = new javax.swing.Timer(timerSpeed, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (fpor) {
                x.setValue((int) (100f * (patViewer.incSizeVisible() / patViewer.getOneOutSize())));
            } else {
                x.setValue((int) (100f * (patViewer.decSizeVisible() / patViewer.getOneOutSize())));
            }
        }
    });

    public static int timerFast() {
        int x3 = timerSpeed;
        timerSpeed = (int) (timerSpeed * 0.9);
        if ((timerSpeed == x3) && (x3 > 1)) {
            timerSpeed--;
        }
        timer.setDelay(timerSpeed);
        return timerSpeed;
    }

    public static int timerSlow() {
        int x3 = timerSpeed;
        timerSpeed = (int) (timerSpeed * 1.1);
        if (timerSpeed == x3) {
            timerSpeed++;
        }
        timer.setDelay(timerSpeed);
        return timerSpeed;
    }

    public static List<PatRectangle> OptimumPat1(List<PatRectangle> l) {
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

    public static List<PatRectangle> OptimumPat(List<PatRectangle> l) {
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

    public static double lengthPat(PatRectangle x1, PatRectangle y1) {
        return ((ExelA * Math.abs(x1.getA() - y1.getA())) + (ExelHW * (Math.abs(x1.getW() - y1.getW()) + Math.abs(x1.getH() - y1.getH()))) + (ExelXY * (Math.abs(x1.getX() - y1.getX()) + Math.abs(x1.getY() - y1.getY()))));
    }

    public static void ReadXml() {
        try {
            File fXml = new File("config.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(fXml);

            doc.getDocumentElement().normalize();

            NodeList nodeLst = doc.getElementsByTagName("point");
            Node fstNode = nodeLst.item(0);
            if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                String scoo;
                Element elj = (Element) fstNode;
                scoo = elj.getAttribute("A");
                ExelA = Integer.parseInt(scoo);
                scoo = elj.getAttribute("HW");
                ExelHW = Integer.parseInt(scoo);
                scoo = elj.getAttribute("XY");
                ExelXY = Integer.parseInt(scoo);
                patViewer.setSaveImag(elj.getAttribute("SaveImage"));
                try {
                    timerSpeed = Integer.parseInt(elj.getAttribute("SpeedPrint"));
                } catch (Exception e2) {
                    timerSpeed = 250;
                }
                try {
                    workCatalog = elj.getAttribute("WorkCatalog");
                } catch (Exception e2) {
                    workCatalog = System.getProperty("user.dir");
                }
                try {
                    saveCatalog = elj.getAttribute("SaveCatalog");
                } catch (Exception e2) {
                    saveCatalog = System.getProperty("user.dir");
                }
                scoo = elj.getAttribute("FontSize");
                FontSize = Integer.parseInt(scoo);
                scoo = elj.getAttribute("FontX");
                FontX = Integer.parseInt(scoo);
                scoo = elj.getAttribute("FontY");
                FontY = Integer.parseInt(scoo);
                patViewer.setFontSize(FontSize);
                patViewer.setFontX(FontX);
                patViewer.setFontY(FontY);
                patViewer.setPrinter(elj.getAttribute("Printer"));
                scoo = elj.getAttribute("WriteName");
                if (scoo.equals("+")) {
                    patViewer.setWriteName(true);
                }
                scoo = elj.getAttribute("WriteData");
                if (scoo.equals("+")) {
                    patViewer.setWriteData(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при открытии Xml файла с настройками, применены стандартные");
        }
    }
}