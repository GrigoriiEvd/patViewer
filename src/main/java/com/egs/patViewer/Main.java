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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    private static int timerSpeed;
    private static String workCatalog;
    private static String saveCatalog;
    private static PatViever patViever;
    private static JProgressBar x;
    private static boolean fpor = true;
    private static int ExelA = 10000;
    private static int ExelHW = 100;
    private static int ExelXY = 1;
    private static boolean fCombo = false;
    public static void main(String[] args) throws IOException {
        String fileName = "";
        JLabel label1 = new JLabel("");
        JLabel label2 = new JLabel("");
        JLabel label3 = new JLabel("");
        JLabel label4 = new JLabel("");
        patViever = new PatViever();
        ReadXml();

        if (args.length > 0) {
            fileName = args[0];
        }
        PatParser patParser = new PatParser();
        List<PatRectangle> list = new ArrayList<>();
        try {
            if (!fileName.equals("")) {
                if (fileName.endsWith(".xls")) {
                    list = ExelParser.parser(fileName);
                    patViever.newList(list);
                } else {
                    list = patParser.parser(fileName);
                    patViever.newList(list);
                }
            } else {
                File file = null;
                JFileChooser fileopen = new JFileChooser(workCatalog);
                fileopen.setSize(1700, 1600);
                JDialog jd = new JDialog();
                jd.setBounds(0, 0, 100, 100);
                int ret = fileopen.showDialog(jd, "Open File");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    file = fileopen.getSelectedFile();
                    fileName = file.getCanonicalPath();
                    if (fileName.endsWith(".xls")) {
                        list = ExelParser.parser(fileName);
                        patViever.newList(list);
                    } else {
                        list = patParser.parser(fileName);
                        patViever.newList(list);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ошибка при открытии файла");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JFrame f = new JFrame();
        f.setTitle("Drawing Graphics in Frames");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel viewerPanel = new JPanel(new BorderLayout());
        viewerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        viewerPanel.add(patViever, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(viewerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JPanel menuPanel = new JPanel();
        BoxLayout buttonPaneLayout = new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS);
        buttonPanel.setLayout(buttonPaneLayout);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label1.setText("В выбранном файле " + Integer.toString(patViever.getList().get(comboBox.getSelectedIndex()).size()) + " элементов   ");
                int symProbeg=0;
                int kolIzmStor=0;
                int kolIzmYgl=0;
                PatRectangle predRect=patViever.getList().get(comboBox.getSelectedIndex()).get(0);
                for (PatRectangle i: patViever.getList().get(comboBox.getSelectedIndex())){
                    symProbeg=symProbeg+Math.abs(predRect.getX()-i.getX())+Math.abs(predRect.getY()-i.getY());
                    if ((predRect.getH()!=i.getH())||(predRect.getW()!=i.getW())){
                        kolIzmStor++;
                    }
                    if ((predRect.getA()!=i.getA())){
                        kolIzmYgl++;
                    }
                    predRect=i;
                }
                label2.setText("Cуммарный пробег по X/Y " + Integer.toString(symProbeg)+"   ");
                label3.setText("Количество событий изменения шторки " + Integer.toString(kolIzmStor)+"   ");
                label4.setText("Количество событий изменения угла поворота " + Integer.toString(kolIzmYgl)+"   ");
            }
        }
        );
        JButton btnClear = new JButton("Очистить");
        btnClear.addActionListener(new ActionListener() {
                                       @Override
                                       public void actionPerformed(ActionEvent e) {
                                           patViever.clearList();
                                           comboBox.removeAllItems();
                                       }
                                   }
        );
        buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnOpen = new JButton("Открыть и вывести новый файл");
        btnOpen.addActionListener(new ActionListener() {
                                      @Override
                                      public void actionPerformed(ActionEvent e) {
                                          File file1 = null;
                                          JFileChooser fileopen = new JFileChooser(workCatalog);
                                          int ret = fileopen.showDialog(null, "Open File");
                                          if (ret == JFileChooser.APPROVE_OPTION) {
                                              file1 = fileopen.getSelectedFile();
                                              List<PatRectangle> list1;
                                              try {
                                                  String fileName = file1.getCanonicalPath();
                                                  if (fileName.substring(fileName.length() - 3, fileName.length()).equals("xls")) {
                                                      list1 = ExelParser.parser(fileName);
                                                      patViever.newList(list1);
                                                      comboBox.addItem(fileName);
                                                  } else {
                                                      list1 = patParser.parser(fileName);
                                                      patViever.newList(list1);
                                                      comboBox.addItem(fileName);
                                                  }
                                              } catch (Exception e1) {
                                                  JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                              }
                                          } else {
                                              JOptionPane.showMessageDialog(null, "Ошибка при открытии файла");
                                          }
                                      }
                                  }

        );
        menuPanel.add(btnOpen);
        JButton btnPrint = new JButton("Напечатать на принтере");
        btnPrint.addActionListener(new ActionListener() {
                                       @Override
                                       public void actionPerformed(ActionEvent e) {
                                           patViever.printing();
                                       }
                                   }

        );
        menuPanel.add(btnPrint);
        menuPanel.add(comboBox);
        JButton btnOptim = new JButton("Оптимизировать файл");
        btnOptim.addActionListener(new ActionListener() {
                                       @Override
                                       public void actionPerformed(ActionEvent e) {
                                           patViever.setList(comboBox.getSelectedIndex(), OptimumPat(patViever.getList().get(comboBox.getSelectedIndex())));
                                       }
                                   }

        );
        menuPanel.add(btnOptim);
        JButton btn0 = new JButton("Сохранить");
        btn0.addActionListener(new ActionListener() {
                                   @Override
                                   public void actionPerformed(ActionEvent e) {
                                       File file1 = null;
                                       JFileChooser fileopen = new JFileChooser(saveCatalog);
                                       int ret = fileopen.showDialog(null, "Save File");
                                       if (ret == JFileChooser.APPROVE_OPTION) {
                                           file1 = fileopen.getSelectedFile();
                                           try {
                                               String fileNameWrite = file1.getCanonicalPath();
                                               if (fileNameWrite.length() > 4) {
                                                   if (fileNameWrite.charAt(fileNameWrite.length()-4)!='.') {
                                                       fileNameWrite = fileNameWrite + ".pat";
                                                   }
                                               } else {
                                                   fileNameWrite = fileNameWrite + ".pat";
                                               }
                                               BufferedWriter bw = new BufferedWriter(new FileWriter(fileNameWrite));
                                               PatRectangle rectangle = null;
                                               for (List<PatRectangle> i : patViever.getList()) {
                                                   for (PatRectangle j : i) {
                                                       String s = "";
                                                       if (rectangle != null) {
                                                           if (rectangle.getX() != j.getX()) {
                                                               s = s + "X" + Integer.toString(j.getX());
                                                           }
                                                           if (rectangle.getY() != j.getY()) {
                                                               s = s + "Y" + Integer.toString(j.getY());
                                                           }
                                                           if (rectangle.getH() != j.getH()) {
                                                               s = s + "H" + Integer.toString(j.getH());
                                                           }
                                                           if (rectangle.getW() != j.getW()) {
                                                               s = s + "W" + Integer.toString(j.getW());
                                                           }
                                                           if (rectangle.getA() != j.getA()) {
                                                               s = s + "A" + Integer.toString(j.getA());
                                                           }
                                                           bw.write(s + ";");
                                                       } else {
                                                           bw.write("X" + Integer.toString(j.getX()) + "Y" + Integer.toString(j.getY()) + "H" + Integer.toString(j.getH()) + "W" + Integer.toString(j.getW()) + "A" + Integer.toString(j.getA()) + ";");
                                                       }
                                                       bw.newLine();
                                                       rectangle = j;
                                                   }
                                               }
                                               bw.write("$;");
                                               bw.close();
                                           } catch (IOException e1) {
                                               JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                           }
                                       }
                                   }
                               }

        );
        menuPanel.add(btn0);
        if (!fileName.equals("")) {
            comboBox.addItem(fileName);
        }
        menuPanel.add(btnClear);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createVerticalStrut(20));
        JPanel PanelPointer = new JPanel();
        JButton btn1 = new JButton("◄");
        btn1.setToolTipText("Переместить изображение влево");
        btn1.addActionListener(new ActionListener() {
                                   @Override
                                   public void actionPerformed(ActionEvent e) {
                                       patViever.incX();
                                   }
                               }

        );
        PanelPointer.add(btn1);
     //   buttonPanel.add(Box.createVerticalStrut(20));
        JButton btn4 = new JButton("►");
        btn4.setToolTipText("Переместить изображение вправо");
        btn4.addActionListener(new ActionListener() {
                                   @Override
                                   public void actionPerformed(ActionEvent e) {
                                       patViever.decX();
                                   }
                               }
        );
        PanelPointer.add(btn4);
       // buttonPanel.add(Box.createVerticalStrut(20));
        JButton btn2 = new JButton("▲");
        btn2.setToolTipText("Переместить изображение вверх");
        btn2.addActionListener(new ActionListener() {
                                   @Override
                                   public void actionPerformed(ActionEvent e) {
                                       patViever.incY();
                                   }
                               }
        );
        PanelPointer.add(btn2);
      //  buttonPanel.add(Box.createVerticalStrut(20));
        JButton btn3 = new JButton("▼");
        btn3.setToolTipText("Переместить изображение вниз");
        btn3.addActionListener(new ActionListener() {
                                           @Override
                                           public void actionPerformed(ActionEvent e) {
                                               patViever.decY();
                                           }
                                       }
        );
        PanelPointer.add(btn3);
      //  buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(PanelPointer);

        JPanel PanelSize= new JPanel();
        JButton btn5 = new JButton("+");
        btn5.setToolTipText("Приблизить изображение");
        btn5.addActionListener(new ActionListener() {
                                   @Override
                                   public void actionPerformed(ActionEvent e) {
                                       patViever.incF();
                                   }
                               }
        );
        PanelSize.add(btn5);
        //   buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnOpt = new JButton("⌂");
        btnOpt.setToolTipText("Установить оптимальную позицию чертежа");
        btnOpt.addActionListener(new ActionListener() {
                                     @Override
                                     public void actionPerformed(ActionEvent e) {
                                         patViever.optimumPosition();
                                     }
                                 }

        );
        PanelSize.add(btnOpt);
        //  buttonPanel.add(Box.createVerticalStrut(20));
        JButton btn6 = new JButton("-");
        btn6.setToolTipText("Отдалить изображение");
        btn6.addActionListener(new ActionListener() {
                                   @Override
                                   public void actionPerformed(ActionEvent e) {
                                       patViever.decF();
                                   }
                               }

        );
        PanelSize.add(btn6);
        buttonPanel.add(PanelSize);
       // buttonPanel.add(Box.createVerticalStrut(20));
        JCheckBox checkBox1 = new JCheckBox("Инвертировать по оси X", true);
        checkBox1.addItemListener(new ItemListener() {
                                      public void itemStateChanged(ItemEvent e) {
                                          patViever.setReverseX(checkBox1.isSelected());
                                      }
                                  }

        );
        buttonPanel.add(checkBox1);
        buttonPanel.add(Box.createVerticalStrut(20));
        JCheckBox checkBox2 = new JCheckBox("Инвертировать по оси Y", true);
        checkBox2.addItemListener(new ItemListener() {
                                      public void itemStateChanged(ItemEvent e) {
                                          patViever.setReverseY(checkBox2.isSelected());
                                      }
                                  }

        );
        buttonPanel.add(checkBox2);
        buttonPanel.add(Box.createVerticalStrut(20));
        JCheckBox checkBox3 = new JCheckBox("Закраска", false);
        checkBox3.addItemListener(new ItemListener() {
                                      public void itemStateChanged(ItemEvent e) {
                                          patViever.setFfill(checkBox3.isSelected());
                                      }
                                  }

        );
        buttonPanel.add(checkBox3);
        buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnTmer = new JButton("Выводить по одному");
        btnTmer.setToolTipText("Вывод изображения последовательно по одному прямоугольнику, с регулируемой скоростью");
        btnTmer.addActionListener(new ActionListener() {
                                      @Override
                                      public void actionPerformed(ActionEvent e) {
                                          if (btnTmer.getText().equals("Выводить по одному")) {
                                              btnTmer.setText("Вывести всё");
                                              if (patViever.getListSize() > 0) {
                                                  timer.start();
                                              } else {
                                                  x.setValue(100);
                                              }
                                          } else {
                                              btnTmer.setText("Выводить по одному");
                                              timer.stop();
                                          }
                                          patViever.oppositefOutput();
                                      }
                                  }

        );
        buttonPanel.add(btnTmer);
        buttonPanel.add(Box.createVerticalStrut(20));
        x = new JProgressBar(0);
        x.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                patViever.setSizeProcent(Double.valueOf(e.getX())/Double.valueOf(x.getWidth()));
            }
        });
        buttonPanel.add(x);
        buttonPanel.add(Box.createVerticalStrut(20));
        JLabel label = new JLabel("Скорость "+Double.toString(Double.valueOf(1000)/Double.valueOf(timerSpeed))+" в секунду");
        JPanel PanelPlay= new JPanel();
        JButton btnFast = new JButton(">>");
        btnFast.setToolTipText("Ускорить вывод элементов");
        btnFast.addActionListener(new ActionListener() {
                                      @Override
                                      public void actionPerformed(ActionEvent e) {
                                          label.setText("Скорость " + Float.toString(1000f / timerFast()) + " в секунду");
                                      }
                                  }

        );
      //  buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnslow = new JButton("<<");
        btnslow.setToolTipText("Замедлить вывод элементов");
        btnslow.addActionListener(new ActionListener() {
                                      @Override
                                      public void actionPerformed(ActionEvent e) {
                                          timerSlow();
                                          label.setText("Скорость " + Float.toString(1000f / timerSpeed) + " в секунду");
                                      }
                                  }

        );
        PanelPlay.add(btnslow);
        PanelPlay.add(btnFast);
     //   buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnStop = new JButton("||");
        btnStop.setToolTipText("Остановить вывод элементов");
        btnStop.addActionListener(new ActionListener() {
                                      @Override
                                      public void actionPerformed(ActionEvent e) {
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
                                      }
                                  }

        );
        PanelPlay.add(btnStop);
     //   buttonPanel.add(Box.createVerticalStrut(20));
        JButton btnNaz = new JButton("Ɔ/с");
        btnNaz.setToolTipText("Вывод элементов задом наперед");
        btnNaz.addActionListener(new ActionListener() {
                                     @Override
                                     public void actionPerformed(ActionEvent e) {
                                         if (fpor) {
                                             fpor = false;
                                             btnTmer.setText("Выводить в обычном порядке");
                                         } else {
                                             fpor = true;
                                             btnTmer.setText("Выводить в обратном порядке");
                                         }
                                     }
                                 }

        );
        PanelPlay.add(btnNaz);
       // buttonPanel.add(Box.createVerticalStrut(20));
        PanelPlay.add(label);
        buttonPanel.add(PanelPlay);
        label1.setText("В выбранном файле " + Integer.toString(patViever.getList().get(comboBox.getSelectedIndex()).size()) + " элементов   ");
        int symProbeg=0;
        int kolIzmStor=0;
        int kolIzmYgl=0;
        PatRectangle predRect=patViever.getList().get(comboBox.getSelectedIndex()).get(0);
        for (PatRectangle i: patViever.getList().get(comboBox.getSelectedIndex())){
            symProbeg=symProbeg+Math.abs(predRect.getX()-i.getX())+Math.abs(predRect.getY()-i.getY());
            if ((predRect.getH()!=i.getH())||(predRect.getW()!=i.getW())){
                kolIzmStor++;
            }
            if ((predRect.getA()!=i.getA())){
                kolIzmYgl++;
            }
            predRect=i;
        }
        label2.setText("Cуммарный пробег по X/Y " + Integer.toString(symProbeg)+"   ");
        label3.setText("Количество событий изменения шторки " + Integer.toString(kolIzmStor)+"   ");
        label4.setText("Количество событий изменения угла поворота " + Integer.toString(kolIzmYgl)+"   ");
        buttonPanel.add(label1);
        buttonPanel.add(label2);
        buttonPanel.add(label3);
        buttonPanel.add(label4);
        //buttonPanel.add(label);
        panel.add(menuPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.EAST);
        f.setContentPane(panel);
        f.setVisible(true);
        timer1.start();
    }

    static javax.swing.Timer timer = new javax.swing.Timer(timerSpeed, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (fpor) {
                x.setValue((int) (100f * (patViever.incSizeVisible() / patViever.getOneOutSize())));
            } else {
                x.setValue((int) (100f * (patViever.decSizeVisible() / patViever.getOneOutSize())));
            }
        }
    });

    static javax.swing.Timer timer1 = new javax.swing.Timer(timerSpeed, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            patViever.optimumPosition();
            timer1.stop();
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
                patViever.setSaveImag(elj.getAttribute("SaveImage"));
                try {
                    timerSpeed=Integer.parseInt( elj.getAttribute("SpeedPrint"));
                }catch (Exception e2){
                    timerSpeed=250;
                }
                try {
                    workCatalog=elj.getAttribute("WorkCatalog");
                }catch (Exception e2){
                    workCatalog=System.getProperty("user.dir");
                }
                try {
                    saveCatalog=elj.getAttribute("SaveCatalog");
                }catch (Exception e2){
                    saveCatalog=System.getProperty("user.dir");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при открытии Xml файла с настройками, применены стандартные");
        }
    }
}