package tse;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.eclipse.collections.impl.list.Interval;

public class LinkedListGenerator {
	
	/**
	 * This method guarantees that garbage collection is done unlike
	 * <code>{@link System#gc()}</code>
	 */
	public static void gc() {
		Object obj = new Object();
		@SuppressWarnings("rawtypes")
		WeakReference ref = new WeakReference<Object>(obj);
		obj = null;
		while (ref.get() != null) {
			System.gc();
		}
	}


	
	/**
	 * 
	 * @param k
	 * @return a non contiguous linked list of random integers
	 */
	public static LinkedList<Integer> makeNCLinkedList(int k) {
		int s;
		int length = (int) Math.pow(2, k) / 4;
		LinkedList<Integer> result = new LinkedList<Integer>();
		for (int i = 0; i < length; i++) {
			if (i % 1_000_000 == 0) {
		         gc();
		    }
			s = (int) Math.random() * 100;
			result.addFirst(s);
		}
		return result;
	}
	
	/**
	 * 
	 * @param workbook
	 * @param sheetName
	 * @return a sheet for every reading method
	 */
	public static Sheet getSheet(Workbook workbook, String sheetName) {
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 4000);

		Row header = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 16);
		font.setBold(true);
		headerStyle.setFont(font);

		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("time");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(1);
		headerCell.setCellValue("k");
		headerCell.setCellStyle(headerStyle);
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

		return sheet;
	}

	/**
	 * This method saves into an excel file all the 'k's and the estimated time for
	 * each reading method
	 * 
	 * @param workbook
	 * @param sheetNames
	 */
	public static void saveIntoExcel(Workbook workbook, String sheetName) {
		int sum = 0;
		int len = 29 - 20 + 1;


			Sheet sheet = getSheet(workbook, sheetName);
			Row row = sheet.createRow(0);
			Row row1 = sheet.createRow(1);
				System.out.println(" ---- START TO END ----");
				for (int i = 0; i < len; i++) {
					LinkedList<Integer> contLinkedList = makeNCLinkedList(i + 20);
					long startTime = System.nanoTime();
					for (Integer val : contLinkedList) {
						sum = sum + val;
					}
					long estimatedTime = System.nanoTime() - startTime;
					System.out.println("the estimated time: " + estimatedTime);
					Cell cell = row.createCell(i);
					cell.setCellValue(i);

					cell = row1.createCell(i);
					cell.setCellValue(estimatedTime);
				}


	}

	public static void main(String[] args) {

		Workbook workbook = new XSSFWorkbook();
		String sheetName = "START_END";
		saveIntoExcel(workbook, sheetName);

		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		System.out.println(path);
		String fileLocation = path.substring(0, path.length() - 1) + "hppLL.xlsx";

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(fileLocation);
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
