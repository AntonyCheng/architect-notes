package top.sharehome.gcandthreecolorremark;

/**
 * 三色标记示例代码
 *
 * @author AntonyCheng
 */
public class ThreeColorRemark {

    public static void main(String[] args) {
        A a = new A();
        // 开始并发标记
        D d = a.b.d;  // 1、读操作
        a.b.d = null; // 2、写操作
        a.d = d;      // 3、写操作
    }

}

class A {
    B b = new B();
    D d = null;
}

class B {
    C c = new C();
    D d = new D();
}

class C {
}

class D {
}