package top.sharehome.jvmnewobject;

import org.openjdk.jol.info.ClassLayout;

/**
 * 使用jol-core计算对象大小
 *
 * @author AntonyCheng
 */
public class JolSample {

    public static void main(String[] args) {
        ClassLayout classLayout1 = ClassLayout.parseClass(Object.class);
        System.out.println(classLayout1.toPrintable());
        System.out.println();
        ClassLayout classLayout2 = ClassLayout.parseInstance(new int[]{});
        System.out.println(classLayout2.toPrintable());
        System.out.println();
        ClassLayout classLayout3 = ClassLayout.parseInstance(new User());
        System.out.println(classLayout3.toPrintable());
    }

    public static class User {
        private int id;
        private String name;
        private byte gender;
        private Object other;
    }

}
