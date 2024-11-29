package top.sharehome.jvmnewobject;

/**
 * 引用计数法示意类
 *
 * @author AntonyCheng
 */
public class ReferenceCountingGc {

    Object instance = null;

    public static void main(String[] args) {
        ReferenceCountingGc objA = new ReferenceCountingGc();
        ReferenceCountingGc objB = new ReferenceCountingGc();
        objA.instance = objB;
        objB.instance = objA;
        objA = null;
        objB = null;
    }

}
