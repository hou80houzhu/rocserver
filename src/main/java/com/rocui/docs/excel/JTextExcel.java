package com.rocui.docs.excel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class JTextExcel {

    private HashMap<String, TextSheet> sheets = new HashMap<String, TextSheet>();

    public JTextExcel addSheet(String name, TextSheet sheet) {
        this.sheets.put(name, sheet);
        return this;
    }

    public void save(String fullname) throws Exception {
        HSSFWorkbook book = this.getBook();
        FileOutputStream fOut = new FileOutputStream(fullname);
        book.write(fOut);
        fOut.flush();
        fOut.close();

    }

    public void save(OutputStream stream) {
        HSSFWorkbook book = this.getBook();
        try {
            book.write(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HSSFWorkbook getBook() {
        HSSFWorkbook book = new HSSFWorkbook();
        int i = 0;
        for (Entry<String, TextSheet> x : this.sheets.entrySet()) {
            HSSFSheet sheet = book.createSheet();
            book.setSheetName(i, x.getKey());
            List<HashMap<String, String>> rows = x.getValue().getRows();
            for (int k = 0; k < rows.size(); k++) {
                HSSFRow row = sheet.createRow(k);
                HashMap<String, String> c = rows.get(k);
                int n = 0;
                for (Entry<String, String> d : c.entrySet()) {
                    HSSFCell cell = row.createCell((short) n);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(d.getValue().toString());
                    n++;
                }
            }
            i++;
        }
        return book;
    }

    public static void main(String[] args) {
        TextSheet sheet = new TextSheet();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("xx", "xxxx");
        map.put("xx2", "eeee");
        map.put("xx3", "ffff");
        sheet.addRow(map);
        JTextExcel excel = new JTextExcel();
        try {
            excel.addSheet("xxx", sheet).save("E:\\xx.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
