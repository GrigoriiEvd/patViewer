package com.egs.patViewer;

import org.apache.poi.hssf.util.HSSFColor;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    private static int timerSpeed=1000;
    private static PatViever patViever;
    private static JProgressBar x;
    public static void main(String[] args) throws IOException {
        String fileName="";
        if (args.length>0){
            fileName=args[0];
        }
        PatParser patParser = new PatParser();
        List<PatRectangle> list = new ArrayList<>();
        try {
            if (!fileName.equals("")) {
                if (fileName.substring(fileName.length()-3, fileName.length()).equals("xls")){
                    fileName=ExelParser.parser(fileName);
                }
                list = patParser.parser(fileName);
            }else{
                File file = null;
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Open File");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    file = fileopen.getSelectedFile();
                }
                fileName=file.getCanonicalPath();
                if (fileName.substring(fileName.length()-3, fileName.length()).equals("xls")){
                    fileName=ExelParser.parser(fileName);
                }
                list = patParser.parser(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame f = new JFrame();
        f.setTitle("Drawing Graphics in Frames");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        patViever = new PatViever();
        int maxX=0;
        int maxY=0;
        int minX=10000000;
        int minY=10000000;
        for(PatRectangle i: list){
            if (maxX<(i.getX()+i.getW()/2)){
                maxX=i.getX()+i.getW()/2;
            }
            if (maxY<(i.getY()+i.getH()/2)){
                maxY=i.getY()+i.getH()/2;
            }
            if (minX>(i.getX()+i.getW()/2)){
                minX=i.getX()+i.getW()/2;
            }
            if (minY>(i.getY()+i.getH()/2)){
                minY=i.getY()+i.getH()/2;
            }
        }
        patViever.setMaxX(maxX);
        patViever.setMaxY(maxY);
        patViever.setMinX(minX);
        patViever.setMinY(minY);
        patViever.setList(list);
        JPanel viewerPanel = new JPanel(new BorderLayout());
        viewerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        viewerPanel.add(patViever, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(viewerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton btnClear = new JButton("Очистить");
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patViever.clearList();
            }
        });
        buttonPanel.add(btnClear);

        JButton btnOpen = new JButton("Открыть и вывести новый файл");
        btnOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file1 = null;
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Open File");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    file1 = fileopen.getSelectedFile();
                }
                List<PatRectangle> list1 = new ArrayList<>();
                try {
                    String fileName=file1.getCanonicalPath();
                    if (fileName.substring(fileName.length()-3, fileName.length()).equals("xls")){
                        fileName=ExelParser.parser(fileName);
                    }
                    list1 = patParser.parser(fileName);
                    if (patViever.getListSize()==0) {
                        int maxX = 0;
                        int maxY = 0;
                        int minX = 10000000;
                        int minY = 10000000;
                        for (PatRectangle i : list1) {
                            if (maxX < (i.getX() + i.getW() / 2)) {
                                maxX = i.getX() + i.getW() / 2;
                            }
                            if (maxY < (i.getY() + i.getH() / 2)) {
                                maxY = i.getY() + i.getH() / 2;
                            }
                            if (minX > (i.getX() + i.getW() / 2)) {
                                minX = i.getX() + i.getW() / 2;
                            }
                            if (minY > (i.getY() + i.getH() / 2)) {
                                minY = i.getY() + i.getH() / 2;
                            }
                        }
                        patViever.setMaxX(maxX);
                        patViever.setMaxY(maxY);
                        patViever.setMinX(minX);
                        patViever.setMinY(minY);
                    }
                    patViever.newList(list1);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        buttonPanel.add(btnOpen);

        JButton btn1 = new JButton("Влево");
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patViever.incX();
            }
        });
        buttonPanel.add(btn1);

        JButton btn2 = new JButton("Вверх");
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patViever.incY();
            }
        });
        buttonPanel.add(btn2);

        JButton btn5 = new JButton("Приблизить");
        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patViever.incF();
            }
        });
        buttonPanel.add(btn5);

        JButton btnOpt = new JButton("Оптимальная позиция");
        btnOpt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patViever.OptimumPosition();
            }
        });
        buttonPanel.add(btnOpt);

        JButton btn6 = new JButton("Отдалить");
        btn6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patViever.decF();
            }
        });
        buttonPanel.add(btn6);

        JButton btn3 = new JButton("Вниз");
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patViever.decY();
            }
        });
        buttonPanel.add(btn3);

        JButton btn4 = new JButton("Вправо");
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patViever.decX();
            }
        });
        buttonPanel.add(btn4);

        JCheckBox checkBox1 = new JCheckBox("Инвертировать по оси X", true);
        checkBox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                patViever.setReverseX(checkBox1.isSelected());
            }
        });
        buttonPanel.add(checkBox1);

        JCheckBox checkBox2 = new JCheckBox("Инвертировать по оси Y", true);
        checkBox2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                patViever.setReverseY(checkBox2.isSelected());
            }
        });
        buttonPanel.add(checkBox2);

        JCheckBox checkBox3 = new JCheckBox("Закраска", false);
        checkBox3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                patViever.setFfill(checkBox3.isSelected());
            }
        });
        buttonPanel.add(checkBox3);

        JButton btnTmer = new JButton("Выводить по одному");
        btnTmer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnTmer.getText().equals("Выводить по одному")) {
                    btnTmer.setText("Вывести всё");
                    timer.start();
                } else {
                    btnTmer.setText("Выводить по одному");
                }
                patViever.oppositefOutput();
            }
        });
        buttonPanel.add(btnTmer);

        x = new JProgressBar(0);
        buttonPanel.add(x);

        JLabel label = new JLabel("Скорость 1 в секунду");

        JButton btnFast = new JButton("Ускорить");
        btnFast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("Скорость " + Float.toString(1000f / timerFast())+" в секунду");
            }
        });
        buttonPanel.add(btnFast);

        JButton btnslow = new JButton("Замедлить");
        btnslow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("Скорость " + Float.toString(1000f / timerSlow()) + " в секунду");
            }
        });
        buttonPanel.add(btnslow);

        buttonPanel.add(label);

        panel.add(buttonPanel, BorderLayout.PAGE_END);

        f.setContentPane(panel);
        f.setVisible(true);
    }

    static javax.swing.Timer timer = new javax.swing.Timer( timerSpeed, new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            x.setValue((int)(100f * (patViever.incSizeVisible()/patViever.getListSize())));
        }
    } );

    public static int timerFast(){
        int x3=timerSpeed;
        timerSpeed=(int)(timerSpeed*0.9);
        if ((timerSpeed==x3)&&(x3>1)){
            timerSpeed--;
        }
        timer.setDelay(timerSpeed);
        return timerSpeed;
    }

    public static int timerSlow(){
        int x3=timerSpeed;
        timerSpeed=(int)(timerSpeed*1.1);
        if (timerSpeed==x3){
            timerSpeed++;
        }
        timer.setDelay(timerSpeed);
        return timerSpeed;
    }
}