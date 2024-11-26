package top.sharehome.jvmmemorymodel;

import java.util.ArrayList;

/**
 * 测试堆内存模型和GC过程
 * 需要搭配Java Visual VM工具
 *
 * @author AntonyCheng
 */
public class TestHeapGc {

    public static void main(String[] args) throws InterruptedException {

        ArrayList<User> list = new ArrayList<>();

        while (true) {
            for (int i = 0; i < 100000; i++) {
                list.add(new User());
            }
            Thread.sleep(100);
        }
    }

}
