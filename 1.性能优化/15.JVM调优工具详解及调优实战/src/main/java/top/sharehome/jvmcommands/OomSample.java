package top.sharehome.jvmcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 内存溢出示例代码
 * 运行命令参数：-Xms5M -Xmx5M -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\jvm.dump
 *
 * @author AntonyCheng
 */
public class OomSample {

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (true) {
            list.add(new User(i++, UUID.randomUUID().toString()));
            new User(j--, UUID.randomUUID().toString());
        }
    }

}
