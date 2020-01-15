package inter;

/**
 * APP关键字接口类
 *
 * @author wangwei
 * @date 2020-01-15
 */
public interface KeyWordInter {
    /**
     * 强制等待
     *
     * @param waitTime 等待时间
     */
    void threadWait(String waitTime);

    /**
     * 隐式等待
     *
     * @param xpath 元素表达式
     */
    void implicityWait(String xpath);

    /**
     * 显式等待
     *
     * @param xpath 元素表达式
     */
    void explicityWait(String xpath);

    /**
     * 执行cmd命令
     *
     * @param command cmd命令表达式
     */
    void runCmd(String command);

    /**
     * 启动appium服务
     *
     * @param serviceIp appium服务地址
     * @param port      端口
     * @param time      等待时间
     */
    void startAppium(String serviceIp, String port, String time);

    /**
     * 关闭appium服务，使用cmd命令结束node进程的方法
     */
    void closeAppium();

    /**
     * 启动APP，传递设备标识、设备名称、设备版本、APP包名、APP启动入口、appium服务地址
     *
     * @param deviceName      设备标识
     * @param platfromName    设备名称 如：安卓、iOS
     * @param platfromVersion 设备系统版本
     * @param appPackage      APP的包名，如：com.wanjia.lend
     * @param appActivity     APP的启动入口，Activity
     * @param appiumServiceIp appium服务地址
     */
    void startApp(String deviceName, String platfromName, String platfromVersion, String appPackage, String appActivity, String appiumServiceIp);

    /**
     * 退出APP
     */
    void closeApp();

    /**
     * 点击元素
     *
     * @param xpath 元素表达式
     */
    void click(String xpath);

    /**
     * 文本框输入
     *
     * @param xpath 元素表达式
     * @param text  文本内容
     */
    void input(String xpath, String text);

    /**
     * 切换context
     *
     * @param contextName context名称
     */
    void switchContext(String contextName);

    /**
     * 截图并保存
     *
     * @param name 截图定义名称
     */
    void saveScrShot(String name);

    /**
     * 使用adb方式滑动屏幕
     *
     * @param startX 起点x坐标
     * @param startY 起点y坐标
     * @param endX   终点x坐标
     * @param endY   终点y坐标
     */
    void adbSwiep(String startX, String startY, String endX, String endY);

    /**
     * adb方式点击坐标，传递x,y轴坐标
     *
     * @param xAxis x轴坐标
     * @param yAxis y轴坐标
     */
    void adbTap(String xAxis, String yAxis);

    /**
     * adb模拟键盘按键操作
     *
     * @param keycode 按键事件编号（百度"adb模拟按键事件keycode"）
     */
    void adbkeycode(String keycode);

    /**
     * 判断元素是否存在
     *
     * @param xpath 元素表达式
     * @param text  预期内容
     */
    void assertSame(String xpath, String text);
}
