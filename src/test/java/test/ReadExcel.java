package test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ReadExcel {

	public static void main(String[] args) {

		ArrayList<String> columnList = new ArrayList<String>();
		File file = new File("test.xls");

		try {
			FileInputStream in = new FileInputStream(file);

			HSSFWorkbook wb = new HSSFWorkbook(in);
			
			Sheet sheet = wb.getSheetAt(0);
			int firstRowNum = sheet.getFirstRowNum();
			int lastRowNum = sheet.getLastRowNum();

			Row row = null;
			Cell cell_a = null;
			Cell cell_b = null;
			for (int i = firstRowNum; i <= lastRowNum; i++) {
				row = sheet.getRow(i); // 取得第i行
				cell_a = row.getCell(0); // 取得i行的第一列
				String cellValue = cell_a.getStringCellValue().trim();

				System.out.println(cellValue);
				columnList.add(cellValue);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
