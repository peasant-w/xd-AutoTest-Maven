package inter;

public interface InterOfApp {
    /**
     * 强制等待
     * @param waitTime 等待时间
     */
    public void threadWait(String waitTime);

    /**
     * 隐式等待
     * @param xpath 元素表达式
     */
    public void implicityWait(String xpath);
}
