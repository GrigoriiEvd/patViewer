package com.egs.patViewer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by EGS on 05.10.2015.
 */
//C:\Users\EGS\Desktop\Tablitsa_GRI-1.xls
public class ExelParser {

    public static List<PatRectangle> parser(String name) throws IOException {
        List<PatRectangle> list = new ArrayList<>();
        InputStream in = new FileInputStream(name);
        HSSFWorkbook wb = new HSSFWorkbook(in);

        Sheet sheet = wb.getSheetAt(0);

        Iterator<Row> it = sheet.iterator();
        Iterator<Row> it1 = sheet.iterator();
        int fl = 0;
        while (it.hasNext()) {
            if (fl == 3) {
                break;
            }
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            Cell cell = cells.next();
            int cellType = cell.getCellType();
            if (fl==0) {
                switch (cellType){
                    case Cell.CELL_TYPE_STRING:
                        if ((cell.getStringCellValue().equals("Файл задания"))) {
                            fl = 1;
                        }
                        break;
                    default:
                        break;
                }
            }else{
                Iterator<Cell> cells1 = cells;
                cells1.next();
                cells1.next();
                cells1.next();
                cells1.next();
                cells1.next();
                cell = cells1.next();
                if ((cell.getCellType() == Cell.CELL_TYPE_FORMULA)||(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)){
                    break;
                }
            }
            it1.next();
            System.out.println();
        }
        it=it1;
   /*     while (it.hasNext()) {
            if (fl == 3) {
                break;
            }
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            Cell cell = cells.next();
            int cellType = cell.getCellType();
            switch (cellType) {
                case Cell.CELL_TYPE_STRING:
                    if ((cell.getStringCellValue().equals("Файл задания"))) {
                        fl = 1;
                    }
                    if (fl == 2) {
                        fl = 1;
                    }
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (fl == 2) {
                        fl = 1;
                    }
                    break;
                case 3:
                    if (fl == 2) {
                        fl = 3;
                    }
                    if (fl == 1) {
                        fl = 2;
                    }
                    break;
                default:
                    System.out.print("|");
                    if (fl == 2) {
                        fl = 1;
                    }
                    break;
            }
            System.out.println();
        }*/
        List<FileMission> fileMissions = new ArrayList<>();
        while (true) {
            try {
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                Cell cell = cells.next();
                FileMission x = new FileMission();
                x.setName(cell.getStringCellValue());
                cell = cells.next();
                cell = cells.next();
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    x.setnX((int) Float.parseFloat(cell.toString()));
                }
                cell = cells.next();
                try {
                    x.setSizeModulX((int) cell.getNumericCellValue());
                } catch (Exception e) {
                    x.setSizeModulX(0);
                }
                cell = cells.next();
                cell = cells.next();
                if (cell.getCellType() == cell.CELL_TYPE_FORMULA) {
                    try {
                        x.setProxod((int) cell.getNumericCellValue());
                    } catch (Exception e) {
                        x.setProxod(0);
                    }
                } else {
                    x.setProxod(1);
                }
                cell = cells.next();
                if (cell.getCellType() == cell.CELL_TYPE_FORMULA) {
                    x.setPFO(cell.getCachedFormulaResultType());
                }else{
                    x.setPFO((int) cell.getNumericCellValue());
                }
                if (x.getProxod() > 1) {
                    x.setnX(fileMissions.get(fileMissions.size() - 1).getnX());
                    x.setnY(fileMissions.get(fileMissions.size() - 1).getnY());
                    x.setSizeModulX(fileMissions.get(fileMissions.size() - 1).getSizeModulX());
                    x.setSizeModulY(fileMissions.get(fileMissions.size() - 1).getSizeModulY());
                }
                cell = cells.next();

                x.setE((int) cell.getNumericCellValue());
                cell = cells.next();
                x.setPrivWindowX((int) cell.getNumericCellValue());
                cell = cells.next();
                x.setSizeWindowX((int) cell.getNumericCellValue());
                cell = cells.next();
                x.setPFOKomplexX((int) cell.getNumericCellValue());
                cell = cells.next();
                x.setOffsetNaFSX((int) cell.getNumericCellValue());
                cell = cells.next();
                cell = cells.next();
                cell = cells.next();
                x.setResultOffsetX((int) cell.getNumericCellValue());
                row = it.next();
                cells = row.iterator();
                cell = cells.next();
                cell = cells.next();
                cell = cells.next();
                if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
                    x.setnY((int) Float.parseFloat(cell.toString()));
                }
                cell = cells.next();
                if (x.getSizeModulY() == 0) {
                    try {
                        x.setSizeModulY((int) cell.getNumericCellValue());
                    } catch (Exception e) {
                        x.setSizeModulY(0);
                    }
                }
                cell = cells.next();
                cell = cells.next();
                cell = cells.next();
                cell = cells.next();
                cell = cells.next();
                x.setPrivWindowY((int) cell.getNumericCellValue());
                cell = cells.next();
                x.setSizeWindowY((int) cell.getNumericCellValue());
                cell = cells.next();
                x.setPFOKomplexY((int) cell.getNumericCellValue());
                cell = cells.next();
                x.setOffsetNaFSY((int) cell.getNumericCellValue());
                cell = cells.next();
                cell = cells.next();
                cell = cells.next();
                x.setResultOffsetY((int) cell.getNumericCellValue());
                fileMissions.add(x);
                it.next();
                if (x.getE() == 0) {
                    fileMissions.remove(fileMissions.size() - 1);
                    break;
                }
            } catch (Exception e1) {
                break;
            }
        }

        int plusX = 51000;
        int plusY = 51000;
        int maxmin = 0;

        for (FileMission i : fileMissions) {
            int x4 = (int) (0.5 * (i.getnX() - 1) * i.getSizeModulX()) - i.getOffsetNaFSX() - (i.getnX() * i.getSizeModulX());
            int y4 = (int) (0.5 * (i.getnY() - 1) * i.getSizeModulY()) + i.getOffsetNaFSY() - (i.getnY() * i.getSizeModulX());
            if ((x4 < 0) && (Math.abs(x4) > plusX)) {
                maxmin = Math.abs(x4);
            }
            if ((y4 < 0) && (Math.abs(y4) > plusY)) {
                maxmin = Math.abs(y4);
            }
        }
        if (maxmin > plusX) {
            //String input = JOptionPane.showInputDialog("картинка уходит в < -"+Integer.toString(plusX/1000)+", установите нужный отступ");
            //plusX = Integer.parseInt(input)*1000;
            plusX = maxmin+1000;
            plusY = plusX;
        }

        for (FileMission i : fileMissions) {
            int centerX = (int) (0.5 * (i.getnX() - 1) * i.getSizeModulX()) - i.getOffsetNaFSX() + plusX;
            int centerY = (int) (0.5 * (i.getnY() - 1) * i.getSizeModulY()) + i.getOffsetNaFSY() + plusY;
            for (int j = 0; j < i.getnX(); j++) {
                for (int k = 0; k < i.getnY(); k++) {
                    int x = centerX - (j * i.getSizeModulX());
                    int y = centerY - (k * i.getSizeModulY());
                    int w = (i.getSizeWindowX() - 4) * 100;
                    int h = (i.getSizeWindowY() - 4) * 100;
                    list.add(new PatRectangle(x, y, h, w, 0));
                }
            }
        }
        return list;
    }

}
