package keyword;

import driver.AutoLogger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestPoi {
    public static void main(String[] args) {
        try {
            Workbook workbook = new XSSFWorkbook(new File("cases/WebCases.xlsx"));
            //获取第一个sheet页
            Sheet sheet = workbook.getSheetAt(0);
            List<List<String>> sheetContent = new ArrayList<>();
            for (int rowNo = 0; rowNo < sheet.getPhysicalNumberOfRows(); rowNo++) {
                //获取指定行
                Row row = sheet.getRow(rowNo);
                //存储每一行的信息
                List<String> rowContent = new ArrayList<>();
                for (int cellNo = 0; cellNo < row.getPhysicalNumberOfCells(); cellNo++) {
                    //获取指定单元格信息
                    Cell cell = row.getCell(cellNo);
                    //存储每个单元格信息
                    String value = "";
                    //判断单元格存储类型，如果是数字，则转成字符串存储
                    if (cell.getCellType().equals(CellType.NUMERIC)) {
                        value = cell.getNumericCellValue() + "";
                    } else {
                        value = cell.getStringCellValue();
                    }
                    //把每个单元格信息添加到list
                    rowContent.add(value);
                }
//                AutoLogger.log.info("第" + (rowNo + 1) + "行的内容是：" + rowContent);
                String content  =  rowContent.get(4);
                System.out.println(content);
//                System.out.println("第" + (rowNo + 1) + "行的内容是：" + rowContent);
                sheetContent.add(rowContent);
            }
            AutoLogger.log.info("整个sheet页的内容是：" + sheetContent);
            //关闭资源
            workbook.close();
//            System.out.println("整个sheet页的内容是：" + sheetContent);
        } catch (Exception e) {
            AutoLogger.log.info(e,e.fillInStackTrace());
        }
    }
}
