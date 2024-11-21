package top.sharehome.jvmloader;

/**
 * 动态加载类
 *
 * @author AntonyCheng
 */
public class DynamicLoad {
    static {
        System.out.println("静态代码块");
    }

    public static void main(String[] args) {
        A a = new A();
        System.out.println("主类方法");
        B b = null;
    }
}

class A {
    static {
        System.out.println("A类的静态代码块");
    }

    public A() {
        System.out.println("初始化A类");
    }
}

class B {
    static {
        System.out.println("B类的静态代码块");
    }

    public B() {
        System.out.println("初始化B类");
    }
}