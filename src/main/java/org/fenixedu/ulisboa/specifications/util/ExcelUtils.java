package org.fenixedu.ulisboa.specifications.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	public static List<List<String>> readExcel(final InputStream stream, int maxCols) throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook(stream);

        XSSFSheet sheet = wb.getSheetAt(0);

        if (sheet == null) {
            throw new RuntimeException("error.ExcelUploadComponent.invalid.spreadsheet");
        }

        final List<List<String>> spreadsheetContent = new ArrayList<List<String>>();

        XSSFRow row;
        for(int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
        	row = sheet.getRow(i);
        	
        	final ArrayList<String> rowContent = new ArrayList<String>();
        	spreadsheetContent.add(rowContent);
        	
        	if(row == null) {
        		continue;
        	}

            XSSFCell cell;
            for(int j = 0; j < maxCols; j++) {
                cell = row.getCell(j);
                
                if(cell == null) {
                    rowContent.add("");
                    continue;
                }
                
                if (Cell.CELL_TYPE_NUMERIC == cell.getCellType() && DateUtil.isCellDateFormatted(cell)) {
                    rowContent.add(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(cell.getDateCellValue()));
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = cell.getStringCellValue();
                    rowContent.add(value);
                }
                
            }
        }

        return spreadsheetContent;
	}
}
