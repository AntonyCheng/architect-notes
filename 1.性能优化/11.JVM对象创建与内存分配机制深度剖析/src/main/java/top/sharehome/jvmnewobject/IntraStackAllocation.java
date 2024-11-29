package top.sharehome.jvmnewobject;

/**
 * 栈内分配（标量引用）
 * 该方法就是调用1亿次alloc()方法，如果是分配到堆上需要大概1GB的内存，如果Eden区小于该值必然会发生GC
 * 注意：-XX:+PrintGC参数就是打印GC日志。
 * 使用如下参数就不会发生GC：
 * -Xmx15m -Xms15m -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:+EliminateAllocations
 * 使用如下参数就会发生大量GC：
 * -Xmx15m -Xms15m -XX:-DoEscapeAnalysis -XX:+PrintGC -XX:+EliminateAllocations
 * -Xmx15m -Xms15m -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:-EliminateAllocations
 *
 * @author AntonyCheng
 */
public class IntraStackAllocation {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            alloc();
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }

    private static void alloc() {
        User user = new User();
        user.setId(1);
        user.setName("AntonyCheng");
    }

}
