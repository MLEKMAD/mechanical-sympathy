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

public class ArrayGenerator {

	
	/**
	 * 
	 * @param k
	 * @return an array of random integers
	 */
	public static Integer[] makeArray(int k) {
		int s;
		int length = (int) Math.pow(2, k) / 4;
		Integer[] result = new Integer[length];
		for (int i = 0; i < length; i++) {
			s = (int) Math.random() * 100;
			result[i] = s;
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
	public static void saveIntoExcel(Workbook workbook, String[] sheetNames) {
		int sum = 0;
		int len = 29 - 20 + 1;

		for (int s = 0; s < sheetNames.length; s++) {

			Sheet sheet = getSheet(workbook, sheetNames[s]);
			Row row = sheet.createRow(0);
			Row row1 = sheet.createRow(1);
			switch (s) {
			case 0:
				System.out.println(" ---- START TO END ----");
				for (int i = 0; i < len; i++) {
					Integer[] array = makeArray(i + 20);
					long startTime = System.nanoTime();
					for (int j = 0; j < array.length; j++) {
						sum = sum + array[i];
					}
					long estimatedTime = System.nanoTime() - startTime;
					System.out.println("the estimated time: " + estimatedTime);
					Cell cell = row.createCell(i);
					cell.setCellValue(i);

					cell = row1.createCell(i);
					cell.setCellValue(estimatedTime);
				}
				break;
			case 1:
				System.out.println(" ---- END TO START ----");
				for (int i = 0; i < len; i++) {
					Integer[] array = (Integer[]) makeArray(31 - i);
					long startTime = System.nanoTime();
					for (int j = array.length - 1; j >= 0; j--) {
						sum = sum + array[j];
					}
					long estimatedTime = System.nanoTime() - startTime;
					System.out.println("the estimated time : " + estimatedTime);
					Cell cell = row.createCell(i);
					cell.setCellValue(i);

					cell = row1.createCell(i);
					cell.setCellValue(estimatedTime);
				}
				break;
			case 2:
				System.out.println(" ----RANDOMLY----  ");
				// read randomly the array
				for (int i = 0; i < len; i++) {
					Integer[] array = makeArray(i + 20);
					Interval indexesInterval = Interval.oneTo(array.length);
					List<Integer> indexesList = indexesInterval.toList();
					Collections.shuffle(indexesList);
					Integer[] indexes = new Integer[array.length]; 
					indexesList.toArray(indexes);
//					indexesList.forEach(System.out::print); // checks shuffled indexes
					long startTime = System.nanoTime();
					for (int j = 0; j < indexes.length; j++) {
						sum = sum + array[indexes[i]-1];
					}
					long estimatedTime = System.nanoTime() - startTime;
					System.out.println("the estimated time : " + estimatedTime);
					Cell cell = row.createCell(i);
					cell.setCellValue(i);

					cell = row1.createCell(i);
					cell.setCellValue(estimatedTime);

				}
				break;
			}

		}

	}

	public static void main(String[] args) {

		Workbook workbook = new XSSFWorkbook();
		String[] sheetNames = { "START_END", "END_START", "RANDOM" };
		saveIntoExcel(workbook, sheetNames);

		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		System.out.println(path);
		String fileLocation = path.substring(0, path.length() - 1) + "hpp.xlsx";

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(fileLocation);
			workbook.write(outputStream);
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
