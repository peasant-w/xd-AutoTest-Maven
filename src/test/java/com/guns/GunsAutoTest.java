package com.guns;

import keyword.KeyWordOfWeb;

public class GunsAutoTest {
    public static void main(String[] args){
        KeyWordOfWeb TG = new KeyWordOfWeb();
        TG.openBrowser("chorme");
        TG.visitUrl("https://wenku.baidu.com/zuowen/search/composition?keywords=%E6%88%91%E6%88%90%E5%8A%9F%E5%95%A6&fr_which_ala=gl_pc_zw");
        TG.implicitlyWait("3");
        TG.click("//span[text()='100字']");
        TG.halt("5000");
        TG.closeBrowser();
    }
}
