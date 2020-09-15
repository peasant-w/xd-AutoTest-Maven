package driver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ExcelReaderTest {
    ExcelReader reader = new ExcelReader("cases/WebCases.xlsx");

    @BeforeMethod
    public void setUp() {
        AutoLogger.log.info("测试开始");
    }

    @AfterMethod
    public void tearDown() {
        AutoLogger.log.info("测试结束");
    }

    @Test
    public void testSetSheet() {
        reader.setSheetByName("商城前台测试用例");
    }

    @Test
    public void testGetSheets() {
        int sheets = reader.getSheets();
        System.out.println(sheets);
    }

    @Test
    public void testCloseExcel() {
        reader.closeExcel();
    }
}