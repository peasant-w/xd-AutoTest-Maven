package driver;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    //用于存储xlsx格式的工作表
    private XSSFWorkbook xssWorkbook;
    //用于存储xls格式的工作表
    private HSSFWorkbook hssWorkbook;
    //工作表的sheet页
    private Sheet sheet;
    //记录工作表的最大行数
    private int rows = 0;
    //记录当前Excel的sheet页的个数
    private int sheets = 0;
    //记录sheet页的名字
    private String sheetName;
    //记录指定行的内容
    private List<String> lineContent;
    //记录指定列的内容
    private List<String> columnContent;

    /**
     * 构造函数，通过文件流读取Excel文件的内容到workbook中
     *
     * @param filePath 文件路径
     */
    public ExcelReader(String filePath) {
        //通过文件名的最后一个“.”截取文件名称后缀xlsx/xls
        String fileType = filePath.substring(filePath.lastIndexOf("."));
        FileInputStream input = null;
        File file = new File(filePath);
        try {
            //通过文件输入流打开文件
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            AutoLogger.log.info("文件读取失败");
            AutoLogger.log.info(e, e.fillInStackTrace());
            return;
        }
        //根据文件后缀名，创建不同的workbook工作簿，为兼容03版本和07版本的工作簿
        try {
            if (fileType.equals(".xlsx")) {
                xssWorkbook = new XSSFWorkbook(input);
                sheet = xssWorkbook.getSheetAt(0);
                rows = sheet.getPhysicalNumberOfRows();
            } else if (fileType.equals(".xls")) {
                hssWorkbook = new HSSFWorkbook(input);
                sheet = hssWorkbook.getSheetAt(0);
                rows = sheet.getPhysicalNumberOfRows();
            }
        } catch (Exception e) {
            AutoLogger.log.info("创建工作簿失败，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
        if (sheet == null) {
            AutoLogger.log.info("文件打开失败");
        }
        //关闭文件输入流，释放资源
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过sheet页的名字设置读取的sheet页
     *
     * @param sheetName sheet页的名字
     */
    public void setSheetByName(String sheetName) {
        AutoLogger.log.info("设置sheet页");
        if (sheet != null) {
            if (xssWorkbook != null) {
                sheet = xssWorkbook.getSheet(sheetName);
            } else {
                sheet = hssWorkbook.getSheet(sheetName);
            }
        }
    }

    /**
     * 通过sheet页的序号设置sheet页
     *
     * @param index sheet页序号（0,1,2,3....）
     */
    public void setSheetByIndex(int index) {
        AutoLogger.log.info("设置sheet页" + index);
        String text = "";
        text = getSheetName(index);
        if (sheet != null) {
            try {
                if (xssWorkbook != null) {
                    sheet = xssWorkbook.getSheetAt(index);
                } else {
                    sheet = hssWorkbook.getSheetAt(index);
                }
            } catch (Exception e) {
                AutoLogger.log.info("sheet页" + text + "不存在，请检查");
                AutoLogger.log.info(e, e.fillInStackTrace());
            }
            rows = sheet.getPhysicalNumberOfRows();
        }
    }

    /**
     * 获取指定sheet页的名字
     *
     * @param index sheet页的序号（0,1,2,3......）
     */
    public String getSheetName(int index) {
        if (xssWorkbook != null) {
            sheetName = xssWorkbook.getSheetName(index);
        } else {
            sheetName = hssWorkbook.getSheetName(index);
        }
        return this.sheetName;
    }

    /**
     * 获取当前Excel下的sheet页个数
     *
     * @return 返回sheet总个数
     */
    public int getSheets() {
        if (xssWorkbook != null) {
            sheets = xssWorkbook.getNumberOfSheets();
        } else {
            sheets = hssWorkbook.getNumberOfSheets();
        }
        return this.sheets;
    }

    /**
     * 获取当前页最大行数
     *
     * @return 返回总行数
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * 读取指定行内容
     *
     * @param rowNo 指定行
     * @return 返回行内容
     */
    public List<String> readLine(int rowNo) {
        lineContent = new ArrayList<>();
        Row row = sheet.getRow(rowNo);
        for (int cell = 0; cell < row.getPhysicalNumberOfCells(); cell++) {
            lineContent.add(getCellValue(row.getCell(cell)));
        }
        return lineContent;
    }

    /**
     * 读取指定列内容
     *
     * @param column 指定列
     * @return 返回列内容
     */
    public List<String> readColumn(int column) {
        columnContent = new ArrayList<>();
        for (int col = 0; col < rows; col++) {
            Row row = sheet.getRow(col);
            columnContent.add(getCellValue(row.getCell(column)));
        }
        return columnContent;
    }

    /**
     * @param rowNo 指定行
     * @param colNo 指定列
     * @return 返回指定单元格内容
     */
    public String readCell(int rowNo, int colNo) {
        String cellContent = "";
        Row row = sheet.getRow(rowNo);
        cellContent = getCellValue(row.getCell(colNo));
        return cellContent;
    }

    /**
     * 针对单元格内容不同格式进行读取
     *
     * @param cell 指定单元格
     * @return 单元格内容
     */
    @SuppressWarnings("deprecation")
    private String getCellValue(Cell cell) {
        String cellValue = "";
        //如果单元格对象为null，则可能是xls文件转xlsx文件格式问题导致读取空单元格时，读到null
        if (cell == null)
            return cellValue;
        //基于不同格式，读取单元格内容并处理。
        try {
            //获取单元格类型。
            CellType cellType = cell.getCellType();
            // 将所有格式转为字符串读取到cellValue
            switch (cellType) {
                case STRING: // 文本
                    cellValue = cell.getStringCellValue();
                    break;
                case NUMERIC: // 数字、日期
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //日期型以年-月-日格式存储
                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                        cellValue = fmt.format(cell.getDateCellValue());
                    } else {
                        //数字保留两位小数
                        Double d = cell.getNumericCellValue();
                        DecimalFormat df = new DecimalFormat("#.##");
                        cellValue = df.format(d);
                    }
                    break;
                case BOOLEAN: // 布尔型
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                case BLANK: // 空白
                    cellValue = cell.getStringCellValue();
                    break;
                case ERROR: // 错误
                    cellValue = "错误";
                    break;
                case FORMULA: // 公式
                    FormulaEvaluator eval;
                    if (hssWorkbook != null)
                        eval = hssWorkbook.getCreationHelper().createFormulaEvaluator();
                    else
                        eval = xssWorkbook.getCreationHelper().createFormulaEvaluator();
                    cellValue = getCellValue(eval.evaluateInCell(cell));
                    break;
                case _NONE:
                    cellValue = "";
                default:
                    cellValue = "错误";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cellValue;
    }

    /**
     * 读取完成，关闭Excel
     */
    public void closeExcel() {
        try {
            if (xssWorkbook != null) {
                xssWorkbook.close();
            } else {
                hssWorkbook.close();
            }
            AutoLogger.log.info("Excel关闭");
        } catch (IOException e) {
            AutoLogger.log.info("Excel关闭异常，请检查");
            AutoLogger.log.info(e, e.fillInStackTrace());
        }
    }
}
