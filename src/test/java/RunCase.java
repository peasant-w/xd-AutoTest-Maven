import driver.ExcelReader;
import driver.ExcelWriter;
import keyword.WebKeyWord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RunCase {
    public static void main(String[] args) {
        //读取用例文件
        ExcelReader cases = new ExcelReader("cases/WebCases.xlsx");
        //设置时间格式
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String time = sdf.format(date);
        //拼接结果文件名
        ExcelWriter results = new ExcelWriter("cases/WebCases.xlsx", "product/result/result-" + time + ".xlsx");
        //实例化关键字
        WebKeyWord web = new WebKeyWord(results);
        int i = cases.getSheets();
//        System.out.println("sheet页个数"+i);
        //遍历sheet页
        for (int sheetNo = 0; sheetNo < cases.getSheets(); sheetNo++) {
            cases.setSheetByIndex(sheetNo);
            results.useSheetByIndex(sheetNo);
            //遍历每个sheet页中的每一行
            for (int rowNo = 0; rowNo < cases.getRows(); rowNo++) {
                web.setWriterLine(rowNo);
                List<String> rowContent = cases.readLine(rowNo);
//                System.out.println(rowContent);
                //
                if ((rowContent.get(0).equals("") || rowContent.get(0).trim().length() < 1) && (rowContent.get(1).equals("") || rowContent.get(1).trim().length() < 1)) {
                    switch (rowContent.get(3)) {
                        case "openBrowser":
                            web.openBrowser(rowContent.get(4));
                            break;
                        case "visitUrl":
                            web.visitUrl(rowContent.get(4));
                            break;
                        case "input":
                            web.inputByXpath(rowContent.get(4), rowContent.get(5));
                            break;
                        case "click":
                            web.clickByxpath(rowContent.get(4));
                            break;
                        case "intoIframe":
                            web.intoIframe(rowContent.get(4));
                            break;
                        case "closeBrowser":
                            web.closeBrowser();
                            break;
                        case "halt":
                            web.halt(rowContent.get(4));
                            break;
                        case "assertIsContains":
                            web.assertIsContains(rowContent.get(4),rowContent.get(5));
                            break;
                        case "select":
                            web.selectByValue(rowContent.get(4),rowContent.get(5));
                            break;
                        case "alertAccept":
                            web.alertAccept();
                            break;
                    }
                }
            }
        }
        cases.closeExcel();
        results.save();
    }
}

