package top.sharehome.jvmloader;

/**
 * 数学类
 *
 * @author AntonyCheng
 */
public class Math {

    public static final int INIT_NUM = 1900;

    public static User user = new User();

    static {
        System.out.println("abcdefghij");
    }

    /**
     * 加法方法
     */
    public int computeAdditionAndMultiplyBy10(int a, int b) {
        int numA = a;
        int numB = b;
        int res = (numA + numB) * 10;
        return res;
    }

    /**
     * 主函数
     */
    public static void main(String[] args) {
        Math math = new Math();
        math.computeAdditionAndMultiplyBy10(10,10);
    }

}
