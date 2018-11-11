package com.egs.patViewer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class Main {

    private static int timerSpeed;

    private static PatViewer patViewer;
    private static JComboBox<String> comboBox;

    private static JLabel label1 = new JLabel("");
    private static JLabel label2 = new JLabel("");
    private static JLabel label3 = new JLabel("");
    private static JLabel label4 = new JLabel("");

    private static JProgressBar x;
    private static boolean fpor = true;

    private static Configuration cfg;

    private static JFileChooser openFileChooser;
    private static JFileChooser saveFileChooser;

    private static File openFile() {
        if (openFileChooser == null) {
            openFileChooser = new JFileChooser(cfg.getWorkCatalog());
            openFileChooser.setSize(1700, 1600);
            openFileChooser.setFileFilter(new PatFileFilter());
        }

        int ret = openFileChooser.showDialog(null, "Open File");
        if (ret == JFileChooser.APPROVE_OPTION) {
            return openFileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    private static JMenuBar createMenu(PatViewer patViewer, JComboBox<String> comboBox) {
        JMenuItem openFile = new JMenuItem("Open file", KeyEvent.VK_O);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
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
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        saveFile.addActionListener(e -> {
            if (saveFileChooser == null) {
                saveFileChooser = new JFileChooser(cfg.getSaveCatalog());
                saveFileChooser.addChoosableFileFilter(new PatFileFilter());
                saveFileChooser.setAcceptAllFileFilterUsed(false);
            }
            int ret = saveFileChooser.showDialog(null, "Save File");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file1 = saveFileChooser.getSelectedFile();

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
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
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

        JMenuItem fileName = new JCheckBoxMenuItem("Show file name");
        fileName.setMnemonic(KeyEvent.VK_N);
        fileName.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        fileName.setSelected(patViewer.isFfill());
        fileName.addChangeListener(e -> {
            patViewer.setNameFile(Utils.extractFileName(comboBox.getItemAt(comboBox.getSelectedIndex())));
            patViewer.lableFlag();
        });

        JMenuItem up = new JMenuItem("Up ▲");
        up.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        up.addActionListener(e -> patViewer.incY());
        JMenuItem down = new JMenuItem("Down ▼");
        down.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        down.addActionListener(e -> patViewer.decY());
        JMenuItem right = new JMenuItem("Right ►");
        right.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        right.addActionListener(e -> patViewer.decX());
        JMenuItem left = new JMenuItem("Left ◄");
        left.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        left.addActionListener(e -> patViewer.incX());

        JMenuItem center = new JMenuItem("Optimal zoom and position");
        center.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.CTRL_MASK));
        center.addActionListener(e -> patViewer.optimumPosition());
        JMenuItem zoomIn = new JMenuItem("Zoom in");
        zoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0));
        zoomIn.addActionListener(e -> patViewer.incCenter());
        JMenuItem zoomOut = new JMenuItem("Zoom out");
        zoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0));
        zoomOut.addActionListener(e -> patViewer.decCenter());

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        viewMenu.add(invertX);
        viewMenu.add(invertY);
        viewMenu.add(fill);
        
        viewMenu.addSeparator();
        viewMenu.add(fileName);
        viewMenu.addSeparator();

        viewMenu.add(up);
        viewMenu.add(down);
        viewMenu.add(right);
        viewMenu.add(left);

        viewMenu.addSeparator();

        viewMenu.add(center);
        viewMenu.add(zoomIn);
        viewMenu.add(zoomOut);

        JMenu optimization = new JMenu("Optimization");
        optimization.setMnemonic(KeyEvent.VK_O);

        JMenuItem optimize = new JMenuItem("Оптимизировать файл");
        optimize.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
        optimize.addActionListener(e -> optimize(comboBox.getSelectedIndex(), new Optimization(cfg)));

        JMenuItem optimizeBlocks = new JMenuItem("Оптимизировать файл с блоками");
        optimizeBlocks.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK + InputEvent.ALT_MASK));
        optimizeBlocks.addActionListener(e -> optimize(comboBox.getSelectedIndex(), new OptimizationBlocks(cfg)));

        optimization.add(optimize);
        optimization.add(optimizeBlocks);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(optimization);
        return menuBar;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        cfg = Configuration.read();

        timerSpeed = cfg.getTimerSpeed();
        patViewer = new PatViewer();
        patViewer.setSaveImag(cfg.getSaveImag());
        patViewer.setFontSize(cfg.getFontSize());
        patViewer.setFontX(cfg.getFontX());
        patViewer.setFontY(cfg.getFontY());
        patViewer.setPrinter(cfg.getPrinter());
        patViewer.setWriteName(cfg.isWriteName());
        patViewer.setWriteData(cfg.isWriteData());

        comboBox = new JComboBox<>();

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
            refreshStat();
        });

        buttonPanel.add(Box.createVerticalStrut(20));

        menuPanel.add(comboBox);

        if (file != null) {
            comboBox.addItem(file.getAbsolutePath());
        }
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createVerticalStrut(20));

        //    buttonPanel.add(btnPovorot);
        buttonPanel.add(Box.createVerticalStrut(20));
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

        refreshStat();

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

    private static void refreshStat() {
        if (comboBox.getItemCount() ==0) {
            label1.setText("");
            label2.setText("");
            label3.setText("");
            label4.setText("");
        }
        else {
            List<PatRectangle> pat = patViewer.getList().get(comboBox.getSelectedIndex());

            label1.setText("В выбранном файле " + pat.size() + " элементов   ");

            Statistic statistic = Statistic.calculate(pat);

            label2.setText("Cуммарный пробег по X/Y " + String.format("%.1f", (statistic.getSymProbeg() / 1000000d)));
            label3.setText("Изменение шторки " + statistic.getKolIzmStor());
            label4.setText("Изменение угла поворота " + statistic.getKolIzmYgl());
        }
    }

    private static String colorNum(long num) {
        if (num == 0)
            return "0";

        String color = num < 0 ? "#f00" : "#0f0";
        return "<span style='color: " + color + "'>" + num + "</span>";
    }

    private static void optimize(int index, AbstractOptimization optimizator) {
        List<PatRectangle> pat = patViewer.getList().get(index);

        Statistic oldStat = Statistic.calculate(pat);

        long startTime = System.currentTimeMillis();

        pat = optimizator.apply(new ArrayList<>(pat));

        long time = System.currentTimeMillis() - startTime;

        Statistic newStat = Statistic.calculate(pat);

        //language=HTML
        String msg = "" +
                "<html>" +
                "<body>" +
                "Пробег: %.1f -> <strong>%.1f</strong> (%.1f)<br>" +
                "Изменение шторки: %d -> <strong>%d</strong> (%d)<br>" +
                "Изменение угла: %d -> <strong>%d</strong> (%d)<br>" +
                "<br>" +
                "Элементов: %d<br>" +
                "Время оптимизации: %.1fсек<br>" +
                "</body>" +
                "</html>";

        int res = JOptionPane.showConfirmDialog(null, String.format(msg,
                oldStat.getSymProbeg() / 1000000d, newStat.getSymProbeg() / 1000000d, (newStat.getSymProbeg() - oldStat.getSymProbeg()) / 1000000d,
                oldStat.getKolIzmStor(), newStat.getKolIzmStor(), newStat.getKolIzmStor() - oldStat.getKolIzmStor(),
                oldStat.getKolIzmYgl(), newStat.getKolIzmYgl(), newStat.getKolIzmYgl() - oldStat.getKolIzmYgl(),
                pat.size(), time / 1000f),
                "Optimization done", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == 0) {
            patViewer.setList(index, pat);
            patViewer.optimumPosition();
            refreshStat();
        }
    }

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

}