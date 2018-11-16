package com.egs.patViewer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Main {

    private static int timerSpeed;

    private static PatViewer patViewer;
    private static JComboBox<String> comboBox;

    private static JLabel statLabel = new JLabel("");
    private static JLabel selectedLabel = new JLabel("");

    private static JProgressBar progress;
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
                    PatWriter.save(file1, patViewer.allRect().collect(Collectors.toList()));
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

        JMenuItem byIndex = new JMenuItem("Show rectangle by index...");
        byIndex.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
        byIndex.addActionListener(e -> showRectangleByIndex());

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

        viewMenu.add(byIndex);
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

        JMenuItem showInvalid = new JMenuItem("Показать невалидные элементы");
        showInvalid.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
        showInvalid.addActionListener(e -> showInvalid());

        optimization.add(optimize);
        optimization.add(optimizeBlocks);
        optimization.add(showInvalid);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(optimization);
        return menuBar;
    }

    private static void showRectangleByIndex() {
        int selectedIndex = comboBox.getSelectedIndex();
        PatFile file = patViewer.getFiles().get(selectedIndex);

        String res = JOptionPane.showInputDialog(null, "Input rectangle index [1.." + (file.getList().size()) + "]\n(" + file.getName() + ')',
                "Show rectangle by index", JOptionPane.PLAIN_MESSAGE);
        if (res == null)
            return;

        res = res.trim();
        if (res.isEmpty())
            return;

        int idx;
        try {
            idx = Integer.parseInt(res);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Input string is not a number: " + res, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idx < 1 || idx > file.getList().size()) {
            JOptionPane.showMessageDialog(null, "Incorrect index, file " + file.getName() + " contains " + file.getList().size()
                    + " lines, so number must be in [1.." + (file.getList().size()) + ']', "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        patViewer.zoomToRectangle(file.getList().get(idx - 1));
    }

    private static void showInvalid() {
        Statistic statistic = Statistic.calculate(patViewer.allRect());

        int limit;

        if (statistic.getMaxX() > 100_000 || statistic.getMaxY() > 100_000)
            limit = 4;
        else
            limit = 16;

        StringBuilder res = new StringBuilder();

        for (PatFile file : patViewer.getFiles()) {
            boolean first = true;

            List<PatRectangle> list = file.getList();

            for (int i = 0; i < list.size(); i++) {
                PatRectangle rect = list.get(i);

                if (rect.getW() < limit || rect.getH() < limit) {
                    if (first) {
                        res.append(file.getName()).append('\n');
                        first = false;
                    }

                    int pos = res.length();
                    res.append(i + 1).append(':');
                    while (res.length() < pos + 7) res.append(' ');

                    res.append('X').append(rect.getX()).append('Y').append(rect.getY())
                            .append('H').append(rect.getH()).append('W').append(rect.getW()).append('A').append(rect.getA())
                            .append('\n');
                }
            }

            if (!first)
                res.append('\n');
        }

        if (res.length() == 0) {
            JOptionPane.showMessageDialog(null, "No invalid rectangle found", "No error", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JDialog dialog = new JDialog();
            dialog.setModal(false);

            JTextArea textArea = new JTextArea(res.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            JPanel panel = new JPanel(new BorderLayout(5, 1));
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panel.add(new JLabel("Invalid rectangles"), BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

            dialog.setContentPane(panel);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            dialog.setMinimumSize(new Dimension(400, 600));
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        cfg = Configuration.read();

        timerSpeed = cfg.getTimerSpeed();
        timer.setDelay(timerSpeed);

        patViewer = new PatViewer();

        patViewer.setShowNonVisible(cfg.isShowNonVisible());
        if (cfg.getNonVisibleColor() != null) {
            patViewer.setNonVisibleColor(new Color(cfg.getNonVisibleColor()));
        }

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

        JPanel menuPanel = new JPanel();

        comboBox.addActionListener(e -> {
            refreshStat();
        });

        menuPanel.add(comboBox);

        if (file != null) {
            comboBox.addItem(file.getAbsolutePath());
        }
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));

        //    buttonPanel.add(btnPovorot);
        // buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnTmer = new JButton("Выводить по одному");
        btnTmer.setToolTipText("Вывод изображения последовательно по одному прямоугольнику, с регулируемой скоростью");
        btnTmer.addActionListener(e -> {
            if (btnTmer.getText().equals("Выводить по одному")) {
                btnTmer.setText("Вывести всё");
                if (patViewer.getOpenFileCount() > 0) {
                    fpor = true;
                    timer.start();
                } else {
                    progress.setValue(100);
                }
            } else {
                btnTmer.setText("Выводить по одному");
                timer.stop();
            }
            patViewer.oppositefOutput();
        });

        progress = new JProgressBar(0);
        progress.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                patViewer.setSizePercent((double) e.getX() / progress.getWidth());
            }
        });
        JLabel label = new JLabel("Скорость " + Double.toString(1000d / timerSpeed) + " в секунду");
        //  JLabel label = new JLabel("");

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

        JPanel panelPlay = new JPanel();
        panelPlay.add(btnslow);
        panelPlay.add(btnFast);
        panelPlay.setMaximumSize(new Dimension(1000, panelPlay.getPreferredSize().height));

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

        JPanel panelPlay1 = new JPanel();
        panelPlay1.add(btnStop);
        panelPlay1.add(btnNaz);
        panelPlay1.setMaximumSize(new Dimension(1000, panelPlay1.getPreferredSize().height));
        // buttonPanel.add(Box.createVerticalStrut(20));


        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 3, 20, 10));

        buttonPanel.add(btnTmer);
        buttonPanel.add(Box.createVerticalStrut(20));

        buttonPanel.add(progress);

        buttonPanel.add(Box.createVerticalStrut(20));


        buttonPanel.add(label);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(panelPlay);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(panelPlay1);
        buttonPanel.add(Box.createVerticalStrut(20));

        patViewer.addSelectionListener(rect -> {
            if (rect == null) {
                selectedLabel.setText("");
            }
            else {
                String text = String.format("<html><body><pre>" +
                        "X: %6d<br>" +
                        "Y: %6d<br>" +
                        "W: %6d<br>" +
                        "H: %6d<br>" +
                        "A: %d<br>" +
                        "</pre></body></html>", rect.getX(), rect.getY(), rect.getW(), rect.getH(), rect.getA());

                selectedLabel.setText(text);
            }
        });

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(selectedLabel);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(statLabel);

        refreshStat();

        panel.add(menuPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.EAST);
        f.setContentPane(panel);
        f.setVisible(true);

        Thread.sleep(100);
        SwingUtilities.invokeLater(() -> patViewer.optimumPosition());
    }

    static javax.swing.Timer timer = new javax.swing.Timer(0, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (fpor) {
                progress.setValue((int) (100f * (patViewer.incSizeVisible() / patViewer.getOneOutSize())));
            } else {
                progress.setValue((int) (100f * (patViewer.decSizeVisible() / patViewer.getOneOutSize())));
            }
        }
    });

    private static void refreshStat() {
        if (comboBox.getItemCount() ==0) {
            statLabel.setText("");
        }
        else {
            Statistic statistic = Statistic.calculate(patViewer.allRect());

            //language=HTML
            String msg = "" +
                    "<html><body style='white-space: pre'>" +
                    "Количество элементов: %d<br><br>" +
                    "Пробег по X/Y: %.1f<br>" +
                    "Изменение шторки: %d<br>" +
                    "Изменение угла: %d<br>" +
                    "Плохие элементы: %s" +
                    "</body></html>";

            String text = String.format(msg,
                    statistic.getCount(), statistic.getSymProbeg() / 1000000d, statistic.getKolIzmStor(), statistic.getKolIzmYgl(),
                    statistic.getInvalidCount() == 0 ? "0" : "<span style='color: #cc0000'>" + statistic.getInvalidCount() + "</span>"
                    );

            statLabel.setText(text);
        }
    }

//    private static String colorNum(long num) {
//        if (num == 0)
//            return "0";
//
//        String color = num < 0 ? "#f00" : "#0f0";
//        return "<span style='color: " + color + "'>" + num + "</span>";
//    }

    private static void optimize(int index, AbstractOptimization optimizator) {
        PatFile patFile = patViewer.getFiles().get(index);

        Statistic oldStat = Statistic.calculate(patFile.getList());

        long startTime = System.currentTimeMillis();

        List<PatRectangle> pat = optimizator.apply(new ArrayList<>(patFile.getList()));

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
            patFile.getList().clear();
            patFile.getList().addAll(pat);

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