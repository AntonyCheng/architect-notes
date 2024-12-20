package top.sharehome.jvmcommands;

/**
 * 进程死锁示例代码
 *
 * @author AntonyCheng
 */
public class DeadLockSample {

    private static Object lock1 = new Object();

    private static Object lock2 = new Object();

    public static void main(String[] args) {
        System.out.println("主线程开始");
        new Thread(() -> {
            synchronized (lock1) {
                try {
                    System.out.println("thread1开始");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                synchronized (lock2) {
                    System.out.println("thread1结束");
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (lock2) {
                try {
                    System.out.println("thread2开始");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                synchronized (lock1) {
                    System.out.println("thread2结束");
                }
            }
        }).start();
        System.out.println("主线程结束");
    }

}
