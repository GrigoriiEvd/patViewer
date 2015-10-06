package com.egs.patViewer;

import org.apache.poi.hssf.extractor.ExcelExtractor;
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
    public static String parser(String name) throws IOException {
        InputStream in = new FileInputStream(name);
        HSSFWorkbook wb = new HSSFWorkbook(in);

        Sheet sheet = wb.getSheetAt(0);

        Iterator<Row> it = sheet.iterator();
        int fl = 0;
        while (it.hasNext()) {
            if (fl == 3) {
                break;
            }
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            Cell cell = cells.next();
            int cellType = cell.getCellType();
            switch (cellType) {
                case Cell.CELL_TYPE_STRING:
                    if ((cell.toString().equals("Файл задания"))) {
                        fl = 1;
                    }
                    if (fl == 2) {
                        fl = 1;
                    }
                    //       System.out.print(cell.getStringCellValue() + "=");
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    //     System.out.print("[" + cell.getNumericCellValue() + "]");
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
        }
        List<FileMission> fileMissions = new ArrayList<>();
        while (true) {
            try {
                fileMissions.add(new FileMission());
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                Cell cell = cells.next();
                FileMission x = fileMissions.get(fileMissions.size() - 1);
                x.setName(cell.toString());
                cell = cells.next();
                cell = cells.next();
                if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
                    x.setnX((int) Float.parseFloat(cell.toString()));
                }
                cell = cells.next();
                if (cell.getCellType() == cell.CELL_TYPE_FORMULA) {
                    x.setSizeModulX(cell.getCachedFormulaResultType());
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
                    x.setPFO(cell.getCachedFormulaResultType());
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
                if (cell.getCellType() == cell.CELL_TYPE_FORMULA) {
                    x.setSizeModulY(cell.getCachedFormulaResultType());
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
                it.next();
                if (x.getE()==0){
                    fileMissions.remove(fileMissions.size()-1);
                    break;
                }
            }catch (Exception e1){
                break;
            }
        }
        if (fileMissions.get(fileMissions.size()-1).getE()==0){
            fileMissions.remove(fileMissions.size()-1);
        }
        return name.substring(0, name.length() - 3) + "pat";
    }

 /*   public int readCell(Cell cell){
        int x4=0;
        if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
            x4=((int) cell.getNumericCellValue());
        } else {
            x4=((int) cell.getNumericCellValue());
        }
        return x4;
    }*/
}
