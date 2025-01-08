package top.sharehome.jvmtools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

/**
 * Arthas工具测试类
 *
 * @author AntonyCheng
 */
public class ArthasTest {

    private static final HashSet<String> HASH_SET = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("程序开始:" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        // 模拟 CPU 过高
        cpuHigh();
        // 模拟线程死锁
        deadThread();
        // 不断的向 hashSet 集合增加数据
        addHashSetThread();
    }

    /**
     * 不断的向 hashSet 集合添加数据
     */
    public static void addHashSetThread() {
        // 初始化常量
        new Thread(() -> {
            int count = 0;
            while (true) {
                try {
                    HASH_SET.add(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + "计数" + count);
                    Thread.sleep(30000);
                    count++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void cpuHigh() {
        new Thread(() -> {
            while (true) {

            }
        }).start();
    }

    /**
     * 死锁
     */
    private static void deadThread() {
        // 创建资源
        Object resourceA = new Object();
        Object resourceB = new Object();
        // 创建线程
        Thread threadA = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println(Thread.currentThread() + "获取A资源");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "等待获取B资源");
                synchronized (resourceB) {
                    System.out.println(Thread.currentThread() + "获取B资源");
                }
            }
        });

        Thread threadB = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println(Thread.currentThread() + "获取B资源");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "等待获取A资源");
                synchronized (resourceA) {
                    System.out.println(Thread.currentThread() + "获取A资源");
                }
            }
        });
        threadA.start();
        threadB.start();
    }

}
